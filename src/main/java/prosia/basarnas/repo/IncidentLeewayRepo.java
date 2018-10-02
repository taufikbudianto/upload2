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
import prosia.basarnas.model.IndexLeeway;

/**
 *
 * @author Aris
 */
@Repository
public interface IncidentLeewayRepo extends JpaRepository<IndexLeeway, String>, JpaSpecificationExecutor<IndexLeeway>{
    
    @Query("Select distinct category from IndexLeeway order by category asc")
    public List<String> findAllByCategory();
    
    @Query("Select distinct category from IndexLeeway where indexLeewayID = ?1")
    public List<String> findAllByCategory(String id);
    
    @Query("select distinct subCategory from IndexLeeway where category = ?1 order by subCategory")
    public List<String> findAllBySubCategory(String id);
    
    @Query("select distinct subCategory from IndexLeeway where indexLeewayID = ?1 order by subCategory")
    public List<String> findAllBySubCategoryAndIndex(String id);
    
    @Query("select distinct leewayDescription from IndexLeeway where subCategory = ?1 order by leewayDescription")
    public List<String> findAllByLeewayDescription(String id);
    
    @Query("select distinct leewayDescription from IndexLeeway where indexLeewayID = ?1 order by leewayDescription")
    public List<String> findAllByLeewayDescriptionAndIndex(String id);
    
    @Query("select indexLeewayID from IndexLeeway where category = ?1 And subCategory = ?2 And leewayDescription = ?3")
    public String findAllByIndexLeewayID(String category, String subCategory, String leewayDescription);
    
    public List<String> findAllByIndexLeewayID(String id);
}
