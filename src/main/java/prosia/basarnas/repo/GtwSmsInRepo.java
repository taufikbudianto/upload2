/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.GtwSmsIn;

/**
 *
 * @author PROSIA
 */
@Repository
public interface GtwSmsInRepo extends JpaRepository<GtwSmsIn, String>, JpaSpecificationExecutor<GtwSmsIn> {
//    @Query("SELECT a.incidentid, a.latitude, a.longitude FROM Incident a WHERE a.status = 'Open'")

    @Query(value = "select b.PERSONNELNAME, b.MOBILEPHONENUMBER, a.SENDER, a.SMSINCOMINGID, a.REFERENCE, a.DELIVEREDDATE, a.MSG FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') WHERE (:nama is null OR b.PERSONNELNAME LIKE '%'||:nama||'%')"
            + "/*#sort*/ /*#pageable*/",
            countQuery = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') "
            + "WHERE (:nama is null OR b.PERSONNELNAME LIKE '%'||:nama||'%')",
            nativeQuery = true)    
    public Page<Object[]> findAllViewNama(@Param("nama") String nama, Pageable pageable);
    
    @Query(value = "select b.PERSONNELNAME, b.MOBILEPHONENUMBER, a.SENDER, a.SMSINCOMINGID, a.REFERENCE, a.DELIVEREDDATE, a.MSG FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') WHERE (:nohp is null OR b.MOBILEPHONENUMBER LIKE '%'||:nohp||'%')"
            + "/*#sort*/ /*#pageable*/",
            countQuery = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') "
            + "WHERE (:nohp is null OR b.MOBILEPHONENUMBER LIKE '%'||:nohp||'%')",
            nativeQuery = true)    
    public Page<Object[]> findAllViewNoHp(@Param("nohp") String nama, Pageable pageable);
    
    @Query(value = "select b.PERSONNELNAME, b.MOBILEPHONENUMBER, a.SENDER, a.SMSINCOMINGID, a.REFERENCE, a.DELIVEREDDATE, a.MSG FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0')"
            + "/*#sort*/ /*#pageable*/",
            countQuery = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') "
            + "",
            nativeQuery = true)    
    public Page<Object[]> findAllViewAll(Pageable pageable);
    
    @Query(value = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') WHERE (:nama is null OR b.PERSONNELNAME LIKE '%'||:nama||'%')",
            nativeQuery = true)    
    public long countAllViewNama(@Param("nama") String nama);
    
    @Query(value = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0') WHERE (:nohp is null OR b.MOBILEPHONENUMBER LIKE '%'||:nohp||'%')",
            nativeQuery = true)    
    public long countAllViewNoHp(@Param("nohp") String nama);
    
    @Query(value = "select count(*) FROM GTW_SMSINCOMING a "
            + "LEFT JOIN RES_PERSONNEL b ON b.MOBILEPHONENUMBER = REPLACE(a.SENDER,'+62','0')",
            nativeQuery = true)    
    public long countAllViewAll();
    
    
}
