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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.web.controller.MapMBean;
import prosia.basarnas.web.model.Vertexs;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.model.map.MonteCarlo;
import prosia.basarnas.web.model.map.SearchAreaDialog;
import prosia.basarnas.web.model.map.SearchPatternParameter;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.map.DriftCalculationKMLGenerator;
import prosia.basarnas.web.util.map.MapCalculation;

/**
 *
 * @author Ismail
 */
@Component
@Scope("view")
@Data
public class SearchAreaMBean implements Serializable {

    @Autowired
    private MapMBean mapMBean;
    @Autowired
    private SearchPatternMBean searchPatternMBean;
    @Autowired
    private SearchAreaRepo searchAreaRepo;

    private String start;
    private List<Vertexs> listVertexs;
    private SearchAreaDialog areaDialog;
    private Coordinate latitudeSad;
    private Coordinate longitudeSad;
    private List<Integer> listWaypoint;
    private Integer waypointIndex;
    private Boolean isNewTrapezium;

    public SearchAreaMBean() {
        latitudeSad = new Coordinate();
        longitudeSad = new Coordinate();
        listVertexs = new ArrayList<>();
        listWaypoint = new ArrayList<>();
        waypointIndex = 1;
        isNewTrapezium = false;
    }

    public void showDialogSAD() {
        areaDialog = new SearchAreaDialog();
        convCoordinateSad(
                Double.valueOf(mapMBean.getIncident().getLatitude()),
                Double.valueOf(mapMBean.getIncident().getLongitude()));

        RequestContext.getCurrentInstance().reset("idDialogSAD");
        RequestContext.getCurrentInstance().update("idDialogSAD");
        RequestContext.getCurrentInstance().execute("PF('showDialogSAD').show()");
    }

    public void createArea() {
//        searchPatternMBean.setTreeSearchPattern(null);
//        searchPatternMBean.gettListPatternParameter().clear();
        latitudeSad.converter();
        longitudeSad.converter();

//        GenerateValueRequestBean generateValueRequestBean = mapMBean.getGenerateValueRequestBean();
        GenerateValueRequestBean generateValueRequestBean = new GenerateValueRequestBean();
        generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA);
        LatLng centerCoordinate = new LatLng(latitudeSad.getValidDecimalDegrees(), longitudeSad.getValidDecimalDegrees());
//        Double radius = MapCalculation.convertToKm(areaDialog.getSatuan(), areaDialog.getSearchNilai()) / 1.852;
        Double radius = MapCalculation.convertToKm(areaDialog.getSatuan(), areaDialog.getSearchNilai());
        generateValueRequestBean.setShape(areaDialog.getShape());
        /*
             * Incident position equals with dataum and LKP position
         */
        generateValueRequestBean.setDatumPoint(centerCoordinate);
        generateValueRequestBean.setLkpPoint(centerCoordinate);
        if (areaDialog.getSearchHeading() == null) {
            generateValueRequestBean.setTiltDrift(Double.valueOf(0));
            generateValueRequestBean.setLkpToDatumAngle(Double.valueOf(0));
        } else {
            generateValueRequestBean.setTiltDrift(areaDialog.getSearchHeading());
            generateValueRequestBean.setLkpToDatumAngle(areaDialog.getSearchHeading());
        }

        generateValueRequestBean.setLkpToDatumDistance(Double.valueOf(0));
        generateValueRequestBean.setRadiusDrift(radius);
        LatLng orgLeftCoord = new LatLng(Double.valueOf(mapMBean.getIncident().getLatitude()),
                Double.valueOf(mapMBean.getIncident().getLongitude()) - (radius / 60));
        LatLng orgRightCoord = new LatLng(Double.valueOf(mapMBean.getIncident().getLatitude()),
                Double.valueOf(mapMBean.getIncident().getLongitude()) + (radius / 60));
        generateValueRequestBean.setLkpToDatumDistance(Double.valueOf(0));
        LatLng centerLatLng = new LatLng(generateValueRequestBean.getDatumPoint().getLat(),
                generateValueRequestBean.getDatumPoint().getLng());
        orgLeftCoord = MapCalculation.rotation(centerLatLng, orgLeftCoord, radius);
        LatLng leftDift = new LatLng(orgLeftCoord.getLat(), orgLeftCoord.getLng());
        generateValueRequestBean.setDriftLeftPoint(leftDift);
        orgRightCoord = MapCalculation.rotation(centerLatLng, orgRightCoord, radius);
        LatLng rightDift = new LatLng(orgRightCoord.getLat(), orgRightCoord.getLng());
        generateValueRequestBean.setDriftRightPoint(rightDift);
        generateValueRequestBean.setDrawLeewayLine(false);

        mapMBean.setCalculationKMLGenerator(new DriftCalculationKMLGenerator(generateValueRequestBean));

        mapMBean.setCenterGeoMap(Double.valueOf(mapMBean.getIncident().getLatitude())
                + "," + Double.valueOf(mapMBean.getIncident().getLongitude()));
        mapMBean.setZoomGeoMap(5);
        mapMBean.setGenerateValueRequestBean(generateValueRequestBean);
        mapMBean.initDcMapSearchArea();
        mapMBean.setMapCalculationType(MapCalculationType.DRIFT_CALCULATION);
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().update("idMap");
        RequestContext.getCurrentInstance().execute("PF('showDialogSAD').hide()");
    }

    public void showDialogSave() {

        if (mapMBean.getMapCalculationType() == null) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                    "Tidak ada Search Area, buat Search Area terlebih dahulu");
            return;
        }
        mapMBean.setSearchArea(new SearchArea());

        listVertexs = new ArrayList<>();
        listWaypoint = new ArrayList<>();
        char alphabet = 'A';
        if (mapMBean.getMapCalculationType().equals(MapCalculationType.DRIFT_CALCULATION)
                ||mapMBean.getMapCalculationType().equals(MapCalculationType.MONTECARLO)) {
            List<LatLng> lsVertexs = mapMBean.getGenerateValueRequestBean().getVertexs();
            for (LatLng ver : lsVertexs) {
                Vertexs v = new Vertexs();
                v.setSequence(String.valueOf(alphabet++));
                v.setLatitude(convertLatLng(ver.getLat(), true));
                v.setLongitude(convertLatLng(ver.getLng(), false));
                listVertexs.add(v);
            }
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            for (LatLng ll : mapMBean.getGenerateValueRequestBean().getVertexs()) {
                array.put(new JSONObject(ll));
            }
            json.put("Area", array);
            json.put("Start", new JSONObject(mapMBean.getGenerateValueRequestBean().getVertexs().get(0)));
            mapMBean.getSearchArea().setArea(json.toString());
            start = convertLatLng(mapMBean.getGenerateValueRequestBean().getVertexs().get(0).getLat(), true)
                    + " - "
                    + convertLatLng(mapMBean.getGenerateValueRequestBean().getVertexs().get(0).getLng(), false);
            mapMBean.getSearchArea().setRadius(
                    mapMBean.getGenerateValueRequestBean().getShape() != null
                            ? mapMBean.getGenerateValueRequestBean().getRadiusDrift() : null);
            mapMBean.getSearchArea().setShape(mapMBean.getGenerateValueRequestBean().getShape());
            mapMBean.getSearchArea().setParrentID(mapMBean.getGenerateValueRequestBean().getParrentId());
        } else if (mapMBean.getMapCalculationType().equals(MapCalculationType.TRAPEZIUM)) {
            isNewTrapezium = true;
            waypointIndex = 1;
            for (int i = 1; i <= mapMBean.getListTrapeziumSearchArea().size(); i++) {
                listWaypoint.add(i);
            }
            TrapeziumSearchArea trapeziumSearchArea = mapMBean.getListTrapeziumSearchArea().get(waypointIndex - 1);
            for (LatLng ver : trapeziumSearchArea.getVertexs()) {
                Vertexs v = new Vertexs();
                v.setSequence(String.valueOf(alphabet++));
                v.setLatitude(convertLatLng(ver.getLat(), true));
                v.setLongitude(convertLatLng(ver.getLng(), false));
                listVertexs.add(v);
            }
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            for (LatLng ll : trapeziumSearchArea.getVertexs()) {
                array.put(new JSONObject(ll));
            }
            json.put("Area", array);
            json.put("Start", new JSONObject(trapeziumSearchArea.getVertexs().get(0)));
            mapMBean.getSearchArea().setArea(json.toString());
            start = convertLatLng(trapeziumSearchArea.getVertexs().get(0).getLat(), true)
                    + " - "
                    + convertLatLng(trapeziumSearchArea.getVertexs().get(0).getLng(), false);
            mapMBean.getSearchArea().setRadius(0.0);
            mapMBean.getSearchArea().setShape(MapCalculationType.TRAPEZIUM.name());
            mapMBean.getSearchArea().setParrentID(trapeziumSearchArea.getParrentID());
        }
        mapMBean.getSearchArea().setIncident(mapMBean.getIncident());
        RequestContext.getCurrentInstance().reset("idDialogSaveSearchArea");
        RequestContext.getCurrentInstance().update("idDialogSaveSearchArea");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchArea').show()");
    }

    public void showDialogEdit() {
        isNewTrapezium = false;
        listVertexs = new ArrayList<>();
        if (StringUtils.isNotBlank(mapMBean.getSearchArea().getJsonMap())) {
            JSONObject object = new JSONObject(mapMBean.getSearchArea().getJsonMap());
            JSONArray overlays = object.getJSONArray("overlays");
            System.out.println("overlays " + overlays.length());
            for (int i = 0; i < overlays.length(); i++) {
                JSONObject obj = overlays.getJSONObject(i);
                if (obj.getString("type").equals("Circle")) {

                }
                if (obj.getString("type").equals("Marker")) {
                    Vertexs v = new Vertexs();
                    v.setSequence(obj.getString("title").replace("Poin ", ""));
                    JSONObject jsonLatLng = obj.getJSONObject("position");
                    v.setLatitude(convertLatLng(jsonLatLng.getDouble("lat"), true));
                    v.setLongitude(convertLatLng(jsonLatLng.getDouble("lng"), false));
                    listVertexs.add(v);
                    if (obj.getString("title").replace("Poin ", "").equals("A")) {
                        start = convertLatLng(jsonLatLng.getDouble("lat"), true) + " - "
                                + convertLatLng(jsonLatLng.getDouble("lng"), false);
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().reset("idDialogSaveSearchArea");
        RequestContext.getCurrentInstance().update("idDialogSaveSearchArea");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchArea').show()");
    }

    public void onWaypointChange() {
        mapMBean.getSearchArea().setName(null);
        mapMBean.getSearchArea().setDescription(null);
        listVertexs = new ArrayList<>();
        char alphabet = 'A';
        TrapeziumSearchArea trapeziumSearchArea = mapMBean.getListTrapeziumSearchArea().get(waypointIndex - 1);
        for (LatLng ver : trapeziumSearchArea.getVertexs()) {
            Vertexs v = new Vertexs();
            v.setSequence(String.valueOf(alphabet++));
            v.setLatitude(convertLatLng(ver.getLat(), true));
            v.setLongitude(convertLatLng(ver.getLng(), false));
            listVertexs.add(v);
        }
        start = convertLatLng(trapeziumSearchArea.getVertexs().get(0).getLat(), true)
                + " - "
                + convertLatLng(trapeziumSearchArea.getVertexs().get(0).getLng(), false);
        mapMBean.getSearchArea().setParrentID(trapeziumSearchArea.getParrentID());
        RequestContext.getCurrentInstance().update("idPanelSA");
    }

    public void saveSearchArea() {
        if (mapMBean.getSearchArea().getSearchAreaID() == null) {
            mapMBean.getSearchArea().setSearchAreaID(generateIdSeq());
        }
        char alphabet = 'A';
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonOverlay = new JSONArray();
        JSONObject jsonArea = new JSONObject();
        if (mapMBean.getMapCalculationType().equals(MapCalculationType.DRIFT_CALCULATION)) {
            if (mapMBean.getGenerateValueRequestBean().getShape() == null
                    || mapMBean.getGenerateValueRequestBean().getShape() == "Square") {
                jsonArea.put("type", "Polyline");
                jsonArea.put("strokeColor", "#FF0000");
                jsonArea.put("strokeWeight", 3);
                JSONArray array = new JSONArray();
                for (LatLng ll : mapMBean.getGenerateValueRequestBean().getVertexs()) {
                    array.put(new JSONObject(ll));
                }
                jsonArea.put("paths", array);
            } else {
                jsonArea.put("type", "Circle");
                jsonArea.put("strokeColor", "#7FFF00");
                jsonArea.put("fillColor", "#000000");
                jsonArea.put("fillOpacity", 0.1);
                jsonArea.put("strokeOpacity", 1.0);
                jsonArea.put("strokeWeight", 5);
                jsonArea.put("center", new JSONObject(mapMBean.getGenerateValueRequestBean().getLkpPoint()));
                jsonArea.put("radius", (mapMBean.getGenerateValueRequestBean().getRadiusDrift() * 1.852) * 1000);
            }
            jsonOverlay.put(jsonArea);
            for (LatLng latLng : mapMBean.getGenerateValueRequestBean().getVertexs()) {
                JSONObject jsonMarker = new JSONObject();
                jsonMarker.put("type", "Marker");
                jsonMarker.put("title", "Poin " + String.valueOf(alphabet++));
                jsonMarker.put("position", new JSONObject(latLng));
                jsonOverlay.put(jsonMarker);
            }
        } else if (mapMBean.getMapCalculationType().equals(MapCalculationType.TRAPEZIUM)) {
            TrapeziumSearchArea trapeziumSearchArea = mapMBean.getListTrapeziumSearchArea().get(waypointIndex - 1);
            jsonArea.put("type", "Polyline");
            jsonArea.put("strokeColor", "#FF0000");
            jsonArea.put("strokeWeight", 3);
            JSONArray array = new JSONArray();
            for (LatLng ll : trapeziumSearchArea.getVertexs()) {
                array.put(new JSONObject(ll));
            }
            jsonArea.put("paths", array);
            jsonOverlay.put(jsonArea);
            for (LatLng latLng : trapeziumSearchArea.getVertexs()) {
                JSONObject jsonMarker = new JSONObject();
                jsonMarker.put("type", "Marker");
                jsonMarker.put("title", "Poin " + String.valueOf(alphabet++));
                jsonMarker.put("position", new JSONObject(latLng));
                jsonOverlay.put(jsonMarker);
            }
        } else if (mapMBean.getMapCalculationType().equals(MapCalculationType.MONTECARLO)) {
            jsonArea.put("type", "Polyline");
            jsonArea.put("strokeColor", "#FF0000");
            jsonArea.put("strokeWeight", 3);
            JSONArray array = new JSONArray();
            for (LatLng ll : mapMBean.getGenerateValueRequestBean().getVertexs()) {
                array.put(new JSONObject(ll));
            }
            jsonArea.put("paths", array);
            jsonOverlay.put(jsonArea);
            for (LatLng latLng : mapMBean.getGenerateValueRequestBean().getVertexs()) {
                JSONObject jsonMarker = new JSONObject();
                jsonMarker.put("type", "Marker");
                jsonMarker.put("title", "Poin " + String.valueOf(alphabet++));
                jsonMarker.put("position", new JSONObject(latLng));
                jsonOverlay.put(jsonMarker);
            }
            for (MonteCarlo monteCarlo : mapMBean.getListMonteCarlo()) {
                JSONObject jsonMarker = new JSONObject();
                jsonMarker.put("type", "Marker");
                jsonMarker.put("position", new JSONObject(monteCarlo.getDatum()));
                jsonOverlay.put(jsonMarker);
            }
        }
        jsonObj.put("overlays", jsonOverlay);

        mapMBean.getSearchArea().setJsonMap(jsonObj.toString());
        mapMBean.getSearchArea().setStatus(true);
        searchAreaRepo.save(mapMBean.getSearchArea());
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Simpan Berhasil");
        mapMBean.initTreeRouting();
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchArea').hide()");
    }

    public void removeSearchArea() {
        mapMBean.getSearchArea().setStatus(false);
        searchAreaRepo.save(mapMBean.getSearchArea());
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi", "Hapus Berhasil");
        mapMBean.initTreeRouting();

        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().execute("PF('showDialogSaveSearchArea').hide()");
    }

    public void clearSearchArea() {
        mapMBean.setMapModel(new DefaultMapModel());
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance().update("idMap");
    }

    public void viewOnMap() {
//        if (mapMBean.getSearchArea().getShape() == null) {
        Polyline polyline = new Polyline();
        JSONObject obj = new JSONObject(mapMBean.getSearchArea().getJsonMap());
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
            }
            if (objOverlays.getString("type").equals("Circle")) {
                JSONObject objCenter = objOverlays.getJSONObject("center");
                Circle circle = new Circle(
                        new LatLng(
                                objCenter.getDouble("lat"), objCenter.getDouble("lng")),
                        objOverlays.getInt("radius"));
                circle.setFillOpacity(objOverlays.getInt("fillOpacity"));
                circle.setFillColor(objOverlays.getString("fillColor"));
                circle.setStrokeWeight(objOverlays.getInt("strokeWeight"));
                circle.setStrokeColor(objOverlays.getString("strokeColor"));
                circle.setStrokeOpacity(objOverlays.getInt("strokeOpacity"));
                mapMBean.getMapModel().addOverlay(circle);
            }
            if (objOverlays.getString("type").equals("Marker")) {
                JSONObject objPosition = objOverlays.getJSONObject("position");
                String title = null;
                if (objOverlays.has("title")) {
                    title = objOverlays.getString("title");
                }
                mapMBean.getMapModel().addOverlay(new Marker(
                        new LatLng(
                                objPosition.getDouble("lat"),
                                objPosition.getDouble("lng")), title));
            }
        }
        mapMBean.getMapModel().addOverlay(polyline);
        RequestContext.getCurrentInstance().update("idMap");
//        }

    }

    private String convertLatLng(Double coordinate, boolean typeLatLng) {
        Coordinate c = new Coordinate();
        return c.getConvertDdToDms(coordinate.toString(), typeLatLng);
    }

    private void convCoordinateSad(Double lat, Double lng) {
        latitudeSad = new Coordinate();
        longitudeSad = new Coordinate();
        latitudeSad.setType(true);
        latitudeSad.setDecimalDegrees(lat);
        latitudeSad.setCounter(1);
        latitudeSad.converter();
        latitudeSad.setCounter(3);
        longitudeSad.setType(false);
        longitudeSad.setDecimalDegrees(lng);
        longitudeSad.setCounter(1);
        longitudeSad.converter();
        longitudeSad.setCounter(3);
    }

    public void actionLatitudeSad() {
        latitudeSad.converter();
        latitudeSad.setCounter(latitudeSad.getCounter() + 1);
        if (latitudeSad.getCounter() > 3) {
            latitudeSad.setCounter(1);
        }
    }

    public void actionLongitudeSad() {
        longitudeSad.converter();
        longitudeSad.setCounter(longitudeSad.getCounter() + 1);
        if (longitudeSad.getCounter() > 3) {
            longitudeSad.setCounter(1);
        }
    }

    private String generateIdSeq() {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<SearchArea> ias = searchAreaRepo.findAllById("CGK");
        String id = null;
        if (ias.isEmpty()) {
            id = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = searchAreaRepo.findByMaxId("CGK");
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

    private void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }

}
