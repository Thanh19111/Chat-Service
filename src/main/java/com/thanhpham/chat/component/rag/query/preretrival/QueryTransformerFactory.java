package com.thanhpham.chat.component.rag.query.preretrival;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryTransformerFactory {
    private final Map<String, QueryTransformer> transformers;

    public List<QueryTransformer> findAll() {
        return List.copyOf(transformers.values());
    }
}
