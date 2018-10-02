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
import prosia.basarnas.model.IndexSpeed;

/**
 *
 * @author Aris
 */
@Repository
public interface IndexSpeedRepo extends JpaRepository<IndexSpeed, String>, JpaSpecificationExecutor<IndexSpeed>{
     @Query("select correction from IndexSpeed where rescuer = ?1 and searchObject = ?2 and speed = ?3")
    public List<Double> findByRescuerAndSearchObjectAndSpeed(String rescuer, String searchObject, double speed);
}
