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
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;

/**
 *
 * @author Owner
 */

@Repository
public interface PersonelHistoryRepo extends JpaRepository<ResPersonnelHistory, String>, JpaSpecificationExecutor<ResPersonnelHistory> {
    
    public List<ResPersonnelHistory> findAllResPersonnelHistoryBypersonnel(ResPersonnel personnel);
    
    @Query("select MAX(personnelHistoryID) FROM ResPersonnelHistory WHERE personnelHistoryID like %?1%")
    public String findPersonnelByMaxId(String cgk);
    
    public List<ResPersonnelHistory> findTop1ByPersonnelHistoryIDContaining(String resPersonnelId);
    
    @Query("select h FROM ResPersonnelHistory h WHERE h.personnelHistoryID like %?1%")
    public List<ResPersonnelHistory> findAllByPersonnelHistoryIDLike(String resPersonnelId);
    
}
