package com.epam.gatewayserver.eception;

public class MissingAuthorizationException extends RuntimeException {
    public MissingAuthorizationException(String message) {
        super(message);
    }
}
