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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.hanzm_10.murico.swingapp.lib.observer.Subscriber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestsObserverLRU {
	@Test
	@DisplayName("test trimCache() to only notify subscribers when exceeding limit.")
	void testObserver() {
		ObserverLRU<String, String> lruObs = new ObserverLRU<String, String>(2);
		var isCalled = new AtomicBoolean(false);
		Subscriber<String> l = (_) -> {
			isCalled.set(true);
		};

		lruObs.subscribe(l);
		lruObs.update("foo", "bar");
		lruObs.update("deez", "nuts");

		assertFalse(isCalled.get());

		lruObs.update("say", "what");

		assertTrue(isCalled.get());

		lruObs.unsubscribe(l);
	}

	@Test
	@DisplayName("test ObserverLRU to not cause indirect recursion when remove() is called by a subscriber")
	void testObserverIndirectRecursion() {
		ObserverLRU<String, String> lruObs = new ObserverLRU<String, String>(2);
		var calls = new AtomicInteger(0);
		Subscriber<String> l2 = (s) -> {
			// basically, if the remove() invocation inside this lambda function calls this
			// subscriber lambda function, then calls != 0 by then. Verifies that the
			// subscriber is only ever called once for the same key that's being removed
			assertEquals(0, calls.getAndIncrement());
			assertNotNull(s);
			lruObs.remove("foo");
		};

		lruObs.update("foo", "bar");
		lruObs.update("deez", "nuts");
		lruObs.subscribe(l2);
		lruObs.remove("foo");
		lruObs.unsubscribe(l2);
	}
}
