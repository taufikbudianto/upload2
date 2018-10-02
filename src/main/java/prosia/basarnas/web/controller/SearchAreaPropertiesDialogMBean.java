/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.vertexModel;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.GSearchArea;
import prosia.basarnas.web.controller.map.googleapi.SearchAreaNode;
import prosia.basarnas.web.util.LatitudeLongitude;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class SearchAreaPropertiesDialogMBean implements InitializingBean {

    private SearchArea searchArea;
    private Incident incident;
    private int state;
    private static final int SAVE_STATE = 0;
    private static final int UPDATE_STATE = 1;
    private SearchAreaNode searchAreaNode;
    private TreeNode rootOfRoutingTree;
    private TreeNode routingTreeModel;
    private List<SearchAreaNode> searchAreaNodes;

    private String ID;
    private String Nama;
    private String Deskripsi;
    private String Start;
    private List<vertexModel> listGLatLng;
    private String pointLabel = null;
    private String latitudeVertex;
    private String longitudeVertex;

    //private LatitudeLongitude latitudeLongitude;
    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private SearchAreaRepo searchAreaRepo;

    public SearchAreaPropertiesDialogMBean() {

    }

    public SearchAreaPropertiesDialogMBean(SearchAreaNode searchAreaNode) {
        this.searchAreaNode = searchAreaNode;
        this.searchArea = searchAreaNode.getObject();
        this.incident = searchArea.getIncident();
        this.state = UPDATE_STATE;
        initModel();

    }

    //constructor sesi pengesavean
    public SearchAreaPropertiesDialogMBean(SearchArea searchArea, Incident incident, TreeNode rootRoutingTree, TreeNode routingTreeModel, List<SearchAreaNode> searchAreaNodes) {
        this.searchArea = searchArea;
        this.incident = incident;
        this.searchAreaNodes = searchAreaNodes;
        this.rootOfRoutingTree = rootRoutingTree;
        this.routingTreeModel = routingTreeModel;
        this.state = SAVE_STATE;
        initModel();
    }

    private void initModel() {

        //DefaultTableModel vertexModel = (DefaultTableModel) tableVertex.getModel();
        ID = searchArea.getSearchAreaID();
        Nama = searchArea.getName();
        Deskripsi = searchArea.getDescription();
        GLatLng searchAreaStart = GSearchArea.getStartFromSearchAreas(searchArea);
        Start = LatitudeLongitude.latitideFormatted(searchAreaStart.getLat()) + " - " + LatitudeLongitude.longitudeFormatted(searchAreaStart.getLng());

        List<GLatLng> vertexs = GSearchArea.getVertexsFromSearchArea(searchArea);
        listGLatLng = new ArrayList<>();
        int i = 1;
        for (GLatLng vertex : vertexs) {
            vertexModel model = new vertexModel();
            switch (i) {
                case 1:
                    model.setPointLabel("A");
                    break;
                case 2:
                    model.setPointLabel("B");
                    break;
                case 3:
                    model.setPointLabel("C");
                    break;
                case 4:
                    model.setPointLabel("D");
                    break;
                default:
                    break;
            }
            model.setLatitudeVertex(LatitudeLongitude.latitideFormatted(vertex.getLat()));
            model.setLongitudeVertex(LatitudeLongitude.longitudeFormatted(vertex.getLng()));

            listGLatLng.add(model);
            ++i;
        }
    }

    public void Simpan() {
        searchArea.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        if (!Nama.isEmpty() || !Nama.equals(null)) {
            searchArea.setName(Nama);
            searchArea.setIncident(incident);
            searchArea.setDescription(Deskripsi);
            searchAreaRepo.save(searchArea);

            if (searchArea != null) {
                SearchAreaNode searchAreaNode = new SearchAreaNode(searchArea, routingTreeModel) {
                    @Override
                    protected void removeFromRoutingControllerCollection() {
                        searchAreaNodes.remove(this);
                    }
                };
                searchAreaNodes.add(searchAreaNode);
                //routingTreeModel.insertNodeInto(searchAreaNode, rootOfRoutingTree, rootOfRoutingTree.getChildCount());
                addMessage("Informasi","Simpan berhasil");                
            }else{
                warnMessage("Informasi","Mohon isi nama Search Area terlebih dahulu");
            }
        }
    }

    public void Update() {
        SearchArea tempSearchArea = new SearchArea();
        tempSearchArea.setName(Nama);
        copySearchAreaPropertyToOtherSearchArea(searchArea, tempSearchArea);
        tempSearchArea.setDescription(Deskripsi);
        tempSearchArea.setModifiedBy(menuNavMBean.getUserSession().getUsername());
        searchAreaRepo.save(tempSearchArea);
        if (tempSearchArea != null) {
            searchArea.setName(tempSearchArea.getName());
            searchArea.setModifiedBy(tempSearchArea.getModifiedBy());
            searchArea.setDescription(tempSearchArea.getDescription());
            searchArea.setLastModified(tempSearchArea.getLastModified());
            searchArea.setModifiedBy(tempSearchArea.getModifiedBy());
            searchArea.setParrentID(tempSearchArea.getParrentID());
            //searchAreaNode.setUserObject(searchArea.getSearchAreaID() + " - " + searchArea.getName());

            addMessage("Informasi", "Update berhasil");
        }
    }
    
    private void copySearchAreaPropertyToOtherSearchArea(SearchArea copiedSearchArea, SearchArea pasteSearchArea) {
        pasteSearchArea.setSearchAreaID(copiedSearchArea.getSearchAreaID());
        pasteSearchArea.setArea(copiedSearchArea.getArea());
        pasteSearchArea.setCreatedBy(copiedSearchArea.getCreatedBy());
        pasteSearchArea.setIncident(copiedSearchArea.getIncident());
        pasteSearchArea.setUserSiteID(copiedSearchArea.getUserSiteID());
//        pasteSearchArea.setSearchPatterns(copiedSearchArea.getSearchPatterns());
//        pasteSearchArea.setTaskSearchAreas(copiedSearchArea.getTaskSearchAreas());
        pasteSearchArea.setParrentID(copiedSearchArea.getParrentID());
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void warnMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static void showPropertiesDialog(SearchAreaNode searchAreaNode) {
        SearchAreaPropertiesDialogMBean propertiesDialog = new SearchAreaPropertiesDialogMBean(searchAreaNode);
        //addMessage("Information",propertiesDialog);
    }

    public static void showPropertiesDialog(SearchArea searchArea, Incident incident, TreeNode root, TreeNode treeModel, List<SearchAreaNode> searchAreaNode) {
        SearchAreaPropertiesDialogMBean propertiesDialog = new SearchAreaPropertiesDialogMBean(searchArea, incident, root, treeModel, searchAreaNode);
        //addMessage("Information",propertiesDialog);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
