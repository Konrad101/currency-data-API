package com.learning.currencyprovider.dataProviders.api;

public class APIResponse {
    private final boolean success;
    private final String message;

    protected static final String CORRECT_RESPONSE_MESSAGE = "OK";


    public APIResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }
}
