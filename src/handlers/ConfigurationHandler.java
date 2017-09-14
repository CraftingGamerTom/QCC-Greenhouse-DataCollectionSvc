/**
 * 
 */
package handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads in the configurations on Boot.
 * 
 * @author Thomas Rokicki
 *
 */
public class ConfigurationHandler {

	// Config file name here
	private InputStream stream = this.getClass().getClassLoader().getResourceAsStream("configuration.cfg");

	// Public tags
	public static String databaseIP;
	public static int databasePort;

	public static String databaseName;
	public static String sensorNamesCollection;
	public static String liveDataCollection;
	public static String rawDataCollection;
	public static String hourlyDataCollection;
	public static String dailyDataCollection;
	public static String weeklyDataCollection;
	public static String monthlyDataCollection;
	public static String yearlyDataCollection;

	/**
	 * Loads the configuration file when created.
	 */
	public ConfigurationHandler() {
	}

	/**
	 * Reads in the configuration file.
	 */
	public void read() {
		try {
			// Loads File then Loads the properties
			Properties properties = new Properties();
			properties.load(stream);

			// Sets the configurations - Add properties here
			databaseIP = properties.getProperty("databaseIP");
			databasePort = Integer.parseInt(properties.getProperty("databasePort"));

			databaseName = properties.getProperty("databaseName");
			sensorNamesCollection = properties.getProperty("sensorNamesCollection");
			liveDataCollection = properties.getProperty("liveDataCollection");
			rawDataCollection = properties.getProperty("rawDataCollection");
			hourlyDataCollection = properties.getProperty("hourlyDataCollection");
			dailyDataCollection = properties.getProperty("dailyDataCollection");
			weeklyDataCollection = properties.getProperty("weeklyDataCollection");
			monthlyDataCollection = properties.getProperty("monthlyDataCollection");
			yearlyDataCollection = properties.getProperty("yearlyDataCollection");

			System.out.println("ConfigurationReader: Configuration Read / Updated");
		} catch (FileNotFoundException e) {
			System.out.println("*** Could not find configuration file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("*** IOException while attempting to load configuration file");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("*** Error while attempting to load configuration file");
			e.printStackTrace();
		}

	}

	/**
	 * Refreshes the data stored on boot when called
	 */
	public void refresh() {
		read();
		System.out.println("Refreshed Configuration");
	}

	/**
	 * Verifies the stored data by printing out the data in the console.
	 * Developer MUST update tags when config tags are changed.
	 */
	public void verify() {
		System.out.println("** CONFIGURATION VISUAL VERIFICATION **\n*");

		System.out.print("* Database IP: ");
		System.out.println(databaseIP);

		System.out.print("* Database Port: ");
		System.out.println(databasePort);

		System.out.println("* databaseName: " + databaseName);
		System.out.println("* sensorNamesCollection: " + sensorNamesCollection);
		System.out.println("* liveDataCollection: " + liveDataCollection);
		System.out.println("* rawDataCollection: " + rawDataCollection);
		System.out.println("* hourlyDataCollection: " + hourlyDataCollection);
		System.out.println("* dailyDataCollection: " + dailyDataCollection);
		System.out.println("* weeklyDataCollection: " + weeklyDataCollection);
		System.out.println("* monthlyDataCollection: " + monthlyDataCollection);
		System.out.println("* yearlyDataCollection: " + yearlyDataCollection);

		System.out.println("*\n** END CONFIGURATION VISUAL VERIFICATION **");
	}
}
