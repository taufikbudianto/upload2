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
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.UtiSighting;

/**
 *
 * @author Aris
 */
@Repository
public interface IncidentLogRepo extends JpaRepository<IncidentLog, String>, JpaSpecificationExecutor<IncidentLog> {

    public List<IncidentLog> findAllBylogIDContaining(String classid);

    @Query("SELECT MAX(logID) FROM IncidentLog WHERE logID LIKE %?1%")
    public String findClassByMaxId(String id);

    public List<IncidentLog> findByIncidentId(String incidentId);
    
    public List<IncidentLog> findByIncidentAndDeleted(Incident incidentId,Boolean delete);
    
    public List<IncidentLog> findBySighting(UtiSighting sighting);
    
    @Query(value = "select Distinct * from PRO_LOG log where log.LONGITUDE is not null "
            + "and log.LATITUDE is not null and log.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<IncidentLog> findAllByLatLongNotNull();
}
