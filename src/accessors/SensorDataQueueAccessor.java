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
	 * Adds an item to the end of the LinkedList
	 * 
	 * @param sens
	 */
	public void queueSensor(SensorData sens) {
		synchronized(this) {
			Application.sdq.addLast(sens);
		}
	}
	
	/**
	 * Gets the first sensorData object on the list and removes it from the list
	 * @throws Queue exception that must be handled by the accessor.
	 * this is thrown for reasons that include not having any more items in the list
	 * @return
	 */
	public SensorData popFirst() throws QueueException{
		synchronized(this) {
			SensorData sens = getNextSensor();
			deleteSensor();
			return sens;
		}
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
	private SensorData getNextSensor() throws QueueException {
		synchronized(this) {
			try {
				return Application.sdq.getFirst();
			} catch (NoSuchElementException e) {
				throw new QueueException(e);
			}
		}
	}
	
	/**
	 * Removes the last item on a queue
	 */
	private void deleteSensor() throws QueueException {
		synchronized(this) {
			try {
				Application.sdq.removeFirst();
			} catch (NoSuchElementException e) {
				System.out.println("SensorDataQueueAccessor::NoSuchElementException while removing a sensor.");
				throw new QueueException(e);
			}
		}
	}
}
