package app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Database {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "life_management_system";
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            MongoClient client = MongoClients.create(CONNECTION_STRING);
            database = client.getDatabase(DB_NAME);
        }
        return database;
    }
}
