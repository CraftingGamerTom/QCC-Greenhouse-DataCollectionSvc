package handlers;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
/**
 * This class handles anything this Listener must do with the database.
 * 
 * @version 0.0.1
 * @author Thomas Rokicki
 *
 */
public class DatabaseHandler {

	private MongoClient mongo;
	private MongoDatabase database;
	private ArrayNode sensorArray;

	/**
	 * Creates a new MongoDB Connection to handle anything that requires access
	 * to the database.
	 * 
	 * @param database
	 *            name
	 */
	DatabaseHandler(String tableName) {
			// Connects to the MongoDB (Set in Configuration File)
			mongo = new MongoClient(ConfigurationHandler.databaseIP, ConfigurationHandler.databasePort);
			database = mongo.getDatabase(tableName);
	}

	/**
	 * Used to add data from the raspberry pi to the mongoDB. This method with
	 * will return a boolean value to show if the process occurred properly. 
	 * BOOLEAN RETURN IS BROKEN (Must reset in for-loop).
	 * 
	 * @param arrayOfData
	 *            the object oriented data.
	 * @param dateNode
	 *            the date and time that the data was logged.
	 * @return isValidType checks if the data is able to be handled. DOES NOT WORK.
	 */
	public boolean addSensorDataToDatabase(ArrayNode arrayOfData, JsonNode dateNode) {
		boolean isValidType = false; 
		sensorArray = arrayOfData;

		// Compute size of array and iterate through it
		int size = sensorArray.size();
		for (int i = 0; i < size; i++) {
			// Gathers data per instance, and accesses collection
			JsonNode sensor = sensorArray.get(i);
			JsonNode sensorId = sensor.get("id");
			JsonNode sensorType = sensor.get("type");
			JsonNode sensorValue = sensor.get("value");
			String date = dateNode.asText();
			MongoCollection<Document> table = database.getCollection(sensorId.toString());

			// Creates document and gathers information
			Document document = new Document();
			document.put("type", sensorType.toString());
			document.put("date", date);

			// Determine type and push according date

			if (sensorType.toString().contains("door")) {
				document.put("value", sensorValue.asBoolean());
				isValidType = true;
			} else if (sensorType.toString().contains("motor")) {
				document.put("value", sensorValue.asBoolean());
				isValidType = true;
			} else if (sensorType.toString().contains("temperature")) {
				document.put("value", sensorValue.asDouble());
				isValidType = true;
			} else if (sensorType.toString().contains("humidity")) {
				document.put("value", sensorValue.asDouble());
				isValidType = true;
			} else if (sensorType.toString().contains("water")) {
				document.put("value", sensorValue.asDouble());
				isValidType = true;
			} else if (sensorType.toString().contains("ph")) {
				document.put("value", sensorValue.asDouble());
				isValidType = true;
			} else {
				document.put("value", -999); // To show something went wrong
				isValidType = false;
			}
			table.insertOne(document);
			
		}
		return isValidType;
	}

	public void addObservationDataToDatabase(JsonNode dateNode, JsonNode usernameNode, JsonNode observationNode) {
		MongoCollection<Document> table = database.getCollection("observations");

		Document document = new Document();
		document.put("date", dateNode.asText());
		document.put("username", usernameNode.asText());
		document.put("observation", observationNode.asText());
		
		table.insertOne(document);
	}
}
