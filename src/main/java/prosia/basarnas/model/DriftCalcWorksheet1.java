/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Aris
 */
@Entity
@Table(name = "dfc_worksheet1")
public @Data
class DriftCalcWorksheet1 extends AbstractAuditingEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "worksheetid", length = 15)
    private String worksheetID;
    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "indexleewayid", nullable = false)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private IndexLeeway indexLeeway;
    @Column(name = "worksheetname", length = 50)
    private String worksheetName;
    @Column(name = "incidentdescription", length = 250)
    private String incidentDescription;
    @Column(name = "incidentlatitude", length = 50)
    private String incidentLatitude;
    @Column(name = "incidentlongitude", length = 50)
    private String incidentLongitude;
    @Column(name = "incidenttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date incidentTime;
    @Column(name = "incidenttimezone")
    //private TimeZone incidentTimeZone;
    private String incidentTimeZone;
    @Column(name = "safetyfactor")
    private Double safetyFactor;
    @Column(name = "drifthours")
    private Double driftHours;
    @Column(name = "seacurrentangle")
    private Double seaCurrentAngle;
    @Column(name = "seacurrentspeed")
    private Double seaCurrentSpeed;
    @Column(name = "seacurrentdistance")
    private Double seaCurrentDistance;
    @Column(name = "surfacewindangle")
    private Double surfaceWindAngle;
    @Column(name = "windcurrentangle")
    private Double windCurrentAngle;
    @Column(name = "windcurrentspeed")
    private Double windCurrentSpeed;
    @Column(name = "windcurrentspeedforcalcdist")
    private Double windCurrentSpeedForCalcDist;
    @Column(name = "windcurrentdistance")
    private Double windCurrentDistance;
    @Column(name = "leewaydivergenceangle")
    private Double leewayDivergenceAngle;
    @Column(name = "leewaydistance")
    private Double leewayDistance;
    @Column(name = "leewayspeed")
    private Double leewaySpeed;
    @Column(name = "leewayleftangle")
    private Double leewayLeftAngle;
    @Column(name = "leewayrightangle")
    private Double leewayRightAngle;
    @Column(name = "driftleftdistance")
    private Double driftLeftDistance;
    @Column(name = "driftrightdistance")
    private Double driftRightDistance;
    @Column(name = "driftlefttorightdistance")
    private Double driftLeftToRightDistance;
    @Column(name = "drifterror")
    private Double driftError;
    @Column(name = "drifterrorpercentage")
    private Double driftErrorPercentage;
    @Column(name = "distresscrafterror")
    private Double distressCraftError;
    @Column(name = "searchcrafterror")
    private Double searchCraftError;
    @Column(name = "totalprobableerror")
    private Double totalProbableError;
    @Column(name = "driftleftlatitude", length = 50)
    private String driftLeftLatitude;
    @Column(name = "driftleftlongitude", length = 50)
    private String driftLeftLongitude;
    @Column(name = "driftrightlatitude", length = 50)
    private String driftRightLatitude;
    @Column(name = "driftrightlongitude", length = 50)
    private String driftRightLongitude;
//    @Column(name = "dedriftleftlatitude", length = 50)
//    private String deDriftLeftLatitude;
//    @Column(name = "dedriftleftlongitude", length = 50)
//    private String deDriftLeftLongitude;
//    @Column(name = "dedriftrightlatitude", length = 50)
//    private String deDriftRightLatitude;
//    @Column(name = "dedriftrightlongitude", length = 50)
//    private String deDriftRightLongitude;
    @Column(name = "datumlatitude", length = 50)
    private String datumLatitude;
    @Column(name = "datumlongitude", length = 50)
    private String datumLongitude;
    @Column(name = "searchradius")
    private Double searchRadius;
    @Column(name = "searcharea")
    private Double searchArea;
    @Column(name = "driftlefttorightangle")
    private Double driftLeftToRightAngle;
//    @OneToOne(cascade = {javax.persistence.CascadeType.ALL})
//    @JoinColumn(name = "searchareaid")
//    private SearchArea datumSearchArea;
    @Column(name = "inctodatumangle")
    private Double incToDatumAngle;
    @Column(name = "inctodatumdistance")
    private Double incToDatumDistance;
    @Column(name = "operationtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationTime;
    @Column(name = "operationtimezone")
    //private TimeZone operationTimeZone;
    private String operationTimeZone;
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
    @Column(name = "isdeleted")
    private boolean deleted;
    @Column(name = "usersiteid")
    private String userSiteID;
    // helper
    @Transient
    private Double incLatDegree;
    @Transient
    private Double incLatMinute;
    @Transient
    private Double incLatSecond;
    @Transient
    private boolean incLatSideIsSouth;
    @Column(name="unit")
    private Double unit;
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
