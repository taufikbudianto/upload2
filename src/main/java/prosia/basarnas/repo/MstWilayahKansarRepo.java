/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstWilayahKansar;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstWilayahKansarRepo extends JpaRepository<MstWilayahKansar, Integer>{
    @Query("SELECT a.kantorId FROM MstWilayahKansar a GROUP BY a.kantorId ORDER BY a.kantorId") 
    public List<Integer> findAllGroupByKantorId();
    
    public List<MstWilayahKansar> findByKantorIdOrderBySeqAsc(Integer kantorId);     
}
