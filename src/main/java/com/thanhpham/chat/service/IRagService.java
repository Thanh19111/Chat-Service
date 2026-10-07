package com.thanhpham.chat.service;

import com.thanhpham.chat.dto.request.RagDocument;
import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface IRagService {
    List<Document> saveDocuments(List<RagDocument> documents);

    List<Document> saveDocument(MultipartFile file) throws IOException;

    String retrieveAndGenerate(String conversationId, String message);

    List<Document> semanticSearch(String query);

    List<Document> semanticSearch(String query, @Nullable Integer top);

    List<Document> semanticSearch(String query, @Nullable Integer top, @Nullable Double threshold);

    List<Document> semanticSearch(String query, @Nullable Integer top, @Nullable Double threshold, @Nullable String filter);
}
