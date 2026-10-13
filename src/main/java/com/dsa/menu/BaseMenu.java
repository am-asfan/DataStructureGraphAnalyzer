package com.dsa.menu;

import com.dsa.model.ApplicationState;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

/**
 * Common behaviour for every submenu (template method pattern).
 *
 * show() contains the loop that every submenu needs: print the options, read a validated choice,
 * run it, and report errors. Subclasses only provide their title, option labels and the code for
 * each option. A final "Return to Main Menu" option is added automatically.
 *
 * Data structures report problems (full, empty, invalid index, unknown vertex) by throwing
 * IllegalStateException, IllegalArgumentException or IndexOutOfBoundsException. They are caught
 * here and shown as friendly error messages, so the application keeps running.
 */
public abstract class BaseMenu {

    protected final InputValidator input;
    protected final ApplicationState state;

    protected BaseMenu(InputValidator input, ApplicationState state) {
        this.input = input;
        this.state = state;
    }

    /** Title shown above the options, e.g. "RECENT ACTIVITIES". */
    protected abstract String getTitle();

    /**
     * Short note naming the data structure or algorithm behind this feature, printed under the title.
     * Menu options use student-management wording; this line keeps the technical concept visible.
     */
    protected abstract String getTechnicalNote();

    /** Option labels, NOT including the final "Return to Main Menu" option. */
    protected abstract String[] getOptions();

    /** Runs the option with the given number (1 to getOptions().length). */
    protected abstract void handleChoice(int choice);

    /** Shows this submenu until the user chooses "Return to Main Menu". */
    public final void show() {
        String[] options = getOptions();
        int returnOption = options.length + 1;
        while (true) {
            System.out.println();
            ConsoleFormatter.printSectionTitle(getTitle());
            System.out.println("Technical view: " + getTechnicalNote());
            System.out.println();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.println(returnOption + ". Return to Main Menu");
            System.out.println();
            int choice = input.readIntInRange("Enter your choice: ", 1, returnOption);
            if (choice == returnOption) {
                ConsoleFormatter.info("Returning to the main menu.");
                return;
            }
            System.out.println();
            try {
                handleChoice(choice);
            } catch (IllegalStateException | IllegalArgumentException | IndexOutOfBoundsException e) {
                ConsoleFormatter.error(e.getMessage());
            }
        }
    }
}
