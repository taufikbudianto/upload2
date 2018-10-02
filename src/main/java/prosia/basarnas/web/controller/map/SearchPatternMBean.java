/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.basarnas.enumeration.ECoordinate;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.enumeration.MapPattern;
import prosia.basarnas.enumeration.MapPatternType;
import prosia.basarnas.enumeration.MapSearch;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.Waypoint;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.service.IncSearchPatternService;
import prosia.basarnas.web.controller.MapMBean;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.model.map.SearchAreaDialog;
import prosia.basarnas.web.model.map.SearchPatternParameter;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.map.MapCalculation;
import prosia.basarnas.web.util.map.ProcessPattern;
import prosia.basarnas.repo.IncTaskSearchAreaRepo;
import prosia.basarnas.web.model.Document;

/**
 *
 * @author Ismail
 */
@Component
@Scope("view")
@Data
public class SearchPatternMBean implements Serializable {

    @Autowired
    private MapMBean mapMBean;

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;

    @Autowired
    private SearchAreaRepo searchAreaRepo;
    @Autowired
    private IncTaskSearchAreaRepo taskSearchAreaRepo;
    @Autowired
    private IncSearchPatternService incSearchPatternService;

    private TreeNode treeSearchPattern;
    private TreeNode selectedTreeSearchPattern;
    private List<SearchArea> listSearchArea;
    private List<IncTaskSearchArea> listTaskSearchArea;
    private List<IncidentAsset> listIncidentAsset;
//Pattern DriftCalculation
    private ProcessPattern processPattern;
    private MapPattern mapTypePattern;
    private Coordinate latitudeSpp;
    private Coordinate longitudeSpp;

    private SearchPatternParameter patternParameter;
    private SearchAreaDialog areaDialog;
    private List<Integer> listWaypoint;
    private List<MapPattern> mapPattern;
    private MapPatternType mapPatternType;
    private List<MapPatternType> listMapPatternType;
    private List<Waypoint> listIncWaypoint;
    private GenerateValueRequestBean generateValueRequestBean;
    private Integer waypointIndex;
    private Boolean isNew;
    private Boolean isEdit;
    private Boolean isTaskArea;
    private Boolean isTreeRendered;
    private Polyline polyline;
    private List<LatLng> listLatLngFreeDefineCenter;
    private static List<LatLng> listLatLngFreeDefineCenterSend;
    private List<LatLng> listLatLngFreeDefine;
    private String namePatternFreeDefine;
    private Boolean addOrCancel;
    private List<LatLng> listLatLngDelFreedefine;
    private static Polyline polylinefordelete;
    private static List<LatLng> listLatLngDelFreedefineSend;
    private List<IncidentSearchPattern> listIncSearchPattern;
    private List<IncidentSearchPattern> selectionlistIncSearchPattern;

    public SearchPatternMBean() {
        treeSearchPattern = null;
        processPattern = new ProcessPattern();
        latitudeSpp = new Coordinate();
        longitudeSpp = new Coordinate();
        listIncidentAsset = new ArrayList<>();
        listWaypoint = new ArrayList<>();
        listLatLngFreeDefineCenter = new ArrayList<>();
        waypointIndex = 0;
        polyline = new Polyline();
        listLatLngFreeDefine = new ArrayList<>();
        isNew = false;
        isEdit = false;
        listIncSearchPattern = new ArrayList<>();
    }

//    public void refresh() {
//        if (!mapMBean.getListTaskSearchArea().isEmpty()) {
//            GenerateValueRequestBean generateValueTaskArea = new GenerateValueRequestBean();
//            for (GTaskSearchArea taskSearchArea : mapMBean.getListTaskSearchArea()) {
//                generateValueTaskArea
//                        = prosia.basarnas.web.util.map.Serializable.createGenerateValueRequest(
//                                null, taskSearchArea);
//                generateValueTaskArea.setState(GenerateValueRequestBean.GenerateValueState.TASK_SEARCH_AREA);
//                DriftCalculationKMLGenerator dckmlg = new DriftCalculationKMLGenerator(generateValueTaskArea);
//                mapMBean.getMapModel().addOverlay(dckmlg.getPolygonTaskArea());
//            }
//        }
//    }
    public void showDialogVal() {
        listTaskSearchArea = taskSearchAreaRepo.findByIncident(mapMBean.getIncident(), true);
        isTaskArea = !listTaskSearchArea.isEmpty();
        if (isTaskArea) {
            mapPatternType = MapPatternType.TASK_SEARCH_AREA;
            listMapPatternType = new ArrayList<>();
            listMapPatternType = Arrays.asList(MapPatternType.values());
        }
        RequestContext.getCurrentInstance().reset("idDialogNotifPattern");
        RequestContext.getCurrentInstance().update("idDialogNotifPattern");
        RequestContext.getCurrentInstance().execute("PF('showDialogNotifPattern').show()");
    }

    public void convertJsonTaskSearchArea(IncTaskSearchArea incTaskSearchArea) {
        JSONObject object = new JSONObject(incTaskSearchArea.getArea());
        JSONObject jsonLatLng = object.getJSONObject("pivot");

        generateValueRequestBean.setPivotTaskSearchArea(
                new LatLng(jsonLatLng.getDouble("lat"), jsonLatLng.getDouble("lng")));
        JSONArray jsonArray = object.getJSONArray("area");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getInt("sequence") == 1) {
                generateValueRequestBean.setStartTaskSearchArea(
                        new LatLng(obj.getDouble("lng"), obj.getDouble("lat")));
            }
        }
        generateValueRequestBean.setWidthTaskSearchArea(object.getDouble("width"));
        generateValueRequestBean.setHeightTaskSearchArea(object.getDouble("height"));
        generateValueRequestBean.setTiltTaskSearchArea(object.getDouble("tiltDrift"));
        generateValueRequestBean.setTrackSpacing(object.getDouble("trackSpacing"));
        generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.TASK_SEARCH_AREA);
    }

    //Search Pattern Param
    public void showDialogSPP() {
        isNew = true;
        isEdit = false;
        mapTypePattern = null;
        mapPattern = new ArrayList<>();
        patternParameter = new SearchPatternParameter();
        generateValueRequestBean = mapMBean.getGenerateValueRequestBean();
        if (mapPatternType != null && mapPatternType.equals(MapPatternType.TASK_SEARCH_AREA)) {
            convertJsonTaskSearchArea(listTaskSearchArea.get(0));
        } else {
            generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA);
            generateValueRequestBean.setTrackSpacing(null);
            isTaskArea = false;
        }

        mapMBean.setMapSearch(MapSearch.SEARCH_PATTERN);
        if (listIncidentAsset.isEmpty()) {
            listIncidentAsset = incidentAssetRepo.findAllByIncident(mapMBean.getIncident());
        }

        if (mapTypePattern == null) {
            if (mapMBean.getMapCalculationType().equals(MapCalculationType.DRIFT_CALCULATION)
                    || mapMBean.getMapCalculationType().equals(MapCalculationType.CUSTOM)
                    || mapMBean.getMapCalculationType().equals(MapCalculationType.MONTECARLO)
                    || isTaskArea) {
                for (MapPattern mp : MapPattern.values()) {
                    if (!mp.equals(MapPattern.TRAPEZIUM_PARAREL_SEARCH) && !mp.equals(MapPattern.SEARCH_PATTERN_FREE_DEFINE)) {
                        mapPattern.add(mp);
                    }
                }
                mapTypePattern = MapPattern.SECTOR_SEARCH;
                patternParameter = processPattern.setPatternTypeChange(generateValueRequestBean, mapTypePattern);
            } else if (mapMBean.getMapCalculationType().equals(MapCalculationType.TRAPEZIUM)) {
                listWaypoint.clear();
                for (int i = 1; i <= mapMBean.getListTrapeziumSearchArea().size(); i++) {
                    listWaypoint.add(i);
                }
                mapTypePattern = MapPattern.TRAPEZIUM_PARAREL_SEARCH;
                mapPattern.add(mapTypePattern);
                patternParameter = processPattern.setPatternTypeTrapez(mapMBean.getListTrapeziumSearchArea().get(0));
            }
            latitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLat(), ECoordinate.Latitude);
            longitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLng(), ECoordinate.Longitude);
            RequestContext.getCurrentInstance().update("idPgStart");
        }
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().reset("idDialogSPP");
        RequestContext.getCurrentInstance().update("idDialogSPP");
        RequestContext.getCurrentInstance().execute("PF('showDialogSPP').show()");
    }

    public void editDialogSPP() {
        Document d = (Document) selectedTreeSearchPattern.getData();
        patternParameter = (SearchPatternParameter) d.getObjClass();
        latitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLat(), ECoordinate.Latitude);
        longitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLng(), ECoordinate.Longitude);
        isEdit = true;
        isNew = false;
        RequestContext.getCurrentInstance().reset("idDialogSPP");
        RequestContext.getCurrentInstance().update("idDialogSPP");
        RequestContext.getCurrentInstance().execute("PF('showDialogSPP').show()");
    }

    public void onTaskSearchChange() {
        try {
            convertJsonTaskSearchArea(patternParameter.getIncTaskSearchArea());
            patternParameter = processPattern.setPatternTypeChange(generateValueRequestBean, mapTypePattern);
            latitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLat(), ECoordinate.Latitude);
            longitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLng(), ECoordinate.Longitude);
            RequestContext.getCurrentInstance().update("idPgParameter");
            RequestContext.getCurrentInstance().update("idPgStart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPatternTypeChange() {
        patternParameter = processPattern.setPatternTypeChange(generateValueRequestBean, mapTypePattern);
        latitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLat(), ECoordinate.Latitude);
        longitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLng(), ECoordinate.Longitude);
        RequestContext.getCurrentInstance().update("idPgParameter");
        RequestContext.getCurrentInstance().update("idPgStart");
    }

    public void onWaypointChange() {
        patternParameter = processPattern.setPatternTypeTrapez(mapMBean.getListTrapeziumSearchArea().get(waypointIndex - 1));
        latitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLat(), ECoordinate.Latitude);
        longitudeSpp = processPattern.convCoordinate(
                patternParameter.getStart().getLng(), ECoordinate.Longitude);
        RequestContext.getCurrentInstance().update("idPgParameter");
        RequestContext.getCurrentInstance().update("idPgStart");
    }

    public void createPattern() {
        try {
            latitudeSpp.converter();
            longitudeSpp.converter();
            patternParameter = processPattern.createPattern(
                    patternParameter, latitudeSpp.getValidDecimalDegrees(),
                    longitudeSpp.getValidDecimalDegrees());
            listIncWaypoint = MapCalculation.waypoints(patternParameter.getPolyline().getPaths());
            initMap();
            if (treeSearchPattern == null) {
                isTreeRendered = true;
                treeSearchPattern = new DefaultTreeNode(new Document("Files", null, "Files"), null);
            }
            TreeNode isExist = null;
            if (!treeSearchPattern.getChildren().isEmpty()) {
                for (TreeNode tn : treeSearchPattern.getChildren()) {
                    Document d = (Document) tn.getData();
                    SearchPatternParameter spp = (SearchPatternParameter) d.getObjClass();
                    if (spp.getType().equals(MapPattern.SEARCH_PATTERN_FREE_DEFINE)) {
                        System.out.println("get Polyline : " + spp.toString());
                    }
                    if (spp.getId().equals(patternParameter.getId())) {
                        isExist = tn;
                        mapTypePattern = spp.getType();
                    } else {
                        mapMBean.getMapModel().addOverlay(spp.getPolyline());
                        if (!spp.getMarkers().isEmpty()) {
                            for (Marker sectorMarker : spp.getMarkers()) {
                                mapMBean.getMapModel().addOverlay(sectorMarker);
                            }
                        }
                    }
                }
            }
            if (isExist != null) {
                treeSearchPattern.getChildren().remove(isExist);
            }
            TreeNode patern
                    = new DefaultTreeNode("searcharea",
                            new Document(treeSearchPattern.getChildren().size() + 1,
                                    patternParameter.getName(), patternParameter,
                                    mapTypePattern.getType(), patternParameter.getHexColor()), treeSearchPattern);
            mapMBean.getMapModel().addOverlay(patternParameter.getPolyline());
            if (!patternParameter.getMarkers().isEmpty()) {
                for (Marker sectorMarker : patternParameter.getMarkers()) {
                    mapMBean.getMapModel().addOverlay(sectorMarker);
                }
            }

//        if (!listPatternParameter.isEmpty()) {
//            listPatternParameter.remove(patternParameter);
//        }
//        listPatternParameter.add(patternParameter);
//
//        for (SearchPatternParameter pp : listPatternParameter) {
//            mapMBean.getMapModel().addOverlay(pp.getPolyline());
//            if (!pp.getMarkers().isEmpty()) {
//                for (Marker sectorMarker : pp.getMarkers()) {
//                    mapMBean.getMapModel().addOverlay(sectorMarker.se);
//                }
//            }
//            mapMBean.setCenterGeoMap(pp.getCenterGeoMap());
//            mapMBean.setZoomGeoMap(pp.getZoomGeoMap());
//        }
            RequestContext.getCurrentInstance().update("idPanelMapSearch");
            RequestContext.getCurrentInstance().update("idMap");
            RequestContext.getCurrentInstance().execute("PF('showDialogSPP').hide()");
        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Error", "Proses pattern Gagal");
        }
    }

    public void removePattern() {
        Document d = (Document) selectedTreeSearchPattern.getData();
        patternParameter = (SearchPatternParameter) d.getObjClass();
        initMap();
        TreeNode isExist = null;
        for (TreeNode tn : treeSearchPattern.getChildren()) {
            Document d1 = (Document) tn.getData();
            SearchPatternParameter spp = (SearchPatternParameter) d1.getObjClass();
            System.out.println("spp " + spp.getName());
            if (spp.getId().equals(patternParameter.getId())) {
                System.out.println("IsExist");
                isExist = tn;
            } else {
                mapMBean.getMapModel().addOverlay(spp.getPolyline());
                if (!spp.getMarkers().isEmpty()) {
                    for (Marker sectorMarker : spp.getMarkers()) {
                        mapMBean.getMapModel().addOverlay(sectorMarker);
                    }
                }
            }
        }
        treeSearchPattern.getChildren().remove(isExist);
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void clearPattern() {
        initMap();
        mapMBean.initTaskSearchArea();
        treeSearchPattern = null;
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().update("idMap");
    }

    private void initMap() {
        switch (mapMBean.getMapCalculationType()) {
            case DRIFT_CALCULATION:
                mapMBean.initDcMapSearchArea();
                break;
            case TRAPEZIUM:
                mapMBean.initTrapezSearchArea();
                break;
            case CUSTOM:
                mapMBean.initCustomArea();
                break;
            case MONTECARLO:
                mapMBean.initMcMapSearchArea();
                break;
            default:
                break;
        }
        mapMBean.initTaskSearchArea();
    }

    public void showDialogSave() {
        if (selectedTreeSearchPattern == null) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi",
                    "Tidak ada Search Pattern yang terpilih pada Pattern Navigation");
            return;
        }
        Document d = (Document) selectedTreeSearchPattern.getData();
        patternParameter = (SearchPatternParameter) d.getObjClass();
        isEdit = false;
        mapMBean.setSearchPattern(new IncidentSearchPattern());
        listSearchArea = searchAreaRepo.findByIncidentAndStatus(mapMBean.getIncident(), true);
        listTaskSearchArea = new ArrayList<>();
        if (listSearchArea.size() == 1) {
            listTaskSearchArea = taskSearchAreaRepo.findBySearchAreaAndStatus(
                    listSearchArea.get(0), true);
        }
        mapMBean.getSearchPattern().setName(patternParameter.getName());
        mapMBean.getSearchPattern().setType(patternParameter.getType().name());
        mapMBean.getSearchPattern().setIncidentAsset(patternParameter.getIncidentAsset());
        mapMBean.getSearchPattern().setDescription(patternParameter.getDescription());
        mapMBean.getSearchPattern().setUserSiteID(mapMBean.getOfficeCode());
        if (patternParameter.getIncTaskSearchArea() != null) {
            mapMBean.getSearchPattern().setSearchArea(patternParameter.getIncTaskSearchArea().getSearchArea());
            mapMBean.getSearchPattern().setTaskSearchArea(patternParameter.getIncTaskSearchArea());
        }
        if (patternParameter.getType().equals(MapPattern.SEARCH_PATTERN_FREE_DEFINE)) {
            patternParameter.setPolyline(polyline);
            patternParameter = processPattern.createPattern(patternParameter, Double.MAX_VALUE, Double.MAX_VALUE);
        }
        listIncWaypoint = MapCalculation.waypoints(patternParameter.getPolyline().getPaths());

        RequestContext.getCurrentInstance().reset("idDialogSaveSearchPattern");
        RequestContext.getCurrentInstance().update("idDialogSaveSearchPattern");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchPattern').show()");
    }

    public void onChangeSearchArea() {
        mapMBean.getSearchPattern().setTaskSearchArea(null);
        listTaskSearchArea = taskSearchAreaRepo.findBySearchAreaAndStatus(
                mapMBean.getSearchPattern().getSearchArea(), true);
        RequestContext.getCurrentInstance().update("idSomTaskSearchArea");
    }

    public void showDialogEdit() {
        listIncWaypoint = new ArrayList<>();
        isEdit = true;
        listSearchArea = searchAreaRepo.findByIncidentAndStatus(mapMBean.getIncident(), true);
        listTaskSearchArea = taskSearchAreaRepo.findBySearchAreaAndStatus(mapMBean.getSearchPattern().getSearchArea(), true);
        if (StringUtils.isNotBlank(mapMBean.getSearchPattern().getJsonMap())) {
            List<LatLng> paths = new ArrayList<>();
            JSONObject object = new JSONObject(mapMBean.getSearchPattern().getJsonMap());
            JSONArray overlays = object.getJSONArray("overlays");
            for (int i = 0; i < overlays.length(); i++) {
                JSONObject obj = overlays.getJSONObject(i);
                if (obj.getString("type").equals("Polyline")) {
                    JSONArray jsonPaths = obj.getJSONArray("paths");
                    for (int ii = 0; ii < jsonPaths.length(); ii++) {
                        JSONObject jsonLatLng = jsonPaths.getJSONObject(ii);
                        LatLng latLng = new LatLng(jsonLatLng.getDouble("lat"), jsonLatLng.getDouble("lng"));
                        paths.add(latLng);
                    }
                }
            }
            listIncWaypoint = MapCalculation.waypoints(paths);
        }
        RequestContext.getCurrentInstance().reset("idDialogSaveSearchPattern");
        RequestContext.getCurrentInstance().update("idDialogSaveSearchPattern");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchPattern').show()");
    }

    public void saveSearchPattern() {
        try {
            mapMBean.getSearchPattern().setStatus(true);
            mapMBean.getSearchPattern().setJsonMap(generateJsonMap(patternParameter.getPolyline().getPaths()).toString());
            incSearchPatternService.saveIncSearchPattern(mapMBean.getSearchPattern());
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Simpan Berhasil");
            mapMBean.initTreeRouting();
            RequestContext.getCurrentInstance().update("idPanelMapSearch");
            RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchPattern').hide()");
        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Error", "Simpan Gagal");
        }
    }

    public void removeSearchPattern() {
        mapMBean.getSearchPattern().setStatus(false);
        try {
            incSearchPatternService.saveIncSearchPattern(mapMBean.getSearchPattern());
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Hapus Berhasil");
            mapMBean.initTreeRouting();
            RequestContext.getCurrentInstance().update("idPanelMapSearch");
            RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchArea').hide()");
        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Error", "Hapus Gagal");
        }
    }

    public void viewOnMap() {
        mapTypePattern = MapPattern.valueOf(mapMBean.getSearchPattern().getType());
        generateValueRequestBean = new GenerateValueRequestBean();
        jsonToPattern(mapMBean.getSearchPattern().getDescription());
        latitudeSpp.converter();
        longitudeSpp.converter();
        if (patternParameter.getType().equals(MapPattern.SEARCH_PATTERN_FREE_DEFINE)) {
            jsonToPatternFreeDefine(mapMBean.getSearchPattern().getJsonMap(), mapMBean.getSearchPattern().getName());
        } else {
            patternParameter = processPattern.createPattern(
                    patternParameter, latitudeSpp.getValidDecimalDegrees(),
                    longitudeSpp.getValidDecimalDegrees());
        }
        mapMBean.getMapModel().addOverlay(patternParameter.getPolyline());
        if (!patternParameter.getMarkers().isEmpty()) {
            for (Marker sectorMarker : patternParameter.getMarkers()) {
                mapMBean.getMapModel().addOverlay(sectorMarker);
            }
        }
        mapMBean.setCenterGeoMap(patternParameter.getCenterGeoMap());
        mapMBean.setZoomGeoMap(patternParameter.getZoomGeoMap());
        RequestContext.getCurrentInstance().update("idMap");
    }

    private void jsonToPattern(String jsonStr) {
        patternParameter = new SearchPatternParameter();
        patternParameter.setType(mapTypePattern);
        JSONObject json = new JSONObject(jsonStr);
        if (json.has("Start")) {
            JSONObject start = json.getJSONObject("Start");
            Double lng = start.getDouble("lng");
            Double lat = start.getDouble("lat");
            patternParameter.setStart(new LatLng(lat, lng));
            latitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLat(), ECoordinate.Latitude);
            longitudeSpp = processPattern.convCoordinate(
                    patternParameter.getStart().getLng(), ECoordinate.Longitude);
        }
        if (json.has("Search_Radius")) {
            patternParameter.setSearchRadius(json.getDouble("Search_Radius"));
        }
        if (json.has("Heading")) {
            patternParameter.setHeading(json.getDouble("Heading"));
        }
        if (json.has("Search_Leg")) {
            patternParameter.setSearchLeg(json.getDouble("Search_Leg"));
        }
        if (json.has("Width")) {
            patternParameter.setWidth(json.getDouble("Width"));
        }
        if (json.has("Track_Spacing")) {
            patternParameter.setTrackSpacing(json.getDouble("Track_Spacing"));
        }
        if (json.has("Min_Search_Leg")) {
            patternParameter.setMinSearchLeg(json.getDouble("Min_Search_Leg"));
        }
        if (json.has("Max_Search_Leg")) {
            patternParameter.setMaxSearchLeg(json.getDouble("Max_Search_Leg"));
        }
        if (json.has("Pivot")) {
            JSONObject pivot = json.getJSONObject("Pivot");
            patternParameter.setPivot(new LatLng(pivot.getDouble("lat"), pivot.getDouble("lng")));
        }
        if (json.has("Hex_Color")) {
            patternParameter.setHexColor(json.getString("Hex_Color"));
        }
    }

    public void actionLatitudeSpp() {
        latitudeSpp.converter();
        latitudeSpp.setCounter(latitudeSpp.getCounter() + 1);
        if (latitudeSpp.getCounter() > 3) {
            latitudeSpp.setCounter(1);
        }
    }

    public void actionLongitudeSpp() {
        longitudeSpp.converter();
        longitudeSpp.setCounter(longitudeSpp.getCounter() + 1);
        if (longitudeSpp.getCounter() > 3) {
            longitudeSpp.setCounter(1);
        }
    }

    public void freeDefineAction(Boolean changeFreedefine) {
        mapMBean.setMapSearch(MapSearch.SEARCH_PATTERN);
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        namePatternFreeDefine = "";
        RequestContext.getCurrentInstance().execute("function handlePointClick(event) {"
                + "var changeFreedefine=" + changeFreedefine + ";"
                + "if(changeFreedefine){"
                + "PF('showDialogSPPFreeDefine').show();}}");
    }

    public void addMarkerFreeDefine() {
        polyline = new Polyline();
        listLatLngDelFreedefine = new ArrayList<>();
        if (listIncidentAsset.isEmpty()) {
            listIncidentAsset = incidentAssetRepo.findAllByIncident(mapMBean.getIncident());
        }
        listLatLngFreeDefine = new ArrayList<>();
        addOrCancel = true;
        patternParameter = new SearchPatternParameter();
        patternParameter.setName(namePatternFreeDefine);
        patternParameter.setHexColor("#0000FF");
        patternParameter.setType(MapPattern.SEARCH_PATTERN_FREE_DEFINE);
        if (treeSearchPattern == null) {
            isTreeRendered = true;
            treeSearchPattern = new DefaultTreeNode(new Document("Files", null, "Files"), null);
        }
        TreeNode isExist = null;
        if (!treeSearchPattern.getChildren().isEmpty()) {
            for (TreeNode tn : treeSearchPattern.getChildren()) {
                Document d = (Document) tn.getData();
                SearchPatternParameter spp = (SearchPatternParameter) d.getObjClass();
                if (spp.getId().equals(patternParameter.getId())) {
                    isExist = tn;
                    mapTypePattern = spp.getType();
                } else {
                    mapMBean.getMapModel().addOverlay(spp.getPolyline());
                    if (!spp.getMarkers().isEmpty()) {
                        for (Marker sectorMarker : spp.getMarkers()) {
                            mapMBean.getMapModel().addOverlay(sectorMarker);
                        }
                    }
                }
            }
        }
        if (isExist != null) {
            treeSearchPattern.getChildren().remove(isExist);
        }
        TreeNode patern = new DefaultTreeNode("searcharea",
                new Document(treeSearchPattern.getChildren().size() + 1,
                        patternParameter.getName(), patternParameter,
                        "free_define", patternParameter.getHexColor()), treeSearchPattern);
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().execute("PF('showDialogSPPFreeDefine').hide()");
        RequestContext.getCurrentInstance().execute("function handlePointClick(event) { "
                + "rcSendPoint([{name: 'latitude', value: event.latLng.lat()}, {name: 'longitude', value: event.latLng.lng()}]);}");
        namePatternFreeDefine = null;
    }

    public void cancelMarkerFreeDefine() {
        addOrCancel = false;
        RequestContext.getCurrentInstance().execute("PF('showDialogSPPFreeDefine').hide()");
        RequestContext.getCurrentInstance().execute("function handlePointClick(event) {}");
        namePatternFreeDefine = null;
    }

    public void onPointSelect() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Double lat = Double.valueOf(params.get("latitude"));
        Double lng = Double.valueOf(params.get("longitude"));
        polyline.getPaths().add(new LatLng(lat, lng));
        polyline.setId(namePatternFreeDefine);
        polyline.setStrokeColor("#0000FF");
        polyline.setStrokeWeight(5);
        polyline.setStrokeOpacity(0.5);
        if (polyline.getPaths().size() == 1) {
            listLatLngFreeDefine.add(new LatLng(lat, lng));
            mapMBean.getMapModel().addOverlay(new Marker(new LatLng(lat, lng)));
            patternParameter.setStart(new LatLng(lat, lng));
            patternParameter.setPivot(new LatLng(lat, lng));

        } else {

            listLatLngFreeDefine.add(new LatLng(lat, lng));
            patternParameter.setPivot(new LatLng(lat, lng));
            LatLng to = new LatLng(lat, lng);
            LatLng from = listLatLngFreeDefine.get(listLatLngFreeDefine.size() - 2);
            movePosition(from, to);
            if (listLatLngFreeDefine.size() > 2) {
                deletePositionAkhir(from, to);
            }

        }
        generateValueRequestBean = mapMBean.getGenerateValueRequestBean();
        generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA);
        generateValueRequestBean.setTrackSpacing(null);
        isTaskArea = false;
        mapMBean.setMapSearch(MapSearch.SEARCH_PATTERN);
        patternParameter = processPattern.createPattern(patternParameter, listLatLngFreeDefine.get(0).getLat(), listLatLngFreeDefine.get(0).getLng());
        System.out.println("pattern : " + patternParameter.toString());

    }

    public void deletePositionAkhir(LatLng from, LatLng to) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            Double lat = Double.valueOf(params.get("lati"));
            Double longi = Double.valueOf(params.get("longi"));
            if (lat != null && longi != null) {
                System.out.println("masuk");
            }

        } catch (NullPointerException e) {

        }
        LatLng[] fromTo = new LatLng[2];
        fromTo[0] = from;
        fromTo[1] = to;
        List<Marker> lisMarker = mapMBean.getMapModel().getMarkers();
        String icon = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/undo_resize.png");
        Double undoLat = (from.getLat() + to.getLat()) / 2;
        Double undoLng = (from.getLng() + to.getLng()) / 2;
        listLatLngDelFreedefine.add(new LatLng(undoLat, undoLng));
        Marker result1 = null;

        SearchPatternMBean.listLatLngDelFreedefineSend = new ArrayList<>();
        SearchPatternMBean.polylinefordelete = new Polyline();
        SearchPatternMBean.listLatLngDelFreedefineSend = listLatLngDelFreedefine;
        SearchPatternMBean.polylinefordelete = polyline;
        if (listLatLngDelFreedefine.size() == 1) {
            mapMBean.getMapModel().addOverlay(new Marker(new LatLng(undoLat, undoLng), "Undo", null, icon));
        } else {
            result1 = lisMarker.stream()
                    .filter(x -> listLatLngDelFreedefine.get(listLatLngDelFreedefine.size() - 2).equals(x.getLatlng()))
                    .findAny()
                    .orElse(null);
            mapMBean.getMapModel().getMarkers().remove(result1);
            mapMBean.getMapModel().addOverlay(new Marker(new LatLng(undoLat, undoLng), "Undo", null, icon));
        }

    }

    public void movePosition(LatLng from, LatLng to) {
        List<LatLng> listPolylineFreeDefine = polyline.getPaths();

        Double latitude = 0.0;
        Double longitude = 0.0;
        for (LatLng latlng : listPolylineFreeDefine) {
            latitude += latlng.getLat();
            longitude += latlng.getLng();
        }
        Double centerLongitude = latitude / (listPolylineFreeDefine.size());
        Double centerLatitude = longitude / (listPolylineFreeDefine.size());
        List<Marker> lisMarker = mapMBean.getMapModel().getMarkers();
        Marker result1 = null;
        if (listLatLngFreeDefine.size() == 2) {
            listLatLngFreeDefineCenter.add(listLatLngFreeDefine.get(0));
            result1 = lisMarker.stream()
                    .filter(x -> listLatLngFreeDefine.get(0).equals(x.getLatlng()))
                    .findAny()
                    .orElse(null);
            listLatLngFreeDefineCenter.add(new LatLng(centerLongitude, centerLatitude));
        } else {

            //listDeleteIconFreeDefine.add(new LatLng(((from.getLat()+to.getLat())/2), ((from.getLat()+to.getLat())/2)));
            result1 = lisMarker.stream()
                    .filter(x -> listLatLngFreeDefineCenter.get(listLatLngFreeDefineCenter.size() - 1).equals(x.getLatlng()))
                    .findAny()
                    .orElse(null);
            listLatLngFreeDefineCenter.add(new LatLng(centerLongitude, centerLatitude));
        }
        SearchPatternMBean.listLatLngFreeDefineCenterSend = new ArrayList<>();
        SearchPatternMBean.listLatLngFreeDefineCenterSend = listLatLngFreeDefineCenter;
        mapMBean.getMapModel().getMarkers().remove(result1);
        mapMBean.getMapModel().addOverlay(polyline);
        mapMBean.getMapModel().addOverlay(new Marker(new LatLng(centerLongitude, centerLatitude)));
    }

    private void jsonToPatternFreeDefine(String jsonStr, String nama) {
        Polyline pol = new Polyline();
        pol.setId(namePatternFreeDefine);
        pol.setStrokeColor("#0000FF");
        pol.setStrokeWeight(5);
        pol.setStrokeOpacity(0.5);
        patternParameter = new SearchPatternParameter();
        patternParameter.setType(mapTypePattern);
        JSONObject jsonArray = new JSONObject(jsonStr);
        JSONArray overlays = (JSONArray) jsonArray.get("overlays");
        JSONObject jsOver = (JSONObject) overlays.get(0);
        JSONArray paths = (JSONArray) jsOver.get("paths");
        for (Object dataJson : paths) {
            JSONObject jsonData = new JSONObject(dataJson.toString());
            patternParameter.setCenterGeoMap(String.valueOf(jsonData.getDouble("lat")) + "," + String.valueOf(jsonData.getDouble("lng")));
            pol.getPaths().add(new LatLng(jsonData.getDouble("lat"), jsonData.getDouble("lng")));
        }
        patternParameter.setZoomGeoMap(10);
        patternParameter.setPolyline(pol);
    }

    public void showAllSearchPattern() {
        if (treeSearchPattern == null || treeSearchPattern.getChildren().isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Search Pattern tidak ada, buat Search Pattern terlebih dahulu.");
            return;
        }
        listSearchArea = searchAreaRepo.findByIncidentAndStatus(mapMBean.getIncident(), Boolean.TRUE);
        if (listSearchArea.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Search Pattern Tidak memiliki Searh Area, buat Search Area terlebih dahulu.");
            return;
        }
        listIncSearchPattern = new ArrayList<>();
        int i = 1;
        for (TreeNode tn : treeSearchPattern.getChildren()) {
            Document d = (Document) tn.getData();
            SearchPatternParameter spp = (SearchPatternParameter) d.getObjClass();
            IncidentSearchPattern incSearchPattern = new IncidentSearchPattern();
            incSearchPattern.setSearchPatternId(String.valueOf(i++));
            incSearchPattern.setDescription(spp.getDescription());
            incSearchPattern.setSearchArea(listSearchArea.get(0));
            incSearchPattern.setIncidentAsset(spp.getIncidentAsset());
            incSearchPattern.setName(spp.getName());
            incSearchPattern.setType(spp.getType().name());
            incSearchPattern.setUserSiteID(mapMBean.getOfficeCode());
            incSearchPattern.setJsonMap(generateJsonMap(spp.getPolyline().getPaths()).toString());
            incSearchPattern.setStatus(true);
            listIncSearchPattern.add(incSearchPattern);
        }
        RequestContext.getCurrentInstance().reset("idDialogAllPattern");
        RequestContext.getCurrentInstance().update("idDialogAllPattern");
        RequestContext.getCurrentInstance().execute("PF('showDialogAllPattern').show()");
    }

    public void saveAllSearchPattern() {
        try {
            if (!selectionlistIncSearchPattern.isEmpty()) {
                incSearchPatternService.saveIncSearchPattern(selectionlistIncSearchPattern);
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Simpan Berhasil");
                mapMBean.initTreeRouting();
                RequestContext.getCurrentInstance().update("idPanelMapSearch");
                RequestContext.getCurrentInstance().execute("PF('showDialogAllPattern').hide()");
            }
        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Error", "Simpan Gagal");
        }
    }

    private JSONObject generateJsonMap(List<LatLng> paths) {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonOverlay = new JSONArray();
        JSONObject jsonArea = new JSONObject();
        jsonArea.put("type", "Polyline");
        jsonArea.put("strokeColor", "#FF0000");
        jsonArea.put("strokeWeight", 3);
        JSONArray array = new JSONArray();
        for (LatLng ll : paths) {
            array.put(new JSONObject(ll));
        }
        jsonArea.put("paths", array);
        jsonOverlay.put(jsonArea);
        jsonObj.put("overlays", jsonOverlay);
        return jsonObj;
    }

    public static Polyline deleteLast() {
        return polylinefordelete;
    }

    public static List<LatLng> deleteList() {
        return listLatLngDelFreedefineSend;
    }

    public static List<LatLng> getListCenter() {
        return listLatLngFreeDefineCenterSend;
    }

    public static Object getRequestParam(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        Map<String, Object> map = component.getAttributes();

        return map.get(name);
    }

    private void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }
}
