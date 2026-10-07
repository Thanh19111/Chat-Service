package com.thanhpham.chat.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class RagDocument {
    private String content;
    private Map<String, Object> metadata;
}
