/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "MST_BEACONMODEL")
public @Data
class BeaconModel extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "BEACONMODELID", nullable = false)
    private String beaconModelId;
    @Column(name = "BEACONMODELNAME")
    private String beaconModelName;
    @Column(name = "CSNO")
    private String csNo;
    @Column(name = "APPLICATION")
    private String application;
    @ManyToOne
    @JoinColumn(name = "MANUFACTURERID")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Manufacturer manufacturer;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "CREATEDBY")
    private String createdBy;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "MODIFIEDBY")
    private String modifiedBy;
    @Column(name = "USERSITEID")
    private String usersiteId;
    @Column(name = "ISDELETED")
    private Integer isDeleted;
}
