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
@Table(name="dfc_trapeziumtaskarea")
@Data
public class TrapeziumTaskArea extends AbstractAuditingEntity implements Serializable {
    @Id
    @Column(name="trapeziumtaskareaid", length=15)
    private String trapeziumTaskAreaID;
    @Column(name="name", length=50)
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="taskarealength")
    private Double taskAreaLength;
    @Column(name="split")
    private String split;
    @Column(name="luastaskarea")
    private Double luasTaskArea;
    @Column(name="smallcoord1")
    private String smallCoord1;
    @Column(name="smallcoord2")
    private String smallCoord2;
    @Column(name="bigcoord1")
    private String bigCoord1;
    @Column(name="bigcoord2")
    private String bigCoord2;
    @ManyToOne
    @JoinColumn(name = "trapeziumareaid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private TrapeziumArea trapeziumArea;
    @Column(name="usersiteid")
    private String userSiteID;
    @Column(name = "isdeleted")
    private boolean deleted;
    @Column(name="datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name="createdby",length=50)
    private String createdBy;
    @Column(name="lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name="modifiedby",length=50)
    private String modifiedBy;
    @Column(name="unit")
    private Double unit;
    @Column(name="waypoint")
    private Double waypoint;

}
