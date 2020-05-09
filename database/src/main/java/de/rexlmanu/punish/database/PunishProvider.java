/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.database;

import de.rexlmanu.punish.protocol.PunishPlayer;

import java.util.List;
import java.util.UUID;

public interface PunishProvider {

    PunishPlayer getPlayer(UUID uuid);

    void updatePlayer(PunishPlayer player);

    List<PunishPlayer> getPlayerByAddresses(String address);

}
