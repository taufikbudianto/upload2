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
import prosia.basarnas.model.IndexSweepWidth;

/**
 *
 * @author Aris
 */
@Repository
public interface IndexSweepWidthRepo extends JpaRepository<IndexSweepWidth, String>, JpaSpecificationExecutor<IndexSweepWidth> {
    @Query("select distinct searchObject from IndexSweepWidth where UPPER(rescuer) = ?1 order by searchObject")
    public List<String> findSearchObject(String id);
    
    @Query("select width from IndexSweepWidth where UPPER(rescuer) = ?1 and searchObject = ?2 and eyeHeight = ?3 and visibility = ?4")
    public List<Double> findWidth(String rescuer, String searchObject, double eyeHeight, double visibility);
}
