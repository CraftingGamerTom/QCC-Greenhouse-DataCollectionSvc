package sensors;

/**
 * @version 1.0
 * @author Thomas Rokicki
 * This object maintains one double value for a sensors temperature.
 */
public class TemperatureSensor extends GenericSensor {

	private double temperature;

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
}
