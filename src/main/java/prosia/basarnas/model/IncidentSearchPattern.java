/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

/**
 *
 * @author Aris
 */
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

@Entity
@Table(name = "inc_searchpattern")
@Data
public class IncidentSearchPattern extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "searchpatternid", length = 15)
    private String searchPatternId;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "type", length = 255)
    private String type;
    @JoinColumn(name = "usersiteid")
    private String userSiteID;
    @Column(name = "description", length = 512)
    private String description;
    @ManyToOne
    @JoinColumn(name = "searchareaid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private SearchArea searchArea;
    @ManyToOne
    @JoinColumn(name = "incassetid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private IncidentAsset incidentAsset;
    @ManyToOne
    @JoinColumn(name = "tasksearchareaid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private IncTaskSearchArea taskSearchArea;
//    @OneToMany(targetEntity = Waypoint.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @JoinColumn(name = "searchpatternid")
//    @OrderBy("sequence")
//    private List<Waypoint> waypoints;
    @Lob
    @Column(name = "json_map")
    private String jsonMap;
    @Column(name = "status")
    private Boolean status;

    /*
    
   
    
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
     */
}
