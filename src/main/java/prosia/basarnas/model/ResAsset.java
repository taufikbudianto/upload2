/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "res_asset")
public @Data class ResAsset extends AbstractAuditingEntity implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Column(name = "assetid", length = 15)
//    private String assetID;
//    @ManyToOne
//    @JoinColumn(name = "assettypeid")
//    @BatchSize(size=40)
//    private MstAssetType assetType;
//    @ManyToOne
//    @JoinColumn(name = "kantorsarid", nullable = true)
//    @BatchSize(size=25)
//    private MstKantorSar officeSar;
//
//    @ManyToOne
//    @JoinColumn(name = "potencyid", nullable = true)
//    @BatchSize(size=25)
//    private ResPotency potency;
//    @Column(name = "code", length = 50)
//    private String code;
//    @Column(name = "name", length = 50)
//    private String name;
//    @Column(name = "goodqty")
//    private Integer goodQty;
//    @Column(name = "rejectedqty")
//    private Integer rejectedQty;
//    @Column(name = "servicedqty")
//    private Integer servicedQty;
//    @Column(name = "otherqty")
//    private Integer otherQty;
//    @Column(name = "assetcondition", length = 50)
//    private String assetCondition;
//    @Column(name = "cargo", length = 50)
//    private String cargo;
//    @Column(name = "functional", length = 50)
//    private String functional;
//    @Column(name = "latitude", length = 15)
//    private String latitude;
//    @Column(name = "longitude", length = 15)
//    private String longitude;
//    @Column(name = "remarks")
//    private String remarks;
//    @Column(name = "status")
//    private Integer status;
//    @Column(name = "endurance")
//    private Double endurance;
//    @Column(name = "speed")
//    private Double speed;
//    @Column(name = "vehicletype", length = 50)
//    private String vehicleType;
//    @Column(name = "usersiteid")
//    private String userSiteID;
//    @Column(name = "datecreated")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateCreated;
//    @Column(name = "createdby", length = 50)
//    private String createdBy;
//    @Column(name = "lastmodified")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModified;
//    @Column(name = "modifiedby", length = 50)
//    private String modifiedBy;
//    @Column(name = "isdeleted")
//    private boolean deleted;
//
//    @OneToMany(targetEntity = ResAssetContact.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size=50)
//    @JoinColumn(name = "assetid")
//    private Set<ResAssetContact> assetContacts;
//    // helper for gis calc
//    @Transient
//    private Double distance;
//    @Transient
//    private Double tilt;
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ASSETID")
    private String assetid;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "GOODQTY")
    private BigInteger goodqty;
    @Column(name = "REJECTEDQTY")
    private BigInteger rejectedqty;
    @Column(name = "SERVICEDQTY")
    private BigInteger servicedqty;
    @Column(name = "OTHERQTY")
    private BigInteger otherqty;
    @Column(name = "ASSETCONDITION")
    private String assetcondition;
    @Column(name = "CARGO")
    private String cargo;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "USERSITEID")
    private String usersiteid;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "FUNCTIONAL")
    private String functional;
    @Basic(optional = false)
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
    @Column(name = "STATUS")
    private Integer status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ENDURANCE")
    private Double endurance;
    @Column(name = "SPEED")
    private Double speed;
    @Column(name = "VEHICLETYPE")
    private String vehicletype;
    @Column(name = "REMARKS")
    private String remarks;
//    @OneToMany(mappedBy = "assetid")
//    private Collection<ResAssetContact> resAssetcontactCollection;
    @JoinColumn(name = "ASSETTYPEID", referencedColumnName = "ASSETTYPEID")
    @ManyToOne
    private MstAssetType assettypeid;
    @JoinColumn(name = "KANTORSARID", referencedColumnName = "KANTORSARID")
    @ManyToOne
    private MstKantorSar kantorsarid;
    @JoinColumn(name = "POSSARID", referencedColumnName = "POSSARID")
    @ManyToOne
    private MstPosSar possarid;
    @JoinColumn(name = "POTENCYID", referencedColumnName = "POTENCYID")
    @ManyToOne
    private ResPotency potencyid;
    @Column(name = "OP_TYPE")
    private Integer op_type;
}
