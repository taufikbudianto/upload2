/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentPotency;
import prosia.basarnas.model.ResPotency;

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentPotencyRepo extends JpaRepository<IncidentPotency, String>, JpaSpecificationExecutor<IncidentPotency> {

    public List<IncidentPotency> findByIncident(Incident incident);
    
    public List<IncidentPotency> findByIncidentAndDeleted(Incident incident,Boolean delete);
    
    public IncidentPotency findAllByIncidentAndPotency(Incident incident, ResPotency resPotency);
    
    public List<IncidentPotency> findTopOneByIncidentPotencyIDContaining(String incassetid);
    
    @Query("select MAX(incidentPotencyID) FROM IncidentPotency WHERE incidentPotencyID like %?1%")
    public String findPotencyByMaxId(String cgk);
}
