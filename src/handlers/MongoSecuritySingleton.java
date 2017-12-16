/**
 * Copyright (c) 2017 Thomas Rokicki
 */

package handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoSecuritySingleton {

	private static MongoSecuritySingleton instance = null; // Singleton

	private String username;
	private String password;

	private static MongoSecuritySingleton getInstance() {
		if (instance == null) {
			instance = new MongoSecuritySingleton();
			instance.read();
		}
		return instance;
	}

	/**
	 * readed the configuration file always done in singleton instance
	 */
	private void read() {

		// Config file name here
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("mongo-security.cfg");

		try {
			// Loads File then Loads the properties
			Properties properties = new Properties();
			properties.load(stream);

			// Sets the configurations - Add properties here
			username = properties.getProperty("username");
			password = properties.getProperty("password");

			System.out.println("SecurityReader: Security Settings Read / Updated");
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

		// close stream
		try {
			stream.close();
		} catch (IOException e) {
			System.out.println("Could not close security configuration stream. IOException: ");
			e.printStackTrace();
		}
	}

	/**
	 * Re-reads the configuration file (use sparingly - only for testing)
	 */
	public void reset() {
		System.out.println("*** RELOADING MONGO SECURITY ***");
		getInstance().read();
		System.out.println("*** FINISHED RELOADING ***");
	}

	/**
	 * Verifies the stored data by printing out the data in the console.
	 * Developer
	 * MUST update tags when config tags are changed.
	 */
	private void verify() {
		System.out.println("** CONFIGURATION VISUAL VERIFICATION **\n*");

		System.out.println("* username: " + getUsername());
		System.out.println("* password: " + getPassword());

		System.out.println("*\n** END CONFIGURATION VISUAL VERIFICATION **");
	}

	// Getters

	public static String getUsername() {
		return getInstance().getPrivateUsername();
	}

	public static String getPassword() {
		return getInstance().getPrivatePassword();
	}

	// Private Getters

	public String getPrivateUsername() {
		return this.username;
	}

	public String getPrivatePassword() {
		return this.password;
	}
}