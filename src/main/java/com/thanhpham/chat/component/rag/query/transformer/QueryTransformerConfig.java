package com.thanhpham.chat.component.rag.query.transformer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryTransformerConfig {

    private static final String TARGET_LANGUAGE = "Vietnamese";

    @Bean
    public RewriteQueryTransformer initRewriteQueryTransformer(ChatClient.Builder builder) {
        return RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    @Bean
    public TranslationQueryTransformer initTranslationQueryTransformer(ChatClient.Builder builder) {
        return TranslationQueryTransformer.builder()
                .chatClientBuilder(builder)
                .targetLanguage(TARGET_LANGUAGE)
                .build();
    }
}
