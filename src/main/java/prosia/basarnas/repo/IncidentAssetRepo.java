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
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.ResAsset;

/**
 *
 * @author PROSIA
 */
@Repository
public interface IncidentAssetRepo extends JpaRepository<IncidentAsset, String>, JpaSpecificationExecutor<IncidentAsset> {

    public List<IncidentAsset> findTopOneByIncidentAssetIDContaining(String incassetid);

    @Query("select MAX(incidentAssetID) FROM IncidentAsset WHERE incidentAssetID like %?1%")
    public String findAssetByMaxId(String cgk);

    @Query("Select a from IncidentAsset a where a.incident = ?1 and a.deleted = 0")
    public List<IncidentAsset> findAllByIncident(Incident incident);

    @Query("Select speed from IncidentAsset where incident.incidentid = ?1 And asset.assetid = ?2")
    public Double findSpeed(String incident, String resAsset);

    public IncidentAsset findBySpeed(Incident incident, ResAsset resAsset);

    public List<IncidentAsset> findByIncidentAndAssetAndDeletedFalse(Incident incident, ResAsset resAsset);

    public List<IncidentAsset> findByAssetType(MstAssetType assetType);

    public IncidentAsset findAllByIncidentAndAsset(Incident incident, ResAsset resAsset);

    @Query("select a from IncidentAsset a where a.incident = ?1 And a.asset = ?2 and a.deleted = ?3")
    public IncidentAsset findAllByIncident(Incident incident, ResAsset resAsset, boolean deleted);

    public IncidentAsset findByIncidentAssetID(String incAssetId);
    
    @Query("select a from IncidentAsset a where a.incident = ?1 And a.assetType = ?2 and a.deleted = ?3")
    public IncidentAsset findAssetTypeByIncident(Incident incident, MstAssetType assetType, boolean deleted);
    
    @Query("select count(a) from IncidentAsset a where a.incident = ?1 And a.assetType = ?2 and a.deleted = ?3" )
    public Long findAssetTypeByIncidentCount(Incident incident, MstAssetType assetType, boolean deleted);

    @Query("Select a from IncidentAsset a where a.incident = ?1 And a.assetType.issru in(1,2,3)")
    public List<IncidentAsset> findByIncidentAndAssetTypeIsNotSru(Incident incident);

    @Query("Select a from IncidentAsset a where a.incident = ?1 and a.deleted = 0 and a.assetType.issru in (1,2,3)")
    public List<IncidentAsset> findByIncidentAndAssetType(Incident incident);

    public List<IncidentAsset> findByIncidentAssetIDContaining(String incidentAssetID);

    @Query("SELECT MAX(incidentAssetID) FROM IncidentAsset WHERE incidentAssetID LIKE %?1%")
    public String findIncidentAssetByMaxId(String id);

    //public List<String> findSpeedByIncidentId(String id);
    /*
    @Query("select i from IncidentAsset i "
            + "where i.incident.incidentid = ?1 and i.vehicleType= ?2 And i.deleted=false And i.assetType.issru=0")
    public List<IncidentAsset> findByIncidentIdAndVehicleType(String id, String type);
    
    public List<IncidentAsset> findByIncidentAndVehicleTypeAndDeletedAndAssetType(Incident incident, String type, boolean deleted, MstAssetType asset);
     */
    @Query(value = "select a.* from INC_ASSET a where a.ASSETTYPEID in :typeId",
            nativeQuery = true)
    public List<IncidentAsset> findAllByIncidentTypeId(@Param("typeId") List<String> typeid);

    @Query("Select a from IncidentAsset a where a.incident = ?1 and a.deleted = ?2 and a.assetType.issru not in (1,2,3)")
    public List<IncidentAsset> findByIncidentAndAssetTypeSru(Incident incident,Boolean delete);
    
}
