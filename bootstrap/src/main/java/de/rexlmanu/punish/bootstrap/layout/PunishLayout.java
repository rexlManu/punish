/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.layout;

import de.rexlmanu.punish.library.PunishLibrary;
import de.rexlmanu.punish.protocol.punish.Context;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PunishLayout {

    public static String getBanLayout(Context context) {
        return String.format(
                "              §a§lTeam§2§lHG\n" +
                        "§8§m---------------------------------------\n" +
                        "\n" +
                        "§cDu wurdest vom §cNetzwerk gebannt\n" +
                        "\n" +
                        "§aGrund §8» §e%s\n" +
                        "\n" +
                        "§aGebannt bis §8» §7%s\n" +
                        "\n" +
                        "§7Stelle einen Entbannungsantrag im TeamSpeak\n" +
                        "\n" +
                        "§8§m---------------------------------------\n",
                context.getReason().getReason(), formatExpiration(context.getExpiration()));
    }

    public static String getKickLayout(String reason) {
        return String.format(
                "              §a§lTeam§2§lHG\n" +
                        "§8§m---------------------------------------\n" +
                        "\n" +
                        "§cDu wurdest vom §cNetzwerk gekickt\n" +
                        "\n" +
                        "§aGrund §8» §e%s\n" +
                        "\n" +
                        "§8§m---------------------------------------\n",
                reason);
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
