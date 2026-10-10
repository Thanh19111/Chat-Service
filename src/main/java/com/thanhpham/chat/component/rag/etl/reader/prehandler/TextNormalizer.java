package com.thanhpham.chat.component.rag.etl.reader.prehandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.document.Document;

public class TextNormalizer extends DocumentHandler {

    @Override
    protected Document doProcess(Document document) {
        String cleanedText = StringUtils.normalizeSpace(document.getText());
        return new Document(document.getId(), cleanedText, document.getMetadata());
    }
}
