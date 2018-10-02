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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;
import prosia.basarnas.enumeration.SkillCategory;

/**
 *
 * @author PROSIA
 */

@Entity
@Table(name = "MST_SKILL")

public @Data
class MstSkill extends AbstractAuditingEntity implements Serializable{
    @Id
    @Basic(optional = false)
    @Column(name = "SKILLID")
    private String skillid;
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
    @Column(name = "SKILLNAME")
    private String skillname;
    @Column(name = "CATEGORY")
    private Integer category;
    
    @Column(name = "KATEGORI")
    @Enumerated (EnumType.ORDINAL)
    private SkillCategory kategori;
}
