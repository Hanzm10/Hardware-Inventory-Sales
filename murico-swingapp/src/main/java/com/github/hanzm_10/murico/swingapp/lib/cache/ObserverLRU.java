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
import java.util.List;

import com.github.hanzm_10.murico.swingapp.lib.observer.Observer;
import com.github.hanzm_10.murico.swingapp.lib.observer.Subscriber;

/** Useful for observing the LRU cache when it evicts an item. */
public class ObserverLRU<K, V> extends LRU<K, V> implements Observer<V> {
	protected List<Subscriber<V>> subscribers;

	public ObserverLRU(int capacity) {
		super(capacity);

		subscribers = new ArrayList<>();
	}

	@Override
	public void notifySubscribers(V value) {
		for (var subscriber : subscribers) {
			subscriber.notify(value);
		}
	}

	@Override
	public synchronized V remove(K key) {
		var val = super.remove(key);

		if (val != null) {
			notifySubscribers(val);
		}

		return val;
	}

	/**
	 * Used for whenever a caller, that's also a subscriber, does not need to be
	 * updated since it's contextually aware that the item it's trying to remove is,
	 * well... removed. Of course if {@code
	 * V} is null, then the subscribers will also simply not be called.
	 *
	 * @param key
	 * @param shouldPublishUpdate
	 * @return
	 */
	public synchronized V remove(K key, boolean shouldPublishUpdate) {
		var val = super.remove(key);

		if (val != null && shouldPublishUpdate) {
			notifySubscribers(val);
		}

		return val;
	}

	@Override
	public void subscribe(Subscriber<V> subscriber) {
		if (subscribers.contains(subscriber)) {
			return;
		}

		subscribers.add(subscriber);
	}

	@Override
	protected V trimCache() {
		var val = super.trimCache();

		if (val != null) {
			notifySubscribers(val);
		}

		return val;
	}

	@Override
	public void unsubscribe(Subscriber<V> subscriber) {
		if (!subscribers.contains(subscriber)) {
			return;
		}

		subscribers.remove(subscriber);
	}
}
