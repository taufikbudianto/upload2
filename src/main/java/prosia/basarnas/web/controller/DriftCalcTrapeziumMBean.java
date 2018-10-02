/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.basarnas.model.Incident;
import prosia.basarnas.repo.IncidentRepo;
//import prosia.basarnas.repo.TrapeziumTaskAreaRepo;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class DriftCalcTrapeziumMBean implements InitializingBean {

    private Incident incident;
    private List<Incident> listIncident;
   
//    @Autowired
//    private TrapeziumTaskAreaRepo trapeziumTaskAreaRepo;

    @Autowired
    private IncidentRepo incidentRepo;
     
    @Autowired
    private IncidentMBean incidentMBean;
        
    public DriftCalcTrapeziumMBean() {

    }
    
    public String constIncidentID(){
        return incident.getIncidentid();
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        incident = null;
        incident = incidentMBean.getIncident();
    }

}
