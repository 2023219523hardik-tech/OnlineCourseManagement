package com.ocms.common.datastructures;

import lombok.Data;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {
    
    @Data
    private static class Node<T> {
        private T data;
        private Node<T> next;
        
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }
    
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
        }
        size++;
    }
    
    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        
        T data = head.getData();
        head = head.getNext();
        size--;
        
        if (head == null) {
            tail = null;
        }
        
        return data;
    }
    
    public T removeLast() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        
        T data;
        if (head == tail) {
            data = head.getData();
            head = null;
            tail = null;
        } else {
            Node<T> current = head;
            while (current.getNext() != tail) {
                current = current.getNext();
            }
            data = tail.getData();
            current.setNext(null);
            tail = current;
        }
        size--;
        return data;
    }
    
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }
    
    public void set(int index, T data) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setData(data);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}