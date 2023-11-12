package com.example.productserviceproject.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
    private String message;
    private String cause;

    public ErrorResponse(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
