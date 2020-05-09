/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.database.mongodb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.rexlmanu.punish.database.PunishProvider;
import de.rexlmanu.punish.protocol.PunishPlayer;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class MongoDBPunishProvider implements PunishProvider {

    public static final Type PLAYER_TYPE = new TypeToken<PunishPlayer>() {
    }.getType();
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final JsonParser PARSER = new JsonParser();
    private static final JsonWriterSettings JSON_WRITER_SETTINGS = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

    private MongoCollection<Document> collection;

    @Override
    public PunishPlayer getPlayer(UUID uuid) {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) return null;
        return GSON.fromJson(document.toJson(JSON_WRITER_SETTINGS), PLAYER_TYPE);
    }

    @Override
    public void updatePlayer(PunishPlayer player) {
        Document document = Document.parse(GSON.toJson(player));
        if (this.getPlayer(player.getUuid()) == null) {
            collection.insertOne(document);
            return;
        }

        collection.updateOne(Filters.eq("uuid", player.getUuid().toString()), new Document("$set", Document.parse(GSON.toJson(document))));
    }

    @Override
    public List<PunishPlayer> getPlayerByAddresses(String address) {
        ArrayList<Document> documents = this.collection.find(Filters.eq("ipAddresses", address)).into(new ArrayList<>());
        List<PunishPlayer> players = new ArrayList<>();
        documents.forEach(document -> GSON.fromJson(document.toJson(JSON_WRITER_SETTINGS), PLAYER_TYPE));
        return players;
    }
}
