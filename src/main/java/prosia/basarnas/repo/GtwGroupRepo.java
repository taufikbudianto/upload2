/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.GtwGroup;

/**
 *
 * @author PROSIA
 */
@Repository
public interface GtwGroupRepo extends JpaRepository<GtwGroup, String>, JpaSpecificationExecutor<GtwGroup> {

    List<GtwGroup> findByDeleted(boolean deleted);
}
