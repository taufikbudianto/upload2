/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstKantorSarContact;

/**
 *
 * @author Taufik
 */
@Repository
public interface MstKantorSarContactRepo extends JpaRepository<MstKantorSarContact, String>,
        JpaSpecificationExecutor<MstKantorSarContact> {
    public List<MstKantorSarContact> findByKantorsarid(MstKantorSar kantorSar);
}
