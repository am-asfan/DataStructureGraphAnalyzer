package com.dsa.model;

import com.dsa.util.ConsoleFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The outcome of one graph traversal (BFS or DFS).
 *
 * Metric definitions (used to compare BFS and DFS fairly):
 *   verticesVisited  - number of vertices added to the traversal order
 *   adjacencyChecks  - number of adjacency-list entries examined (one per neighbour looked at).
 *                      In an undirected graph each edge appears in two lists, so a full traversal
 *                      of a component examines 2 x (edges in that component) entries.
 */
public class TraversalResult {

    private final String algorithm;
    private final String startVertex;
    private final List<String> order;
    private final int adjacencyChecks;
    private final long elapsedNanos;

    public TraversalResult(String algorithm, String startVertex, List<String> order,
                           int adjacencyChecks, long elapsedNanos) {
        this.algorithm = algorithm;
        this.startVertex = startVertex;
        this.order = Collections.unmodifiableList(new ArrayList<>(order));
        this.adjacencyChecks = adjacencyChecks;
        this.elapsedNanos = elapsedNanos;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getStartVertex() {
        return startVertex;
    }

    public List<String> getOrder() {
        return order;
    }

    public int getVerticesVisited() {
        return order.size();
    }

    public int getAdjacencyChecks() {
        return adjacencyChecks;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    /** Returns the traversal order as "A -> B -> C". */
    public String getOrderText() {
        return String.join(" -> ", order);
    }

    public String getSummary() {
        return algorithm + " from " + startVertex + ": " + getOrderText()
                + " (" + getVerticesVisited() + " vertices visited, " + adjacencyChecks
                + " adjacency checks, " + ConsoleFormatter.formatNanos(elapsedNanos) + ")";
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
