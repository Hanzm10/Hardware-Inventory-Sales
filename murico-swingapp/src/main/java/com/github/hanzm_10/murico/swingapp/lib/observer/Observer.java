package com.github.hanzm_10.murico.swingapp.lib.observer;

public interface Observer<V> {
    void notifySubscribers(V value);

    void subscribe(Subscriber<V> subscriber);

    void unsubscribe(Subscriber<V> subscriber);
}
