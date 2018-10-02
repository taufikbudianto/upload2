/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.app.repo.TaskRepo;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.IncidentRepo;

/**
 *
 * @author Owner
 */
@Service
@Transactional(readOnly = false, rollbackFor = { Exception.class })
public class IncidentPersonnelService {
    
    @Autowired
    private IncidentRepo incidentRepo;
    
    @Autowired
    private IncidentPersonnelRepo incidentPersonnelRepo;
    
    @Autowired
    private TaskRepo taskRepo;
    
    
    public void save(IncidentPersonnel personnel , List<IncidentPersonnel> listpersonel){
        
      
    }
    
}
