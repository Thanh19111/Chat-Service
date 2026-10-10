package com.thanhpham.chat.component.rag.etl.reader.filesupport;

import com.thanhpham.chat.component.rag.etl.reader.Artifact;
import com.thanhpham.chat.component.rag.etl.reader.prehandler.DocumentHandler;
import com.thanhpham.chat.util.CommonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class MdDocumentHandler extends Artifact {

    public MdDocumentHandler(MultipartFile file) {
        this.file = file;
    }

    public MdDocumentHandler(MultipartFile file, DocumentHandler handler) {
        this.file = file;
        this.handler = handler;
    }

    private MdDocumentHandler(MdDocumentHandler.Builder builder) {
        this.file = builder.file;
        this.handler = builder.handler;
    }

    public static MdDocumentHandler.Builder builder() {
        return new MdDocumentHandler.Builder();
    }

    @Override
    public List<Document> read() throws IOException {
        if (ObjectUtils.isEmpty(file)) throw new IllegalArgumentException("File cannot be null or empty");
        Resource resource = CommonUtils.createResource(file);
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", "code.md")
                .build();
        DocumentReader markdownReader = new MarkdownDocumentReader(resource, config);
        return markdownReader.get();
    }

    @Override
    public void handler(DocumentHandler handler) {
        this.handler = handler;
    }

    public static class Builder {
        private MultipartFile file;
        private DocumentHandler handler;

        public MdDocumentHandler.Builder file(MultipartFile file) {
            this.file = file;
            return this;
        }

        public MdDocumentHandler.Builder file(DocumentHandler handler) {
            this.handler = handler;
            return this;
        }

        public MdDocumentHandler build() {
            return new MdDocumentHandler(this);
        }
    }
}
