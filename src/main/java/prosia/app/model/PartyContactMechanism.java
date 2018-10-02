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
@Table(name = "psa_party_ctc_mechanism")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyContactMechanismPK"})
@ToString(exclude = {"party", "contactMechanism", "contactMechanismPurpose"})
@NoArgsConstructor
public class PartyContactMechanism extends AbstractAuditingEntity implements Serializable {
    
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private PartyContactMechanismPK partyContactMechanismPK;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    @JoinColumn(name = "party_id", referencedColumnName = "party_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Party party;
    
    @JoinColumn(name = "cm_id", referencedColumnName = "cm_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactMechanism contactMechanism;
    
    @JoinColumn(name = "cm_purpose_id", referencedColumnName = "enum_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Enumeration contactMechanismPurpose;

    public PartyContactMechanism(Party party, ContactMechanism contactMechanism, 
            Enumeration contactMechanismPurpose, Date fromDate) {
        this.partyContactMechanismPK = new PartyContactMechanismPK(
                party.getPartyId(), contactMechanism.getId(), contactMechanismPurpose.getEnumId(), fromDate);
        this.party = party;
        this.contactMechanism = contactMechanism;
        this.contactMechanismPurpose = contactMechanismPurpose;
    }

    public Date getFromDate() {
        return partyContactMechanismPK.getFromDate();
    }
    
    public void setFromDate(Date fromDate) {
        partyContactMechanismPK.setFromDate(fromDate);
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyId", "contactMechanismId", "contactMechanismPurposeId", "fromDate"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PartyContactMechanismPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "party_id")
    private int partyId;
    
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
