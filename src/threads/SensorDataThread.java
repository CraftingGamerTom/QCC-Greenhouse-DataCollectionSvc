package threads;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import accessors.SensorDataQueueAccessor;
import dataSets.SensorData;
import exceptions.QueueException;
import executable.Application;
import handlers.ConfigurationHandler;

/**
 * Thread to handle taking new sensor data and putting it into the database
 * 
 * @author Thomas Rokicki
 *
 */
public class SensorDataThread implements Runnable {

	private static Logger logger = Logger.getLogger(SensorDataThread.class);
	private static final SensorDataQueueAccessor qa = new SensorDataQueueAccessor();
	private String command;
	private MongoDatabase database;

	/**
	 * constructor
	 *
	 * @param s
	 *            command string
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
					// logger.info("Sleeping thread: " +
					// Thread.currentThread().getName());
					Thread.sleep(10 * 1000);
				}
			} catch (Exception e) {
				logger.error("Exception caught in message loop processing: " + e);
				e.printStackTrace();
			}
		}

		logger.info("Ending thread: " + Thread.currentThread().getName());

	}

	/**
	 * loop in thread doing the work In this case the sensor data is added to
	 * the database
	 *
	 * @return
	 */
	private boolean threadLoop() {
		boolean queueExists = true;
		while (queueExists) {
			SensorData sens;
			try {
				sens = qa.popFirst();

				// logger.info("Sensor: " + sens.getSensorId());
				putDataInDatabase(sens);

			} catch (QueueException e) {
				queueExists = false;
			}
		}
		return queueExists;
	}

	/**
	 * Takes a sensorData object and puts it into the Mongo Database Raw Data
	 * Table Continues to update the other related tables based on highs and
	 * lows
	 * 
	 * Must also check the sensor type to make sure it is not new. The Sensor
	 * Ids must be maintained in their own table because the web application
	 * needs to store a user friendly name for them which can be set from an
	 * admin account on the web application
	 * 
	 * @param sens
	 */
	private void putDataInDatabase(SensorData sens) {
		database = Application.client.getDatabase(ConfigurationHandler.databaseName);

		compareSensorNames(sens);
		pushLiveData(sens);
		pushRawData(sens);
		pushPersistantData(sens);

	}

	/**
	 * Checks against current contents of Database Adds sensor Name and type if
	 * it does not exist
	 */
	private void compareSensorNames(SensorData sens) {
		MongoCollection<Document> collection = null;
		try {
			collection = database.getCollection(ConfigurationHandler.sensorNamesCollection);

			// Looks for the sensor and returns if it exists
			Document searchResult = collection.find(Filters.eq("sensorId", sens.getSensorId())).first();
			if (searchResult != null) {
				return;
			}
			// Only hits this code if the SensorId does not exist.
			System.out.println("EMPTY - NOW WRITING");
			Document document = new Document();

			document.put("date", sens.getDate());
			document.put("sensorId", sens.getSensorId());
			document.put("type", sens.getType());

			collection.insertOne(document);

		} catch (Exception e) {

			System.out.println("Error in comparing sensorID names: ");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Puts data into database overwriting the past input based on the sensorId
	 */
	private void pushLiveData(SensorData sens) {
		try {

			MongoCollection<Document> collection = database.getCollection(ConfigurationHandler.liveDataCollection);

			// Form document to replace old one
			Document replacement = new Document();
			replacement.put("date", sens.getDate());
			replacement.put("sensorId", sens.getSensorId());
			replacement.put("type", sens.getType());
			replacement.put("value", sens.getValue());

			// Finds the document that matches the sensorId and replaces it
			Document returnedDoc = collection.findOneAndReplace(Filters.eq("sensorId", sens.getSensorId()),
					replacement);

			// If no sensor matches then it hits the code below
			if (returnedDoc == null) {
				System.out.println("LIVE DATA - SENSOR NOT FOUND: ADDING IT");

				// Renamed document to make more sense..
				Document document = replacement;
				collection.insertOne(document);

				return;
			}

		} catch (Exception e) {
			System.out.println("Error in pushing live data: ");
			e.printStackTrace();
		}

	}

	/**
	 * Pushes data to raw data collection
	 */
	private void pushRawData(SensorData sens) {
		MongoCollection<Document> collection = database.getCollection(ConfigurationHandler.rawDataCollection);
		Document document = new Document();
		document.put("date", sens.getDate());
		document.put("sensorId", sens.getSensorId());
		document.put("type", sens.getType());
		document.put("value", sens.getValue());

		collection.insertOne(document);
	}

	/**
	 * Checks data against the data in different time frames and puts the data
	 * into the database
	 */
	private void pushPersistantData(SensorData sens) {

		// Creates the list of collections to iterate through unless the
		// criteria is not met
		ArrayList<String> persistantCollections = new ArrayList<String>();
		persistantCollections.add(ConfigurationHandler.hourlyDataCollection);
		persistantCollections.add(ConfigurationHandler.dailyDataCollection);
		persistantCollections.add(ConfigurationHandler.weeklyDataCollection);
		persistantCollections.add(ConfigurationHandler.monthlyDataCollection);
		persistantCollections.add(ConfigurationHandler.yearlyDataCollection);

		MongoCollection<Document> collection = null;
		Document searchResult = null;

		// Filter Types
		// Same Sensor
		Bson sensorFilter = Filters.eq("sensorId", sens.getSensorId());
		// Greater than value
		Bson gtFilter = Filters.gt("highValue", sens.getValue());
		// Less than value
		Bson ltFilter = Filters.lt("lowValue", sens.getValue());

		// Update Types
		// Update just High Value
		Bson highUpdate = Updates.combine(Updates.set("date", sens.getDate()),
				Updates.set("highValue", sens.getValue()));
		// Update just Low Value
		Bson lowUpdate = Updates.combine(Updates.set("date", sens.getDate()), Updates.set("lowValue", sens.getValue()));

		// Search And Update HIGH VALUES
		for (int index = 0; index < persistantCollections.size(); index++) {

			try {
				// Iterates through the list of persistent collections
				collection = database.getCollection(persistantCollections.get(index));

				// Looks for the sensor and returns if it exists

				searchResult = collection.find(Filters.and(sensorFilter, gtFilter,
						determineDateFilter(sens, collection.getNamespace().getCollectionName()))).first();

				if (searchResult != null) {
					// Hits code if their is a higher value
					// Stops looking further
					break;
				}

				if (searchResult == null) {
					// Hits this code only if there is no sensor with greater
					// value or sensor doesn't exist
					searchResult = collection.findOneAndUpdate(
							Filters.and(sensorFilter,
									determineDateFilter(sens, collection.getNamespace().getCollectionName())),
							highUpdate);

					if (searchResult == null) {
						// Hits this code if the sensor does not exist.
						// Puts a new document in the collection.
						// Does not break as this will need to be done for all
						// further collections.
						System.out.println("PERSISTANT DATA - SENSOR NOT FOUND: ADDING IT");
						Document document = new Document();
						document.put("date", sens.getDate());
						document.put("sensorId", sens.getSensorId());
						document.put("type", sens.getType());
						document.put("highValue", sens.getValue());
						document.put("lowValue", sens.getValue());

						collection.insertOne(document);
					}
				}
			} catch (Exception e) {
				System.out.println("Error in pushing high persistant data: ");
				e.printStackTrace();
			}
		}

		// Search And Update LOW VALUES
		for (int index = 0; index < persistantCollections.size(); index++) {

			try {
				// Sets collection to hourly
				collection = database.getCollection(persistantCollections.get(index));

				// Looks for the sensor and returns if it exists

				searchResult = collection.find(Filters.and(sensorFilter, ltFilter,
						determineDateFilter(sens, collection.getNamespace().getCollectionName()))).first();

				if (searchResult != null) {
					// Hits code if their is a higher value
					// Stops looking further
					break;
				}

				if (searchResult == null) {
					// Hits this code only if there is no sensor with lower
					// value or sensor doesn't exist
					System.out.println("LOW null 1");
					searchResult = collection.findOneAndUpdate(
							Filters.and(sensorFilter,
									determineDateFilter(sens, collection.getNamespace().getCollectionName())),
							lowUpdate);

					if (searchResult == null) {
						// This should never be hit because above for loop will
						// handle new documents
						System.out.println("PERSISTANT DATA - SENSOR NOT FOUND: ADDING IT");

						Document document = new Document();
						document.put("date", sens.getDate());
						document.put("sensorId", sens.getSensorId());
						document.put("type", sens.getType());
						document.put("highValue", sens.getValue());
						document.put("lowValue", sens.getValue());

						collection.insertOne(document);
					}
				}
			} catch (Exception e) {
				System.out.println("Error in pushing low persistant data: ");
				e.printStackTrace();
			}
		}
	}

	private Bson determineDateFilter(SensorData sens, String collectionName) {

		try {
			Bson dateFilter = null;
			ZonedDateTime startDate = ZonedDateTime.parse(sens.getDate());
			ZonedDateTime endDate = startDate;

			if (collectionName.equals(ConfigurationHandler.hourlyDataCollection)) {
				startDate = ZonedDateTime.parse(sens.getDate());
				startDate = startDate.minusMinutes(startDate.getMinute());
				startDate = startDate.minusSeconds(startDate.getSecond());
				endDate = startDate;
				endDate = endDate.plusMinutes(59);
				endDate = endDate.plusSeconds(59);

			} else if (collectionName.equals(ConfigurationHandler.dailyDataCollection)) {
				startDate = ZonedDateTime.parse(sens.getDate());
				startDate = startDate.minusHours(startDate.getHour());
				startDate = startDate.minusMinutes(startDate.getMinute());
				startDate = startDate.minusSeconds(startDate.getSecond());
				endDate = startDate;
				endDate = endDate.plusHours(23);
				endDate = endDate.plusMinutes(59);
				endDate = endDate.plusSeconds(59);

			} else if (collectionName.equals(ConfigurationHandler.weeklyDataCollection)) {
				startDate = ZonedDateTime.parse(sens.getDate());
				startDate = startDate.minusDays((startDate.getDayOfMonth() - 1));
				startDate = startDate.minusHours(startDate.getHour());
				startDate = startDate.minusMinutes(startDate.getMinute());
				startDate = startDate.minusSeconds(startDate.getSecond());
				endDate = startDate;
				endDate = endDate.plusMonths(1);
				endDate = endDate.plusHours(23);
				endDate = endDate.plusMinutes(59);
				endDate = endDate.plusSeconds(59);

			} else if (collectionName.equals(ConfigurationHandler.monthlyDataCollection)) {
				startDate = ZonedDateTime.parse(sens.getDate());
				startDate = startDate.minusMonths(startDate.getMonthValue() - 1);
				startDate = startDate.minusDays((startDate.getDayOfMonth() - 1));
				startDate = startDate.minusHours(startDate.getHour());
				startDate = startDate.minusMinutes(startDate.getMinute());
				startDate = startDate.minusSeconds(startDate.getSecond());
				endDate = startDate;
				endDate = endDate.plusMonths(12);
				endDate = endDate.plusMonths(1);
				endDate = endDate.plusHours(23);
				endDate = endDate.plusMinutes(59);
				endDate = endDate.plusSeconds(59);

			} else if (collectionName.equals(ConfigurationHandler.yearlyDataCollection)) {
				startDate = ZonedDateTime.parse(sens.getDate());
				startDate = startDate.minusYears(10);
				startDate = startDate.minusMonths(startDate.getMonthValue() - 1);
				startDate = startDate.minusDays((startDate.getDayOfMonth() - 1));
				startDate = startDate.minusHours(startDate.getHour());
				startDate = startDate.minusMinutes(startDate.getMinute());
				startDate = startDate.minusSeconds(startDate.getSecond());
				endDate = startDate;
				endDate = endDate.plusYears(10);
				endDate = endDate.plusMonths(12);
				endDate = endDate.plusMonths(1);
				endDate = endDate.plusHours(23);
				endDate = endDate.plusMinutes(59);
				endDate = endDate.plusSeconds(59);

			} else {
				System.out.println("SensorDataThread - couldnt handle time frame");
				endDate = startDate;

			}

			Bson startFilter = Filters.gte("date", startDate.toString());
			Bson endFilter = Filters.lt("date", endDate.toString());
			dateFilter = Filters.and(startFilter, endFilter);

			return dateFilter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
