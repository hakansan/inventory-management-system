package com.oneamz.inventory.exception;

public class InventoryException extends RuntimeException {
    private final String code;

    public InventoryException(ApiResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    public InventoryException(ApiResponseCode responseCode, String customMessage) {
        super(customMessage);
        this.code = responseCode.getCode();
    }

    public String getCode() {
        return code;
    }
}