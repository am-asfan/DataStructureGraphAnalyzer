package com.dsa.util;

import com.dsa.graph.Graph;

import java.util.Random;

/**
 * Builds sample and benchmark data used for demonstrations and performance comparisons.
 */
public final class SampleData {

    private SampleData() {
        // Utility class.
    }

    /**
     * Replaces the graph's contents with a small demonstration graph:
     *
     *     A --- B          G --- H
     *     |     |
     *     C --- D --- E --- F
     *
     * It contains a cycle (A-B-D-C-A) and two connected components ({A..F} and {G, H}).
     * Expected traversals from A:  BFS = A, B, C, D, E, F   DFS = A, B, D, C, E, F
     */
    public static void loadSampleGraph(Graph graph) {
        graph.clear();
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");
        graph.addEdge("D", "E");
        graph.addEdge("E", "F");
        graph.addEdge("G", "H");
    }

    /**
     * Creates a connected benchmark graph with vertices V0..V(n-1).
     * A path V0-V1-...-V(n-1) guarantees connectivity; extra random edges are then added.
     * A fixed seed makes the graph identical every time for the same parameters.
     */
    public static Graph generateBenchmarkGraph(int vertexCount, int extraEdges, long seed) {
        if (vertexCount < 2) {
            throw new IllegalArgumentException("A benchmark graph needs at least 2 vertices.");
        }
        Graph graph = new Graph();
        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex("V" + i);
        }
        for (int i = 0; i < vertexCount - 1; i++) {
            graph.addEdge("V" + i, "V" + (i + 1));
        }
        long maxPossibleEdges = (long) vertexCount * (vertexCount - 1) / 2;
        long target = Math.min(maxPossibleEdges, graph.getEdgeCount() + (long) extraEdges);
        Random random = new Random(seed);
        int attempts = 0;
        // The attempt limit stops the loop if random picks keep hitting existing edges.
        while (graph.getEdgeCount() < target && attempts < extraEdges * 20 + 100) {
            int a = random.nextInt(vertexCount);
            int b = random.nextInt(vertexCount);
            if (a != b) {
                graph.addEdge("V" + a, "V" + b); // returns false for duplicates, which is fine
            }
            attempts++;
        }
        return graph;
    }

    /** Returns the sorted dataset 1, 2, 3, ..., size (value v is stored at index v - 1). */
    public static int[] generateSortedDataset(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Dataset size must be at least 1.");
        }
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i + 1;
        }
        return data;
    }
}
