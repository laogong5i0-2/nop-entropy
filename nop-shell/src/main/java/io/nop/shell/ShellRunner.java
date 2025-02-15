/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.shell;

import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.time.CoreMetrics;
import io.nop.commons.concurrent.executor.GlobalExecutors;
import io.nop.commons.env.PlatformEnv;
import io.nop.commons.util.IoHelper;
import io.nop.commons.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.nop.shell.ShellErrors.*;

/**
 * refactor Shell.java from hadoop-commons project
 */
public class ShellRunner implements IShellRunner {

    /**
     * Windows CreateProcess synchronization object
     */
    public static final Object WindowsProcessLaunchLock = new Object();

    static final Logger LOG = LoggerFactory.getLogger(ShellRunner.class);

    public static String exeCmd(String command) {
        ShellCommand cmd = new ShellCommand(command).redirectErrorStream(true);
        ShellRunner runner = new ShellRunner();
        DefaultShellOutputCollector collector = new DefaultShellOutputCollector();
        runner.run(cmd, collector);
        return collector.getOutput();
    }

    public int run(final ShellCommand command, IShellOutputCollector collector) {
        if (collector == null)
            collector = LogShellOutputCollector.INSTANCE;

        try {
            long beginTime = CoreMetrics.nanoTime();

            final Process process = this.newProcess(command);

            AtomicBoolean stopped = new AtomicBoolean();

            final Future<?> timerFuture = scheduleTimeout(command, () -> {
                safeDestroy(command, process, stopped);
                return null;
            });

            final Future<?> errorFuture = collectError(command, process, stopped, collector);

            try {
                if (command.getInputBytes() != null) {
                    IoHelper.write(process.getOutputStream(), command.getInputBytes(), null);
                }

                readOutput(process, command, collector, stopped);

                // stopped表示已经超时，在超时处理器中会关闭进程
                if (!stopped.get()) {
                    if (command.getTimeout() > 0) {
                        long endTime = beginTime + TimeUnit.MILLISECONDS.toNanos(command.getTimeout());
                        long diff = endTime - CoreMetrics.nanoTime();
                        if (diff < 0)
                            diff = 0;
                        boolean b = process.waitFor(diff, TimeUnit.NANOSECONDS);
                        if (b)
                            return process.exitValue();

                        stopped.set(true);
                    } else {
                        return process.waitFor();
                    }
                }
            } finally {
                if (timerFuture != null)
                    timerFuture.cancel(false);

                if (errorFuture != null)
                    errorFuture.cancel(false);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw NopException.adapt(e);
        } catch (Exception e) {
            throw new NopException(ERR_SHELL_EXEC_COMMAND_FAIL, e).param(ARG_COMMAND, command.getCommandString());
        }

        throw new NopException(ERR_SHELL_EXEC_COMMAND_TIMEOUT).param(ARG_TIMEOUT, command.getTimeout())
                .param(ARG_COMMAND, command.getCommandString());
    }

    Future<?> collectError(ShellCommand command, Process process, AtomicBoolean stopped,
                           IShellOutputCollector collector) {
        if (!command.isRedirectErrorStream()) {
            return GlobalExecutors.cachedThreadPool().submit(() -> {
                final BufferedReader errReader = this.createReader(process.getErrorStream(), command.getEncoding());
                try {
                    String line = errReader.readLine();
                    while ((line != null) && !stopped.get()) {
                        collector.onError(line);
                        line = errReader.readLine();
                    }
                } catch (IOException ioe) {
                    if (!stopped.get())
                        LOG.warn("nop.warn.shell.read-error-stream-fail:command={}", command.getCommandString(), ioe);
                }
                return null;
            });
        } else {
            return null;
        }
    }

    Process newProcess(ShellCommand command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command.getCmds());

        if (!command.isInheritEnv())
            builder.environment().clear();

        if (command.getEnvs() != null) {
            builder.environment().putAll(command.getEnvs());
        }

        if (command.getRemoveEnvs() != null) {
            builder.environment().keySet().removeAll(command.getRemoveEnvs());
        }

        if (command.getWorkDir() != null) {
            builder.directory(new File(command.getWorkDir()));
        }

        builder.redirectErrorStream(command.isRedirectErrorStream());
        return createProcess(command, builder);
    }

    void readOutput(Process process, ShellCommand command, IShellOutputCollector collector, AtomicBoolean stopped)
            throws Exception {
        if (command.isBinaryOutput()) {
            InputStream is = process.getInputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (true) {
                n = is.read(buf);
                if (n < 0) {
                    break;
                }
                collector.onOutputBytes(buf, 0, n);
                if (stopped.get())
                    break;
            }
        } else {
            BufferedReader inReader = this.createReader(process.getInputStream(), command.getEncoding());

            // clear the input stream buffer
            String line = inReader.readLine();
            while (line != null) {
                collector.onOutput(line);
                if (stopped.get())
                    break;

                line = inReader.readLine();
            }
        }
    }

    Future<?> scheduleTimeout(ShellCommand command, final Callable<?> cleanup) {
        if (command.getTimeout() > 0) {
            return GlobalExecutors.globalTimer().schedule(cleanup, command.getTimeout(), TimeUnit.MILLISECONDS);
        } else {
            return null;
        }
    }

    Process createProcess(ShellCommand command, ProcessBuilder builder) throws IOException {
        Process process;

        if (PlatformEnv.isWindows()) {
            synchronized (WindowsProcessLaunchLock) {
                // To workaround the race condition issue with child processes
                // inheriting unintended handles during process launch that can
                // lead to hangs on reading output and error streams, we
                // serialize process creation. More info available at:
                // http://support.microsoft.com/kb/315939
                process = builder.start();
            }
        } else {
            process = builder.start();
        }

        LOG.debug("shell.create_process:id={}, command={},process={}", command.getId(), command.getCommandString(),
                process);
        return process;
    }

    BufferedReader createReader(InputStream is, String encoding) throws IOException {
        if (encoding == null)
            encoding = StringHelper.ENCODING_UTF8;
        return new BufferedReader(new InputStreamReader(is, encoding));
    }

    void safeDestroy(ShellCommand command, Process process, AtomicBoolean stopped) {
        LOG.debug("nop.shell.destroy-process:id={},command={},process={}", command.getId(), command.getCommandString(),
                process);
        stopped.set(true);
        IoHelper.safeClose(process.getInputStream());
        IoHelper.safeClose(process.getOutputStream());
        IoHelper.safeClose(process.getErrorStream());
        try {
            process.destroy();
        } catch (Exception e) {
            LOG.error("nop.err.shell.process-destroy-fail", e);
        }
    }
}