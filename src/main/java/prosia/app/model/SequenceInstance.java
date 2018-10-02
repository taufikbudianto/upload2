/*
 * To change this template, choose Tools | Templates
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
@Table(name = "psa_app_sequence_instance")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"sequenceInstanceId"})
@ToString
@NoArgsConstructor
public class SequenceInstance extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "seq_instance_id")
    private Integer sequenceInstanceId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "seq_id")
    private Sequence sequence;
    
    @Column(name = "prefix_suffix", length = 100)
    private String prefixSuffix;
    
    @Column(name = "next_number")
    private Integer nextNumber;

    public SequenceInstance(Integer sequenceInstanceId) {
        this.sequenceInstanceId = sequenceInstanceId;
    }
    
    public SequenceInstance(Sequence sequence, String prefixSuffix, Integer nextNumber) {
        this.sequence = sequence;
        this.prefixSuffix = prefixSuffix;
        this.nextNumber = nextNumber;
    }
    
}
