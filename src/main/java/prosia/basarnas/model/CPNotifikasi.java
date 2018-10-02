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
@Table(name = "CP_NOTIFIKASI")
public @Data
class CPNotifikasi extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTIFIKASI_ID")
    @SequenceGenerator(sequenceName = "SEQ_NOTIFIKASI", allocationSize = 1, name = "SEQ_NOTIFIKASI_ID")
    @Column(name = "NOTIF_ID")
    private Integer notifId;

    @Column(name = "NOTIF_JUDUL", length = 25)
    private String notifJudul;

    @Column(name = "NOTIF_TIPE")
    private Integer notifTipe;

    @Lob
    @Column(name = "NOTIF_KONTEN", length = 250)
    private String notifKonten;

    @Column(name = "NOTIF_ATTACHMENT", length = 250)
    private String notifAttachment;

    @Column(name = "TANGGAPI")
    private Boolean tanggapi;
}
