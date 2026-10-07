package com.thanhpham.chat.component.rag.query.transformer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class QueryTransformerFactory {
    private final Map<String, QueryTransformer> transformerMap;

    public QueryTransformerFactory(Map<String, QueryTransformer> transformerMap) {
        log.info("Total query transformers: {} have been registered", transformerMap.size());
        this.transformerMap = transformerMap;
    }

    public Query applyAll(Query query) {
        return transformerMap.values().stream()
                .reduce(query,
                        (currentQuery, transformer) -> transformer.transform(currentQuery),
                        (q1, q2) -> q2);
    }

    public List<QueryTransformer> getAll() {
        return transformerMap.values().stream().toList();
    }
}
