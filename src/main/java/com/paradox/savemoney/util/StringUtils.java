package com.paradox.savemoney.util;

import org.springframework.stereotype.Service;

@Service
public class StringUtils {

    public String stripSurroundingBrackets(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String result = str;
        // Remove leading '[' if present
        if (result.startsWith("[")) {
            result = result.substring(1);
        }
        // Remove trailing ']' if present
        if (result.endsWith("]")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
