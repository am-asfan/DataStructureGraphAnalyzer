package com.dsa.tests;

import com.dsa.queue.CustomQueue;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class CustomQueueTest {

    private CustomQueueTest() {
    }

    public static void run() {
        TestHelper.section("CustomQueue (circular array)");

        CustomQueue<Integer> queue = new CustomQueue<>(3);
        check(queue.isEmpty(), "new queue is empty");
        assertThrows(IllegalStateException.class, queue::dequeue, "dequeue from an empty queue is rejected");
        assertThrows(IllegalStateException.class, queue::peek, "peek at an empty queue is rejected");

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        check(queue.isFull(), "queue is full after 3 enqueues (capacity 3)");
        assertThrows(IllegalStateException.class, () -> queue.enqueue(4), "enqueue when full is rejected (overflow)");
        assertEquals(1, (long) queue.peek(), "peek returns the front value");

        assertEquals(1, (long) queue.dequeue(), "dequeue returns the first value enqueued (FIFO)");
        assertEquals(2, (long) queue.dequeue(), "dequeue returns 2 next");
        assertEquals(2, queue.getFrontIndex(), "front index moved forward to 2");

        // Circular reuse: rear is at index 2 (the last slot), so the next enqueues must wrap to 0 and 1.
        queue.enqueue(4);
        assertEquals(0, queue.getRearIndex(), "rear wraps around to index 0");
        queue.enqueue(5);
        assertEquals(1, queue.getRearIndex(), "rear then moves to index 1");
        check(queue.isFull(), "queue is full again after wrapping");
        assertEquals("[3, 4, 5]", queue.toString(), "elements are in FIFO order across the wrap");

        assertEquals(3, (long) queue.dequeue(), "dequeue after wrap returns 3");
        assertEquals(0, queue.getFrontIndex(), "front index wraps around to 0");
        assertEquals(4, (long) queue.dequeue(), "dequeue returns 4");
        assertEquals(5, (long) queue.dequeue(), "dequeue returns 5");
        check(queue.isEmpty(), "queue is empty after dequeuing everything");
    }
}
