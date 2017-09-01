package database;

import com.mongodb.MongoClient;

import handlers.ConfigurationHandler;

public class MongoClientConnection {
	
	private static MongoClient client = null;
	
	protected MongoClientConnection() {
		
	}
	
	public static MongoClient getInstance() {
		if(client == null) {
			
			client = new MongoClient( ConfigurationHandler.databaseIP , ConfigurationHandler.databasePort );
		}
		return client;
	}

}