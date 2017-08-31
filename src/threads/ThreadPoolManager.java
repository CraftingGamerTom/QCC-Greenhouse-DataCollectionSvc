
package threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

	private static final int SENSORDATATHREADS = 5;
	private ExecutorService executor = Executors.newFixedThreadPool(SENSORDATATHREADS);

	/**
	 * constructor
	 */
	public ThreadPoolManager() {
	}

	/**
	 * start thread pools
	 */
	public void startThreads() {
		startSensorDataThreads();
	}

	/**
	 * shutdown thread pools
	 */
	public void stopThreads() {
		shutdownSensorDataThreads();
	}

	/**
	 * start SensorData threads
	 */
	private void startSensorDataThreads() {
		for (int i = 1; i <= SENSORDATATHREADS; i++) {
			Runnable wt = new SensorDataThread(String.format("SensorDataThread%d",i));
			executor.execute(wt);
		}
	}
	
	/**
	 * Shuts down Sensor Data Threads
	 */
	private void shutdownSensorDataThreads() {
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
}
