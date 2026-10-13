package com.dsa.tests;

import com.dsa.model.SearchResult;
import com.dsa.searching.SearchAlgorithms;
import com.dsa.util.SampleData;

import java.util.Arrays;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class SearchAlgorithmsTest {

    private SearchAlgorithmsTest() {
    }

    public static void run() {
        TestHelper.section("SearchAlgorithms");

        int[] data = {42, 7, 19, 3, 25};
        SearchResult first = SearchAlgorithms.linearSearch(data, 42);
        assertEquals(0, first.getIndex(), "linear search finds the first element");
        assertEquals(1, first.getComparisons(), "first element needs 1 comparison (best case)");

        SearchResult last = SearchAlgorithms.linearSearch(data, 25);
        assertEquals(4, last.getIndex(), "linear search finds the last element");
        assertEquals(5, last.getComparisons(), "last element needs n comparisons");

        SearchResult missing = SearchAlgorithms.linearSearch(data, 100);
        assertEquals(-1, missing.getIndex(), "linear search returns -1 for a missing element");
        assertEquals(5, missing.getComparisons(), "missing element needs n comparisons (worst case)");

        SearchResult empty = SearchAlgorithms.linearSearch(new int[0], 1);
        assertEquals(-1, empty.getIndex(), "linear search on an empty dataset returns -1");
        assertEquals(0, empty.getComparisons(), "empty dataset needs 0 comparisons");
        assertEquals(-1, SearchAlgorithms.binarySearch(new int[0], 1).getIndex(), "binary search on an empty dataset returns -1");

        int[] original = Arrays.copyOf(data, data.length);
        int[] sorted = SearchAlgorithms.createSortedCopy(data);
        check(Arrays.equals(original, data), "createSortedCopy leaves the original array unchanged");
        check(Arrays.equals(new int[]{3, 7, 19, 25, 42}, sorted), "sorted copy is in ascending order");
        assertEquals(2, SearchAlgorithms.binarySearch(sorted, 19).getIndex(), "binary search finds the middle element");
        assertEquals(1, SearchAlgorithms.binarySearch(sorted, 19).getComparisons(), "middle element found in 1 comparison");
        assertEquals(0, SearchAlgorithms.binarySearch(sorted, 3).getIndex(), "binary search finds the first element");
        assertEquals(4, SearchAlgorithms.binarySearch(sorted, 42).getIndex(), "binary search finds the last element");
        assertEquals(-1, SearchAlgorithms.binarySearch(sorted, 20).getIndex(), "binary search returns -1 for a missing element");
        assertThrows(IllegalArgumentException.class, () -> SearchAlgorithms.binarySearch(data, 7),
                "binary search rejects unsorted data");

        // Comparison counts on a large dataset: binary search needs at most floor(log2 n) + 1 comparisons.
        int[] large = SampleData.generateSortedDataset(1000);
        SearchResult linearLarge = SearchAlgorithms.linearSearch(large, 750);
        SearchResult binaryLarge = SearchAlgorithms.binarySearch(large, 750);
        assertEquals(749, linearLarge.getIndex(), "linear search finds 750 at index 749");
        assertEquals(750, linearLarge.getComparisons(), "linear search used 750 comparisons");
        assertEquals(749, binaryLarge.getIndex(), "binary search finds 750 at index 749");
        check(binaryLarge.getComparisons() <= 10, "binary search used <= 10 comparisons for n=1000 (used "
                + binaryLarge.getComparisons() + ")");
        check(SearchAlgorithms.binarySearch(large, 5000).getComparisons() <= 10,
                "binary search worst case (missing) stays within log2(n) + 1");
    }
}
