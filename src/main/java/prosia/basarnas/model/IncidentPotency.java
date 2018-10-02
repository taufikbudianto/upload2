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
@Table(name = "inc_potency")
public @Data
class IncidentPotency extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "incpotencyid", length = 15)
    private String incidentPotencyID;
    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "potencyid", nullable = true)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private ResPotency potency;
    @ManyToOne
    @JoinColumn(name = "kantorsarid", nullable = true)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstKantorSar officeSar;
    @Column(name = "potencyname", length = 100)
    private String potencyName;
    @Column(name = "address", length = 255)
    private String address;
    @Column(name = "longitude", length = 50)
    private String longitude;
    @Column(name = "latitude", length = 50)
    private String latitude;
    @Column(name = "phonenumber1", length = 50)
    private String phoneNumber1;
    @Column(name = "phonenumber2", length = 50)
    private String phoneNumber2;
    @Column(name = "phonenumber3", length = 50)
    private String phoneNumber3;
    @Column(name = "faxnumber", length = 50)
    private String faxNumber;
    @Column(name = "radiofrequency", length = 50)
    private String radioFrequency;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "socialnetwork", length = 255)
    private String socialNetwork;
    @Column(name = "gissymbol", length = 25)
    private String gisSymbol;
    @Column(name = "potencytype")
    private Integer potencyType;
    @ManyToOne
    @JoinColumn(name = "areacodeid")
    @BatchSize(size = 20)
    private CrmAreaCode codeArea;
    @ManyToOne
    @JoinColumn(name = "regionid", nullable = true)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstRegion region;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "dateCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "createdBy", length = 50)
    private String createdBy;
    @Column(name = "lastmodified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "modifiedby", length = 50)
    private String modifiedBy;
    @Column(name = "usersiteid")
    private String userSiteID;
    @Column(name = "isdeleted")
    private boolean deleted;

}
