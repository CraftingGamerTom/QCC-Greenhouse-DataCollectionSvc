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
	private String sensorId;
	private String type;
	private Double value;

	/**
	 * Generic Sensor initialization with no data.
	 */
	public SensorData() {

	}

	/**
	 * Creates a sensor. The method includes the date when created.
	 * @param sensorId
	 * @param type
	 * @param value
	 * @param date
	 */
	public SensorData(String date, String sensorId, String type, Double value) {
		this.date = date;
		this.sensorId = sensorId;
		this.type = type;
		this.value = value;
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
}