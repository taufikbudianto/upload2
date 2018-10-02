/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import prosia.basarnas.model.CrmAreaCode;
import prosia.basarnas.model.MstRegion;

/**
 *
 * @author Owner
 */
public interface CrmAreaRepo extends JpaRepository<CrmAreaCode, String>, JpaSpecificationExecutor<CrmAreaCode>  {
    
    public List<CrmAreaCode> findByregion(MstRegion region); 
    
}
