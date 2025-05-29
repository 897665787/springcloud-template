package com.company.gateway.developer.policy.filter;

public class TokenContext {
    private static final ThreadLocal<String> TOKEN_TL = new ThreadLocal<>();

    private TokenContext() {
    }

    public static void setToken(String token) {
        TOKEN_TL.set(token);
    }

    public static String getToken() {
        return TOKEN_TL.get();
    }

    public static void removeToken() {
        TOKEN_TL.remove();
    }
}
