/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import de.rexlmanu.punish.bootstrap.layout.PunishLayout;
import de.rexlmanu.punish.library.PunishLibrary;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "punish.command.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /kick <name> <grund...>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(arguments[0]);
        if (target == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler konnte nicht gefunden werden."));
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < arguments.length; i++) {
            builder.append(arguments[i]);
        }
        target.disconnect(TextComponent.fromLegacyText(PunishLayout.getKickLayout(builder.toString())));
        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du hast erfolgreich den Spieler gekickt."));
    }
}
