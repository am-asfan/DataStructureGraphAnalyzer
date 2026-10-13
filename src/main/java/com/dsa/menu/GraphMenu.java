package com.dsa.menu;

import com.dsa.graph.Graph;
import com.dsa.model.ApplicationState;
import com.dsa.model.TraversalResult;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;
import com.dsa.util.SampleData;

import java.util.List;

/**
 * "University Connection Network": members (students, departments, clubs) and the connections
 * between them. Technically this is the undirected adjacency-list graph: a member is a vertex,
 * a connection is an edge, "level by level" is BFS and "path by path" is DFS.
 */
public class GraphMenu extends BaseMenu {

    private final Graph graph;

    public GraphMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.graph = state.getGraph();
    }

    @Override
    protected String getTitle() {
        return "UNIVERSITY CONNECTION NETWORK";
    }

    @Override
    protected String getTechnicalNote() {
        return "undirected graph (adjacency list); level by level = BFS, path by path = DFS";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Add Network Member",
                "Connect Two Members",
                "View Network",
                "Find a Member",
                "Explore Network Level by Level",
                "Explore Network Path by Path",
                "Network Summary",
                "View All Connected Groups",
                "Load Sample Network"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> addMember();
            case 2 -> connectMembers();
            case 3 -> viewNetwork();
            case 4 -> findMember();
            case 5 -> explore(true);
            case 6 -> explore(false);
            case 7 -> displaySummary();
            case 8 -> viewConnectedGroups();
            case 9 -> loadSampleNetwork();
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void addMember() {
        String member = input.readVertexName("Enter the member name (student, department or club): ");
        if (graph.addVertex(member)) {
            ConsoleFormatter.success("Member '" + member + "' added to the network.");
        } else {
            ConsoleFormatter.error("Member '" + member + "' is already in the network. Duplicate members are not allowed.");
        }
    }

    private void connectMembers() {
        String first = input.readVertexName("Enter the first member: ");
        String second = input.readVertexName("Enter the second member: ");
        if (first.equals(second)) {
            ConsoleFormatter.error("A member cannot be connected to themselves. Choose two different members.");
            return;
        }
        boolean firstIsNew = !graph.searchVertex(first);
        boolean secondIsNew = !graph.searchVertex(second);
        if (graph.addEdge(first, second)) {
            if (firstIsNew) {
                ConsoleFormatter.info("Member '" + first + "' was not in the network and has been added automatically.");
            }
            if (secondIsNew) {
                ConsoleFormatter.info("Member '" + second + "' was not in the network and has been added automatically.");
            }
            ConsoleFormatter.success(first + " and " + second + " are now connected (the connection works both ways).");
        } else {
            ConsoleFormatter.error(first + " and " + second + " are already connected. Duplicate connections are ignored.");
        }
    }

    private void viewNetwork() {
        if (graph.isEmpty()) {
            printEmptyNetwork();
            return;
        }
        System.out.println("University connection network (each member followed by its connections):");
        graph.displayGraph();
    }

    private void findMember() {
        String member = input.readVertexName("Enter the member to find: ");
        if (graph.searchVertex(member)) {
            List<String> connections = graph.getNeighbours(member);
            ConsoleFormatter.success("Member '" + member + "' found. Connections: " + connections.size()
                    + (connections.isEmpty() ? " (none)" : " (" + String.join(", ", connections) + ")"));
        } else {
            ConsoleFormatter.info("Member '" + member + "' is not in the network.");
        }
    }

    private void explore(boolean levelByLevel) {
        if (graph.isEmpty()) {
            printEmptyNetwork();
            return;
        }
        System.out.println("Members: " + String.join(", ", graph.getVertices()));
        String start = input.readVertexName("Enter the starting member: ");
        if (!graph.searchVertex(start)) {
            ConsoleFormatter.error("Member '" + start + "' is not in the network. Choose one of the members listed above.");
            return;
        }
        TraversalResult result = levelByLevel ? graph.bfs(start) : graph.dfs(start);
        if (levelByLevel) {
            state.recordBfsResult(result);
        } else {
            state.recordDfsResult(result);
        }

        System.out.println();
        String method = levelByLevel
                ? "level by level (Breadth-First Search, using a queue)"
                : "path by path (Depth-First Search, using recursion)";
        ConsoleFormatter.success("Explored the network " + method + " from " + start);
        System.out.println("Visit order         : " + result.getOrderText());
        System.out.println("Members reached     : " + result.getVerticesVisited() + " of " + graph.getVertexCount());
        System.out.println("Connections checked : " + result.getAdjacencyChecks());
        System.out.println("Time taken          : " + ConsoleFormatter.formatNanos(result.getElapsedNanos()));
        System.out.println("Complexity          : O(V + E)");
        if (result.getVerticesVisited() < graph.getVertexCount()) {
            ConsoleFormatter.info("Some members are in a separate group that cannot be reached from " + start
                    + ". Use 'View All Connected Groups' to see every member.");
        }
    }

    private void displaySummary() {
        System.out.println("Stored as           : undirected graph (adjacency list, Map<String, List<String>>)");
        System.out.println("Members             : " + graph.getVertexCount());
        System.out.println("Connections         : " + graph.getEdgeCount());
        if (graph.isEmpty()) {
            printEmptyNetwork();
            return;
        }
        int groups = graph.getConnectedComponentCount();
        System.out.println("Connected groups    : " + groups
                + (groups == 1 ? " (everyone is linked)" : " (some members are not linked to the rest)"));
        System.out.println("Connections per member:");
        for (String member : graph.getVertices()) {
            System.out.println("  " + member + " : " + graph.getDegree(member));
        }
        System.out.println("Total = 2 x connections = " + (2 * graph.getEdgeCount())
                + " (each connection is stored for both members).");
    }

    private void viewConnectedGroups() {
        if (graph.isEmpty()) {
            printEmptyNetwork();
            return;
        }
        int mode = input.readIntInRange("Explore each group (1) level by level [BFS] or (2) path by path [DFS]? ", 1, 2);
        List<TraversalResult> groups = graph.traverseAllComponents(mode == 1);
        System.out.println();
        ConsoleFormatter.success("Found " + groups.size() + " connected group(s):");
        int number = 1;
        for (TraversalResult group : groups) {
            System.out.println("  Group " + number + " (starting at " + group.getStartVertex() + "): "
                    + group.getOrderText());
            number++;
        }
    }

    private void loadSampleNetwork() {
        if (!graph.isEmpty() && !input.readYesNo("This replaces the current network. Continue? (y/n): ")) {
            ConsoleFormatter.info("The current network was kept.");
            return;
        }
        SampleData.loadSampleGraph(graph);
        ConsoleFormatter.success("Sample network loaded: 8 members, 7 connections, one loop (A-B-D-C-A) "
                + "and two separate groups.");
        graph.displayGraph();
    }

    private void printEmptyNetwork() {
        ConsoleFormatter.error("The network is empty. Add members and connections, or load the sample network first.");
    }
}
