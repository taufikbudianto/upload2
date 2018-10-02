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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name="PRO_LOG")
public @Data class IncidentLog extends AbstractAuditingEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "logid", length = 15)
    private String logID;
    @Column(name = "logdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Column(name= "logdatetimezone", nullable= false)
    private String logDateTimeZone;
    @Column(name = "logtype", nullable = false, length = 20)
    private String logType;
    @Column(name = "remarks", nullable = false)
    private String remarks;
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    @Column(name = "attachment", length = 100)
    private String attachment;
    @Column(name = "longitude", length = 50)
    private String longitude;
    @Column(name = "latitude", length = 50)
    private String latitude;
    @Column(name = "stages", length = 20)
    private String stages;
    @Column(name = "STATUS_APPROVE", length = 30)
    private String stsapprove;
    @Column(name = "INCIDENTID")
    private String incidentId;
    
//    @ManyToOne
//    @JoinColumn(name = "incidentid")
//    //@fetch
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size=25)
//    private Incident incident;
    
    @JoinColumn(name = "INCIDENTID", referencedColumnName = "INCIDENTID" ,insertable = false, updatable = false)
    @ManyToOne
    private Incident incident;
    
    @ManyToOne
    @JoinColumn(name = "sightingid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private UtiSighting sighting;
    
    //private String sightingId;
    
    @Column(name = "sruid", length = 20)
    private String sruID;
    
    
    @ManyToOne
    @JoinColumn(name = "personnelid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPersonnel personnel;
    
    //private String personelId;
    
    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "radiogramid")
    private IncidentRadiogram radiogram;
    
    @ManyToOne
    @JoinColumn(name = "searchpatternid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private IncidentSearchPattern searchPattern;
    
    @Column(name="usersiteid",length=3)
    private String userSiteID;
    @Column(name="isdeleted")
    private boolean deleted;
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "createdby", length = 50)
    private String createdBy;
    @Column(name = "lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "modifiedby", length = 50)
    private String modifiedBy;
    @Column(name = "subject")
    private String subject;
    
}
