/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import com.google.common.collect.Lists;
import de.dytanic.cloudnet.api.CloudAPI;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.bootstrap.layout.PunishLayout;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Type;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.stream.Collectors;

public class BanHistoryCommand extends Command {
    public BanHistoryCommand() {
        super("banhistory", "punish.command.banhistory");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length != 1) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Verwendung: /banhistory <name>"));
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }

        PunishPlayer punishPlayer = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (punishPlayer == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Dieser Spieler wurde noch nie gebannt."));
            return;
        }

        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Banhistorie: "));
        Lists.reverse(punishPlayer.getContexts().stream().filter(c -> c.getType().equals(Type.BAN)).collect(Collectors.toList())).forEach(context -> {
            sender.sendMessage(TextComponent.fromLegacyText(String.format(
                    "Grund: %s, Bandatum: %s, Entbannungsdatum: %s, Manuelle Entbannung: %s",
                    context.getReason().getReason(),
                    PunishLibrary.formatDate(context.getCreateDate()),
                    PunishLayout.formatExpiration(context.getExpiration()),
                    context.isPardon() ? "Ja" : "Nein"
            )));
        });
    }
}
