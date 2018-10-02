/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "res_potency")
public @Data
class ResPotency extends AbstractAuditingEntity implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Column(name = "potencyid", length = 15)
//    private String potencyID;
//    @ManyToOne
//    @JoinColumn(name = "kantorsarid", nullable = true)
//    
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size=25)
//    private MstKantorSar officeSar;
//    
//    @Column(name = "potencyname", length = 100)
//    private String potencyName;
//    @Column(name = "address", length = 255)
//    private String address;
//    @Column(name = "longitude", length = 50)
//    private String longitude;
//    @Column(name = "latitude", length = 50)
//    private String latitude;
//
//    @Column(name = "phonenumber1", length = 50)
//    private String phoneNumber1;
//    @Column(name = "phonenumber2", length = 50)
//    private String phoneNumber2;
//    @Column(name = "phonenumber3", length = 50)
//    private String phoneNumber3;
//    @Column(name = "faxnumber", length = 50)
//    private String faxNumber;
//    @Column(name = "radiofrequency", length = 50)
//    private String radioFrequency;
//    @Column(name = "email", length = 255)
//    private String email;
//    @Column(name = "socialnetwork", length = 255)
//    private String socialNetwork;
//    @Column(name = "gissymbol", length = 25)
//    private String gisSymbol;
//    @Column(name = "potencytype")
//    private Integer potencyType;
//    @ManyToOne
//    @JoinColumn(name = "regionid", nullable = true)
//    @BatchSize(size=20)
//    private MstRegion region;
//    @Column(name = "remarks")
//    private String remarks;
//    @Column(name = "dateCreated")
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    private Date dateCreated;
//    @Column(name = "createdBy", length = 50)
//    private String createdBy;
//    @Column(name = "lastmodified")
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    private Date lastModified;
//    @Column(name = "modifiedby", length = 50)
//    private String modifiedBy;
//    @Column(name = "usersiteid")
//    private String userSiteID;
//    @Column(name = "isdeleted")
//    private boolean deleted;
//
//    @OneToMany(targetEntity = ResAsset.class, fetch = FetchType.EAGER)
//    @Where(clause = "isdeleted = 0")
//    //@Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size=20)
//    @JoinColumn(name = "potencyid")
//    private Set<ResAsset> potencyAssets;
//    
//    @OneToMany(targetEntity = ResPotencyContact.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    //@Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size=20)
//    @JoinColumn(name = "potencyid")
//    private Set<ResPotencyContact> potencyContacts;
//    
//    @OneToMany(targetEntity = ResPersonnel.class, fetch = FetchType.EAGER)    
//    @Where(clause = "isdeleted = 0")
//    //@Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size=20)
//    @JoinColumn(name = "potencyid")
//    private Set<ResPersonnel> potencyPersonnels;
//    @ManyToOne
//    @JoinColumn(name="areacodeid")
//    @BatchSize(size=20)
//    private CrmAreaCode codeArea;
//    // helper for gis calc
//    @Transient
//    private Double distance;
//    @Transient
//    private Double tilt;

    //--------------------------------------------
//    @OneToMany(mappedBy = "potencyid")
//    private Collection<ResPotencyContact> resPotencycontactCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "POTENCYID")
    private String potencyid;
//    @Column(name = "KANTORSARID")
//    private String sarid;
    @Column(name = "POTENCYNAME")
    private String potencyname;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "LATITUDE")
    private String latitude;
    @Basic(optional = false)
    @Column(name = "POTENCYLEVEL")
    private BigInteger potencylevel;
    @Column(name = "PHONENUMBER1")
    private String phonenumber1;
    @Column(name = "PHONENUMBER2")
    private String phonenumber2;
    @Column(name = "PHONENUMBER3")
    private String phonenumber3;
    @Column(name = "FAXNUMBER")
    private String faxnumber;
    @Column(name = "RADIOFREQUENCY")
    private String radiofrequency;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SOCIALNETWORK")
    private String socialnetwork;
    @Column(name = "GISSYMBOL")
    private String gissymbol;

//    @OneToMany(targetEntity = ResPotencyContact.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @BatchSize(size=20)
//    @JoinColumn(name = "potencyid")
//    private Set<ResPotencyContact> potencyContacts;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "USERSITEID")
    private String usersiteid;
    @Basic(optional = false)
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
    @Column(name = "POTENCYTYPE")
    private BigInteger potencytype;
    @Lob
    @Column(name = "REMARKS")
    private String remarks;
//    @OneToMany(mappedBy = "potencyid")
//    private Collection<ResPersonnel> resPersonnelCollection;

//    @OneToMany(targetEntity = ResPersonnel.class, fetch = FetchType.EAGER)    
//    @Where(clause = "isdeleted = 0")
//    @BatchSize(size=20)
//    @JoinColumn(name = "potencyid")
//    private Set<ResPersonnel> potencyPersonnels;        
    @JoinColumn(name = "AREACODEID", referencedColumnName = "AREACODEID")
    @ManyToOne
    private CrmAreaCode areacodeid;
    @JoinColumn(name = "KANTORSARID", referencedColumnName = "KANTORSARID")
    @ManyToOne
    private MstKantorSar kantorsarid;
//    @Column(name = "KANTORSARID")    
//    private String kantorsarid;
    @JoinColumn(name = "POSSARID", referencedColumnName = "POSSARID")
    @ManyToOne
    private MstPosSar possarid;
    @JoinColumn(name = "REGIONID", referencedColumnName = "REGIONID")
    @ManyToOne
    private MstRegion regionid;

    @Column(name = "N_INSTANSI")
    private String namaInstansi;
    @Column(name = "JML_HEWAN")
    private Integer jmlHewan;
    @Column(name = "JML_IT")
    private Integer jmlIT;
    @Column(name = "JML_LAUT")
    private Integer jmlLaut;
    @Column(name = "JML_UDARA")
    private Integer jmlUdara;
    @Column(name = "JML_DARAT")
    private Integer jmlDarat;
    @Column(name = "JML_KUALIFIKASI")
    private Integer jmlKualifikasi;
    @Column(name = "JML_SDM")
    private Integer jmlSdm;
    @Column(name = "C_STAT_INST")
    private Integer statInstitusi;
    @Column(name = "C_INSTITUSI")
    private Integer cInstitusi;
}
