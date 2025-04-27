/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.cache;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple LRU (Least Recently Used) cache implementation.
 *
 * @param <K> The type of the keys in the cache.
 * @param <V> The type of the values in the cache.
 */
public class LRU<K, V> {
    private int length;
    private int capacity;

    private Node<V> tail;
    private Node<V> head;

    private HashMap<K, Node<V>> lookup;
    private HashMap<Node<V>, K> reverseLookup;

    private ArrayList<LRUListener<K, V>> listeners = new ArrayList<>();

    @FunctionalInterface
    public interface LRUListener<K, V> {
        void onEvict(K key, V value);
    }

    public void addListener(LRUListener<K, V> listener) {
        listeners.add(listener);
    }

    public void removeListener(LRUListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value) {
        for (var listener : listeners) {
            listener.onEvict(key, value);
        }
    }

    public LRU(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.capacity = capacity;

        lookup = new HashMap<>();
        reverseLookup = new HashMap<>();
    }

    public synchronized boolean containsKey(K key) {
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

    /**
     * Returns the value associated with the key in the cache. If the key is not
     * present, it returns null.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or null if not found.
     */
    public synchronized V get(K key) {
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

        notifyListeners(key, referenceToTail.value);

        lookup.remove(key);
        reverseLookup.remove(referenceToTail);
        length -= 1;
    }

    /**
     * Updates the cache with the given key and value. If the key already exists, it
     * updates the value and moves the node to the front of the cache.
     *
     * @param key   The key to update.
     * @param value The value to associate with the key.
     */
    public synchronized void update(K key, V value) {
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

    public synchronized void clear() {
        while (length > 0) {
            trimCache();
        }
    }

    public synchronized V remove(K key) {
        var node = lookup.get(key);

        if (node == null) {
            return null;
        }

        detach(node);

        notifyListeners(key, node.value);

        lookup.remove(key);
        reverseLookup.remove(node);
        length -= 1;

        return node.value;
    }

    public synchronized HashMap<K, V> getAll() {
        var result = new HashMap<K, V>();

        for (var entry : lookup.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue().value;

            result.put(key, value);
        }

        return result;
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
