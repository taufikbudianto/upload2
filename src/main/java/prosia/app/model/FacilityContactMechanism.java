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
@Table(name = "psa_facility_mechanism")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"facilityContactMechanismPK"})
@ToString(exclude = {"facility", "contactMechanism", "contactMechanismPurpose"})
@NoArgsConstructor
public class FacilityContactMechanism extends AbstractAuditingEntity implements Serializable{
    
    @EmbeddedId
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private FacilityContactMechanismPK facilityContactMechanismPK;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Facility facility;
    
    @JoinColumn(name = "cm_id", referencedColumnName = "cm_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactMechanism contactMechanism;
    
    @JoinColumn(name = "cm_purpose_id", referencedColumnName = "enum_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Enumeration contactMechanismPurpose;

    public FacilityContactMechanism(Facility facility, ContactMechanism contactMechanism, 
            Enumeration contactMechanismPurpose, Date fromDate) {
        this.facilityContactMechanismPK = new FacilityContactMechanismPK(
                facility.getFacilityId(), contactMechanism.getId(), contactMechanismPurpose.getEnumId(), fromDate);
        this.facility = facility;
        this.contactMechanism = contactMechanism;
        this.contactMechanismPurpose = contactMechanismPurpose;
    }

    public Date getFromDate() {
        return facilityContactMechanismPK.getFromDate();
    }
    
    public void setFromDate(Date fromDate) {
        facilityContactMechanismPK.setFromDate(fromDate);
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"facilityId", "contactMechanismId", "contactMechanismPurposeId", "fromDate"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
class FacilityContactMechanismPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "facility_id")
    private int facilityId;
    
    @Basic(optional = false)
    @Column(name = "cm_id")
    private int contactMechanismId;
    
    @Basic(optional = false)
    @Column(name = "cm_purpose_id")
    private int contactMechanismPurposeId;
    
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    
}