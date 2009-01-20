package com.jsravn.mc;

class SimpleRunner implements Runner {
    final Simulator sim;
    final int N;
    final double absoluteError;
    final double relativeError;

    SimpleRunner(String args[], Simulation simulation) {
	if (args.length != 3) {
	    System.out.println("Correct usage: java <simulation> N absolute relative");
	    System.exit(-1);
	}
	N = Integer.parseInt(args[0]);
	absoluteError = Double.parseDouble(args[1]);
	relativeError = Double.parseDouble(args[2]);
	this.sim = Simulations.newSimulator(simulation);
    }

    final Runnable printer = new Runnable() {
	    public void run() {
		if (sim.iteration() % 500 == 0) {
		    System.out.printf("%d,%f,%f,%f\n",
				      sim.iteration(), sim.mu(),
				      sim.variance(), sim.fastError());
		}
	    }
	};

    public void run() {
	System.out.println("n,mu,var,fasterr");
	sim.runMany(N, absoluteError, relativeError, printer);

	System.out.println("\nFinal output:");
	System.out.printf("N: %d\n", sim.iteration());
	System.out.printf("mu: %f\n", sim.mu());
	System.out.printf("var: %f\n", sim.variance());
	System.out.printf("err(67): %f\n", sim.error(67));
    }
}