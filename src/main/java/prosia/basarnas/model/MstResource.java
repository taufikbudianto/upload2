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
@Table(name = "mst_resource")
public @Data class MstResource extends AbstractAuditingEntity implements Serializable {
    @Id
    @Column(name="resourceid",length=10)
    private String resourceID;
    @Column(name="resourcename",nullable=false,length=100)
    private String resourceName;
    @ManyToOne
    @JoinColumn(name="regionid",nullable=false)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstRegion region;
    @ManyToOne
    @JoinColumn(name="agencyid",nullable=false)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstAgency agency;
    @Column(name="specialization",length=150)
    private String specialization;
    @Column(name="longitude",length=15)
    private String longitude;
    @Column(name="latitude",length=15)
    private String latitude;
    @Column(name="address",length=200)
    private String address;
    @Column(name="contactperson",length=50)
    private String contactPerson;
    @Column(name="phonenumber",length=25)
    private String phoneNumber;
    @Column(name="mobilenumber",length=25)
    private String mobileNumber;
    @Column(name="fax",length=25)
    private String fax;
    @Column(name="email",length=50)
    private String email;
    @Column(name="imageurl",length=200)
    private String imageurl;
    @ManyToOne
    @JoinColumn(name="parentresourceid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstResource parentResource;
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
    @Column(name="issar")
    private boolean sar;
}
