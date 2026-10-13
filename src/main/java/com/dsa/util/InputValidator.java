package com.dsa.util;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Reusable console input helper.
 *
 * Input strategy: every value is read as a whole line with Scanner.nextLine() and then
 * parsed. This avoids the classic Scanner problem where nextInt() leaves a newline or
 * an invalid token in the buffer. Every read method re-prompts until the input is valid,
 * so ordinary mistakes (letters, empty input, out-of-range numbers) never crash the program.
 */
public class InputValidator implements AutoCloseable {

    /** Vertex names: letters, digits, '_' or '-', 1 to 20 characters, no spaces. */
    private static final Pattern VERTEX_NAME = Pattern.compile("[A-Za-z0-9_-]{1,20}");

    private final Scanner scanner;

    public InputValidator(InputStream source) {
        this.scanner = new Scanner(source);
    }

    /**
     * Prints the prompt and returns the next line with surrounding spaces removed.
     *
     * @throws InputClosedException if the input stream has ended
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new InputClosedException();
        }
        // Remove an invisible byte-order mark that some terminals add when input is piped in.
        return scanner.nextLine().replace("﻿", "").trim();
    }

    /** Reads any whole number (negative values allowed). */
    public int readInt(String prompt) {
        while (true) {
            String line = readLine(prompt);
            Integer value = parseInteger(line);
            if (value != null) {
                return value;
            }
        }
    }

    /** Reads a whole number between min and max (inclusive). Used for menu choices and sizes. */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            ConsoleFormatter.error("Please enter a number between " + min + " and " + max + ".");
        }
    }

    /** Reads a whole number that is zero or greater. Used for indexes. */
    public int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            ConsoleFormatter.error("Negative numbers are not allowed here. Please enter 0 or more.");
        }
    }

    /** Reads a line that is not empty. */
    public String readNonEmptyString(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            ConsoleFormatter.error("Input cannot be empty.");
        }
    }

    /** Reads a vertex name that matches the allowed vertex-name format. */
    public String readVertexName(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line.isEmpty()) {
                ConsoleFormatter.error("Name cannot be empty.");
            } else if (!VERTEX_NAME.matcher(line).matches()) {
                ConsoleFormatter.error("Invalid name '" + line + "'. Use 1-20 letters, digits, "
                        + "'_' or '-' with no spaces (for example Student01 or IT_Dept).");
            } else {
                return line;
            }
        }
    }

    /** Reads a yes/no answer (y, yes, n, no - not case sensitive). */
    public boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt).toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            ConsoleFormatter.error("Please answer y (yes) or n (no).");
        }
    }

    /** Converts text to an int, printing a friendly error and returning null if it is not valid. */
    private Integer parseInteger(String text) {
        if (text.isEmpty()) {
            ConsoleFormatter.error("Input cannot be empty. Please enter a whole number.");
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            if (text.matches("[+-]?\\d+")) {
                ConsoleFormatter.error("The number is too large. Enter a value between "
                        + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + ".");
            } else {
                ConsoleFormatter.error("'" + text + "' is not a valid whole number.");
            }
            return null;
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}
