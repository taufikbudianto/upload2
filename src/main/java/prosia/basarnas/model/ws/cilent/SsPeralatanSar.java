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
@Table(name = "SS_PERALATANSAR")
public @Data 
class SsPeralatanSar extends AbstractAuditingEntity implements  Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "SS_PERALATANSAR_SEQ_ID")
    @SequenceGenerator(sequenceName = "SS_PERALATANSAR_SEQ",allocationSize = 1, name="SS_PERALATANSAR_SEQ_ID")
    @Column(name = "PERALATANSARID")
    private Integer peralatanSarID;
    @Column(name="NAMA")
    private String nama;
    @Column(name="KODESATKER")
    private String kodeSatker;
    @Column(name="SARANASAR")
    private String saranaSar;
    @Column(name="JUMLAH")
    private Integer jumlah;
    @Column(name="POSISI")
    private String posisi;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name="S")
    private Integer s;
    @Column(name="US")
    private Integer us;
    @Lob
    @Column(name="KETERANGAN",length = 100000)
    private String keterangan;
    @Column(name="TGLENTRY")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tglentry;
}
