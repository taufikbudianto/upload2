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
import prosia.basarnas.model.PotencyImages;
import prosia.basarnas.model.ResPotency;

/**
 *
 * @author Owner
 */

@Repository
public interface PotencyImagesRepo extends JpaRepository<PotencyImages, Integer>, JpaSpecificationExecutor<PotencyImages>{
    public List<PotencyImages> findByPotencyidAndDeleted(ResPotency potencyid, boolean deleted);
    public PotencyImages findByPotencyid(ResPotency id);
    public List<PotencyImages> findAllBypotencyid (ResPotency potencyid);
    public PotencyImages findByImageID(Integer ID);
}
