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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import oracle.sql.CLOB;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "uti_beaconmessage")
public @Data
class UtiBeaconMessage extends AbstractAuditingEntity implements Serializable{

    @Id
    @Column(name = "messageid", length = 11)
    private Integer messageID;
    @Column(name = "msgnumber")
    private String msgNumber;
    @Column(name = "reportmcc")
    private String reportMcc;
    @Column(name = "transmitdtg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transmitDtg;
    @Column(name = "sit")
    private String sit;
    @Column(name = "messagetext")
    private String messageText;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "isdeleted")
    private boolean deleted;
    @Column(name="datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

   
}
