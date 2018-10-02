/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "MST_CHECKLIST")
//@Subselect("select * from MST_CHECKLIST")

public @Data
class MstChecklist extends AbstractAuditingEntity implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "CHECKLISTID")
    private String checklistid;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "NAME")
    private String name;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "USERSITEID")
    private String usersiteid;
    @Basic(optional = false)
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
}
