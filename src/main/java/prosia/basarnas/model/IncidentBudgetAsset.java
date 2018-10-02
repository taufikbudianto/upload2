/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Data
@Table(name = "INC_BUDGET_ASSET")
public class IncidentBudgetAsset extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "BUDGETASSETID", length = 15)
    private String incBudgetAssetId;

    @JoinColumn(name = "INCASSETID", referencedColumnName = "INCASSETID")
    @ManyToOne
    private IncidentAsset incidentAsset;

    @JoinColumn(name = "BBMID", referencedColumnName = "BBMID")
    @ManyToOne
    private MstBbm bbm;

    @Column(name = "BUDGET_ASSET_DESKRIPSI")
    private String budgetAssetDescr;

    @Column(name = "BUDGET_ASSET_DURASI")
    private Double budgetAssetDur;

    @Column(name = "BBB_ASSET_PEMAKAIAN")
    private Double bbbAssetPemakaian;

    @Column(name = "ISDELETED")
    private Boolean isDeleted;

    @Transient
    private Double total;
    @Transient
    private String incidentId;

}
