/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
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
@Table(name = "res_personnel")
public @Data
class ResPersonnel extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "personnelid", nullable = false)
    private String personnelID;
    @Column(name = "personnelcode", length = 50)
    @Size(max = 50)
    private String personnelCode;
    @Column(name = "personnelname", length = 180)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String personnelName;
    @Column(name = "personneltype")
    private Integer personnelType;
    @Column(name = "title", length = 20)
    private String title;
    @Column(name = "suffix", length = 30)
    @Size(max = 30, message = "Maksimal 30 karakter")
    private String suffix;
    @Column(name = "birthplace", length = 50)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String birthPlace;
    @Column(name = "birthdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date birthDate;
    @Column(name = "gender", length = 5)
    private String gender;
    @Column(name = "religion", length = 50)
    private String religion;
    @Column(name = "maritalstatus")
    private Integer maritalStatus;
    @Column(name = "employmenttype")
    private Integer employmentType;
    @Column(name = "tmtdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tmtDate;
    @Column(name = "lasteducation", length = 50)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String lastEducation;
    @Column(name = "status")
    private Integer status;
    @Column(name = "photourl", length = 100)
    @Size(max = 100)
    private String photoUrl;
    @Column(name = "homeaddress", length = 150)//DEFAULT 100
    @Size(max = 1000, message = "Maksimal 1000 karakter")
    private String homeAddress;
    @Column(name = "phonenumber", length = 50)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String phoneNumber;
    @Column(name = "mobilephonenumber", length = 50)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String mobilePhoneNumber;
    @Column(name = "email", length = 50)
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String email;
    @Column(name = "img")
    private byte[] img;

    @ManyToOne
    @JoinColumn(name = "kantorsarid", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstKantorSar officeSar;

    @ManyToOne
    @JoinColumn(name = "possarid")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstPosSar posSar;

    @ManyToOne
    @JoinColumn(name = "potencyid", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private ResPotency potency;

    @ManyToOne
    @JoinColumn(name = "employmentclassid")

    @BatchSize(size = 25)
    private MstEMployeeClass employmentClass;

    @ManyToOne
    @JoinColumn(name = "employmentunitid")
    @BatchSize(size = 25)
    private MstEmployeeUnit unit;

    @ManyToOne
    @JoinColumn(name = "structuralpositionid")
    @BatchSize(size = 25)
    private MstEmployeePosition structuralPosition;

    @ManyToOne
    @JoinColumn(name = "functionalpositionid")
    @BatchSize(size = 25)
    private MstEmployeePosition functionalPosition;

    @ManyToOne
    @JoinColumn(name = "standbypositionid")
    @BatchSize(size = 20)
    private MstEmployeePosition standByPosition;

    @Column(name = "REMARKS")
    @Size(max = 1000, message = "Maksimal 1000 karakter")
    private String remarks;
    @Column(name = "PASSPORTNUMBER")
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String passportnumber;
    @Column(name = "TOEFLSCORE")
    @Size(max = 50, message = "Maksimal 50 karakter")
    private String toeflscore;
    @Column(name = "PERSONHEIGHT")
    @Size(max = 10, message = "Maksimal 10 karakter")
    private String personheight;
    @Column(name = "PERSONWEIGHT")
    @Size(max = 10, message = "Maksimal 10 karakter")
    private String personweight;
    @Column(name = "BLOODTYPE")
    private String bloodtype;
    @Column(name = "MEDICALHISTORY")
    @Size(max = 1000, message = "Maksimal 1000 karakter")
    private String medicalhistory;
    @Column(name = "ISDELETED")
    private BigInteger isdeleted;
    @Column(name = "dateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "USERSITEID")
    private String usersiteid;
    /*
    @Column(name = "dateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Column(name = "createdBy", length = 50)
    private String createdby;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "MODIFIEDBY")
    private String modifiedby;
    @Basic(optional = false)
    

    @OneToMany(targetEntity = ResPersonnelTraining.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=20)
    @JoinColumn(name = "personnelid")
    private Set<ResPersonnelTraining> personnelTrainings;
    
    @OneToMany(targetEntity = ResPersonnelHistory.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=20)
    @JoinColumn(name = "personnelid")
    private Set<ResPersonnelHistory> personnelHistories;
    





     */
    @Column(name = "C_SATKER")
    private Integer satker;
    @Column(name = "ID_PERSONIL")
    private String idPersonil;
    @Column(name = "ID_INSTANSI")
    private String idInstansi;
    @Column(name = "I_KTP")
    private String iKtp;
    @Column(name = "C_PROPINSI")
    private Integer idProvinsi;
    @Column(name = "C_KOTA")
    private Integer idKota;
    @Column(name = "Q_KODEPOS")
    private Integer kodePos;
    @Column(name = "I_ENTRY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tglEntry;
    @Column(name = "D_MULAI_MASUK")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mulaiMasuk;
    @Column(name = "D_VALID")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validDate;
    @Column(name = "C_KUALIFIKASI")
    private Integer kualifikasi;
    @Column(name = "D_ENTRY")
    private String entryBy;
    @Column(name = "C_STAT_PERS")
    private Integer statPers;
}
