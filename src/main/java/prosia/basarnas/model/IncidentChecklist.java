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
@Table(name = "inc_checklist")
public @Data
class IncidentChecklist extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "incidentchecklistid", length = 15)
    private String incidentChecklistID;
    @Column(name = "checklistname", length = 200)
    private String checklistName;
    @Column(name = "category", length = 50)
    private String category;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status", length = 10)
    private String status;
    @ManyToOne
    @JoinColumn(name = "personnelid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private IncidentPersonnel personnel;
    @ManyToOne
    @JoinColumn(name = "incidentid")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
}
