package com.github.hanzm_10.murico.swingapp.lib.cache;

import java.util.HashMap;

public class LRU<K, V> {
    private int length;
    private int capacity;

    private Node<V> tail;
    private Node<V> head;

    private HashMap<K, Node<V>> lookup;
    private HashMap<Node<V>, K> reverseLookup;

    public LRU(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.capacity = capacity;

        lookup = new HashMap<>();
        reverseLookup = new HashMap<>();
    }

    public boolean containsKey(K key) {
        return lookup.containsKey(key);
    }

    private void detach(Node<V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (head == node) {
            head = head.next;
        }

        if (tail == node) {
            tail = tail.prev;
        }

        node.next = null;
        node.prev = null;
    }

    public V get(K key) {
        var node = lookup.get(key);

        if (node == null) {
            return null;
        }

        detach(node);
        prepend(node);

        return node.value;
    }

    private void prepend(Node<V> node) {
        if (head == null) {
            head = tail = node;
            return;
        }

        node.next = head;
        head.prev = node;
        head = node;
    }

    private void trimCache() {
        if (length <= capacity) {
            return;
        }

        var referenceToTail = tail;

        detach(tail);

        var key = reverseLookup.get(referenceToTail);

        lookup.remove(key);
        reverseLookup.remove(referenceToTail);
        length -= 1;
    }

    public void update(K key, V value) {
        var node = lookup.get(key);

        if (node == null) {
            node = Node.createNode(value);

            length += 1;
            prepend(node);
            trimCache();

            lookup.put(key, node);
            reverseLookup.put(node, key);
        } else {
            detach(node);
            prepend(node);

            node.value = value;
        }
    }
}


class Node<T> {
    public static <T> Node<T> createNode(T data) {
        return new Node<>(data);
    }

    T value;
    Node<T> prev;
    Node<T> next;

    public Node(T value) {
        this.value = value;
        prev = null;
        next = null;
    }
}
