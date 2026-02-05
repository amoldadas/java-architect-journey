package com.softwarearchitect.orderplatform.shared.domain;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
