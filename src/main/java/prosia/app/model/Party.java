/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_party")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyId"})
@ToString(exclude = {"partyRelationships", "partyRoles", "partyContactMechanisms"})
public class Party extends AbstractAuditingEntity implements Serializable {
        
    public static final String ORGANIZATION_PARTY_RELATIONSHIP = "ORGANIZATION_ROLLUP";
    public static final String ORGANIZATION_TOP_PARENT_NAME = "INTERNAL_ORGANIZATION";
    public static final String ORGANIZATION_TYPE_KEY = "INTERNAL_";
    
    public static final String PARTY_ROLE_CONTACT = "CONTACT";
    public static final String PARTY_ROLE_CUSTOMER = "CUSTOMER";
    public static final String PARTY_ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String PARTY_ROLE_MEMBER = "MEMBER";
    public static final String PARTY_ROLE_SUPPLIER = "SUPPLIER";
    public static final String PARTY_ROLE_DEPARTMENT = "INTERNAL_DEPARTMENT";
    
    public enum PartyType {
        PERSON,
        ORGANIZATION,
        INTERNAL_ORGANIZATION
    }
    
    public enum Gender {
        MALE,
        FEMALE,
        UNSPECIFIED
    }
    
    public enum MaritalStatus {
        SINGLE,
        MARRIED,
        WIDOW,
        WIDOWER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTY_SEQ_ID")
    @SequenceGenerator(sequenceName = "PARTY_SEQ", allocationSize = 1, name = "PARTY_SEQ_ID")
    @Basic(optional = false)
    @Column(name = "party_id")
    private Integer partyId;
    
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    @ManyToOne(optional = false)
    private Tenant tenant;
    
    @Column(name = "party_type", length = 30)
    @Enumerated(EnumType.STRING)
    private PartyType partyType;
    
    @JoinColumn(name = "nationality_id", referencedColumnName = "geo_id")
    @ManyToOne
    private GeoBoundary nationality;
    
    @Column(name = "person_title", length = 20)
    private String personTitle;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "middle_name", length = 30)
    private String middleName;
    
    @Column(name = "taxation_id", length = 30)
    private String taxationId;
    
    @Column(name = "gender", length = 15)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "pob", length = 100)
    private String pob;
    
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    
    @Column(name = "marital_status", length = 10)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    
    @Column(name = "mother_maiden_name", length = 50)
    private String motherMaidenName;
    
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "leftParty")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<PartyRelationship> partyRelationships;
    
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "party")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<PartyRole> partyRoles;
    
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "party")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<PartyContactMechanism> partyContactMechanisms;

    public Party() {
        this.partyRelationships = new ArrayList<>();
        this.partyRoles = new ArrayList<>();
        this.partyContactMechanisms = new ArrayList<>();
    }

    public Party(Tenant tenant, PartyType partyType) {
        this.tenant = tenant;
        this.partyType = partyType;
        
        this.partyRelationships = new ArrayList<>();
        this.partyRoles = new ArrayList<>();
        this.partyContactMechanisms = new ArrayList<>();
    }

    public Party(Tenant tenant, PartyType partyType, String firstName) {
        this.tenant = tenant;
        this.partyType = partyType;
        this.firstName = firstName;
        
        this.partyRelationships = new ArrayList<>();
        this.partyRoles = new ArrayList<>();
        this.partyContactMechanisms = new ArrayList<>();
    }
    
    /**
     * Add a new PartyRelationship to this Party object. As a note, after call this method, save method must 
     * be called from repository class to commit the new PartyRelationship to database. 
     * @param childParty other Party that want to form a relation with this Party.
     * @param relationshipType
     * @throws java.lang.Exception when relationship_type is mismatch or party_relationship is already exists.
     */
    public void addPartyRelationship(Party childParty, Enumeration relationshipType) throws Exception {
        if (!relationshipType.getType().equals(Enumeration.EnumerationType.PARTY_RELATIONSHIP_TYPE)) {
            throw new Exception("Relationship type is mismatch.");
        }
        
        PartyRelationship partyRelationship = new PartyRelationship(
                this, childParty, new Date(), relationshipType);
        
        if(this.partyRelationships == null) {
            this.partyRelationships = new ArrayList<>();
        } else if (this.partyRelationships.contains(partyRelationship)) {
            throw new Exception("Party Relationship is already exists.");
        }
        
        this.partyRelationships.add(partyRelationship);
    }
    
    /**
     * Remove a specific PartyRelationship from this Party object. As a note, after call this method, 
     * save method must be called from repository class to remove the PartyRelationship from database. 
     * @param partyRelationship
     * @throws java.lang.Exception when party_relationship is not exists in this party.
     */
    public void removePartyRelationship(PartyRelationship partyRelationship) throws Exception {
        if (this.partyRelationships == null) {
            throw new Exception("Party Relationship is empty.");
        }
        
        int index = this.partyRelationships.indexOf(partyRelationship);
        if (index != -1) {
            this.partyRelationships.get(index).setToDate(new Date());
        } else {
            throw new Exception("Party Relationship is not exists.");
        }
    }
    
    /**
     * Remove the all the relationships for a specific Party. As a note, after call this method, 
     * save method must be called from repository class to remove the PartyRelationship from database. 
     * @param otherParty
     * @throws Exception when party_relationship is not exists in this party.
     */
    public void removePartyRelationships(Party otherParty) throws Exception {
        if (this.partyRelationships == null) {
            throw new Exception("Party Relationship is empty.");
        }
        
        List<PartyRelationship> deletedList = new ArrayList<>();
        
        for (PartyRelationship partyRelationship : this.partyRelationships) {
            if (partyRelationship.getRightParty().equals(otherParty)) {
                deletedList.add(partyRelationship);
            }
        }
        
        // delete parties
        this.partyRelationships.removeAll(deletedList);
    }
    
    /**
     * Get children of this Party based on its relationshipType.
     * @param relationshipType
     * @return
     * @throws Exception when relationshipType is not PARTY_RELATIONSHIP_TYPE.
     */
    public List<Party> getChildByRelationshipType(Enumeration relationshipType) throws Exception {
        if (!relationshipType.getType().equals(Enumeration.EnumerationType.PARTY_RELATIONSHIP_TYPE)) {
            throw new Exception("Relationship type is mismatch.");
        }
        
        List<Party> children = new ArrayList<>();
        
        if (this.partyRelationships != null && relationshipType.getType().equals(
                Enumeration.EnumerationType.PARTY_RELATIONSHIP_TYPE)) {
            for (PartyRelationship partyRelationship : this.partyRelationships) {
                if (partyRelationship.getRelationshipType().equals(relationshipType) &&
                        partyRelationship.getFromDate().before(new Date()) &&
                        (partyRelationship.getToDate() == null 
                            || partyRelationship.getToDate().after(new Date()))) {
                    children.add(partyRelationship.getRightParty());
                }
            }
        }
        
        return children;
    }
    
    /**
     * Add a new PartyRelationship to this Party object. As a note, after call this method, save method must 
     * be called from repository class to commit the new PartyRelationship to database. 
     * @param roleType
     * @throws java.lang.Exception when party_role already exists.
     */
    public void addPartyRole(Enumeration roleType) throws Exception {
        if (!roleType.getType().equals(Enumeration.EnumerationType.PARTY_ROLE_TYPE)) {
            throw new Exception("Party Role Type is mismatch.");
        }
        
        PartyRole partyRole = new PartyRole(this, roleType, new Date());
        partyRole.setEnabled(true);
        
        if(this.partyRoles == null) {
            this.partyRoles = new ArrayList<>();
        } else if (this.partyRoles.contains(partyRole)) {
            throw new Exception("Party Role is already exists.");
        }
        
        this.partyRoles.add(partyRole);
    }
    
    /**
     * Remove a specific PartyRole from this Party object. As a note, after call this method, 
     * save method must be called from repository class to remove the PartyRole from database. 
     * @param partyRole
     * @throws java.lang.Exception when party_role not exists.
     */
    public void removePartyRole(PartyRole partyRole) throws Exception {
        if (this.partyRelationships == null) {
            throw new Exception("Party Role is empty.");
        }
        
        int index = this.partyRoles.indexOf(partyRole);
        if (index != -1) {
            this.partyRoles.get(index).setToDate(new Date());
        } else {
            throw new Exception("Party Role is not exists.");
        }
    }
    
    /**
     * @return the first role.
     */
    @JsonIgnore
    public PartyRole getFirstRole() {
        return (this.partyRoles != null && this.partyRoles.size() > 0) ? getActivePartyRoles().get(0) : null;
    }
    
    /**
     * @param roleType
     * @return the first role that still active for the specific roleType.
     */
    public PartyRole getFirstRoleByType(Enumeration roleType) {
        if (this.partyRoles != null) {
            for (PartyRole partyRole : getActivePartyRoles()) {    
                if (partyRole.getPartyRole().equals(roleType)) {
                    return partyRole;
                }
            }
        }
        
        return null;
    }
    
    /**
     * @return list of party role that still active.
     */
    public List<PartyRole> getActivePartyRoles() {
        List<PartyRole> activePartyRoles = new ArrayList<>();
        
        if (this.partyRoles != null) {
            for (PartyRole partyRole : this.partyRoles) {
                if (partyRole.getFromDate().before(new Date()) && 
                        (partyRole.getToDate() == null || 
                        partyRole.getToDate().after(new Date()))) {    
                    activePartyRoles.add(partyRole);
                }
            }
        }
        
        return activePartyRoles;
    }
    
    /**
     * Add a new PartyContactMechanism to this Party object. As a note, after call this method, save method must 
     * be called from repository class to commit the new PartyContactMechanism to database.
     * @param contactMechanism
     * @param contactMechanismPurpose
     * @throws java.lang.Exception when contact_mechanism_purpose is mismatch or 
     * PartyContactMechanism is already exists.
     */
    public void addPartyContactMechanism(ContactMechanism contactMechanism, Enumeration contactMechanismPurpose) 
            throws Exception {
        if (!contactMechanismPurpose.getType().equals(Enumeration.EnumerationType.CONTACT_MECHANISM_PURPOSE)) {
            throw new Exception("Contact mechanism purpose is mismatch.");
        }
        
        PartyContactMechanism partyContactMechanism = new PartyContactMechanism(
                this, contactMechanism, contactMechanismPurpose, new Date());
        
        if(this.partyContactMechanisms == null) {
            this.partyContactMechanisms = new ArrayList<>();
        } else if (this.partyContactMechanisms.contains(partyContactMechanism)) {
            throw new Exception("Party Contact Mechanism is already exists.");
        }
        
        this.partyContactMechanisms.add(partyContactMechanism);
    }
    
    /**
     * Remove a specific PartyContactMechanism from this Party object. As a note, after call this method, 
     * save method must be called from repository class to remove the PartyContactMechanism from database. 
     * @param partyContactMechanism
     * @throws java.lang.Exception when PartyContactMechanism is not exists.
     */
    public void removePartyContactMechanism(PartyContactMechanism partyContactMechanism) throws Exception {
        if (this.partyContactMechanisms == null) {
            throw new Exception("Party Contact Mechanism is empty.");
        }
        
        int index = this.partyContactMechanisms.indexOf(partyContactMechanism);
        if (index != -1) {
            this.partyContactMechanisms.get(index).setToDate(new Date());
        } else {
            throw new Exception("Party Contact Mechanism is not exists.");
        }
    }

    /**
     * @param contactMechanismPurposeName
     * @return ContactMechanism for this Party with the specific ContactMechanismPurpose.
     */
    public ContactMechanism getContactMechanismByPurpose(String contactMechanismPurposeName) {
        if (this.partyContactMechanisms != null) {
            for (PartyContactMechanism partyContactMechanism : this.partyContactMechanisms) {
                if (partyContactMechanism.getContactMechanismPurpose().getEnumName()
                        .equals(contactMechanismPurposeName) && 
                        partyContactMechanism.getFromDate().before(new Date()) && 
                        (partyContactMechanism.getToDate() == null || 
                            partyContactMechanism.getToDate().after(new Date()))) {

                    return partyContactMechanism.getContactMechanism();
                }
            }
        }
        
        return null;
    }
    
    /**
     * Fetch contact from specific ContactMechanismPurpose.
     * @param contactMechanismPurposeName
     * @return address if the type is POSTAL_ADDRESS, email if the type is ELECTRONIC_ADDRESS, 
     * and phone number if the type is TELECOMMUNICATION_NUMBER.
     */
    public String getContactByPurpose(String contactMechanismPurposeName) {
        ContactMechanism contactMechanism = getContactMechanismByPurpose(contactMechanismPurposeName);
        
        if (contactMechanism != null) {
            switch (contactMechanism.getMechanismType()) {
                case POSTAL_ADDRESS : 
                    return contactMechanism.getAddress1();
                case ELECTRONIC_ADDRESS : 
                    return contactMechanism.getElectronicAddress();
                case TELECOMMUNICATION_NUMBER :
                    return contactMechanism.getPhoneNumber();
            }
        }
        
        return null;
    }
    
    /**
     * @return party's full name.
     */
    public String getFullName() {
        String fullName = this.firstName != null 
                ? (this.personTitle != null ? this.personTitle.concat(" ") : "").concat(this.firstName) : null;
        
        if (fullName != null) {
            fullName += (this.middleName != null ? (" ".concat(this.middleName)) : "");
            fullName += (this.lastName != null ? (" ".concat(this.lastName)) : "");
        }
        
        return fullName;
    }

    public List<PartyRelationship> getPartyRelationships() {
        return Collections.unmodifiableList(partyRelationships != null ? 
                partyRelationships : new ArrayList<>());
    }
    
    public List<PartyRole> getPartyRoles() {
        return Collections.unmodifiableList(partyRoles != null ? partyRoles : new ArrayList<>());
    }

    public List<PartyContactMechanism> getContactMechanisms() {
        return Collections.unmodifiableList(partyContactMechanisms != null ? 
                partyContactMechanisms : new ArrayList<>());
    }
    
}
