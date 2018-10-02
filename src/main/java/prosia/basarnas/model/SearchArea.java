/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
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
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Aris
 */
@Entity
@Table(name = "inc_searcharea")
public @Data
class SearchArea extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "searchareaid", length = 15)
    private String searchAreaID;
    @Column(name = "name", length = 20)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "area", nullable = true)
    private String area;
    @ManyToOne
    @JoinColumn(name = "incidentid", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
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
    @Column(name = "parrentid")
    private String parrentID;
//    @OneToMany(targetEntity= IncidentSearchPattern.class, fetch= FetchType.EAGER, cascade= CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @JoinColumn(name="searchareaid")
//    private Set<IncidentSearchPattern> searchPatterns;
//    @OneToMany(targetEntity= TaskSearchArea.class, fetch= FetchType.EAGER, cascade= CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @JoinColumn(name="searchareaid")
//    private Set<TaskSearchArea> taskSearchAreas;
    @Column(name = "radius")
    private Double radius;
    @Column(name = "shape")
    private String shape;
    @Lob
    @Column(name = "json_map")
    private String jsonMap;
    @Column(name = "status")
    private Boolean status;
}
