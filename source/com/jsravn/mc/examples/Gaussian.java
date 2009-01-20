package com.jsravn.mc.examples;

import com.jsravn.mc.Simulations;
import com.jsravn.mc.Simulation;
import com.jsravn.mc.Simulator;
import com.jsravn.mc.Runner;

import java.util.Random;

/**
 * Test the gaussian distribution against the simulator.
 */
public class Gaussian implements Simulation {
    final Random rand;

    Gaussian() {
	rand = new Random();
    }
    
    public double simulate() {
	return rand.nextGaussian();
    }

    public static void main(String args[]) {
	Simulator sim = Simulations.newSimulator(new Gaussian());
	Simulations.newTextRunner(args, sim).run();
    }
}
