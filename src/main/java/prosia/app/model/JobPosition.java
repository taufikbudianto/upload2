/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "psa_app_job_position")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"positionId"})
@ToString
@NoArgsConstructor
public class JobPosition extends AbstractAuditingEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "pos_id")
    private Integer positionId;
    
    @JoinColumn(name = "job_pos_type_id", referencedColumnName = "enum_id")
    @ManyToOne
    private Enumeration positionType;
    
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    @ManyToOne
    private Tenant tenant;
    
    @Column(name = "pos_name", length = 50)
    private String positionName;
    
    @Column(name = "enabled")
    private Boolean enabled;

    public JobPosition(Enumeration positionType, Tenant tenant, String positionName, Boolean enabled) {
        this.positionType = positionType;
        this.tenant = tenant;
        this.positionName = positionName;
        this.enabled = enabled;
    }
    
}
