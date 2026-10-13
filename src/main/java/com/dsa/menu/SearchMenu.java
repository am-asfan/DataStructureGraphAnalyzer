package com.dsa.menu;

import com.dsa.array.ArrayManager;
import com.dsa.model.ApplicationState;
import com.dsa.model.SearchResult;
import com.dsa.searching.SearchAlgorithms;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

import java.util.Arrays;
import java.util.List;

/**
 * "Find a Student": searches the Student Records for a student ID.
 *
 * Option 1 is linear search on the records in their stored order. Option 2 is binary search, which
 * needs sorted data, so it runs on a sorted COPY of the student IDs; the stored order never changes.
 */
public class SearchMenu extends BaseMenu {

    private static final String STORED_LABEL = "student records";
    private static final String SORTED_LABEL = "sorted copy";

    private final ArrayManager array;

    public SearchMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.array = state.getArrayManager();
    }

    @Override
    protected String getTitle() {
        return "FIND A STUDENT";
    }

    @Override
    protected String getTechnicalNote() {
        return "option 1 = Linear Search, option 2 = Binary Search on a sorted copy";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Search Records in Stored Order",
                "Search Sorted Student IDs",
                "Compare Both Search Methods"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        if (array.isEmpty()) {
            ConsoleFormatter.error("There are no student records to search. Add students in Student Records "
                    + "(main menu option 1) first.");
            return;
        }
        switch (choice) {
            case 1 -> linearSearch();
            case 2 -> binarySearch();
            case 3 -> compareBoth();
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void linearSearch() {
        int[] records = array.toArray();
        System.out.println("Student records (stored order): " + Arrays.toString(records));
        int target = input.readInt("Enter the student ID to find: ");
        SearchResult result = SearchAlgorithms.linearSearch(records, target, STORED_LABEL);
        printResult(result, "O(1) best, O(n) average and worst");
        state.recordSearchResults(List.of(result));
    }

    private void binarySearch() {
        int[] records = array.toArray();
        int[] sorted = SearchAlgorithms.createSortedCopy(records);
        System.out.println("Student records (stored order, unchanged): " + Arrays.toString(records));
        System.out.println("Sorted copy of student IDs used for this search: " + Arrays.toString(sorted));
        int target = input.readInt("Enter the student ID to find: ");
        SearchResult result = SearchAlgorithms.binarySearch(sorted, target, SORTED_LABEL);
        printResult(result, "O(1) best, O(log n) average and worst");
        state.recordSearchResults(List.of(result));
    }

    private void compareBoth() {
        int[] records = array.toArray();
        int[] sorted = SearchAlgorithms.createSortedCopy(records);
        System.out.println("Student records (stored order): " + Arrays.toString(records));
        System.out.println("Sorted copy of student IDs    : " + Arrays.toString(sorted));
        int target = input.readInt("Enter the student ID to find: ");

        SearchResult linear = SearchAlgorithms.linearSearch(records, target, STORED_LABEL);
        SearchResult binary = SearchAlgorithms.binarySearch(sorted, target, SORTED_LABEL);

        System.out.println();
        String format = "%-15s %-8s %-16s %-12s %14s  %s%n";
        System.out.printf(format, "Search method", "Found", "Position", "Comparisons", "Time taken", "Complexity (worst)");
        printRow(format, linear, "O(n)");
        printRow(format, binary, "O(log n)");
        System.out.println();
        System.out.println("Why the results can differ:");
        System.out.println(" - Linear search checks the records one by one in their STORED order, so its position");
        System.out.println("   refers to the student records. Binary search returns a position in the SORTED COPY.");
        System.out.println(" - Binary search halves the search range each step, so it needs at most about");
        System.out.println("   log2(n) + 1 comparisons, while linear search may need n comparisons.");
        System.out.println(" - If a student ID appears more than once, the two methods may find different copies.");
        System.out.println(" - Sorting the copy costs O(n log n) and is NOT included in binary search's time.");
        System.out.println(" - Times vary between runs (JVM warm-up, hardware, other programs); for a small number");
        System.out.println("   of records they are dominated by timer overhead. Comparison counts are a more");
        System.out.println("   stable way to study algorithm behaviour.");
        state.recordSearchResults(List.of(linear, binary));
    }

    private void printRow(String format, SearchResult result, String complexity) {
        String label = result.getDatasetLabel().equals(STORED_LABEL) ? "stored" : "sorted";
        String position = result.isFound() ? result.getIndex() + " (" + label + ")" : "-1";
        System.out.printf(format, result.getAlgorithm(), result.isFound() ? "Yes" : "No", position,
                result.getComparisons(), ConsoleFormatter.formatNanos(result.getElapsedNanos()), complexity);
    }

    private void printResult(SearchResult result, String complexity) {
        System.out.println();
        if (result.isFound()) {
            ConsoleFormatter.success("Student ID " + result.getTarget() + " found at position " + result.getIndex()
                    + " of the " + result.getDatasetLabel() + ".");
        } else {
            ConsoleFormatter.info("Student ID " + result.getTarget() + " was not found (returned -1).");
        }
        System.out.println("Search method    : " + result.getAlgorithm());
        System.out.println("Records searched : " + result.getDatasetSize());
        System.out.println("Comparisons      : " + result.getComparisons());
        System.out.println("Time taken       : " + ConsoleFormatter.formatNanos(result.getElapsedNanos())
                + " (varies between runs)");
        System.out.println("Complexity       : " + complexity);
    }
}
