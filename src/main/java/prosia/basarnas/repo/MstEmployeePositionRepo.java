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
import prosia.basarnas.model.MstEmployeePosition;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstEmployeePositionRepo extends JpaRepository<MstEmployeePosition, String>, JpaSpecificationExecutor<MstEmployeePosition>{   
    public MstEmployeePosition findTopOneMstEmployeePositionByEmploymentpositionnameAndCategory (String employmentpositionname,Integer category);
    
    public MstEmployeePosition findByEmploymentpositionname(String employmentpositionname);
    
    public List<MstEmployeePosition> findAllByEmploymentpositionidContaining(String cgk);
    
    @Query("SELECT v FROM MstEmployeePosition v WHERE v.category = 1")
    public List<MstEmployeePosition> findAllPositionStruktural();
    
    @Query("SELECT v FROM MstEmployeePosition  v WHERE v.category = 2 ")
    public List<MstEmployeePosition> findAllPositionFungsional();
    
    @Query("SELECT v FROM MstEmployeePosition v WHERE v.category = 3 ")
    public List<MstEmployeePosition> findAllPositionSiaga();
    
    @Query("SELECT MAX(employmentpositionid) FROM MstEmployeePosition WHERE employmentpositionid LIKE %?1%")
    public String findPositionByMaxId(String cgk);
}

