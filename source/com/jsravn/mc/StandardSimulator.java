package com.jsravn.mc;

import java.lang.Math;
import java.lang.Runnable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

class StandardSimulator implements Simulator {
    private final Simulation sim;
    private final BootstrapError bootstrap;
    private int N;
    private double sum;
    private double sumOfSquares;
    private double mu;
    private double variance;
    private double fastError;

    StandardSimulator(Simulation sim) {
	this.sim = sim;
	bootstrap = new BootstrapError();
    }

    public void run() {
	updateMetrics(sim.simulate());
    }

    public void runMany(int iterations, double absoluteError,
			double relativeError) {
	runMany(iterations, absoluteError, relativeError, null);
    }

    public void runMany(int iterations, double absoluteError,
			double relativeError, Runnable callback) {
	for (int i = 0; i < iterations; i++) {
	    run();
	    
	    if (callback != null)
		callback.run();

	    if (errorMet(absoluteError, relativeError))
		break;
	}
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

    public void runManyThreaded(int iterations, double absoluteError,
				double relativeError, Runnable callback) {
	// Based on Java Concurrency In Practice recommendation.
	final int numThreads = Runtime.getRuntime().availableProcessors() + 1;
	final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
	final BlockingQueue<Double> queue = new LinkedBlockingQueue<Double>();
	for (int i = 0; i < numThreads; i++) {
	    exec.submit(new MiniSimulator(sim, queue));
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

    private boolean errorMet(double absolute, double relative) {
	if (fastError > 0.0
	    && (fastError < absolute || fastError < relative * mu)) {
	    return true;
	}
	return false;
    }

    private void updateMetrics(double result) {
	N += 1;
	sum += result;
	sumOfSquares += result * result;
	mu = sum / N;
	variance = sumOfSquares / N - mu * mu;
	fastError = Math.sqrt(variance / N);
	bootstrap.addSample(result);
    }

    public int iteration() {
	return N;
    }

    public double mu() {
	return mu;
    }

    public double variance() {
	return variance;
    }

    public double fastError() {
	return fastError;
    }

    public double error(int confidence) {
	return bootstrap.calcError(confidence);
    }
}