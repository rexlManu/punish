/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.bootstrap.listener;

import de.rexlmanu.punish.bootstrap.PunishPlugin;
import de.rexlmanu.punish.bootstrap.layout.PunishLayout;
import de.rexlmanu.punish.protocol.PunishPlayer;
import de.rexlmanu.punish.protocol.punish.Context;
import de.rexlmanu.punish.protocol.punish.Type;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;

public class PunishListener implements Listener {

    private static final List<String> FORBIDDEN_MUTE_COMMANDS = Arrays.asList("/msg ", "/p ", "/clan chat ");

    @EventHandler
    public void handle(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PunishPlayer punishPlayer = PunishPlugin.getPlugin().getProvider().getPlayer(player.getUniqueId());
        if (punishPlayer == null) return;
        PunishPlugin.PUNISH_PLAYER_MAP.put(player.getUniqueId(), punishPlayer);
        punishPlayer.getActiveContexts().stream().filter(context -> context.getType().equals(Type.BAN)).findFirst().ifPresent(context -> {
            player.disconnect(TextComponent.fromLegacyText(PunishLayout.getBanLayout(context)));
        });

    }

    @EventHandler
    public void handle(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PunishPlugin.PUNISH_PLAYER_MAP.remove(player.getUniqueId());
    }

    @EventHandler
    public void handle(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (event.getMessage().startsWith("/") && FORBIDDEN_MUTE_COMMANDS.stream().noneMatch(s -> event.getMessage().startsWith(s)))
            return;

        if (!PunishPlugin.PUNISH_PLAYER_MAP.containsKey(player.getUniqueId())) return;
        PunishPlayer punishPlayer = PunishPlugin.PUNISH_PLAYER_MAP.get(player.getUniqueId());
        Context context = punishPlayer.getActiveContexts().stream().filter(c -> c.getType().equals(Type.MUTE)).findFirst().orElse(null);
        if (context == null) return;
        event.setCancelled(true);
        PunishLayout.sendChatLayout(player, context);
    }
}
