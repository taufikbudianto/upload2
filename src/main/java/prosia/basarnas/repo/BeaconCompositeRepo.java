/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.UtiBeaconComposite;

/**
 *
 * @author PROSIA
 */
@Repository
public interface BeaconCompositeRepo extends JpaRepository<UtiBeaconComposite, Integer>, JpaSpecificationExecutor<UtiBeaconComposite> {

    public List<UtiBeaconComposite> findAllByIncidentid(Incident incident);
    
    public UtiBeaconComposite findByIncidentid(Incident incident);
}
