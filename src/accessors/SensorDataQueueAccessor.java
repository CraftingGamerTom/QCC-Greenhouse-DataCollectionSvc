package accessors;

import java.util.NoSuchElementException;

import dataSets.SensorData;
import exceptions.QueueException;
import executable.Application;

public class SensorDataQueueAccessor {

	/**
	 * constructor
	 */
	public SensorDataQueueAccessor() {
	}

	/**
	 * Gets a sensor from the 'front' of the list. Called a getFirst on the
	 * linked list but is ready to throw an exception so our threads know when
	 * to sleep.
	 * 
	 * @return Sensor with all the data we care about inside
	 * @throws QueueException
	 *             to allow the thread to sleep
	 */
	public SensorData getNextSensor() throws QueueException {
		try {
			return Application.sdq.getFirst();
		} catch (NoSuchElementException e) {
			throw new QueueException(e);
		}
	}

	/**
	 * Adds an item to the end of the LinkedList
	 * 
	 * @param sens
	 */
	public void queueSensor(SensorData sens) {
		Application.sdq.addLast(sens);
	}

	/**
	 * Removes the last item on a queue
	 */
	public void deleteSensor(SensorData sens) throws QueueException {
		try {
			Application.sdq.removeLast();
		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException while removing a sensor.");
			throw new QueueException(e);
		}
	}
}
