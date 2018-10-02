/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.IncidentAsw;

/**
 *
 * @author Ismail
 */
@Repository
public interface IncidentAswRepo extends JpaRepository<IncidentAsw, String>, JpaSpecificationExecutor<IncidentAsw> {

    @Query("SELECT MAX(aswId) FROM IncidentAsw WHERE aswId LIKE %?1%")
    public String findIncidentAswByMaxId(String id);
}
