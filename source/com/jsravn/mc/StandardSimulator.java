package com.jsravn.mc;

class StandardSimulator implements Simulator {
    private Simulation sim;
    private int N;
    private double sum;
    private double sumOfSquares;
    private double mu;
    private double variance;
    private double fastError;

    StandardSimulator(Simulation sim) {
	this.sim = sim;
    }

    public void run() {
	N += 1;

	final double result = sim.simulate();
	sum += result;
	sumOfSquares += result * result;	
	mu = sum / N;
	variance = sumOfSquares / N - mu * mu;
	fastError = variance / N;
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

    public double error() {
	if (N == 0)
	    return Double.NaN;
	return -1.0;
    }
}