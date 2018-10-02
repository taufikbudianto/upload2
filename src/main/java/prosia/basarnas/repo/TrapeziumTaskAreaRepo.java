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
import prosia.basarnas.model.TrapeziumArea;
import prosia.basarnas.model.TrapeziumTaskArea;

/**
 *
 * @author Aris
 */
@Repository
public interface TrapeziumTaskAreaRepo extends JpaRepository<TrapeziumTaskArea, String>, JpaSpecificationExecutor<TrapeziumTaskArea>{
    
    public TrapeziumTaskArea findByTrapeziumTaskAreaID(String classid);
    
    public List<TrapeziumTaskArea> findByTrapeziumAreaAndDeletedFalse(TrapeziumArea id);
    
    public List<TrapeziumTaskArea> findAllByTrapeziumTaskAreaIDContaining(String classid);
    
    public List<TrapeziumTaskArea> findByTrapeziumAreaAndWaypoint(TrapeziumArea trapeziumArea, double id);
    
    @Query("SELECT MAX(trapeziumTaskAreaID) FROM TrapeziumTaskArea WHERE trapeziumTaskAreaID LIKE %?1%")
    public String findClassByMaxId(String id);
}
