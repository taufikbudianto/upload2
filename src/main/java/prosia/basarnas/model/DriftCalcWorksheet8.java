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
@Table(name = "dfc_worksheet8")

public @Data class DriftCalcWorksheet8 extends AbstractAuditingEntity implements Serializable, Cloneable{
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
    @Column(name = "searchobject", length = 50)
    private String searchObject;
    @Column(name = "searchplatform", length = 50)
    private String searchPlatform;
    @ManyToOne
    @JoinColumn(name = "assetid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResAsset asset;
    @Column(name = "worksheetname", length = 50)
    private String worksheetName;
    @Column(name = "meteorologicalvisibility")
    private Double meteorologicalVisibility;
    @Column(name = "searchheight")
    private Double searchHeight;
    @Column(name = "searchrepeat", length = 15)
    private String searchRepeat;
    @Column(name = "windspeed")
    private Double windSpeed;
    @Column(name = "fatiguefactor")
    private Boolean fatigueFactor;
    @Column(name = "uncorrectedsweepwidth")
    private Double uncorrectedSweepWidth;
    @Column(name = "weathercorrectionfactor")
    private Double weatherCorrectionFactor;
    @Column(name = "speedcorrectionfactor")
    private Double speedCorrectionFactor;
    @Column(name = "fatiguecorrectionfactor")
    private Double fatigueCorrectionFactor;
    @Column(name = "sweepwidthfactor")
    private Double sweepWidthFactor;
    @Column(name = "practicaltrackspacing")
    private Double practicalTrackSpacing;
    @Column(name = "coveragefactor")
    private Double coverageFactor;
    @Column(name = "probabilityofdetection")
    private Double probabilityOfDetection;
    @Column(name = "searcharea")
    private Double searchArea;
    @Column(name = "searchhours")
    private Double searchhours;
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
    @Column(name="unit")
    private Double unit;
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
