/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.integration.file.local;

import io.nop.api.core.util.progress.IStepProgressListener;
import io.nop.commons.util.IoHelper;
import io.nop.core.resource.impl.AbstractResource;
import io.nop.integration.api.file.FileStatus;
import io.nop.integration.api.file.IFileServiceClient;
import io.nop.integration.api.file.IFileServiceClientFactory;

import java.io.OutputStream;

public class FileServiceResource extends AbstractResource {
    private final IFileServiceClientFactory factory;
    private FileStatus status;
    private String remotePath;

    public FileServiceResource(String path, String remotePath,
                               IFileServiceClientFactory factory, FileStatus status) {
        super(path);
        this.factory = factory;
        this.status = status;
        this.remotePath = remotePath;
    }

    private synchronized FileStatus getFileStatus() {
        if (status == null) {
            synchronized (this) {
                IFileServiceClient client = factory.newClient();
                try {
                    status = client.getFileStatus(remotePath);
                    if (status == null) {
                        status = new FileStatus();
                        status.setName(getName());
                        status.setLastModified(-1);
                        status.setSize(0);
                    }
                } finally {
                    IoHelper.safeCloseObject(client);
                }
            }
        }
        return status;
    }

    @Override
    public void writeToStream(OutputStream os, IStepProgressListener listener) {
        IFileServiceClient client = factory.newClient();
        try {
            client.downloadToStream(remotePath, os);
        } finally {
            IoHelper.safeClose(client);
        }
    }

    @Override
    public boolean delete() {
        IFileServiceClient client = factory.newClient();
        try {
            client.deleteFile(remotePath);
            return true;
        } finally {
            IoHelper.safeClose(client);
        }
    }

    @Override
    public long length() {
        return getFileStatus().getSize();
    }

    @Override
    public long lastModified() {
        return getFileStatus().getLastModified();
    }

    @Override
    public boolean exists() {
        return getFileStatus().getLastModified() > 0;
    }

    @Override
    protected Object internalObj() {
        return this;
    }
}
