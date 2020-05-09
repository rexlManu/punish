/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.library.cloud;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionEntity;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;

import java.util.UUID;

public class CloudUtil {

    public static boolean playerHasPermission(UUID uuid, String permission) {
        OfflinePlayer player = CloudAPI.getInstance().getOfflinePlayer(uuid);
        if (player == null) return false;

        PermissionEntity entity = player.getPermissionEntity();
        PermissionGroup group = entity.getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        if (group == null) return false;
        return entity.getGroups().stream().anyMatch(groupEntityData -> entity.hasPermission(CloudAPI.getInstance().getPermissionPool(), permission, groupEntityData.getGroup()));
    }

}
