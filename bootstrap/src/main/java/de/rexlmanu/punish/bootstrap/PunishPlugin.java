/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap;

import com.google.gson.reflect.TypeToken;
import de.rexlmanu.punish.bootstrap.command.*;
import de.rexlmanu.punish.bootstrap.configuration.DatabaseConfiguration;
import de.rexlmanu.punish.bootstrap.configuration.TemplateConfiguration;
import de.rexlmanu.punish.bootstrap.listener.PunishListener;
import de.rexlmanu.punish.database.Database;
import de.rexlmanu.punish.database.PunishProvider;
import de.rexlmanu.punish.database.mongodb.MongoDBCredentials;
import de.rexlmanu.punish.database.mongodb.MongoDBDatabase;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.template.PunishTemplate;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PunishPlugin extends Plugin {

    public static final Map<UUID, PunishPlayer> PUNISH_PLAYER_MAP = new HashMap<>();

    @Getter
    private static PunishPlugin plugin;

    private DatabaseConfiguration databaseConfiguration;
    private TemplateConfiguration templateConfiguration;
    private Database database;

    private PunishProvider provider;

    private List<PunishTemplate> templates;

    @Override
    public void onEnable() {
        PunishPlugin.plugin = this;
        this.getDataFolder().mkdir();

        this.databaseConfiguration = new DatabaseConfiguration();
        this.templateConfiguration = new TemplateConfiguration();

        if (this.databaseConfiguration.getDriver().equals(DatabaseConfiguration.MONGODB_DRIVER)) {
            this.database = new MongoDBDatabase(PunishLibrary.GSON.fromJson(this.databaseConfiguration.getElement().getAsJsonObject().get("credentials"),
                    new TypeToken<MongoDBCredentials>() {}.getType()));
            this.database.connect();
            this.provider = this.database.provider();
        } else {
            throw new UnsupportedOperationException("Currently only mongodb is supported as database.");
        }
        this.templates = this.templateConfiguration.getTemplates();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new PunishListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TemplateCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteHistoryCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanHistoryCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand());
    }

    @Override
    public void onDisable() {
        if (this.database != null && this.database.isConnected())
            this.database.disconnect();
    }
}
