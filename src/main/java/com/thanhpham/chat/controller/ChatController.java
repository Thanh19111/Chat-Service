package com.thanhpham.chat.controller;

import com.thanhpham.chat.dto.response.ApiResponse;
import com.thanhpham.chat.dto.response.BillDetail;
import com.thanhpham.chat.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatController {
    private final IChatService iChatService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<String>> chat(@RequestParam("message") String message,
                                                    @RequestParam(name = "conversationId", required = false) String conversationId) {
        conversationId = StringUtils.defaultIfBlank(conversationId, UUID.randomUUID().toString());
        String result = iChatService.chat(conversationId, message);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .statusCode(200)
                        .result(result)
                        .message("SUCCESS")
                        .build());
    }

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message,
                               @RequestParam(name = "conversationId", required = false) String conversationId) {
        conversationId = StringUtils.defaultIfBlank(conversationId, UUID.randomUUID().toString());
        return iChatService.streamChat(conversationId, message);
    }

    @PostMapping("/ocr")
    public ResponseEntity<ApiResponse<List<BillDetail>>> ocr(@RequestParam("files") List<MultipartFile> files,
                                                             @RequestParam(name = "conversationId", required = false) String conversationId) {
        conversationId = StringUtils.defaultIfBlank(conversationId, UUID.randomUUID().toString());
        List<BillDetail> result = iChatService.imageProcessing(conversationId, files);
        return ResponseEntity.ok(
                ApiResponse.<List<BillDetail>>builder()
                        .result(result)
                        .message("SUCCESS")
                        .build());
    }
}
