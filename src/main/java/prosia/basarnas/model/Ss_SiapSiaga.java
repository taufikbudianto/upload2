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
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;


@Entity
@Table (name = "SS_SIAPSIAGA")
public @Data

class Ss_SiapSiaga extends AbstractAuditingEntity implements Serializable {
 
    @Id
    @Basic (optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "SS_SIAPSIAGA_SEQ_ID")
    @SequenceGenerator(sequenceName = "SS_SIAPSIAGA_SEQ",allocationSize = 1, name="SS_SIAPSIAGA_SEQ_ID")
    @Column(name = "SIAPSIAGAID")
    private Integer siagaUdaraID;
    @Column (name = "ISDELETED")
    private boolean deleted;
    @Column (name = "KODE")
    private String kode;
    @Column (name = "NAMA")
    private String nama;
    //@Lob
    @Column (name = "DETAIL", length = 1000)
    private String detail;
    @Column (name = "PENEMPATAN")
    private String penempatan;
    @Column (name = "TGLENTRY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglEntry;
}
