package com.thanhpham.chat.component.rag.query.postretrival.custom;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Order
@Component
public class DocumentLogger implements DocumentPostProcessor {

    @Override
    public @NonNull List<Document> process(@NonNull Query query, @NonNull List<Document> list) {
        list.forEach(document -> log.info("{}", document));
        return list;
    }

    @Override
    public @NonNull List<Document> apply(@NonNull Query query, @NonNull List<Document> documents) {
        return this.process(query, documents);
    }
}
