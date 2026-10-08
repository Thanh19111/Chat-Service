package com.thanhpham.chat.component.rag.query.pre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryTransformerFactory {
    private final Map<String, QueryTransformer> transformers;

    public Query applyAll(Query query) {
        return transformers.values().stream()
                .reduce(query,
                        (currentQuery, transformer) -> transformer.transform(currentQuery),
                        (q1, q2) -> q2);
    }

    public List<QueryTransformer> getAll() {
        return List.copyOf(transformers.values());
    }
}
