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
import prosia.basarnas.model.ResAsset;

/**
 *
 * @author TOMY
 */
@Repository
public interface ResPrasaranaRepo extends JpaRepository<ResAsset, String>, JpaSpecificationExecutor<ResAsset> {
    
    public List<ResAsset> findAllByAssetidContaining(String cgk);
    
    @Query("SELECT MAX(assetid) FROM ResAsset WHERE assetid LIKE %?1%")
    public String findAssetByMaxId(String cgk);
    
    public ResAsset findByName(String name);
    
    
}
