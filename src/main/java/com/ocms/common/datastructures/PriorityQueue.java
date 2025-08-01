package com.ocms.common.datastructures;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueue<T> {
    
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> head;
    private final Comparator<T> comparator;
    private int size;
    
    public PriorityQueue(Comparator<T> comparator) {
        this.head = null;
        this.comparator = comparator;
        this.size = 0;
    }
    
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null || comparator.compare(data, head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null && comparator.compare(data, current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }
    
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return head.data;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}