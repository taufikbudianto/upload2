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
import prosia.basarnas.model.CPNotifPersonil;
import prosia.basarnas.model.CPNotifikasi;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface NotifikasiPersonilRepo extends JpaRepository<CPNotifPersonil, Integer>,
        JpaSpecificationExecutor<CPNotifPersonil> {

    public List<CPNotifPersonil> findByNotifikasi(CPNotifikasi cpn);
    @Query(value = "SELECT NOTIFPERSONEL.NOTIF_ID FROM CP_NOTIF_PERSONIL NOTIFPERSONEL WHERE NOTIFPERSONEL.PERSONNEL_ID=:id",
            nativeQuery = true)
    public List<Integer> getNotifId(@Param("id") String id);
}
