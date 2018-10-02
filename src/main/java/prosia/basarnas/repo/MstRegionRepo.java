/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstRegion;

/**
 *
 * @author Owner
 */
@Repository
public interface MstRegionRepo  extends JpaRepository<MstRegion, String> {
    
    @Query(value = "select v.name from MstRegion v ")
    public List<String> findAllRegion();
    
}
