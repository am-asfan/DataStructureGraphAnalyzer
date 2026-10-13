package com.dsa.tests;

import com.dsa.graph.Graph;
import com.dsa.model.OperationResult;
import com.dsa.performance.PerformanceAnalyzer;
import com.dsa.util.SampleData;

import java.util.List;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class PerformanceAnalyzerTest {

    private PerformanceAnalyzerTest() {
    }

    public static void run() {
        TestHelper.section("PerformanceAnalyzer");
        PerformanceAnalyzer analyzer = new PerformanceAnalyzer();

        List<OperationResult> search = analyzer.compareSearchAlgorithms(SampleData.generateSortedDataset(1000), 750);
        assertEquals(2, search.size(), "search comparison returns two rows");
        assertEquals(750, search.get(0).getOperationCount(), "linear search comparisons are measured (750)");
        check(search.get(1).getOperationCount() <= 10, "binary search comparisons are measured (<= 10)");
        assertEquals("found at 749", search.get(1).getResult(), "binary search result is reported");
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.compareSearchAlgorithms(new int[]{3, 1, 2}, 1), "comparison rejects unsorted data");

        List<OperationResult> scenarios = analyzer.compareSearchScenarios(SampleData.generateSortedDataset(100));
        assertEquals(8, scenarios.size(), "scenarios give 4 targets x 2 algorithms");
        assertEquals(1, scenarios.get(0).getOperationCount(), "linear search best case (first element) = 1");
        assertEquals(100, scenarios.get(6).getOperationCount(), "linear search worst case (missing) = n");

        Graph graph = new Graph();
        SampleData.loadSampleGraph(graph);
        List<OperationResult> traversal = analyzer.compareGraphTraversals(graph, "A");
        assertEquals("BFS", traversal.get(0).getSubject(), "first traversal row is BFS");
        assertEquals("DFS", traversal.get(1).getSubject(), "second traversal row is DFS");
        assertEquals(12, traversal.get(0).getOperationCount(), "BFS adjacency checks measured on the same graph");
        assertEquals("6 visited", traversal.get(1).getResult(), "DFS vertices visited reported");

        int n = 100;
        List<OperationResult> structures = analyzer.analyzeDataStructureOperations(n);
        assertEquals(0, find(structures, "Array", "insert at end").getOperationCount(), "array insert at end shifts nothing");
        assertEquals((long) n * (n - 1) / 2, find(structures, "Array", "insert at index 0").getOperationCount(),
                "array insert at index 0 shifts n(n-1)/2 elements in total");
        assertEquals((long) n * (n - 1) / 2, find(structures, "Array", "delete at index 0").getOperationCount(),
                "array delete at index 0 shifts n(n-1)/2 elements in total");
        assertEquals(n, find(structures, "Stack", "push").getOperationCount(), "stack performed n pushes");
        assertEquals(n, find(structures, "Circular Queue", "dequeue").getOperationCount(), "queue performed n dequeues");
        assertEquals(0, find(structures, "Linked List", "insert at beginning").getOperationCount(),
                "linked list insert at beginning needs no traversal");
        assertEquals((long) (n - 1) * (n - 2) / 2, find(structures, "Linked List", "insert at end").getOperationCount(),
                "linked list insert at end traverses 0+1+...+(n-2) nodes in total");
        check(structures.stream().allMatch(r -> r.getElapsedNanos() >= 0), "all elapsed times are measured (>= 0)");
    }

    private static OperationResult find(List<OperationResult> rows, String subject, String operationPrefix) {
        for (OperationResult row : rows) {
            if (row.getSubject().equals(subject) && row.getOperation().startsWith(operationPrefix)) {
                return row;
            }
        }
        throw new IllegalStateException("No row for " + subject + " / " + operationPrefix);
    }
}
