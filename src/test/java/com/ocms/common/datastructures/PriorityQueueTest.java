package com.ocms.common.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueTest {

    private PriorityQueue<Integer> priorityQueue;

    @BeforeEach
    void setUp() {
        priorityQueue = new PriorityQueue<>(Comparator.naturalOrder());
    }

    @Test
    void testEnqueueAndDequeue() {
        priorityQueue.enqueue(3);
        priorityQueue.enqueue(1);
        priorityQueue.enqueue(2);

        assertEquals(1, priorityQueue.dequeue());
        assertEquals(2, priorityQueue.dequeue());
        assertEquals(3, priorityQueue.dequeue());
    }

    @Test
    void testPeek() {
        priorityQueue.enqueue(3);
        priorityQueue.enqueue(1);
        priorityQueue.enqueue(2);

        assertEquals(1, priorityQueue.peek());
        assertEquals(1, priorityQueue.peek()); // Should not remove
    }

    @Test
    void testIsEmpty() {
        assertTrue(priorityQueue.isEmpty());
        
        priorityQueue.enqueue(1);
        assertFalse(priorityQueue.isEmpty());
        
        priorityQueue.dequeue();
        assertTrue(priorityQueue.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, priorityQueue.size());
        
        priorityQueue.enqueue(1);
        priorityQueue.enqueue(2);
        priorityQueue.enqueue(3);
        
        assertEquals(3, priorityQueue.size());
        
        priorityQueue.dequeue();
        assertEquals(2, priorityQueue.size());
    }

    @Test
    void testClear() {
        priorityQueue.enqueue(1);
        priorityQueue.enqueue(2);
        priorityQueue.enqueue(3);

        priorityQueue.clear();

        assertTrue(priorityQueue.isEmpty());
        assertEquals(0, priorityQueue.size());
    }

    @Test
    void testCustomComparator() {
        PriorityQueue<String> stringQueue = new PriorityQueue<>(Comparator.reverseOrder());
        
        stringQueue.enqueue("apple");
        stringQueue.enqueue("banana");
        stringQueue.enqueue("cherry");

        assertEquals("cherry", stringQueue.dequeue());
        assertEquals("banana", stringQueue.dequeue());
        assertEquals("apple", stringQueue.dequeue());
    }

    @Test
    void testDequeueEmptyQueue() {
        assertThrows(java.util.NoSuchElementException.class, () -> {
            priorityQueue.dequeue();
        });
    }

    @Test
    void testPeekEmptyQueue() {
        assertThrows(java.util.NoSuchElementException.class, () -> {
            priorityQueue.peek();
        });
    }
}