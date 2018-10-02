/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "res_potencycontact")
public @Data
class ResPotencyContact extends AbstractAuditingEntity implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Column(name = "potencycontactid", nullable = false)
//    private String potencyContactID;
//    @ManyToOne
//    @JoinColumn(name = "potencyid", nullable = false)
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size=25)
//    private ResPotency potency;
//    @Column(name = "contacttitle", length = 5)
//    private String contactTitle;
//    @Column(name = "contactname", length = 50)
//    private String contactName;
//    @Column(name = "contactsuffix", length = 10)
//    private String contactSuffix;
//    @Column(name = "contactposition", length = 50)
//    private String contactPosition;
//    @Column(name = "phonenumber1", length = 50)
//    private String phoneNumber1;
//    @Column(name = "phonenumber2", length = 50)
//    private String phoneNumber2;
//    @Column(name = "email", length = 255)
//    private String email;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
// 
    @Column(name = "POTENCYCONTACTID")
    private String potencycontactid;
    @Column(name = "CONTACTTITLE")
    private String contacttitle;
    @Column(name = "CONTACTNAME")
    private String contactname;
    @Column(name = "CONTACTSUFFIX")
    private String contactsuffix;
    @Column(name = "CONTACTPOSITION")
    private String contactposition;
    @Column(name = "PHONENUMBER1")
    private String phonenumber1;
    @Column(name = "PHONENUMBER2")
    private String phonenumber2;
    @Column(name = "EMAIL")
    private String email;
    @JoinColumn(name = "POTENCYID", referencedColumnName = "POTENCYID")
    @ManyToOne
    private ResPotency potencyid;
    @Column(name = "CONTACT_PERSON_A")
    private String contactPersonA;
    @Column(name = "CONTACT_PERSON_B")
    private String contactPersonB;
    @Column(name = "NO_HP_A")
    private String noHpA;
    @Column(name = "NO_HP_B")
    private String noHpB;

}
