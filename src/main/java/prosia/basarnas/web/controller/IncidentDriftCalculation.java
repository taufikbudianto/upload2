/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.basarnas.model.Incident;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentDriftCalculationRepo;
import prosia.basarnas.repo.IncidentLeewayRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.service.map_js.TaskSearchAreaController;
//import prosia.basarnas.service.MyFile;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class IncidentDriftCalculation implements InitializingBean {

    private Incident incident;
    private List<Incident> listIncident;
    
    @Autowired
    private IncidentDriftCalculationRepo incidentDriftCalculationRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private IncidentMBean incidentMBean;
    
    @Autowired
    private IncidentPlanningMBean incidentPlanningMBean;

     
    public IncidentDriftCalculation() {
        
    }

    public void btnClose() {
        try{
//            incidentMBean.setIsDriftCalculation(false);
            incidentPlanningMBean.setIsDriftCalculation(false);
            incidentPlanningMBean.setIsMonteCarlo(false);

//            incidentMBean.prepareShowIncidentDetail(2, incident);
        }catch(Exception ex){
            System.out.println("exception :"+ex.getMessage());
        }
        
//        incidentMBean.prepareShowIncidentDetail(2, incident);
//        incident = null;
    }

    public String constIncidentID() {
        return incident.getIncidentid();
    }

    public void SimpanTaskSearchArea(){
       
    }
    
    public void OpenTaskSearchArea(){
        
    }
    
    
        
    @Override
    public void afterPropertiesSet() throws Exception {
        incident = null;
        incident = incidentMBean.getIncident();
    }
    
}
