package com.thanhpham.chat.component.rag;

import com.thanhpham.chat.component.rag.etl.reader.handler.DocumentHandler;
import org.springframework.web.multipart.MultipartFile;

public abstract class Artifact implements ArtifactProcessor {
    public MultipartFile file;
    public DocumentHandler handler;
}
