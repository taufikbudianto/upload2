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
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;

/**
 *
 * @author PROSIA
 */
@Repository
public interface ResPotencyContactRepo  extends JpaRepository<ResPotencyContact, String>, JpaSpecificationExecutor<ResPotencyContact> {
     
    public List<ResPotencyContact> findAllBypotencyid (ResPotency potency);
    
    @Query("select MAX(potencycontactid) FROM ResPotencyContact WHERE potencycontactid like %?1%")
    public String findPersonnelByMaxId(String cgk);
    
    @Query("select h FROM ResPotencyContact h WHERE h.potencycontactid like %?1%")
    public List<ResPotencyContact> findAllByPContactIdLike(String resPersonnelId);
    @Query(value = "select * from RES_POTENCYCONTACT contact where CONTACT.POTENCYID= :potensi",
            nativeQuery = true)
    public ResPotencyContact findByPotencyId(@Param("potensi") String potensi);

    @Query(value = "select Distinct * from RES_POTENCYCONTACT contact where CONTACT.POTENCYID= :potensi",
            nativeQuery = true)
    public ResPotencyContact findByPotencyIdDist(@Param("potensi") String potensi);
    
    
}
