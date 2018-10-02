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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "psa_app_job_pos_fullfillment")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"fullfillId"})
@ToString
@NoArgsConstructor
public class JobPositionFullfillment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "fullfill_id")
    private Integer fullfillId;
    
    @JoinColumn(name = "position_id", referencedColumnName = "pos_id")
    @ManyToOne
    private JobPosition position;
    
    @JoinColumn(name = "emp_id", referencedColumnName = "party_id")
    @ManyToOne
    private Party employee;
    
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    public JobPositionFullfillment(JobPosition position, Party employee, Date fromDate, Date toDate) {
        this.position = position;
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    
}
