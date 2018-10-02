/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import prosia.basarnas.model.BeaconModel;
import prosia.basarnas.model.Manufacturer;

/**
 *
 * @author PROSIA
 */
public interface MstBeaconModelRepo extends JpaRepository<BeaconModel, String> {

    public List<BeaconModel> findAllByManufacturerAndIsDeleted(Manufacturer manufacturer, Integer isDeleted);
    
    //public List<BeaconModel> findAllByBeaconModelIdAndIsDeleted(String manufacturerId, Integer isDeleted);
}
