/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentPersonnel;

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentPersonnelRepo extends JpaRepository<IncidentPersonnel, String>, JpaSpecificationExecutor<IncidentPersonnel> {

    public List<IncidentPersonnel> findAllByIncident(Incident incident);
    
    public List<IncidentPersonnel> findByIncidentAndDeleted(Incident incident, Boolean delete);
    
    public List<IncidentPersonnel> findTopOneByIncidentPersonnelIDContaining(String incPersonnelId);
    
    @Query("select MAX(incidentPersonnelID) FROM IncidentPersonnel WHERE incidentPersonnelID like %?1%")
    public String findPotencyByMaxId(String cgk);
}
