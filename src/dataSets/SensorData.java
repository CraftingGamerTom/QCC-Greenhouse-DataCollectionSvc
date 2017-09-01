package dataSets;

/**
 * Sensor Object maintains information about one sensor. This is the 
 * "go-between" for raw data and stored data.
 * 
 * @version 0.0.1
 * @author Thomas Rokicki
 *
 */
public class SensorData {
	private String date;
	private String id;
	private String type;
	private String value;

	/**
	 * Generic Sensor initialization with no data.
	 */
	public SensorData() {

	}

	/**
	 * Creates a sensor. The method includes the date when created.
	 * @param id
	 * @param type
	 * @param value
	 * @param date
	 */
	public SensorData(String date, String id, String type, String value) {
		this.date = date;
		this.id = id;
		this.type = type;
		this.value = value;
	}

	/**
	 * Creates a sensor. This method does not include a date but the date will
	 * must be provided later or it will be left blank.
	 * 
	 * @param id
	 * @param type
	 * @param value
	 */
	public SensorData(String id, String type, String value) {
		this.id = id;
		this.type = type;
		this.value = value;
		this.date = "";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}