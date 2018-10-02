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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "MST_MANUFACTURER")
public @Data
class Manufacturer extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "MANUFACTURERID", nullable = false)
    private String manufacturerId;
    @Column(name = "MANUFACTURERNAME")
    private String manufacturerName;
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
    @Column(name = "ISDELETED", nullable = false)
    private Integer isDeleted;
}
