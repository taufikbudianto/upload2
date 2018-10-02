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
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelTraining;

/**
 *
 * @author Owner
 */
@Repository
public interface PersonelTrainingRepo extends JpaRepository<ResPersonnelTraining, String>, JpaSpecificationExecutor<ResPersonnelTraining> {

    public List<ResPersonnelTraining> findAllResPersonnelTrainingBypersonnel(ResPersonnel personnel);

    @Query("select MAX(personnelTrainingID) FROM ResPersonnelTraining WHERE personnelTrainingID like %?1%")
    public String findPersonnelTrainingByMaxId(String cgk);

//    public List<ResPersonnelTraining> findTopOneBypersonnelTrainingIDContaining(String resPersonnelId);
    
    @Query("select a FROM ResPersonnelTraining a WHERE a.personnelTrainingID like %?1%")
    public List<ResPersonnelTraining> findAllBypersonnelTrainingID(String resPersonnelId);

    //
    @Query(value = "SELECT * FROM RES_PERSONNELTRAINING training "
            + " where TRAINING.TRAININGDATE = :startTraining AND TRAINING.D_DIKLAT_SELESAI= :endTraining "
            + " AND TRAINING.N_PELATIHAN= :nPelatihan "
            + " AND TRAINING.PERSONNELID=(SELECT PERSONEL.PERSONNELID FROM RES_PERSONNEL personel "
            + " WHERE PERSONEL.PERSONNELNAME= :personName AND PERSONEL.POTENCYID=(SELECT POTENSI.POTENCYID "
            + "FROM RES_POTENCY potensi WHERE POTENSI.POTENCYNAME = :potName))",
            nativeQuery = true)
    public ResPersonnelTraining getPersonnelTraining(@Param("startTraining") String startTraining
            ,@Param("endTraining") String endTraining,@Param("nPelatihan") String nPelatihan,@Param("personName") String personName
            ,@Param("potName") String potName);
}
