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
@Table(name = "SS_PERSONEL")
public @Data
class SsPersonel extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "SS_PERSONEL_SEQ_ID")
    @SequenceGenerator(sequenceName = "SS_PERSONEL_SEQ",allocationSize = 1, name="SS_PERSONEL_SEQ_ID")
    @Column(name = "SSPERSONELID")
    private Integer ssPersonelID;
    @Column(name = "KODESATKER")
    private String kodeSatker;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name = "NAMA")
    private String nama;
    @Lob
    @Column(name="PENEMPATAN",length = 100000)
    private String penempatan;
    @Column(name="PIKET")
    private Integer  piket;
    @Column(name="ONCALL")
    private Integer  oncall;
    @Column(name="DINAS")
    private Integer  dinas;
    @Column(name="IJIN")
    private Integer  ijin;
    @Column(name="SAKIT")
    private Integer  sakit;
    @Column(name="CUTI")
    private Integer  cuti;
    @Column(name="TGLENTRY")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tglentry;
    @Lob
    @Column(name="PENEMPATANDECODE",length = 100000)
    private String penempatanDecode;
    
}
