/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;
/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "INCIDENT")
//@ManagedBean
public @Data
class Incident extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INCIDENTID")
    private String incidentid;
    @Basic(optional = false)
    @Column(name = "INCIDENTNUMBER")
    private String incidentnumber;
    @Basic(optional = false)
    @Column(name = "INCIDENTTYPE")
    private Integer incidenttype;
    @Column(name = "SITUATION")
    private String situation;
    @Column(name = "ALERTEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date alertedat;
    @Column(name = "ALERTEDBY")
    private String alertedby;
    @Column(name = "METHOD")
    private String method;
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Lob
    @Column(name = "REMARKS")
    private String remarks;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DEATHPEOPLE")
    private Integer deathpeople;
    @Column(name = "HEAVYINJUREDPEOPLE")
    private Integer heavyinjuredpeople;
    @Column(name = "LIGHTINJUREDPEOPLE")
    private Integer lightinjuredpeople;
    @Column(name = "LOSTPEOPLE")
    private Integer lostpeople;
    @Column(name = "SURVIVEDPEOPLE")
    private Integer survivedpeople;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "CREATEBY")
    private String createby;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "USERSITEID")
    private String usersiteid;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
    @Column(name = "CLOSEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedate;
    @Column(name = "ALERTEDATTIMEZONE") //laporan
    private String alertedattimezone;
    @Column(name = "CLOSEDATETIMEZONE")
    private String closedatetimezone;
    @Column(name = "STARTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    @Column(name = "STARTDATETIMEZONE") //insiden
    //private TimeZone startdatetimezone;
    private String startdatetimezone;
    @Column(name = "STARTOPSDATE") 
    @Temporal(TemporalType.TIMESTAMP)
    private Date startopsdate;
    @Column(name = "STARTOPSDATETIMEZONE")
    private String startopsdatetimezone;
    @Column(name = "SMC")
    private String smc;
    @Column(name = "SMCPHONE")
    private String smcphone;
    @Column(name = "INCIDENTNAME")
    @Size(max = 50)
    private String incidentname;
    @Column(name = "OWNERNAME")
    @Size(max = 50)
    private String ownername;
    @Column(name = "OWNERADDRESS")
    @Size(max = 50)
    private String owneraddress;
    @Column(name = "OWNERPHONE")
    @Size(max = 20)
    private String ownerphone;
    @Column(name = "OWNERCELL")
    @Size(max = 20)
    private String ownercell;
    @Column(name = "OWNERFAX")
    @Size(max = 20)
    private String ownerfax;
    @Column(name = "OWNEREMAIL")
    @Size(max = 25)
    private String owneremail;
    @Column(name = "OBJECTCALLSIGN")
    @Size(max = 25)
    private String objectcallsign;
    @Column(name = "OBJECTCOLOR")
    @Size(max = 25)
    private String objectcolor;
    @Column(name = "OBJECTLENGTH")
    @Size(max = 25)
    private String objectlength;
    @Column(name = "OBJECTCAPACITY")
    @Size(max = 25)
    private String objectcapacity;
    @Column(name = "OBJECTHOMEBASE")
    @Size(max = 25)
    private String objecthomebase;    
    @JoinColumn(name = "CHECKLISTID", referencedColumnName = "CHECKLISTID")
    @ManyToOne(optional = true)
    private MstChecklist checklistid;
    @Column(name = "INCIDENTSCALA")
    private Integer incidentScala;
    
    /*@Column(name = "KATEGORI")
    @Enumerated (EnumType.STRING)
    private Kategori kategori;
    
    @Column(name = "JENIS")
    @Enumerated (EnumType.STRING)
    private IncidentType jenis;*/
}
