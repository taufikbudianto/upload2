/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "res_personnelhistory")
public @Data class ResPersonnelHistory extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "personnelhistoryid", nullable = false)
    private String personnelHistoryID;
    @ManyToOne
    @JoinColumn(name = "personnelid", nullable = false)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private ResPersonnel personnel;
    @Column(name = "incidentyear", length = 20)
    private String incidentYear;
    @Column(name = "incidentname", length = 100)
    private String incidentName;
    @Column(name = "incidentposition", length = 50)
    private String incidentPosition;
    @Column(name = "incidentdesc", length = 250)
    private String incidentDesc;
    @ManyToOne
    @JoinColumn(name = "incidentid", nullable = true)
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size=25)
    private Incident incident;
}
