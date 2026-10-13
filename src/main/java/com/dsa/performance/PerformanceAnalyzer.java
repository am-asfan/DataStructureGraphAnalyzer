package com.dsa.performance;

import com.dsa.array.ArrayManager;
import com.dsa.graph.Graph;
import com.dsa.linkedlist.CustomLinkedList;
import com.dsa.model.OperationResult;
import com.dsa.model.SearchResult;
import com.dsa.model.TraversalResult;
import com.dsa.queue.CustomQueue;
import com.dsa.searching.SearchAlgorithms;
import com.dsa.stack.CustomStack;
import com.dsa.util.ConsoleFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the real algorithms and data structure operations and records measured results.
 * Nothing here is hard-coded: every count and time comes from executing the code.
 *
 * Timing approach:
 *   - Searches and traversals are fast, so one run is too short to time reliably. Each is run
 *     WARM_UP_RUNS times first (to let the JVM's JIT compiler optimise the code) and then
 *     REPETITIONS times; the reported time is the average of the times measured inside the
 *     algorithm (System.nanoTime() around the algorithm's main loop).
 *   - Data structure tests time a whole batch of n operations once and report the total.
 *   - Measured times depend on hardware, JVM warm-up, garbage collection and other programs,
 *     so they vary between runs. Operation counts are deterministic and are the fairer comparison.
 *
 * The data structure tests always use NEW structures, so the user's own data is never changed.
 */
public class PerformanceAnalyzer {

    public static final int SEARCH_REPETITIONS = 200;
    public static final int GRAPH_REPETITIONS = 50;
    private static final int WARM_UP_RUNS = 20;

    /**
     * Runs linear search and binary search on the SAME sorted dataset with the SAME target.
     */
    public List<OperationResult> compareSearchAlgorithms(int[] sortedData, int target) {
        if (!SearchAlgorithms.isSorted(sortedData)) {
            throw new IllegalArgumentException("The comparison dataset must be sorted so binary search can use it.");
        }
        String label = "generated dataset";
        SearchResult linear = SearchAlgorithms.linearSearch(sortedData, target, label);
        SearchResult binary = SearchAlgorithms.binarySearch(sortedData, target, label);

        long linearTotal = 0;
        long binaryTotal = 0;
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            SearchAlgorithms.linearSearch(sortedData, target, label);
            SearchAlgorithms.binarySearch(sortedData, target, label);
        }
        for (int i = 0; i < SEARCH_REPETITIONS; i++) {
            linearTotal += SearchAlgorithms.linearSearch(sortedData, target, label).getElapsedNanos();
            binaryTotal += SearchAlgorithms.binarySearch(sortedData, target, label).getElapsedNanos();
        }

        List<OperationResult> rows = new ArrayList<>();
        rows.add(searchRow(linear, linearTotal / SEARCH_REPETITIONS, "O(n)"));
        rows.add(searchRow(binary, binaryTotal / SEARCH_REPETITIONS, "O(log n)"));
        return rows;
    }

    /**
     * Compares both searches for four targets that show best and worst cases:
     * the first element, the middle element, the last element and a missing value.
     */
    public List<OperationResult> compareSearchScenarios(int[] sortedData) {
        if (sortedData.length == 0) {
            throw new IllegalArgumentException("The dataset is empty.");
        }
        int[] targets = {
                sortedData[0],
                sortedData[(sortedData.length - 1) / 2],
                sortedData[sortedData.length - 1],
                sortedData[sortedData.length - 1] + 1 // larger than every value, so it is missing
        };
        List<OperationResult> rows = new ArrayList<>();
        for (int target : targets) {
            rows.addAll(compareSearchAlgorithms(sortedData, target));
        }
        return rows;
    }

    /**
     * Runs BFS and DFS on the SAME graph from the SAME start vertex.
     */
    public List<OperationResult> compareGraphTraversals(Graph graph, String startVertex) {
        TraversalResult bfs = graph.bfs(startVertex);
        TraversalResult dfs = graph.dfs(startVertex);

        long bfsTotal = 0;
        long dfsTotal = 0;
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            graph.bfs(startVertex);
            graph.dfs(startVertex);
        }
        for (int i = 0; i < GRAPH_REPETITIONS; i++) {
            bfsTotal += graph.bfs(startVertex).getElapsedNanos();
            dfsTotal += graph.dfs(startVertex).getElapsedNanos();
        }

        List<OperationResult> rows = new ArrayList<>();
        rows.add(traversalRow(bfs, graph, bfsTotal / GRAPH_REPETITIONS));
        rows.add(traversalRow(dfs, graph, dfsTotal / GRAPH_REPETITIONS));
        return rows;
    }

    /**
     * Performs n operations of each kind on fresh data structures and records the work done.
     * Shows that the same structure can have O(1) or O(n) operations depending on the position used.
     */
    public List<OperationResult> analyzeDataStructureOperations(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be at least 1.");
        }
        List<OperationResult> rows = new ArrayList<>();

        // ---------- Array ----------
        ArrayManager array = new ArrayManager(n);
        long steps = 0;
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            array.insert(i);
            steps += array.getLastOperationSteps();
        }
        rows.add(new OperationResult("Array", "insert at end x " + n, n, steps, "element shifts",
                System.nanoTime() - start, "size " + array.size(), "O(1) each"));

        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            array.deleteAt(array.size() - 1);
            steps += array.getLastOperationSteps();
        }
        rows.add(new OperationResult("Array", "delete last x " + n, n, steps, "element shifts",
                System.nanoTime() - start, "size " + array.size(), "O(1) each"));

        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            array.insertAt(0, i);
            steps += array.getLastOperationSteps();
        }
        rows.add(new OperationResult("Array", "insert at index 0 x " + n, n, steps, "element shifts",
                System.nanoTime() - start, "size " + array.size(), "O(n) each"));

        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            array.deleteAt(0);
            steps += array.getLastOperationSteps();
        }
        rows.add(new OperationResult("Array", "delete at index 0 x " + n, n, steps, "element shifts",
                System.nanoTime() - start, "size " + array.size(), "O(n) each"));

        // ---------- Stack ----------
        CustomStack<Integer> stack = new CustomStack<>(n);
        long operations = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            stack.push(i);
            operations++;
        }
        rows.add(new OperationResult("Stack", "push x " + n, n, operations, "pushes performed",
                System.nanoTime() - start, "size " + stack.size(), "O(1) each"));

        operations = 0;
        start = System.nanoTime();
        while (!stack.isEmpty()) {
            stack.pop();
            operations++;
        }
        rows.add(new OperationResult("Stack", "pop x " + n, n, operations, "pops performed",
                System.nanoTime() - start, "size " + stack.size(), "O(1) each"));

        // ---------- Circular queue ----------
        CustomQueue<Integer> queue = new CustomQueue<>(n);
        operations = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
            operations++;
        }
        rows.add(new OperationResult("Circular Queue", "enqueue x " + n, n, operations, "enqueues performed",
                System.nanoTime() - start, "size " + queue.size(), "O(1) each"));

        operations = 0;
        start = System.nanoTime();
        while (!queue.isEmpty()) {
            queue.dequeue();
            operations++;
        }
        rows.add(new OperationResult("Circular Queue", "dequeue x " + n, n, operations, "dequeues performed",
                System.nanoTime() - start, "size " + queue.size(), "O(1) each"));

        // ---------- Singly linked list ----------
        CustomLinkedList list = new CustomLinkedList();
        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.insertAtBeginning(i);
            steps += list.getLastTraversalSteps();
        }
        rows.add(new OperationResult("Linked List", "insert at beginning x " + n, n, steps, "node traversals",
                System.nanoTime() - start, "size " + list.size(), "O(1) each"));

        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.deleteAt(0);
            steps += list.getLastTraversalSteps();
        }
        rows.add(new OperationResult("Linked List", "delete at index 0 x " + n, n, steps, "node traversals",
                System.nanoTime() - start, "size " + list.size(), "O(1) each"));

        steps = 0;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.insertAtEnd(i);
            steps += list.getLastTraversalSteps();
        }
        rows.add(new OperationResult("Linked List", "insert at end x " + n, n, steps, "node traversals",
                System.nanoTime() - start, "size " + list.size(), "O(n) each (no tail)"));

        start = System.nanoTime();
        int index = list.search(n - 1); // the last value: worst case for a successful search
        rows.add(new OperationResult("Linked List", "search last value", n, list.getLastTraversalSteps(),
                "node traversals", System.nanoTime() - start, "found at " + index, "O(n)"));

        return rows;
    }

    /** Prints a table of measured values, then the theoretical complexities separately. */
    public static void printTable(String title, List<OperationResult> rows, String timeNote) {
        System.out.println();
        System.out.println(title);
        System.out.println("-".repeat(title.length()));
        String format = "%-15s %-26s %8s %12s  %-19s %14s  %s%n";
        System.out.printf(format, "Subject", "Operation", "n", "Count", "Count metric", "Time", "Result");
        for (OperationResult row : rows) {
            System.out.printf(format, row.getSubject(), row.getOperation(), row.getInputSize(),
                    String.format("%,d", row.getOperationCount()), row.getCountMetric(),
                    ConsoleFormatter.formatNanos(row.getElapsedNanos()), row.getResult());
        }
        System.out.println("Time column: " + timeNote);

        System.out.println();
        System.out.println("Expected time complexity (theoretical, not measured):");
        String previous = "";
        for (OperationResult row : rows) {
            String line = "  " + row.getSubject() + " - " + row.getOperation() + ": " + row.getExpectedComplexity();
            if (!line.equals(previous)) {
                System.out.println(line);
            }
            previous = line;
        }

        boolean hasDetails = false;
        for (OperationResult row : rows) {
            if (row.getDetails() != null) {
                if (!hasDetails) {
                    System.out.println();
                    System.out.println("Details:");
                    hasDetails = true;
                }
                System.out.println("  " + row.getSubject() + ": " + row.getDetails());
            }
        }
    }

    /** Explains how to interpret the measured times. */
    public static void printTimingDisclaimer() {
        System.out.println();
        System.out.println("NOTE: Execution times are measured with System.nanoTime() and are affected by hardware,");
        System.out.println("JVM warm-up (JIT compilation), garbage collection and other running programs. They vary");
        System.out.println("between runs, and one measurement does not prove that an algorithm is always faster.");
        System.out.println("Operation counts are deterministic and give a more reliable view of algorithm behaviour.");
    }

    private OperationResult searchRow(SearchResult result, long averageNanos, String complexity) {
        String outcome = result.isFound() ? "found at " + result.getIndex() : "not found";
        return new OperationResult(result.getAlgorithm(), "search for " + result.getTarget(), result.getDatasetSize(),
                result.getComparisons(), "comparisons", averageNanos, outcome, complexity);
    }

    private OperationResult traversalRow(TraversalResult result, Graph graph, long averageNanos) {
        String operation = "from " + result.getStartVertex() + " (E=" + graph.getEdgeCount() + ")";
        String order = result.getVerticesVisited() <= 30
                ? result.getOrderText()
                : String.join(" -> ", result.getOrder().subList(0, 30)) + " -> ... ("
                    + (result.getVerticesVisited() - 30) + " more)";
        return new OperationResult(result.getAlgorithm(), operation, graph.getVertexCount(),
                result.getAdjacencyChecks(), "adjacency checks", averageNanos,
                result.getVerticesVisited() + " visited", "O(V + E)", "order " + order);
    }
}
