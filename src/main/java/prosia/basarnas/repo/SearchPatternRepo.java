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
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.model.IncTaskSearchArea;

/**
 *
 * @author Aris
 */
@Repository
public interface SearchPatternRepo extends JpaRepository<IncidentSearchPattern, String>, JpaSpecificationExecutor<IncidentSearchPattern> {

    @Query("select MAX(searchPatternId) FROM IncidentSearchPattern WHERE searchPatternId like %?1%")
    public String findByMaxId(String officeCode);

    @Query("select a FROM IncidentSearchPattern a WHERE a.searchPatternId like ?1%")
    public List<IncidentSearchPattern> findAllById(String officeCode);

    public List<IncidentSearchPattern> findAllBySearchPatternIdContaining(String searchPatternID);

    @Query("select a FROM IncidentSearchPattern a WHERE a.searchArea = ?1")
    public List<IncidentSearchPattern> findBySearchArea(SearchArea searchArea);

    public List<IncidentSearchPattern> findBySearchAreaAndStatus(SearchArea searchArea, Boolean status);

    public List<IncidentSearchPattern> findBySearchAreaAndStatusAndTaskSearchAreaIsNull(SearchArea searchArea, Boolean status);

    public List<IncidentSearchPattern> findBySearchAreaAndTaskSearchAreaAndStatus(SearchArea searchArea, IncTaskSearchArea taskSearchArea, Boolean status);

}
