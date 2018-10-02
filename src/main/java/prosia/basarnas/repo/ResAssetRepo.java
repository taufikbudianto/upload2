/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPotency;

/**
 *
 * @author PROSIA
 */
@Repository
public interface ResAssetRepo extends JpaRepository<ResAsset, String>, JpaSpecificationExecutor<ResAsset> {

    public ResAsset findAllByAssetid(String id);

    @Query("select MAX(assetid) FROM ResAsset WHERE assetid like %?1%")
    public String findAssetByMaxId(String cgk);

    public List<ResAsset> findTopOneByassetidContaining(String resPersonnelId);

    @Query("select a FROM ResAsset a WHERE a.assetid like %?1%")
    public List<ResAsset> findAllByassetid(String resPersonnelId);

    @Query("select a FROM ResAsset a WHERE a.potencyid = ?1 and a.isdeleted = 0")
    public List<ResAsset> findBypotencyidAndisdeleted(ResPotency potency);

    @Query(value = "select * from RES_ASSET res where RES.ISDELETED =0 and RES.OP_TYPE = :op "
            + " and RES.KANTORSARID in  :listkansar ", nativeQuery = true)
    public List<ResAsset> findSruNearestKansar(@Param("op") String op, @Param("listkansar") List<String> listkansar);

    @Query(value = "select Distinct * from RES_ASSET asset where asset.LONGITUDE is not null "
            + "and asset.LATITUDE is not null and asset.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<ResAsset> findAllByLatLongNotNull();
    
    @Query(value="select * from RES_ASSET where usersiteid = :usersite", nativeQuery = true )
    public List<ResAsset> findAllByKansarID(@Param("usersite") String usersite);
}
