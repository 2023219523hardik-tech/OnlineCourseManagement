package com.ocms.common.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    private LinkedList<String> linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new LinkedList<>();
    }

    @Test
    void testAddAndGet() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        assertEquals("First", linkedList.get(0));
        assertEquals("Second", linkedList.get(1));
        assertEquals("Third", linkedList.get(2));
        assertEquals(3, linkedList.size());
    }

    @Test
    void testAddFirst() {
        linkedList.addFirst("First");
        linkedList.addFirst("Second");

        assertEquals("Second", linkedList.get(0));
        assertEquals("First", linkedList.get(1));
        assertEquals(2, linkedList.size());
    }

    @Test
    void testRemoveFirst() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        assertEquals("First", linkedList.removeFirst());
        assertEquals("Second", linkedList.get(0));
        assertEquals(2, linkedList.size());
    }

    @Test
    void testRemoveLast() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        assertEquals("Third", linkedList.removeLast());
        assertEquals("First", linkedList.get(0));
        assertEquals("Second", linkedList.get(1));
        assertEquals(2, linkedList.size());
    }

    @Test
    void testSet() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        linkedList.set(1, "Updated");

        assertEquals("First", linkedList.get(0));
        assertEquals("Updated", linkedList.get(1));
        assertEquals("Third", linkedList.get(2));
    }

    @Test
    void testIsEmpty() {
        assertTrue(linkedList.isEmpty());
        
        linkedList.add("First");
        assertFalse(linkedList.isEmpty());
        
        linkedList.removeFirst();
        assertTrue(linkedList.isEmpty());
    }

    @Test
    void testClear() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        linkedList.clear();

        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.size());
    }

    @Test
    void testIterator() {
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");

        int count = 0;
        for (String item : linkedList) {
            count++;
            assertNotNull(item);
        }
        assertEquals(3, count);
    }
}