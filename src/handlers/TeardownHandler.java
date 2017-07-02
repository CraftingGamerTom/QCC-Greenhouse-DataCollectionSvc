package handlers;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import sensors.Sensor;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
/**
 * This class is in charge of handling data that is sent to this server.
 * @version 0.0.1
 * @author Thomas Rokicki
 *
 */
public class TeardownHandler {

	private String test = "{\"date\":\"01-01-2017T12:01:00Z\", \"sensors\":[{\"id\":\"/arduino1/temperature1\", \"type\":\"temperature\", \"value\":\"test\"}, {\"id\":\"/arduino1/humidity1\", \"type\":\"humidity\", \"value\":22}] }";

	/**
	 * Takes a JSON String and parses it into objects. For more information on 
	 * object types and formating see the README file for the project.
	 * 
	 * @param in the JSON String.
	 */
	public void parseJson(String in) {

		final ObjectMapper mapper = new ObjectMapper();
		final String databaseName = "greenhouseTest";
		try {
			JsonNode json = mapper.readTree(in);
			JsonNode dateNode = json.get("date");
			ArrayNode arrayOfData = (ArrayNode) json.get("sensors");
			
			DatabaseHandler dbh = new DatabaseHandler(databaseName);
			dbh.addSensorDataToDatabase(arrayOfData, dateNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void test() {
		parseJson(test);
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
