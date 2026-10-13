package com.dsa.menu;

import com.dsa.model.ApplicationState;
import com.dsa.stack.CustomStack;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

/**
 * "Recent Activities": a log of the student IDs involved in recent activities, where the latest
 * activity is always handled first. Technically this is the array-based stack (CustomStack, LIFO).
 */
public class StackMenu extends BaseMenu {

    private final CustomStack<Integer> stack;

    public StackMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.stack = state.getStack();
    }

    @Override
    protected String getTitle() {
        return "RECENT ACTIVITIES";
    }

    @Override
    protected String getTechnicalNote() {
        return "array-based stack (LIFO - the latest activity is on top)";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Log New Activity",
                "Undo Latest Activity",
                "View Latest Activity",
                "View Activity History",
                "Check if Activity Log is Empty",
                "Check if Activity Log is Full"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> logActivity();
            case 2 -> undoLatestActivity();
            case 3 -> viewLatestActivity();
            case 4 -> viewHistory();
            case 5 -> ConsoleFormatter.info(stack.isEmpty()
                    ? "The activity log is empty."
                    : "The activity log contains " + stack.size() + " activity(ies).");
            case 6 -> ConsoleFormatter.info(stack.isFull()
                    ? "The activity log is full (" + stack.capacity() + " of " + stack.capacity() + ")."
                    : "The activity log has room for " + (stack.capacity() - stack.size()) + " more activity(ies).");
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void logActivity() {
        if (stack.isFull()) {
            ConsoleFormatter.error("The activity log is full (" + stack.capacity()
                    + " activities). Undo an activity first. (stack overflow)");
            return;
        }
        int studentId = input.readInt("Enter the student ID for this activity: ");
        stack.push(studentId);
        ConsoleFormatter.success("Activity for student ID " + studentId + " logged. [O(1)]");
    }

    private void undoLatestActivity() {
        if (stack.isEmpty()) {
            ConsoleFormatter.error("There are no recent activities to undo. (stack underflow)");
            return;
        }
        ConsoleFormatter.success("Undid the latest activity (student ID " + stack.pop() + "). [O(1)]");
    }

    private void viewLatestActivity() {
        if (stack.isEmpty()) {
            ConsoleFormatter.error("There are no recent activities. (empty stack)");
            return;
        }
        ConsoleFormatter.success("Latest activity: student ID " + stack.peek() + ". [O(1)]");
    }

    private void viewHistory() {
        if (stack.isEmpty()) {
            ConsoleFormatter.info("No recent activities have been logged (0 of " + stack.capacity() + ").");
            return;
        }
        System.out.println("Activity history (latest activity at the top):");
        stack.display();
    }
}
