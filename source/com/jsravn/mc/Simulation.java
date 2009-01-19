package com.jsravn.mc;

/**
 * This class is implemented by your simulation. Implement the simulate method
 * and pass your object into a SimulateMany object.
 */
public interface Simulation {
    /**
     * Performs a single simulation that returns a float representing the
     * result.
     *
     * In the case of a Bernoulli experiment (yes/no result), this function
     * could return 0.0 for a no answer, and 1.0 for a yes answer. Similarly
     * for true/false.
     */
    double simulate();
}
