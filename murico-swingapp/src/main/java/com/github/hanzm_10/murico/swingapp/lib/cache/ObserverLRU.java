package com.github.hanzm_10.murico.swingapp.lib.cache;

import java.util.List;

import com.github.hanzm_10.murico.swingapp.lib.observer.Observer;
import com.github.hanzm_10.murico.swingapp.lib.observer.Subscriber;

/**
 * Useful for observing the LRU cache when it evicts an item.
 */
public class ObserverLRU<K, V> extends LRU<K, V> implements Observer<V> {
    protected List<Subscriber<V>> subscribers;

    public ObserverLRU(int capacity) {
        super(capacity);
    }

    @Override
    protected V trimCache() {
        var val = super.trimCache();
        notifySubscribers(val);

        return val;
    }

    @Override
    public void notifySubscribers(V value) {
        for (var subscriber : subscribers) {
            subscriber.notify(value);
        }
    }

    @Override
    public void subscribe(Subscriber<V> subscriber) {
        if (subscribers.contains(subscriber)) {
            return;
        }

        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber<V> subscriber) {
        if (!subscribers.contains(subscriber)) {
            return;
        }

        subscribers.remove(subscriber);
    }

}
