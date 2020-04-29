/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.configuration;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.punish.template.PunishTemplate;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class TemplateConfiguration extends Configuration {

    private static final Type TEMPLATE_LIST_TYPE = new TypeToken<List<PunishTemplate>>() {
    }.getType();

    public TemplateConfiguration() {
        super(new File(PunishPlugin.getPlugin().getDataFolder(), "templates.json"));

        JsonArray defaultArray = new JsonArray();
        defaultArray.add(PunishLibrary.GSON.toJsonTree(new PunishTemplate(1, "Client Mods", de.rexlmanu.punish.protocol.punish.Type.BAN, -1)));
        this.createDefault(defaultArray);

        this.load();
    }

    public List<PunishTemplate> getTemplates() {
        return PunishLibrary.GSON.fromJson(this.getElement().getAsJsonArray(), TEMPLATE_LIST_TYPE);
    }
}
