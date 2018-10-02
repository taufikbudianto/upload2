/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
@Table(name="MST_COUNTRY")
public @Data class MstNegara extends AbstractAuditingEntity implements Serializable {
    @Id
    @Column(name="countryid",length=3)
    private String countryID;
    @Column(name="countryname",nullable=false,length=50)
    private String countryName;
    @Column(name="mccid",length=50)
    private String mccID;
    @Column(name="rccid",length=50)
    private String rccID;
    @Column(name="rccphone",length=50)
    private String rccPhone;
    @Column(name="rccfax",length=50)
    private String rccFax;
    @Column(name="usersiteid",length=3)
    private String userSiteID;
    @Column(name="isdeleted")
    private BigInteger isdeleted;
    @Column(name="datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name="createdby",length=50)
    private String createdBy;
    @Column(name="lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name="modifiedby",length=50)
    private String modifiedBy;
    @Column(name = "ISDELETED34")
    private BigInteger deleted;
}
