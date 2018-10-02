/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "GTW_SMSINCOMING")
public @Data
        class GtwSmsIn extends AbstractAuditingEntity implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "SMSINCOMINGID")
    private String smsincomingid;
    @Column(name = "OPERATOR")
    private String operator;
    
    @Column(name = "RECEIVER")
    private BigInteger receiver;
    @Column(name = "MSG")
    private String msg;
    @Column(name = "MESSAGETYPE")
    private String messagetype;
    @Column(name = "REFERENCE")
    private String reference;
    
    
    @Column(name = "SENTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date senttime;
    
    @Column(name = "RECEIVEDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedtime;
   
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
    @Column(name = "EXPIREDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireddate;
    @Column(name = "DELIVEREDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date delivereddate;
    @Column(name = "PRIORITAS")
    private BigInteger prioritas;
    @Column(name = "INDEXSMS")
    private String indexsms;
    @Column(name = "SMSCATEGORYID")
    private String smscategoryid;
    
    @Column(name = "SENDER")
    private String sender;
    
}
