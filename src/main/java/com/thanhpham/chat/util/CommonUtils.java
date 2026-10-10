package com.thanhpham.chat.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static Resource createResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                return file.getSize();
            }
        };
    }

    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("```(?:json|markdown|text)?\\s*([\\s\\S]*?)\\s*```", Pattern.CASE_INSENSITIVE);

    public static String formatResponse(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return "";
        }

        String formatted = rawText;
        
        Matcher matcher = CODE_BLOCK_PATTERN.matcher(formatted);
        if (matcher.find()) {
            formatted = matcher.group(1);
        }

        formatted = formatted.replace("\r\n", "\n");

        formatted = formatted.trim();

        formatted = formatted.replaceAll("\\n{3,}", "\n\n");

        return formatted;
    }
}
