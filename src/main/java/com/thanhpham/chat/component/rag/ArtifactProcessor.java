package com.thanhpham.chat.component.rag;

import com.thanhpham.chat.component.rag.etl.reader.handler.DocumentHandler;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.List;

public interface ArtifactProcessor {
    List<Document> read() throws IOException;
    void handler(DocumentHandler handler);
}
