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
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.TrapeziumArea;

/**
 *
 * @author Aris
 */
@Repository
public interface TrapeziumAreaRepo extends JpaRepository<TrapeziumArea, String>, JpaSpecificationExecutor<TrapeziumArea> {

    @Query("Select worksheetName from TrapeziumArea where incident.incidentid = ?1")
    public List<String> findAllByIncident(String incidentid);

    @Query("Select waypoint from TrapeziumArea where incident.incidentid = ?1 And parentID = ?2")
    public List<Double> findByWaypoint(String incidentid, String parentID);

    //public List<TrapeziumArea> findByIncidentAndTrapeziumAreaID(Incident incidentid, String trapeziumAreaID);
    public List<TrapeziumArea> findByIncidentAndParentIDAndDeletedFalse(Incident incidentid, String parentID);

    public List<TrapeziumArea> findByIncidentAndWaypoint(Incident incidentid, double id);

    public List<TrapeziumArea> findAllByWorksheetNameAndDeletedFalse(String id);

    public List<TrapeziumArea> findAllByIncidentAndWaypointAndDeletedFalse(Incident incidentid, double waypoint);

    public List<TrapeziumArea> findAllByTrapeziumAreaID(TrapeziumArea id);

    public List<TrapeziumArea> findAllByParentID(TrapeziumArea id);

    public TrapeziumArea findByTrapeziumAreaID(String id);
//    

    public TrapeziumArea findAllByTrapeziumAreaID(String id);

    public TrapeziumArea findByParentID(String id);

    public TrapeziumArea findAllByParentID(String id);

    public List<TrapeziumArea> findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(Incident incidentId, String parentId);

    public List<TrapeziumArea> findAllByTrapeziumAreaIDContaining(String classid);

    @Query("SELECT MAX(trapeziumAreaID) FROM TrapeziumArea WHERE trapeziumAreaID LIKE %?1%")
    public String findClassByMaxId(String id);

    @Query("Select trap from TrapeziumArea trap where trap.incident.incidentid = ?1")
    public List<TrapeziumArea> findTrapByIncident(String incidentid);
}
