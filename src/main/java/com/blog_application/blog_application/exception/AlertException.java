package com.blog_application.blog_application.exception;

import com.blog_application.blog_application.model.User;

public class AlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String alertType;

    public AlertException(String alertType, String alertMessage) {
        super(alertMessage);
        this.alertType = alertType;
    }

    // Constructor with an additional User parameter
    public AlertException(String alertType, String alertMessage, User user) {
        super(alertMessage);
        this.alertType = alertType;
        // Handle the User object as needed
    }

    public String getAlertType() {
        return alertType;
    }
}
