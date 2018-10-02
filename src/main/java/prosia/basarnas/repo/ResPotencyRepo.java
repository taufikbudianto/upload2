/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.Collection;
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
public interface ResPotencyRepo extends JpaRepository<ResPotency, String>, JpaSpecificationExecutor<ResPotency> {
    
    @Query("select MAX(potencyid) FROM ResPotency WHERE potencyid like %?1%")
    public String findPotencyByMaxId(String cgk);
    
    public List<ResPotency> findTopOneBypotencyidContaining(String resPersonnelId);
    
    @Query("select a FROM ResPotency a WHERE a.potencyid like %?1%")
    public List<ResPotency> findAllpotencyid(String resPersonnelId);
    
    @Query(value ="select a FROM ResPotency a Where a.usersiteid in ?1")
    public Page<ResPotency> findAllByKansarId(Collection<String> listKansar, Pageable pageable);
    
    @Query("select count(a) FROM ResPotency a Where a.usersiteid in ?1")
    public Long findAllByKansarId(Collection<String> listKansar);
    
    @Query(value = "SELECT count(*) from RES_ASSET ra join RES_POTENCY rp on rp.POTENCYID = ra.POTENCYID " +
            " where ra.ASSETTYPEID = :assetType and rp.KANTORSARID in :kansar", nativeQuery = true)
    public Long findAllAssetType(@Param("assetType") String assetType, @Param("kansar") List<String> kansar);
    
     @Query(value = "select * from RES_POTENCY a where a.ISDELETED <>1 and a.KANTORSARID IN :listnearest ORDER BY a.POTENCYNAME DESC ",
            nativeQuery = true)
    public List<ResPotency> findAllByNearestKansar(@Param("listnearest") List<String> listNearest);
    
    @Query(value = "select Distinct * from RES_POTENCY potensi where potensi.LONGITUDE is not null "
            + "and potensi.LATITUDE is not null and potensi.ISDELETED=0 And ROWNUM <=20", nativeQuery = true)
    List<ResPotency> findAllByLatLongNotNull();
    
   @Query("select a FROM ResPotency a WHERE a.potencyname =?1")
    public List<ResPotency> getPotensiByPotencyName(String satker);
    @Query(value = "SELECT * FROM RES_POTENCY pot where POT.POTENCYNAME=:potname "
            + " and POT.KANTORSARID=(select KANSAR.KANTORSARID from MST_KANTORSAR kansar where KANSAR.C_SATKER=:cSatker)",nativeQuery = true)
    public ResPotency getPotencyByKansarAndPotencyName(@Param("potname") String potname,@Param("cSatker") String cSatker);
    
    @Query(value="select * from RES_POTENCY where KANTORSARID = :usersite", nativeQuery = true )
    public List<ResPotency> findAllByKansarID(@Param("usersite") String usersite);
}
