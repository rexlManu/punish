/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.configuration;

import com.google.gson.JsonObject;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.database.mongodb.MongoDBCredentials;
import de.rexlmanu.punish.library.PunishLibrary;

import java.io.File;

public class DatabaseConfiguration extends Configuration {

    public static final String MONGODB_DRIVER = "mongodb";

    public DatabaseConfiguration() {
        super(new File(PunishPlugin.getPlugin().getDataFolder(), "database.json"));
        JsonObject defaultObject = new JsonObject();
        defaultObject.addProperty("driver", MONGODB_DRIVER);
        defaultObject.add("credentials", PunishLibrary.GSON.toJsonTree(new MongoDBCredentials(
                "localhost",
                "admin",
                "",
                "",
                27017,
                false
        )));
        this.createDefault(defaultObject);
        this.load();
    }

    public String getDriver() {
        return this.getElement().getAsJsonObject().getAsJsonPrimitive("driver").getAsString();
    }
}
