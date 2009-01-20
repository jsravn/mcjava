package com.jsravn.mc;

import java.lang.Runnable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

class ThreadedSimulator extends StandardSimulator {
    ThreadedSimulator(Simulation sim) {
	super(sim);
    }

    class MiniSimulator implements Runnable {
	final Simulation sim;
	final BlockingQueue<Double> queue;
	MiniSimulator(Simulation sim, BlockingQueue<Double> queue) {
	    this.sim = sim;
	    this.queue = queue;
	}
	public void run() {
	    while (!Thread.currentThread().isInterrupted()) {
		try {
		    queue.put(sim.simulate());
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		    break;
		}
	    }
	}
    }

    @Override
    public void runMany(int iterations, double absoluteError,
			double relativeError, Runnable callback) {
	// Based on Java Concurrency In Practice recommendation.
	final int numThreads = Runtime.getRuntime().availableProcessors() + 1;
	final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
	final BlockingQueue<Double> queue = new LinkedBlockingQueue<Double>();
	for (int i = 0; i < numThreads; i++) {
	    exec.submit(new MiniSimulator(simulation(), queue));
	}

	int count = 0;
	double result;
	while (true) {
	    try {
		result = queue.take();
	    } catch (InterruptedException e) {
		break;
	    }

	    count++;
	    updateMetrics(result);
	    if (callback != null)
		callback.run();

	    if (count >= iterations || errorMet(absoluteError, relativeError))
		break;
	}

	exec.shutdownNow();
    }
}