package com.dsa.tests;

import com.dsa.stack.CustomStack;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class CustomStackTest {

    private CustomStackTest() {
    }

    public static void run() {
        TestHelper.section("CustomStack");

        CustomStack<Integer> stack = new CustomStack<>(3);
        check(stack.isEmpty(), "new stack is empty");
        assertThrows(IllegalStateException.class, stack::pop, "pop from an empty stack is rejected");
        assertThrows(IllegalStateException.class, stack::peek, "peek at an empty stack is rejected");

        stack.push(1);
        stack.push(2);
        stack.push(3);
        check(stack.isFull(), "stack is full after 3 pushes (capacity 3)");
        assertThrows(IllegalStateException.class, () -> stack.push(4), "push beyond capacity is rejected (overflow)");
        assertEquals(3, (long) stack.peek(), "peek returns the top value");
        assertEquals(3, stack.size(), "peek does not remove the value");

        assertEquals(3, (long) stack.pop(), "first pop returns the last value pushed (LIFO)");
        assertEquals(2, (long) stack.pop(), "second pop returns 2");
        assertEquals(1, (long) stack.pop(), "third pop returns 1");
        check(stack.isEmpty(), "stack is empty after popping everything");

        stack.push(7);
        assertEquals("[7] (top = 7)", stack.toString(), "stack can be reused after emptying");

        CustomStack<String> strings = new CustomStack<>(2);
        strings.push("A");
        assertEquals("A", strings.pop(), "generic stack works with Strings");
    }
}
