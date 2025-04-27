package com.github.hanzm_10.murico.swingapp.lib.observer;

/**
 * A simple interface for a subscriber in the observer pattern.
 *
 * @param <V> The type of the value that the subscriber receives.
 */
@FunctionalInterface
public interface Subscriber<V> {
    void notify(V value);
}
