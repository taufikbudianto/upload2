/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_job_pos_structure")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"jobPositionStructurePK"})
@ToString(exclude = {"managedPosition", "reportToPosition"})
@NoArgsConstructor
public class JobPositionStructure implements Serializable {
    
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobPositionStructurePK jobPositionStructurePK;
    
    @Column(name = "primary_")
    private Boolean primary;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    @JoinColumn(name = "managed_pos_id", referencedColumnName = "pos_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private JobPosition managedPosition;
    
    @JoinColumn(name = "report_to_pos_id", referencedColumnName = "pos_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private JobPosition reportToPosition;

    public JobPositionStructure(JobPosition managedPosition, JobPosition reportToPosition, 
            Date fromDate, Date toDate, Boolean primary) {
        this.jobPositionStructurePK = new JobPositionStructurePK(
                managedPosition.getPositionId(), reportToPosition.getPositionId(), fromDate);
        this.toDate = toDate;
        this.primary = primary;
        this.managedPosition = managedPosition;
        this.reportToPosition = reportToPosition;
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
class JobPositionStructurePK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "managed_pos_id")
    private int managedPosId;
    
    @Basic(optional = false)
    @Column(name = "report_to_pos_id")
    private int reportToPosId;
    
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    
}
