package com.dsa.tests;

import com.dsa.graph.Graph;
import com.dsa.model.TraversalResult;
import com.dsa.util.SampleData;

import java.util.HashSet;
import java.util.List;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class GraphTest {

    private GraphTest() {
    }

    public static void run() {
        TestHelper.section("Graph, BFS and DFS");

        Graph graph = new Graph();
        check(graph.isEmpty(), "new graph is empty");
        String emptyDisplay = TestHelper.captureOutput(graph::displayGraph);
        check(emptyDisplay.contains("The graph is empty"), "displaying an empty graph prints a clear message");
        assertThrows(IllegalStateException.class, () -> graph.bfs("A"), "BFS on an empty graph is rejected");
        assertThrows(IllegalStateException.class, () -> graph.dfs("A"), "DFS on an empty graph is rejected");

        check(graph.addVertex("A"), "add a vertex");
        check(!graph.addVertex("A"), "adding a duplicate vertex returns false");
        assertEquals(1, graph.getVertexCount(), "duplicate vertex not added twice");

        check(graph.addEdge("A", "B"), "add an edge (creates missing vertex B)");
        check(graph.searchVertex("B"), "vertex B was created automatically");
        check(!graph.addEdge("A", "B"), "duplicate edge A-B returns false");
        check(!graph.addEdge("B", "A"), "reverse duplicate B-A returns false (undirected)");
        assertEquals(1, graph.getEdgeCount(), "duplicate edges are not counted");
        assertEquals(List.of("B"), graph.getNeighbours("A"), "A's list contains B");
        assertEquals(List.of("A"), graph.getNeighbours("B"), "B's list contains A (edge stored both ways)");
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "A"), "self-loops are rejected");
        assertThrows(IllegalArgumentException.class, () -> graph.addVertex("  "), "blank vertex names are rejected");

        check(graph.searchVertex("A"), "search finds an existing vertex");
        check(!graph.searchVertex("Z"), "search reports a missing vertex");
        assertThrows(IllegalArgumentException.class, () -> graph.bfs("Z"), "BFS from a missing vertex is rejected");
        assertThrows(IllegalArgumentException.class, () -> graph.dfs("Z"), "DFS from a missing vertex is rejected");

        // Sample graph: cycle A-B-D-C-A, path D-E-F, and a separate component G-H.
        Graph sample = new Graph();
        SampleData.loadSampleGraph(sample);
        assertEquals(8, sample.getVertexCount(), "sample graph has 8 vertices");
        assertEquals(7, sample.getEdgeCount(), "sample graph has 7 edges");

        TraversalResult bfs = sample.bfs("A");
        assertEquals(List.of("A", "B", "C", "D", "E", "F"), bfs.getOrder(), "BFS order from A (level by level)");
        TraversalResult dfs = sample.dfs("A");
        assertEquals(List.of("A", "B", "D", "C", "E", "F"), dfs.getOrder(), "DFS order from A (deep first)");

        check(new HashSet<>(bfs.getOrder()).size() == bfs.getOrder().size(), "BFS never visits a vertex twice (cycle)");
        check(new HashSet<>(dfs.getOrder()).size() == dfs.getOrder().size(), "DFS never visits a vertex twice (cycle)");
        assertEquals(12, bfs.getAdjacencyChecks(), "BFS examines 2 x 6 edges in A's component");
        assertEquals(12, dfs.getAdjacencyChecks(), "DFS examines 2 x 6 edges in A's component");

        check(!bfs.getOrder().contains("G"), "BFS from A does not reach the disconnected component");
        assertEquals(List.of("G", "H"), sample.bfs("G").getOrder(), "BFS from G visits only its component");
        assertEquals(List.of("H", "G"), sample.dfs("H").getOrder(), "DFS from H visits only its component");

        List<TraversalResult> components = sample.traverseAllComponents(true);
        assertEquals(2, components.size(), "traverseAllComponents finds 2 components");
        assertEquals(List.of("G", "H"), components.get(1).getOrder(), "second component is G, H");
        assertEquals(2, sample.getConnectedComponentCount(), "connected component count is 2");

        sample.addVertex("Z");
        assertEquals(3, sample.getConnectedComponentCount(), "an isolated vertex forms its own component");
        assertEquals(List.of("Z"), sample.dfs("Z").getOrder(), "traversal from an isolated vertex visits only itself");

        long before = sample.getModificationCount();
        sample.addEdge("Z", "A");
        check(sample.getModificationCount() > before, "modification count increases when the graph changes");

        Graph benchmark = SampleData.generateBenchmarkGraph(500, 700, 42);
        assertEquals(500, benchmark.bfs("V0").getVerticesVisited(), "BFS visits all vertices of a connected benchmark graph");
        assertEquals(500, benchmark.dfs("V0").getVerticesVisited(), "DFS visits all vertices of a connected benchmark graph");
        assertEquals(2L * benchmark.getEdgeCount(), benchmark.bfs("V0").getAdjacencyChecks(),
                "BFS adjacency checks equal 2E on a connected graph (O(V + E))");
    }
}
