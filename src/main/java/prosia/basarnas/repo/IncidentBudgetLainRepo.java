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
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentBiayaLain;

/**
 *
 * @author Taufik AB
 */
@Repository
public interface IncidentBudgetLainRepo extends JpaRepository<IncidentBiayaLain, String>, JpaSpecificationExecutor<IncidentBiayaLain> {

    List<IncidentBiayaLain> findByIncidentAndIsdeleted(Incident incident, Boolean delete);

    List<IncidentBiayaLain> findByIncident(Incident incident);

    public List<IncidentBiayaLain> findTopOneBybiayaLainIdContaining(String incassetid);

    @Query("select MAX(biayaLainId) FROM IncidentBiayaLain WHERE biayaLainId like %?1%")
    public String findBiayaLainByMaxId(String cgk);

    public IncidentBiayaLain findByBiayaLainId(String id);
}
