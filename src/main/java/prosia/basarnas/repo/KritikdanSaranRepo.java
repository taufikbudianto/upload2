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
import prosia.basarnas.model.KritikdanSaran;

/**
 *
 * @author PROSIA
 */
@Repository
public interface KritikdanSaranRepo extends JpaRepository<KritikdanSaran, Integer>, JpaSpecificationExecutor<KritikdanSaran> {
    
}
