package com.dsa.menu;

import com.dsa.array.ArrayManager;
import com.dsa.graph.Graph;
import com.dsa.linkedlist.CustomLinkedList;
import com.dsa.model.ApplicationState;
import com.dsa.model.OperationResult;
import com.dsa.model.PerformanceReport;
import com.dsa.model.SearchResult;
import com.dsa.model.TraversalResult;
import com.dsa.queue.CustomQueue;
import com.dsa.stack.CustomStack;
import com.dsa.util.ConsoleFormatter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Main menu option 8, "System Overview": the current state of every feature and the most recent results.
 *
 * Results that were computed before the related data changed are labelled as PREVIOUS results,
 * so they are never presented as if they described the current data.
 */
public class ResultsSummary {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ApplicationState state;

    public ResultsSummary(ApplicationState state) {
        this.state = state;
    }

    public void display() {
        ConsoleFormatter.printBanner("SYSTEM OVERVIEW");
        displayStudentServices();
        displayNetwork();
        displaySearchResults();
        displayExplorationResults();
        displayPerformanceReport();
        System.out.println("=".repeat(ConsoleFormatter.WIDTH));
    }

    private void displayStudentServices() {
        System.out.println();
        System.out.println("STUDENT RECORDS AND SERVICES");
        ArrayManager array = state.getArrayManager();
        System.out.println("  Student records       : " + (array.isEmpty() ? "none" : array.toString())
                + "  (" + array.size() + " of " + array.capacity() + " places used)");
        CustomStack<Integer> stack = state.getStack();
        System.out.println("  Recent activities     : " + (stack.isEmpty() ? "none" : "oldest -> latest " + stack)
                + "  (" + stack.size() + " of " + stack.capacity() + ")");
        CustomQueue<Integer> queue = state.getQueue();
        System.out.println("  Registration requests : " + (queue.isEmpty() ? "none" : "next -> last " + queue)
                + "  (" + queue.size() + " of " + queue.capacity() + ")");
        CustomLinkedList list = state.getLinkedList();
        System.out.println("  Student directory     : " + (list.isEmpty() ? "empty" : list.toString())
                + "  (" + list.size() + " student(s))");
    }

    private void displayNetwork() {
        Graph graph = state.getGraph();
        System.out.println();
        System.out.println("UNIVERSITY CONNECTION NETWORK");
        if (graph.isEmpty()) {
            System.out.println("  The network is empty.");
            return;
        }
        System.out.println("  Members (" + graph.getVertexCount() + "): " + String.join(", ", graph.getVertices()));
        System.out.println("  Connections: " + graph.getEdgeCount() + ", connected groups: "
                + graph.getConnectedComponentCount());
    }

    private void displaySearchResults() {
        System.out.println();
        System.out.println("LATEST STUDENT SEARCH");
        List<SearchResult> results = state.getLastSearchResults();
        if (results.isEmpty()) {
            System.out.println("  No student search has been performed yet.");
            return;
        }
        if (!state.isSearchResultCurrent()) {
            System.out.println("  PREVIOUS RESULT - the student records have changed since this search was run:");
        }
        for (SearchResult result : results) {
            System.out.println("  " + result.getSummary());
        }
    }

    private void displayExplorationResults() {
        System.out.println();
        System.out.println("LATEST NETWORK EXPLORATION");
        printExploration("Level by level (BFS)", state.getLastBfsResult(), state.isBfsResultCurrent());
        printExploration("Path by path (DFS)", state.getLastDfsResult(), state.isDfsResultCurrent());
    }

    private void printExploration(String name, TraversalResult result, boolean current) {
        if (result == null) {
            System.out.println("  " + name + ": no exploration has been performed yet.");
        } else if (current) {
            System.out.println("  " + result.getSummary());
        } else {
            System.out.println("  PREVIOUS RESULT (the network has changed since): " + result.getSummary());
        }
    }

    private void displayPerformanceReport() {
        System.out.println();
        System.out.println("LATEST PERFORMANCE REPORT");
        PerformanceReport report = state.getLastPerformanceReport();
        if (report == null || report.isEmpty()) {
            System.out.println("  No performance report has been run yet.");
            return;
        }
        System.out.println("  Run at " + report.getCreatedAt().format(TIME_FORMAT)
                + " on generated or snapshot data (times vary between runs).");
        for (Map.Entry<String, List<OperationResult>> section : report.getSections().entrySet()) {
            System.out.println("  " + section.getKey());
            for (OperationResult row : section.getValue()) {
                System.out.printf("    %-15s %-26s %,12d %-19s %14s  %s%n", row.getSubject(), row.getOperation(),
                        row.getOperationCount(), row.getCountMetric(),
                        ConsoleFormatter.formatNanos(row.getElapsedNanos()), row.getResult());
            }
        }
    }
}
