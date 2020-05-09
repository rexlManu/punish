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
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.UUID;
import java.util.stream.Collectors;

public class MuteHistoryCommand extends Command implements TabExecutor {
    public MuteHistoryCommand() {
        super("mutehistory", "punish.command.mutehistory");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length != 1) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /mutehistory <name>"));
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }

        PunishPlayer punishPlayer = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (punishPlayer == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Dieser Spieler wurde noch nie gemutet."));
            return;
        }

        if (punishPlayer.getContexts().isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Dieser Spieler hat keine History."));
            return;
        }

        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Mutehistorie: "));
        Lists.reverse(punishPlayer.getContexts().stream().filter(c -> c.getType().equals(Type.MUTE)).collect(Collectors.toList())).forEach(context -> {
            sender.sendMessage(TextComponent.fromLegacyText(String.format(
                    "§eGrund§8: §7%s§8, §eBandatum§8: §7%s§8, §eEntbannungsdatum§8: §7%s§8, §eManuelle Entbannung§8: §7%s",
                    context.getReason().getReason(),
                    PunishLibrary.formatDate(context.getCreateDate()),
                    PunishLayout.formatExpiration(context.getExpiration()),
                    context.isPardon() ? "§aJa" : "§cNein"
            )));
        });
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return null;
    }
}
