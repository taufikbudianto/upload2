/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.app.model.Enumeration;
import prosia.app.model.Party;
import prosia.app.model.PartyContactMechanism;
import prosia.app.model.PartyRole;
import prosia.app.repo.PartyRepo;

/**
 *
 * @author Randy
 */
@Service
@Transactional(readOnly = false, rollbackFor = { Exception.class })
public class PartyService {
    
    @Autowired
    private PartyRepo partyRepo;
    
    /**
     * Create a new party, and also add the role and relationship to its parent.
     * @param party
     * @param parent
     * @param partyRoleType
     * @param relationshipType
     * @throws Exception 
     */
    public void createNewParty(Party party, Party parent, 
            Enumeration partyRoleType, Enumeration relationshipType) throws Exception {
        // register the party
        party = partyRepo.save(party);
        
        // register the role
        party.addPartyRole(partyRoleType);
        
        // save changes
        partyRepo.save(party);
        
        // register the relationship
        if (parent != null) {
            parent.addPartyRelationship(party, relationshipType);
            
            // save parent's changes
            partyRepo.save(parent);
        }
    }
    
    /**
     * Update party's detail and update party's first role.
     * @param party
     * @param partyRoleType
     * @throws Exception 
     */
    public void updateParty(Party party, Enumeration partyRoleType) throws Exception {
        // update organization role
        PartyRole oldRole = party.getFirstRole();
        if (!oldRole.getPartyRole().equals(partyRoleType)) {
            // remove old role
            party.removePartyRole(oldRole);
            
            // insert new role
            party.addPartyRole(partyRoleType);
        }
        
        // save organization
        partyRepo.save(party);
    }
    
    /**
     * Delete specific party and its children, its role, and its relationship from database.
     * @param party
     * @param parentParty 
     * @param partyType 
     * @throws Exception 
     */
    public void deletePartyByPartyType(Party party, Party parentParty, Party.PartyType partyType) 
            throws Exception {
        // reload parent in case it had been changed
        parentParty = partyRepo.findOne(parentParty.getPartyId());
        
        // remove relationship from parent
        parentParty.removePartyRelationships(party);
        
        // save parent changes
        partyRepo.save(parentParty);
        
        // get children
        List<Party> children = partyRepo.findAllByParentAndPartyTypeAndCurrentTenant(party, partyType);
        
        // delete the children first
        for (Party child : children) {
            deletePartyByPartyType(child, party, partyType);
        }
        
        // reload party in case it had been changed
        party = partyRepo.findOne(party.getPartyId());
        
        // delete roles
        for (PartyRole partyRole : party.getPartyRoles()) {
            party.removePartyRole(partyRole);
        }
        
        // delete contact mechanism
        for (PartyContactMechanism contactMechanism : party.getContactMechanisms()) {
            party.removePartyContactMechanism(contactMechanism);
        }
        
        // save changes
        party = partyRepo.save(party);
        
        // delete party
        partyRepo.delete(party);
    }
    
    /**
     * Delete list of parties and its children, its role, and its relationship from database.
     * @param parties
     * @param parentParty
     * @param partyType
     * @throws Exception 
     */
    public void deletePartiesByPartyType(List<Party> parties, Party parentParty, 
            Party.PartyType partyType) throws Exception {
        for (Party party : parties) {
            deletePartyByPartyType(party, parentParty, partyType);
        }
    }
    
}
