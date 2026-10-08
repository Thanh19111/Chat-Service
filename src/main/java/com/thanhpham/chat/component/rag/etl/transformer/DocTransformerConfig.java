package com.thanhpham.chat.component.rag.etl.transformer;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DocTransformerConfig {

    @Bean
    public TokenTextSplitter initTokenTextSplitter() {
        return TokenTextSplitter.builder()
                .withMinChunkSizeChars(350)
                .withChunkSize(800)
                .withPunctuationMarks(List.of('.', '?', '!', '\n', ';', ':', '。'))
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(10000)
                .withKeepSeparator(true)
                .build();
    }

    @Bean
    public KeywordMetadataEnricher initKeywordMetadataEnricher(ChatModel chatModel) {
        return KeywordMetadataEnricher.builder(chatModel)
                .keywordCount(5)
                .build();
    }

    @Bean
    public SummaryMetadataEnricher initSummaryMetadataEnricher(ChatModel chatModel) {
        return new SummaryMetadataEnricher(chatModel,
                List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS,
                        SummaryMetadataEnricher.SummaryType.CURRENT,
                        SummaryMetadataEnricher.SummaryType.NEXT));
    }
}
