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
import prosia.basarnas.model.ResPersonnelImages;
import prosia.basarnas.model.ResPersonnel;

/**
 *
 * @author Owner
 */

@Repository
public interface ResPersonnelImagesRepo extends JpaRepository<ResPersonnelImages, Integer>, JpaSpecificationExecutor<ResPersonnelImages>{
    public List<ResPersonnelImages> findByPersonnelIDAndDeleted(ResPersonnel personnelID, boolean deleted);
    
    @Query(value = "select * from RES_PERSONNEL_IMAGES person where person.PERSONNELID=:personnelID and person.ISDELETED=0",
            nativeQuery = true)
    public ResPersonnelImages findImageByPersonnelIDAndDeleted(@Param("personnelID") String personnelID);
}
