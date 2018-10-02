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
@Table(name = "INC_ASW")
public @Data
class IncidentAsw extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ASWID", length = 15)
    private String aswId;
    @ManyToOne
    @JoinColumn(name = "INCIDENTID", nullable = false)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
    @Column(name = "TIMEOBSERVATION", length = 50)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeObservation;
    @Column(name = "TIMEZONE")
    private String timeZone;
    @Column(name = "TIMEINTERVAL1", length = 50)
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private String timeInterval1;
    @Column(name = "TIMEINTERVAL2", length = 50)
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private String timeInterval2;
    @Column(name = "WINDIR", length = 50)
    private String winDir;
    @Column(name = "WINSPEED", length = 100)
    private String winSpeed;
    @Column(name = "NUMBEROFHOURS", length = 100)
    private String numberHours;
    @Column(name = "CONTRIBUTION", length = 100)
    private String contribution;
    @Column(name = "RADIAN", length = 100)
    private String radian;
    @Column(name = "SIN", length = 100)
    private String sinus;
    @Column(name = "COS", length = 100)
    private String cosinus;
    @Column(name = "ASIN", length = 100)
    private String asin;
    @Column(name = "ACOS", length = 100)
    private String acos;

}
