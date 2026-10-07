package com.thanhpham.chat.service;

import com.thanhpham.chat.dto.response.BillDetail;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IChatService {
    String chat(String conversationId, String message);
    List<BillDetail> imageProcessing(String conversationId, List<MultipartFile> files);
    Flux<String> streamChat(String conversationId, String message);
}
