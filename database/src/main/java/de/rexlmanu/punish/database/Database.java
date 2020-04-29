/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.database;

public interface Database {

    boolean connect();

    void disconnect();

    boolean isConnected();

    PunishProvider provider();
}
