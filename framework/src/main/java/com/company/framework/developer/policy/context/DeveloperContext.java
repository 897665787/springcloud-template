package com.company.framework.developer.policy.context;

public class DeveloperContext {
    private static final ThreadLocal<String> DEVELOPER_TL = new ThreadLocal<>();

    private DeveloperContext() {
    }

    public static void set(String developer) {
        DEVELOPER_TL.set(developer);
    }

    public static String get() {
        return DEVELOPER_TL.get();
    }

    public static void remove() {
        DEVELOPER_TL.remove();
    }
}
