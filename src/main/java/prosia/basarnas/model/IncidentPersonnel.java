/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "INC_PERSONNEL")
public @Data class IncidentPersonnel extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "incpersonnelid", nullable = false)
    private String incidentPersonnelID;
    @Column(name = "personnelcode", length = 50)
    private String personnelCode;
    @Column(name = "personnelname", length = 50)
    private String personnelName;
    @Column(name = "personneltype")
    private Integer personnelType;
    @Column(name = "title", length = 10)
    private String title;
    @Column(name = "suffix", length = 10)
    private String suffix;
    @Column(name = "birthplace", length = 50)
    private String birthPlace;
    @Column(name = "birthdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date birthDate;
    @Column(name = "gender", length = 1)
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
    private String lastEducation;
    @Column(name = "status")
    private Integer status;
    @Column(name = "photourl", length = 100)
    private String photoUrl;
    @Column(name = "homeaddress", length = 100)
    private String homeAddress;
    @Column(name = "phonenumber", length = 50)
    private String phoneNumber;
    @Column(name = "mobilephonenumber", length = 50)
    private String mobilePhoneNumber;
    @Column(name = "email", length = 50)
    private String email;
    @ManyToOne
    @JoinColumn(name = "kantorsarid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstKantorSar officeSar;
    @ManyToOne
    @JoinColumn(name = "possarid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstPosSar posSar;
    @ManyToOne
    @JoinColumn(name = "potencyid")
    
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPotency potency;
    @ManyToOne
    @JoinColumn(name = "employmentclassid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEMployeeClass employmentClass;
    @ManyToOne
    @JoinColumn(name = "employmentunitid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEmployeeUnit unit;
    @ManyToOne
    @JoinColumn(name = "structuralpositionid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEmployeePosition structuralPosition;
    @ManyToOne
    @JoinColumn(name = "functionalpositionid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEmployeePosition functionalPosition;
    @ManyToOne
    @JoinColumn(name = "standbypositionid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEmployeePosition standByPosition;
    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "personnelid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPersonnel personnel;
    @Column(name = "responsibility")
    private String responsibility;
    @Column(name = "assignmentdate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date assignmentDate;
    @Column(name = "assignmentenddate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date assignmentEndDate;
    @ManyToOne
    @JoinColumn(name = "operationalpositionid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private MstEmployeePosition operationalPosition;
    @Column(name = "position")
    private String position;
    @Column(name = "dateCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "createdBy", length = 50)
    private String createdBy;
    @Column(name = "lastmodified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "modifiedby", length = 50)
    private String modifiedBy;
    @Column(name = "usersiteid")
    private String userSiteID;
    @Column(name = "isdeleted")
    private boolean deleted;
}
