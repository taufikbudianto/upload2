/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.model.MstChecklist;
import prosia.basarnas.model.MstChecklistdetail;
import prosia.basarnas.model.MstKantorSar;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstChecklistdetailRepo extends JpaRepository<MstChecklistdetail, String>, JpaSpecificationExecutor<MstChecklistdetail>{
    public List<MstChecklistdetail> findAllByChecklistdetailidContaining(String cgk);
    
    @Query("SELECT MAX(checklistdetailid) FROM MstChecklistdetail WHERE checklistdetailid LIKE %?1%")
    public String findCheckdetailByMaxId(String cgk);
    
    public MstChecklistdetail findByChecklistdetailid(String checklistdetailid);
    
    //public MstChecklistdetail findByChecklistid(MstChecklist checklistid);
    
    public List<MstChecklistdetail> findByChecklistid(MstChecklist checklistid);
    
    //    @Modifying
    //    @Transactional
    //   
    //    @Query("DELETE FROM MstChecklistdetail WHERE checklistdetailid LIKE %?1%")
    //    void deleteByChecklistdetailid(String checklistdetailid);
    
    public MstChecklistdetail findTopOneMstChecklistdetailByCategoryAndDescription (String category,String description);
    
}
