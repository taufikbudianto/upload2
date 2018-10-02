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
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Aris
 */
@Entity
@Table(name="dfc_trapeziumarea")
@Data
public class TrapeziumArea extends AbstractAuditingEntity implements Serializable {
    @Id
    @Column(name="trapeziumareaid")
    private String trapeziumAreaID;
    @Column(name="worksheetname")
    private String worksheetName;
    @Column(name="deskripsi")
    private String deskripsi;
    @Column(name = "waktuoperasi")
    @Temporal(TemporalType.TIMESTAMP)
    private Date waktuoperasi;
    @Column(name="waktuoperasitimezone")
    private TimeZone waktuOperasiTimezone;
    @Column(name="latlkp")
    private Double latLkp;
    @Column(name="longlkp")
    private Double longLkp;
    @Column(name="latdest")
    private Double latDest;
    @Column(name="longdest")
    private Double longDest;
    @Column(name="safetyfactor")
    private Double safetyFactor;
    @Column(name="distresserror")
    private Double distressError;
    @Column(name="searcherror")
    private Double searchError;
    @Column(name="luasarea")
    private Double luasArea;
    @Column(name="taskarealastpoint")
    private String taskAreaLastPoint;
    @Column(name="totaltaskarealength")
    private Double totalTaskAreaLength;
    @Column(name="waypoint")
    private Double waypoint;
    @Column(name="parentid")
    private String parentID;
    
    @ManyToOne
    @JoinColumn(name="incidentid")
    private Incident incident;
    
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
}
