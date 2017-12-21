package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import handlers.ConfigurationHandler;
import handlers.MongoSecuritySingleton;

public class MongoClientConnection {

	private static MongoClient client = null;

	private static String DB_SRV_USR = MongoSecuritySingleton.getUsername();
	private static String DB_SRV_PWD = MongoSecuritySingleton.getPassword();
	private static String DB_URL = ConfigurationHandler.databaseIP;
	private static int DB_PORT = ConfigurationHandler.databasePort;
	private static String dbName = ConfigurationHandler.databaseName;

	protected MongoClientConnection() {

	}

	public static MongoClient getInstance() {
		if (client == null) {

			String uri = "mongodb://" + DB_SRV_USR + ":" + DB_SRV_PWD + "@" + DB_URL + ":" + DB_PORT + "/" + dbName
					+ "?authSource=admin";

			MongoClientURI mongoClientURI = new MongoClientURI(uri);

			client = new MongoClient(mongoClientURI);
		}
		return client;
	}

}