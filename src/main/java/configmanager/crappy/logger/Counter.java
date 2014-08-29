package configmanager.crappy.logger;

import configmanager.crappy.logger.Logger;

/**
 * Simple timer which outputs the logged time with a message to the logs
 * 
 * @author Allard van Altena
 */
public class Counter extends Logger {
	private Long startTime;
	private String MSG; 
	
	public Counter() {}
	
	/**
	 * Start a new counter
	 * @param msgIn Message to display when stopping the counter and displaying the results
	 */
	public void start(String msgIn) {
		MSG = msgIn;
		
		reset();
	}
	
	/**
	 * Set start time to current time
	 */
	public void reset() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * Stop the counter and log the result
	 */
	public void stop() {
		log("Execution time " + MSG + ": " + ((System.currentTimeMillis() - startTime) / 1000) + " sec", 5);
	}
}
