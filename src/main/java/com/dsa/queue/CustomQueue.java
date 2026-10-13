package com.dsa.queue;

/**
 * A fixed-capacity queue implemented with a circular array (FIFO: First In, First Out).
 *
 * How the circular array works:
 *   - {@code front} is the index of the oldest element (the next one to leave).
 *   - {@code rear} is the index of the newest element.
 *   - Both indexes move forward with (index + 1) % capacity, so after reaching the last slot
 *     they wrap around to slot 0 and reuse the positions freed by earlier dequeues.
 *   - {@code size} tells us how many slots are in use, which makes the empty and full checks
 *     simple and unambiguous (front == rear alone could mean either one element or none).
 *
 * The class is generic: the Queue menu stores Integers, and the Graph uses it for BFS with Strings.
 *
 * Time complexity:
 *   enqueue O(1), dequeue O(1), peek O(1), display O(n)
 */
public class CustomQueue<T> {

    private final Object[] elements;
    private int front = 0;
    private int rear = -1;
    private int size = 0;

    public CustomQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Queue capacity must be greater than zero.");
        }
        elements = new Object[capacity];
    }

    /** Adds a value at the rear of the queue. */
    public void enqueue(T value) {
        if (isFull()) {
            throw new IllegalStateException("Queue overflow: the queue is full (capacity " + capacity() + ").");
        }
        rear = (rear + 1) % elements.length; // wrap around to 0 after the last slot
        elements[rear] = value;
        size++;
    }

    /** Removes and returns the value at the front of the queue. */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue underflow: cannot dequeue from an empty queue.");
        }
        T value = elementAt(front);
        elements[front] = null; // free the slot so it can be reused
        front = (front + 1) % elements.length;
        size--;
        return value;
    }

    /** Returns the front value without removing it. */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek: the queue is empty.");
        }
        return elementAt(front);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    public int getFrontIndex() {
        return front;
    }

    public int getRearIndex() {
        return rear;
    }

    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    /** Prints the queue from front to rear, followed by the raw array slots to show circular reuse. */
    public void display() {
        if (isEmpty()) {
            System.out.println("The queue is empty (capacity " + capacity() + ").");
        } else {
            System.out.println("Front -> " + joinFrontToRear() + " <- Rear");
        }
        StringBuilder slots = new StringBuilder("Array slots: ");
        for (int i = 0; i < elements.length; i++) {
            slots.append("[").append(i).append("]=").append(elements[i] == null ? "_" : elements[i]).append("  ");
        }
        System.out.println(slots.toString().stripTrailing());
        System.out.println("front index = " + front + ", rear index = " + rear
                + ", size = " + size + " / capacity = " + capacity());
    }

    @Override
    public String toString() {
        return "[" + joinFrontToRear() + "]";
    }

    /** Builds "a, b, c" by walking size steps from front, wrapping with the modulo operator. */
    private String joinFrontToRear() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int index = (front + i) % elements.length;
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(elements[index]);
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        // Safe: only values of type T are ever stored through enqueue().
        return (T) elements[index];
    }
}
