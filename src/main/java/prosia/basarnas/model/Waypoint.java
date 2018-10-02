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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
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
@Table(name = "inc_waypoint")
public @Data
class Waypoint extends AbstractAuditingEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INV_WAYPOINT_SEQ_ID")
//    @SequenceGenerator(sequenceName = "INV_WAYPOINT_SEQ", allocationSize = 1, name = "INV_WAYPOINT_SEQ_ID")
    @Column(name = "waypointid", nullable = false, length = 15)
    private String waypointID;
    @Column(name = "waypointname", length = 50)
    private String waypointName;
    @Column(name = "sequence")
    private int sequence;
    @Column(name = "latitude", length = 100)
    private String latitude;
    @Column(name = "longitude", length = 100)
    private String longitude;
    @Column(name = "distance")
    private Double distance;
    @Column(name = "createdby", length = 50)
    private String createdBy;
    @Column(name = "datecreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "lastmodified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "modifiedby")
    private String modifiedBy;
    @Column(name = "usersiteid")
    private String userSiteID;
    @ManyToOne
    @JoinColumn(name = "searchpatternid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private IncidentSearchPattern searchPattern;
}
