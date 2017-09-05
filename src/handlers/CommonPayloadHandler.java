package handlers;

import payloads.SensorDataPayload;

/**
 * Handles payloads
 * @author Thomas Rokicki
 *
 */
public class CommonPayloadHandler {
	
	protected SensorDataPayload payload;
	protected String body;

	public CommonPayloadHandler(SensorDataPayload payload){
		this.payload = payload;
		this.body = payload.getBody();
	}

}
