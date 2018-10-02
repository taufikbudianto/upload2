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
import prosia.basarnas.model.CPNotifikasi;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface NotifikasiRepo extends JpaRepository<CPNotifikasi, Integer>,
         JpaSpecificationExecutor<CPNotifikasi> {    
    public CPNotifikasi findByNotifId(Integer notifId);
    @Query(value = "SELECT * FROM CP_NOTIFIKASI NOTIF WHERE NOTIF.NOTIF_ID in :idList",
            nativeQuery = true)
    public List<CPNotifikasi> getAllByNotifId(@Param("idList")List<Integer> data);
}
