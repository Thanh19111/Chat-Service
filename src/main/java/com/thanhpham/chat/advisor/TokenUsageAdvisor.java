package com.thanhpham.chat.advisor;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;

@Slf4j
public class TokenUsageAdvisor implements CallAdvisor {

    @Override
    public @NonNull ChatClientResponse adviseCall(@NonNull ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponseMetadata metadata = chatClientResponse.chatResponse().getMetadata();
        Usage usage = metadata.getUsage();
        RateLimit rateLimit = metadata.getRateLimit();
        log.info("Token usage details : {}", usage);
        log.info("Rate limit details : {}", rateLimit);
        return chatClientResponse;
    }

    @Override
    public @NonNull String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
