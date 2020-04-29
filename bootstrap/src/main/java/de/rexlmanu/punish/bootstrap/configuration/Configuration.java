/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.configuration;

import com.google.gson.JsonElement;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.library.utility.load.Loadable;
import de.rexlmanu.punish.library.utility.reload.Reloadable;
import de.rexlmanu.punish.library.utility.save.Savable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Getter
@AllArgsConstructor
public class Configuration implements Loadable, Reloadable, Savable {

    private File file;
    private JsonElement element;

    public Configuration(File file) {
        this.file = file;
    }

    public void load() {
        try {
            this.element = PunishLibrary.PARSER.parse(new String(Files.readAllBytes(this.file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDefault(JsonElement element) {
        if (!this.file.exists()) {
            this.element = element;
            this.save();
        }
    }

    public void reload() {
        this.save();
        this.load();
    }

    public void save() {
        try {
            Files.write(this.file.toPath(), PunishLibrary.GSON_WITH_PRETTY.toJson(this.element).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
