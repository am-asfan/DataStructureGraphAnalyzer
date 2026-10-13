package com.dsa.menu;

import com.dsa.linkedlist.CustomLinkedList;
import com.dsa.model.ApplicationState;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

/**
 * "Student Directory": an ordered directory of student IDs where each entry links to the next.
 * Technically this is the singly linked list (CustomLinkedList); a position is a node index.
 */
public class LinkedListMenu extends BaseMenu {

    private final CustomLinkedList list;

    public LinkedListMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.list = state.getLinkedList();
    }

    @Override
    protected String getTitle() {
        return "STUDENT DIRECTORY";
    }

    @Override
    protected String getTechnicalNote() {
        return "singly linked list (each entry is a node that links to the next)";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Add Student to Start of Directory",
                "Add Student to End of Directory",
                "Add Student at Position",
                "Remove Student by ID",
                "Remove Student at Position",
                "Find Student in Directory",
                "View Directory",
                "Count Students in Directory"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> addToStart();
            case 2 -> addToEnd();
            case 3 -> addAtPosition();
            case 4 -> removeById();
            case 5 -> removeAtPosition();
            case 6 -> findStudent();
            case 7 -> viewDirectory();
            case 8 -> ConsoleFormatter.info("The directory lists " + list.countNodes()
                    + " student(s) (counted entry by entry from the start). [O(n)]");
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void addToStart() {
        int studentId = input.readInt("Enter the student ID: ");
        list.insertAtBeginning(studentId);
        ConsoleFormatter.success("Student ID " + studentId + " added to the start of the directory. [O(1)]");
        System.out.println("Directory: " + list);
    }

    private void addToEnd() {
        int studentId = input.readInt("Enter the student ID: ");
        list.insertAtEnd(studentId);
        ConsoleFormatter.success("Student ID " + studentId + " added to the end of the directory ("
                + list.getLastTraversalSteps() + " entries passed). [O(n)]");
        System.out.println("Directory: " + list);
    }

    private void addAtPosition() {
        int position = input.readNonNegativeInt("Enter the position (0 to " + list.size() + "): ");
        int studentId = input.readInt("Enter the student ID: ");
        if (position > list.size()) {
            printInvalidPosition(position, list.size());
            return;
        }
        list.insertAt(position, studentId);
        ConsoleFormatter.success("Student ID " + studentId + " added at position " + position + ". [O(n)]");
        System.out.println("Directory: " + list);
    }

    private void removeById() {
        if (list.isEmpty()) {
            ConsoleFormatter.error("The student directory is empty. There is no one to remove.");
            return;
        }
        int studentId = input.readInt("Enter the student ID to remove: ");
        if (list.deleteByValue(studentId)) {
            ConsoleFormatter.success("Student ID " + studentId + " removed from the directory. [O(n)]");
            System.out.println("Directory: " + list);
        } else {
            ConsoleFormatter.error("Student ID " + studentId + " is not in the directory.");
        }
    }

    private void removeAtPosition() {
        if (list.isEmpty()) {
            ConsoleFormatter.error("The student directory is empty. There is no one to remove.");
            return;
        }
        int position = input.readNonNegativeInt("Enter the position (0 to " + (list.size() - 1) + "): ");
        if (position >= list.size()) {
            printInvalidPosition(position, list.size() - 1);
            return;
        }
        int removed = list.deleteAt(position);
        ConsoleFormatter.success("Student ID " + removed + " removed from position " + position
                + ". [O(n), O(1) for position 0]");
        System.out.println("Directory: " + list);
    }

    private void findStudent() {
        if (list.isEmpty()) {
            ConsoleFormatter.info("The student directory is empty, so no student can be found.");
            return;
        }
        int studentId = input.readInt("Enter the student ID to find: ");
        int position = list.search(studentId);
        if (position == -1) {
            ConsoleFormatter.info("Student ID " + studentId + " is not in the directory. [O(n)]");
        } else {
            ConsoleFormatter.success("Student ID " + studentId + " is at position " + position
                    + " in the directory. [O(n)]");
        }
    }

    private void viewDirectory() {
        if (list.isEmpty()) {
            ConsoleFormatter.info("The student directory is empty.");
            return;
        }
        System.out.println("Student directory (student IDs in linked order, HEAD = first entry):");
        list.display();
    }

    private void printInvalidPosition(int position, int lastValid) {
        ConsoleFormatter.error("Position " + position + " is not available. Choose a position from 0 to "
                + lastValid + ".");
    }
}
