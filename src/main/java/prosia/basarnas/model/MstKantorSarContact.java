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
 * @author Taufik
 */
@Entity
@Table(name = "MST_KANTORSARCONTACT")
public @Data
class MstKantorSarContact extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "KANTORSARCONTACTID")
    private String kantorsarcontactid;
    @JoinColumn(name = "KANTORSARID", referencedColumnName = "KANTORSARID")
    @ManyToOne
    private MstKantorSar kantorsarid;
    @Column(name = "CONTACTTITLE")
    private String contactTitle;
    @Column(name = "CONTACTNAME")
    private String name;
    @Column(name = "CONTACTSUFFIX")
    private String suffix;
    @Column(name = "CONTACTPOSITION")
    private String position;
    @Column(name = "PHONENUMBER1")
    private String phonenumber1;
    @Column(name = "PHONENUMBER2")
    private String phonenumber2;
    @Column(name = "EMAIL")
    private String email;
}
