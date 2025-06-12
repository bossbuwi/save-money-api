package com.paradox.savemoney.config.structure;

public class HttpStructure {
    public static class HeaderKey {
        public static final String ACCEPT = "Accept";
        public static final String AUTHORIZATION = "Authorization";
    }

    public static class HeaderValue {
        public static final String ACCEPT_JSON = "application/json";
        public static final String AUTHORIZATION_BEARER = "Bearer ";
    }
}
