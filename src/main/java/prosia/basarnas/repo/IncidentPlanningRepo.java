/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

public interface IncidentPlanningRepo extends JpaRepository<Incident, String>, JpaSpecificationExecutor<Incident> {

    @Query(value
            = " SELECT * FROM (SELECT a.WORKSHEETID,a.WORKSHEETNAME,a.INCIDENTDESCRIPTION,a.OPERATIONTIME,'Drift Calculation' as metode,a.CREATED_BY,a.LASTMODIFIED from DFC_WORKSHEET1 a where a.INCIDENTID = :incident and a.ISDELETED=0 "
            + "UNION "
            + "SELECT b.TRAPEZIUMAREAID,b.WORKSHEETNAME,b.DESKRIPSI,b.WAKTUOPERASI,'Trapesium Search Area' as metode,b.CREATED_BY,b.LASTMODIFIED from DFC_TRAPEZIUMAREA b where b.INCIDENTID = :incident and b.ISDELETED=0 and b.WAYPOINT = 1.0 "
            + "UNION "
            + "SELECT c.SEARCHAREAID,c.NAME,c.DESCRIPTION,null,'Custom' as metode,c.CREATED_BY,c.LASTMODIFIED from INC_SEARCHAREA c where c.INCIDENTID = :incident and (c.SHAPE = 'Circle' or c.SHAPE = 'Square') and c.STATUS = 1 ) order by metode "
            + "/*#sort*/ /*#pageable*/",
            countQuery = "select sum(cnt)from "
            + "(select count(*) cnt from DFC_WORKSHEET1 a where a.INCIDENTID = :incident and a.ISDELETED=0 "
            + "UNION "
            + "SELECT count(*) cnt from DFC_TRAPEZIUMAREA b where b.INCIDENTID = :incident and b.ISDELETED=0 and b.WAYPOINT = 1.0 "
            + "UNION "
            + "SELECT count(*) cnt from INC_SEARCHAREA c where c.INCIDENTID = :incident and (c.SHAPE = 'Circle' or c.SHAPE = 'Square') ) "
            + "",
            nativeQuery = true)
    public Page<Object[]> findAllViewAll(@Param("incident") String incident, Pageable pageable);

    @Query(value = "SELECT sum(cnt)from "
            + "(SELECT count(*) cnt from DFC_WORKSHEET1 a where a.INCIDENTID = :incident and a.ISDELETED=0 "
            + "UNION "
            + "SELECT count(*) cnt from DFC_TRAPEZIUMAREA b where b.INCIDENTID = :incident and b.ISDELETED=0 and b.WAYPOINT = 1.0 "
            + "UNION "
            + "SELECT count(*) cnt from INC_SEARCHAREA c where c.INCIDENTID = :incident and (c.SHAPE = 'Circle' or c.SHAPE = 'Square')) ",
            nativeQuery = true)
    public long countAllViewAll(@Param("incident") String incident);

    @Query(value
            = " SELECT * FROM (SELECT a.WORKSHEETID,a.WORKSHEETNAME,a.INCIDENTDESCRIPTION,a.OPERATIONTIME,'Drift Calculation' as metode,a.CREATED_BY,a.LASTMODIFIED from DFC_WORKSHEET1 a where a.INCIDENTID = :incident and a.ISDELETED=0 "
            + "UNION "
            + "SELECT b.TRAPEZIUMAREAID,b.WORKSHEETNAME,b.DESKRIPSI,b.WAKTUOPERASI,'Trapesium Search Area' as metode,b.CREATED_BY,b.LASTMODIFIED from DFC_TRAPEZIUMAREA b where b.INCIDENTID = :incident and b.ISDELETED=0 and b.WAYPOINT = 1.0 "
            + "UNION "
            + "SELECT c.SEARCHAREAID,c.NAME,c.DESCRIPTION,null,'Custom' as metode,c.CREATED_BY,c.LASTMODIFIED from INC_SEARCHAREA c where c.INCIDENTID = :incident and (c.SHAPE = 'Circle' or c.SHAPE = 'Square') and c.STATUS = 1 ) order by metode "
            ,nativeQuery = true)
    public List<Object[]> findAllView(@Param("incident") String incident);

}
