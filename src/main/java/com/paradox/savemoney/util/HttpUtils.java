package com.paradox.savemoney.util;

import com.paradox.savemoney.exception.UnauthorizedException;

public class HttpUtils {

    public static String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }
        return authHeader.substring(7);
    }
}
