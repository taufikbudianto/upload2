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

/**
 *
 * @author Aris
 */
@Repository
public interface SearchAreaRepo extends JpaRepository<SearchArea, String>, JpaSpecificationExecutor<SearchArea> {

    @Query("select MAX(searchAreaID) FROM SearchArea WHERE searchAreaID like %?1%")
    public String findByMaxId(String officeCode);

    @Query("select a FROM SearchArea a WHERE a.searchAreaID like %?1%")
    public List<SearchArea> findAllById(String officeCode);
    
    public List<SearchArea> findByIncidentAndStatus(Incident incident,Boolean status);
    
    public SearchArea findAllBySearchAreaID(String searchAreaID);
}
