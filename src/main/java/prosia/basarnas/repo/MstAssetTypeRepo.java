/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstSkill;

/**
 *
 * @author PROSIA
 */

@Repository
public interface MstAssetTypeRepo extends JpaRepository<MstAssetType, String>, JpaSpecificationExecutor<MstAssetType>{   
    public MstAssetType findTopOneMstAssetTypeByAssetnameAndIssruAndMatra (String assetname,Integer issru,Integer matra);
    
    public MstAssetType findByAssetname(String assetname);
    
    public List<MstAssetType> findTopOneByAssettypeidContaining(String cgk);
    
    public List<MstAssetType> findAllByIssru(Integer issru);
    
    @Query("SELECT MAX(assettypeid) FROM MstAssetType WHERE assettypeid LIKE %?1%")
    public String findAssetByMaxId(String cgk);
    
    List<MstAssetType> findByIsdeletedOrderByAssetname(BigInteger isdeleted);
    
    public List<MstAssetType> findByAssettypeid(String assettypeid);
    
    @Query(value = "select res.ASSETTYPEID from RES_ASSET res join RES_POTENCY pot "
            + " on res.POTENCYID = pot.POTENCYID where res.ASSETTYPEID is not null "
            + " and pot.KANTORSARID in :idkansar group by res.ASSETTYPEID",nativeQuery = true)
    List<String> listAssetTypeId(@Param("idkansar") List<String> idkansar);
}
