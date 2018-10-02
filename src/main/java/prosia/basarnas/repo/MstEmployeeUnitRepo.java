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
import prosia.basarnas.model.MstEmployeeUnit;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstEmployeeUnitRepo extends JpaRepository<MstEmployeeUnit, String>, JpaSpecificationExecutor<MstEmployeeUnit>{   
    public MstEmployeeUnit findTopOneMstMstEmployeeUnitByEmploymentunitname (String employmentunitname);
    
    public List<MstEmployeeUnit> findAllByEmploymentunitidContaining(String cgk);
    
    @Query("SELECT MAX(employmentunitid) FROM MstEmployeeUnit WHERE employmentunitid LIKE %?1%")
    public String findUnitByMaxId(String cgk);
}


