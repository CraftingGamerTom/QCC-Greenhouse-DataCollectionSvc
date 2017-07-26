package executable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import handlers.TeardownHandler;
import payloads.SensorDataResponse;
import payloads.SensorUpdateResponse;

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
	@RequestMapping(value = "/sensorData/{id}", method = RequestMethod.GET)
	public ResponseEntity<SensorDataResponse> get(@PathVariable String id) {

		// return response
		SensorDataResponse res = new SensorDataResponse();
		res.setValue(id);
		return new ResponseEntity<SensorDataResponse>(res, HttpStatus.OK);
	}

	/**
	 * Handles a GET request based on a specific sensor.
	 * 
	 * @param ard
	 *            the name of the arduino the sensor belongs to.
	 * @param sens
	 *            the specific sensors name.
	 * @return an HTTP status code.
	 */
	@RequestMapping(value = "/sensorData/{ard}/{sens}", method = RequestMethod.GET)
	public ResponseEntity<SensorDataResponse> get(@PathVariable String ard, @PathVariable String sens) {

		// return response
		SensorDataResponse res = new SensorDataResponse();
		res.setValue(sens);
		return new ResponseEntity<SensorDataResponse>(res, HttpStatus.OK);
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
	public ResponseEntity<SensorUpdateResponse> recieveSensorData(@RequestBody String req) {

		SensorUpdateResponse res = new SensorUpdateResponse();
		res.setValue(req);
		TeardownHandler tearHandler = new TeardownHandler();
		tearHandler.parseSensorJson(req);
		return new ResponseEntity<SensorUpdateResponse>(res, HttpStatus.OK);
	}

	/**
	 * Method to handle the data incoming for observation notes. This data will
	 * ultimately be put into the database. This simply gets the pay load and
	 * forwards it through the rest of the api and then gives a response.
	 * 
	 * @param req
	 *            The JSON formatted String.
	 * @return an HTTP status code.
	 */
	@RequestMapping(value = "/observationData", method = RequestMethod.POST)
	public ResponseEntity<SensorUpdateResponse> recieveObservationData(@RequestBody String req) {

		SensorUpdateResponse res = new SensorUpdateResponse();
		res.setValue(req);
		TeardownHandler tearHandler = new TeardownHandler();
		tearHandler.parseObservationJson(req);
		return new ResponseEntity<SensorUpdateResponse>(res, HttpStatus.OK);
	}
}