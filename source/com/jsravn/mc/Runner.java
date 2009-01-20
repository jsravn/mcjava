package com.jsravn.mc;

/**
 * Defines an object that can run a Simulation. Objects that implement this
 * should provide an easy way to simulate without requiring the user to use a
 * Simulator directly.
 */
public interface Runner {
    /**
     * Run it.
     */
    public void run();
}