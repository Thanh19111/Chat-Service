package com.thanhpham.chat.component.rag.query.pre;

import org.springframework.ai.chat.client.ChatClient;
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
    public CompressionQueryTransformer initCompressionQueryTransformer(ChatClient.Builder builder) {
        return CompressionQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    @Bean
    @Order(1)
    public RewriteQueryTransformer initRewriteQueryTransformer(ChatClient.Builder builder) {
        return RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    @Bean
    @Order(3)
    public TranslationQueryTransformer initTranslationQueryTransformer(ChatClient.Builder builder) {
        return TranslationQueryTransformer.builder()
                .chatClientBuilder(builder)
                .targetLanguage(TARGET_LANGUAGE)
                .build();
    }
}
