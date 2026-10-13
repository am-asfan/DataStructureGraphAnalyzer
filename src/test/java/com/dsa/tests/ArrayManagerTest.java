package com.dsa.tests;

import com.dsa.array.ArrayManager;

import java.util.Arrays;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class ArrayManagerTest {

    private ArrayManagerTest() {
    }

    public static void run() {
        TestHelper.section("ArrayManager");

        ArrayManager array = new ArrayManager(5);
        check(array.isEmpty(), "new array is empty");
        array.insert(10);
        assertEquals("[10]", array.toString(), "insert into an empty array");

        array.insert(30);
        array.insertAt(0, 5);   // beginning
        array.insertAt(2, 20);  // middle
        array.insertAt(4, 40);  // end (index == size)
        assertEquals("[5, 10, 20, 30, 40]", array.toString(), "insert at beginning, middle and end");
        check(array.isFull(), "array reports full at capacity");
        assertThrows(IllegalStateException.class, () -> array.insert(99), "insert when full is rejected");
        assertThrows(IllegalStateException.class, () -> array.insertAt(0, 99), "insertAt when full is rejected");

        assertEquals(2, array.search(20), "search finds an existing value");
        assertEquals(3, array.getLastOperationSteps(), "search counted 3 comparisons");
        assertEquals(-1, array.search(99), "search for a missing value returns -1");

        assertEquals(2, array.deleteValue(20), "delete an existing value returns its index");
        assertEquals("[5, 10, 30, 40]", array.toString(), "elements shift left after deleting by value");
        assertEquals(-1, array.deleteValue(99), "delete a missing value returns -1");
        assertEquals(5, array.deleteAt(0), "deleteAt(0) returns the removed value");
        assertEquals("[10, 30, 40]", array.toString(), "elements shift left after deleting the first element");
        assertEquals(3, array.getLastOperationSteps(), "deleting index 0 of 4 elements shifts the other 3");

        assertThrows(IndexOutOfBoundsException.class, () -> array.deleteAt(3), "deleteAt index == size is rejected");
        assertThrows(IndexOutOfBoundsException.class, () -> array.deleteAt(-1), "deleteAt negative index is rejected");
        assertThrows(IndexOutOfBoundsException.class, () -> array.insertAt(5, 1), "insertAt index > size is rejected");
        assertThrows(IndexOutOfBoundsException.class, () -> array.insertAt(-1, 1), "insertAt negative index is rejected");
        assertEquals(3, array.size(), "size is unchanged after rejected operations");
        assertEquals(5, array.capacity(), "capacity is fixed");

        int[] copy = array.toArray();
        copy[0] = 999;
        assertEquals(10, array.get(0), "toArray returns a copy that cannot change the array");
        check(Arrays.equals(new int[]{10, 30, 40}, array.toArray()), "toArray contains the elements in order");

        ArrayManager empty = new ArrayManager(3);
        assertThrows(IllegalStateException.class, () -> empty.deleteAt(0), "deleteAt on an empty array is rejected");
        assertThrows(IllegalStateException.class, () -> empty.deleteValue(1), "deleteValue on an empty array is rejected");
        assertEquals(-1, empty.search(1), "search on an empty array returns -1");
        assertThrows(IllegalArgumentException.class, () -> new ArrayManager(0), "capacity 0 is rejected");
    }
}
