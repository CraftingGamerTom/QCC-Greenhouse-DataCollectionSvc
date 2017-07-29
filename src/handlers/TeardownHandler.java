package handlers;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import executable.ConfigurationReader;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
/**
 * This class is in charge of handling data that is sent to this server.
 * @version 0.0.1
 * @author Thomas Rokicki
 *
 */
public class TeardownHandler {


	/**
	 * Takes a JSON String and parses it into objects. For more information on 
	 * object types and formating see the README file for the project.
	 * 
	 * @param in the JSON String.
	 */
	public void parseSensorJson(String in) {

		final ObjectMapper mapper = new ObjectMapper();
		final String tableName = ConfigurationReader.rawSensorTable;
		try {
			JsonNode json = mapper.readTree(in);
			JsonNode dateNode = json.get("date");
			ArrayNode arrayOfData = (ArrayNode) json.get("sensors");
			
			DatabaseHandler dbh = new DatabaseHandler(tableName);
			dbh.addSensorDataToDatabase(arrayOfData, dateNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Takes a JSON String and parses it into objects. For more information on 
	 * object types and formating see the README file for the project.
	 * 
	 * @param in the JSON String.
	 */
	public void parseObservationJson(String in) {

		final ObjectMapper mapper = new ObjectMapper();
		final String tableName = ConfigurationReader.observationTable;
		try {
			JsonNode json = mapper.readTree(in);
			JsonNode dateNode = json.get("date");
			JsonNode usernameNode = json.get("username");
			JsonNode observationNode = json.get("observation");
			
			DatabaseHandler dbh = new DatabaseHandler(tableName);
			dbh.addObservationDataToDatabase(dateNode, usernameNode, observationNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// ---------------------------------------------
	/**
	 * Currently not used - Intention is to check if the JSON object exists to
	 * prevent any exceptions from occurring.
	 * @param object to be tested.
	 * @param key a value belonging to that object.
	 * @return true if it exists, false if it does not.
	 */
	public static boolean objectExists(JSONObject object, String key) {
		Object o;

		try {
			o = object.get(key);
		} catch (Exception e) {
			return false;
		}

		return o != null;
	}

}
