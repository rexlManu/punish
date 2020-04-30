/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import de.dytanic.cloudnet.api.CloudAPI;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.bootstrap.layout.PunishLayout;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.library.PunishPermission;
import de.rexlmanu.punish.library.cloud.CloudUtil;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Context;
import de.rexlmanu.punish.protocol.punish.Reason;
import de.rexlmanu.punish.protocol.punish.Type;
import de.rexlmanu.punish.protocol.punish.template.PunishTemplate;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MuteCommand extends Command {
    public MuteCommand() {
        super("mute", "punish.command.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length != 2) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Verwendung: /mute <name> <id>"));
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }
        if (CloudUtil.playerHasPermission(uuid, PunishPermission.TEAM) && !sender.hasPermission(PunishPermission.TEAM_BYPASS)) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du kannst keine Teammitglieder muten."));
            return;
        }

        if (!PunishLibrary.isInteger(arguments[1])) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Die id ist kein integer."));
            return;
        }

        PunishTemplate template = PunishPlugin.getPlugin().getTemplates().stream().filter(punishTemplate ->
                punishTemplate.getId() == Integer.parseInt(arguments[1])
                        && punishTemplate.getType().equals(Type.MUTE)
        ).findFirst().orElse(null);
        if (template == null) {
            sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Es konnte kein Template gefunden werden."));
            return;
        }

        PunishPlayer player = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (player == null) {
            player = PunishPlayer.create(uuid);
        } else {
            Context activeContext = player.getActiveContexts().stream().filter(c -> c.getType().equals(Type.MUTE) && !c.isOver()).findFirst().orElse(null);
            if (activeContext != null) {
                sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Der Spieler ist bereits gemutet."));
                return;
            }
        }

        Context context = new Context(
                new Reason(template.getId(), template.getReason()), template.getType(),
                template.getExpiration() == -1 ? -1 : System.currentTimeMillis() + template.getExpiration());
        player.getContexts().add(context);
        PunishPlugin.getPlugin().getProvider().updatePlayer(player);
        sender.sendMessage(new TextComponent(String.format(PunishLibrary.PREFIX + "Du hast den Spieler %s erfolgreich gemutet.", arguments[0])));

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        if (proxiedPlayer != null) {
            PunishLayout.sendChatLayout(proxiedPlayer, context);
        }
    }
}
