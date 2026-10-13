package com.dsa.model;

import com.dsa.util.ConsoleFormatter;

/**
 * The outcome of one run of a searching algorithm.
 *
 * "Comparisons" means the number of dataset elements examined against the target.
 * Binary search examines one middle element per loop iteration, so its count equals
 * the number of iterations.
 */
public class SearchResult {

    private final String algorithm;
    private final int target;
    private final int index;
    private final int comparisons;
    private final long elapsedNanos;
    private final int datasetSize;
    private final String datasetLabel;

    public SearchResult(String algorithm, int target, int index, int comparisons,
                        long elapsedNanos, int datasetSize, String datasetLabel) {
        this.algorithm = algorithm;
        this.target = target;
        this.index = index;
        this.comparisons = comparisons;
        this.elapsedNanos = elapsedNanos;
        this.datasetSize = datasetSize;
        this.datasetLabel = datasetLabel;
    }

    public boolean isFound() {
        return index != -1;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getTarget() {
        return target;
    }

    /** Index in the searched dataset, or -1 when the target was not found. */
    public int getIndex() {
        return index;
    }

    public int getComparisons() {
        return comparisons;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public String getDatasetLabel() {
        return datasetLabel;
    }

    /** One-line description, e.g. "Linear Search for 7 in original array (n=5): found at index 2 ...". */
    public String getSummary() {
        String outcome = isFound() ? "found at index " + index : "not found";
        return algorithm + " for " + target + " in " + datasetLabel + " (n=" + datasetSize + "): "
                + outcome + ", " + comparisons + " comparison(s), " + ConsoleFormatter.formatNanos(elapsedNanos);
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
