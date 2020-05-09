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
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        super("unban", "punish.command.unban");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length != 1) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /unban <name>"));
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
        Context context = punishPlayer.getActiveContexts().stream().filter(c -> c.getType().equals(Type.BAN)).findFirst().orElse(null);
        if (context == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler ist zurzeit nicht gebannt."));
            return;
        }
        context.setPardon(true);
        PunishPlugin.getPlugin().getProvider().updatePlayer(punishPlayer);
        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du hast erfolgreich diesen Spieler entbannt."));
    }
}
