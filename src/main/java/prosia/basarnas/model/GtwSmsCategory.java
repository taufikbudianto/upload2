/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "gtw_smscategory")
public @Data
class GtwSmsCategory extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "smscategoryid", nullable = false)
    private String smsCategoryID;
    @Column(name = "categoryname", length = 50)
    private String categoryName;
    @Column(name = "categorycolor", length = 50)
    private String categoryColor;
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
    @Column(name = "usersiteid", length = 3)
    private String userSiteID;
    @Column(name = "isdeleted")
    private boolean deleted;
}
