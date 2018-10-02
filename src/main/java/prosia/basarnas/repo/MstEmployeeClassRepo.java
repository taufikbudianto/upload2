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
import prosia.basarnas.model.MstEMployeeClass;

/**
 *
 * @author PROSIA
 */

@Repository
public interface MstEmployeeClassRepo extends JpaRepository<MstEMployeeClass, String>, JpaSpecificationExecutor<MstEMployeeClass>{   
    public MstEMployeeClass findTopOneMstEMployeeClassByEmploymentclasscodeAndEmploymentclassname (String employmentclasscode,String employmentclassname);
    
    public List<MstEMployeeClass> findAllByEmploymentclassidContaining(String classid);
    
    @Query("SELECT MAX(employmentclassid) FROM MstEMployeeClass WHERE employmentclassid LIKE %?1%")
    public String findClassByMaxId(String id);
}

