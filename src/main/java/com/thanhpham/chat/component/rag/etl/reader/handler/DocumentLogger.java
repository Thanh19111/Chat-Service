package com.thanhpham.chat.component.rag.etl.reader.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;

@Slf4j
public class DocumentLogger extends DocumentHandler {

    @Override
    protected Document doProcess(Document document) {
        log.info(document.toString());
        return document;
    }
}
