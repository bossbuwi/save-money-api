package com.paradox.savemoney.util;

public class HttpUtils {

    public static String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

        }
        return authHeader.substring(7);
    }
}
