package threads;

import org.apache.log4j.Logger;

import accessors.SensorDataQueueAccessor;
import dataSets.Sensor;
import exceptions.QueueException;

public class SensorDataThread implements Runnable {

	private static Logger logger = Logger.getLogger(SensorDataThread.class);
	private static final SensorDataQueueAccessor qa = new SensorDataQueueAccessor();
	private String command;
	
	/**
	 * constructor
	 *
	 * @param s command string
	 */
	public SensorDataThread(String s) {
		this.command = s;
	}
	
	/**
	 * Checks if it should sleep or do work and does it
	 */
	@Override
	public void run() {
		logger.info("Starting thread: " + Thread.currentThread().getName() + " Start. Command = " + command);

		boolean dataWaiting = true;
		while (dataWaiting) {
			try {
				if (!threadLoop()) {
					// logger.info("Sleeping thread: " + Thread.currentThread().getName());
					Thread.sleep(10*1000);
				}
			} catch (Exception e) {
				logger.error("Exception caught in message loop processing: " + e);
			}
		}

		logger.info("Ending thread: " + Thread.currentThread().getName());

	}

	/**
	 * loop in thread doing the work
	 * In this case the sensor data is added to the database
	 *
	 * @return
	 */
	private boolean threadLoop() {
		boolean queueExists = true;
		while (queueExists) {
			Sensor sens;
			try {
				sens = qa.getNextSensor();

				/* do work here */
				// logger.info("Sensor: " + sens.getId());
				putDataInDatabase(sens);

				qa.deleteSensor(sens);
			} catch (QueueException e) {
				queueExists = false;
			}
		}
		return queueExists;
	}

	private void putDataInDatabase(Sensor sens) {
		// TODO Auto-generated method stub
		
	}
}
