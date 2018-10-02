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
@Table(name = "INC_ASSET")
@Data
public class IncidentAsset extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "incassetid", length = 15)
    private String incidentAssetID;
    @ManyToOne
    @JoinColumn(name = "assettypeid", nullable = false)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstAssetType assetType;
    @ManyToOne
    @JoinColumn(name = "kantorsarid", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstKantorSar officeSar;
    @ManyToOne
    @JoinColumn(name = "possarid", nullable = true)
    private MstPosSar posSar;
    @ManyToOne
    @JoinColumn(name = "potencyid", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPotency potency;
    @Column(name = "code", length = 50)
    private String code;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "goodqty")
    private BigInteger goodQty;
    @Column(name = "rejectedqty")
    private BigInteger rejectedQty;
    @Column(name = "servicedqty")
    private BigInteger servicedQty;
    @Column(name = "otherqty")
    private BigInteger otherQty;
    @Column(name = "assetcondition", length = 25)
    private String assetCondition;
    @Column(name = "cargo", length = 10)
    private String cargo;
    @Column(name = "functional", length = 50)
    private String functional;
    @Column(name = "latitude", length = 15)
    private String latitude;
    @Column(name = "longitude", length = 15)
    private String longitude;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status")
    private Integer status;
    @Column(name = "endurance")
    private Double endurance;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "vehicletype", length = 50)
    private String vehicleType;
    @ManyToOne
    @JoinColumn(name = "incidentid", nullable = false)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "assetid", nullable = false)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResAsset asset;
    @Column(name = "usageqty")
    private Integer usageQty;
//    @Column(name = "usagestatus")
//    private Integer usageStatus;
    @Column(name = "usagedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usageDate;
    @Column(name = "assignmentdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date assignmentDate;
    @Column(name = "assignmentenddate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date assignmentEndDate;
    @Column(name = "usersiteid")
    private String userSiteID;
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
    
}
