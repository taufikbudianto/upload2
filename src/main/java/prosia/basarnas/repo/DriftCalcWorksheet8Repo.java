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
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.basarnas.model.Incident;

/**
 *
 * @author Aris
 */
@Repository
public interface DriftCalcWorksheet8Repo extends JpaRepository<DriftCalcWorksheet8, String>, JpaSpecificationExecutor<DriftCalcWorksheet8>{
    public List<DriftCalcWorksheet8> findAllByWorksheetIDContainingAndIncident(String wsID,Incident inc);
    
    @Query("SELECT MAX(worksheetID) FROM DriftCalcWorksheet8 WHERE worksheetID LIKE %?1%")
    public String findClassByMaxId(String id);
    
    public DriftCalcWorksheet8 findByWorksheetName(String worksheetName);
    
    public List<DriftCalcWorksheet8> findAllByIncidentAndDeletedFalse(Incident incident);
    
    public DriftCalcWorksheet8 findByworksheetID(String id);
}
