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
import prosia.basarnas.model.DriftCalcWorksheet3;
import prosia.basarnas.model.Incident;

/**
 *
 * @author Aris
 */
@Repository
public interface DriftCalcWorksheet3Repo extends JpaRepository<DriftCalcWorksheet3, String>, JpaSpecificationExecutor<DriftCalcWorksheet3> {
    
   
    public List<DriftCalcWorksheet3> findAllByWorksheetIDContainingAndIncident(String wsID,Incident inc);
    
    @Query("SELECT MAX(worksheetID) FROM DriftCalcWorksheet3 WHERE worksheetID LIKE %?1%")
    public String findClassByMaxId(String id);
    
    public DriftCalcWorksheet3 findByWorksheetName(String worksheetName);
    
    public List<DriftCalcWorksheet3> findAllByIncidentAndDeletedFalse(Incident incident);
    
    public DriftCalcWorksheet3 findByworksheetID(String id);
}