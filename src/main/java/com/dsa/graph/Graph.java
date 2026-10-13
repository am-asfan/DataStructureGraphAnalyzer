package com.dsa.graph;

import com.dsa.model.TraversalResult;
import com.dsa.queue.CustomQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An undirected, unweighted graph stored as an adjacency list.
 *
 * Storage: Map<vertex, list of neighbours>. A LinkedHashMap keeps vertices in insertion order,
 * so the display and traversal results are predictable and easy to explain.
 *
 * Design decisions:
 *   - Vertex names are case sensitive ("A" and "a" are different vertices) and are trimmed.
 *   - Adding an existing vertex is rejected (addVertex returns false).
 *   - addEdge automatically creates any missing vertex.
 *   - Duplicate edges are ignored (addEdge returns false). Because the graph is undirected,
 *     A-B and B-A are the same edge.
 *   - Self-loops (A-A) are rejected with an IllegalArgumentException.
 *   - Each undirected edge is stored in BOTH vertices' adjacency lists.
 *
 * Traversals:
 *   - BFS uses this project's CustomQueue; DFS is recursive (the call stack acts as the stack).
 *   - Both use a visited set so no vertex is processed twice, even when the graph has cycles.
 *   - A traversal from one start vertex visits only the connected component containing that vertex.
 *     traverseAllComponents() restarts from every unvisited vertex to cover a disconnected graph.
 *   - Complexity: O(V + E) with an adjacency list.
 */
public class Graph {

    private final Map<String, List<String>> adjacencyList = new LinkedHashMap<>();
    private int edgeCount;

    /** Increases on every change; used to detect outdated traversal results. */
    private long modificationCount;

    /**
     * Adds a vertex. O(1) on average.
     *
     * @return true if added, false if the vertex already exists
     */
    public boolean addVertex(String vertex) {
        String name = normalize(vertex);
        if (adjacencyList.containsKey(name)) {
            return false;
        }
        adjacencyList.put(name, new ArrayList<>());
        modificationCount++;
        return true;
    }

    /**
     * Adds an undirected edge between source and destination, creating missing vertices.
     * O(degree) because of the duplicate-edge check.
     *
     * @return true if the edge was added, false if it already existed
     * @throws IllegalArgumentException for a self-loop or an empty vertex name
     */
    public boolean addEdge(String source, String destination) {
        String a = normalize(source);
        String b = normalize(destination);
        if (a.equals(b)) {
            throw new IllegalArgumentException("Self-loops are not supported (" + a + " - " + b + ").");
        }
        addVertex(a);
        addVertex(b);
        if (adjacencyList.get(a).contains(b)) {
            return false; // duplicate edge
        }
        adjacencyList.get(a).add(b);
        adjacencyList.get(b).add(a); // undirected: store the edge in both lists
        edgeCount++;
        modificationCount++;
        return true;
    }

    /** True if the vertex exists. O(1) on average (hash lookup). */
    public boolean searchVertex(String vertex) {
        if (vertex == null) {
            return false;
        }
        return adjacencyList.containsKey(vertex.trim());
    }

    /** Prints every vertex followed by its neighbours. O(V + E). */
    public void displayGraph() {
        if (isEmpty()) {
            System.out.println("The graph is empty. Add a vertex or an edge first.");
            return;
        }
        System.out.println("Adjacency list (vertex : neighbours)");
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String neighbours = entry.getValue().isEmpty() ? "(no neighbours)" : String.join(", ", entry.getValue());
            System.out.println("  " + entry.getKey() + " : " + neighbours);
        }
    }

    /** Breadth-first search from the start vertex (visits its connected component only). */
    public TraversalResult bfs(String startVertex) {
        String start = requireVertex(startVertex);
        long startTime = System.nanoTime();
        List<String> order = new ArrayList<>();
        int checks = bfsFrom(start, new HashSet<>(), order);
        long elapsed = System.nanoTime() - startTime;
        return new TraversalResult("BFS", start, order, checks, elapsed);
    }

    /** Depth-first search from the start vertex (visits its connected component only). */
    public TraversalResult dfs(String startVertex) {
        String start = requireVertex(startVertex);
        long startTime = System.nanoTime();
        List<String> order = new ArrayList<>();
        int checks = dfsVisit(start, new HashSet<>(), order);
        long elapsed = System.nanoTime() - startTime;
        return new TraversalResult("DFS", start, order, checks, elapsed);
    }

    /**
     * Traverses every connected component: starts a BFS (or DFS) from each vertex that has not been
     * visited yet, in insertion order. Returns one TraversalResult per component.
     */
    public List<TraversalResult> traverseAllComponents(boolean breadthFirst) {
        if (isEmpty()) {
            throw new IllegalStateException("The graph is empty. Add vertices first.");
        }
        List<TraversalResult> components = new ArrayList<>();
        Set<String> visited = new HashSet<>(); // shared, so each vertex belongs to exactly one component
        for (String vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                long startTime = System.nanoTime();
                List<String> order = new ArrayList<>();
                int checks = breadthFirst ? bfsFrom(vertex, visited, order) : dfsVisit(vertex, visited, order);
                long elapsed = System.nanoTime() - startTime;
                components.add(new TraversalResult(breadthFirst ? "BFS" : "DFS", vertex, order, checks, elapsed));
            }
        }
        return components;
    }

    /**
     * BFS core. A vertex is marked visited when it is ENQUEUED (not when dequeued), so it can never be
     * in the queue twice; therefore a queue with capacity V is always large enough.
     *
     * @return the number of adjacency-list entries examined
     */
    private int bfsFrom(String start, Set<String> visited, List<String> order) {
        CustomQueue<String> queue = new CustomQueue<>(adjacencyList.size());
        int adjacencyChecks = 0;
        visited.add(start);
        queue.enqueue(start);
        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            order.add(current);
            for (String neighbour : adjacencyList.get(current)) {
                adjacencyChecks++;
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.enqueue(neighbour);
                }
            }
        }
        return adjacencyChecks;
    }

    /**
     * Recursive DFS core: visit the vertex, then fully explore each unvisited neighbour before
     * moving on to the next neighbour.
     *
     * @return the number of adjacency-list entries examined in this part of the traversal
     */
    private int dfsVisit(String vertex, Set<String> visited, List<String> order) {
        visited.add(vertex);
        order.add(vertex);
        int adjacencyChecks = 0;
        for (String neighbour : adjacencyList.get(vertex)) {
            adjacencyChecks++;
            if (!visited.contains(neighbour)) {
                adjacencyChecks += dfsVisit(neighbour, visited, order);
            }
        }
        return adjacencyChecks;
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    /** Number of neighbours of a vertex. */
    public int getDegree(String vertex) {
        return adjacencyList.get(requireVertex(vertex)).size();
    }

    /** Read-only view of a vertex's neighbours. */
    public List<String> getNeighbours(String vertex) {
        return Collections.unmodifiableList(adjacencyList.get(requireVertex(vertex)));
    }

    /** Vertices in insertion order. */
    public List<String> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    public int getConnectedComponentCount() {
        return isEmpty() ? 0 : traverseAllComponents(true).size();
    }

    public long getModificationCount() {
        return modificationCount;
    }

    public void clear() {
        adjacencyList.clear();
        edgeCount = 0;
        modificationCount++;
    }

    /** Checks that the graph is not empty and the vertex exists; returns the trimmed name. */
    private String requireVertex(String vertex) {
        if (isEmpty()) {
            throw new IllegalStateException("The graph is empty. Add vertices first.");
        }
        String name = normalize(vertex);
        if (!adjacencyList.containsKey(name)) {
            throw new IllegalArgumentException("Vertex '" + name + "' does not exist in the graph.");
        }
        return name;
    }

    private String normalize(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            throw new IllegalArgumentException("Vertex name cannot be empty.");
        }
        return vertex.trim();
    }
}
