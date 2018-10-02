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
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.model.IncTaskSearchArea;

/**
 *
 * @author Aris
 */
@Repository
public interface IncTaskSearchAreaRepo extends JpaRepository<IncTaskSearchArea, String>, JpaSpecificationExecutor<IncTaskSearchArea> {

    @Query("select MAX(taskSearchAreaID) FROM IncTaskSearchArea WHERE taskSearchAreaID like %?1%")
    public String findByMaxId(String officeCode);

    @Query("select a FROM IncTaskSearchArea a WHERE a.taskSearchAreaID like %?1%")
    public List<IncTaskSearchArea> findAllById(String officeCode);

    public List<IncTaskSearchArea> findBySearchAreaAndStatus(SearchArea searchArea, Boolean status);

    @Query("select a FROM IncTaskSearchArea a WHERE a.searchArea.incident = ?1 and a.status = ?2")
    public List<IncTaskSearchArea> findByIncident(Incident incident, Boolean status);

}
