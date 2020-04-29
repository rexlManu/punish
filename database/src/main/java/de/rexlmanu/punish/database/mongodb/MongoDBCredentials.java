/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.database.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MongoDBCredentials {

    private String address, database, username, password;
    private int port;
    private boolean useAuth;

}
