package com.dsa.menu;

import com.dsa.graph.Graph;
import com.dsa.model.ApplicationState;
import com.dsa.model.OperationResult;
import com.dsa.model.PerformanceReport;
import com.dsa.performance.PerformanceAnalyzer;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;
import com.dsa.util.SampleData;

import java.util.List;

/**
 * "System Performance Report": runs the PerformanceAnalyzer and prints the measured results.
 * The report tables use technical names (Linear Search, BFS, Array, ...) because they are an
 * educational analysis of the algorithms behind each feature.
 */
public class PerformanceMenu extends BaseMenu {

    private static final int MAX_DATASET_SIZE = 1_000_000;
    private static final int MAX_STRUCTURE_OPERATIONS = 10_000;
    private static final int MAX_BENCHMARK_VERTICES = 2_000; // keeps recursive DFS well within the call-stack limit
    private static final long BENCHMARK_SEED = 42;

    private static final String SEARCH_TIME_NOTE = "average per search over "
            + PerformanceAnalyzer.SEARCH_REPETITIONS + " runs (after warm-up)";
    private static final String GRAPH_TIME_NOTE = "average per exploration over "
            + PerformanceAnalyzer.GRAPH_REPETITIONS + " runs (after warm-up)";
    private static final String STRUCTURE_TIME_NOTE = "total for all n operations (single run)";

    private final PerformanceAnalyzer analyzer = new PerformanceAnalyzer();

    public PerformanceMenu(InputValidator input, ApplicationState state) {
        super(input, state);
    }

    @Override
    protected String getTitle() {
        return "SYSTEM PERFORMANCE REPORT";
    }

    @Override
    protected String getTechnicalNote() {
        return "measures Linear/Binary Search, BFS/DFS and array, stack, queue and linked list operations";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Student Search Speed Test",
                "Search Best and Worst Cases",
                "Network Exploration Test (Current Network)",
                "Network Exploration Test (Generated Network)",
                "Record Operation Costs",
                "Run Full Performance Report"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> searchComparison();
            case 2 -> searchScenarios();
            case 3 -> currentNetworkComparison();
            case 4 -> generatedNetworkComparison();
            case 5 -> recordOperationCosts();
            case 6 -> fullReport();
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void searchComparison() {
        int size = input.readIntInRange("Number of student IDs to generate (1 to " + MAX_DATASET_SIZE + "): ",
                1, MAX_DATASET_SIZE);
        int target = input.readInt("Student ID to search for (generated IDs are 1 to " + size + "): ");
        PerformanceReport report = new PerformanceReport();
        runSearchSection(report, size, target);
        finish(report);
    }

    private void searchScenarios() {
        int size = input.readIntInRange("Number of student IDs to generate (1 to " + MAX_DATASET_SIZE + "): ",
                1, MAX_DATASET_SIZE);
        int[] data = SampleData.generateSortedDataset(size);
        List<OperationResult> rows = analyzer.compareSearchScenarios(data);
        PerformanceReport report = new PerformanceReport();
        String title = "SEARCH BEST AND WORST CASES - Linear vs Binary Search (sorted student IDs 1.." + size + ")";
        PerformanceAnalyzer.printTable(title, rows, SEARCH_TIME_NOTE);
        report.addSection(title, rows);
        finish(report);
    }

    private void currentNetworkComparison() {
        Graph graph = state.getGraph();
        if (graph.isEmpty()) {
            ConsoleFormatter.error("The current network is empty. Build one in University Connection Network "
                    + "(or load the sample network), or use the generated-network option.");
            return;
        }
        System.out.println("Members: " + String.join(", ", graph.getVertices()));
        String start = input.readVertexName("Starting member for both explorations: ");
        if (!graph.searchVertex(start)) {
            ConsoleFormatter.error("Member '" + start + "' is not in the current network.");
            return;
        }
        PerformanceReport report = new PerformanceReport();
        runNetworkSection(report, graph, start, "current network");
        finish(report);
    }

    private void generatedNetworkComparison() {
        int members = input.readIntInRange("Number of members (2 to " + MAX_BENCHMARK_VERTICES + "): ",
                2, MAX_BENCHMARK_VERTICES);
        int extraConnections = input.readIntInRange("Extra random connections (0 to 10000): ", 0, 10_000);
        Graph graph = SampleData.generateBenchmarkGraph(members, extraConnections, BENCHMARK_SEED);
        PerformanceReport report = new PerformanceReport();
        runNetworkSection(report, graph, "V0", "generated network, seed " + BENCHMARK_SEED);
        finish(report);
    }

    private void recordOperationCosts() {
        int n = input.readIntInRange("Number of operations n (1 to " + MAX_STRUCTURE_OPERATIONS + "): ",
                1, MAX_STRUCTURE_OPERATIONS);
        PerformanceReport report = new PerformanceReport();
        runStructureSection(report, n);
        finish(report);
    }

    /** Runs every comparison with fixed, documented parameters. */
    private void fullReport() {
        System.out.println();
        ConsoleFormatter.printBanner("SYSTEM PERFORMANCE REPORT");
        PerformanceReport report = new PerformanceReport();
        runSearchSection(report, 1000, 750);

        Graph graph = state.getGraph();
        if (graph.isEmpty()) {
            Graph generated = SampleData.generateBenchmarkGraph(200, 300, BENCHMARK_SEED);
            runNetworkSection(report, generated, "V0", "generated network, 200 members, seed " + BENCHMARK_SEED);
        } else {
            String start = graph.getVertices().get(0);
            runNetworkSection(report, graph, start, "current network, starting at first member " + start);
        }

        runStructureSection(report, 1000);
        System.out.println("=".repeat(ConsoleFormatter.WIDTH));
        finish(report);
    }

    private void runSearchSection(PerformanceReport report, int size, int target) {
        int[] data = SampleData.generateSortedDataset(size);
        List<OperationResult> rows = analyzer.compareSearchAlgorithms(data, target);
        String title = "STUDENT SEARCH COMPARISON - Linear vs Binary Search (sorted student IDs 1.." + size
                + ", target " + target + ")";
        PerformanceAnalyzer.printTable(title, rows, SEARCH_TIME_NOTE);
        report.addSection(title, rows);
    }

    private void runNetworkSection(PerformanceReport report, Graph graph, String start, String description) {
        List<OperationResult> rows = analyzer.compareGraphTraversals(graph, start);
        String title = "NETWORK EXPLORATION COMPARISON - BFS vs DFS (" + description + ": V=" + graph.getVertexCount()
                + ", E=" + graph.getEdgeCount() + ")";
        PerformanceAnalyzer.printTable(title, rows, GRAPH_TIME_NOTE);
        System.out.println("Metric definitions: 'visited' = members reached from the starting member;");
        System.out.println("'adjacency checks' = connection entries examined (2 per connection in the starting member's group).");
        report.addSection(title, rows);
    }

    private void runStructureSection(PerformanceReport report, int n) {
        List<OperationResult> rows = analyzer.analyzeDataStructureOperations(n);
        String title = "RECORD OPERATION COSTS (n = " + n + ", fresh structures; your data is not changed)";
        PerformanceAnalyzer.printTable(title, rows, STRUCTURE_TIME_NOTE);
        System.out.println("Feature mapping: Student Records = Array, Recent Activities = Stack,");
        System.out.println("Registration Requests = Circular Queue, Student Directory = Linked List.");
        System.out.println("Shifts/traversal steps show why position matters: adding or removing at the front of an");
        System.out.println("array shifts records (O(n) each), while a linked list changes only the head (O(1)).");
        report.addSection(title, rows);
    }

    private void finish(PerformanceReport report) {
        PerformanceAnalyzer.printTimingDisclaimer();
        state.recordPerformanceReport(report);
    }
}
