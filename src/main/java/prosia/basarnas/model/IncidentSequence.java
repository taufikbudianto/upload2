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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "INCIDENT_SEQUENCE")
public @Data
class IncidentSequence extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "INCIDENTSEQUENCEID")
    private String incidentSequenceId;
    @Basic(optional = false)
    @Column(name = "KANTORSARID")
    private String kantorSarId;
    @Basic(optional = false)
    @Column(name = "BULAN")
    private String bulan;
    @Basic(optional = false)
    @Column(name = "TAHUN")
    private String tahun;
    @Basic(optional = false)
    @Column(name = "SEQUENCE")
    private String sequence;
}
