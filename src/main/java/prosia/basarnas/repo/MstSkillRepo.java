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
import prosia.basarnas.model.MstSkill;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstSkillRepo extends JpaRepository<MstSkill, String>, JpaSpecificationExecutor<MstSkill>{  
    public MstSkill findTopOneMstSkillBySkillnameAndCategory (String skillname,Integer category);
    
    public List<MstSkill> findAllBySkillidContaining(String cgk);
    
    @Query("SELECT v FROM MstSkill v WHERE v.isdeleted = 0 ")
    public List<MstSkill> findSkill();
    
    @Query("SELECT MAX(skillid) FROM MstSkill WHERE skillid LIKE %?1%")
    public String findSkillByMaxId(String cgk);
}

