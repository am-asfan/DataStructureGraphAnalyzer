package com.dsa.array;

/**
 * A manually managed, fixed-capacity integer array.
 *
 * Capacity strategy: the capacity is chosen once in the constructor and never grows.
 * When the array is full, further insertions are rejected with an IllegalStateException.
 * This keeps the element-shifting logic visible and makes the "full array" case testable.
 *
 * Only the first {@code size} slots of {@code elements} hold real data; the remaining
 * slots are unused space.
 *
 * Time complexity summary (n = current size):
 *   insert (at end)  O(1)
 *   insertAt         O(n)  - later elements shift one place right
 *   deleteAt         O(n)  - later elements shift one place left
 *   deleteValue      O(n)  - linear search plus shifting
 *   search           O(n)  - linear search
 *   get              O(1)
 *   display          O(n)
 */
public class ArrayManager {

    private final int[] elements;
    private int size;

    /** Element shifts or comparisons performed by the most recent operation (for analysis). */
    private int lastOperationSteps;

    /** Increases every time the contents change; used to detect outdated search results. */
    private long modificationCount;

    public ArrayManager(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        elements = new int[capacity];
    }

    /** Appends a value after the last element. O(1). */
    public void insert(int value) {
        ensureNotFull();
        elements[size] = value;
        size++;
        lastOperationSteps = 0;
        modificationCount++;
    }

    /**
     * Inserts a value at the given index, shifting the elements from that index one place right.
     * Valid indexes are 0 to size (index == size appends to the end).
     * O(n) worst case: inserting at index 0 shifts every element.
     */
    public void insertAt(int index, int value) {
        ensureNotFull();
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " is invalid. Valid insertion indexes are 0 to "
                    + size + ".");
        }
        int shifts = 0;
        // Work from the end backwards so no value is overwritten before it is moved.
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
            shifts++;
        }
        elements[index] = value;
        size++;
        lastOperationSteps = shifts;
        modificationCount++;
    }

    /**
     * Removes and returns the element at the given index, shifting later elements one place left.
     * O(n) worst case: deleting index 0 shifts every remaining element.
     */
    public int deleteAt(int index) {
        ensureNotEmpty();
        checkElementIndex(index);
        int removed = elements[index];
        int shifts = 0;
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
            shifts++;
        }
        size--;
        elements[size] = 0; // clear the slot that is no longer used
        lastOperationSteps = shifts;
        modificationCount++;
        return removed;
    }

    /**
     * Deletes the first occurrence of a value.
     * O(n): linear search to find it, then shifting to close the gap.
     *
     * @return the index the value was removed from, or -1 if the value is not in the array
     */
    public int deleteValue(int value) {
        ensureNotEmpty();
        int index = search(value);
        if (index == -1) {
            return -1; // lastOperationSteps already holds the comparisons made by search
        }
        int comparisons = lastOperationSteps;
        deleteAt(index);
        lastOperationSteps += comparisons; // comparisons + shifts
        return index;
    }

    /**
     * Linear search for a value. O(n) worst case, O(1) best case.
     *
     * @return the index of the first occurrence, or -1 if not found
     */
    public int search(int value) {
        int comparisons = 0;
        for (int i = 0; i < size; i++) {
            comparisons++;
            if (elements[i] == value) {
                lastOperationSteps = comparisons;
                return i;
            }
        }
        lastOperationSteps = comparisons;
        return -1;
    }

    /** Returns the element at an index. O(1) - direct access is the main strength of arrays. */
    public int get(int index) {
        checkElementIndex(index);
        return elements[index];
    }

    /** Prints the array as an index/value table. O(n). */
    public void display() {
        if (isEmpty()) {
            System.out.println("The array is empty (capacity " + capacity() + ").");
            return;
        }
        StringBuilder indexRow = new StringBuilder("Index : ");
        StringBuilder valueRow = new StringBuilder("Value : ");
        for (int i = 0; i < size; i++) {
            String value = String.valueOf(elements[i]);
            int width = Math.max(value.length(), String.valueOf(i).length()) + 2;
            indexRow.append(String.format("%-" + width + "s", i));
            valueRow.append(String.format("%-" + width + "s", value));
        }
        System.out.println(indexRow.toString().stripTrailing());
        System.out.println(valueRow.toString().stripTrailing());
        System.out.println("Size: " + size + " / Capacity: " + capacity());
    }

    /** Returns a copy of the stored elements so callers cannot change the internal array. O(n). */
    public int[] toArray() {
        int[] copy = new int[size];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        return copy;
    }

    /** Removes all elements. O(1) - old values are simply ignored because size is reset. */
    public void clear() {
        size = 0;
        lastOperationSteps = 0;
        modificationCount++;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    public int getLastOperationSteps() {
        return lastOperationSteps;
    }

    public long getModificationCount() {
        return modificationCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(elements[i]);
        }
        return builder.append("]").toString();
    }

    private void ensureNotFull() {
        if (isFull()) {
            throw new IllegalStateException("The array is full (capacity " + capacity()
                    + "). Delete an element before inserting.");
        }
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("The array is empty. Insert an element first.");
        }
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            String valid = isEmpty() ? "the array is empty" : "valid indexes are 0 to " + (size - 1);
            throw new IndexOutOfBoundsException("Index " + index + " is invalid; " + valid + ".");
        }
    }
}
