/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.LogImages;
import java.util.List;

/**
 *
 * @author TOMY
 */
@Repository
public interface LogImagesRepo extends JpaRepository<LogImages, Integer>, JpaSpecificationExecutor<LogImages> {
    
    public List<LogImages> findByLogIDAndDeleted(IncidentLog logID,boolean deleted); 
    public LogImages findByImageID(Integer id);
}
