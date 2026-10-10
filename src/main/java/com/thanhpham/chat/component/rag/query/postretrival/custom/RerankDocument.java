package com.thanhpham.chat.component.rag.query.postretrival.custom;

import com.thanhpham.chat.util.RagUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Order(1)
@Component
public class RerankDocument implements DocumentPostProcessor {
    private final ChatClient chatClient;
    private static final int MAX_LIMIT = 5;

    public RerankDocument(ChatModel chatModel) {
        chatClient = ChatClient
                .builder(chatModel)
                .build();
    }

    @Override
    public @NonNull List<Document> process(@NonNull Query query, @NonNull List<Document> list) {
        if (CollectionUtils.isEmpty(list)) return List.of();
        List<Integer> ids = rerank(query, list);
        return ids.stream().map(list::get).toList();
    }

    @Override
    public @NonNull List<Document> apply(@NonNull Query query, @NonNull List<Document> documents) {
        return this.process(query, documents);
    }

    public List<Integer> rerank(@NonNull Query query, @NonNull List<Document> documents) {
        List<String> formated = RagUtils.getContent(documents, Document::getText);
        String docs = RagUtils.toString(formated);
        String prompt = String.format("""
                Bạn là một hệ thống Reranker tài liệu. Hãy đánh giá độ liên quan của các tài liệu sau với câu hỏi của người dùng.
                
                ⚠️ QUY TẮC BẮT BUỘC:
                1. Nếu có các tài liệu chứa nội dung trùng lặp hoặc tương tự nhau, BẮT BUỘC CHỈ CHỌN 1 ID TỐT NHẤT và KHÔNG ĐƯỢC CHỌN CÁC ID CÒN LẠI.
                2. Trả về tối đa %s ID theo thứ tự từ liên quan nhất đến ít liên quan nhất, CÓ THỂ ÍT HƠN HOẶC KHÔNG CHỌN NẾU NỘI DUNG TRÙNG HOẶC KHÔNG LIÊN QUAN.
                3. Định dạng chính xác mỗi dòng một ID (Ví dụ:
                0
                3
                1
                )
                4. Chỉ trả về danh sách ID, tuyệt đối không giải thích hay thêm bất kỳ ký tự nào khác.
                
                Câu hỏi: "%s"
                
                Danh sách tài liệu:
                %s
                """, MAX_LIMIT, query.text(), docs);

        return chatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }
}
