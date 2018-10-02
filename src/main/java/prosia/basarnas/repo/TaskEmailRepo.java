/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import prosia.basarnas.model.TaskEmail;

/**
 *
 * @author PROSIA
 */
public interface TaskEmailRepo extends JpaRepository<TaskEmail, String>, JpaSpecificationExecutor<TaskEmail>{
    
}
