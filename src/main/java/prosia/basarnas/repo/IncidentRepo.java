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

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentRepo extends JpaRepository<Incident, String>, JpaSpecificationExecutor<Incident> {

    @Query("SELECT a.incidentid, a.latitude, a.longitude FROM Incident a WHERE a.status = 'Open'")
    public List<Incident> findAllIncidentOpen();

    public List<Incident> findByStatus(String status);

    public List<Incident> findAllByIncidentidContaining(String incidentId);

    public Incident findAllByIncidentid(String incidentId);

    @Query("SELECT MAX(incidentid) FROM Incident WHERE incidentid LIKE %?1%")
    public String findIncidentByMaxId(String id);

    @Query(value = "select * from (SELECT t.id, t.longitude, t.latitude , "
            + "SQRT(POWER(TO_NUMBER(t.longitude, '999.99999999999999999') - TO_NUMBER(:longitude, '999.99999999999999999'), 2) "
            + "+ POWER(TO_NUMBER(t.latitude, '999.99999999999999999') - TO_NUMBER(:latitude, '999.99999999999999999'), 2)) distance "
            + "FROM (SELECT kansar.kantorsarid id, longitude,latitude FROM mst_kantorsar kansar "
            + "WHERE kansar.isdeleted = 0 AND kansar.longitude IS NOT NULL "
            + "AND kansar.latitude IS NOT NULL and kansar.KANTORSARID not in('BKL','DOH')) t ORDER BY distance ASC) where rownum<=3 ",
            nativeQuery = true)
    public List<Object[]> getNearestKansar(@Param("longitude") String longitude, @Param("latitude") String latitude);

    @Query(value = "SELECT t.id, t.longitude, t.latitude ,"
            + "SQRT(POWER(t.longitude - :longitude, 2) + POWER(t.latitude - :latitude, 2)) distance "
            + "FROM (SELECT kansar.kantorsarid id, longitude,latitude FROM mst_kantorsar kansar "
            + "WHERE kansar.isdeleted = 0 AND kansar.longitude IS NOT NULL "
            + "AND kansar.latitude IS NOT NULL and rownum<=3) t ORDER BY distance ASC ",
            nativeQuery = true)
    public long countKansar(@Param("longitude") String longitude, @Param("latitude") String latitude);
    
    @Query(value = "select * from INCIDENT inc where inc.STATUS='Open' and inc.LONGITUDE is not null and inc.LATITUDE is not null",nativeQuery = true)
    public List<Incident> findAllIncidentOpenLatLongNotNull();
    
    @Query(value="select * from INCIDENT where USERSITEID = :usersite", nativeQuery = true )
    public List<Incident> findAllByKansarID(@Param("usersite") String usersite);

}
