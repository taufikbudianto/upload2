/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model.ws.cilent;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Taufik
 */

@Entity
@Table(name = "SS_KOMUNIKASI")
public @Data 
class SsSiagaKomunikasi extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "SS_KOMUNIKASI_SEQ_ID")
    @SequenceGenerator(sequenceName = "SS_KOMUNIKASI_SEQ",allocationSize = 1, name="SS_KOMUNIKASI_SEQ_ID")
    @Column(name = "KOMUNIKASIID")
    private Integer komunikasiID;
    @Column(name="NAMA")
    private String nama;
    @Column(name="KODESATKER")
    private String kodeSatker;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name="BARANG")
    private String barang;
    @Column(name="NAMAKELOMPOK")
    private String namaKelompok;
    @Column(name="JUMLAH")
    private Integer jumlah;
    @Column(name="LOKASI")
    private String lokasi;
    @Column(name="SERVICEABLECONDITION")
    private Integer serviceableCondition;
    @Column(name="UNSERVICEABLECONDITION")
    private Integer unserviceable_condition;
    @Lob
    @Column(name="KETERANGAN",length = 100000)
    private String keterangan;
    @Column(name="TGLENTRY")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tglentry;
    
}
