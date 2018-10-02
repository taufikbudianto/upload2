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
@Table(name = "CP_KALENDER_PERSONIL")
public @Data
class CPKalenderPersonil extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CAL_PERSONIL_ID")
    @SequenceGenerator(sequenceName = "SEQ_CAL_PERSONIL", allocationSize = 1, name = "SEQ_CAL_PERSONIL_ID")
    @Column(name = "CAL_PERSONIL_ID")
    private Integer calPersonilId;

    @Column(name = "PERSONIL_CODE", length = 25)
    private String personilCode;

    @Column(name = "PERSONIL_NAME", length = 50)
    private String personilName;

    @Column(name = "PERSONIL_TIPE")
    private Integer personilTipe;

    @JoinColumn(name = "CAL_ID", referencedColumnName = "CAL_ID")
    @ManyToOne(optional = true)
    private CPKalender kalender;

    @JoinColumn(name = "EMPLOYMENT_CLASS_ID", referencedColumnName = "EMPLOYMENTCLASSID")
    @ManyToOne(optional = true)
    private MstEMployeeClass employeeClass;
}
