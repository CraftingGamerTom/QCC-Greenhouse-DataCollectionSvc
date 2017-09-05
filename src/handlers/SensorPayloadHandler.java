package handlers;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import accessors.SensorDataQueueAccessor;
import dataSets.SensorData;
import payloads.SensorDataPayload;

public class SensorPayloadHandler extends CommonPayloadHandler{

	private ArrayList<SensorData> allSensors = new ArrayList<SensorData>();
	
	public SensorPayloadHandler(SensorDataPayload payload) {
		super(payload);
	}
	
	public void handleSensorPayload(){
		createSensorObjects();
		queueData();
	}
	
	/**
	 * Gets the data from the JSON and breaks them up into SensorData Objects
	 */
	private void createSensorObjects() {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert Contents of the pay load to node objects
			JsonNode json = mapper.readTree(body);
			JsonNode dateNode = json.get("date");
			ArrayNode arrayOfData = (ArrayNode) json.get("sensors");
			
			// Iterate through the sensors and create SensorData Objects
			int size = arrayOfData.size();
			for (int i = 0; i < size; i++) {
			JsonNode sensor = arrayOfData.get(i);
			JsonNode sensorId = sensor.get("id");
			JsonNode sensorType = sensor.get("type");
			JsonNode sensorValue = sensor.get("value");
			SensorData sens = new SensorData(dateNode.asText(), sensorId.asText(), sensorType.asText(), sensorValue.asDouble());
			
			allSensors.add(sens);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Puts each SensorData Object into the queue to be added to the database
	 * by a Thread
	 */
	private void queueData() {
		for(int i = 0; i < allSensors.size(); i++) {
			SensorDataQueueAccessor sdqa = new SensorDataQueueAccessor();
			System.out.println("queuing data. sensor #:" + i);
			sdqa.queueSensor(allSensors.get(i));
		}
	}

}
