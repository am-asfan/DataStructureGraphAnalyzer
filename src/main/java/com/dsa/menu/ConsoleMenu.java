package com.dsa.menu;

import com.dsa.model.ApplicationState;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputClosedException;
import com.dsa.util.InputValidator;

/**
 * The integrated main menu. Every submenu shares the same InputValidator and ApplicationState.
 *
 * Feature names use student-management terminology. Behind each feature is one of the assignment's
 * data structures or algorithms:
 *   Student Records               -> array            (ArrayMenu)
 *   Recent Activities             -> stack            (StackMenu)
 *   Registration Requests         -> circular queue   (QueueMenu)
 *   Student Directory             -> linked list      (LinkedListMenu)
 *   Find a Student                -> linear / binary search (SearchMenu)
 *   University Connection Network -> graph, BFS, DFS  (GraphMenu)
 *   System Performance Report     -> performance analysis (PerformanceMenu)
 */
public class ConsoleMenu {

    private static final int EXIT_OPTION = 9;

    private final InputValidator input;
    private final ArrayMenu arrayMenu;
    private final StackMenu stackMenu;
    private final QueueMenu queueMenu;
    private final LinkedListMenu linkedListMenu;
    private final SearchMenu searchMenu;
    private final GraphMenu graphMenu;
    private final PerformanceMenu performanceMenu;
    private final ResultsSummary resultsSummary;

    public ConsoleMenu(InputValidator input, ApplicationState state) {
        this.input = input;
        this.arrayMenu = new ArrayMenu(input, state);
        this.stackMenu = new StackMenu(input, state);
        this.queueMenu = new QueueMenu(input, state);
        this.linkedListMenu = new LinkedListMenu(input, state);
        this.searchMenu = new SearchMenu(input, state);
        this.graphMenu = new GraphMenu(input, state);
        this.performanceMenu = new PerformanceMenu(input, state);
        this.resultsSummary = new ResultsSummary(state);
    }

    /** Runs the application until the user selects Exit (or the input stream ends). */
    public void run() {
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = input.readIntInRange("Enter your choice: ", 1, EXIT_OPTION);
                System.out.println();
                switch (choice) {
                    case 1 -> arrayMenu.show();
                    case 2 -> stackMenu.show();
                    case 3 -> queueMenu.show();
                    case 4 -> linkedListMenu.show();
                    case 5 -> searchMenu.show();
                    case 6 -> graphMenu.show();
                    case 7 -> performanceMenu.show();
                    case 8 -> resultsSummary.display();
                    default -> running = false; // only 9 (Exit) can reach here because the input is validated
                }
            }
            System.out.println("Thank you for using the University Student Management System. Goodbye!");
        } catch (InputClosedException e) {
            System.out.println();
            ConsoleFormatter.info("No more input is available. Exiting the application.");
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=".repeat(ConsoleFormatter.WIDTH));
        System.out.println(ConsoleFormatter.center("UNIVERSITY STUDENT MANAGEMENT SYSTEM", ConsoleFormatter.WIDTH));
        System.out.println("=".repeat(ConsoleFormatter.WIDTH));
        System.out.println();
        System.out.println("1. Student Records");
        System.out.println("2. Recent Activities");
        System.out.println("3. Registration Requests");
        System.out.println("4. Student Directory");
        System.out.println("5. Find a Student");
        System.out.println("6. University Connection Network");
        System.out.println("7. System Performance Report");
        System.out.println("8. System Overview");
        System.out.println("9. Exit");
        System.out.println();
        System.out.println("=".repeat(ConsoleFormatter.WIDTH));
    }
}
