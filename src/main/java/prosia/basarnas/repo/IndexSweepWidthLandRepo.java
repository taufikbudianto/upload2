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
import prosia.basarnas.model.IndexSweepWidthLand;

/**
 *
 * @author Aris
 */
@Repository
public interface IndexSweepWidthLandRepo extends JpaRepository<IndexSweepWidthLand, String>, JpaSpecificationExecutor<IndexSweepWidthLand>{
    @Query("select distinct searchObject from IndexSweepWidthLand where searchObject = ?1 order by searchObject")
    public List<String> findSearchObject(String id);
    
    @Query("select width from IndexSweepWidthLand where searchObject = ?1 and eyeHeight = ?2 and visibility = ?3")
    public List<Double> findWidth(String searchObject, double eyeHeight, double visibility);
}
