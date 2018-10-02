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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.model.ResPotencyContact;

/**
 *
 * @author PROSIA
 */
@Repository
public interface ResAssetContactRepo extends JpaRepository<ResAssetContact, String>, JpaSpecificationExecutor<ResAssetContact> {
     public List<ResAssetContact> findAllByAssetcontactidContaining(String cgk);
    
    @Query("SELECT MAX(assetcontactid) FROM ResAssetContact WHERE assetcontactid LIKE %?1%")
    public String findAssetContactByMaxId(String cgk);
    
    public List<ResAssetContact> findAllByAssetid(ResAsset assetid);
    
    @Query(value = "select Distinct * from RES_ASSETCONTACT contact where CONTACT.ASSETID= :asset",
            nativeQuery = true)
    public ResAssetContact findByAssetIdDist(@Param("asset") String asset);
}
