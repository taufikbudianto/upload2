/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstJenisBiaya;

/**
 *
 * @author Taufik AB
 */
@Repository
public interface MstJenisBiayaRepo extends JpaRepository<MstJenisBiaya, Integer>, JpaSpecificationExecutor<MstJenisBiaya> {
    
    public MstJenisBiaya findByNama(String nama);
}
