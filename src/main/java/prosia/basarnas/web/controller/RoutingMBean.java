/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.service.map_js.GTaskSearchArea;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.controller.map.googleapi.SearchAreaNode;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class RoutingMBean implements InitializingBean {

    private Incident incident;
    private ArrayList<SearchAreaNode> daftarSearchAreaNodes = new ArrayList<SearchAreaNode>();
    public static int JumArrNode;

    @Autowired
    MapMBean mapMBean;

    @Autowired
    SearchAreaPropertiesDialogMBean searchAreaPropertiesDialogMBean;

    public RoutingMBean() {

    }

    public void getSearchAreaHandler() {
        SearchArea searchArea = null;
        int i;
        if (MapController.jumArea == 0) {
            searchArea = mapMBean.getController().getSearchAreaFormMap(0);
            if (searchArea == null) {
                addMessage("Informasi", "SearchArea tidak ditemukan pada Map");
            } else {
                //di sini mengeluarkan form penyimpanan area
                searchAreaPropertiesDialogMBean.showPropertiesDialog(searchArea, incident, null, null, daftarSearchAreaNodes);
            }
        } else {
            SearchAreaPropertiesDialogMultiple propertiesDialog = null;

            for (i = 0; i < MapController.jumArea; i++) {
                //buat combobox waypoint

                searchArea = mapMBean.getController().getSearchAreaFormMap(i);
                if (searchArea == null) {
                    addMessage("Informasi", "SearchArea tidak ditemukan pada Map");
                } else {
                    //di sini mengeluarkan form penyimpanan area
                    propertiesDialog = new SearchAreaPropertiesDialogMultiple(searchArea, incident, null, null, daftarSearchAreaNodes, i);
                }
            }
            if (i >= 0) {
                propertiesDialog.setWayPointIndex(0);
            }
            //SWINGFunction.setFormToCenter(propertiesDialog);
            //propertiesDialog.setVisible(true); 
        }
    }

    public void GetTaskSearchArea(){
        /*
        TaskSearchAreaNavigator taskSearchAreaNavigator = Main.mapForm.getLayerControlWarpper().getTaskSearchAreaNavigator();
                GTaskSearchArea gTaskSearchArea = taskSearchAreaNavigator.getSelectedObject();
                if (gTaskSearchArea == null) {
                    addMessage("Informasi", "Tidak ada Task Search Area yang dipilih pada Task Search Area Navigation");
                    
                } else if (daftarSearchAreaNodes.isEmpty()) {
                    addMessage("Informasi", "Task Search Area Tidak memiliki Search Area, buat Search Area terlebih dahulu");
                   
                } else {
                    try {
                        gTaskSearchArea.updatePivot();
                        gTaskSearchArea.updateWidth();
                        gTaskSearchArea.updateTilt();
                        gTaskSearchArea.updateHeight();
                        gTaskSearchArea.updateVertexs();
                    } catch (Exception ex) {
                        warnMessage("Error","Gagal mendapatkan task search area dari map");
                        return;
                    }
                    TaskSearchAreaPropertiesDialog.showTaskSearchAreaProperties(gTaskSearchArea, daftarSearchAreaNodes);
                }
*/
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void warnMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
