package com.thanhpham.chat.component.rag.etl.reader;

import com.thanhpham.chat.component.rag.etl.reader.prehandler.DocumentHandler;
import org.springframework.web.multipart.MultipartFile;

public abstract class Artifact implements ArtifactProcessor {
    public MultipartFile file;
    public DocumentHandler handler;
}
