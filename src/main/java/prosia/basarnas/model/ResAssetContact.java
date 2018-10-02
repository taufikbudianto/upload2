/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
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
@Table(name = "res_assetcontact")
public @Data class ResAssetContact extends AbstractAuditingEntity implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Column(name = "assetcontactid", nullable = false)
//    private String assetContactID;
//    @ManyToOne
//    @JoinColumn(name = "assetid", nullable = false)
//    //@fetch
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size=25)
//    private ResAsset asset;
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
    @Column(name = "ASSETCONTACTID")
    private String assetcontactid;
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
    @JoinColumn(name = "ASSETID", referencedColumnName = "ASSETID")
    @ManyToOne
    private ResAsset assetid;
}
