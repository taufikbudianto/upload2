/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.DriftCalcWorksheet1;
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.DriftCalcWorksheet3;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.repo.DriftCalcWorksheet1Repo;
import prosia.basarnas.repo.DriftCalcWorksheet2Repo;
import prosia.basarnas.repo.DriftCalcWorksheet3Repo;
import prosia.basarnas.repo.DriftCalcWorksheet8Repo;
import prosia.basarnas.repo.IncidentPlanningRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.SearchAreaRepo;
import prosia.basarnas.web.rest.dto.PlanningMainModelWs;
import prosia.basarnas.web.rest.dto.PlanningWs1;
import prosia.basarnas.web.rest.dto.PlanningWs2;
import prosia.basarnas.web.rest.dto.PlanningWs3;
import prosia.basarnas.web.rest.dto.PlanningWs8;

/**
 *
 * @author Taufik AB
 */
@RestController
public class PlanningControllerWs {

    @Autowired
    private IncidentPlanningRepo incPlanningRepo;
    @Autowired
    private DriftCalcWorksheet1Repo driftCalcWorksheet1Repo;
    @Autowired
    private DriftCalcWorksheet2Repo driftCalcWorksheet2Repo;
    @Autowired
    private DriftCalcWorksheet3Repo driftCalcWorksheet3Repo;
    @Autowired
    private DriftCalcWorksheet8Repo driftCalcWorksheet8Repo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private SearchAreaRepo areaRepo;

    @Autowired
    private EntityManager entityManager;

    @RequestMapping(value = "/planning/drift/{incidentid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataIncidentDetail(@PathVariable String incidentid) {
        Incident incident = incidentRepo.findAllByIncidentid(incidentid);
        Response response = new Response();
        if (incident == null) {
            response.setResponseCode(ResponseCode.INVALID_PARAMS);
            response.setResponseMessage("Incident ID Tidak Tersedia");
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        } else if (!incident.getStatus().equals("Open")) {
            response.setResponseCode(ResponseCode.INVALID_PARAMS);
            response.setResponseMessage("Incident Sudah Close");
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        List<DriftCalcWorksheet1> listDriftCalc1 = driftCalcWorksheet1Repo.findByIncidentAndDeleted(incident, false);
        List<DriftCalcWorksheet2> listDriftCalc2 = driftCalcWorksheet2Repo.findAllByIncidentAndDeletedFalse(incident);
        List<DriftCalcWorksheet3> listDriftCalc3 = driftCalcWorksheet3Repo.findAllByIncidentAndDeletedFalse(incident);
        List<DriftCalcWorksheet8> listDriftCalc8 = driftCalcWorksheet8Repo.findAllByIncidentAndDeletedFalse(incident);

        System.out.println(listDriftCalc1.size());
        if (listDriftCalc1.isEmpty() && listDriftCalc2.isEmpty() && listDriftCalc3.isEmpty() && listDriftCalc8.isEmpty()) {
            response.setResponseCode(ResponseCode.ZERO_RESULT);
            response.setResponseMessage("DriftCalculation null (Tidak ada driftcalculation di workssheet ini)");
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        List<PlanningWs1> listSendPlanningDfc1 = new ArrayList<>();
        List<PlanningWs2> listSendPlanningDfc2 = new ArrayList<>();
        List<PlanningWs3> listSendPlanningDfc3 = new ArrayList<>();
        List<PlanningWs8> listSendPlanningDfc8 = new ArrayList<>();
        for (DriftCalcWorksheet1 dfc1 : listDriftCalc1) {
            PlanningWs1 planningWs = new PlanningWs1();
            planningWs.setDatum_latitude(dfc1.getDatumLatitude());
            planningWs.setDatum_longitude(dfc1.getDatumLongitude());
            //planningWs.setDescription(dfc1.get);
            planningWs.setDistress_craft_error(dfc1.getDistressCraftError());
            planningWs.setDrif_left_to_right_distance(dfc1.getDriftLeftToRightDistance());
            planningWs.setDrift_error(dfc1.getDriftError());
            planningWs.setDrift_error_percentage(dfc1.getDriftErrorPercentage());
            planningWs.setDrift_left_distance(dfc1.getDriftLeftDistance());
            planningWs.setDrift_left_latitude(dfc1.getDriftLeftLatitude());
            planningWs.setDrift_left_longitude(dfc1.getDriftLeftLongitude());
            planningWs.setDrift_left_to_right_angle(dfc1.getDriftLeftToRightAngle());
            planningWs.setDrift_right_distance(dfc1.getDriftRightDistance());
            planningWs.setDrift_right_latitude(dfc1.getDriftRightLatitude());
            planningWs.setDrift_right_longitude(dfc1.getDriftRightLongitude());
            planningWs.setIncident_time(String.valueOf(dfc1.getIncidentTime()));
            planningWs.setIncident_to_datum_angle(dfc1.getIncToDatumAngle());
            planningWs.setLatitude(dfc1.getIncidentLatitude());
            planningWs.setLongitude(dfc1.getIncidentLongitude());
            planningWs.setLeeway_distance(dfc1.getLeewayDistance());
            planningWs.setLeeway_divergence_angle(dfc1.getLeewayDistance());
            planningWs.setLeeway_left_angle(dfc1.getLeewayLeftAngle());
            planningWs.setLeeway_right_angle(dfc1.getLeewayRightAngle());
            planningWs.setLeeway_speed(dfc1.getLeewaySpeed());
            planningWs.setOperation_time(String.valueOf(dfc1.getOperationTime()));
            planningWs.setProbable_error_total(dfc1.getTotalProbableError());
            planningWs.setSafety_factor(dfc1.getSafetyFactor());
            planningWs.setSea_current_angle(dfc1.getSeaCurrentAngle());
            planningWs.setSea_current_distance(dfc1.getSeaCurrentDistance());
            planningWs.setSea_current_speed(dfc1.getSeaCurrentSpeed());
            planningWs.setSearch_area(dfc1.getSearchArea());//ko double
            planningWs.setSearch_craft_error(dfc1.getSearchCraftError());
            planningWs.setSearch_radius(dfc1.getSearchRadius());
            planningWs.setSurface_wind_angle(dfc1.getSurfaceWindAngle());
            planningWs.setUnit(String.valueOf(dfc1.getUnit()));
            planningWs.setWind_current_angle(dfc1.getWindCurrentAngle());
            planningWs.setWind_current_distance(dfc1.getWindCurrentDistance());
            planningWs.setWind_current_speed_cal_distance(dfc1.getWindCurrentSpeedForCalcDist());
            planningWs.setWorksheet_name(dfc1.getWorksheetName());
            SearchArea searcharea = null;
            try {
                TypedQuery query = entityManager
                        .createQuery("SELECT s FROM SearchArea s where s.incident = :incident order by s.createdDate desc", SearchArea.class);
                query.setParameter("incident", dfc1.getIncident());
                query.setMaxResults(1);
                searcharea = (SearchArea) query.getSingleResult();
                JSONObject jsObj = new JSONObject(searcharea.getJsonMap());

                PlanningWs1.SearchArea sa = new PlanningWs1.SearchArea();
                List<Object> listObject = new ArrayList<>();
                JSONObject json = new JSONObject(String.valueOf(jsObj));
                JSONArray overlays = (JSONArray) json.get("overlays");
                for (int i = 1; i < overlays.length(); i++) {
                    JSONObject position = (JSONObject) overlays.get(i);
                    JSONObject jsonData2 = new JSONObject(position.get("position").toString());
                    PlanningWs1.LatitudeLongitude latlongitude = new PlanningWs1.LatitudeLongitude();
                    latlongitude.setLat(jsonData2.getDouble("lat"));
                    latlongitude.setLng(jsonData2.getDouble("lng"));
                    PlanningWs1.Position posisi = new PlanningWs1.Position();
                    posisi.setPosition(latlongitude);
                    posisi.setTitle((String) position.get("title"));
                    posisi.setType((String) position.get("type"));
                    listObject.add(posisi);
                }
                JSONObject jsOverLay = (JSONObject) overlays.get(0);//,"":"Marker","":"Poin D"
                JSONArray paths = (JSONArray) jsOverLay.get("paths");
                PlanningWs1.Paths pathmodel = new PlanningWs1.Paths();
                List<PlanningWs1.LatitudeLongitude> latlong = new ArrayList<>();
                for (Object dataJson : paths) {
                    JSONObject jsonData = new JSONObject(dataJson.toString());
                    PlanningWs1.LatitudeLongitude latlongitude = new PlanningWs1.LatitudeLongitude();
                    latlongitude.setLat(jsonData.getDouble("lat"));
                    latlongitude.setLng(jsonData.getDouble("lng"));
                    latlong.add(latlongitude);
                }
                pathmodel.setPaths(latlong);
                pathmodel.setStrokeColor((String) jsOverLay.get("strokeColor"));
                pathmodel.setStrokeWeight(jsOverLay.getDouble("strokeWeight"));
                pathmodel.setType((String) jsOverLay.get("type"));
                listObject.add(pathmodel);
                sa.setOverlays(listObject);
                planningWs.setSearch_area_on_map(sa);
            } catch (Exception e) {
            }
            try {
                TypedQuery querySP = entityManager
                        .createQuery("SELECT s FROM IncidentSearchPattern s where s.searchArea = :searchArea order by s.createdDate desc", IncidentSearchPattern.class);
                querySP.setParameter("searchArea", searcharea);
                querySP.setMaxResults(1);
                IncidentSearchPattern searchpattern = (IncidentSearchPattern) querySP.getSingleResult();
                JSONObject jsObjPat = new JSONObject(searchpattern.getJsonMap());

                PlanningWs1.SearchPattern spp = new PlanningWs1.SearchPattern();
                List<Object> listObjectPat = new ArrayList<>();
                JSONObject jsonObjSp = new JSONObject(String.valueOf(jsObjPat));
                JSONArray overlaysSp = (JSONArray) jsonObjSp.get("overlays");
                JSONObject jsOverLaySp = (JSONObject) overlaysSp.get(0);//,"":"Marker","":"Poin D"
                JSONArray pathsSP = (JSONArray) jsOverLaySp.get("paths");
                PlanningWs1.Paths pathmodelSp = new PlanningWs1.Paths();
                List<PlanningWs1.LatitudeLongitude> latlongSP = new ArrayList<>();
                for (Object dataJson : pathsSP) {
                    JSONObject jsonData = new JSONObject(dataJson.toString());
                    PlanningWs1.LatitudeLongitude latlongitude = new PlanningWs1.LatitudeLongitude();
                    latlongitude.setLat(jsonData.getDouble("lat"));
                    latlongitude.setLng(jsonData.getDouble("lng"));
                    latlongSP.add(latlongitude);
                }
                pathmodelSp.setPaths(latlongSP);
                pathmodelSp.setStrokeColor((String) jsOverLaySp.get("strokeColor"));
                pathmodelSp.setStrokeWeight(jsOverLaySp.getDouble("strokeWeight"));
                pathmodelSp.setType((String) jsOverLaySp.get("type"));
                listObjectPat.add(pathmodelSp);
                spp.setOverlays(listObjectPat);
                planningWs.setSearch_pattern(spp);
            } catch (Exception e) {
            }
            try {
                TypedQuery queryTask = entityManager
                        .createQuery("SELECT s FROM IncTaskSearchArea s where s.searchArea = :search order by s.createdDate desc", IncTaskSearchArea.class);
                queryTask.setParameter("search", searcharea);
                queryTask.setMaxResults(1);
                IncTaskSearchArea searchtask = (IncTaskSearchArea) queryTask.getSingleResult();
                JSONObject jsObjTask = new JSONObject(searchtask.getJsonMap());

                PlanningWs1.TaskSearch taskSearch = new PlanningWs1.TaskSearch();
                List<Object> listObjectTask = new ArrayList<>();
                JSONObject jsonObjTask = new JSONObject(String.valueOf(jsObjTask));
                JSONArray overlaysTask = (JSONArray) jsonObjTask.get("overlays");
                JSONObject jsOverLayTask = (JSONObject) overlaysTask.get(0);//,"":"Marker","":"Poin D"
                JSONArray pathsTask = (JSONArray) jsOverLayTask.get("paths");
                PlanningWs1.PathsTask pathmodelTask = new PlanningWs1.PathsTask();
                List<PlanningWs1.LatitudeLongitude> latlongTask = new ArrayList<>();
                for (Object dataJson : pathsTask) {
                    JSONObject jsonData = new JSONObject(dataJson.toString());
                    PlanningWs1.LatitudeLongitude latlongitude = new PlanningWs1.LatitudeLongitude();
                    latlongitude.setLat(jsonData.getDouble("lat"));
                    latlongitude.setLng(jsonData.getDouble("lng"));
                    latlongTask.add(latlongitude);
                }
                pathmodelTask.setPaths(latlongTask);
                pathmodelTask.setStrokeColor((String) jsOverLayTask.get("strokeColor"));
                pathmodelTask.setStrokeWeight(jsOverLayTask.getDouble("strokeWeight"));
                pathmodelTask.setType((String) jsOverLayTask.get("type"));
                pathmodelTask.setFillOpacity(jsOverLayTask.getDouble("fillOpacity"));
                pathmodelTask.setStrokeOpacity(jsOverLayTask.getDouble("strokeOpacity"));
                pathmodelTask.setStrokeColor((String) jsOverLayTask.get("fillColor"));
                pathmodelTask.setFillColor((String) jsOverLayTask.get("fillColor"));
                listObjectTask.add(pathmodelTask);
                taskSearch.setOverlays(listObjectTask);
                planningWs.setSearch_task_area(taskSearch);
            } catch (Exception e) {
            }

            listSendPlanningDfc1.add(planningWs);
        }
        for (DriftCalcWorksheet2 dfc2 : listDriftCalc2) {
            PlanningWs2 ws2 = new PlanningWs2();
            ws2.setCoverage_factor(dfc2.getCoverageFactor());
            ws2.setFatigue_correction_factor(dfc2.getFatigueCorrectionFactor());
            ws2.setFatigue_factor(dfc2.getFatigueFactor());
            ws2.setMeteorlogical_visibility(dfc2.getMeteorologicalVisibility());
            ws2.setPractical_track_spacing(dfc2.getPracticalTrackSpacing());
            ws2.setProbability_detection(dfc2.getProbabilityOfDetection());
            ws2.setSearch_area(dfc2.getSearchArea());
            ws2.setSearch_height(dfc2.getSearchHeight());
            ws2.setSearch_hours(dfc2.getSearchhours());
            ws2.setSearch_object(dfc2.getSearchObject());
            ws2.setSearch_platform(dfc2.getSearchPlatform());
            ws2.setSearch_platform_tas(dfc2.getSearchPlatformTas());
            ws2.setSearch_repeat(dfc2.getSearchRepeat());
            ws2.setSpeed_correction_factor(dfc2.getSpeedCorrectionFactor());
            ws2.setSweep_width_factor(dfc2.getSweepWidthFactor());
            ws2.setUncorrected_sweep_width(dfc2.getUncorrectedSweepWidth());
            ws2.setWeather_correction_factor(dfc2.getWeatherCorrectionFactor());
            ws2.setWindspeed(dfc2.getWindSpeed());
            ws2.setWorksheet_name(dfc2.getWorksheetName());
            listSendPlanningDfc2.add(ws2);
        }
        for (DriftCalcWorksheet3 dfc3 : listDriftCalc3) {
            PlanningWs3 ws3 = new PlanningWs3();
            ws3.setCoverage_factor(dfc3.getCoverageFactor());
            ws3.setFatigue_correction_factor(dfc3.getFatigueCorrectionFactor());
            ws3.setFatigue_factor(dfc3.getFatigueFactor());
            ws3.setMeteorlogical_visibility(dfc3.getMeteorologicalVisibility());
            ws3.setPractical_track_spacing(dfc3.getPracticalTrackSpacing());
            ws3.setProbability_detection(dfc3.getProbabilityOfDetection());
            ws3.setSearch_area(dfc3.getSearchArea());
            ws3.setSearch_height(dfc3.getSearchHeight());
            ws3.setSearch_hours(dfc3.getSearchhours());
            ws3.setSearch_object(dfc3.getSearchObject());
            ws3.setSearch_repeat(dfc3.getSearchRepeat());
            ws3.setSweep_width_factor(dfc3.getSweepWidthFactor());
            ws3.setTerrain_correction_factor(dfc3.getTerrainCorrectionFactor());
            ws3.setUncorrected_sweep_width(dfc3.getUncorrectedSweepWidth());
            ws3.setVegetation(dfc3.getVegetation());
            ws3.setWorksheet_name(dfc3.getWorksheetName());
            listSendPlanningDfc3.add(ws3);
        }
        for (DriftCalcWorksheet8 dfc8 : listDriftCalc8) {
            PlanningWs8 ws8 = new PlanningWs8();
            ws8.setCoverage_factor(dfc8.getCoverageFactor());
            ws8.setFatigue_correction_factor(dfc8.getFatigueCorrectionFactor());
            ws8.setFatigue_factor(dfc8.getFatigueFactor());
            ws8.setMeteorlogical_visibility(dfc8.getMeteorologicalVisibility());
            ws8.setPractical_track_spacing(dfc8.getPracticalTrackSpacing());
            ws8.setProbability_detection(dfc8.getProbabilityOfDetection());
            ws8.setSearch_area(dfc8.getSearchArea());
            ws8.setSearch_height(dfc8.getSearchHeight());
            ws8.setSearch_hours(dfc8.getSearchhours());
            ws8.setSearch_object(dfc8.getSearchObject());
            ws8.setSearch_repeat(dfc8.getSearchRepeat());
            ws8.setSweep_width_factor(dfc8.getSweepWidthFactor());
            ws8.setUncorrected_sweep_width(dfc8.getUncorrectedSweepWidth());
            ws8.setWorksheet_name(dfc8.getWorksheetName());
            ws8.setWeather_correction_factor(dfc8.getWeatherCorrectionFactor());
            ws8.setSpeed_correction_factor(dfc8.getSpeedCorrectionFactor());
            ws8.setWinds_speed(dfc8.getWindSpeed());
            ws8.setSearch_platform(dfc8.getSearchPlatform());
            listSendPlanningDfc8.add(ws8);

        }
        PlanningMainModelWs planning = new PlanningMainModelWs();
        planning.setWorksheet1(listSendPlanningDfc1);
        planning.setWorksheet2(listSendPlanningDfc2);
        planning.setWorksheet3(listSendPlanningDfc3);
        planning.setWorksheet8(listSendPlanningDfc8);
        ResponseData responsedata = new ResponseData();
        List<PlanningMainModelWs> planningList = new ArrayList<>();
        planningList.add(planning);
        responsedata.setResponseCode(ResponseCode.SUCCESS);
        responsedata.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
        responsedata.setData(planningList);
        return new ResponseEntity<ResponseData>(responsedata, HttpStatus.OK);

    }
}
