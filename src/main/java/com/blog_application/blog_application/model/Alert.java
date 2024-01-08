package com.blog_application.blog_application.model;

public class Alert {

    private final String type;
    private final String message;

    public Alert(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
