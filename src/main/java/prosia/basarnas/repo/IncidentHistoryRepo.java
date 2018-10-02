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
 * @author TOMY
 */
@Repository
public interface IncidentHistoryRepo extends JpaRepository<Incident, String>,JpaSpecificationExecutor<Incident>{
    
    @Query(value = "SELECT * FROM (SELECT inc.incidentid id, inc.longitude, inc.latitude,"
            + " ACOS(CASE WHEN SIN(TO_NUMBER(inc.latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "* SIN(TO_NUMBER(:latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "+ COS(TO_NUMBER(inc.latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) * "
            + " COS(TO_NUMBER(:latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "* COS(TO_NUMBER(:longitude, '999.99999999999999999999') / 180.0 * 3.141592653589793 "
            + "- TO_NUMBER(inc.longitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) > 1 THEN 1 "
            + " ELSE SIN(TO_NUMBER(inc.latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "* SIN(TO_NUMBER(:latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "+ COS(TO_NUMBER(inc.latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) "
            + "* COS(TO_NUMBER(:latitude, '999.99999999999999999999') / 180.0 * 3.141592653589793) * "
            + " COS(TO_NUMBER(:longitude, '999.99999999999999999999') / 180.0 * 3.141592653589793 "
            + "- TO_NUMBER(inc.longitude, '999.99999999999999999999') / 180.0 * 3.141592653589793)END) "
            + "* 3963.19 AS distance,"
            + " CASE WHEN TO_NUMBER(inc.longitude, '999.99999999999999999999') = "
            + "TO_NUMBER(:longitude, '999.99999999999999999999') AND "
            + "TO_NUMBER(inc.latitude, '999.99999999999999999999') = "
            + "TO_NUMBER(:latitude, '999.99999999999999999999')  THEN 0 "
            + " ELSE (90) - (ATAN2(TO_NUMBER(:latitude, '999.99999999999999999999') "
            + "- TO_NUMBER(inc.latitude, '999.99999999999999999999'),"
            + "TO_NUMBER(:longitude, '999.99999999999999999999') "
            + "- TO_NUMBER(inc.longitude, '999.99999999999999999999')) * 180.0 / 3.141592653589793) END AS angle   "
            + " FROM incident inc WHERE inc.isdeleted = 0   AND inc.longitude IS NOT NULL   AND inc.latitude IS NOT NULL ) t  "
            + " WHERE t.distance <= 50.0 ORDER BY t.distance ASC ",
            nativeQuery = true)
    public List<Object[]> getNearestIncident(@Param("longitude") String longitude, @Param("latitude") String latitude) throws Exception;
    
    @Query(value = "select inc from Incident inc where inc.incidentid in :incid")
    public List<Incident> getListIncident(@Param("incid") List<String> incid);//select * from INCIDENT inc where INC.INCIDENTID in :incid

    @Query(value = "select count(inc) from Incident inc where inc.incidentid in :incid")
    public long countListIncident(@Param("incid") List<String> incid);
}
