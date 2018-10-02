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
import prosia.basarnas.model.IncidentChecklist;
import prosia.basarnas.model.MstNegara;

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentChecklistRepo extends JpaRepository<IncidentChecklist, String>, JpaSpecificationExecutor<IncidentChecklist> {
    public IncidentChecklist findByIncidentChecklistID(String incidentChecklistID); 
    
    public List<IncidentChecklist> findByIncident(Incident incident);
    
    public List<IncidentChecklist> findAllByIncidentChecklistIDContaining(String cgk);
    
    @Query("SELECT MAX(incidentChecklistID) FROM IncidentChecklist WHERE incidentChecklistID LIKE %?1%")
    public String findCheckByMaxId(String cgk);
    
    public String findTopOneByIncidentChecklistIDContaining(String IncidentChecklistID);
}
