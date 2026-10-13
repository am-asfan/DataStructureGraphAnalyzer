package com.dsa.util;

/**
 * Shared output helpers so every menu prints headers and messages in the same style.
 */
public final class ConsoleFormatter {

    public static final int WIDTH = 50;

    private ConsoleFormatter() {
        // Utility class - no objects needed.
    }

    /** Prints a title between two lines of '=' characters. */
    public static void printBanner(String title) {
        System.out.println("=".repeat(WIDTH));
        System.out.println(center(title, WIDTH));
        System.out.println("=".repeat(WIDTH));
    }

    /** Prints a submenu title such as "--------------- STACK OPERATIONS ---------------". */
    public static void printSectionTitle(String title) {
        String text = " " + title + " ";
        int dashes = Math.max(4, WIDTH - 2 - text.length());
        int left = dashes / 2;
        int right = dashes - left;
        System.out.println("-".repeat(left) + text + "-".repeat(right));
    }

    public static void success(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    /** Centres text within the given width using spaces on the left. */
    public static String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    /** Formats a nanosecond value with thousands separators, e.g. "12,345 ns". */
    public static String formatNanos(long nanos) {
        return String.format("%,d ns", nanos);
    }
}
