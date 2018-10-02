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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import prosia.basarnas.enumeration.MapCalculationType;

/**
 *
 * @author Aris
 */
@Entity
@Table(name = "inc_tasksearcharea")
@Data
public class IncTaskSearchArea extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "tasksearchareaid", length = 15)
    private String taskSearchAreaID;
    @Column(name = "trapeziumtaskareaid", length = 50)
    private String trapeziumTaskAreaID;
    @Column(name = "drifttaskareaid", length = 50)
    private String driftTaskAreaID;
    @Column(name = "name", length = 20)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "area", nullable = true)
    private String area;
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
    @ManyToOne()
    @JoinColumn(name = "searchareaid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private SearchArea searchArea;
//    @OneToMany(targetEntity = IncidentSearchPattern.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @JoinColumn(name = "tasksearchareaid")
//    @Fetch(FetchMode.SUBSELECT)
//    private Set<IncidentSearchPattern> searchPatterns;

    @Lob
    @Column(name = "json_map")
    private String jsonMap;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "type_map", length = 50)
    @Enumerated(EnumType.STRING)
    private MapCalculationType type;
}
