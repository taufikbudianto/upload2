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
@Table(name = "MST_POSSARCONTACT")
@Data
public class MstPosSarContact extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "POSSARCONTACTID")
    private String possarcontactid;
    @JoinColumn(name = "POSSARID", referencedColumnName = "POSSARID")
    @ManyToOne
    private MstPosSar possarid;
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

}
