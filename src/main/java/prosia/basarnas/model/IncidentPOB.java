/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author TOMY
 */
@Entity
@Table(name="INC_POB")
@Data 
public class IncidentPOB extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "pobid", length = 15)
    private String pobID;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "ageunit", nullable = false)
    private String ageunit;
    @Column(name = "gender", nullable = false, length = 1)
    private String gender;
    @Column(name = "timefoundtimezone")
    private String timefoundtimezone;
    @ManyToOne
    @JoinColumn(name = "countryid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstNegara country;
    @Column(name = "address", length = 250)
    private String address;
    @Column(name = "timefound")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFound;
    @Column(name = "latitude", length = 15)
    private String latitude;
    @Column(name = "longitude", length = 15)
    private String longitude;
    @Column(name = "condition", length = 100)
    private String condition;
    @Column(name = "diagnosis", length = 50)
    private String diagnosis;
    @Column(name = "destination", nullable = false, length = 50)
    private String destination;
    @Column(name = "destphone", length = 25)
    private String destPhone;
    @Column(name = "vehicle", nullable = false, length = 50)
    private String vehicle;
    @Column(name="isdeleted")
    private BigInteger isdeleted;

    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
    
//    @JoinColumn(name = "incidentid", referencedColumnName = "incidentid")
//    @ManyToOne(fetch=FetchType.LAZY)
//    private Incident incident;
    
    @Column(name = "usersiteid", length = 3)
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
}
