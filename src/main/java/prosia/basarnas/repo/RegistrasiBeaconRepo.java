/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.UtiAirvessel;

/**
 *
 * @author PROSIA
 */
@Repository
public interface RegistrasiBeaconRepo extends JpaRepository<UtiAirvessel, String>, JpaSpecificationExecutor<UtiAirvessel> {
  
}