/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Aris
 */
@Entity
@Table(name="pro_radiogram")
public @Data class IncidentRadiogram extends AbstractAuditingEntity implements Serializable{
    @Id
    @Column(name="radiogramid",length=15)
    private String radiogramID;
    @Column(name="isKirim")
    private Boolean kirim=false;
    @Column(name="isnasionalformat")
    private Boolean nasionalformat=true;
    @Column(name="panggilan")
    private String panggilan;
    @Column(name="jenis")
    private String jenis;
    @Column(name="stn")
    private String stn;
    @Column(name="derajat")
    private String derajat;
    @Column(name="instruksi")
    private String instruksi;
    @Column(name="datepenunjukan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePenunjukan;
    @Column(name="datepenunjukantimezone")
    private TimeZone datePenunjukanTimeZone;
    @Column(name="datepembuatan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePembuatan;
    @Column(name="datepembuatantimezone")
    private TimeZone datePembuatanTimeZone;
    @Column(name="prefixnomor")
    private String prefixNomor;
    @Column(name="jumlahkata")
    private String jumlahKata;
    @Column(name="dari")
    private String dari;
    @Column(name="aksi")
    private String aksi;
    @Column(name="info")
    private String info;
    @Column(name="klasifikasi")
    private String klasifikasi;
    @Column(name="nomor")
    private String nomor;
    @Column(name="pengirim")
    private String pengirim;
    @ManyToOne
    @JoinColumn(name="nama")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPersonnel nama;
    @Column(name="pangkat")
    private String pangkat;
    @Column(name="jabatan")
    private String jabatan;
    @Column(name="datetwt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTwt;
    @Column(name="datetwttimezone")
    private TimeZone dateTwtTimeZone;
    @Column(name="datetwk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTwk;
    @Column(name="datetwktimezone")
    private TimeZone dateTwkTimeZone;
    @OneToOne
    @JoinColumn(name="referensi")
    private IncidentRadiogram referensi;
    @Column(name="referensinotes")
    private String referensiNotes;
    
    @Column(name="usersiteid",length=3)
    private String userSiteID;
    @Column(name="isdeleted")
    private Boolean deleted;
    @Column(name="datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name="createdby",length=50)
    private String createdBy;
    @Column(name="lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name="modifiedby",length=50)
    private String modifiedBy;
    
    @Column(name="sistem", length=50)
    private String sistem;
    @Column(name="paraf", length=50)
    private String paraf;
}
