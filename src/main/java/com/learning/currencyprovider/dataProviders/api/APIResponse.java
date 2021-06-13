package com.learning.currencyprovider.dataProviders.api;

public class APIResponse {
    private final int code;
    private final String message;

    protected static final int CORRECT_RESPONSE_CODE = 200;
    protected static final String CORRECT_RESPONSE_MESSAGE = "OK";

    public APIResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
