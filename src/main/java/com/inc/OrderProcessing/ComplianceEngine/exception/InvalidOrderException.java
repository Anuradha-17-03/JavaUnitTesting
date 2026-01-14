package com.inc.OrderProcessing.ComplianceEngine.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
