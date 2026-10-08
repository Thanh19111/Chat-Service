package com.thanhpham.chat.component.rag.query.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostProcesserFactory {
    private final Map<String, DocumentPostProcessor> processors;

    public List<DocumentPostProcessor> getAll() {
        return List.copyOf(processors.values());
    }
}
