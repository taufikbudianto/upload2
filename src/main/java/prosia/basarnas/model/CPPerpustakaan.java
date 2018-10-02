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
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Irfan Rofie
 */
@Entity
@Table(name = "CP_PERPUSTAKAAN")
public @Data
class CPPerpustakaan extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LIB_ID")
    @SequenceGenerator(sequenceName = "SEQ_LIB", allocationSize = 1, name = "SEQ_LIB_ID")
    @Column(name = "LIB_ID")
    private Integer libId;

    @Column(name = "LIB_JUDUL", length = 25)
    private String libJudul;

    @Lob
    @Column(name = "NOTIF_KONTEN", length = 2500)
    private String libKonten;

    @Column(name = "LIB_ATTACHMENT", length = 50)
    private String libAttachment;
    
    @Column(name = "DELETED")
    private Boolean deleted;
}
