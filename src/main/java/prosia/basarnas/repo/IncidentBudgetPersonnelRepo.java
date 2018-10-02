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
import prosia.basarnas.model.IncidentBudgetPersonil;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.model.MstBiayaPesonnel;

/**
 *
 * @author Taufik AB
 */
@Repository
public interface IncidentBudgetPersonnelRepo extends JpaRepository<IncidentBudgetPersonil, String>, JpaSpecificationExecutor<IncidentBudgetPersonil> {

    public List<IncidentBudgetPersonil> findTopOneByBudgetPersonalIdContaining(String incassetid);

    public IncidentBudgetPersonil findByBudgetPersonalId(String id);
    
    public IncidentBudgetPersonil findByMstBiayaPersonnel(MstBiayaPesonnel biaya);

    public List<IncidentBudgetPersonil> findByIncPersonelIdAndIsDeleted(IncidentPersonnel personel,Boolean delete);
    
    @Query(value="select * from INC_BUDGET_PERSONAL budget where (budget.ISDELETED is null or budget.ISDELETED=0) "
            + " and budget.INCPERSONNELID in (select personel.INCPERSONNELID "
            + " from INC_PERSONNEL personel where personel.INCIDENTID =:id)",nativeQuery = true)
    public List<IncidentBudgetPersonil> findAllByNotDeleted(@Param("id") String id);
    
    public IncidentBudgetPersonil findByIncPersonelId(IncidentPersonnel personel);

    @Query("select MAX(budgetPersonalId) FROM IncidentBudgetPersonil WHERE budgetPersonalId like %?1%")
    public String findBudgetPersonelByMaxId(String cgk);
}
