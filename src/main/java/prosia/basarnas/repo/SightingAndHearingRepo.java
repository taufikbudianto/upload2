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
import prosia.basarnas.model.UtiSighting;

/**
 *
 * @author PROSIA
 */
@Repository
public interface SightingAndHearingRepo extends JpaRepository<UtiSighting, String>, JpaSpecificationExecutor<UtiSighting> {

    public List<UtiSighting> findByIncident(Incident incident);

    public List<UtiSighting> findBySightingIdContaining(String sightingId);

    @Query("SELECT MAX(sightingId) FROM UtiSighting WHERE sightingId LIKE %?1%")
    public String findUtiSightingByMaxId(String id);
    
    @Query(value = "select Distinct * from UTI_SIGHTING s where s.OBSERVERLONGITUDE is not null "
            + "and s.OBSERVERLATITUDE is not null and s.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<UtiSighting> findAllByLatLongNotNull();
    
    public UtiSighting findBySightingId(UtiSighting sighting);
}
