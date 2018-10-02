/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
@Table(name = "psa_app_facility")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"facilityId"})
@ToString(of = {"facilityId", "facilityType", "tenant", "facilityName"})
@NoArgsConstructor
public class Facility extends AbstractAuditingEntity implements Serializable {
    
    public static final String FACILITY_TOP_PARENT_NAME = "ROOT";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "facility_id")
    private Integer facilityId;
    
    @JoinColumn(name = "parent_id", referencedColumnName = "facility_id")
    @ManyToOne(optional = false)
    private Facility parent;
    
    @JoinColumn(name = "facility_type_id", referencedColumnName = "enum_id")
    @ManyToOne(optional = false)
    private Enumeration facilityType;
    
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    @ManyToOne(optional = false)
    private Tenant tenant;
    
    @Column(name = "facility_name", length = 50)
    private String facilityName;
    
    @Column(name="square_footage", precision = 10, scale = 4)
    private BigDecimal squareFootage;
    
    @Column(name="longitute", precision = 10, scale = 4)
    private BigDecimal longitute;
    
    @Column(name="latitude", precision = 10, scale = 4)
    private BigDecimal latitude;
    
    @Setter(value = AccessLevel.NONE)
    @Getter(value = AccessLevel.NONE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    private List<Facility> childFacilities;
    
    @Setter(value = AccessLevel.NONE)
    @Getter(value = AccessLevel.NONE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "facility")
    private List<FacilityContactMechanism> facilityContactMechanisms;

    public Facility(Enumeration facilityType, Tenant tenant, String facilityName) {
        this.facilityType = facilityType;
        this.tenant = tenant;
        this.facilityName = facilityName;
    }
    
    /**
     * Add a new child to this Facility object. As a note, after call this method, save method must be called 
     * from repository class to commit the new child to database.
     * @param child 
     * @throws java.lang.Exception when child already exists.
     */
    public void addChild(Facility child) throws Exception {
        if(childFacilities == null) {
            childFacilities = new ArrayList<>();
        } else if (childFacilities.contains(child)) {
            throw new Exception("Child is already exists.");
        }
        childFacilities.add(child);
    }
    
    /**
     * Remove a specific child from this Facility object. As a note, after call this method, save method must be 
     * called from repository class to remove the child from database.
     * @param child 
     * @throws java.lang.Exception when child is not exists.
     */
    public void removeChild(Facility child) throws Exception {
        if (childFacilities == null) {
            throw new Exception("Child is empty.");
        }
        
        boolean result = childFacilities.remove(child);
        
        if (!result) {
            throw new Exception("Child is not exists.");
        }
    }
    
    /**
     * Update a specific child's values from this Facility object. As a note, after call this method, save method 
     * must be called from repository class to update the child in database.
     * @param child 
     * @throws java.lang.Exception when child is not exists.
     */
    public void updateChild(Facility child) throws Exception {
        if (childFacilities == null) {
            throw new Exception("Child is empty.");
        }
        
        int index = childFacilities.indexOf(child);
        if (index != -1) {
            childFacilities.get(index).setFacilityName(child.getFacilityName());
            childFacilities.get(index).setSquareFootage(child.getSquareFootage());
            childFacilities.get(index).setLongitute(child.getLongitute());
            childFacilities.get(index).setLatitude(child.getLatitude());
        } else {
            throw new Exception("Child is not exists.");
        }
    }

    /**
     * Add a new FacilityContactMechanism to this Facility object. As a note, after call this method, save method must 
     * be called from repository class to commit the new FacilityContactMechanism to database.
     * @param contactMechanism
     * @param contactMechanismPurpose
     * @throws java.lang.Exception when contact_mechanism_purpose is mismatch or 
     * FacilityContactMechanism is already exists.
     */
    public void addFacilityContactMechanism(ContactMechanism contactMechanism, Enumeration contactMechanismPurpose) 
            throws Exception {
        if (!contactMechanismPurpose.getType().equals(Enumeration.EnumerationType.CONTACT_MECHANISM_PURPOSE)) {
            throw new Exception("Contact mechanism purpose is mismatch.");
        }
        
        FacilityContactMechanism facilityContactMechanism = new FacilityContactMechanism(
                this, contactMechanism, contactMechanismPurpose, new Date());
        
        if(facilityContactMechanisms == null) {
            facilityContactMechanisms = new ArrayList<>();
        } else if (facilityContactMechanisms.contains(facilityContactMechanism)) {
            throw new Exception("Facility Contact Mechanism already exists.");
        }
        
        facilityContactMechanisms.add(facilityContactMechanism);
    }
    
    /**
     * Remove a specific FacilityContactMechanism from this Facility object. As a note, after call this method, 
     * save method must be called from repository class to remove the FacilityContactMechanism from database. 
     * @param facilityContactMechanism
     * @throws java.lang.Exception when Facility Contact Mechanism is not exists.
     */
    public void removeFacilityContactMechanism(FacilityContactMechanism facilityContactMechanism) throws Exception {
        if (facilityContactMechanisms == null) {
            throw new Exception("Facility Contact Mechanism is empty.");
        }
        
        int index = facilityContactMechanisms.indexOf(facilityContactMechanism);
        if (index != -1) {
            facilityContactMechanisms.get(index).setToDate(new Date());
        } else {
            throw new Exception("Facility Contact Mechanism is not exists.");
        }
    }

    public List<Facility> getChilds() {
        return Collections.unmodifiableList(childFacilities != null ? 
                childFacilities : new ArrayList<>());
    }

    public List<FacilityContactMechanism> getContactMechanisms() {
        return Collections.unmodifiableList(facilityContactMechanisms != null ? 
                facilityContactMechanisms : new ArrayList<>());
    }
    
}
