package controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import handlers.SensorPayloadHandler;
import payloads.SensorDataPayload;
import payloads.SensorDataResponse;

/**
 * Sensor data controller class handles the Incoming traffic Posting data to the
 * database
 * 
 * @author Thomas Rokicki
 *
 */
@RestController
public class SensorDataController {

	/**
	 * Handles a GET request via id of a sensor (Example).
	 * 
	 * @param id
	 *            the identification.
	 * @return an HTTP status code.
	 */
	@RequestMapping(value = "/testResponse", method = RequestMethod.GET)
	public String get() {

		return "Test";
	}
	

	/**
	 * The is how the data is posted the database. The Raspberry Pi is what is
	 * going to send a POST request. This method handles that POST request.
	 * Ultimately the data will be put into the database.
	 * 
	 * @param req
	 *            The JSON formatted String.
	 * @return an HTTP status code.
	 */
	@RequestMapping(value = "/sensorData", method = RequestMethod.POST)
	public ResponseEntity<SensorDataResponse> recieveSensorData(@RequestBody String req) {

		//Send pay load to be added to a queue
		SensorDataPayload payload = new SensorDataPayload();
		payload.setBody(req);
		
		// Takes the pay load, converts it to SensorData, then adds to the queue
		SensorPayloadHandler payloadHandler = new SensorPayloadHandler(payload);
		payloadHandler.handleSensorPayload();
		
		// Creates a response to send back to the caller
		SensorDataResponse res = new SensorDataResponse();
		res.setBody(req);
		return new ResponseEntity<SensorDataResponse>(res, HttpStatus.OK);
	}

}