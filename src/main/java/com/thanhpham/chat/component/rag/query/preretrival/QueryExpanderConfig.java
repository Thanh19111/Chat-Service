package com.thanhpham.chat.component.rag.query.preretrival;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryExpanderConfig {

    @Bean
    public MultiQueryExpander initMultiQueryExpander(ChatModel chatModel) {
        return MultiQueryExpander.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .numberOfQueries(2)
                .includeOriginal(false)
                .build();
    }
}
