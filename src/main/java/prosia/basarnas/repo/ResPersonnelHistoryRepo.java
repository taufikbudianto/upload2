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
import prosia.basarnas.model.ResPersonnelHistory;

/**
 *
 * @author PROSIA
 */
@Repository
public interface ResPersonnelHistoryRepo extends JpaRepository<ResPersonnelHistory, String>, JpaSpecificationExecutor<ResPersonnelHistory> {    
    public List<ResPersonnelHistory> findAllByPersonnelHistoryIDContaining(String cgk);
    
    @Query("SELECT MAX(personnelHistoryID) FROM ResPersonnelHistory WHERE personnelHistoryID LIKE %?1%")
    public String findPersonnelHistoryByMaxId(String cgk);
}
