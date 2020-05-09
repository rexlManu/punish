/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.command;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.punish.Type;
import de.rexlmanu.punish.protocol.punish.template.TemplateStage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class TemplateCommand extends Command {
    public TemplateCommand() {
        super("template", "punish.command.template");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        switch (arguments.length) {
            case 0:
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Hilfeübersicht"));
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "/template <Type>"));
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Types: mute, ban"));
                break;
            case 1:
                Optional<Type> optional = Enums.getIfPresent(Type.class, arguments[0].toUpperCase());
                if (!optional.isPresent()) {
                    sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Dieser Type konnte nicht gefunden werden."));
                    break;
                }
                Type type = optional.get();
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Folgende Templates wurden gefunden: "));
                PunishPlugin.getPlugin().getTemplates().stream().filter(punishTemplate -> punishTemplate.getType().equals(type)).forEach(punishTemplate -> {
                    sender.sendMessage(TextComponent.fromLegacyText(String.format(PunishLibrary.PREFIX + "reason: %s, id: %s, expiration: %s",
                            punishTemplate.getReason(), punishTemplate.getId(), punishTemplate.getStages().stream().map(TemplateStage::getExpiration).toString())));

                });
                break;
            default:
                sender.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Hilfe? /template"));
                break;
        }
    }
}
