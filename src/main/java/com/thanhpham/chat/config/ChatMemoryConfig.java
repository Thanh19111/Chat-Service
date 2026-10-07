package com.thanhpham.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class ChatMemoryConfig {
    private static final int MAX_MESSAGES = 20;

    @Bean
    @ConditionalOnProperty(
            name = "app.chat-memory.type",
            havingValue = "IN_MEMORY",
            matchIfMissing = true
    )
    public ChatMemory inMemoryChatMemory() {
        log.info(">> Init In-Memory Store successfully!");
        ChatMemoryRepository inMemoryStore = new InMemoryChatMemoryRepository();
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(inMemoryStore)
                .maxMessages(MAX_MESSAGES)
                .build();
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.chat-memory.type",
            havingValue = "POSTGRES",
            matchIfMissing = false
    )
    public ChatMemory jdbcChatMemory(JdbcTemplate jdbcTemplate) {
        log.info(">> Init JDBC Memory Store successfully!");
        JdbcChatMemoryRepository jdbcStore = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .build();

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcStore)
                .maxMessages(MAX_MESSAGES)
                .build();
    }
}
