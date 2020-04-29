/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PunishLibrary {

    public static final Gson GSON_WITH_PRETTY = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static final JsonParser PARSER = new JsonParser();

    public static final String PREFIX = "§7» §a§lTeam§2§lHG §8§l| §7",
            PERMISSION_REQUIRED = PREFIX + "Dir fehlen die Rechte um diese Aktion auszuführen!";

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static boolean isInteger(String rawInt) {
        try {
            Integer.parseInt(rawInt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatDate(long millis) {
        return TIME_FORMATTER.format(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
