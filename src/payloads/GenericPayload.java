package payloads;

/**
 * This is returned to the caller of an endpoint
 * @author Thomas Rokicki
 *
 */
public class GenericPayload {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
