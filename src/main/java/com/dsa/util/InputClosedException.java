package com.dsa.util;

/**
 * Thrown when the input stream has no more lines (for example, the user pressed
 * Ctrl+D / Ctrl+Z or a scripted input file ended).
 *
 * Without this, a validation loop that keeps re-prompting would spin forever.
 * The main menu catches it and exits the application cleanly.
 */
public class InputClosedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InputClosedException() {
        super("No more input is available.");
    }
}
