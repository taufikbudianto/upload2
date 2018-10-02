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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Irfan Rofie
 */
@Entity
@Table(name = "CP_KALENDER")
public @Data
class CPKalender extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KALENDER_ID")
    @SequenceGenerator(sequenceName = "SEQ_KALENDER", allocationSize = 1, name = "SEQ_KALENDER_ID")
    @Column(name = "CAL_ID")
    private Integer calId;

    @Column(name = "CAL_JUDUL", length = 50)
    private String calJudul;

    @Column(name = "CAL_LOKASI", length = 50)
    private String calLokasi;

    @Column(name = "CAL_SEHARIAN")
    private Boolean calSeharian;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAL_MULAI")
    private Date calMulai;

    @Column(name = "CAL_MULAI_TIMEZONE")
    private String calMulaiTimezone;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAL_SAMPAI")
    private Date calSampai;

    @Column(name = "CAL_SAMPAI_TIMEZONE")
    private String calSampaiTimezone;

    @Column(name = "CAL_PERULANGAN")
    private Integer calPerulangan;

    @Column(name = "CAL_PENGINGAT")
    private Integer calPengingat;

    @Column(name = "DELETED")
    private Boolean deleted;

    @JoinColumn(name = "KANTOR_SAR_ID", referencedColumnName = "KANTORSARID")
    @ManyToOne(optional = true)
    private MstKantorSar kantorSar;
}
