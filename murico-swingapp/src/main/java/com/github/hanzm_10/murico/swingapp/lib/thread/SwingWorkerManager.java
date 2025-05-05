package com.github.hanzm_10.murico.swingapp.lib.thread;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.jetbrains.annotations.NotNull;

/**
 * A utility class that manages the lifecycle of {@link SwingWorker} instances
 * used in a Swing application.
 * <p>
 * This class provides a centralized way to register and cleanly shut down
 * background workers. All registered workers are executed immediately and
 * tracked for later cancellation.
 * </p>
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * SwingWorker<Void, Void> worker = new SomeWorker();
 * WorkerManager.register(worker);
 * ...
 * WorkerManager.shutdown(); // typically called during application shutdown
 * }</pre>
 * </p>
 *
 */
public class SwingWorkerManager {
	private static final List<SwingWorker<?, ?>> workers = new ArrayList<>();

	/**
	 * Registers and immediately executes a {@link SwingWorker} instance.
	 * <p>
	 * The worker will be tracked and can later be canceled during shutdown.
	 * </p>
	 *
	 * @param worker the non-null {@code SwingWorker} to register and execute
	 */
	public static synchronized void register(@NotNull final SwingWorker<?, ?> worker) {
		workers.add(worker);
		worker.execute();
	}

	/**
	 * Cancels all running or pending workers that have been registered.
	 * <p>
	 * This method removes all tracked workers and cancels any that have not yet
	 * completed. Intended to be called during application shutdown.
	 * </p>
	 */
	public static synchronized void shutdown() {
		while (workers.size() != 0) {
			var worker = workers.removeFirst();

			if (!worker.isDone()) {
				worker.cancel(true);
			}
		}
	}
}
