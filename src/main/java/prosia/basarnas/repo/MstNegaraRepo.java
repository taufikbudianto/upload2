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
import prosia.basarnas.model.MstNegara;

/**
 *
 * @author PROSIA
 */

@Repository

public interface MstNegaraRepo extends JpaRepository<MstNegara, String>, JpaSpecificationExecutor<MstNegara>{
   public MstNegara findByCountryName(String countryname); 
   @Query("SELECT MAX(countryID) FROM MstNegara WHERE countryID LIKE %?1%")
   public String findClassByMaxId(String id);
   
   public MstNegara findTopOneMstNegaraByCountryID (String countryid);
}
