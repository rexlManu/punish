/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import de.dytanic.cloudnet.api.CloudAPI;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.PunishPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class ClearHistoryCommand extends Command {
    public ClearHistoryCommand() {
        super("clearhistory", "punish.command.clearhistory");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 1) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /clearhistory <name>"));
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }

        PunishPlayer punishPlayer = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (punishPlayer == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Dieser Spieler wurde noch nie gebannt."));
            return;
        }

        if (punishPlayer.getContexts().isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Dieser Spieler hat keine History."));
            return;
        }

        punishPlayer.getContexts().clear();
        PunishPlugin.getPlugin().getProvider().updatePlayer(punishPlayer);
        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Seine History wurde erfolgreich gelöscht."));
        if (PunishPlugin.PUNISH_PLAYER_MAP.containsKey(uuid)) {
            PunishPlugin.PUNISH_PLAYER_MAP.put(uuid, punishPlayer);
        }

    }
}
