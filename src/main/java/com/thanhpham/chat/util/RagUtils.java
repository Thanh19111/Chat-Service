package com.thanhpham.chat.util;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.function.Function;

public class RagUtils {
    public static String toString(List<String> documents) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < documents.size(); i++) {
            String content = documents.get(i);
            sb.append("[ID: ").append(i).append("]\n");
            sb.append(content).append("\n\n");
        }
        return sb.toString();
    }

    public static <T> List<T> getContent(List<Document> documents, Function<Document, T> extractor) {
        return documents.stream().map(extractor).toList();
    }
}
