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
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Taufik AB
 */
@Entity
@Table(name = "INC_BUDGET_PERSONAL")
@Data
public class IncidentBudgetPersonil extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "BUDGETPERSONALID")
    private String budgetPersonalId;
    @Column(name = "DESKRIPSI")
    private String deskripsi;
    @Column(name = "KANSAR")
    private String kansar;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name = "TGL_MULAI")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tglMulai;
    @Column(name = "TGL_AKHIR")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tglAkhir;

    @ManyToOne
    @JoinColumn(name = "INCPERSONNELID")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private IncidentPersonnel incPersonelId;

    @ManyToOne
    @JoinColumn(name = "BIAYAPERSONNELID")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstBiayaPesonnel mstBiayaPersonnel;

    @Transient
    private String incidentId;
    @Transient
    private Double total;
}
