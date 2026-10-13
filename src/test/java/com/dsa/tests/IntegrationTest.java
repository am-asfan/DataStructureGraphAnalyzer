package com.dsa.tests;

import com.dsa.menu.ConsoleMenu;
import com.dsa.menu.ResultsSummary;
import com.dsa.model.ApplicationState;
import com.dsa.model.SearchResult;
import com.dsa.searching.SearchAlgorithms;
import com.dsa.util.InputValidator;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.dsa.tests.TestHelper.check;

/**
 * Drives the real ConsoleMenu with scripted keyboard input and checks the printed output.
 */
public final class IntegrationTest {

    private IntegrationTest() {
    }

    public static void run() {
        TestHelper.section("Integration (scripted menu session)");

        String script = String.join("\n",
                "abc", "", "-1", "99",           // invalid main-menu input
                "1",                              // Student Records
                "1", "5",                         //   insert 5
                "2", "0", "3",                    //   insert 3 at index 0
                "2", "9", "1",                    //   invalid index 9 -> error
                "3", "2", "42",                   //   delete missing value 42 -> error
                "5",                              //   display
                "7",                              //   return
                "2",                              // Recent Activities
                "1", "10", "3", "2", "2",         //   push 10, peek, pop, pop (empty -> error)
                "7",
                "3",                              // Registration Requests
                "1", "1", "1", "2", "2", "2", "2", //  enqueue 1, 2; dequeue x3 (third -> error)
                "7",
                "4",                              // Student Directory
                "1", "7", "2", "8", "5", "5",     //   insert 7 at start, 8 at end, deleteAt 5 -> error
                "7", "9",
                "5",                              // Find a Student
                "1", "5", "2", "5", "3", "3",     //   linear 5, binary 5, compare 3
                "4",
                "6",                              // University Connection Network
                "5",                              //   BFS on empty graph -> error
                "9",                              //   load sample graph
                "5", "A", "6", "A", "5", "Q",     //   BFS A, DFS A, BFS from missing vertex
                "2", "A", "A",                    //   self-loop -> error
                "8", "1", "7",                    //   all components, info
                "10",
                "7",                              // System Performance Report
                "1", "1000", "750",               //   search comparison
                "3", "A",                         //   BFS vs DFS on current graph
                "5", "50",                        //   data structure operations
                "7",
                "8",                              // System Overview
                "1", "1", "4", "7",               // change the array after the search
                "8",                              // results should now be labelled PREVIOUS
                "9") + "\n";                      // Exit

        String output = runSession(script);
        check(output.contains("'abc' is not a valid whole number"), "main menu rejects letters");
        check(output.contains("Please enter a number between 1 and 9"), "main menu rejects out-of-range choices");
        check(output.contains("UNIVERSITY STUDENT MANAGEMENT SYSTEM"), "main menu banner shown");
        check(output.contains("STUDENT RECORDS") && output.contains("RECENT ACTIVITIES")
                && output.contains("REGISTRATION REQUESTS") && output.contains("STUDENT DIRECTORY")
                && output.contains("FIND A STUDENT") && output.contains("UNIVERSITY CONNECTION NETWORK")
                && output.contains("SYSTEM PERFORMANCE REPORT"), "every submenu was opened");
        check(output.contains("Technical view: fixed-capacity array"), "technical view caption shown");
        check(output.contains("Position 9 is not available"), "invalid array position reported");
        check(output.contains("Student ID 42 was not found in the student records"), "removing a missing student ID reported");
        check(output.contains("(stack underflow)"), "undo on empty activity log reported");
        check(output.contains("(queue underflow)"), "processing an empty request queue reported");
        check(output.contains("Position 5 is not available"), "invalid directory position reported");
        check(output.contains("Student records (stored order, unchanged): [3, 5]"), "binary search uses a sorted copy");
        check(output.contains("The network is empty"), "exploring an empty network reported");
        check(output.contains("Visit order         : A -> B -> C -> D -> E -> F"), "BFS order printed");
        check(output.contains("Visit order         : A -> B -> D -> C -> E -> F"), "DFS order printed");
        check(output.contains("Member 'Q' is not in the network"), "invalid starting member reported");
        check(output.contains("cannot be connected to themselves"), "self-loop rejected");
        check(output.contains("Found 2 connected group(s)"), "all connected groups explored");
        check(output.contains("STUDENT SEARCH COMPARISON") && output.contains("NETWORK EXPLORATION COMPARISON")
                && output.contains("RECORD OPERATION COSTS"), "performance tables printed");
        check(output.contains("SYSTEM OVERVIEW"), "system overview displayed");
        check(output.contains("PREVIOUS RESULT - the student records have changed"), "outdated search result labelled as previous");
        check(output.contains("Goodbye"), "application exits normally through option 9");

        String truncated = runSession("1\n1\n");
        check(truncated.contains("No more input is available"), "ending input early exits cleanly without a crash");

        // Summary on a fresh state must not crash and must say nothing has been run yet.
        ApplicationState fresh = new ApplicationState();
        String summary = TestHelper.captureOutput(() -> new ResultsSummary(fresh).display());
        check(summary.contains("No student search has been performed yet"), "summary handles no searches");
        check(summary.contains("Level by level (BFS): no exploration has been performed yet"), "summary handles no explorations");
        check(summary.contains("The network is empty"), "summary handles an empty network");

        fresh.getArrayManager().insert(1);
        SearchResult result = SearchAlgorithms.linearSearch(fresh.getArrayManager().toArray(), 1);
        fresh.recordSearchResults(List.of(result));
        check(fresh.isSearchResultCurrent(), "search result is current right after the search");
        fresh.getArrayManager().insert(2);
        check(!fresh.isSearchResultCurrent(), "search result becomes previous after the array changes");
    }

    private static String runSession(String script) {
        return TestHelper.captureOutput(() -> {
            InputValidator input = new InputValidator(
                    new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
            new ConsoleMenu(input, new ApplicationState()).run();
        });
    }
}
