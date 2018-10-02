package prosia.basarnas.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.DriftCalcWorksheet1;
import prosia.basarnas.model.Incident;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aris
 */
@Repository
public interface DriftCalcWorksheet1Repo extends JpaRepository<DriftCalcWorksheet1, String>, JpaSpecificationExecutor<DriftCalcWorksheet1> {
    /*
    @Query("Select worksheetName from DriftCalcWorksheet1 where incident.incidentid = ?1")
    public List<String> findAllByIncident(String incidentid);
*/
    /*
    @Query("SELECT c.incidentid, d.worksheetID FROM DriftCalcWorksheet1 d Right Join Incident c On c.incidentid = d.incidentid WHERE c.incidentid = ?1 And d.worksheetID LIKE %?2%")
    public List<DriftCalcWorksheet1> findAllByWorksheetIDAndIncidentID(String incID, String wsID);
    */
    public List<DriftCalcWorksheet1> findAllByWorksheetIDContainingAndIncident(String wsID,Incident inc);
    
    @Query("SELECT MAX(worksheetID) FROM DriftCalcWorksheet1 WHERE worksheetID LIKE %?1%")
    public String findClassByMaxId(String id);
            
    public List<DriftCalcWorksheet1> findAllByIncidentAndDeletedFalse(Incident incident);
    
    public DriftCalcWorksheet1 findByWorksheetName(String worksheetName);
    
    public DriftCalcWorksheet1 findByWorksheetID(String id);
    
    public List<DriftCalcWorksheet1> findByIncidentAndDeleted(Incident incident,Boolean delete);
}
