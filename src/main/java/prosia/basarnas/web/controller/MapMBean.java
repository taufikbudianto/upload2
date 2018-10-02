/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.enumeration.MapPattern;
import prosia.basarnas.enumeration.MapSearch;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstWilayahKansar;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstWilayahKansarRepo;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.repo.SearchPatternRepo;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.model.Document;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.util.map.DriftCalculationKMLGenerator;
import prosia.basarnas.web.util.map.TrapesiumKMLGenerator;
import prosia.basarnas.repo.IncTaskSearchAreaRepo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.web.controller.map.SearchPatternMBean;
import prosia.basarnas.repo.UtiBeaconCompositeRepo;
import prosia.basarnas.web.model.map.MonteCarlo;
import prosia.basarnas.web.model.map.TaskSearchArea;
import prosia.basarnas.web.util.map.TaskAreaKMLGenerator;

/**
 *
 * @author Ismail
 */
@Component
@Scope("session")
@Data
public class MapMBean extends AbstractManagedBean implements Serializable {

    @Autowired
    private MstWilayahKansarRepo mstWilayahKansarRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private ResPotencyRepo potensiRepo;

    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;
    @Autowired
    private SearchAreaRepo searchAreaRepo;
    @Autowired
    private SearchPatternRepo searchPatternRepo;
    @Autowired
    private IncTaskSearchAreaRepo taskSearchAreaRepo;
    @Autowired
    private UtiBeaconCompositeRepo beaconRepo;

    private String officeCode;
    private MapModel mapModel = new DefaultMapModel();
    private MapModel mapSetting = new DefaultMapModel();

    //Setting
    private Boolean isIconInsiden;
    private Boolean isBatasKansar;
    private Boolean isPoi;
    private Boolean isTraffic;
    private Boolean isBeacon;

    //DEDI
    private Integer i;

    private Marker marker;
    private boolean clustercheck;
    //END
    private String centerGeoMap;
    private Integer zoomGeoMap;
    private String search;
    private String radiobutton;
    private boolean point;

    //POI potensi
    private List<ResPotency> listPotensi;
    private List<UtiBeaconComposite> listBeacon;

    private MapController controller;

    private MapCalculationType mapCalculationType;
    private Incident incident;
    //DriftCalculation
    private MapSearch mapSearch;
    private DriftCalculationKMLGenerator calculationKMLGenerator;
    private TrapesiumKMLGenerator trapesiumKMLGenerator;

    private GenerateValueRequestBean generateValueRequestBean;
    private List<TaskSearchArea> listTaskSearchArea;
    private List<TrapeziumSearchArea> listTrapeziumSearchArea;
    private List<MonteCarlo> listMonteCarlo;

    private List<IncidentAsset> incidentAssets;

    private TreeNode treeRouting;
    private TreeNode selectedTreeRouting;
    private SearchArea searchArea;
    private IncidentSearchPattern searchPattern;
    private IncTaskSearchArea incTaskSearchArea;
    private String measureData;

    public MapMBean() {
        officeCode = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        isBatasKansar = true;
        isPoi = false;
        isTraffic = false;
        isBeacon = false;
        isIconInsiden = true;
        generateValueRequestBean = new GenerateValueRequestBean();
        mapSearch = MapSearch.ROUTING;
        listTaskSearchArea = new ArrayList<>();
        listTrapeziumSearchArea = new ArrayList<>();
        incidentAssets = new ArrayList<>();
        incTaskSearchArea = new IncTaskSearchArea();
        listMonteCarlo = new ArrayList<>();
        incident = null;
        setCenterGeoMap();
    }

//    @PostConstruct
    public void reset() {
        search = null;
        mapModel = new DefaultMapModel();
        searchArea = new SearchArea();
        searchPattern = new IncidentSearchPattern();
        mapSearch = MapSearch.ROUTING;
        incident = null;
        isIconInsiden = true;
        //DEDI
        isBatasKansar = true;
        isPoi = false;
        isTraffic = false;
        isBeacon = false;
        //END
        generateValueRequestBean = new GenerateValueRequestBean();
        listTaskSearchArea = new ArrayList<>();
        listTrapeziumSearchArea = new ArrayList<>();

        setSampleModel();
        setPolygonKansar();
        setCenterGeoMap();

    }

    private void methodContohInsiden() {
        DriftCalcWorksheet1Result dcWorksheetResult;
        incident = incidentRepo.findAllByIncidentid("BSN-1711-0008");
        dcWorksheetResult = new DriftCalcWorksheet1Result();
        dcWorksheetResult.setDatumLatLng(new GLatLng(-5.672446, 106.830294));
        dcWorksheetResult.setDrawLeewayLine(true);
        dcWorksheetResult.setDriftLeftLatLng(new GLatLng(-5.671857, 106.536101));
        dcWorksheetResult.setDriftRightLatLng(new GLatLng(-5.672877, 107.12448));
        dcWorksheetResult.setLkpLatLng(new GLatLng(-6.17912626, 106.829338));
        dcWorksheetResult.setLkpToDatumAngle(0.1075);
        dcWorksheetResult.setLkpToDatumDistance(30.4555);
        dcWorksheetResult.setParrentID("0.21392083553937413");
        dcWorksheetResult.setRadius(25.0);
        dcWorksheetResult.setTilt(90.1);
        generateValueRequestBean
                = prosia.basarnas.web.util.map.Serializable.createGenerateValueRequest(dcWorksheetResult, null);
        generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA);
        listTaskSearchArea.clear();
        TaskSearchArea tsa = new TaskSearchArea();
        tsa.setId(listTaskSearchArea.size() + 1);
        tsa.setUnrotateStart(new LatLng(-5.639552666666667, 106.72283666666667));
        tsa.setUnrotatePivot(new LatLng(-5.722886, 106.76450333333334));
        tsa.setIncidentAsset(incidentAssetRepo.findOne("CGK-1711-0090"));
        tsa.setPivotDatumPoint(new LatLng(-5.672446, 106.830294));
        tsa.setWidth(5.0);
        tsa.setHeight(10.0);
        tsa.setTrackSpacing(10.0);
        tsa.setTiltDrift(90.1);
        tsa.setRadiusType(0);
        tsa.setName("xxx");
        tsa.setParrentID(String.valueOf(0.7860895789896607));
        tsa.setChildID(String.valueOf(0.687286572769353));
        tsa.setDriftTaskAreaID("CGK-1711-0001");
        tsa.setTypeTaskArea(MapCalculationType.DRIFT_CALCULATION);
        listTaskSearchArea.add(tsa);

    }

    //preRenderView
    public void initMap() {
        //jika sudah selesai tolong d hapus method di bawah
        //?incident=BSN-1711-0008

        log.debug("Init Map");
        // get parameters
        Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String incidentId = parameterMap.get("incident");
        String search = parameterMap.get("search");
        //mapModel = new DefaultMapModel();
        if (search != null) {

            isBatasKansar = false;
            isIconInsiden = false;
            isTraffic = false;
            isPoi = false;
            if (search.equals("DRIFTCALCULATION")) {
                mapCalculationType = MapCalculationType.DRIFT_CALCULATION;
                if (incident == null) {
                    methodContohInsiden();
                }
                if (incident != null && incident.getIncidentid().equals(incidentId)) {

                    setCalculationKMLGenerator(new DriftCalculationKMLGenerator(generateValueRequestBean));

//        
//            if (!listTaskSearchArea.isEmpty()) {
//                GenerateValueRequestBean generateValueTaskArea = new GenerateValueRequestBean();
//                for (GTaskSearchArea taskSearchArea : listTaskSearchArea) {
//                    generateValueTaskArea
//                            = prosia.basarnas.web.util.map.Serializable.createGenerateValueRequest(
//                                    currentDriftCalculation, taskSearchArea);
//                    generateValueTaskArea.setState(GenerateValueRequestBean.GenerateValueState.TASK_SEARCH_AREA);
//                    calculationKMLGenerator.createDriftCalculationStructurKML(generateValueTaskArea);
//                    mapSearchArea.addOverlay(calculationKMLGenerator.getPolygonTaskArea());
//                }
//            }
                    centerGeoMap = generateValueRequestBean.getDatumPoint().getLat()
                            + "," + generateValueRequestBean.getDatumPoint().getLng();
                    zoomGeoMap = 10;
                    try {
                        initDcMapSearchArea();
                    } catch (Exception e) {
                        log.error("initDcMapSearchArea error : {}", e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        initTaskSearchArea();
                    } catch (Exception e) {
                        log.error("initTaskSearchArea error : {}", e.getMessage());
                        e.printStackTrace();
                    }
                    RequestContext.getCurrentInstance().update("idMap");
                }
            } else if (search.equals("TRAPEZIUM")) {
                mapCalculationType = MapCalculationType.TRAPEZIUM;
                setTrapesiumKMLGenerator(new TrapesiumKMLGenerator(listTrapeziumSearchArea));
                try {
                    initTrapezSearchArea();
                    try {
                        initTaskSearchArea();
                    } catch (Exception e) {
                        log.error("initTaskSearchArea error : {}", e.getMessage());
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    log.error("initTrapezSearchArea error : {}", e.getMessage());
                    e.printStackTrace();
                }
            } else if (search.equals("CUSTOM")) {
                mapCalculationType = MapCalculationType.CUSTOM;
                if (searchArea != null) {
                    initCustomArea();
                }
            } else if (search.equals("MONTECARLO")) {
                mapCalculationType = MapCalculationType.MONTECARLO;
                setCalculationKMLGenerator(new DriftCalculationKMLGenerator(generateValueRequestBean));
                try {
                    initMcMapSearchArea();

                    RequestContext.getCurrentInstance().update("idMap");
                } catch (Exception e) {
                    log.error("initDcMapSearchArea error : {}", e.getMessage());
                    e.printStackTrace();
                }

            } else {
                reset();
            }
            initTreeRouting();
        }
    }

    public void clearMaps() {
        mapModel = new DefaultMapModel();
    }

    public void initDcMapSearchArea() {
        mapModel = new DefaultMapModel();
        RequestContext.getCurrentInstance().execute("IconBasarnas()");
        RequestContext.getCurrentInstance().execute("PositionBarControl()");
        for (Marker m : getCalculationKMLGenerator().getListMarkerArea()) {
            mapModel.addOverlay(m);
        }
        for (Polyline p : getCalculationKMLGenerator().getListShapeSquere()) {
            mapModel.addOverlay(p);
        }
        for (Polyline p : getCalculationKMLGenerator().getListLeewayPolylines()) {
            mapModel.addOverlay(p);
//            RequestContext.getCurrentInstance().execute(
//                    "addPolylineSearchArea('" + p.getStrokeColor() + "',"
//                    + p.getPaths().get(0).getLat() + ","
//                    + p.getPaths().get(0).getLng() + ","
//                    + p.getPaths().get(1).getLat() + ","
//                    + p.getPaths().get(1).getLng() + ")");
        }
        mapModel.addOverlay(getCalculationKMLGenerator().getLkpToDatum());
        for (Circle c : getCalculationKMLGenerator().getListShapeCircle()) {
            mapModel.addOverlay(c);
        }
    }

    public void initMcMapSearchArea() {
        mapModel = new DefaultMapModel();
        RequestContext.getCurrentInstance().execute("IconBasarnas()");
        RequestContext.getCurrentInstance().execute("PositionBarControl()");
        for (Marker m : getCalculationKMLGenerator().getListMarkerArea()) {
            mapModel.addOverlay(m);
        }
        for (Polyline p : getCalculationKMLGenerator().getListShapeSquere()) {
            mapModel.addOverlay(p);
        }
        for (MonteCarlo monteCarlo : listMonteCarlo) {
//                    Circle c = new Circle(monteCarlo.getDatum(), monteCarlo.getRadius());
//                    c.setStrokeColor("#FF0000");
//                    c.setFillColor("#000000");
//                    c.setFillOpacity(0.1);
//                    c.setStrokeOpacity(1.0);
//                    c.setStrokeWeight(5);
            System.out.println("Show map MC lat : " + monteCarlo.getDatum().getLat()
                    + ", lng : " + monteCarlo.getDatum().getLng());
            Marker m = new Marker(monteCarlo.getDatum());
            mapModel.addOverlay(m);
        }
    }

    public void initTrapezSearchArea() {
        mapModel = new DefaultMapModel();
        RequestContext.getCurrentInstance().execute("IconBasarnas()");
        RequestContext.getCurrentInstance().execute("PositionBarControl()");
        for (Polyline polyline : getTrapesiumKMLGenerator().getListPolyline()) {
            mapModel.addOverlay(polyline);
        }
        for (Marker marker : getTrapesiumKMLGenerator().getListMarker()) {
            mapModel.addOverlay(marker);
        }
        for (TrapeziumSearchArea tsa : listTrapeziumSearchArea) {
            List<LatLng> latLngs = new ArrayList<>();
            Polyline polyline = new Polyline();
            polyline.setStrokeColor("#FFFFFF");
            polyline.setStrokeWeight(3);
            polyline.setStrokeOpacity(1);
            latLngs.add(new LatLng(tsa.getSmallRoundPivot().getLat(),
                    tsa.getSmallRoundPivot().getLng()));
            latLngs.add(new LatLng(tsa.getLargeRoundPivot().getLat(),
                    tsa.getLargeRoundPivot().getLng()));
            polyline.setPaths(latLngs);

//            RequestContext.getCurrentInstance().execute(
//                    "addPolylineSearchArea('#FFFFFF',"
//                    + tsa.getSmallRoundPivot().getLat() + ","
//                    + tsa.getSmallRoundPivot().getLng() + ","
//                    + tsa.getLargeRoundPivot().getLat() + ","
//                    + tsa.getLargeRoundPivot().getLng() + ")");
        }
    }

    public void initCustomArea() {
        if (searchArea.getShape() != null) {
            Polyline polyline = new Polyline();
            JSONObject obj = new JSONObject(searchArea.getJsonMap());
            JSONArray arrOverlays = obj.getJSONArray("overlays");
            for (int i = 0; i < arrOverlays.length(); i++) {
                JSONObject objOverlays = arrOverlays.getJSONObject(i);
                if (objOverlays.getString("type").equals("Polyline")) {
                    polyline.setStrokeWeight(objOverlays.getInt("strokeWeight"));
                    polyline.setStrokeColor(objOverlays.getString("strokeColor"));
                    JSONArray arrPaths = objOverlays.getJSONArray("paths");
                    for (int ii = 0; ii < arrPaths.length(); ii++) {
                        JSONObject objPaths = arrPaths.getJSONObject(ii);
                        polyline.getPaths().add(new LatLng(objPaths.getDouble("lat"), objPaths.getDouble("lng")));
                    }
                    JSONObject objPaths = arrPaths.getJSONObject(0);
                    polyline.getPaths().add(new LatLng(objPaths.getDouble("lat"), objPaths.getDouble("lng")));
                } else if (objOverlays.getString("type").equals("Circle")) {
                    JSONObject objCenter = objOverlays.getJSONObject("center");
                    Circle circle = new Circle(new LatLng(
                            objCenter.getDouble("lat"),
                            objCenter.getDouble("lng")), objOverlays.getDouble("radius"));
                    circle.setStrokeColor(objOverlays.getString("strokeColor"));
                    circle.setFillColor(objOverlays.getString("fillColor"));
                    circle.setFillOpacity(objOverlays.getDouble("fillOpacity"));
                    circle.setStrokeOpacity(objOverlays.getDouble("strokeOpacity"));
                    circle.setStrokeWeight(objOverlays.getInt("strokeWeight"));
                    mapModel.addOverlay(circle);
                }
                if (objOverlays.getString("type").equals("Marker")) {
                    JSONObject objPosition = objOverlays.getJSONObject("position");
                    mapModel.addOverlay(new Marker(
                            new LatLng(
                                    objPosition.getDouble("lat"),
                                    objPosition.getDouble("lng")),
                            objOverlays.getString("title")));
                }
            }
            mapModel.addOverlay(polyline);
            RequestContext.getCurrentInstance().update("idMap");

        }
    }

    public void initTaskSearchArea() {
        if (!listTaskSearchArea.isEmpty()) {
            int index = 0;
            for (TaskSearchArea tsa : listTaskSearchArea) {
                System.out.println(tsa.toString());
                TaskAreaKMLGenerator taskAreaKMLGenerator = new TaskAreaKMLGenerator(tsa);
                listTaskSearchArea.set(index, tsa);
                mapModel.addOverlay(taskAreaKMLGenerator.getPolygonTaskArea());
                index++;
            }
        }
    }

    public void initTreeRouting() {
        treeRouting = null;
        List<SearchArea> searchAreas = searchAreaRepo.findByIncidentAndStatus(incident, true);
        if (!searchAreas.isEmpty()) {
            treeRouting = new DefaultTreeNode(new Document("Files", null, "Files"), null);
            TreeNode map = new DefaultTreeNode(new Document("Tree Routing", null, "Tree Routing"), treeRouting);
            for (SearchArea area : searchAreas) {
                TreeNode tnArea = new DefaultTreeNode("searcharea", new Document(
                        area.getSearchAreaID() + " - " + area.getName(), area, "searcharea"), map);

                List<IncTaskSearchArea> listTaskSearchArea = taskSearchAreaRepo.findBySearchAreaAndStatus(area, true);
                if (!listTaskSearchArea.isEmpty()) {
                    for (IncTaskSearchArea taskArea : listTaskSearchArea) {
                        TreeNode tnTaskArea
                                = new DefaultTreeNode("tasksearcharea",
                                        new Document(taskArea.getName(), taskArea, "tasksearcharea"), tnArea);
                        List<IncidentSearchPattern> listSearchPattern
                                = searchPatternRepo.findBySearchAreaAndTaskSearchAreaAndStatus(area, taskArea, true);
                        treeNodePattern(listSearchPattern, tnTaskArea);
                    }
                }
                List<IncidentSearchPattern> listSearchPattern
                        = searchPatternRepo.findBySearchAreaAndStatusAndTaskSearchAreaIsNull(area, true);
                treeNodePattern(listSearchPattern, tnArea);
            }
        }
    }

    private void treeNodePattern(List<IncidentSearchPattern> listSearchPattern, TreeNode parent) {
        if (!listSearchPattern.isEmpty()) {
            for (IncidentSearchPattern searchPattern : listSearchPattern) {
                if (searchPattern.getType().equals(MapPattern.EXPANDING_SQUARE_SEARCH.name())) {
                    TreeNode square
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "square"), parent);
                } else if (searchPattern.getType().equals(MapPattern.PARAREL_SEARCH.name())) {
                    TreeNode pararel
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "pararel"), parent);
                } else if (searchPattern.getType().equals(MapPattern.SECTOR_SEARCH.name())) {
                    TreeNode sector
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "sector"), parent);
                } else if (searchPattern.getType().equals(MapPattern.TRACK_SEARCH_NON_RETURN_SEARCH.name())) {
                    TreeNode tsn
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "tsn"), parent);
                } else if (searchPattern.getType().equals(MapPattern.TRACK_SEARCH_RETURN_SEARCH.name())) {
                    TreeNode tsr
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "tsr"), parent);
                } else if (searchPattern.getType().equals(MapPattern.TRAPEZIUM_PARAREL_SEARCH.name())) {
                    TreeNode trapezium
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "trapezium"), parent);
                } else if (searchPattern.getType().equals(MapPattern.SEARCH_PATTERN_FREE_DEFINE.name())) {
                    TreeNode trapezium
                            = new DefaultTreeNode("pattern",
                                    new Document(searchPattern.getName(), searchPattern, "free_define"), parent);
                }
            }
        }
    }

    public void showTreeRoutingProperties() {
        Document d = (Document) selectedTreeRouting.getData();
        if (selectedTreeRouting.getType().equals("searcharea")) {
            searchArea = (SearchArea) d.getObjClass();
        } else if (selectedTreeRouting.getType().equals("tasksearcharea")) {
            incTaskSearchArea = (IncTaskSearchArea) d.getObjClass();
        } else if (selectedTreeRouting.getType().equals("pattern")) {
            searchPattern = (IncidentSearchPattern) d.getObjClass();
        }
    }

    public boolean isClustercheck() {
        return clustercheck;
    }

    public void setClustercheck(boolean clustercheck) {
        this.clustercheck = clustercheck;
    }

    public void addMessage() {
        String summary = clustercheck ? "Checked" : "Unchecked";
        if (summary.equals("Unchecked")) {
            mapModel = new DefaultMapModel();
            setSetting();
            RequestContext.getCurrentInstance().update("idMap");
        }

        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public void setIconInsiden() {
        mapModel = new DefaultMapModel();
        showPoint();
        setSetting();
        //String summary = isIconInsiden ? "Checked" : "Unchecked";
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Icon Insiden", summary));
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void setPoi() {
        mapModel = new DefaultMapModel();
        showPoint();
        setSetting();
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void setBeacon() {
        mapModel = new DefaultMapModel();
        showPoint();
        setSetting();
        RequestContext.getCurrentInstance().update("idMap");
    }

    private void setSetting() {
        if (isBatasKansar) {
            setPolygonKansar();
        }
        if (isIconInsiden) {
            setSampleModel();
        }
        if (isPoi) {
            setPoiMarker();
        }
        if (isBeacon) {
            callBeacon();
        }
        if (isTraffic) {
            callTraffic();
        }
    }

    //DEDI
    public void setBatasKansar() {
        mapModel = new DefaultMapModel();
        showPoint();
        setSetting();
        //String summary = isBatasKansar ? "Checked" : "Unchecked";
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Batas Wilayah Kansar", summary));
        RequestContext.getCurrentInstance().update("idMap");
    }

    private void setPolygonKansar() {
        for (Integer kantor : mstWilayahKansarRepo.findAllGroupByKantorId()) {
            Polygon polygon = new Polygon();
            for (MstWilayahKansar kansar : mstWilayahKansarRepo.findByKantorIdOrderBySeqAsc(kantor)) {
                LatLng ll = new LatLng(Double.valueOf(kansar.getLatitude()), Double.valueOf(kansar.getLongitude()));
                polygon.getPaths().add(ll);
            }
            polygon.setStrokeColor("#FF9900");
            polygon.setFillColor("#FF9900");
            polygon.setStrokeOpacity(0.5);
            polygon.setFillOpacity(0.2);
            mapModel.addOverlay(polygon);
        }
        setLatLongKansar();
    }

    private void setLatLongKansar() {
        //setCenterGeoMap();
        for (MstKantorSar kantorsar : mstKantorSarRepo.findAll()) {
            try {
                if (kantorsar.getLatitude() != null && kantorsar.getLongitude() != null) {
                    LatLng coord = new LatLng(Double.valueOf(kantorsar.getLatitude()), Double.valueOf(kantorsar.getLongitude()));
                    mapModel.addOverlay(new Marker(coord,
                            kantorsar.getKantorsarname() + ", " + kantorsar.getAddress(),
                            "",
                            "http://maps.google.com/mapfiles/kml/pal2/icon10.png"));
                }
            } catch (Exception e) {

            }
        }
    }

    public void onPolygonSelect(OverlaySelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Polygon Selected", null));
    }
    //END

    private void setCenterGeoMap() {
//        centerGeoMap = "-2.6446973,116.2018089,5";
        centerGeoMap = "-1.105428, 120.0475485";
        zoomGeoMap = 4;
    }

    private void setSampleModel() {
        //setCenterGeoMap();
        List<Incident> listIncident = incidentRepo.findByStatus("Open");
        if (!listIncident.isEmpty()) {
            for (Incident incident : listIncident) {
                if (incident.getLatitude() != null && incident.getLongitude() != null) {
                    LatLng coord = new LatLng(Double.valueOf(incident.getLatitude()), Double.valueOf(incident.getLongitude()));
                    String iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/icon/incident.png");
                    mapModel.addOverlay(new Marker(coord, "Incident Number : " + incident.getIncidentnumber(), "", iconPath));
                }
            }
        }
    }

    public void setPoiMarker() {
        if (isPoi) {
            listPotensi = new ArrayList<>();
            if (listPotensi == null || listPotensi.isEmpty()) {
                listPotensi = potensiRepo.findAll();
                if (!listPotensi.isEmpty()) {
                    for (ResPotency potensi : listPotensi) {
                        if (potensi.getLatitude() != null && potensi.getLongitude() != null) {
                            try {
                                LatLng coord = new LatLng(Double.valueOf(
                                        potensi.getLatitude()),
                                        Double.valueOf(potensi.getLongitude()));
                                String iconPath = FacesContext.getCurrentInstance()
                                        .getExternalContext().getRequestContextPath()
                                        .concat("/resources/basarnas/images/icon/potency.png");
                                mapModel.addOverlay(new Marker(coord, "Potency Name : "
                                        + potensi.getPotencyname() + " " + potensi.getAddress(), "", iconPath));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void callTraffic() {
        if (isTraffic) {
            RequestContext.getCurrentInstance().execute("trafficLayerStart()");
        } else {
            RequestContext.getCurrentInstance().execute("trafficLayerEnd()");
        }
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void callBeacon() {
        if (isBeacon) {
            listBeacon = new ArrayList<>();
            if (listBeacon == null || listBeacon.isEmpty()) {
                listBeacon = beaconRepo.findAll();
                if (!listBeacon.isEmpty()) {
                    for (UtiBeaconComposite beacon : listBeacon) {
                        if (beacon.getLatitude() != null && beacon.getLongitude() != null) {
                            try {
                                LatLng coord = new LatLng(Double.valueOf(
                                        beacon.getLatitude()),
                                        Double.valueOf(beacon.getLongitude()));
                                String iconPath = FacesContext.getCurrentInstance()
                                        .getExternalContext().getRequestContextPath()
                                        .concat("/resources/basarnas/images/icon/beacon.png");

                                mapModel.addOverlay(new Marker(coord, "Beacon : "
                                        + beacon.getBeaconid() + " " + "Beacon Message: " + beacon.getLongmsg(), "",
                                        iconPath));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            setCenterGeoMap();
        }
//        else {
//            for (UtiBeaconComposite beacon : listBeacon) {
//                try {
//                    mapModel.getMarkers().remove(new Marker(new LatLng(Double.valueOf(beacon.getLatitude()), Double.valueOf(beacon.getLongitude()))));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }

    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        try {
            marker = (Marker) event.getOverlay();
            if (marker.getTitle().equals("Undo")) {
                Polyline poly = SearchPatternMBean.deleteLast();
                poly.getPaths().remove(new LatLng(poly.getPaths().get(poly.getPaths().size() - 1).getLat(), poly.getPaths().get(poly.getPaths().size() - 1).getLng()));
                List<LatLng> listLatlng = SearchPatternMBean.deleteList();
                List<LatLng> listLatlngCenter = SearchPatternMBean.getListCenter();
                listLatlng.removeIf((t) -> {
                    if (marker.getLatlng().getLat() == t.getLat() && marker.getLatlng().getLng() == t.getLng()) {
                        return true;
                    }
                    return false;
                });

                if (listLatlng.size() >= 1) {
                    String icon = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/undo_resize.png");
                    mapModel.addOverlay(new Marker(listLatlng.get(listLatlng.size() - 1), "Undo", null, icon));
                }
                mapModel.getMarkers().remove(new Marker(listLatlngCenter.get(listLatlngCenter.size() - 1)));
                mapModel.getMarkers().remove(marker);
                //mapModel.addOverlay(new Marker(listLatlngCenter.get(listLatlngCenter.size() - 2)));
                RequestContext.getCurrentInstance().update("idMap");
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "", marker.getTitle()));
        } catch (Exception e) {
//            polygon = (Polygon) event.getOverlay();
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Kansar :", marker.getTitle()));
        }
    }

    public void condition() {
        if (isIconInsiden) {
            setSampleModel();
//            if (isBatasKansar) {
//                setSampleModel();
//                setPolygonKansar();
//            }
        } else if (isBatasKansar) {
            setPolygonKansar();
            if (isIconInsiden) {
                setSampleModel();
                setPolygonKansar();
            }
        }
    }

    public void searchPoint() {
        if (StringUtils.isNotBlank(search)) {
            mapModel = new DefaultMapModel();
            RequestContext.getCurrentInstance().execute("PF('idnGmap').geocode('" + search + "')");
        } else {
//            setCenterGeoMap();
            setSetting();

        }
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void showPoint() {
        System.out.println("showPoint : " + search);
        if (StringUtils.isNotBlank(search)) {
            mapModel = new DefaultMapModel();
//            condition();
            RequestContext.getCurrentInstance().execute("PF('idnGmap').geocode('" + search + "')");
        } else {
//            setCenterGeoMap();
            setSetting();
            RequestContext.getCurrentInstance().update("idMap");
        }

    }

    public void onGeocode(GeocodeEvent event) {
        System.out.println("onGeocode");
        List<GeocodeResult> results = event.getResults();
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            zoomGeoMap = 8;
            centerGeoMap = center.getLat() + "," + center.getLng();

            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                mapModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
    }

    public void barClick() {
        if ("Measure".equals(radiobutton)) {
            RequestContext.getCurrentInstance().execute("measurenew()");
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("radioValue", radiobutton);
        } else if ("PLACEMARK".equals(radiobutton)) {
            RequestContext.getCurrentInstance().execute("measureStop()");
            mapSearch = MapSearch.PLACEMARK;
            if (incident != null) {
                initDcMapSearchArea();
                RequestContext.getCurrentInstance().update("idMap");
            }
//            System.out.println("Ini Placemark: Muncul");
//            mapModel = new DefaultMapModel();
//            condition();
//            RequestContext.getCurrentInstance().reset("idDialogPlacemark");
//            RequestContext.getCurrentInstance().execute("PF('showDialogPlacemark').show()");
        } else {
            RequestContext.getCurrentInstance().execute("measureStop()");
            /* coment by irfan
            condition();
            RequestContext.getCurrentInstance().execute("measureEnd()");
            RequestContext.getCurrentInstance().update("idMap");
            System.out.println("Screenshoot" + radiobutton);*/
        }
    }

    public void onReturnList(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(search, search));
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
