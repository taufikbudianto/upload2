/*
 * To change this template, choose Tools | Templates
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
 * @author rinamay
 */
@Entity
@Table(name = "inc_placemark")
public @Data
class IncidentPlacemark implements Serializable {

    @Id
    @Column(name = "placemarkid", length = 15)
    private String placemarkID;
    @Column(name = "placemarkname", length = 50)
    private String placemarkName;
    @Column(name = "description", length = 300)
    private String description;
    @Column(name = "latitude", length = 50)
    private String latitude;
    @Column(name = "longitude", length = 50)
    private String longitude;
    @Column(name = "image", length = 255)
    private String image;
    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
    @Column(name = "usersiteid", length = 3)
    private String userSiteID;
    @Column(name = "isdeleted")
    private boolean deleted;
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
    @Column(name = "markerid")
    private String markerId;
}
