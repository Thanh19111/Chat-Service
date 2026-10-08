package com.thanhpham.chat.component.rag.etl.reader.file;

import com.thanhpham.chat.component.rag.etl.reader.Artifact;
import com.thanhpham.chat.component.rag.etl.reader.handler.DocumentHandler;
import com.thanhpham.chat.util.CommonUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class TikaDocumentHandler extends Artifact {

    @Override
    public List<Document> read() throws IOException {
        Resource resource = CommonUtils.createResource(file);
        DocumentReader tikaReader = new TikaDocumentReader(resource);
        return tikaReader.get()
                .stream()
                .map(handler::apply)
                .toList();
    }

    @Override
    public void handler(DocumentHandler handler) {
        this.handler = handler;
    }

    public TikaDocumentHandler(MultipartFile file) {
        this.file = file;
        this.handler = null;
    }

    public TikaDocumentHandler(MultipartFile file, DocumentHandler handler) {
        this.file = file;
        this.handler = handler;
    }

    private TikaDocumentHandler(Builder builder) {
        this.file = builder.file;
        this.handler = builder.handler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MultipartFile file;
        private DocumentHandler handler;

        public Builder file(MultipartFile file) {
            this.file = file;
            return this;
        }

        public Builder handler(DocumentHandler handler) {
            this.handler = handler;
            return this;
        }

        public TikaDocumentHandler build() {
            return new TikaDocumentHandler(this);
        }
    }
}
