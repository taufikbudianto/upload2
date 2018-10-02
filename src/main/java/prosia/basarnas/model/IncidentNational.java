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
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Irfan Rofie
 */
@Entity
@Table(name = "INC_NATIONAL")
public @Data
class IncidentNational  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INC_NATIONAL_ID")
    @SequenceGenerator(sequenceName = "SEQ_INC_NATIONAL", allocationSize = 1, name = "SEQ_INC_NATIONAL_ID")
    @Column(name = "INC_NATIONAL_ID", length = 15)
    private Integer incNationalId;

    @JoinColumn(name = "INCIDENT_ID", referencedColumnName = "INCIDENTID")
    @ManyToOne(optional = true)
    private Incident incident;

    @Column(name = "KANTOR_SAR_ID", length = 3)
    private String kantorSarId;
}
