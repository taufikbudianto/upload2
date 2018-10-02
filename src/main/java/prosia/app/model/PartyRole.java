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
import javax.persistence.Index;
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
@Table(name = "psa_app_party_role", 
        indexes = { @Index(columnList = "role_identifier") })
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyRolePK"})
@ToString(exclude = {"partyRole", "party"})
@NoArgsConstructor
public class PartyRole extends AbstractAuditingEntity implements Serializable {
    
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private PartyRolePK partyRolePK;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    
    @Column(name = "role_identifier", length = 30)
    private String roleIdentifier;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @JoinColumn(name = "party_role_id", referencedColumnName = "enum_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Enumeration partyRole;
    
    @JoinColumn(name = "party_id", referencedColumnName = "party_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Party party;

    public PartyRole(Party party, Enumeration partyRole, Date fromDate) {
        this.partyRolePK = new PartyRolePK(party.getPartyId(), partyRole.getEnumId(), fromDate);
        this.party = party;
        this.partyRole = partyRole;
    }

    public Date getFromDate() {
        return partyRolePK.getFromDate();
    }
    
    public void setFromDate(Date fromDate) {
        partyRolePK.setFromDate(fromDate);
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"partyId", "partyRoleId", "fromDate"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PartyRolePK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "party_id")
    private int partyId;
    
    @Basic(optional = false)
    @Column(name = "party_role_id")
    private int partyRoleId;
    
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    
}
