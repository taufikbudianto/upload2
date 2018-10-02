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
import prosia.basarnas.model.IncidentPOB;

/**
 *
 * @author TOMY
 */

@Repository
public interface IncidentTabPOBRepo extends JpaRepository<IncidentPOB, String>,JpaSpecificationExecutor<IncidentPOB>{
   
    public List<IncidentPOB> findAllByPobIDContaining(String cgk);
    
    @Query("SELECT MAX(pobID) FROM IncidentPOB WHERE pobID LIKE %?1%")
    public String findPobByMaxId(String cgk);
    
    public List<IncidentPOB> findByIncident(Incident incident);
    
    public List<IncidentPOB> findByIncidentAndIsdeleted(Incident incident,BigInteger deleted);
    
    @Query(value = "select Distinct * from INC_POB pob where pob.LONGITUDE is not null "
            + "and pob.LATITUDE is not null and pob.ISDELETED=0", nativeQuery = true)
    public List<IncidentPOB> findAllByLatLongNotNull();
}
