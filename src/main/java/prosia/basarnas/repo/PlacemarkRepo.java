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
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentPlacemark;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface PlacemarkRepo extends JpaRepository<IncidentPlacemark, String>,
        JpaSpecificationExecutor<IncidentPlacemark> {

    public List<IncidentPlacemark> findByPlacemarkIDContaining(String placemarkId);
    
    public List<IncidentPlacemark> findByIncident(Incident incident);

    public List<IncidentPlacemark> findByIncidentAndDeletedFalse(Incident incident);

    @Query("SELECT MAX(placemarkID) FROM IncidentPlacemark WHERE placemarkID LIKE %?1%")
    public String findByMaxId(String id);
    
    @Query(value="select * from INC_PLACEMARK where usersiteid = :usersite", nativeQuery = true )
    public List<IncidentPlacemark> findAllByKansarID(@Param("usersite") String usersite);
}
