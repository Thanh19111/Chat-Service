package com.thanhpham.chat.service;

import com.thanhpham.chat.component.AsyncExecutor;
import com.thanhpham.chat.component.rag.etl.reader.Artifact;
import com.thanhpham.chat.component.rag.etl.reader.filesupport.TikaDocumentHandler;
import com.thanhpham.chat.component.rag.etl.reader.prehandler.DocumentHandler;
import com.thanhpham.chat.component.rag.etl.reader.prehandler.DocumentLogger;
import com.thanhpham.chat.component.rag.etl.reader.prehandler.TextNormalizer;
import com.thanhpham.chat.component.rag.etl.transformer.DocTransformerFactory;
import com.thanhpham.chat.dto.request.RagDocument;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService implements IRagService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final AsyncExecutor executor;
    private final DocTransformerFactory transformerFactory;
    private final RetrievalAugmentationAdvisor augmentationAdvisor;
    private final VectorStoreChatMemoryAdvisor chatMemoryAdvisor;

    private static final int BATCH_SIZE = 15;
    private static final Duration BATCH_DELAY = Duration.ofMinutes(2);

    @Override
    public String retrieveAndGenerate(String conversationId, @NonNull String message) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(augmentationAdvisor, chatMemoryAdvisor)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @Override
    public List<Document> semanticSearch(String query) {
        return semanticSearch(query, null, null, null);
    }

    @Override
    public List<Document> semanticSearch(String query, @Nullable Integer top) {
        return semanticSearch(query, top, null, null);
    }

    @Override
    public List<Document> semanticSearch(String query, @Nullable Integer top, @Nullable Double threshold) {
        return semanticSearch(query, top, threshold, null);
    }

    @Override
    public List<Document> semanticSearch(String query, @Nullable Integer top, @Nullable Double threshold, @Nullable String filter) {
        SearchRequest request = buildSearchRequest(query, top, threshold, filter);
        return vectorStore.similaritySearch(request);
    }

    @Override
    public List<Document> saveDocuments(List<RagDocument> documents) {
        List<Document> rawDocuments = documents.stream()
                .map(document -> new Document(document.getContent(), document.getMetadata() == null ? new HashMap<>() : document.getMetadata())).toList();
        return saveRawDocuments(rawDocuments);
    }

    @Override
    public List<Document> saveDocument(MultipartFile file) throws IOException {
        DocumentHandler handler = new TextNormalizer()
                .setNext(new DocumentLogger());
        Artifact processor = TikaDocumentHandler.builder()
                .file(file)
                .handler(handler)
                .build();
        List<Document> transformed = transformerFactory.applyAll(processor.read());
        return saveRawDocuments(transformed);
    }

    private List<Document> saveRawDocuments(@Nullable List<Document> chunks) {
        if (CollectionUtils.isEmpty(chunks)) return List.of();
        executor.runAsync(() -> {
            log.info("START processing chunks...");
            if (CollectionUtils.isEmpty(chunks)) {
                log.info("No chunks to process.");
                return;
            }

            int totalItems = chunks.size();
            List<List<Document>> batches = ListUtils.partition(chunks, BATCH_SIZE);
            int totalBatches = batches.size();

            AtomicInteger processedBatches = new AtomicInteger(0);
            AtomicInteger processedItems = new AtomicInteger(0);

            batches.forEach(batch -> {
                int currentBatch = processedBatches.incrementAndGet();
                int currentItems = processedItems.addAndGet(batch.size());
                double progressPercent = ((double) currentItems / totalItems) * 100;

                log.info("[PROGRESS TASK] Batch {}/{} ({}/{} items - {}%)",
                        currentBatch,
                        totalBatches,
                        currentItems,
                        totalItems,
                        String.format("%.2f", progressPercent));

                vectorStore.accept(batch);

                if (currentBatch < totalBatches) {
                    try {
                        Thread.sleep(BATCH_DELAY.toMillis());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Process interrupted while sleeping", e);
                    }
                }
            });

            log.info("END: Processed {} items across {} batches successfully.", totalItems, totalBatches);
        });
        return chunks;
    }

    private SearchRequest buildSearchRequest(String query, @Nullable Integer top, @Nullable Double threshold, @Nullable String filter) {
        SearchRequest.Builder builder = SearchRequest.builder().query(query);
        if (ObjectUtils.isNotEmpty(top)) builder.topK(top);
        if (ObjectUtils.isNotEmpty(threshold)) builder.similarityThreshold(threshold);
        if (StringUtils.isNotBlank(filter)) builder.filterExpression(filter);
        return builder.build();
    }
}
