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
@Table(name = "MST_EMPLOYMENTCLASS")
public @Data
class MstEMployeeClass extends AbstractAuditingEntity implements Serializable{
    @Id
    @Basic(optional = false)
    @Column(name = "EMPLOYMENTCLASSID")
    private String employmentclassid;
    @Column(name = "EMPLOYMENTCLASSCODE")
    private String employmentclasscode;
    @Column(name = "EMPLOYMENTCLASSNAME")
    private String employmentclassname;
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
    @Basic(optional = false)
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
}
