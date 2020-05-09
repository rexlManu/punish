/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.configuration;

import com.google.gson.JsonObject;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.bootstrap.layout.PunishLayout;
import de.rexlmanu.punish.library.PunishLibrary;

import java.io.File;
import java.util.Arrays;

public class LayoutConfiguration extends Configuration {

    private PunishLayout banLayout, kickLayout;

    public LayoutConfiguration() {
        super(new File(PunishPlugin.getPlugin().getDataFolder(), "layout.json"));

        JsonObject object = new JsonObject();
        object.add("banLayout", PunishLibrary.GSON_WITH_PRETTY.toJsonTree(new PunishLayout(Arrays.asList(
                "    §a§lTeam§2§lHG\n",
                "§8§m---------------------------------------\n",
                "\n",
                "§cDu wurdest vom §cNetzwerk gebannt\n",
                "\n",
                "§aGrund §8» §e%reason\n",
                "\n",
                "§aGebannt bis §8» §7%date\n",
                "\n",
                "§7Stelle einen Entbannungsantrag im TeamSpeak\n",
                "\n",
                "§8§m---------------------------------------\n"
        ))));
        object.add("kickLayout", PunishLibrary.GSON_WITH_PRETTY.toJsonTree(new PunishLayout(Arrays.asList(
                "    §a§lTeam§2§lHG\n",
                "§8§m---------------------------------------\n",
                "\n",
                "§cDu wurdest vom §cNetzwerk gekickt\n",
                "\n",
                "§aGrund §8» §e%s\n",
                "\n",
                "§8§m---------------------------------------\n"
        ))));
        this.createDefault(object);
        this.load();
    }

    public PunishLayout getBanLayout() {
        return PunishLibrary.GSON.fromJson(this.getElement().getAsJsonObject().get("banLayout"), PunishLayout.class);
    }
    public PunishLayout getKickLayout() {
        return PunishLibrary.GSON.fromJson(this.getElement().getAsJsonObject().get("kickLayout"), PunishLayout.class);
    }
}
