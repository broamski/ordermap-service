// A stopwatch class.
package com.nuszkowski.utils;

public class Timer {

	private long start;
	private long stop;
	private long elapsed;

	public void start() {
		start = System.currentTimeMillis();
	}

	public void stop() {
		stop = System.currentTimeMillis();
	}

	public long getElapsed() {
		elapsed = stop - start;
		return elapsed;
	}
}
