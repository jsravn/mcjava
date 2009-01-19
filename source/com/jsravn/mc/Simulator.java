package com.jsravn.mc;

/**
 * A simulator runs a simulation and gathers statistics on the output.
 */
public interface Simulator {
    /**
     * Runs the simulation a single time, adding the result to its metrics.
     */
    public void run();

    /**
     * Returns the number of completed iterations.
     */
    public int iteration();

    /**
     * Returns the mean value of simulation values.
     */
    public double mu();

    /**
     * Returns the variance of simulation values.
     */
    public double variance();

    /**
     * Returns a fast approximation of error on mu. Usually good enough for
     * determining when to stop a simulation.
     */
    public double fastError();

    /**
     * Returns a more accurate approximation of error on mu. Don't use this
     * every iteration or your program will be very slow! It uses a bootstrap
     * algorithm by approximating the output distribution through simulation
     * samples.
     *
     * @param confidence is the confidence interval you want on your
     * error. For example, 67 would represent a 67% confidence interval
     * (roughly equal to one standard deviation in a normal distribution).
     * @precondition confidence should be in the range (0, 100)
     */
    public double error(int confidence);
}
