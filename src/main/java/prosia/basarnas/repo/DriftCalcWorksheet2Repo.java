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
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;

/**
 *
 * @author Aris
 */
@Repository
public interface DriftCalcWorksheet2Repo extends JpaRepository<DriftCalcWorksheet2, String>, JpaSpecificationExecutor<DriftCalcWorksheet2> {
  
    public List<DriftCalcWorksheet2> findAllByWorksheetIDContainingAndIncident(String wsID,Incident inc);
    
    @Query("SELECT MAX(worksheetID) FROM DriftCalcWorksheet2 WHERE worksheetID LIKE %?1%")
    public String findClassByMaxId(String id);
    
    public DriftCalcWorksheet2 findByWorksheetName(String worksheetName);
    
    public List<DriftCalcWorksheet2> findAllByIncidentAndDeletedFalse(Incident incident);
    
    public DriftCalcWorksheet2 findByworksheetID(String id);
    
    public DriftCalcWorksheet2 findByIncidentAndSearchPlatform(String incident, String Platform);
    
}
