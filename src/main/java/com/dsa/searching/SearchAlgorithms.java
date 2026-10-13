package com.dsa.searching;

import com.dsa.model.SearchResult;

import java.util.Arrays;

/**
 * Linear search and iterative binary search, each counting comparisons and measuring time.
 *
 * Comparison definition: one comparison = one dataset element examined against the target.
 * Elapsed time is measured with System.nanoTime() around the search loop only; it varies between
 * runs (JVM warm-up, CPU frequency, other programs), so the comparison count is the more stable
 * way to study algorithm behaviour.
 *
 * Complexity:
 *   Linear search - best O(1), average O(n), worst O(n); works on unsorted data.
 *   Binary search - best O(1), average O(log n), worst O(log n); requires sorted data.
 */
public final class SearchAlgorithms {

    private SearchAlgorithms() {
        // Utility class - all methods are static.
    }

    public static SearchResult linearSearch(int[] data, int target) {
        return linearSearch(data, target, "dataset");
    }

    /** Checks each element from left to right until the target is found or the data ends. */
    public static SearchResult linearSearch(int[] data, int target, String datasetLabel) {
        requireData(data);
        long start = System.nanoTime();
        int comparisons = 0;
        int foundIndex = -1;
        for (int i = 0; i < data.length; i++) {
            comparisons++;
            if (data[i] == target) {
                foundIndex = i;
                break;
            }
        }
        long elapsed = System.nanoTime() - start;
        return new SearchResult("Linear Search", target, foundIndex, comparisons, elapsed, data.length, datasetLabel);
    }

    public static SearchResult binarySearch(int[] sortedData, int target) {
        return binarySearch(sortedData, target, "sorted dataset");
    }

    /**
     * Iterative binary search: repeatedly examines the middle element and discards the half
     * that cannot contain the target.
     *
     * The sorted-order check runs before the timer starts, so it does not affect the measured time.
     *
     * @throws IllegalArgumentException if the data is not sorted in ascending order
     */
    public static SearchResult binarySearch(int[] sortedData, int target, String datasetLabel) {
        requireData(sortedData);
        if (!isSorted(sortedData)) {
            throw new IllegalArgumentException("Binary search requires data sorted in ascending order.");
        }
        long start = System.nanoTime();
        int low = 0;
        int high = sortedData.length - 1;
        int comparisons = 0;
        int foundIndex = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2; // same as (low + high) / 2 but cannot overflow
            comparisons++;
            if (sortedData[mid] == target) {
                foundIndex = mid;
                break;
            } else if (sortedData[mid] < target) {
                low = mid + 1;  // target can only be in the right half
            } else {
                high = mid - 1; // target can only be in the left half
            }
        }
        long elapsed = System.nanoTime() - start;
        return new SearchResult("Binary Search", target, foundIndex, comparisons, elapsed,
                sortedData.length, datasetLabel);
    }

    /**
     * Returns a sorted copy of the data. The original array is NOT modified, so the application's
     * array keeps its insertion order.
     */
    public static int[] createSortedCopy(int[] data) {
        requireData(data);
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy;
    }

    /** True if every element is less than or equal to the next one. O(n). */
    public static boolean isSorted(int[] data) {
        requireData(data);
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1] > data[i]) {
                return false;
            }
        }
        return true;
    }

    private static void requireData(int[] data) {
        if (data == null) {
            throw new IllegalArgumentException("The dataset cannot be null.");
        }
    }
}
