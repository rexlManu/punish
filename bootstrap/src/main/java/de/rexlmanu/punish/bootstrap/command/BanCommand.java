/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import de.dytanic.cloudnet.api.CloudAPI;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.library.PunishPermission;
import de.rexlmanu.punish.library.cloud.CloudUtil;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Context;
import de.rexlmanu.punish.protocol.punish.Reason;
import de.rexlmanu.punish.protocol.punish.Type;
import de.rexlmanu.punish.protocol.punish.template.PunishTemplate;
import de.rexlmanu.punish.protocol.punish.template.TemplateStage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "punish.command.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        List<PunishTemplate> templates = PunishPlugin.getPlugin().getTemplates();
        if (arguments.length != 2) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Hier sind alle Bangründe aufgelistet"));
            templates.stream().filter(t -> t.getType().equals(Type.BAN)).forEach(punishTemplate -> {
                sender.sendMessage(TextComponent.fromLegacyText(String.format("§7» §7[§e§l%s§7] §7§l- §c§l%s", punishTemplate.getId(), punishTemplate.getReason())));
            });
            return;
        }
        UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(arguments[0]);

        if (uuid == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die uuid konnte nicht gefunden werden."));
            return;
        }
        if (sender.getName().toLowerCase().equals(arguments[0].toLowerCase())) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Bitte versuche keine Selbstverletzung."));
            return;
        }

        if (CloudUtil.playerHasPermission(uuid, PunishPermission.TEAM) && !sender.hasPermission(PunishPermission.TEAM_BYPASS)) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du kannst keine Teammitglieder bannen."));
            return;
        }

        if (!PunishLibrary.isInteger(arguments[1])) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Die id ist kein integer."));
            return;
        }

        PunishTemplate template = templates.stream().filter(punishTemplate ->
                punishTemplate.getId() == Integer.parseInt(arguments[1])
                        && punishTemplate.getType().equals(Type.BAN)
        ).findFirst().orElse(null);
        if (template == null) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Es konnte kein Template gefunden werden."));
            return;
        }

        PunishPlayer player = PunishPlugin.getPlugin().getProvider().getPlayer(uuid);
        if (player == null) {
            player = PunishPlayer.create(uuid);
        } else {
            Context activeContext = player.getActiveContexts().stream().filter(c -> c.getType().equals(Type.BAN) && !c.isOver()).findFirst().orElse(null);
            if (activeContext != null) {
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler ist bereits gebannt."));
                return;
            }
        }

        Context activeContext = player.getActiveContexts().stream().filter(c -> c.getType().equals(Type.BAN) && !c.isOver()).findFirst().orElse(null);

        if (template.getStages().isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Grund hat keine Stufungen."));
            return;
        }

        TemplateStage stage = template.getStages().get(0);

        Context lastContext = player.getContexts().stream().filter(context -> context.getReason().getReason().equals(template.getReason())).findFirst().orElse(null);

        if (lastContext != null) {
            int index = template.getStages().indexOf(lastContext.getReason().getStage()) + 1;
            if (template.getStages().size() <= index) {
                stage = template.getStages().get(template.getStages().size() - 1);
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler war bereits für die höchste Stufe gebannt."));
            } else {
                stage = template.getStages().get(index);
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Der Spieler war bereits gebannt und wird nun eine höhere Stufe gebannt."));
            }
        }

        Context context = new Context(
                new Reason(template.getId(), template.getReason(), stage), template.getType(),
                stage.getExpiration() == -1 ? -1 : System.currentTimeMillis() + stage.getExpiration());

        player.getContexts().add(context);
        PunishPlugin.getPlugin().getProvider().updatePlayer(player);
        sender.sendMessage(TextComponent.fromLegacyText(String.format(PunishLibrary.PREFIX + "Du hast den Spieler %s erfolgreich gebannt.", arguments[0])));

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        if (proxiedPlayer != null) {
            player.getIpAddresses().add(proxiedPlayer.getAddress().getAddress().getHostAddress().replace("\\", ""));
            PunishPlugin.getPlugin().getProvider().updatePlayer(player);
            proxiedPlayer.disconnect(PunishPlugin.getPlugin().getLayoutConfiguration().getBanLayout().getAsKickLayout(context.getReason().getReason(), context.getExpiration()));
        }
    }
}
