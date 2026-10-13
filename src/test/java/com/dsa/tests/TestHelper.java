package com.dsa.tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A very small assertion library so the tests run with the plain JDK (no JUnit download needed).
 * Each check is counted; AllTests prints the totals and exits with code 1 if anything failed.
 */
public final class TestHelper {

    private static int passed;
    private static final List<String> failures = new ArrayList<>();

    private TestHelper() {
    }

    public static void section(String name) {
        System.out.println();
        System.out.println("== " + name + " ==");
    }

    public static void check(boolean condition, String description) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + description);
        } else {
            failures.add(description);
            System.out.println("  FAIL  " + description);
        }
    }

    public static void assertEquals(long expected, long actual, String description) {
        check(expected == actual, description + " (expected " + expected + ", got " + actual + ")");
    }

    public static void assertEquals(Object expected, Object actual, String description) {
        check(Objects.equals(expected, actual), description + " (expected " + expected + ", got " + actual + ")");
    }

    /** Passes if running the code throws the expected exception type. */
    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable code, String description) {
        try {
            code.run();
            check(false, description + " (no exception was thrown)");
        } catch (Throwable thrown) {
            check(expectedType.isInstance(thrown),
                    description + " (threw " + thrown.getClass().getSimpleName() + ")");
        }
    }

    /** Runs code while capturing everything it prints to System.out, and returns that text. */
    public static String captureOutput(Runnable code) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            code.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static int getPassed() {
        return passed;
    }

    public static List<String> getFailures() {
        return failures;
    }
}
