package com.dsa;

import com.dsa.menu.ConsoleMenu;
import com.dsa.model.ApplicationState;
import com.dsa.util.InputValidator;

/**
 * Entry point of the University Student Management System.
 *
 * Creates the shared application state and the input helper, then hands control
 * to the main console menu until the user chooses Exit.
 */
public class Main {

    public static void main(String[] args) {
        // try-with-resources closes the Scanner (and System.in) when the application exits.
        try (InputValidator input = new InputValidator(System.in)) {
            ConsoleMenu menu = new ConsoleMenu(input, new ApplicationState());
            menu.run();
        }
    }
}
