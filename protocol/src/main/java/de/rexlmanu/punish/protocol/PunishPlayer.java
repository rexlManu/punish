/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.protocol;

import de.rexlmanu.punish.protocol.punish.Context;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PunishPlayer {

    public static PunishPlayer create(UUID uuid) {
        return new PunishPlayer(uuid, new ArrayList<>(), new ArrayList<>());
    }

    private UUID uuid;
    private List<Context> contexts;
    private List<String> ipAddresses;

    public List<Context> getActiveContexts() {
        return this.contexts.stream().filter(context -> !context.isOver()).collect(Collectors.toList());
    }
}
