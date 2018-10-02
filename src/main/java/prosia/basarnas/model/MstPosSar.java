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
@Table(name = "MST_POSSAR")
public @Data class MstPosSar extends AbstractAuditingEntity implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "POSSARID")
    private String possarid;
    @Basic(optional = false)
    @Column(name = "POSSARNAME")
    private String possarname;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "PHONENUMBER1")
    private String phonenumber1;
    @Column(name = "PHONENUMBER2")
    private String phonenumber2;
    @Column(name = "PHONENUMBER3")
    private String phonenumber3;
    @Column(name = "FAXNUMBER")
    private String faxnumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "RADIOFREQUENCY")
    private String radiofrequency;
    @Column(name = "CONTACTNAME")
    private String contactname;
    @Column(name = "CONTACTPOSITION")
    private String contactposition;
    @Column(name = "CONTACTMOBILEPHONE")
    private String contactmobilephone;
    @Column(name = "SITEID")
    private String siteid;
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
    @JoinColumn(name = "KANTORSARID", referencedColumnName = "KANTORSARID")
    @ManyToOne(optional = false)
    private MstKantorSar kantorsarid;
}
