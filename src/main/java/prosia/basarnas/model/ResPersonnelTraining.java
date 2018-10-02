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
import javax.persistence.TemporalType;
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
@Table(name = "res_personneltraining")
public @Data
class ResPersonnelTraining extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "personneltrainingid", nullable = false)
    private String personnelTrainingID;
    @ManyToOne
    @JoinColumn(name = "personnelid", nullable = false)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private ResPersonnel personnel;
    @Column(name = "certificatenumber", length = 50)
    private String certificateNumber;
    @Column(name = "trainingdate", length = 50)
    private String trainingDate;
    @Column(name = "organizedby", length = 58)
    private String organizedBy;
    @Column(name = "traininglocation", length = 50)
    private String trainingLocation;
    @ManyToOne
    @JoinColumn(name = "skillid")//nullable = true)//, ) //default false
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstSkill skill;

    @Column(name = "D_DIKLAT_SELESAI")
    private String diklatEnd;
    @Column(name = "N_DIKLAT")
    private String namaDiklat;
    @Column(name = "N_PELATIHAN")
    private String namaPelatihan;

}
