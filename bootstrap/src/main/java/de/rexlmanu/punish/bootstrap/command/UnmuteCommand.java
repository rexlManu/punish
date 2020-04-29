/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import de.dytanic.cloudnet.api.CloudAPI;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Context;
import de.rexlmanu.punish.protocol.punish.Type;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnmuteCommand extends Command {
    public UnmuteCommand() {
        super("unmute", "punish.command.unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length != 1) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Verwendung: /unmute <name>"));
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }

        PunishPlayer punishPlayer = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (punishPlayer == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Dieser Spieler wurde noch nie gemutet."));
            return;
        }
        Context context = punishPlayer.getActiveContexts().stream().filter(c -> c.getType().equals(Type.MUTE)).findFirst().orElse(null);
        if (context == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Der Spieler ist zurzeit nicht gemutet."));
            return;
        }
        context.setPardon(true);
        PunishPlugin.getPlugin().getProvider().updatePlayer(punishPlayer);
        sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Du hast erfolgreich diesen Spieler entmutet."));

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            PunishPlugin.PUNISH_PLAYER_MAP.put(uuid, punishPlayer);
            player.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "§7Du wurdest entmutet."));
        }
    }
}
