package com.learning.currencyprovider.dataProviders.api;

public abstract class APIResponse {
    private final int code;
    private final String message;

    public static final int CORRECT_RESPONSE_CODE = 200;
    public static final String CORRECT_RESPONSE_MESSAGE = "OK";

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public APIResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
