package com.thanhpham.chat.component.rag.query.post.custom;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Order(1)
@Component
public class RerankDocument implements DocumentPostProcessor {

    @Override
    public @NonNull List<Document> process(@NonNull Query query, @NonNull List<Document> list) {
        if (CollectionUtils.isEmpty(list)) return List.of();
        return list.stream()
                .sorted((doc1, doc2) -> {
                    Float score1 = NumberUtils.createFloat(doc1.getMetadata().getOrDefault("distance", 0.0).toString());
                    Float score2 = NumberUtils.createFloat(doc2.getMetadata().getOrDefault("distance", 0.0).toString());
                    return score2.compareTo(score1);
                })
                .limit(3)
                .toList();
    }

    @Override
    public @NonNull List<Document> apply(@NonNull Query query, @NonNull List<Document> documents) {
        return this.process(query, documents);
    }
}
