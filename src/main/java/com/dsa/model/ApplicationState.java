package com.dsa.model;

import com.dsa.array.ArrayManager;
import com.dsa.graph.Graph;
import com.dsa.linkedlist.CustomLinkedList;
import com.dsa.queue.CustomQueue;
import com.dsa.stack.CustomStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared application state: one instance of every data structure plus the most recent results.
 *
 * All menus receive the same ApplicationState object, which is how the components are integrated
 * (for example, Find a Student searches the array built in Student Records).
 *
 * Outdated-result detection: when a result is recorded, the modification count of the structure it
 * was computed from is stored with it. If the structure changes later, the counts no longer match and
 * "System Overview" labels the result as a previous result instead of presenting it as current.
 */
public class ApplicationState {

    // Small capacities make the full/overflow cases quick to demonstrate. Change them here if needed.
    public static final int ARRAY_CAPACITY = 10;
    public static final int STACK_CAPACITY = 5;
    public static final int QUEUE_CAPACITY = 5;

    private final ArrayManager arrayManager = new ArrayManager(ARRAY_CAPACITY);
    private final CustomStack<Integer> stack = new CustomStack<>(STACK_CAPACITY);
    private final CustomQueue<Integer> queue = new CustomQueue<>(QUEUE_CAPACITY);
    private final CustomLinkedList linkedList = new CustomLinkedList();
    private final Graph graph = new Graph();

    private List<SearchResult> lastSearchResults = Collections.emptyList();
    private long arrayVersionAtLastSearch;

    private TraversalResult lastBfsResult;
    private long graphVersionAtLastBfs;
    private TraversalResult lastDfsResult;
    private long graphVersionAtLastDfs;

    private PerformanceReport lastPerformanceReport;

    public ArrayManager getArrayManager() {
        return arrayManager;
    }

    public CustomStack<Integer> getStack() {
        return stack;
    }

    public CustomQueue<Integer> getQueue() {
        return queue;
    }

    public CustomLinkedList getLinkedList() {
        return linkedList;
    }

    public Graph getGraph() {
        return graph;
    }

    /** Stores the results of the latest search (one result, or two when algorithms were compared). */
    public void recordSearchResults(List<SearchResult> results) {
        lastSearchResults = Collections.unmodifiableList(new ArrayList<>(results));
        arrayVersionAtLastSearch = arrayManager.getModificationCount();
    }

    public List<SearchResult> getLastSearchResults() {
        return lastSearchResults;
    }

    /** True if the array has not changed since the last search was performed. */
    public boolean isSearchResultCurrent() {
        return arrayVersionAtLastSearch == arrayManager.getModificationCount();
    }

    public void recordBfsResult(TraversalResult result) {
        lastBfsResult = result;
        graphVersionAtLastBfs = graph.getModificationCount();
    }

    public void recordDfsResult(TraversalResult result) {
        lastDfsResult = result;
        graphVersionAtLastDfs = graph.getModificationCount();
    }

    public TraversalResult getLastBfsResult() {
        return lastBfsResult;
    }

    public TraversalResult getLastDfsResult() {
        return lastDfsResult;
    }

    public boolean isBfsResultCurrent() {
        return graphVersionAtLastBfs == graph.getModificationCount();
    }

    public boolean isDfsResultCurrent() {
        return graphVersionAtLastDfs == graph.getModificationCount();
    }

    public void recordPerformanceReport(PerformanceReport report) {
        lastPerformanceReport = report;
    }

    public PerformanceReport getLastPerformanceReport() {
        return lastPerformanceReport;
    }
}
