/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.rexlmanu.punish.database.Database;
import de.rexlmanu.punish.database.PunishProvider;
import org.bson.Document;

import java.util.Collections;

public class MongoDBDatabase implements Database {

    private MongoClient client;
    private MongoDatabase database;
    private MongoDBCredentials credentials;

    private MongoCollection<Document> punishCollection;

    public MongoDBDatabase(MongoDBCredentials credentials) {
        this.credentials = credentials;
    }

    public boolean connect() {
        if (credentials.isUseAuth()) {
            this.client = new MongoClient(new ServerAddress(credentials.getAddress(), credentials.getPort()), Collections.singletonList(
                    MongoCredential.createCredential(credentials.getUsername(), credentials.getDatabase(), credentials.getPassword().toCharArray())
            ));
        } else {
            this.client = new MongoClient(credentials.getAddress(), credentials.getPort());
        }
        this.database = this.client.getDatabase(credentials.getDatabase());
        this.punishCollection = this.database.getCollection("punish");
        return true;
    }

    public void disconnect() {
        this.client.close();
    }

    public boolean isConnected() {
        try {
            this.client.getAddress();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PunishProvider provider() {
        return new MongoDBPunishProvider(this.punishCollection);
    }
}
