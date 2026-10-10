package com.thanhpham.chat.component.rag.etl.reader.prehandler;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.ai.document.Document;

public abstract class DocumentHandler {
    protected DocumentHandler nextHandler;

    protected abstract Document doProcess(Document document);

    public DocumentHandler setNext(DocumentHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public Document apply(Document document) {
        Document newDocument = doProcess(document);
        if (ObjectUtils.isNotEmpty(nextHandler)) {
            return nextHandler.apply(newDocument);
        }
        return newDocument;
    }
}
