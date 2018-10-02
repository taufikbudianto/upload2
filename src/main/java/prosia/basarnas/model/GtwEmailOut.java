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
import javax.persistence.Lob;
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
@Table(name = "GTW_EMAILOUTGOING")
public @Data 
        class GtwEmailOut extends AbstractAuditingEntity implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "EMAILOUTGOINGID")
    private String emailoutgoingid;
    @Column(name = "RECIPIENTNAME")
    private String recipientname;
    @Column(name = "EMAILADDRESS")
    private String emailaddress;
    @Column(name = "SUBJECT")
    private String subject;
    @Lob
    @Column(name = "TEXTMESSAGE")
    private String textmessage;
    @Column(name = "ISDELIVERED")
    private BigInteger isdelivered;
    @Column(name = "REFERENCE")
    private String reference;
    @Column(name = "MESSAGETYPE")
    private BigInteger messagetype;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "USERSITEID")
    private String usersiteid;
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
    @Column(name = "EMAILCATEGORYID")
    private String emailcategoryid;
    
}
