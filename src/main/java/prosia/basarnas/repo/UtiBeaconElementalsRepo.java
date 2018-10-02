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
import prosia.basarnas.model.UtiBeaconElemental;

/**
 *
 * @author PROSIA
 */
@Repository
public interface UtiBeaconElementalsRepo extends JpaRepository<UtiBeaconElemental, Integer>, JpaSpecificationExecutor<UtiBeaconElemental>{
    @Query(value = "select Distinct * from UTI_BEACONELEMENTAL elem where elem.LONGITUDE is not null "
            + "and elem.LATITUDE is not null and elem.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<UtiBeaconElemental> findAllByLatLongNotNull();
  
}
