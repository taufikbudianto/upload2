/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.UtiBeaconComposite;

/**
 *
 * @author PROSIA
 */
@Repository
public interface UtiBeaconCompositeRepo extends JpaRepository<UtiBeaconComposite, Integer> {    
    @Query("select a FROM UtiBeaconComposite a WHERE a.isdeleted = '0' AND a.incidentid is null AND a.datecreated BETWEEN TRUNC(SYSDATE - 7) AND TRUNC(SYSDATE)")
    public List<UtiBeaconComposite> findAllByIsDeletedDateCreatedIncidentId();
    
    public List<UtiBeaconComposite> findByIsdeleted(Integer isdeleted);
    
    @Query(value = "select Distinct * from UTI_BEACONCOMPOSITE comp where comp.LONGITUDE is not null "
            + "and comp.LATITUDE is not null and comp.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<UtiBeaconComposite> findAllByLatLongNotNull();
    }
