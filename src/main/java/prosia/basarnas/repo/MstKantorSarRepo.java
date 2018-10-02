/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.MstKantorSar;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstKantorSarRepo extends JpaRepository<MstKantorSar, String>, JpaSpecificationExecutor<MstKantorSar> {
    public MstKantorSar findTopOneMstKantorSarByKantorsarid (String kantorsarid);
    
    public MstKantorSar findByKantorsarname(String kantorsarname);
    
    public MstKantorSar findByKantorsarid(String kantorsarid);
    
    List<MstKantorSar> findByIsdeletedOrderByKantorsarname (BigInteger isdeleted);
    public MstKantorSar findBySatker(String satker);
    
    @Query(value = "select * from MST_KANTORSAR kansar where kansar.LONGITUDE is not null and kansar.LATITUDE is not null and kansar.ISDELETED=0",
            nativeQuery = true)
    List<MstKantorSar>findAllByLatLongNotNull();
    
    @Query(value="select * from MST_KANTORSAR where KANTORSARID = :usersite", nativeQuery = true )
    public List<MstKantorSar> findAllByKansarID(@Param("usersite") String usersite);
}
