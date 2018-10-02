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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Owner
 */

@Entity
@Table(name="RES_PERSONNEL_IMAGES")
public @Data
class ResPersonnelImages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RES_PERSONNEL_IMAGES_SEQ_ID")
    @SequenceGenerator(sequenceName = "RES_PERSONNEL_IMAGES_SEQ", allocationSize = 1, name = "RES_PERSONNEL_IMAGES_SEQ_ID")
    @Column(name = "IMAGEID", length = 15)    
    private Integer imageID;
    
    @JoinColumn(name = "PERSONNELID", referencedColumnName = "PERSONNELID")
    @ManyToOne
    private ResPersonnel personnelID;
    
    @Column(name = "PATHNAME", length = 200)
    private String pathname;
    
    @Column(name="ISDELETED")
    private boolean deleted;
}
