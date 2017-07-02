package payloads;

// {"time":"MMDDYY", 
//  "sensors":[
//	    {"id":"/ardXX/sensYY", "value":100}, 
//      {"id":"/ardXX/sensZZ", "value":101},
//	   ] 
//	}

public class GenericPayload {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
