package com.thanhpham.chat.config;

import com.knuddels.jtokkit.api.EncodingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class VectorStoreConfig {
    @Bean
    @ConditionalOnProperty(
            name = "app.vector-store.type",
            havingValue = "POSTGRES",
            matchIfMissing = false
    )
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        log.info(">> Init PG VectorStore");
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(1536)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .initializeSchema(true)
                .build();
    }

    @Bean
    @ConditionalOnProperty(
            name = "app.vector-store.type",
            havingValue = "IN_MEMORY",
            matchIfMissing = true
    )
    public VectorStore inMemoryVectorStore(EmbeddingModel embeddingModel) {
        log.info(">> Init In-Memory VectorStore");
        return SimpleVectorStore.builder(embeddingModel)
                .build();
    }

    @Bean
    public BatchingStrategy customBatchingStrategy() {
        return new TokenCountBatchingStrategy(EncodingType.CL100K_BASE, 6144, 0.1);
    }
}
