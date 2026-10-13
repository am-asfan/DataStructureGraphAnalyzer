package com.dsa.stack;

/**
 * A fixed-capacity stack implemented with an array (LIFO: Last In, First Out).
 *
 * {@code top} is the index of the most recently pushed element; it is -1 when the stack is empty.
 * The class is generic so it can store Integers for the Stack menu and other types elsewhere.
 *
 * Time complexity:
 *   push    O(1) - write at top + 1
 *   pop     O(1) - read at top, then move top down
 *   peek    O(1)
 *   display O(n)
 */
public class CustomStack<T> {

    private final Object[] elements; // Java cannot create a generic array (new T[]), so Object[] is used
    private int top = -1;

    public CustomStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Stack capacity must be greater than zero.");
        }
        elements = new Object[capacity];
    }

    /** Adds a value to the top of the stack. */
    public void push(T value) {
        if (isFull()) {
            throw new IllegalStateException("Stack overflow: the stack is full (capacity " + capacity() + ").");
        }
        top++;
        elements[top] = value;
    }

    /** Removes and returns the top value. */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow: cannot pop from an empty stack.");
        }
        T value = elementAt(top);
        elements[top] = null; // remove the reference so the object can be garbage collected
        top--;
        return value;
    }

    /** Returns the top value without removing it. */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek: the stack is empty.");
        }
        return elementAt(top);
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == elements.length - 1;
    }

    public int size() {
        return top + 1;
    }

    public int capacity() {
        return elements.length;
    }

    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    /** Prints the stack vertically, top element first. */
    public void display() {
        if (isEmpty()) {
            System.out.println("The stack is empty (capacity " + capacity() + ").");
            return;
        }
        for (int i = top; i >= 0; i--) {
            String marker = (i == top) ? "   <- top" : "";
            System.out.println(String.format("| %-10s |", elements[i]) + marker);
        }
        System.out.println("+------------+");
        System.out.println("Size: " + size() + " / Capacity: " + capacity());
    }

    /** Returns the elements from bottom to top, e.g. "[10, 20, 30] (top = 30)". */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i <= top; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(elements[i]);
        }
        return builder.append("] (top = ").append(elements[top]).append(")").toString();
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        // Safe: only values of type T are ever stored through push().
        return (T) elements[index];
    }
}
