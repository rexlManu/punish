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

public class TempBanCommand extends Command {
    public TempBanCommand() {
        super("tempban", "punish.command.tempban");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Verwendung: /tempban <name> <zeit> <grund...>"));
            return;
        }
        UUID uniqueId = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);
        if (uniqueId == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }
        if (CloudUtil.playerHasPermission(uniqueId, PunishPermission.TEAM) && !sender.hasPermission(PunishPermission.TEAM_BYPASS)) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du kannst keine Teammitglieder bannen."));
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
            Context activeContext = player.getActiveContexts().stream().filter(c -> c.getType().equals(Type.BAN) && !c.isOver()).findFirst().orElse(null);
            if (activeContext != null) {
                sender.sendMessage(new TextComponent(PunishLibrary.PREFIX + "Der Spieler ist bereits gebannt."));
                return;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < arguments.length; i++) builder.append(arguments[i]);

        Context context = new Context(new Reason(-1, builder.toString()), Type.BAN, expiration + System.currentTimeMillis());
        player.getContexts().add(context);
        PunishPlugin.getPlugin().getProvider().updatePlayer(player);

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uniqueId);
        if (proxiedPlayer != null) {
            proxiedPlayer.disconnect(TextComponent.fromLegacyText(PunishLayout.getBanLayout(context)));
        }

        sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler wurde erfolgreich zeitlich gebannt."));
    }
}
