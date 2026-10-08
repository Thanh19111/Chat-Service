package com.thanhpham.chat.config;

import com.thanhpham.chat.advisor.TokenUsageAdvisor;
import com.thanhpham.chat.component.rag.query.post.PostProcesserFactory;
import com.thanhpham.chat.component.rag.query.pre.QueryTransformerFactory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AdvisorConfig {

    @Bean
    public MessageChatMemoryAdvisor initChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor initSimpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    @Bean
    public SafeGuardAdvisor initSafeGuardAdvisor() {
        return new SafeGuardAdvisor(List.of());
    }

    @Bean
    public TokenUsageAdvisor initTokenUsageAdvisor() {
        return new TokenUsageAdvisor();
    }

    @Bean
    public RetrievalAugmentationAdvisor initRetrievalAugmentationAdvisor(VectorStore vectorStore,
                                                                         QueryTransformerFactory transformerFactory,
                                                                         PostProcesserFactory processerFactory,
                                                                         MultiQueryExpander expander) {
        return RetrievalAugmentationAdvisor.builder()
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .queryTransformers(transformerFactory.getAll())
                .queryExpander(expander)
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.65)
                        .topK(7)
                        .vectorStore(vectorStore)
                        .build())
                .documentPostProcessors(processerFactory.getAll())
                .build();
    }
}
