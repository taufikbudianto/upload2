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
@Table(name = "index_sweepwidth")
public @Data class IndexSweepWidth extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "indexsweepwidthid", length = 15)
    private String indexSweepWidthID;
    @Column(name = "rescuer", length = 50)
    private String rescuer;
    @Column(name = "searchobject", length = 50)
    private String searchObject;
    @Column(name = "visibility")
    private Double visibility;
    @Column(name = "eyeheight")
    private Double eyeHeight;
    @Column(name = "width")
    private Double width;
}
