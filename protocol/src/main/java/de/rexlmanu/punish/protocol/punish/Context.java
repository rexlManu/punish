/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.protocol.punish;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Context {

    private Reason reason;
    private Type type;
    private long expiration;
    private boolean pardon;
    private long createDate;

    public Context(Reason reason, Type type, long expiration) {
        this.reason = reason;
        this.type = type;
        this.expiration = expiration;
        this.pardon = false;
        this.createDate = new Date().getTime();
    }

    public boolean isOver() {
        return (expiration < new Date().getTime() && expiration != -1) || pardon;
    }
}
