/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.layout;

import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.punish.Context;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

@AllArgsConstructor
@Data
public class PunishLayout {

    private List<String> layout;

    public String getAsKickLayout(String reason, String expiration) {
        StringBuilder builder = new StringBuilder();
        for (String s : this.layout) builder.append(s.replace("%reason", reason).replace("%date", expiration));
        return builder.toString();
    }

    public String getAsKickLayout(String reason, long expiration) {
        return this.getAsKickLayout(reason, formatExpiration(expiration));
    }

    public static String formatExpiration(long expiration) {
        return expiration == -1 ? "Nie" : PunishLibrary.formatDate(expiration);
    }

    public static void sendChatLayout(ProxiedPlayer player, Context context) {
        player.sendMessage(TextComponent.fromLegacyText(PunishLibrary.PREFIX + "Du kannst nicht mit dem Chat interagieren, da du gemutet wurdest."));
        player.sendMessage(TextComponent.fromLegacyText(String.format(PunishLibrary.PREFIX
                + "Grund: %s §8- §7Verbleibend bis: %s", context.getReason().getReason(), formatExpiration(context.getExpiration()))));
    }

}
