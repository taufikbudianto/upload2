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
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.Waypoint;

/**
 *
 * @author Aris
 */
@Repository
public interface WaypointRepo extends JpaRepository<Waypoint, String>, JpaSpecificationExecutor<Waypoint> {

    @Query("select MAX(waypointID) FROM Waypoint WHERE waypointID like %?1%")
    public String findByMaxId(String officeCode);

    @Query("select a FROM Waypoint a WHERE a.waypointID like %?1%")
    public List<Waypoint> findAllById(String officeCode);

    public List<Waypoint> findBySearchPattern(IncidentSearchPattern incidentSearchPattern);
}
