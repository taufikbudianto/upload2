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
@Table(name = "SS_SIAGALAUT")
public @Data 
class SsSiagaLaut extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "SS_SIAGALAUT_SEQ_ID")
    @SequenceGenerator(sequenceName = "SS_SIAGALAUT_SEQ",allocationSize = 1, name="SS_SIAGALAUT_SEQ_ID")
    @Column(name = "SIAGALAUTID")
    private Integer siagaLautID;
    @Column(name="NAMA")
    private String nama;
    @Column(name="KODESATKER")
    private String kodeSatker;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name="BARANG")
    private String barang;
    @Column(name="SPEED")
    private String speed;
    @Column(name="RANGE")
    private String range;
    @Column(name="ENDURANCE")
    private String endurance;
    @Lob
    @Column(name="POSISI",length = 100000)
    private String posisi;
    @Column(name="JUMLAH")
    private Integer jumlah;
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
