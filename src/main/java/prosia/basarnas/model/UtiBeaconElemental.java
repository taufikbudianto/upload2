/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
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
import oracle.sql.CLOB;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "UTI_BEACONELEMENTAL")
public @Data
class UtiBeaconElemental extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "elementalid", length = 15)
    private Integer elementalID;
    @Column(name = "msgnumber")
    private String msgNumber;
    @Column(name = "reportmcc")
    private String reportMcc;
    @Column(name = "transmitdtg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transmitDtg;
    @Column(name = "sit")
    private String sit;
    @Column(name = "sol")
    private String sol;
    @Column(name = "destmcc")
    private String destMcc;
    @Column(name = "alertdoppler")
    private String alertDoppler;
    @Column(name = "retransmit")
    private String retransmit;
    @Column(name = "servicearea")
    private String serviceArea;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "probability")
    private String probability;
    @Column(name = "confidence")
    private String confidence;
    @Column(name = "beaconid")
    private String beaconID;
    @Column(name = "spacecraftid")
    private String spacecraftID;
    @Column(name = "sourceid")
    private String sourceID;
    @Column(name = "flagfreq")
    private String flagFreq;
    @Column(name = "bias")
    private String bias;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tca")
    private Date tca;
    @Column(name = "errorellipse")
    private String errorEllipse;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assigndate")
    private Date assignDate;
    @Column(name = "windowfactor")
    private String windowFactor;
    @Column(name = "iteration")
    private String iteration;
    @Column(name = "ctangle")
    private String ctAngle;
    @Column(name = "secondarysourceid")
    private String secondarySourceID;
    @Column(name = "sideband")
    private String sideband;
    @Column(name = "numofpoint")
    private String numOfPoint;
    @Column(name = "longmsg")
    private String longMsg;
    @ManyToOne
    @JoinColumn(name = "compositeID")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private UtiBeaconComposite beaconComposite;
    @Column(name = "isdeleted")
    private boolean deleted;
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @ManyToOne
    @JoinColumn(name = "kantorsarid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstKantorSar officeSar;

}
