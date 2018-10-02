/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.app.model.Party;

/**
 *
 * @author Randy
 */
@Repository
public interface PartyRepo extends JpaRepository<Party, Integer> {
    
    /**
     * @param parent
     * @param partyType
     * @return list of party for the specific partyType and specific parent, 
     * and also has the same tenant as the current tenant of the user's login.
     */
    @Query(value = "select p.rightParty from PartyRelationship p where p.leftParty = ?1 "
            + "and p.rightParty.partyType = ?2 and p.rightParty.tenant = ?#{principal.defaultTenant}")
    public List<Party> findAllByParentAndPartyTypeAndCurrentTenant(Party parent, Party.PartyType partyType);
    
}
