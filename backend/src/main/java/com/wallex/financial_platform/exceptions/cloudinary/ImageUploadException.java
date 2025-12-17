package com.wallex.financial_platform.exceptions.cloudinary;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message) {
        super(message);
    }
    public ImageUploadException(String message, String description) {
        super(message);
    }
}
