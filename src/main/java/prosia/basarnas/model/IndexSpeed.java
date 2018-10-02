/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Aris
 */
@Entity
@Table(name = "index_speed")
public @Data class IndexSpeed extends AbstractAuditingEntity  implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "indexspeedid", length = 15)
    private String indexSpeedID;
    @Column(name = "rescuer", length = 50)
    private String rescuer;
    @Column(name = "searchobject", length = 50)
    private String searchObject;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "correction")
    private Double correction;
}
