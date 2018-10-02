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
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.GtwSmsOut;

/**
 *
 * @author PROSIA
 */
@Repository
public interface GtwSmsOutRepo extends JpaRepository<GtwSmsOut, String>, JpaSpecificationExecutor<GtwSmsOut>{
//    public String findTopOneBySmsoutgoingidContaining(String smsoutgoingId);
    public List<GtwSmsOut> findTopOneBySmsoutgoingidContaining(String smsoutgoingId);
    
    @Query("SELECT MAX(smsoutgoingid) FROM GtwSmsOut WHERE smsoutgoingid LIKE %?1%")
    public String findGtwSmsOutByMaxId(String id);
}
