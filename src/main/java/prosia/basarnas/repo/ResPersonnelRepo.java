/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPotency;

/**
 *
 * @author PROSIA
 */
@Repository
public interface ResPersonnelRepo extends JpaRepository<ResPersonnel, String>, JpaSpecificationExecutor<ResPersonnel> {

    public ResPersonnel findTopOneResPersonnelBypersonnelCode (String nip);
    
    public ResPersonnel findByPersonnelID(String personelId);
    public ResPersonnel findByPersonnelCode(String code);

    @Query("select MAX(personnelID) FROM ResPersonnel WHERE personnelID like %?1%")
    public String findPersonnelByMaxId(String cgk);

    public List<ResPersonnel> findTopOneBypersonnelIDContaining(String resPersonnelId);

    @Query("select a FROM ResPersonnel a WHERE a.personnelID like %?1%")
    public List<ResPersonnel> findAllBypersonnelIDLike(String resPersonnelId);

    public List<ResPersonnel> findAllBypotencyAndIsdeleted(ResPotency potency, BigInteger isDeleted);

    @Query(value = "select count(*) from RES_PERSONNEL a where a.BIRTHDATE=:dtLahir and a.PERSONNELNAME= :name "
            + "and a.BIRTHPLACE=:birthplace and a.GENDER=:gender",
            nativeQuery = true)
    public long countCheckUpdateOrSave(@Param("dtLahir") Date dtLahir, @Param("name") String name,
            @Param("birthplace") String birthplace, @Param("gender") String gender);

    @Query(value = "select a.PERSONNELID from RES_PERSONNEL a where a.BIRTHDATE=:dtLahir and a.PERSONNELNAME= :name "
            + "and a.BIRTHPLACE=:birthplace and a.GENDER=:gender",
            nativeQuery = true)
    public String getPersonelId(@Param("dtLahir") Date dtLahir, @Param("name") String name,
            @Param("birthplace") String birthplace, @Param("gender") String gender);
    
    @Query("select a FROM ResPersonnel a WHERE a.potency = ?1")
    public ResPersonnel findPersonelByPotencyAndName(ResPotency potency);

    @Query(value = "SELECT * FROM RES_PERSONNEL personnel WHERE PERSONNEL.KANTORSARID = :idKansar "
            + " AND PERSONNEL.PERSONNELNAME= :name",
            nativeQuery = true)
    public ResPersonnel getPersonnelByNameAndIdKansar(@Param("name") String name,@Param("idKansar") String idKansar);
    
    @Query(value= "select a FROM ResPersonnel a Where a.officeSar.kantorsarid in ?1 "
                + "AND a.employmentClass is not null AND a.isdeleted != 1")
    public Page<ResPersonnel> findAllByKansarId(Collection<String> listKansar, Pageable pageable);
    
    @Query("select count(a) FROM ResPersonnel a Where a.officeSar.kantorsarid in ?1 "
         + "AND a.employmentClass is not null AND a.isdeleted != 1")
    public Long findAllByKansarId(Collection<String> listKansar);

    @Query(value= "select a FROM ResPersonnel a Where a.officeSar.kantorsarid in ?1 "
                + "AND UPPER(a.personnelName) like UPPER(CONCAT('%',?2,'%')) "
                + "AND a.employmentClass is not null AND a.isdeleted != 1")
    public Page<ResPersonnel> findAllByKansarIdAndName(Collection<String> listKansar, String nama, Pageable pageable);
    
    @Query("select count(a) FROM ResPersonnel a Where a.officeSar.kantorsarid in ?1 "
         + "AND UPPER(a.personnelName) like UPPER(CONCAT('%',?2,'%')) "
         + "AND a.employmentClass is not null AND a.isdeleted != 1")
    public Long findAllByKansarIdAndName(Collection<String> listKansar, String nama);
}
