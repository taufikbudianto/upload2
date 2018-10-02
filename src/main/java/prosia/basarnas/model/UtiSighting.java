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
@Table(name = "UTI_SIGHTING")
public @Data
class UtiSighting extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "SIGHTINGID")
    private String sightingId;
    @Column(name = "CREATEDBY")
    private String createdBy;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "MODIFIEDBY")
    private String modifiedBy;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
//    @ManyToOne
//    @JoinColumn(name = "INCIDENTID")
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size = 25)
    @JoinColumn(name = "INCIDENTID", referencedColumnName = "INCIDENTID")
    @ManyToOne
    private Incident incident;
    @Column(name = "OBSERVERNAME")
    private String observerName;
    @Column(name = "OBSERVERPHONE")
    private String observerPhone;
    @Column(name = "OBSERVERLOCATION")
    private String observerLocation;
    @Column(name = "OBSERVERLONGITUDE")
    private String observerLongitude;
    @Column(name = "OBSERVERLATITUDE")
    private String observerLatitude;
    @Column(name = "OBSERVERUNCERTAINTY")
    private String observerUncertainty;
    @Column(name = "OBSERVERREMARKS")
    private String observerRemarks;
    @Column(name = "OBJECTPOSITION")
    private String objectPosition;
    @Column(name = "OBJECTLONGITUDE")
    private String objectLongitude;
    @Column(name = "OBJECTLATITUDE")
    private String objectLatitude;
    @Column(name = "OBJECTUNCERTAINTY")
    private String objectUncertainty;
    @Column(name = "OBJECTREMARKS")
    private String objectRemarks;
    @Column(name = "REPORTTYPE")
    private Integer reportType;
    @Column(name = "OBJECTTYPE")
    private Integer objectType;
    @Column(name = "REMARKS")
    private String remarks;
    @Column(name = "WEATHERDESCRIPTION")
    private String weatherDescription;
    @Column(name = "ASSESMENTPRIORITY")
    private Integer assesmentPriority;
    @Column(name = "ASSESMENTANNOTATION")
    private String assesmentAnnotation;
    @Column(name = "ASSESMENTNOTES")
    private String assesmentNotes;
    @Column(name = "USERSITEID")
    private String usersiteId;
    @Column(name = "OBJECTDTG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date objectDtg;
    @Column(name = "OBJECTDTGUNCERTAINTY")
    private String objectDtgUncertainty;
    @Column(name = "ASSESMENTSTATUS")
    private Integer assesmentStatus;
    @Column(name = "ASSESMENTSTATUSOTHER")
    private String assesmentStatusOther;
    @Column(name = "OBJECTTYPEOTHER")
    private String objectTypeOther;
    @Column(name = "OBJECTDTGTIMEZONE")
    private String objectDtgTimezone;
    @Column(name = "OBJECTUNCERTAINTYUNIT")
    private String objectUncertaintyUnit;
    @Column(name = "OBJECTDTGUNCERTAINTYUNIT")
    private String objectDtgUncentaintyUnit;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "ISCRM")
    private Boolean iscrm;
    @Column(name = "SIGHTINGNUMBER")
    private String sightingNumber;
}
