/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentNational;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface IncidentNationalRepo extends JpaRepository<IncidentNational, Integer>, JpaSpecificationExecutor<IncidentNational> {

    public List<IncidentNational> findByIncident(Incident incident);
    
    @Transactional
    public void deleteByIncident(Incident incident);
}
