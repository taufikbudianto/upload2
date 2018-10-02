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
import prosia.basarnas.model.CPNotifTanggapan;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface NotifikasiTanggapanRepo extends JpaRepository<CPNotifTanggapan, Integer>,
         JpaSpecificationExecutor<CPNotifTanggapan> {
    
    @Query(value = "select * from CP_NOTIF_TANGGAPAN tanggapan where TANGGAPAN.NOTIF_ID=:notifId ",
            nativeQuery = true)
    public List<CPNotifTanggapan>getDataByNotifIdAndIdPersonel(@Param("notifId")Integer notifId);
}
