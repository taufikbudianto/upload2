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
import javax.persistence.OneToMany;
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
@Table(name = "MST_KANTORSAR")
public @Data
class MstKantorSar extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "KANTORSARID")
    private String kantorsarid;
    @Basic(optional = false)
    @Column(name = "KANTORSARNAME")
    private String kantorsarname;
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
    @Column(name = "KANTORSARNAME_SIMPEG")
    private String kantorsarnameSimpeg;
    @Column(name = "C_SATKER")
    private String satker;
//    @OneToMany(mappedBy = "kantorsarid")
//    private List<UtiBeaconComposite> utiBeaconcompositeList;   
}
