/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_sequence")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"sequenceId"})
@ToString(exclude = {"sequenceInstances"})
@NoArgsConstructor
public class Sequence extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "seq_id")
    private Integer sequenceId;
    
    @Column(name = "format", length = 100)
    private String format;
    
    @Column(name = "name_", length = 200)
    private String name;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    private Tenant tenant;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sequence")
    @OrderBy(value = "seq_instance_id")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<SequenceInstance> sequenceInstances;

    /**
     * Insert a new SequenceInstance to this Sequence object. As a note, after call this method, save method 
     * must be called from repository class to commit the new SequenceInstance to database. 
     * @param prefixSuffix
     * @param nextNumber
     * @throws java.lang.Exception when SequenceInstance is already exists.
     */
    public void addInstance(String prefixSuffix, Integer nextNumber) throws Exception {
        SequenceInstance sequenceInstance = new SequenceInstance(this, prefixSuffix, nextNumber);
        
        if (this.sequenceInstances == null) {
            this.sequenceInstances = new ArrayList<>();
        } else if (sequenceInstance.getSequenceInstanceId() != null 
                && this.sequenceInstances.contains(sequenceInstance)) {
            throw new Exception("Sequence Instance is already exists.");
        }
        
        this.sequenceInstances.add(sequenceInstance);
    }
    
    /**
     * Remove a specific SequenceInstance that has been mapped to this Sequence object. As a note, after call 
     * this method, save method must be called from repository class to remove the mapping SequenceInstance 
     * from database. 
     * @param sequenceInstanceId
     * @throws java.lang.Exception when SequenceInstance is not exists.
     */
    public void removeInstance(int sequenceInstanceId) throws Exception {
        if (this.sequenceInstances == null) {
            throw new Exception("Sequence Instance is empty.");
        }
        
        SequenceInstance sequenceInstace = new SequenceInstance(sequenceInstanceId);
        
        boolean result = this.sequenceInstances.remove(sequenceInstace);
        
        if (!result) {
            throw new Exception("Sequence Instance is not exists.");
        }
    }
    
    public SequenceInstance getInstanceByPrefixSuffix(String prefixSuffix) {
        if (this.sequenceInstances != null) {
            for (SequenceInstance instance : this.sequenceInstances) {
                if (instance.getPrefixSuffix().equals(prefixSuffix)) {
                    return instance;
                }
            }
        }
        return null;
    }
    
    /**
     * Remove SequenceInstance at the specific index. As a note, after call this method, save method must be 
     * called from repository class to remove the mapping SequenceInstance from database. 
     * @param index
     * @throws java.lang.Exception when SequenceInstance is not exists.
     */
    public void removeInstanceAt(int index) throws Exception {
        if (this.sequenceInstances == null) {
            throw new Exception("Sequence Instance is empty.");
        } else if (index != -1 && index < this.sequenceInstances.size()) {
            this.sequenceInstances.remove(index);
        } else {
            throw new Exception("Sequence Instance is not exists.");
        }
    }

    public List<SequenceInstance> getSequenceInstances() {
        return Collections.unmodifiableList(this.sequenceInstances != null 
                ? this.sequenceInstances : new ArrayList<>());
    }
    
}
