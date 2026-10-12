package com.thanhpham.chat.component.rag.query.preretrival;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class QueryTransformerConfig {

    private static final String TARGET_LANGUAGE = "Vietnamese";

    @Bean
    @Order(2)
    public CompressionQueryTransformer initCompressionQueryTransformer(ChatModel chatModel) {
        return CompressionQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .build();
    }

    @Bean
    @Order(1)
    public RewriteQueryTransformer initRewriteQueryTransformer(ChatModel chatModel) {
        return RewriteQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .build();
    }

    @Bean
    @Order(3)
    public TranslationQueryTransformer initTranslationQueryTransformer(ChatModel chatModel) {
        return TranslationQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .targetLanguage(TARGET_LANGUAGE)
                .build();
    }
}
