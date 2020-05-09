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
import de.rexlmanu.punish.library.time.TimeParser;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Context;
import de.rexlmanu.punish.protocol.punish.Reason;
import de.rexlmanu.punish.protocol.punish.Type;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class TempMuteCommand extends Command {
    public TempMuteCommand() {
        super("tempmute", "punish.command.tempmute");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /tempmute <name> <zeit> <grund...>"));
            return;
        }
        UUID uniqueId = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);
        if (uniqueId == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }
        if (sender.getName().toLowerCase().equals(arguments[0].toLowerCase())) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Bitte versuche keine Selbstverletzung."));
            return;
        }
        if (CloudUtil.playerHasPermission(uniqueId, PunishPermission.TEAM) && !sender.hasPermission(PunishPermission.TEAM_BYPASS)) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du kannst keine Teammitglieder muten."));
            return;
        }
        long expiration = TimeParser.parseStringToMillis(arguments[1]);
        if (expiration == 0) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die Zeit wurde falsch angegeben."));
            return;
        }
        PunishPlayer player = PunishPlugin.getPlugin().getProvider().getPlayer(uniqueId);
        if (player == null) {
            player = PunishPlayer.create(uniqueId);
        } else {
            Context activeContext = player.getActiveContexts().stream().filter(c -> c.getType().equals(Type.MUTE) && !c.isOver()).findFirst().orElse(null);
            if (activeContext != null) {
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler ist bereits gemutet."));
                return;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < arguments.length; i++) builder.append(arguments[i]).append(" ");

        Context context = new Context(new Reason(-1, builder.toString(), null), Type.MUTE, expiration + System.currentTimeMillis());
        player.getContexts().add(context);
        PunishPlugin.getPlugin().getProvider().updatePlayer(player);

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uniqueId);
        if (proxiedPlayer != null) {
            PunishPlugin.PUNISH_PLAYER_MAP.put(player.getUuid(), player);
            PunishLayout.sendChatLayout(proxiedPlayer, context);
        }

        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler wurde erfolgreich zeitlich gemutet."));
    }
}
