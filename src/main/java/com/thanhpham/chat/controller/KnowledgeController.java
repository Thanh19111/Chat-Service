package com.thanhpham.chat.controller;

import com.thanhpham.chat.dto.request.RagDocument;
import com.thanhpham.chat.dto.response.ApiResponse;
import com.thanhpham.chat.service.IRagService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {
    private final IRagService iRagService;

    @GetMapping("/ask")
    public ResponseEntity<ApiResponse<String>> askKnowledge(@RequestParam("message") String message,
                                                            @RequestParam(name = "conversationId", required = false) String conversationId) {
        conversationId = StringUtils.defaultIfBlank(conversationId, UUID.randomUUID().toString());
        String result = iRagService.retrieveAndGenerate(conversationId, message);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result(result)
                        .message("SUCCESS")
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Document>>> searchKnowledge(@RequestParam("query") String query,
                                                                       @RequestParam(name = "top", required = false) Integer top,
                                                                       @RequestParam(name = "threshold", required = false) Double threshold,
                                                                       @RequestParam(name = "filter", required = false) String filter) {
        List<Document> result = iRagService.semanticSearch(query, top, threshold, filter);
        return ResponseEntity.ok(
                ApiResponse.<List<Document>>builder()
                        .result(result)
                        .message("SUCCESS")
                        .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Document>>> ingestKnowledge(@RequestBody List<RagDocument> documents) {
        List<Document> chunks = iRagService.saveDocuments(documents);
        return ResponseEntity.ok(
                ApiResponse.<List<Document>>builder()
                        .result(chunks)
                        .message("SUCCESS")
                        .build());
    }

    @PostMapping("/file")
    public ResponseEntity<ApiResponse<List<Document>>> readFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
        List<Document> chunks = iRagService.saveDocument(file);
        return ResponseEntity.ok(
                ApiResponse.<List<Document>>builder()
                        .result(chunks)
                        .message("SUCCESS")
                        .build());
    }
}
