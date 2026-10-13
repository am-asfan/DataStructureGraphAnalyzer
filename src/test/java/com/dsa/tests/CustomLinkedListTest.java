package com.dsa.tests;

import com.dsa.linkedlist.CustomLinkedList;

import static com.dsa.tests.TestHelper.assertEquals;
import static com.dsa.tests.TestHelper.assertThrows;
import static com.dsa.tests.TestHelper.check;

public final class CustomLinkedListTest {

    private CustomLinkedListTest() {
    }

    public static void run() {
        TestHelper.section("CustomLinkedList");

        CustomLinkedList list = new CustomLinkedList();
        check(list.isEmpty(), "new list is empty");
        assertEquals("HEAD -> null", list.toString(), "empty list display");
        assertThrows(IllegalStateException.class, () -> list.deleteByValue(1), "deleteByValue on empty list is rejected");
        assertThrows(IllegalStateException.class, () -> list.deleteAt(0), "deleteAt on empty list is rejected");
        assertEquals(-1, list.search(1), "search on empty list returns -1");

        list.insertAtEnd(20);
        assertEquals("HEAD -> 20 -> null", list.toString(), "insert at end into an empty list");
        list.insertAtBeginning(10);
        list.insertAtEnd(40);
        list.insertAt(2, 30);
        assertEquals("HEAD -> 10 -> 20 -> 30 -> 40 -> null", list.toString(), "insert at beginning, end and index");
        list.insertAt(0, 5);
        list.insertAt(5, 50);
        assertEquals("HEAD -> 5 -> 10 -> 20 -> 30 -> 40 -> 50 -> null", list.toString(), "insertAt index 0 and index == size");
        assertEquals(6, list.countNodes(), "countNodes counts by traversal");
        assertEquals(6, list.size(), "size matches countNodes");

        assertEquals(3, list.search(30), "search finds an existing value");
        assertEquals(-1, list.search(99), "search for a missing value returns -1");

        check(list.deleteByValue(5), "delete the head by value");
        assertEquals("HEAD -> 10 -> 20 -> 30 -> 40 -> 50 -> null", list.toString(), "head reference updated");
        check(list.deleteByValue(50), "delete the last node by value");
        assertEquals("HEAD -> 10 -> 20 -> 30 -> 40 -> null", list.toString(), "last node removed, links intact");
        check(!list.deleteByValue(99), "deleting a missing value returns false");
        assertEquals(20, list.deleteAt(1), "deleteAt middle index returns the value");
        assertEquals(40, list.deleteAt(2), "deleteAt last index returns the value");
        assertEquals(10, list.deleteAt(0), "deleteAt index 0 returns the head value");
        assertEquals("HEAD -> 30 -> null", list.toString(), "links correct after index deletions");

        assertThrows(IndexOutOfBoundsException.class, () -> list.insertAt(3, 1), "insertAt index > size is rejected");
        assertThrows(IndexOutOfBoundsException.class, () -> list.insertAt(-1, 1), "insertAt negative index is rejected");
        assertThrows(IndexOutOfBoundsException.class, () -> list.deleteAt(1), "deleteAt index == size is rejected");

        list.deleteAt(0);
        check(list.isEmpty(), "list empty after deleting the only node");
        assertEquals(0, list.countNodes(), "countNodes is 0 for an empty list");
    }
}
