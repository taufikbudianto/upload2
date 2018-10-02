/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */

@Entity
@Table(name = "gtw_group")
public @Data class GtwGroup extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "groupid", nullable = false, length = 15)
    private String groupID;
    @Column(name = "groupname", length = 50)
    private String groupName;
    @Column(name = "usersiteid", length = 3)
    private String userSiteID;
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "createdby", length = 50)
    private String createdBy;
    @Column(name = "lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "modifiedby", length = 50)
    private String modifiedBy;
    @Column(name = "isdeleted")
    private boolean deleted;
//    @OneToMany(targetEntity = GtwGroupDetail.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn(name = "groupid")
//    private Set<GtwGroupDetail> gtwGroupDetails;
    
}
