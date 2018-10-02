/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
 * @author PROSIA
 */
@Entity
@Table(name = "UTI_BEACONCOMPOSITE")
public @Data
class UtiBeaconComposite extends AbstractAuditingEntity implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "COMPOSITEID")
    private Integer compositeid;
    @Column(name = "ISDELETED")
    private Integer isdeleted;
    @Column(name = "MSGNUMBER")
    private String msgnumber;
    @Column(name = "REPORTMCC")
    private String reportmcc;
    @Column(name = "TRANSMITDTG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transmitdtg;
    @Column(name = "SIT")
    private String sit;
    @Column(name = "DESTMCC")
    private String destmcc;
    @Column(name = "ALERTDOPPLER")
    private String alertdoppler;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "PROBABILITY")
    private String probability;
    @Column(name = "CONFIDENCE")
    private String confidence;
    @Column(name = "BEACONID")
    private String beaconid;
    @Column(name = "BEACONMESSAGE")
    private String beaconmessage;
    @Column(name = "SPACECRAFTID")
    private String spacecraftid;
    @Column(name = "SOURCEID")
    private String sourceid;
    @Column(name = "FLAGFREQ")
    private String flagfreq;
    @Column(name = "BIAS")
    private String bias;
    @Column(name = "TCA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tca;
    @Column(name = "WINDOWFACTOR")
    private String windowfactor;
    @Column(name = "ITERATION")
    private String iteration;
    @Column(name = "CTANGLE")
    private String ctangle;
    @Column(name = "SECONDARYSOURCEID")
    private String secondarysourceid;
    @Column(name = "SIDEBAND")
    private String sideband;
    @Column(name = "NUMOFPOINT")
    private String numofpoint;
    @Column(name = "LONGMSG")
    private String longmsg;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "RETRANSMIT")
    private String retransmit;
    @Column(name = "SERVICEAREA")
    private String servicearea;
    @Column(name = "ERRORELLIPSE")
    private String errorellipse;
    @Column(name = "ASSIGNDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assigndate;
    @Column(name = "GPSLATITUDE")
    private String gpslatitude;
    @Column(name = "GPSLONGITUDE")
    private String gpslongitude;
    @JoinColumn(name = "INCIDENTID", referencedColumnName = "INCIDENTID")
    @ManyToOne
    private Incident incidentid;
    @JoinColumn(name = "KANTORSARID", referencedColumnName = "KANTORSARID")
    @ManyToOne
    private MstKantorSar kantorsarid;
}