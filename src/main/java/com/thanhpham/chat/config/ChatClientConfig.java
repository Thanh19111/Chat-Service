package com.thanhpham.chat.config;

import com.thanhpham.chat.advisor.TokenUsageAdvisor;
import com.thanhpham.chat.util.Constants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class ChatClientConfig {
    private final String DESCRIPTION = Constants.NAME + " " + Constants.DESCRIPTION;

    @Bean
    public ChatClient initSimpleChatClient(ChatClient.Builder builder,
                                           MessageChatMemoryAdvisor chatMemoryAdvisor,
                                           SimpleLoggerAdvisor simpleLoggerAdvisor,
                                           TokenUsageAdvisor tokenUsageAdvisor,
                                           ObjectProvider<SyncMcpToolCallbackProvider> mcpToolCallbackProvider) {
        Object[] toolCallBacks = Optional.ofNullable(mcpToolCallbackProvider.getIfAvailable())
                .map(SyncMcpToolCallbackProvider::getToolCallbacks)
                .orElseGet(() -> new ToolCallback[0]);

        return builder
                .defaultSystem(DESCRIPTION)
                .defaultTools(toolCallBacks)
                .defaultAdvisors(List.of(chatMemoryAdvisor, simpleLoggerAdvisor, tokenUsageAdvisor))
                .build();
    }
}
