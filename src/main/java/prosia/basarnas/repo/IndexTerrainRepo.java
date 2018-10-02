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
import prosia.basarnas.model.IndexTerrain;

/**
 *
 * @author Aris
 */
@Repository
public interface IndexTerrainRepo extends JpaRepository<IndexTerrain, String>, JpaSpecificationExecutor<IndexTerrain>{
    @Query("select correction from IndexTerrain where searchObject = ?1 and vegetation = ?2")
    public List<Double> findCorrection(String searchObject, String vegetation);
}
