/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.protocol.punish;

import de.rexlmanu.punish.protocol.punish.template.TemplateStage;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Reason {

    private int id;
    private String reason;
    private TemplateStage stage;

}
