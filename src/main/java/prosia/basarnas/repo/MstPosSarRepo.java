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
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;

/**
 *
 * @author PROSIA
 */
@Repository
public interface MstPosSarRepo extends JpaRepository<MstPosSar, String>, JpaSpecificationExecutor<MstPosSar> {

    public List<MstPosSar> findAllMstPosSarBykantorsarid(MstKantorSar kantorSar);

    public MstPosSar findTopOneMstPosSarByPossarid(String possarid);

    @Query(value = "select * from MST_POSSAR possar where possar.LONGITUDE is not null and possar.LATITUDE is not null and possar.ISDELETED=0",
            nativeQuery = true)
    List<MstPosSar> findAllByLatLongNotNull();
    
    @Query(value="select * from MST_POSSAR where USERSITEID = :usersite", nativeQuery = true )
    public List<MstPosSar> findAllByKansarID(@Param("usersite") String user);
}
