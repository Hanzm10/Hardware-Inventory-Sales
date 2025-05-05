package com.github.hanzm_10.murico.swingapp.lib.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class TestDebouncer {
	@Test
	void testCancelPreventsExecution() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Debouncer debouncer = new Debouncer(100);

		debouncer.call(latch::countDown);
		debouncer.cancel();

		boolean completed = latch.await(300, TimeUnit.MILLISECONDS);
		assertFalse(completed, "Action should not run after cancel");
	}

	@Test
	void testDebounceExecutesAfterDelay() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Debouncer debouncer = new Debouncer(100);

		debouncer.call(latch::countDown);

		// Wait up to 500ms for the latch to count down
		boolean completed = latch.await(500, TimeUnit.MILLISECONDS);

		assertTrue(completed, "Debounced action should have been called after delay");
	}

	@Test
	void testDebounceSkipsEarlierCall() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Debouncer debouncer = new Debouncer(100);

		debouncer.call(() -> fail("First call should be skipped"));
		Thread.sleep(50);
		debouncer.call(latch::countDown);

		boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
		assertTrue(completed, "Only the last debounced call should have executed");
	}
}
