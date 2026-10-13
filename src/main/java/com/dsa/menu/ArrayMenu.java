package com.dsa.menu;

import com.dsa.array.ArrayManager;
import com.dsa.model.ApplicationState;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

/**
 * "Student Records": a fixed number of record places holding student IDs.
 * Technically this is the fixed-capacity array (ArrayManager); a position is an array index.
 */
public class ArrayMenu extends BaseMenu {

    private final ArrayManager array;

    public ArrayMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.array = state.getArrayManager();
    }

    @Override
    protected String getTitle() {
        return "STUDENT RECORDS";
    }

    @Override
    protected String getTechnicalNote() {
        return "fixed-capacity array (" + array.capacity() + " places); position = array index";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Add Student Record",
                "Add Student Record at Position",
                "Remove Student Record",
                "Look Up Student Record",
                "View All Student Records",
                "View Record Capacity"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> addRecord();
            case 2 -> addRecordAtPosition();
            case 3 -> removeRecord();
            case 4 -> lookUpRecord();
            case 5 -> viewRecords();
            case 6 -> viewCapacity();
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void addRecord() {
        if (array.isFull()) {
            printFullMessage();
            return;
        }
        int studentId = input.readInt("Enter the student ID: ");
        array.insert(studentId);
        ConsoleFormatter.success("Student ID " + studentId + " added at position " + (array.size() - 1) + ". [O(1)]");
    }

    private void addRecordAtPosition() {
        if (array.isFull()) {
            printFullMessage();
            return;
        }
        int position = input.readNonNegativeInt("Enter the position (0 to " + array.size() + "): ");
        int studentId = input.readInt("Enter the student ID: ");
        if (position > array.size()) {
            printInvalidPosition(position, array.size());
            return;
        }
        array.insertAt(position, studentId);
        ConsoleFormatter.success("Student ID " + studentId + " added at position " + position + "; "
                + array.getLastOperationSteps() + " record(s) moved down one place. [O(n)]");
    }

    private void removeRecord() {
        if (array.isEmpty()) {
            ConsoleFormatter.error("There are no student records to remove.");
            return;
        }
        int mode = input.readIntInRange("Remove by (1) Position or (2) Student ID? ", 1, 2);
        if (mode == 1) {
            int position = input.readNonNegativeInt("Enter the position (0 to " + (array.size() - 1) + "): ");
            if (position >= array.size()) {
                printInvalidPosition(position, array.size() - 1);
                return;
            }
            int removed = array.deleteAt(position);
            ConsoleFormatter.success("Removed student ID " + removed + " from position " + position + "; "
                    + array.getLastOperationSteps() + " record(s) moved up one place. [O(n)]");
        } else {
            int studentId = input.readInt("Enter the student ID to remove: ");
            int position = array.deleteValue(studentId);
            if (position == -1) {
                ConsoleFormatter.error("Student ID " + studentId + " was not found in the student records.");
            } else {
                ConsoleFormatter.success("Removed student ID " + studentId + " (it was at position " + position
                        + "). [O(n)]");
            }
        }
    }

    private void lookUpRecord() {
        if (array.isEmpty()) {
            ConsoleFormatter.info("There are no student records yet, so no student can be found.");
            return;
        }
        int studentId = input.readInt("Enter the student ID to look up: ");
        int position = array.search(studentId);
        int checked = array.getLastOperationSteps();
        if (position == -1) {
            ConsoleFormatter.info("Student ID " + studentId + " was not found (" + checked
                    + " record(s) checked). [O(n)]");
        } else {
            ConsoleFormatter.success("Student ID " + studentId + " found at position " + position + " ("
                    + checked + " record(s) checked). [O(n)]");
        }
    }

    private void viewRecords() {
        if (array.isEmpty()) {
            ConsoleFormatter.info("There are no student records yet (0 of " + array.capacity() + " places used).");
            return;
        }
        System.out.println("Student records (Index = position, Value = student ID):");
        array.display();
    }

    private void viewCapacity() {
        System.out.println("Records stored : " + array.size());
        System.out.println("Total places   : " + array.capacity());
        System.out.println("Free places    : " + (array.capacity() - array.size()));
        System.out.println("Status         : " + (array.isEmpty() ? "empty" : array.isFull() ? "full" : "partially filled"));
    }

    private void printFullMessage() {
        ConsoleFormatter.error("Student records are full (" + array.capacity() + " of " + array.capacity()
                + " places used). Remove a record first.");
    }

    private void printInvalidPosition(int position, int lastValid) {
        ConsoleFormatter.error("Position " + position + " is not available. Choose a position from 0 to "
                + lastValid + ".");
    }
}
