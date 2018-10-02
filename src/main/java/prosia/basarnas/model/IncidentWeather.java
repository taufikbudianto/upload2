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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "inc_weather")
public @Data
class IncidentWeather extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "weatherid", length = 15)
    private String weatherID;
    @Column(name = "location", nullable = false, length = 50)
    private String location;
    @Column(name = "dtg", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtg;
    @Column(name = "dtgtimezone", nullable = false)
    private TimeZone dtgTimeZone;
    @Column(name = "windward", length = 15)
    private String windWard;
    @Column(name = "windspeed", length = 15)
    private String windSpeed;
    @Column(name = "seaward", length = 15)
    private String seaWard;
    @Column(name = "seaspeed", length = 15)
    private String seaSpeed;
    @Column(name = "temperature", length = 10)
    private String temperature;
    @Column(name = "skycondition", length = 15)
    private String skyCondition;
    @Column(name = "heightofwaves", length = 15)
    private String heightOfWaves;

    @ManyToOne
    @JoinColumn(name = "incidentid")
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
}
