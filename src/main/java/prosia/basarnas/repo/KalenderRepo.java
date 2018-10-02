/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.CPKalender;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface KalenderRepo extends JpaRepository<CPKalender, Integer>,
         JpaSpecificationExecutor<CPKalender> {
    
    @Query(value = "SELECT CAL.*  FROM CP_KALENDER Cal WHERE TO_CHAR(CAL.CAL_MULAI, 'YYYY')=:tahun ",
            nativeQuery = true)
    public List<CPKalender>getAllDataByYear(@Param("tahun")String tanggal1);
}