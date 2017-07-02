package sensors;

/**
 * Version 1.0
 * 
 * @author Thomas Rokicki This is a generic / parent class to the objects that
 *         will store information about the sensors, monitors, and motors. Since
 *         each object will have obtained an unique id and will have a
 *         corresponding name, that is what is stored here.
 */
public class GenericSensor {

	private String name;
	private String id;

	public String getName() {
		return name;
	}

	/**
	 * This is not provided prior to receiving the raspberry pi pay load. This
	 * is created at the server level.
	 * 
	 * @param name
	 *            the name of the object / sensor.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
