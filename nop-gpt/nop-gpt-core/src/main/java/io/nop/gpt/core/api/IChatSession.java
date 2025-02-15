package io.nop.gpt.core.api;

import java.util.concurrent.CompletionStage;

public interface IChatSession extends AutoCloseable {
    String getSessionId();

    CompletionStage<String> sendChatAsync(String role, String question, ChatOptions options);
}