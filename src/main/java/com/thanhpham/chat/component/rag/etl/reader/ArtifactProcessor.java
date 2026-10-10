package com.thanhpham.chat.component.rag.etl.reader;

import com.thanhpham.chat.component.rag.etl.reader.prehandler.DocumentHandler;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.List;

public interface ArtifactProcessor {
    List<Document> read() throws IOException;
    void handler(DocumentHandler handler);
}
