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
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import oracle.sql.BLOB;
import prosia.app.model.AbstractAuditingEntity;
import prosia.basarnas.enumeration.AssetKategori;
import prosia.basarnas.enumeration.AssetMatra;

/**
 *
 * @author PROSIA
 */

@Entity
@Table(name = "MST_ASSETTYPE")

public @Data
class MstAssetType extends AbstractAuditingEntity implements Serializable{
    @Id
    @Basic(optional = false)
    @Column(name = "ASSETTYPEID")
    private String assettypeid;
    @Column(name = "ISSRU")
    private Integer issru;
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
    @Column(name = "GISSYMBOL")
    private String gissymbol;
    @Column(name = "ASSETNAME")
    private String assetname;
    @Column(name = "MATRA")
    private Integer matra;
    @Column(name = "KATEGORI")
    @Enumerated (EnumType.ORDINAL)
    private AssetKategori kategori;
    @Column(name = "MATRA1")
    @Enumerated (EnumType.ORDINAL)
    private AssetMatra matra1;
    @Column(name = "GISSYMBOL2")
    private byte[] gissymbol2;
}
