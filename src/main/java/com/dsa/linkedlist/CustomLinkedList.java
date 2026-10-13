package com.dsa.linkedlist;

/**
 * A manually implemented singly linked list of integers.
 *
 * Each Node stores a value and a reference to the next node. {@code head} points to the
 * first node (null when the list is empty). No tail reference is kept, so inserting at the
 * end must walk the whole list.
 *
 * Time complexity (n = number of nodes):
 *   insertAtBeginning O(1)
 *   insertAtEnd       O(n) - walk to the last node (would be O(1) with a tail reference)
 *   insertAt          O(n) - walk to the node before the index
 *   deleteByValue     O(n)
 *   deleteAt          O(n) - O(1) for index 0
 *   search            O(n)
 *   countNodes        O(n) - counts by traversal; size() is O(1) using a stored counter
 *   display           O(n)
 */
public class CustomLinkedList {

    /** One element of the list. */
    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private int size;

    /** Number of "move to next node" steps made by the most recent operation (for analysis). */
    private int lastTraversalSteps;

    public void insertAtBeginning(int value) {
        Node node = new Node(value);
        node.next = head; // new node points to the old first node
        head = node;
        size++;
        lastTraversalSteps = 0;
    }

    public void insertAtEnd(int value) {
        Node node = new Node(value);
        lastTraversalSteps = 0;
        if (head == null) {
            head = node; // inserting into an empty list
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
                lastTraversalSteps++;
            }
            current.next = node;
        }
        size++;
    }

    /**
     * Inserts a value so that it ends up at the given position. Valid indexes are 0 to size.
     */
    public void insertAt(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " is invalid. Valid insertion indexes are 0 to "
                    + size + ".");
        }
        if (index == 0) {
            insertAtBeginning(value);
            return;
        }
        Node previous = nodeBefore(index);
        Node node = new Node(value);
        node.next = previous.next; // link the new node to the rest of the list first...
        previous.next = node;      // ...then link the previous node to the new node
        size++;
    }

    /**
     * Deletes the first node containing the value.
     *
     * @return true if a node was deleted, false if the value was not found
     */
    public boolean deleteByValue(int value) {
        ensureNotEmpty();
        lastTraversalSteps = 0;
        if (head.value == value) {
            head = head.next; // deleting the first node
            size--;
            return true;
        }
        Node previous = head;
        while (previous.next != null && previous.next.value != value) {
            previous = previous.next;
            lastTraversalSteps++;
        }
        if (previous.next == null) {
            return false; // reached the end without finding the value
        }
        previous.next = previous.next.next; // bypass the deleted node (works for the last node too)
        size--;
        return true;
    }

    /** Deletes the node at the given index and returns its value. */
    public int deleteAt(int index) {
        ensureNotEmpty();
        checkElementIndex(index);
        if (index == 0) {
            int removed = head.value;
            head = head.next;
            size--;
            lastTraversalSteps = 0;
            return removed;
        }
        Node previous = nodeBefore(index);
        int removed = previous.next.value;
        previous.next = previous.next.next;
        size--;
        return removed;
    }

    /**
     * Searches for a value from the head onwards.
     *
     * @return the index of the first matching node, or -1 if not found
     */
    public int search(int value) {
        Node current = head;
        int index = 0;
        lastTraversalSteps = 0;
        while (current != null) {
            if (current.value == value) {
                return index;
            }
            current = current.next;
            index++;
            lastTraversalSteps++;
        }
        return -1;
    }

    /** Counts the nodes by walking the list (demonstrates traversal; size() gives the same answer in O(1)). */
    public int countNodes() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public int get(int index) {
        checkElementIndex(index);
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("The linked list is empty (HEAD -> null).");
            return;
        }
        System.out.println(toString());
        System.out.println("Number of nodes: " + size);
    }

    public int[] toArray() {
        int[] values = new int[size];
        Node current = head;
        for (int i = 0; i < size; i++) {
            values[i] = current.value;
            current = current.next;
        }
        return values;
    }

    public void clear() {
        head = null; // the old nodes become unreachable and are garbage collected
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getLastTraversalSteps() {
        return lastTraversalSteps;
    }

    /** Returns "HEAD -> 10 -> 20 -> null". */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("HEAD -> ");
        Node current = head;
        while (current != null) {
            builder.append(current.value).append(" -> ");
            current = current.next;
        }
        return builder.append("null").toString();
    }

    /** Walks to the node at position index - 1 (index must be at least 1). */
    private Node nodeBefore(int index) {
        Node previous = head;
        lastTraversalSteps = 0;
        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
            lastTraversalSteps++;
        }
        return previous;
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("The linked list is empty. Insert a node first.");
        }
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            String valid = isEmpty() ? "the list is empty" : "valid indexes are 0 to " + (size - 1);
            throw new IndexOutOfBoundsException("Index " + index + " is invalid; " + valid + ".");
        }
    }
}
