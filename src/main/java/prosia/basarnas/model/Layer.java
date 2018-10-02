/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Fauzi
 */
@Entity
@Table(name = "layer")
@Data
public class Layer extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "layerid", length = 15)
    private String layerId;
    @Column(name = "opacity", length = 15)
    private String opacity;
    @Column(name = "path", length = 255)
    private String path;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "type", length = 3)
    private String type;
    @Column(name = "status")
    private Boolean status;
}
