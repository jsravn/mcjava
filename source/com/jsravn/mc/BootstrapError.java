package com.jsravn.mc;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;
import java.lang.IllegalArgumentException;

class BootstrapError {
    final ArrayList<Double> samples;
    final int INITIAL_CAPACITY = 1024;
    final Random rand;

    BootstrapError() {
	samples = new ArrayList<Double>(INITIAL_CAPACITY);
	rand = new Random();
    }

    /**
     * Add a sample to the bootstrap distribution.
     */
    void addSample(double sample) {
	samples.add(sample);
    }

    /**
     * Samples the bootstrap distribution, returning a random value.
     */
    double sample() {
	return samples.get(rand.nextInt(samples.size()));
    }

    /**
     * Calculate an error value on the bootstrap distribution with the given
     * confidence interval.
     */
    double calcError(int confidence) {
	if (0 >= confidence || confidence >= 100)
	    throw new IllegalArgumentException("Confidence must be in the (0,100) range.");

	if (samples.size() == 0)
	    return Double.NaN;

	final ArrayList<Double> means = new ArrayList<Double>(201);
	double mean;
	int i, j;
	
	for (i = 0; i < 201; i++) {
	    mean = 0.0;
	    for (j = 0; j < samples.size(); j++) {
		mean += sample() / samples.size();
	    }
	    means.add(mean);
	}

	Collections.sort(means);
	return (means.get(100 + confidence) - means.get(100 - confidence)) / 2.0;
    }
}
