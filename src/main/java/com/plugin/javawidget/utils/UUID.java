package com.plugin.javawidget.utils;

public enum UUID {
    UUID;

    private UUID() { }

    public static String get() {
       return java.util.UUID.randomUUID().toString();
    }
}
