package com.thanhpham.chat.component.rag.etl.transformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocTransformerFactory {
    private final Map<String, DocumentTransformer> transformers;

    public List<Document> applyAll(List<Document> documents) {
        return transformers.values().stream()
                .reduce(documents,
                        (currentDocuments, transformer) -> transformer.apply(documents),
                        (q1, q2) -> q2);
    }
}
