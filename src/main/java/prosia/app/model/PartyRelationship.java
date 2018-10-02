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
@Table(name = "psa_app_party_relationship")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyRelationshipPK"})
@ToString(exclude = {"leftParty", "rightParty"})
@NoArgsConstructor
public class PartyRelationship extends AbstractAuditingEntity implements Serializable {
    
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private PartyRelationshipPK partyRelationshipPK;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    @JoinColumn(name = "relationship_type_id", referencedColumnName = "enum_id")
    @ManyToOne(optional = false)
    private Enumeration relationshipType;
    
    @JoinColumn(name = "left_party_id", referencedColumnName = "party_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Party leftParty;
    
    @JoinColumn(name = "right_party_id", referencedColumnName = "party_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Party rightParty;
    
    public PartyRelationship(Party leftParty, Party rightParty, Date fromDate, Enumeration relationshipType) {
        this.partyRelationshipPK = new PartyRelationshipPK(
                leftParty.getPartyId(), rightParty.getPartyId(), fromDate);
        this.relationshipType = relationshipType;
        this.leftParty = leftParty;
        this.rightParty = rightParty;        
    }

    public Date getFromDate() {
        return partyRelationshipPK.getFromDate();
    }
    
    public void setFromDate(Date fromDate) {
        partyRelationshipPK.setFromDate(fromDate);
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"leftPartyId", "rightPartyId", "fromDate"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PartyRelationshipPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "left_party_id")
    private int leftPartyId;
    
    @Basic(optional = false)
    @Column(name = "right_party_id")
    private int rightPartyId;
    
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    
}
