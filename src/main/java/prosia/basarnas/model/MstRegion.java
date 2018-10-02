/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "mst_region")
public @Data class MstRegion extends AbstractAuditingEntity implements Serializable {
    @Id
    @Column(name="regionid",length=5)
    private String regionID;
    @Column(name="name",nullable=false,length=50)
    private String name;
    @Column(name="area")
    private String area;
    @ManyToOne
    @JoinColumn(name="countryid",nullable=false)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size=20)
    private MstNegara country;
    @Column(name="kml")
    private String kml;
    @Column(name="location",length=150)
    private String location;
    @Column(name="usersiteid",length=3)
    private String userSiteID;
    @Column(name="isdeleted")
    private boolean deleted;
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
}
