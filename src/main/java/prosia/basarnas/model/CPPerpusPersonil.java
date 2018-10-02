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
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Irfan Rofie
 */
@Entity
@Table(name = "CP_PERPUS_PERSONIL")
public @Data
class CPPerpusPersonil extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LIB_PERSONIL_ID")
    @SequenceGenerator(sequenceName = "SEQ_LIB_PERSONIL", allocationSize = 1, name = "SEQ_LIB_PERSONIL_ID")
    @Column(name = "LIB_PERSONIL_ID")
    private Integer libPersonilId;

    @JoinColumn(name = "LIB_ID", referencedColumnName = "LIB_ID")
    @ManyToOne(optional = true)
    private CPPerpustakaan perpustakaan;
    
    @JoinColumn(name = "PERSONEL_ID", referencedColumnName = "PERSONNELID")
    @ManyToOne(optional = true)
    private ResPersonnel personil;
}
