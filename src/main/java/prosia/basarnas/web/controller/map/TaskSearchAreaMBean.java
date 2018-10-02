/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.repo.IncTaskSearchAreaRepo;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.web.controller.MapMBean;
import prosia.basarnas.web.model.Document;
import prosia.basarnas.web.model.Vertexs;
import prosia.basarnas.web.model.map.TaskSearchArea;

/**
 *
 * @author Ismail
 */
@Component
@Scope("view")
@Data
public class TaskSearchAreaMBean implements Serializable {
    
    @Autowired
    private MapMBean mapMBean;
    @Autowired
    private IncTaskSearchAreaRepo taskSearchAreaRepo;
    @Autowired
    private SearchAreaRepo searchAreaRepo;
    private List<SearchArea> listSearchArea;
    private List<Vertexs> listVertexs;
    private TreeNode treeTaskArea;
    private TreeNode selectedTreeTaskArea;
    private List<IncTaskSearchArea> listIncTaskArea;
    private List<IncTaskSearchArea> selectionListIncTaskArea;
    private Double height;
    private Double width;
    private Boolean isRendered;
    
    @PostConstruct
    public void initTreeTaskArea() {
        isRendered = false;
        listIncTaskArea = new ArrayList<>();
        selectionListIncTaskArea = new ArrayList<>();
        treeTaskArea = new DefaultTreeNode(new Document("Files", null, "Files"), null);
        List<TaskSearchArea> listTaskSearchArea = mapMBean.getListTaskSearchArea();
        if (!listTaskSearchArea.isEmpty()) {
            for (TaskSearchArea tsa : mapMBean.getListTaskSearchArea()) {
                TreeNode taskArea = new DefaultTreeNode("tasksearcharea",
                        new Document(tsa.getName(), tsa, "tasksearcharea"), treeTaskArea);
            }
        }
        if (!listTaskSearchArea.isEmpty()) {
            isRendered = true;
        }
    }
    
    public void showDialogSave() {
        listSearchArea = searchAreaRepo.findByIncidentAndStatus(mapMBean.getIncident(), Boolean.TRUE);
        
        if (listSearchArea.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Task Search Area Tidak memiliki Searh Area, buat Search Area terlebih dahulu.");
            return;
        }
        Document d = (Document) selectedTreeTaskArea.getData();
        TaskSearchArea tsa = (TaskSearchArea) d.getObjClass();
        mapMBean.setIncTaskSearchArea(new IncTaskSearchArea());
        mapMBean.getIncTaskSearchArea().setName(tsa.getName());
        height = tsa.getHeight();
        width = tsa.getWidth();
        
        listVertexs = new ArrayList<>();
        int seq = 1;
        for (LatLng ll : tsa.getVertexs()) {
            Vertexs vertexs = new Vertexs();
            vertexs.setLatitude(String.valueOf(ll.getLat()));
            vertexs.setLongitude(String.valueOf(ll.getLng()));
            vertexs.setSequence(String.valueOf(seq++));
            listVertexs.add(vertexs);
            
        }
        mapMBean.getIncTaskSearchArea().setArea(generateJsonArea(tsa).toString());
        mapMBean.getIncTaskSearchArea().setType(tsa.getTypeTaskArea());
        RequestContext.getCurrentInstance().reset("idDialogSaveTaskSearchArea");
        RequestContext.getCurrentInstance().update("idDialogSaveTaskSearchArea");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveTaskSearchArea').show()");
    }
    
    public void saveTaskSearchArea() {
        if (mapMBean.getIncTaskSearchArea().getTaskSearchAreaID() == null) {
            mapMBean.getIncTaskSearchArea().setTaskSearchAreaID(generateIdSeq());
        }
        Document d = (Document) selectedTreeTaskArea.getData();
        TaskSearchArea tsa = (TaskSearchArea) d.getObjClass();
        mapMBean.getIncTaskSearchArea().setJsonMap(generateJsonMap(tsa).toString());
        mapMBean.getIncTaskSearchArea().setStatus(Boolean.TRUE);
        taskSearchAreaRepo.save(mapMBean.getIncTaskSearchArea());
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Simpan Berhasil");
        mapMBean.initTreeRouting();
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveTaskSearchArea').hide()");
    }
    
    public void removeTaskSearchArea() {
        System.out.println("removeTaskSearchArea");
        Document d = (Document) selectedTreeTaskArea.getData();
        TaskSearchArea tsa = (TaskSearchArea) d.getObjClass();
        mapMBean.getListTaskSearchArea().remove(tsa);
        initTreeTaskArea();
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
    }
    
    public void clearTaskSearchArea() {
        mapMBean.getListTaskSearchArea().clear();
        initTreeTaskArea();
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
    }
    
    public void showAllTaskArea() {
        if (treeTaskArea == null || treeTaskArea.getChildren().isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Task Search Area tidak ada, buat Task Search Area terlebih dahulu.");
            return;
        }
        listSearchArea = searchAreaRepo.findByIncidentAndStatus(mapMBean.getIncident(), Boolean.TRUE);
        if (listSearchArea.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Task Search Area Tidak memiliki Searh Area, buat Search Area terlebih dahulu.");
            return;
        }
        listIncTaskArea = new ArrayList<>();
        for (TreeNode tn : treeTaskArea.getChildren()) {
            Document d = (Document) tn.getData();
            TaskSearchArea tsa = (TaskSearchArea) d.getObjClass();
            IncTaskSearchArea incTaskSearchArea = new IncTaskSearchArea();
            incTaskSearchArea.setTaskSearchAreaID(UUID.randomUUID().toString());
            incTaskSearchArea.setName(tsa.getName());
            incTaskSearchArea.setSearchArea(listSearchArea.get(0));
            incTaskSearchArea.setArea(generateJsonArea(tsa).toString());
            incTaskSearchArea.setType(tsa.getTypeTaskArea());
            incTaskSearchArea.setJsonMap(generateJsonMap(tsa).toString());
            listIncTaskArea.add(incTaskSearchArea);
        }
        RequestContext.getCurrentInstance().reset("idDialogAllTask");
        RequestContext.getCurrentInstance().update("idDialogAllTask");
        RequestContext.getCurrentInstance().execute("PF('showDialogAllTask').show()");
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void saveAllTaskArea() {
        if (!selectionListIncTaskArea.isEmpty()) {
            for (IncTaskSearchArea taskSearchArea : selectionListIncTaskArea) {
                taskSearchArea.setTaskSearchAreaID(generateIdSeq());
                taskSearchArea.setStatus(Boolean.TRUE);
                taskSearchAreaRepo.save(taskSearchArea);
            }
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Simpan Berhasil");
            mapMBean.initTreeRouting();
            RequestContext.getCurrentInstance().update("idPanelMapSearch");
            RequestContext.getCurrentInstance().execute("PF('showDialogAllTask').hide()");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Tidak ada data yang dipilih.");
        }
    }
    
    private JSONObject generateJsonArea(TaskSearchArea tsa) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        int seq = 1;
        for (LatLng ll : tsa.getVertexs()) {
            JSONObject areajson = new JSONObject();
            areajson.put("sequence", seq++);
            areajson.put("lng", Double.valueOf(ll.getLng()));
            areajson.put("lat", Double.valueOf(ll.getLat()));
            array.put(areajson);
        }
        json.put("area", array);
        json.put("width", tsa.getWidth());
        json.put("height", tsa.getHeight());
        json.put("pivot", new JSONObject(tsa.getUnrotatePivot()));
        json.put("trackSpacing", tsa.getTrackSpacing());
        json.put("tiltDrift", tsa.getTiltDrift());
        json.put("ParrentID", tsa.getParrentID());
        json.put("childID", tsa.getChildID());
        return json;
    }
    
    private JSONObject generateJsonMap(TaskSearchArea tsa) {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonOverlay = new JSONArray();
        JSONObject jsonArea = new JSONObject();
        jsonArea.put("type", "Polygon");
        jsonArea.put("strokeColor", "#00FF00");
        jsonArea.put("fillColor", "#000000");
        jsonArea.put("strokeOpacity", 3);
        jsonArea.put("strokeWeight", 3);
        jsonArea.put("fillOpacity", 0.1);
        JSONArray array = new JSONArray();
        for (LatLng ll : tsa.getVertexs()) {
            array.put(new JSONObject(ll));
        }
        jsonArea.put("paths", array);
        jsonOverlay.put(jsonArea);
        jsonObj.put("overlays", jsonOverlay);
        return jsonObj;
    }
    
    private String generateIdSeq() {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<IncTaskSearchArea> ias = taskSearchAreaRepo.findAllById(mapMBean.getOfficeCode());
        String id = null;
        if (ias.isEmpty()) {
            id = mapMBean.getOfficeCode() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = taskSearchAreaRepo.findByMaxId("CGK");
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        break;
                    case 2:
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        nextval = "" + next;
                        break;
                    default:
                        break;
                }
                id = mapMBean.getOfficeCode() + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = mapMBean.getOfficeCode() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }
    
    private void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }
}
