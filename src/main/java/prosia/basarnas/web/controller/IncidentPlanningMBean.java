/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.repo.IncidentPlanningRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.web.controller.map.SearchAreaMBean;

/**
 *
 * @author TOMY
 */
@Controller
@Scope("view")

public @Data
class IncidentPlanningMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Incident incident;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean isDriftCalculation;
    private Boolean isTrapesium;
    private Boolean isMonteCarlo;
    private Boolean disabled;
    private Incident currIncident;
    private SearchArea searchArea;
    private List<String> allData;
    private LazyDataModelJPA<Object[]> lazyDataModelJPAallData;
    @Autowired
    private IncidentMBean incidentMBean;

    @Autowired
    private SearchAreaMBean searchAreaMBean;

    @Autowired
    private MapMBean mapMBean;

    @Autowired
    private IncidentPlanningRepo planningRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private SearchAreaRepo searchAreaRepo;

    public IncidentPlanningMBean() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        allData = new ArrayList<>();
//        kansar = 0;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setData();
        currIncident = incidentMBean.getIncident();
        disabled = incidentMBean.getDisableForm();
        incident = incidentRepo.findAllByIncidentid(currIncident.getIncidentid());
        if (currIncident != null) {
            lazyDataModelJPAallData = new LazyDataModelJPA(planningRepo) {
                long pageNumber;

                @Override
                protected Page getDatas(PageRequest request) {

                    return planningRepo.findAllViewAll(currIncident.getIncidentid(), request);
                }

                @Override
                protected long getDataSize() {
                    return planningRepo.countAllViewAll(currIncident.getIncidentid());
                }
            };
        }
    }

    public void setData() {
        isDriftCalculation = false;
        isTrapesium = false;
        isMonteCarlo = false;
    }

    public void showDriftCalculation() {
        //System.out.println("masuk");
        isDriftCalculation = true;
        
    }
    
    public void showMonteCarlo() {
        //System.out.println("masuk");
        isMonteCarlo = true;
        isDriftCalculation = false;
    }

    public void prepareShowIncidentDetail(Incident inc) {
        logger.debug("INCIDENT : {}", inc);
        //logger.debug("STATUS : {}", status);
        //showIncidentDetail = status;
        isTrapesium = true;
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void hideTrapesium() {
        isTrapesium = false;
    }
    
    //fungsinya sudah ada search area id dari data yang ada di datatable
    public void viewMapPlanCustom() {
        searchArea = searchAreaRepo.findAllBySearchAreaID((String) getRequestParam("searchAreaId"));
        mapMBean.setIncident(incident);
        mapMBean.setSearchArea(searchArea);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/map/map/list.xhtml?search=CUSTOM"
                + "&incident="
                + incident.getIncidentid() + "', "
                + "'_blank')");
    }
    
    //fungsinya belum dapat search area id
    public void viewMapPlanButtonCustom() {
        mapMBean.setIncident(incident);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/map/map/list.xhtml?search=CUSTOM"
                + "&incident="
                + incident.getIncidentid() + "', "
                + "'_blank')");
    }

}
