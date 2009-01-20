package com.jsravn.mc;

/**
 * Factory class for generating simulation related objects.
 */
public final class Simulations {
    private Simulations() { }

    /**
     * Returns a new Simulator that will run the given simulation.
     * @param sim the Simulation the simulator will run.
     */
    public static Simulator newSimulator(Simulation sim) {
	return new StandardSimulator(sim);
    }

    /**
     * Returns a new Runner that will run the given simulation and output the
     * results on the command line. Use this instead of a Simulator if you
     * don't need the extra flexibility. It is much less work to use a Runner
     * than a Simulator.
     *
     * @param args is an array of arguments, usually passed on the command
     * line. Their interpretation is handled by the Runner, and a help menu
     * will be output to the console if arguments are omitted.
     * @param sim is the Simulation to run.
     */
    public static Runner newTextRunner(String[] args, Simulation sim) {
	return new SimpleRunner(args, sim);
    }
}