/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.AssetTypeImages;
import prosia.basarnas.model.MstAssetType;

/**
 *
 * @author TOMY
 */
@Repository
public interface AssetTypeImageRepo extends JpaRepository<AssetTypeImages, Integer>, JpaSpecificationExecutor<AssetTypeImages>{
    public List<AssetTypeImages> findByAssettypeidAndDeleted(MstAssetType assettypeid,boolean deleted); 
    public List<AssetTypeImages> findByAssettypeid(MstAssetType id);
    public AssetTypeImages findByImageID(Integer id);
}
