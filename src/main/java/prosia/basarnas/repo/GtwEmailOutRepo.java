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
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;

/**
 *
 * @author PROSIA
 */
@Repository
public interface GtwEmailOutRepo extends JpaRepository<GtwEmailOut, String>, JpaSpecificationExecutor<GtwEmailOut>{
    public List<GtwEmailOut> findTopOneByEmailoutgoingidContaining(String emailoutgoingId);
    
    @Query("SELECT MAX(emailoutgoingid) FROM GtwEmailOut WHERE emailoutgoingid LIKE %?1%")
    public String findGtwEmailOutByMaxId(String id);
    
    @Query("select MAX(emailoutgoingid) FROM GtwEmailOut WHERE emailoutgoingid like %?1%")
    public String findEmailByMaxId(String cgk);
}
