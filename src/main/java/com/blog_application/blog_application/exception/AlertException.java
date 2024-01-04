package com.blog_application.blog_application.exception;

public class AlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String alertType;

    public AlertException(String alertType, String alertMessage) {
        super(alertMessage);
        this.alertType = alertType;
    }

    public String getAlertType() {
        return alertType;
    }
}
