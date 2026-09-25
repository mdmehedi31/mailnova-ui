package com.mailnova.view.utils;

public final class CustomNotification {

    private CustomNotification() {
    }

    public static void success(String title, String message) {
        show(NotificationType.SUCCESS, title, message);
    }

    public static void error(String title, String message) {
        show(NotificationType.ERROR, title, message);
    }

    public static void warning(String title, String message) {
        show(NotificationType.WARNING, title, message);
    }

    public static void info(String title, String message) {
        show(NotificationType.INFO, title, message);
    }

    /** Overloads for callers that want a custom on-screen duration (in milliseconds). */
    public static void success(String title, String message, int durationMillis) {
        show(NotificationType.SUCCESS, title, message, durationMillis);
    }

    public static void error(String title, String message, int durationMillis) {
        show(NotificationType.ERROR, title, message, durationMillis);
    }

    public static void warning(String title, String message, int durationMillis) {
        show(NotificationType.WARNING, title, message, durationMillis);
    }

    public static void info(String title, String message, int durationMillis) {
        show(NotificationType.INFO, title, message, durationMillis);
    }

    private static void show(NotificationType type, String title, String message) {
        new MailNovaNotification(type, title, message).open();
    }

    private static void show(NotificationType type, String title, String message, int durationMillis) {
        new MailNovaNotification(type, title, message, durationMillis).open();
    }
}
