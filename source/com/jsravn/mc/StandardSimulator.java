package com.jsravn.mc;

import java.lang.Math;

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

    protected boolean errorMet(double absolute, double relative) {
	if (fastError > 0.0
	    && (fastError < absolute || fastError < relative * mu)) {
	    return true;
	}
	return false;
    }

    protected void updateMetrics(double result) {
	N += 1;
	sum += result;
	sumOfSquares += result * result;
	mu = sum / N;
	variance = sumOfSquares / N - mu * mu;
	fastError = Math.sqrt(variance / N);
	bootstrap.addSample(result);
    }

    public Simulation simulation() {
	return sim;
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