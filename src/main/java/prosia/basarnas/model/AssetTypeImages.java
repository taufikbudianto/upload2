package prosia.basarnas.model;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import prosia.basarnas.model.MstAssetType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author TOMY
 */
@Entity
@Table(name = "MST_ASSETTYPE_IMAGES")

public @Data
class AssetTypeImages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "MST_ASSETTYPE_IMAGES_SEQ_ID")
    @SequenceGenerator(sequenceName = "MST_ASSETTYPE_IMAGES_SEQ",allocationSize = 1, name="MST_ASSETTYPE_IMAGES_SEQ_ID")
    @Column(name = "IMAGEID", length = 15)
    private Integer imageID;
    
    @JoinColumn(name = "ASSETTYPEID", referencedColumnName = "ASSETTYPEID")
    @ManyToOne
    private MstAssetType assettypeid;
    
    //private String personelId;
    @Column(name = "PATHNAME", length = 200)
    private String pathname;
    
    
    @Column(name="ISDELETED")
    private boolean deleted;
    
}
