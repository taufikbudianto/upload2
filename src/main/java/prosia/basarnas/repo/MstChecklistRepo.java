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
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstChecklist;

/**
 *
 * @author PROSIA
 */

@Repository
public interface MstChecklistRepo extends JpaRepository<MstChecklist, String>, JpaSpecificationExecutor<MstChecklist>{   
    public MstChecklist findTopOneMstMstChecklistByName (String name);
    
    public List<MstChecklist> findAllByChecklistidContaining(String cgk);
    
    @Query("SELECT MAX(checklistid) FROM MstChecklist WHERE checklistid LIKE %?1%")
    public String findCheckByMaxId(String cgk);
    
    public MstChecklist findByChecklistid(String checklistid);
    
    public List<MstChecklist> findByName(String name);
    
    public List<MstChecklist> findAllByIsdeleted(BigInteger isdeleted);
}
