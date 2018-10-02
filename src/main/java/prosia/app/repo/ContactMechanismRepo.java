/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prosia.app.model.ContactMechanism;

/**
 *
 * @author Randy
 */
@Repository
public interface ContactMechanismRepo extends JpaRepository<ContactMechanism, Integer> {
    
}
