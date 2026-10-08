package com.thanhpham.chat.component.rag.etl.reader.filesupport;

import com.thanhpham.chat.component.rag.etl.reader.Artifact;
import com.thanhpham.chat.component.rag.etl.reader.handler.DocumentHandler;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class PdfDocumentHandler extends Artifact {

    @Override
    public List<Document> read() throws IOException {
        Resource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        DocumentReader pdfReader = new PagePdfDocumentReader(resource);
        return pdfReader.get();
    }

    @Override
    public void handler(DocumentHandler handler) {
        this.handler = handler;
    }

    public PdfDocumentHandler(MultipartFile file) {
        this.file = file;
    }

    private PdfDocumentHandler(PdfDocumentHandler.Builder builder) {
        this.file = builder.file;
        this.handler = builder.handler;
    }

    public static PdfDocumentHandler.Builder builder() {
        return new PdfDocumentHandler.Builder();
    }

    public static class Builder {
        private MultipartFile file;
        private DocumentHandler handler;

        public PdfDocumentHandler.Builder file(MultipartFile file) {
            this.file = file;
            return this;
        }

        public PdfDocumentHandler.Builder file(DocumentHandler handler) {
            this.handler = handler;
            return this;
        }

        public PdfDocumentHandler build() {
            return new PdfDocumentHandler(this);
        }
    }
}
