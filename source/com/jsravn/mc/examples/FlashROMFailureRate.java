package com.jsravn.mc.examples;

import com.jsravn.mc.Simulations;
import com.jsravn.mc.Simulation;
import com.jsravn.mc.Simulator;
import com.jsravn.mc.Runner;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Integer;

/**
 * Let's say you've been asked to come up with a failure estimate for this
 * scenario:
 *
 * Flash ROM size of 1024 blocks.
 * Consists of many segments of varying block sizes.
 * 10 to 15 randomly located blocks are bad.
 * Flashing fails if any segment has 2 or more bad blocks.
 *
 * This simulation will provide an estimate of the failure rate. In this case,
 * it turns out to be 53%.
 */
public class FlashROMFailureRate implements Simulation {
    final Random rand;

    public FlashROMFailureRate() {
	rand = new Random();
    }

    public double simulate() {
	final int bad = rand.nextInt(15);
	final ArrayList<Integer> flash = new ArrayList<Integer>(1024);
	for (int i = 0; i < bad; i++)
	    flash.add(1);
	for (int i = bad; i < 1024; i++)
	    flash.add(0);
	Collections.shuffle(flash);
	
	/* Segment boundaries */
	final int[] boundary = { 30, 50, 95, 125, 160, 200, 210,
				 250, 300, 350, 400, 450, 500,
				 550, 600, 640, 680, 700, 750,
				 800, 820, 840, 860, 880, 900,
				 970, 1024 };
	for (int i = 0, j = 0; i < boundary.length; i++) {	    
	    for (int found = 0; j < boundary[i] ; j++) {
		if (flash.get(j) == 1)
		    found++;
		if (found > 1)
		    return 1.0;
	    }
	}
	return 0.0;
    }

    public static void main(String args[]) {
	Simulations.newTextRunner(args, new FlashROMFailureRate()).run();
    }
}