/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.punish.protocol.punish.template;

import de.rexlmanu.punish.protocol.punish.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PunishTemplate {

    private int id;
    private String reason;
    private Type type;
    private List<TemplateStage> stages;

}
