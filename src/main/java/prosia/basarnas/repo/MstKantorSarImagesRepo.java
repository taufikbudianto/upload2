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
import prosia.basarnas.model.MstKantorSarImages;

/**
 *
 * @author Owner
 */

@Repository
public interface MstKantorSarImagesRepo extends JpaRepository<MstKantorSarImages, Integer>, JpaSpecificationExecutor<MstKantorSarImages>{
    public List<MstKantorSarImages> findByKanSarAndDeleted(MstKantorSar kanSar, boolean deleted);
    
    public MstKantorSarImages findByImageID (Integer ID);
    
    public MstKantorSarImages findAllByKanSarAndDeleted (MstKantorSar kantorSar, Boolean deleted);
    
}
