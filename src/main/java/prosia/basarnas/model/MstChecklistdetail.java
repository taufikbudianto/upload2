/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
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
@Table(name = "MST_CHECKLISTDETAIL")

public @Data 
class MstChecklistdetail extends AbstractAuditingEntity implements Serializable{
    @Id
    @Basic(optional = false)
    @Column(name = "CHECKLISTDETAILID")
    private String checklistdetailid;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "CREATEDBY")
    private String createdby;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Column(name = "USERSITEID")
    private String usersiteid;
    @JoinColumn(name = "CHECKLISTID", referencedColumnName = "CHECKLISTID")
    @ManyToOne(optional = false)
    private MstChecklist checklistid;

   
}
