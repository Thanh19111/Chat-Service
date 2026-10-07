package com.thanhpham.chat.service;

import com.thanhpham.chat.dto.response.BillDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {
    private final ChatClient chatClient;

    @Override
    public String chat(String conversationId, String message) {
        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(message)
                .call()
                .content();
    }

    public Flux<String> streamChat(String conversationId, String message) {
        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(message)
                .stream()
                .content();
    }

    private String chatWithImage(String conversationId, String message, List<MultipartFile> files) {
        List<Media> media = files.stream()
                .map(img -> new Media(MimeTypeUtils.IMAGE_PNG, img.getResource()))
                .toList();

        UserMessage userMessage = UserMessage.builder()
                .media(media)
                .text(message).build();

        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .messages(userMessage)
                .call()
                .content();
    }

    @Override
    public List<BillDetail> imageProcessing(String conversationId, List<MultipartFile> files) {
        List<Media> media = files
                .stream()
                .map(img -> new Media(MimeTypeUtils.IMAGE_PNG, img.getResource()))
                .toList();

        UserMessage userMessage = UserMessage.builder()
                .text("Extract detail from these images")
                .media(media)
                .build();

        return chatClient
                .prompt()
                .messages(userMessage)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }
}
