package com.thanhpham.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultedPrompt {
    private String information;
    private PromptTemplate promptTemplate;
}
