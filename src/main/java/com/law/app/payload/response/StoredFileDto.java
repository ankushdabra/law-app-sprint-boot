package com.law.app.payload.response;

public record StoredFileDto(String data, String contentType, String fileName) {
    public StoredFileDto {
        if (data == null) {
            throw new RuntimeException("Error: File data is required.");
        }
        if (contentType == null) {
            throw new RuntimeException("Error: File content type is required.");
        }
    }
}
