package com.law.app.utils;

import com.law.app.payload.response.StoredFileDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

@Component
public class StorageUtil {
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpeg";

    public StoredFileDto parseMultipartFile(MultipartFile file, String requiredContentTypePrefix, String fieldLabel) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = StringUtils.cleanPath(Objects.toString(file.getOriginalFilename(), ""));
        String fileName;
        if (!StringUtils.hasText(originalName) || originalName.contains("..")) {
            fileName = null;
        } else {
            int slashIndex = Math.max(originalName.lastIndexOf('/'), originalName.lastIndexOf('\\'));
            fileName = slashIndex >= 0 ? originalName.substring(slashIndex + 1) : originalName;
        }

        String contentType = normalizeContentType(file.getContentType(), fileName);
        String requiredPrefix = Objects.toString(requiredContentTypePrefix, "").trim().toLowerCase(Locale.ROOT);
        String label = StringUtils.hasText(fieldLabel) ? fieldLabel : "File";
        if (StringUtils.hasText(requiredPrefix) && !contentType.startsWith(requiredPrefix)) {
            throw new RuntimeException("Error: " + label + " must be of type " + requiredPrefix + "*.");
        }

        try {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            return new StoredFileDto(base64, contentType, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error: Failed to read " + label.toLowerCase(Locale.ROOT) + ".", e);
        }
    }

    private String normalizeContentType(String contentType, String fileName) {
        String normalized = Objects.toString(contentType, "").trim().toLowerCase(Locale.ROOT);
        if (StringUtils.hasText(normalized) && !"application/octet-stream".equals(normalized)) {
            return normalized;
        }
        if (!StringUtils.hasText(fileName)) {
            return DEFAULT_IMAGE_CONTENT_TYPE;
        }
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".bmp")) return "image/bmp";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        return DEFAULT_IMAGE_CONTENT_TYPE;
    }
}
