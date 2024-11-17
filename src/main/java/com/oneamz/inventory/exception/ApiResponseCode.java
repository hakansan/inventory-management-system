package com.oneamz.inventory.exception;

public enum ApiResponseCode {
    PRODUCT_NOT_FOUND("E001", "Product not found"),
    CATEGORY_NOT_FOUND("E002", "Category not found"),
    INVALID_REQUEST("E003", "Invalid request"),
    INTERNAL_ERROR("E004", "Internal server error");

    private final String code;
    private final String message;

    ApiResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}