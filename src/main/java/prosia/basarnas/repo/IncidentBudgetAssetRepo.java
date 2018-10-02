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
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentBudgetAsset;

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentBudgetAssetRepo extends JpaRepository<IncidentBudgetAsset, String>, JpaSpecificationExecutor<IncidentBudgetAsset> {

    @Query(value = "select * from INC_BUDGET_ASSET budget join INC_ASSET asset "
            + " on budget.INCASSETID = asset.INCASSETID "
            + "where asset.INCIDENTID=:incidentid and asset.ASSETTYPEID in"
            + "(select mst.ASSETTYPEID from MST_ASSETTYPE mst where mst.ISSRU =0 and mst.ISDELETED<>1) "
            + "and asset.ISDELETED<>1 ", nativeQuery = true)
    public List<IncidentBudgetAsset> findAllByIncidentId(@Param("incidentid") String id);

    public IncidentBudgetAsset findByIncidentAsset(IncidentAsset insAsset);
    
    @Query(value="select * from INC_BUDGET_ASSET budget where (budget.ISDELETED is null "
            + " or budget.ISDELETED=0) and budget.INCASSETID in "
            + " (select asset.INCASSETID from INC_ASSET asset where asset.INCIDENTID=:id)",nativeQuery = true)
    public List<IncidentBudgetAsset>findAllByNotDeleted(@Param("id") String id);
            
    public List<IncidentBudgetAsset> findTopOneByIncBudgetAssetIdContaining(String incassetid);

    @Query("select MAX(incBudgetAssetId) FROM IncidentBudgetAsset WHERE incBudgetAssetId like %?1%")
    public String findAssetByMaxId(String cgk);
}
