/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import com.sun.javafx.scene.control.SelectedCellsMap;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
class SearchAreaPropertiesDialogMultiple implements InitializingBean {

    private static SearchArea[] searchArea = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static String[] SearchAreaID = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static String[] SearchAreaName = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static String[] SearchAreaDesc = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private vertexModel vertexModel;
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

    private List<SelectItem> listItemWayPoint;
    private String WayPoint;
    private int WayPointIndex;

    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private SearchAreaRepo searchAreaRepo;

    public SearchAreaPropertiesDialogMultiple() {

    }

    public SearchAreaPropertiesDialogMultiple(SearchAreaNode searchAreaNode) {
        this.searchAreaNode = searchAreaNode;
        this.searchArea[0] = searchAreaNode.getObject();
        this.incident = searchArea[0].getIncident();
        this.state = UPDATE_STATE;
        initModel(0);
    }

    //constructor sesi pengesavean
    public SearchAreaPropertiesDialogMultiple(SearchArea searchArea, Incident incident, TreeNode rootRoutingTree, TreeNode routingTreeModel, List<SearchAreaNode> searchAreaNodes, int index) {
        this.searchArea[index] = searchArea;
        this.incident = incident;
        this.searchAreaNodes = searchAreaNodes;
        this.rootOfRoutingTree = rootRoutingTree;
        this.routingTreeModel = routingTreeModel;
        this.state = SAVE_STATE;
        initModel(index);
        listItemWayPoint = new ArrayList<>();

        for (int i = 0; i <= index; i++) {
            listItemWayPoint.add(new SelectItem(i + 1));
        }
    }

    private void initModel(int index) {
        //vertexModel = (DefaultTableModel) tableVertex.getModel();

        SearchAreaID[index] = searchArea[index].getSearchAreaID();
        SearchAreaName[index] = searchArea[index].getName();
        SearchAreaDesc[index] = searchArea[index].getDescription();
        ID = SearchAreaID[index];
        Nama = SearchAreaName[index];
        Deskripsi = SearchAreaDesc[index];
        //textIncident.setText(incident.getIncidentNumber());
        GLatLng searchAreaStart = GSearchArea.getStartFromSearchAreas(searchArea[index]);
        Start = LatitudeLongitude.latitideFormatted(searchAreaStart.getLat()) + " - " + LatitudeLongitude.longitudeFormatted(searchAreaStart.getLng());
        List<GLatLng> vertexs = GSearchArea.getVertexsFromSearchArea(searchArea[index]);
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
        searchArea[WayPointIndex].setCreatedBy(menuNavMBean.getUserSession().getUsername());
        if (!Nama.isEmpty() || !Nama.equals(null)) {
            searchArea[WayPointIndex].setName(Nama);
            searchArea[WayPointIndex].setIncident(incident);
            searchArea[WayPointIndex].setDescription(Deskripsi);
            searchAreaRepo.save(searchArea[WayPointIndex]);

            if (searchArea[WayPointIndex] != null) {
                SearchAreaNode searchAreaNode = new SearchAreaNode(searchArea[WayPointIndex], routingTreeModel) {

                    @Override
                    protected void removeFromRoutingControllerCollection() {
                        searchAreaNodes.remove(this);
                    }
                };
                searchAreaNodes.add(searchAreaNode);
                //routingTreeModel.insertNodeInto(searchAreaNode, rootOfRoutingTree, rootOfRoutingTree.getChildCount());
                addMessage("Informasi", "Simpan berhasil");

            } else {
                warnMessage("Informasi", "Mohon isi nama Search Area terlebih dahulu");
            }
        }
    }

    public void Update() {
        SearchArea tempSearchArea = new SearchArea();
        tempSearchArea.setName(Nama);
        copySearchAreaPropertyToOtherSearchArea(searchArea[WayPointIndex], tempSearchArea);
        tempSearchArea.setDescription(Deskripsi);
        tempSearchArea.setModifiedBy(menuNavMBean.getUserSession().getUsername());
        searchAreaRepo.save(tempSearchArea);
        if (tempSearchArea != null) {
            searchArea[WayPointIndex].setName(tempSearchArea.getName());
            searchArea[WayPointIndex].setModifiedBy(tempSearchArea.getModifiedBy());
            searchArea[WayPointIndex].setDescription(tempSearchArea.getDescription());
            searchArea[WayPointIndex].setLastModified(tempSearchArea.getLastModified());
            searchArea[WayPointIndex].setModifiedBy(tempSearchArea.getModifiedBy());
            searchArea[WayPointIndex].setParrentID(tempSearchArea.getParrentID());
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
