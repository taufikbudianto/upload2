/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo.ws.cilent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.ws.cilent.SsSiagaLaut;

/**
 *
 * @author Taufik
 */
@Repository
public interface SsSiagaLautRepo extends JpaRepository<SsSiagaLaut, String>, JpaSpecificationExecutor<SsSiagaLaut>{
    
}