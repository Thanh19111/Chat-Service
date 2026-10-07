package com.thanhpham.chat.component.rag.query.transformer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryExpanderConfig {

    @Bean
    public MultiQueryExpander initMultiQueryExpander(ChatClient.Builder builder) {
        return MultiQueryExpander.builder()
                .chatClientBuilder(builder)
                .numberOfQueries(3)
                .includeOriginal(false)
                .build();
    }
}
