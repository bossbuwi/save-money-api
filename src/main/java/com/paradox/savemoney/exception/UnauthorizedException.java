package com.paradox.savemoney.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Bearer token is not authorized");
    }
}
