/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.UtiBeaconComposite;
/**
 *
 * @author PROSIA
 */
@Repository
public interface CompositeBeaconRepo extends JpaRepository<UtiBeaconComposite, Integer>, JpaSpecificationExecutor<UtiBeaconComposite>{
    @Query(value = "select max(compositeid) from uti_beaconcomposite",nativeQuery = true)
    public Integer findMaxId();
  
}
