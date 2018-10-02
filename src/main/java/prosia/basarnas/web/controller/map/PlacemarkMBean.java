/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncidentPlacemark;
import prosia.basarnas.repo.PlacemarkRepo;
import prosia.basarnas.web.controller.MapMBean;
import prosia.basarnas.web.model.Document;
import prosia.basarnas.web.model.Theme;
import prosia.basarnas.web.model.map.Placemark;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
@Data
public class PlacemarkMBean extends AbstractManagedBean implements InitializingBean {

    private List<IncidentPlacemark> placemarkList;
    private IncidentPlacemark placemark;
    private Coordinate latPlacemark;
    private Coordinate lngPlacemark;
    private Double latitude;
    private Double longitude;
    private TreeNode treePlacemark;
    private TreeNode[] selectedTreePlacemark;
    private TreeNode treePlacemarkInc;
    private TreeNode[] selectedTreePlacemarkInc;
    private Marker marker;
    private String description;
    private String icon;
    private Boolean fromIncident;
    private String search;
    private List<Theme> listIconPlacemark;
    private Theme theme;
    @Value("${uploadFolder}")
    private String direktory;

    @Autowired
    private MapMBean mapMBean;
    @Autowired
    private PlacemarkRepo placemarkRepo;

    public PlacemarkMBean() {
        placemarkList = new ArrayList<>();
        latPlacemark = new Coordinate();
        lngPlacemark = new Coordinate();
        fromIncident = false;
    }

    public void showDialog() {
        placemark = new IncidentPlacemark();
        theme = new Theme();
        latPlacemark = new Coordinate();
        lngPlacemark = new Coordinate();
        latPlacemark.setDecimalDegrees(latitude);
        latPlacemark.setType(true);
        latPlacemark.setCounter(1);
        latPlacemark.converter();
        latPlacemark.setCounter(3);
        lngPlacemark.setDecimalDegrees(longitude);
        lngPlacemark.setType(false);
        lngPlacemark.setCounter(1);
        lngPlacemark.converter();
        lngPlacemark.setCounter(3);
        RequestContext.getCurrentInstance().reset("idDialogPlacemark");
        RequestContext.getCurrentInstance().update("idDialogPlacemark");
        RequestContext.getCurrentInstance().execute("PF('showDialogPlacemark').show()");
    }

    public void addMarker() {
        icon = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestContextPath().concat("/resources/basarnas/images/placemark/" + theme.getName());
        description = new StringBuilder("<b>Placemark Insiden</b><br>")
                .append("<br>Nama Placemark : ").append(placemark.getPlacemarkName())
                .append("<br>Latitude : ").append(
                latPlacemark.getConvertDdToDms(String.valueOf(latitude), Boolean.TRUE))
                .append("<br>Longitude : ").append(
                lngPlacemark.getConvertDdToDms(String.valueOf(longitude), Boolean.FALSE)).toString();
        if (placemark.getPlacemarkID() == null) {
            Marker marker = new Marker(new LatLng(latitude, longitude),
                    null, description, icon);
            marker.setDraggable(true);
            mapMBean.getMapModel().addOverlay(marker);
            placemark.setPlacemarkID(marker.getId());
            placemark.setLatitude(latitude.toString());
            placemark.setLongitude(longitude.toString());
            placemark.setImage(theme.getName());
            placemark.setMarkerId(marker.getId());
            placemarkList.add(placemark);
            createTreePlacemark(placemark);
        } else {
            //perulangan untuk update marker
            for (Marker m : mapMBean.getMapModel().getMarkers()) {
                //jika placemark yang diedit sama dengan markernya
                //latitude longitude nya mengalami perubahan ataupun tidak
                if (placemark.getMarkerId().equals(m.getId())) {
                    //marker lama dihilangkan dari map
                    m.setVisible(false);
                    //buat marker baru
                    Marker mkr = new Marker(new LatLng(
                            Double.valueOf(placemark.getLatitude()),
                            Double.valueOf(placemark.getLongitude())),
                            null, description, icon);
                    mkr.setDraggable(true);
                    mapMBean.getMapModel().addOverlay(mkr);

                    Integer index = 0;
                    //perulangan untuk update list
                    List<IncidentPlacemark> listPlacemark = null;
                    if (!fromIncident) {
                        listPlacemark = placemarkList;
                    } else {
                        listPlacemark = placemarkRepo.
                                findByIncidentAndDeletedFalse(mapMBean.getIncident());
                    }
                    for (IncidentPlacemark pMark : listPlacemark) {
                        //jika placemark yang diedit sama dengan yang ada di list
                        if (placemark.getMarkerId().equals(pMark.getMarkerId())) {
                            placemark.setMarkerId(mkr.getId());
                            if (!fromIncident) {
                                placemarkList.set(index, placemark);
                                createTreePlacemark(placemark);
                            } else {
                                placemarkRepo.save(placemark);
                                createTreePlacemarkInc();
                            }
                            break;
                        }
                        index++;
                    }
                    break;
                }
            }
        }
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Placemark berhasil disimpan");
        RequestContext.getCurrentInstance().update("idMap");
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().execute("PF('showDialogPlacemark').hide()");
    }

    public void removePlacemark() {
        System.out.println("placemark : " + placemark.getPlacemarkID());
        placemarkList.remove(placemark);
        for (Marker mark : mapMBean.getMapModel().getMarkers()) {
            if (mark.getId().equals(placemark.getMarkerId())) {
                mark.setVisible(false);
                break;
            }
        }
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Berhasil", "Placemark berhasil dihapus");
        createTreePlacemark(null);
        RequestContext.getCurrentInstance().update("idMap");
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
    }

    public void removePlacemarkFromIncident() {
        placemark.setDeleted(true);
        placemarkRepo.save(placemark);
        for (Marker mark : mapMBean.getMapModel().getMarkers()) {
            if (mark.getId().equals(placemark.getMarkerId())) {
                mark.setVisible(false);
                break;
            }
        }
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Berhasil", "Placemark berhasil dihapus");
        createTreePlacemarkInc();
        RequestContext.getCurrentInstance().update("idMap");
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
    }

    public void editPlacemark(Boolean fromIncident) {
        this.fromIncident = fromIncident;
        if (marker != null && marker.getId().equals(placemark.getMarkerId())) {
            System.out.println("lat lama : " + placemark.getLatitude());
            System.out.println("lng lama : " + placemark.getLongitude());
            placemark.setLatitude(String.valueOf(latitude));
            placemark.setLongitude(String.valueOf(longitude));
            System.out.println("lat baru : " + placemark.getLatitude());
            System.out.println("lng baru : " + placemark.getLongitude());
        }
        latPlacemark = new Coordinate();
        lngPlacemark = new Coordinate();
        latPlacemark.setDecimalDegrees(Double.valueOf(placemark.getLatitude()));
        latPlacemark.setType(true);
        latPlacemark.setCounter(1);
        latPlacemark.converter();
        latPlacemark.setCounter(3);
        lngPlacemark.setDecimalDegrees(Double.valueOf(placemark.getLongitude()));
        lngPlacemark.setType(false);
        lngPlacemark.setCounter(1);
        lngPlacemark.converter();
        lngPlacemark.setCounter(3);
        RequestContext.getCurrentInstance().reset("idDialogPlacemark");
        RequestContext.getCurrentInstance().update("idDialogPlacemark");
        RequestContext.getCurrentInstance().execute("PF('showDialogPlacemark').show()");
    }

    public void savePlacemark() {
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Berhasil", "Placemark berhasil disimpan");
    }

    public void updatePlacemark() {
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Berhasil", "Placemark berhasil diupdate");
    }

    public void savePlacemarkToIncident() {
        if (treePlacemark == null || treePlacemark.getChildCount() == 0
                || selectedTreePlacemark.length == 0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan", "TIdak ada placemark yang disimpan");
        } else {
            for (TreeNode node : selectedTreePlacemark) {
                Document dc = (Document) node.getData();
                IncidentPlacemark ipl = (IncidentPlacemark) dc.getObjClass();
                ipl.setPlacemarkID(generateIdSeq());
                ipl.setIncident(mapMBean.getIncident());
                ipl.setDeleted(false);
                ipl.setDateCreated(new Date());
                ipl.setCreatedBy(getCurrentUser().getUsername());
                ipl.setUserSiteID("CGK");
                placemarkRepo.save(ipl);
                placemarkList.remove(ipl);
            }
            createTreePlacemark(null);
            createTreePlacemarkInc();
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Berhasil", "Placemark berhasil disimpan ke insiden");
        }
    }

    public void movePlacemark(IncidentPlacemark placemark, Placemark p) {
        placemark.setPlacemarkID(generateIdSeq());
        placemark.setPlacemarkName(p.getName());
        placemark.setImage(p.getImage());
        placemark.setIncident(mapMBean.getIncident());
        placemark.setDeleted(false);
        placemark.setDescription(p.getDescription());
        placemark.setLatitude(String.valueOf(p.getPosisi().getLat()));
        placemark.setLongitude(String.valueOf(p.getPosisi().getLng()));
        placemark.setUserSiteID("CGK");
    }

    public void actionLatPlacemark() {
        latPlacemark.converter();
        latPlacemark.setCounter(latPlacemark.getCounter() + 1);
        if (latPlacemark.getCounter() > 3) {
            latPlacemark.setCounter(1);
        }
    }

    public void actionLngPlacemark() {
        lngPlacemark.converter();
        lngPlacemark.setCounter(lngPlacemark.getCounter() + 1);
        if (lngPlacemark.getCounter() > 3) {
            lngPlacemark.setCounter(1);
        }
    }

    private String generateIdSeq() {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<IncidentPlacemark> ias = placemarkRepo.findByPlacemarkIDContaining("CGK");
        String id = null;
        if (ias.isEmpty()) {
            id = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = placemarkRepo.findByMaxId("CGK");
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
                id = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }

    public void createTreePlacemark(IncidentPlacemark ip) {
        treePlacemark = new CheckboxTreeNode(new Document(null, null, ip, null), null);
        for (IncidentPlacemark placemark : placemarkList) {
            TreeNode treeNode = new CheckboxTreeNode(
                    new Document(placemark.getPlacemarkName(), true, placemark, ""),
                    treePlacemark);
            treeNode.setSelected(true);
        }
    }

    public void createTreePlacemarkInc() {
        treePlacemarkInc = new CheckboxTreeNode(new Document(null, null, null, null), null);
        for (IncidentPlacemark placemark
                : placemarkRepo.findByIncidentAndDeletedFalse(mapMBean.getIncident())) {
            TreeNode treeNode = new CheckboxTreeNode(
                    new Document(placemark.getPlacemarkName(), true, placemark, ""),
                    treePlacemarkInc);
            treeNode.setSelected(true);
        }
    }

    public void showPlacemarkProperties() {
        Document doc = (Document) getRequestParam("obj");
        placemark = (IncidentPlacemark) doc.getObjClass();
        for (Marker mark : mapMBean.getMapModel().getMarkers()) {
            if (!doc.getSelected() && mark.getId().equals(placemark.getPlacemarkID())) {
                mark.setVisible(false);
            } else if (doc.getSelected() && mark.getId().equals(placemark.getPlacemarkID())) {
                mark.setVisible(true);
            }
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        Document doc = (Document) event.getTreeNode().getData();
        placemark = (IncidentPlacemark) doc.getObjClass();
        for (Marker mark : mapMBean.getMapModel().getMarkers()) {
            if (placemark.getMarkerId() != null
                    && event.getTreeNode().isSelected()
                    && placemark.getMarkerId().equals(mark.getId())) {
                mark.setVisible(true);
                break;
            }
        }
        //recallPolylineSearchArea();
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        Document doc = (Document) event.getTreeNode().getData();
        placemark = (IncidentPlacemark) doc.getObjClass();
        for (Marker mark : mapMBean.getMapModel().getMarkers()) {
            if (placemark.getMarkerId() != null
                    && !event.getTreeNode().isSelected()
                    && placemark.getMarkerId().equals(mark.getId())) {
                mark.setVisible(false);
                break;
            }
        }
        //recallPolylineSearchArea();
    }

    public void recallPolylineSearchArea() {
        if (StringUtils.isNotBlank(search)) {
            if (search.equals("DRIFTCALCULATION")) {
                for (Polyline p : mapMBean.getCalculationKMLGenerator().getListLeewayPolylines()) {
                    RequestContext.getCurrentInstance().execute(
                            "addPolylineSearchArea('" + p.getStrokeColor() + "',"
                            + p.getPaths().get(0).getLat() + ","
                            + p.getPaths().get(0).getLng() + ","
                            + p.getPaths().get(1).getLat() + ","
                            + p.getPaths().get(1).getLng() + ")");
                }
            } else if (search.equals("TRAPEZIUM")) {
                for (TrapeziumSearchArea tsa : mapMBean.getListTrapeziumSearchArea()) {
                    RequestContext.getCurrentInstance().execute(
                            "addPolylineSearchArea('#FFFFFF',"
                            + tsa.getSmallRoundPivot().getLat() + ","
                            + tsa.getSmallRoundPivot().getLng() + ","
                            + tsa.getLargeRoundPivot().getLat() + ","
                            + tsa.getLargeRoundPivot().getLng() + ")");
                }
            }
        }
    }

    public void onContextMenu(NodeSelectEvent event) {
        Document doc = (Document) event.getTreeNode().getData();
        placemark = (IncidentPlacemark) doc.getObjClass();
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        System.out.println("Marker Lat : " + marker.getLatlng().getLat());
        System.out.println("Marker Lng : " + marker.getLatlng().getLng());
        RequestContext.getCurrentInstance().execute("updateMarker('" + marker.getLatlng().getLat() + "',"
                + "'" + marker.getLatlng().getLng() + "')");
    }

    public List<Theme> getListIconPlacemark() {
        if (listIconPlacemark == null) {
            try {
                listIconPlacemark = new ArrayList<>();
                String pathFile = System.getProperty("user.dir")
                        + "\\src\\main\\webapp\\resources\\basarnas\\images\\placemark";
                System.out.println("Path Placemark : " + pathFile);
                File file = new File(pathFile);
                String[] listFile = file.list();
                listIconPlacemark.add(new Theme(0, "default_placemark", "default_placemark.png"));
                for (int i = 0; i < listFile.length; i++) {
                    listIconPlacemark.add(new Theme(i + 1, FilenameUtils.removeExtension(listFile[i]), listFile[i]));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return listIconPlacemark;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        search = parameterMap.get("search");
        if (mapMBean.getIncident() != null
                && mapMBean.getRadiobutton() != null && mapMBean.getRadiobutton().equals("PLACEMARK")) {
            List<IncidentPlacemark> list = placemarkRepo.findByIncidentAndDeletedFalse(mapMBean.getIncident());
            for (IncidentPlacemark place : list) {
                icon = FacesContext.getCurrentInstance().getExternalContext()
                        .getRequestContextPath().concat("/resources/basarnas/images/placemark/" + place.getImage());
                description = new StringBuilder("<b>Placemark Insiden</b><br>")
                        .append("<br>Nama Placemark : ").append(place.getPlacemarkName())
                        .append("<br>Latitude : ").append(
                        latPlacemark.getConvertDdToDms(String.valueOf(place.getLatitude()), Boolean.TRUE))
                        .append("<br>Longitude : ").append(
                        lngPlacemark.getConvertDdToDms(String.valueOf(place.getLongitude()), Boolean.FALSE)).toString();
                Marker marker = new Marker(new LatLng(Double.valueOf(place.getLatitude()),
                        Double.valueOf(place.getLongitude())), null, description, icon);
                marker.setDraggable(true);
                mapMBean.getMapModel().addOverlay(marker);
                place.setMarkerId(marker.getId());
                placemarkRepo.save(place);
            }
            createTreePlacemarkInc();
        }
    }
}
