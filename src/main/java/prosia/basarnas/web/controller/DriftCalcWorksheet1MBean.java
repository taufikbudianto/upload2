/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.constant.StatusConstant;
import prosia.basarnas.model.DriftCalcWorksheet1;
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IndexLeeway;
import prosia.basarnas.repo.DriftCalcWorksheet1Repo;
import prosia.basarnas.repo.IncidentLeewayRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.service.Tanggal;
import prosia.basarnas.service.map_js.GPoly;
import prosia.basarnas.service.map_js.GTrapeziumSearchArea;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.util.map.Serializable;
import prosia.basarnas.web.util.TaskSearchAreaController;
import prosia.app.web.controller.ReportController;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRParameter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JasperPrint;
import javax.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import prosia.basarnas.web.model.map.MonteCarlo;
import prosia.basarnas.web.util.map.MonteCarloCalculation;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class DriftCalcWorksheet1MBean extends AbstractManagedBean implements InitializingBean {

    private final double valKnot = 0.0194384449;
    private final double DEFAULT_KM = 0.53961;
    private String zonaWaktu;
    private IndexLeeway indexLeeway;
    private String indexLeewayID;
    private DriftCalcWorksheet1 driftCalcWorksheet1;
    private DriftCalcWorksheet2 driftCalcWorksheet2;
    private LazyDataModelJPA<DriftCalcWorksheet1> lazyDataModelJPA;
    private String worksheetID;
    private String worksheetName;
    private String incDescription;
    private double incLatitude;
    private double incLongitude;
    private double incLatDegree;
    private boolean incLatSideIsSouth;
    private double incLatMinute;
    private double incLatSecond;
    private String incTime;
    //private String incTime;
    private TimeZone incTimeZone;
    private double safetyFactor;
    private double driftHours;
    private Date operationTime;
    private TimeZone operationTimeZone;
    private double seaCurrentAngle;
    private double surfaceWindAngle;
    private double seaCurrentSpeed;
    private double seaCurrentSpeed_knot;
    private double windCurrentSpeed;
    private double windCurrentSpeed_knot;
    private double driftErrorPctg = 12.5;
    private double distressCraftError;
    private double searchCraftError;
    private double seaCurrentDistance;
    private double seaCurrentX;
    private double seaCurrentY;
    private double windCurrentAngle;
    private double windCurrentSpeedForCalcDist;
    private double windCurrentSpeedForCalcDist_knot;
    private double windCurrentDistance;
    private double windCurrentX;
    private double windCurrentY;
    private double lwyDivergenceAngle;
    private double lwySpeed;
    private double lwyDistance;
    private double lwyLeftAngle;
    private double lwyLeftX;
    private double lwyLeftY;
    private double lwyRightAngle;
    private double lwyRightX;
    private double lwyRightY;
    private double driftLeftX;
    private double driftLeftY;
    private double driftLeftAngle;
    private double driftLeftDistanceInNm;
    private Double[] driftLeftLatLon;
    private BigDecimal driftLeftLat;
    private BigDecimal driftLeftLon;
    private double driftRightX;
    private double driftRightY;
    private double driftRightAngle;
    private double driftRightDistanceInNm;
    private Double[] driftRightLatLon;
    private BigDecimal driftRightLat;
    private BigDecimal driftRightLon;
    private double driftLeftToRightDistance;
    private double deDriftLeftToRightDistance;
    private double driftError;
    private double totalProbableError;
    private double searchRadius;
    private double searchArea;
    private double deLeftToDriftLeftDistance;
    private double driftLeftToDatumDistance;
    private double driftLeftToRightX;
    private double driftLeftToRightY;
    private double driftLeftToRightAngle;
    private double datumX;
    private double datumY;
    private double datumAngle;
    private double datumDistanceInNm;
    private Double[] datumLatLon;
    private BigDecimal datumLat;
    private BigDecimal datumLon;
    private Incident incident;
    private SimpleDateTimeZoneFormat sdfDtz;
    private List<SelectItem> listItemWorksheet1;
    private List<String> listWorksheet1;
    private List<DriftCalcWorksheet1> listWorksheet1Model;
    private String createdBy;
    private Date lastModified;
    private List<Incident> listIncidentID;
    private String searchTarget1;
    private String searchTarget2;
    private String searchTarget3;
    private String cboLatitude;
    private String cboLongitude;
    private String cboSatuan;
    private String currentIncidentID;
    private DriftCalcWorksheet1Result worksheetResult;
    private double unit = 0;
    private boolean isSearchAreaDatum = false;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean isNew = false;
    private boolean isCalculated = false;
    private boolean isViewMap;
    private boolean isviewMapPlanDriftCalcWorksheet1;
    private String LastWorksheetID;
    private Coordinate coordinateLat;
    private Coordinate coordinateLong;
    private String INCIDENTID;
    private TaskSearchAreaController taskSearchAreaController;
    //montecarlo var
    private Integer jmlData = 0;
    private Double varianceSea = 0.0;
    private Double varianceWind = 0.0;
    List<MonteCarlo> listMonteCarlo = new ArrayList<>();

    @Autowired
    private ReportController reportController;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DriftCalcWorksheet1Repo driftCalcWorksheet1Repo;

    @Autowired
    private IncidentRepo incidentRepo;
    /*
    @Autowired
    private IncidentMBean incidentMBean;
     */

    @Autowired
    private IncidentPlanningMBean incidentPlanningMBean;

    @Autowired
    private IncidentDriftCalculation incidentDriftCalculation;

    @Autowired
    private IncidentLeewayRepo incidentLeewayRepo;

    @Autowired
    private DriftCalcWorksheet2MBean driftCalcWorksheet2MBean;

    @Autowired
    private DriftCalcWorksheet3MBean driftCalcWorksheet3MBean;

    @Autowired
    private DriftCalcWorksheet8MBean driftCalcWorksheet8MBean;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    MapMBean mapMBean;

    public DriftCalcWorksheet1MBean() {
        initCoordinate();
        isviewMapPlanDriftCalcWorksheet1 = false;
    }

    public void setValueSetAreaDatum() {
        isSearchAreaDatum = true;

        driftCalcWorksheet2MBean.setSearchArea(searchArea);
        driftCalcWorksheet3MBean.setSearchArea(searchArea);
        driftCalcWorksheet8MBean.setSearchArea(searchArea);

        if (datumLat != null) {
            driftCalcWorksheet2MBean.setCboLatitude(datumLat.toString());
            driftCalcWorksheet8MBean.setCboLatitude(datumLat.toString());
        }
        if (datumLon != null) {
            driftCalcWorksheet2MBean.setCboLongitude(datumLon.toString());
            driftCalcWorksheet8MBean.setCboLongitude(datumLon.toString());
        }
        driftCalcWorksheet2MBean.resetGridSruSearchArea();
        driftCalcWorksheet8MBean.resetGridSruSearchArea();
        driftCalcWorksheet2MBean.initCoordinate();
        driftCalcWorksheet2MBean.viewCoordinate();
        driftCalcWorksheet8MBean.initCoordinate();
        driftCalcWorksheet8MBean.viewCoordinate();

        RequestContext.getCurrentInstance().execute("PF('widgetOpenSearchArea').hide()");
    }

    public void pilihLeeway(String indexLeewayID) {
        if (indexLeewayID != null) {
            indexLeeway = incidentLeewayRepo.findOne(indexLeewayID);
            if (indexLeeway != null) {
                searchTarget1 = indexLeeway.getCategory();
                searchTarget2 = indexLeeway.getSubCategory();
                searchTarget3 = indexLeeway.getLeewayDescription();
            }
        }
        RequestContext.getCurrentInstance().execute("PF('widgetLeeway').hide()");
    }

    public double calculateDriftHours() {
        if (operationTime != null) {
            Date incidentTime = null;
            Calendar calOperationTime = Calendar.getInstance();
            calOperationTime.setTime(operationTime);
            String strIncTime;

//            if (calOperationTime != null) {
            operationTime = calOperationTime.getTime();

//            } else {
//                operationTime = null;
//            }
            strIncTime = deFormatZonaWaktu(incTime);
            if (strIncTime != null) {
                try {
                    Calendar calIncTime = sdfDtz.parseCalendar(strIncTime);
                    incidentTime = calIncTime.getTime();
                } catch (Exception ex) {
                    incTime = null;
                }
            }
            Double result = Tanggal.hoursDiff(incidentTime, operationTime);
            return DecimalUtil.rounding(result);
        } else {
            return 0.0;
        }

    }

    public void generateSeaCurrentSpeed_knot() {
        if (indexLeeway != null) {
            seaCurrentSpeed_knot = valKnot * seaCurrentSpeed;
            seaCurrentSpeed = seaCurrentSpeed_knot / valKnot;
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Warning", "Pilih Index Leeway terlebih dahulu");
        }
    }

    public void generateWindCurrentSpeed_knot() {
        if (indexLeeway != null) {
            windCurrentSpeed_knot = valKnot * windCurrentSpeed;
            windCurrentSpeed = windCurrentSpeed_knot / valKnot;
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Pilih Index Leeway terlebih dahulu");
        }
    }

    private String formatZonaWaktu(String nilai, String gmt) {
        String result = null;
//        System.out.println("--nilai--" + nilai);
        if (gmt.contains("GMT+07:00")) {
            if (nilai.contains("ICT")) {
                result = nilai.replace("ICT", "G");
            }
        } else if (gmt.contains("GMT+08:00")) {
            if (nilai.contains("ICT")) {
                result = nilai.replace("ICT", "H");
            }
        } else if (gmt.contains("GMT+09:00")) {
            if (nilai.contains("ICT")) {
                result = nilai.replace("ICT", "I");
            }
        } else if (gmt.contains("GMT+00:00")) {
            if (nilai.contains("ICT")) {
                result = nilai.replace("ICT", "Z");
            }
        }
        return result;
    }

    private String deFormatZonaWaktu(String nilai) {
        String result = null;
//        System.out.println("--de nilai--" + nilai);
        if (nilai.contains("G")) {
            result = nilai.replace("G", "+0700");
        } else if (nilai.contains("H")) {
            result = nilai.replace("H", "+0800");
        } else if (nilai.contains("I")) {
            result = nilai.replace("I", "+0900");
        } else if (nilai.contains("Z")) {
            result = nilai.replace("Z", "+0000");
        }
        return result;
    }

    public void generateDriftHours() {
        if (indexLeeway != null) {
            driftHours = calculateDriftHours();
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Pilih Index Leeway terlebih dahulu");
        }

    }

    public void calculateWS1() {

        if (objectValidasi() == false) {
            return;
        }

        isCalculated = true;
        //driftCalcWorksheet1 = new DriftCalcWorksheet1();
        unit = Double.parseDouble(cboSatuan);

        if (unit == 0) {
            seaCurrentDistance = seaCurrentSpeed_knot * driftHours;
        } else {
            seaCurrentDistance = seaCurrentSpeed_knot * driftHours / DEFAULT_KM;
        }
        seaCurrentDistance = DecimalUtil.rounding(seaCurrentDistance);

        seaCurrentX = MapCalculation.calculateX(seaCurrentDistance,
                seaCurrentAngle);
        seaCurrentY = MapCalculation.calculateY(seaCurrentDistance,
                seaCurrentAngle);

        windCurrentAngle = surfaceWindAngle;
        if (incLatSideIsSouth) {
            if (incLatDegree > 10) {
                windCurrentAngle = surfaceWindAngle - 30;
            } else if (incLatDegree == 10 && (incLatMinute > 0 || incLatSecond > 0)) {
                windCurrentAngle = surfaceWindAngle - 30;
            }
        }
        windCurrentAngle = DecimalUtil.rounding(windCurrentAngle);

        windCurrentSpeedForCalcDist_knot = windCurrentSpeed_knot / 28;
        windCurrentSpeedForCalcDist_knot = DecimalUtil.rounding(windCurrentSpeedForCalcDist_knot);

        windCurrentSpeedForCalcDist = windCurrentSpeedForCalcDist_knot / valKnot;

        if (unit == 0) {
            windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours;
        } else {
            windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours / DEFAULT_KM;
        }

        windCurrentDistance = DecimalUtil.rounding(windCurrentDistance);

        windCurrentX = MapCalculation.calculateX(windCurrentDistance,
                windCurrentAngle);

        windCurrentY = MapCalculation.calculateY(windCurrentDistance,
                windCurrentAngle);

        //index leeway
        //indexLeeway.setAngle(31.0);
        lwyDivergenceAngle = indexLeeway.getAngle();
        lwyDivergenceAngle = DecimalUtil.rounding(lwyDivergenceAngle);

        //indexLeeway.setMultiplier(0.013);
        lwySpeed = indexLeeway.getMultiplier()
                * windCurrentSpeed_knot + indexLeeway.getModifier();
        lwySpeed = DecimalUtil.rounding(lwySpeed);
        //end
        if (unit == 0) {
            lwyDistance = lwySpeed * driftHours;
        } else {
            lwyDistance = lwySpeed * driftHours / DEFAULT_KM;
        }

        lwyDistance = DecimalUtil.rounding(lwyDistance);

        lwyLeftAngle = MapCalculation.subtractDegree(windCurrentAngle,
                lwyDivergenceAngle);
        lwyLeftAngle = DecimalUtil.rounding(lwyLeftAngle);

        lwyLeftX = MapCalculation.calculateX(lwyDistance, lwyLeftAngle);
        lwyLeftY = MapCalculation.calculateY(lwyDistance, lwyLeftAngle);

        lwyRightAngle = MapCalculation.addDegree(windCurrentAngle,
                lwyDivergenceAngle);
        lwyRightAngle = DecimalUtil.rounding(lwyRightAngle);
        lwyRightX = MapCalculation.calculateX(lwyDistance, lwyRightAngle);
        lwyRightY = MapCalculation.calculateY(lwyDistance, lwyRightAngle);
        driftLeftX = windCurrentX + seaCurrentX + lwyLeftX;
        driftLeftY = windCurrentY + seaCurrentY + lwyLeftY;
        driftLeftAngle = MapCalculation.calculateAngle(driftLeftX,
                driftLeftY);
        if (unit == 0) {
            driftLeftDistanceInNm = MapCalculation.calculateDistance(
                    driftLeftX,
                    driftLeftY);
        } else {
            driftLeftDistanceInNm = MapCalculation.calculateDistance(
                    driftLeftX,
                    driftLeftY) / DEFAULT_KM;
        }

        driftLeftDistanceInNm = DecimalUtil.rounding(driftLeftDistanceInNm);

        driftLeftLatLon = MapCalculation.calculatePointInNm(incLatitude,
                incLongitude, driftLeftDistanceInNm, driftLeftAngle);

        driftLeftLat = new BigDecimal(driftLeftLatLon[0]).setScale(6,
                BigDecimal.ROUND_HALF_UP);

        driftLeftLon = new BigDecimal(driftLeftLatLon[1]).setScale(6,
                BigDecimal.ROUND_HALF_UP);

        driftRightX = windCurrentX + seaCurrentX + lwyRightX;
        driftRightY = windCurrentY + seaCurrentY + lwyRightY;
        driftRightAngle = MapCalculation.calculateAngle(driftRightX,
                driftRightY);
        if (unit == 0) {
            driftRightDistanceInNm = MapCalculation.calculateDistance(
                    driftRightX,
                    driftRightY);
        } else {
            driftRightDistanceInNm = MapCalculation.calculateDistance(
                    driftRightX,
                    driftRightY) / DEFAULT_KM;
        }

        driftRightDistanceInNm = DecimalUtil.rounding(driftRightDistanceInNm);

        driftRightLatLon = MapCalculation.calculatePointInNm(
                incLatitude,
                incLongitude, driftRightDistanceInNm, driftRightAngle);

        driftRightLat = new BigDecimal(driftRightLatLon[0]).setScale(
                6, BigDecimal.ROUND_HALF_UP);

        driftRightLon = new BigDecimal(driftRightLatLon[1]).setScale(
                6, BigDecimal.ROUND_HALF_UP);
        if (unit == 0) {
            driftLeftToRightDistance = Math.sqrt(Math.pow(
                    lwyDistance,
                    2)
                    + Math.pow(lwyDistance, 2)
                    - 2 * lwyDistance * lwyDistance * Math.cos(
                            Math.toRadians(lwyDivergenceAngle * 2)));
        } else {
            driftLeftToRightDistance = Math.sqrt(Math.pow(
                    lwyDistance,
                    2)
                    + Math.pow(lwyDistance, 2)
                    - 2 * lwyDistance * lwyDistance * Math.cos(
                            Math.toRadians(lwyDivergenceAngle * 2))) / DEFAULT_KM;
        }

        driftLeftToRightDistance = DecimalUtil.rounding(driftLeftToRightDistance);

        deDriftLeftToRightDistance = (driftErrorPctg * driftLeftDistanceInNm / 100)
                + (driftErrorPctg * driftRightDistanceInNm / 100) + driftLeftToRightDistance;
        if (unit == 0) {
            driftError = deDriftLeftToRightDistance / 2;
        } else {
            driftError = (deDriftLeftToRightDistance / 2) / DEFAULT_KM;
        }

        driftError = DecimalUtil.rounding(driftError);

        if (unit == 0) {
            totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                    + Math.pow(distressCraftError, 2)
                    + Math.pow(searchCraftError, 2));
        } else {
            totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                    + Math.pow(distressCraftError, 2)
                    + Math.pow(searchCraftError, 2)) / DEFAULT_KM;
        }

        totalProbableError = DecimalUtil.rounding(totalProbableError);

        if (unit == 0) {
            searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                    setScale(0, BigDecimal.ROUND_UP).doubleValue();
        } else {
            searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                    setScale(0, BigDecimal.ROUND_UP).doubleValue() / DEFAULT_KM;
        }

        searchRadius = DecimalUtil.rounding(searchRadius);

        if (unit == 0) {
            searchArea = Math.pow((2 * searchRadius), 2);
        } else {
            searchArea = Math.pow((2 * searchRadius), 2) / DEFAULT_KM;
        }

        searchArea = DecimalUtil.rounding(searchArea);

        deLeftToDriftLeftDistance = driftErrorPctg * driftLeftDistanceInNm / 100;
        driftLeftToDatumDistance = deDriftLeftToRightDistance / 2 - deLeftToDriftLeftDistance;
        driftLeftToRightX = driftRightX - driftLeftX;
        driftLeftToRightY = driftRightY - driftLeftY;
        driftLeftToRightAngle = MapCalculation.calculateAngle(
                driftLeftToRightX, driftLeftToRightY);

        datumX = (driftLeftToRightX / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftX;
        datumY = (driftLeftToRightY / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftY;

        datumAngle = MapCalculation.calculateAngle(datumX, datumY);
        datumDistanceInNm = MapCalculation.calculateDistance(datumX,
                datumY);

        datumLatLon = MapCalculation.calculatePointInNm(
                incLatitude,
                incLongitude, datumDistanceInNm, datumAngle);

        datumLat = new BigDecimal(datumLatLon[0]).setScale(
                6, BigDecimal.ROUND_HALF_UP);

        datumLon = new BigDecimal(datumLatLon[1]).setScale(
                6, BigDecimal.ROUND_HALF_UP);

        //calculate montecarlo
        if (!incidentPlanningMBean.getIsDriftCalculation()) {
            listMonteCarlo = MonteCarloCalculation.calculatMc(cboSatuan, seaCurrentSpeed_knot, driftHours,
                    seaCurrentAngle, surfaceWindAngle,
                    incLatSideIsSouth, incLatDegree, incLatMinute, incLatSecond,
                    windCurrentSpeed_knot, indexLeeway,
                    incLatitude, incLongitude, driftErrorPctg,
                    distressCraftError, searchCraftError, safetyFactor,
                    jmlData, varianceSea, varianceWind);
            RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:idDtMonteCarlo");
        }
        //validasi tambahan untuk monte carlo
        if (isViewMap == false && incidentPlanningMBean.getIsDriftCalculation()) {
            RequestContext.getCurrentInstance().execute("PF('widgetOpenSearchArea').show()");
        }
    }

    public void openSaveDialog() {
        //validasi
        if (isCalculated == false) {
            calculateWS1();
        } else if (objectValidasi() == false) {
            return;
        }

        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs1').show()");
    }

    public void simpanWS1() throws CloneNotSupportedException {
        DriftCalcWorksheet1 cloneDriftCalcWorksheet1 = (DriftCalcWorksheet1) driftCalcWorksheet1.clone();

        incident = incidentRepo.findAllByIncidentid(currentIncidentID);

        driftCalcWorksheet1.setIncident(incident);
        driftCalcWorksheet1.setIndexLeeway(indexLeeway);
        driftCalcWorksheet1.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        driftCalcWorksheet1.setDateCreated(new Date());
        driftCalcWorksheet1.setDatumLatitude(datumLat.toString());
        driftCalcWorksheet1.setDatumLongitude(datumLon.toString());
        driftCalcWorksheet1.setDistressCraftError(distressCraftError);
        driftCalcWorksheet1.setDriftError(driftError);
        driftCalcWorksheet1.setDriftErrorPercentage(driftErrorPctg);
        driftCalcWorksheet1.setDriftHours(driftHours);
        driftCalcWorksheet1.setDriftLeftDistance(driftLeftDistanceInNm);
        driftCalcWorksheet1.setDriftLeftLatitude(driftLeftLat.toString());
        driftCalcWorksheet1.setDriftLeftLongitude(driftLeftLon.toString());
        driftCalcWorksheet1.setDriftLeftToRightAngle(driftLeftToRightAngle);
        driftCalcWorksheet1.setDriftLeftToRightDistance(driftLeftToRightDistance);
        driftCalcWorksheet1.setDriftRightDistance(driftRightDistanceInNm);
        driftCalcWorksheet1.setDriftRightLatitude(driftRightLat.toString());
        driftCalcWorksheet1.setDriftRightLongitude(driftRightLon.toString());
        driftCalcWorksheet1.setIncLatDegree(incLatDegree);
        driftCalcWorksheet1.setIncLatMinute(incLatMinute);
        driftCalcWorksheet1.setIncLatSecond(incLatSecond);
        driftCalcWorksheet1.setIncLatSideIsSouth(incLatSideIsSouth);
        driftCalcWorksheet1.setIncToDatumAngle(datumAngle);
        driftCalcWorksheet1.setIncToDatumDistance(driftLeftToDatumDistance);
        driftCalcWorksheet1.setIncidentDescription(incDescription);
        driftCalcWorksheet1.setIncidentLatitude(String.valueOf(incLatitude));
        driftCalcWorksheet1.setIncidentLongitude(String.valueOf(incLongitude));
        driftCalcWorksheet1.setIncidentTime(incident.getStartdate());
//            driftCalcWorksheet1.setIncidentTimeZone(incident.getStartdatetimezone());
        driftCalcWorksheet1.setLeewayDistance(lwyDistance);
        driftCalcWorksheet1.setLeewayDivergenceAngle(lwyDivergenceAngle);
        driftCalcWorksheet1.setLeewayLeftAngle(lwyLeftAngle);
        driftCalcWorksheet1.setLeewayRightAngle(lwyRightAngle);
        driftCalcWorksheet1.setLeewaySpeed(lwySpeed);
        driftCalcWorksheet1.setModifiedBy(null);
        driftCalcWorksheet1.setOperationTime(operationTime);
//            driftCalcWorksheet1.setOperationTimeZone(incident.getStartdatetimezone());
        driftCalcWorksheet1.setSafetyFactor(safetyFactor);
        driftCalcWorksheet1.setSeaCurrentAngle(seaCurrentAngle);
        driftCalcWorksheet1.setSeaCurrentDistance(seaCurrentDistance);
        driftCalcWorksheet1.setSeaCurrentSpeed(seaCurrentSpeed);
        driftCalcWorksheet1.setSearchArea(searchArea);
        driftCalcWorksheet1.setSearchCraftError(searchCraftError);
        driftCalcWorksheet1.setSearchRadius(searchRadius);
        driftCalcWorksheet1.setSurfaceWindAngle(surfaceWindAngle);
        driftCalcWorksheet1.setTotalProbableError(totalProbableError);
        unit = Integer.parseInt(cboSatuan);
        driftCalcWorksheet1.setUnit(unit);
        driftCalcWorksheet1.setUserSiteID(ProsiaConstant.SITES);
        driftCalcWorksheet1.setWindCurrentAngle(windCurrentAngle);
        driftCalcWorksheet1.setWindCurrentDistance(windCurrentDistance);
        driftCalcWorksheet1.setWindCurrentSpeed(windCurrentSpeed);
        driftCalcWorksheet1.setWindCurrentSpeedForCalcDist(windCurrentSpeedForCalcDist);
        driftCalcWorksheet1.setWorksheetID(formatclassId());
        driftCalcWorksheet1.setWorksheetName(worksheetName);

        //driftCalcWorksheet1.setIncidentTimeZone(incTimeZone);
        if (isNew == true) {
            driftCalcWorksheet1.setLastModified(null);
            driftCalcWorksheet1.setDeleted(false);
            driftCalcWorksheet1Repo.save(driftCalcWorksheet1);

            addPopUpMessage("Sukses", "Berhasil di simpan");
        } else {
            cloneDriftCalcWorksheet1.setWorksheetID(LastWorksheetID);
            cloneDriftCalcWorksheet1.setDeleted(true);
            cloneDriftCalcWorksheet1.setLastModified(new Date());
            cloneDriftCalcWorksheet1.setModifiedBy(menuNavMBean.getUserSession().getUsername());
            driftCalcWorksheet1Repo.save(cloneDriftCalcWorksheet1);

            driftCalcWorksheet1.setDeleted(false);
            driftCalcWorksheet1.setLastModified(null);
            driftCalcWorksheet1.setModifiedBy(null);
            driftCalcWorksheet1Repo.save(driftCalcWorksheet1);

            addPopUpMessage("Sukses", "Berhasil di update");
        }

        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs1').hide()");
        RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws1");
    }

    public void deleteWS1() {
        if (incident != null && indexLeeway != null) {
            driftCalcWorksheet1.setWorksheetID(LastWorksheetID);
            driftCalcWorksheet1.setIncident(incident);
            driftCalcWorksheet1.setIndexLeeway(indexLeeway);
            driftCalcWorksheet1.setDeleted(true);
            driftCalcWorksheet1Repo.save(driftCalcWorksheet1);
            newWS1();
            addPopUpMessage("Sukses", "Worksheet berhasil di hapus !");
        }
    }

    public void pilihWS1() {
        System.out.println("sanalah");
        driftCalcWorksheet1 = driftCalcWorksheet1Repo.findByWorksheetID(worksheetID);
        if (driftCalcWorksheet1 != null) {
            isNew = false;
            //search indexLeeway
            indexLeeway = incidentLeewayRepo.findOne(driftCalcWorksheet1.getIndexLeeway().getIndexLeewayID());
            searchTarget1 = indexLeeway.getCategory();
            searchTarget2 = indexLeeway.getSubCategory();
            searchTarget3 = indexLeeway.getLeewayDescription();
            //search Incident
            //incident = incidentRepo.findAllByIncidentid(driftCalcWorksheet1.getIncident().getIncidentid());
            unit = driftCalcWorksheet1.getUnit();
            incDescription = driftCalcWorksheet1.getIncidentDescription();
            incLatitude = Double.parseDouble(driftCalcWorksheet1.getIncidentLatitude());
            incLongitude = Double.parseDouble(driftCalcWorksheet1.getIncidentLongitude());
            safetyFactor = driftCalcWorksheet1.getSafetyFactor();
            if (driftCalcWorksheet1.getIncidentTime() != null) {
                incTime = driftCalcWorksheet1.getIncidentTime().toString();
            }
            if (driftCalcWorksheet1.getOperationTime() != null) {
                operationTime = driftCalcWorksheet1.getOperationTime();
            }
            driftHours = driftCalcWorksheet1.getDriftHours();
            seaCurrentAngle = driftCalcWorksheet1.getSeaCurrentAngle();
            seaCurrentSpeed = driftCalcWorksheet1.getSeaCurrentSpeed();
            seaCurrentSpeed_knot = valKnot * seaCurrentSpeed;
            if (unit == 0) {
                seaCurrentDistance = driftCalcWorksheet1.getSeaCurrentDistance();
            } else {
                seaCurrentDistance = driftCalcWorksheet1.getSeaCurrentDistance() / DEFAULT_KM;
            }
            surfaceWindAngle = driftCalcWorksheet1.getSurfaceWindAngle();
            windCurrentSpeed = driftCalcWorksheet1.getWindCurrentSpeed();
            windCurrentSpeed_knot = valKnot * windCurrentSpeed;
            windCurrentAngle = driftCalcWorksheet1.getWindCurrentAngle();
            /*
            windCurrentSpeedForCalcDist = driftCalcWorksheet1.getWindCurrentSpeedForCalcDist();
            windCurrentSpeedForCalcDist_knot = 0;
             */
            windCurrentSpeedForCalcDist_knot = driftCalcWorksheet1.getWindCurrentSpeedForCalcDist();
            windCurrentSpeedForCalcDist = windCurrentSpeedForCalcDist_knot / valKnot;

            if (unit == 0) {
                windCurrentDistance = driftCalcWorksheet1.getWindCurrentDistance();
            } else {
                windCurrentDistance = driftCalcWorksheet1.getWindCurrentDistance() / DEFAULT_KM;
            }
            lwyDivergenceAngle = driftCalcWorksheet1.getLeewayDivergenceAngle();
            lwySpeed = driftCalcWorksheet1.getLeewaySpeed();
            lwyLeftAngle = driftCalcWorksheet1.getLeewayLeftAngle();
            if (unit == 0) {
                lwyDistance = driftCalcWorksheet1.getLeewayDistance();
            } else {
                lwyDistance = driftCalcWorksheet1.getLeewayDistance() / DEFAULT_KM;
            }
            lwyRightAngle = driftCalcWorksheet1.getLeewayRightAngle();
            if (unit == 0) {
                driftLeftDistanceInNm = driftCalcWorksheet1.getDriftLeftDistance();
            } else {
                driftLeftDistanceInNm = driftCalcWorksheet1.getDriftLeftDistance() / DEFAULT_KM;
            }
            if (unit == 0) {
                driftLeftToRightDistance = driftCalcWorksheet1.getDriftLeftToRightDistance();
            } else {
                driftLeftToRightDistance = driftCalcWorksheet1.getDriftLeftToRightDistance() / DEFAULT_KM;
            }

            distressCraftError = driftCalcWorksheet1.getDistressCraftError();
            if (unit == 0) {
                driftRightDistanceInNm = driftCalcWorksheet1.getDriftRightDistance();
            } else {
                driftRightDistanceInNm = driftCalcWorksheet1.getDriftRightDistance() / DEFAULT_KM;
            }
            if (unit == 0) {
                driftError = driftCalcWorksheet1.getDriftError();
            } else {
                driftError = driftCalcWorksheet1.getDriftError() / DEFAULT_KM;
            }
            searchCraftError = driftCalcWorksheet1.getSearchCraftError();
            driftErrorPctg = driftCalcWorksheet1.getDriftErrorPercentage();
            if (unit == 0) {
                totalProbableError = driftCalcWorksheet1.getTotalProbableError();
            } else {
                totalProbableError = driftCalcWorksheet1.getTotalProbableError() / DEFAULT_KM;
            }
            if (unit == 0) {
                searchRadius = driftCalcWorksheet1.getSearchRadius();
            } else {
                searchRadius = driftCalcWorksheet1.getSearchRadius() / DEFAULT_KM;
            }
            if (unit == 0) {
                searchArea = driftCalcWorksheet1.getSearchArea();
            } else {
                searchArea = driftCalcWorksheet1.getSearchArea() / DEFAULT_KM;
            }

        }
        if (!isviewMapPlanDriftCalcWorksheet1) {
            clearOpenDialog();
            RequestContext.getCurrentInstance().execute("PF('widgetOpenWs1').hide()");
            RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws1-panel1");
        }
    }

    public void viewPlanDriftCalcWorksheet1(String ID) {
        incidentPlanningMBean.setIsDriftCalculation(true);
        driftCalcWorksheet1 = driftCalcWorksheet1Repo.findByWorksheetID(ID);

        if (driftCalcWorksheet1 != null) {
            isNew = false;
            //search indexLeeway
            indexLeeway = incidentLeewayRepo.findOne(driftCalcWorksheet1.getIndexLeeway().getIndexLeewayID());
            searchTarget1 = indexLeeway.getCategory();
            searchTarget2 = indexLeeway.getSubCategory();
            searchTarget3 = indexLeeway.getLeewayDescription();
            //search Incident
            //incident = incidentRepo.findAllByIncidentid(driftCalcWorksheet1.getIncident().getIncidentid());
            unit = driftCalcWorksheet1.getUnit();
            incDescription = driftCalcWorksheet1.getIncidentDescription();
            incLatitude = Double.parseDouble(driftCalcWorksheet1.getIncidentLatitude());
            incLongitude = Double.parseDouble(driftCalcWorksheet1.getIncidentLongitude());
            safetyFactor = driftCalcWorksheet1.getSafetyFactor();
            if (driftCalcWorksheet1.getIncidentTime() != null) {
                incTime = driftCalcWorksheet1.getIncidentTime().toString();
            }
            if (driftCalcWorksheet1.getOperationTime() != null) {
                operationTime = driftCalcWorksheet1.getOperationTime();
            }
            driftHours = driftCalcWorksheet1.getDriftHours();
            seaCurrentAngle = driftCalcWorksheet1.getSeaCurrentAngle();
            seaCurrentSpeed = driftCalcWorksheet1.getSeaCurrentSpeed();
            seaCurrentSpeed_knot = valKnot * seaCurrentSpeed;
            if (unit == 0) {
                seaCurrentDistance = driftCalcWorksheet1.getSeaCurrentDistance();
            } else {
                seaCurrentDistance = driftCalcWorksheet1.getSeaCurrentDistance() / DEFAULT_KM;
            }
            surfaceWindAngle = driftCalcWorksheet1.getSurfaceWindAngle();
            windCurrentSpeed = driftCalcWorksheet1.getWindCurrentSpeed();
            windCurrentSpeed_knot = valKnot * windCurrentSpeed;
            windCurrentAngle = driftCalcWorksheet1.getWindCurrentAngle();
            /*
            windCurrentSpeedForCalcDist = driftCalcWorksheet1.getWindCurrentSpeedForCalcDist();
            windCurrentSpeedForCalcDist_knot = 0;
             */
            windCurrentSpeedForCalcDist_knot = driftCalcWorksheet1.getWindCurrentSpeedForCalcDist();
            windCurrentSpeedForCalcDist = windCurrentSpeedForCalcDist_knot / valKnot;

            if (unit == 0) {
                windCurrentDistance = driftCalcWorksheet1.getWindCurrentDistance();
            } else {
                windCurrentDistance = driftCalcWorksheet1.getWindCurrentDistance() / DEFAULT_KM;
            }
            lwyDivergenceAngle = driftCalcWorksheet1.getLeewayDivergenceAngle();
            lwySpeed = driftCalcWorksheet1.getLeewaySpeed();
            lwyLeftAngle = driftCalcWorksheet1.getLeewayLeftAngle();
            if (unit == 0) {
                lwyDistance = driftCalcWorksheet1.getLeewayDistance();
            } else {
                lwyDistance = driftCalcWorksheet1.getLeewayDistance() / DEFAULT_KM;
            }
            lwyRightAngle = driftCalcWorksheet1.getLeewayRightAngle();
            if (unit == 0) {
                driftLeftDistanceInNm = driftCalcWorksheet1.getDriftLeftDistance();
            } else {
                driftLeftDistanceInNm = driftCalcWorksheet1.getDriftLeftDistance() / DEFAULT_KM;
            }
            if (unit == 0) {
                driftLeftToRightDistance = driftCalcWorksheet1.getDriftLeftToRightDistance();
            } else {
                driftLeftToRightDistance = driftCalcWorksheet1.getDriftLeftToRightDistance() / DEFAULT_KM;
            }

            distressCraftError = driftCalcWorksheet1.getDistressCraftError();
            if (unit == 0) {
                driftRightDistanceInNm = driftCalcWorksheet1.getDriftRightDistance();
            } else {
                driftRightDistanceInNm = driftCalcWorksheet1.getDriftRightDistance() / DEFAULT_KM;
            }
            if (unit == 0) {
                driftError = driftCalcWorksheet1.getDriftError();
            } else {
                driftError = driftCalcWorksheet1.getDriftError() / DEFAULT_KM;
            }
            searchCraftError = driftCalcWorksheet1.getSearchCraftError();
            driftErrorPctg = driftCalcWorksheet1.getDriftErrorPercentage();
            if (unit == 0) {
                totalProbableError = driftCalcWorksheet1.getTotalProbableError();
            } else {
                totalProbableError = driftCalcWorksheet1.getTotalProbableError() / DEFAULT_KM;
            }
            if (unit == 0) {
                searchRadius = driftCalcWorksheet1.getSearchRadius();
            } else {
                searchRadius = driftCalcWorksheet1.getSearchRadius() / DEFAULT_KM;
            }
            if (unit == 0) {
                searchArea = driftCalcWorksheet1.getSearchArea();
            } else {
                searchArea = driftCalcWorksheet1.getSearchArea() / DEFAULT_KM;
            }

        }
        clearOpenDialog();
        RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws1-panel1");
    }

    private void clearOpenDialog() {
        worksheetID = null;
        createdBy = null;
        lastModified = null;
    }

    public void printWS1() {

    }

    public void viewMap() {
        if (objectValidasi() == true) {
            isViewMap = true;
            calculateWS1();
            setWorksheetResult();
            GTrapeziumSearchArea.removeCurrentTrapeziumArea();
            worksheetResult.initOutsideTaskSearchAreaController();
            worksheetResult.setDrawLeewayLine(true);
            //validasi untuk montecarlo
            if (incidentPlanningMBean.getIsDriftCalculation()) {
                worksheetResult.setShape(null);
            } else {
                worksheetResult.setShape(StatusConstant.SEARCH_AREA_MONTECARLO);
            }

            /*cara jelek mengirim value ke ws 2 untuk task search area */
            taskSearchAreaController = new TaskSearchAreaController(worksheetResult.getRadius(), worksheetResult.getLng1(), worksheetResult.getLng2(), worksheetResult.getStartLat());
            driftCalcWorksheet2MBean.setRadiusTaskSearchArea(worksheetResult.getRadius());
            driftCalcWorksheet2MBean.setLng1TaskSearchArea(worksheetResult.getLng1());
            driftCalcWorksheet2MBean.setLng2TaskSearchArea(worksheetResult.getLng2());
            driftCalcWorksheet2MBean.setStartLatTaskSearchArea(worksheetResult.getStartLat());
            driftCalcWorksheet2MBean.setWorksheetResult(worksheetResult);

            driftCalcWorksheet8MBean.setRadiusTaskSearchArea(worksheetResult.getRadius());
            driftCalcWorksheet8MBean.setLng1TaskSearchArea(worksheetResult.getLng1());
            driftCalcWorksheet8MBean.setLng2TaskSearchArea(worksheetResult.getLng2());
            driftCalcWorksheet8MBean.setStartLatTaskSearchArea(worksheetResult.getStartLat());
            driftCalcWorksheet8MBean.setWorksheetResult(worksheetResult);

            GenerateValueRequestBean generateValueRequestBean
                    = Serializable.createGenerateValueRequest(worksheetResult, null);
            generateValueRequestBean.setState(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA);
            mapMBean.setIncident(incident);
            mapMBean.setGenerateValueRequestBean(generateValueRequestBean);
            mapMBean.setListTaskSearchArea(new ArrayList<>());
            RequestContext context = RequestContext.getCurrentInstance();
            //validasi untuk montecarlo
            if (incidentPlanningMBean.getIsDriftCalculation()) {
                System.out.println("masuk");
                context.execute("window.open('/map/map/list.xhtml?search=DRIFTCALCULATION"
                        + "&incident="
                        + incident.getIncidentid() + "', "
                        + "'_blank')");
            } else {
                System.out.println("sini");
                listMonteCarlo = MonteCarloCalculation.calculatMc(cboSatuan, seaCurrentSpeed_knot, driftHours,
                        seaCurrentAngle, surfaceWindAngle,
                        incLatSideIsSouth, incLatDegree, incLatMinute, incLatSecond,
                        windCurrentSpeed_knot, indexLeeway,
                        incLatitude, incLongitude, driftErrorPctg,
                        distressCraftError, searchCraftError, safetyFactor,
                        jmlData, varianceSea, varianceWind);
                mapMBean.setListMonteCarlo(listMonteCarlo);
                context.execute("window.open('/map/map/list.xhtml?search=MONTECARLO"
                        + "&incident="
                        + incident.getIncidentid() + "', "
                        + "'_blank')");
            }
            isViewMap = false;
        }
    }

    public void viewMapPlanDriftCalcWorksheet1(String ID, boolean isviewMapPlanDriftCalcWorksheet1) {
        //incidentPlanningMBean.setIsDriftCalculation(true);
        try{
        worksheetID = ID;
        cboSatuan = "0";
        pilihWS1();
        viewMap();
        }catch(Exception e){
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Gagal", "View Peta Drift Calculation Bermasalah");
        }
    }

    private void setWorksheetResult() {
        worksheetResult = new DriftCalcWorksheet1Result();
        worksheetResult.setParrentID(GPoly.generateParrentID());
        worksheetResult.setLkpLatLng(new GLatLng(incLatitude, incLongitude));
        worksheetResult.setDriftLeftLatLng(new GLatLng(Double.parseDouble(driftLeftLat.toString()), Double.parseDouble(driftLeftLon.toString())));
        worksheetResult.setDriftRightLatLng(new GLatLng(Double.parseDouble(driftRightLat.toString()), Double.parseDouble(driftRightLon.toString())));
        worksheetResult.setLkpToDatumAngle(DecimalUtil.rounding(datumAngle));
        worksheetResult.setLkpToDatumDistance(DecimalUtil.rounding(datumDistanceInNm));
        worksheetResult.setDatumLatLng(new GLatLng(Double.parseDouble(datumLat.toString()), Double.parseDouble(datumLon.toString())));
        worksheetResult.setRadius(DecimalUtil.rounding(searchRadius));
        worksheetResult.setTilt(DecimalUtil.rounding(driftLeftToRightAngle));

    }

    public void newWS1() {
        isNew = true;
        indexLeeway = null;
        searchTarget1 = null;
        searchTarget2 = null;
        searchTarget3 = null;
        cboSatuan = "0";
        incDescription = null;
        safetyFactor = 1.1;
        operationTime = null;
        zonaWaktu = "G";
        driftHours = 0;
        seaCurrentAngle = 0;
        seaCurrentSpeed = 0;
        seaCurrentSpeed_knot = 0;
        seaCurrentDistance = 0;
        surfaceWindAngle = 0;
        windCurrentSpeed = 0;
        windCurrentSpeed_knot = 0;
        windCurrentAngle = 0;
        windCurrentSpeedForCalcDist = 0;
        windCurrentSpeedForCalcDist_knot = 0;
        windCurrentDistance = 0;
        lwyDivergenceAngle = 0;
        lwySpeed = 0;
        lwyLeftAngle = 0;
        lwyDistance = 0;
        lwyRightAngle = 0;
        driftLeftDistanceInNm = 0;
        driftLeftToRightDistance = 0;
        distressCraftError = 0.1;
        driftRightDistanceInNm = 0;
        driftError = 0;
        searchCraftError = 0.1;
        driftErrorPctg = 12.5;
        totalProbableError = 0;
        searchRadius = 0;
        searchArea = 0;
    }

    private boolean objectValidasi() {
        boolean result = true;
        if (indexLeeway == null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Index Leeway tidak boleh kosong !");
            result = false;
        }
        if (operationTime == null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "TW Operation tidak boleh kosong !");
            result = false;
        }
        if (seaCurrentAngle == 0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Sea Current Angle tidak boleh kosong !");
            result = false;
        }
        if (seaCurrentSpeed == 0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Sea Current Speed tidak boleh kosong !");
            result = false;
        }
        if (surfaceWindAngle == 0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Surface Wind Angle tidak boleh kosong !");
            result = false;
        }
        if (windCurrentSpeed == 0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Wind Current Speed tidak boleh kosong !");
            result = false;
        }
        if (driftErrorPctg == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Drift Error tidak boleh kosong !");
            result = false;
        }
//monte carlo
        if (incidentPlanningMBean.getIsMonteCarlo()) {
            if (jmlData == 0) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Jumlah data harus diisi.");
                result = false;
            }
            System.out.println("varianceSea.intValue() "+varianceSea.intValue());
            if (varianceSea == 0.0) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Sea harus diisi.");
                result = false;
            }
            if (varianceWind == 0.0) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Wind harus diisi.");
                result = false;
            }
            System.out.println("seaCurrentAngle : "+seaCurrentAngle+", seaCurrentSpeed : "+seaCurrentSpeed+", varianceSea : "+ varianceSea);
            System.out.println("surfaceWindAngle : "+surfaceWindAngle+", windCurrentSpeed : "+windCurrentSpeed+", varianceWind : "+ varianceWind);
            if (seaCurrentAngle < varianceSea) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Sea tidak boleh lebih besar dari Sea Tidal Current (T)");
                result = false;
            }
            if (seaCurrentSpeed < varianceSea) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Sea tidak boleh lebih besar dari Sea Tidal Current (cm/s)");
                result = false;
            }
            if (surfaceWindAngle < varianceWind) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Sea tidak boleh lebih besar dari Surface Wind (T)");
                result = false;
            }
            if (windCurrentSpeed < varianceWind) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Variance Sea tidak boleh lebih besar dari Surface Wind (cm/s)");
                result = false;
            }
        }
        return result;
    }

    public void openWS1() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs1').show()");
    }

    public void batalWS1() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs1').hide()");
    }

    public void batalSaveWS1() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs1').hide()");
    }

    public void changeWorksheetName() {

        if (worksheetID.equals("")) {
            worksheetID = null;
        } else {
            driftCalcWorksheet1 = driftCalcWorksheet1Repo.findByWorksheetID(worksheetID);
            LastWorksheetID = worksheetID;
            worksheetName = driftCalcWorksheet1.getWorksheetName();
            createdBy = driftCalcWorksheet1.getIncident().getCreatedby();
            lastModified = driftCalcWorksheet1.getLastModified();
            if (lastModified == null) {
                lastModified = driftCalcWorksheet1.getDateCreated();
            }
        }

    }

    public List<SelectItem> getListWorksheetName() {

        try {
            incident = incidentRepo.findOne(currentIncidentID);
            if (listItemWorksheet1 == null) {
                listItemWorksheet1 = new ArrayList<>();
                listWorksheet1Model = driftCalcWorksheet1Repo.findAllByIncidentAndDeletedFalse(incident);
                listItemWorksheet1.add(new SelectItem("0", " "));

                for (DriftCalcWorksheet1 lst : listWorksheet1Model) {
                    listItemWorksheet1.add(new SelectItem(lst.getWorksheetID(), lst.getWorksheetName()));
                }

            }
        } catch (Exception ex) {
            listItemWorksheet1 = null;
        }
        return listItemWorksheet1;
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<DriftCalcWorksheet1> lis = driftCalcWorksheet1Repo.findAllByWorksheetIDContainingAndIncident(ProsiaConstant.SITES, incident);
        //System.out.println("--lis--"+lis);
        if (lis.isEmpty()) {
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            String maxId = driftCalcWorksheet1Repo.findClassByMaxId(ProsiaConstant.SITES);
            //String[] splitId = i.getIncidentid().split("-");
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
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return classId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //listWorksheet1Model = driftCalcWorksheet1Repo.findAllByIncident(incident);
        //search Incident
        currentIncidentID = incidentDriftCalculation.constIncidentID();
        driftCalcWorksheet1 = new DriftCalcWorksheet1();
        isNew = true;
        incident = incidentRepo.findAllByIncidentid(currentIncidentID);
        if (incident.getLatitude() == null && incident.getLongitude() == null) {
            return;
        } else {
            incLatitude = Double.parseDouble(incident.getLatitude());
            incLongitude = Double.parseDouble(incident.getLongitude());
        }
        viewCoordinate();
        SimpleDateFormat formatter = new SimpleDateFormat(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT);
        sdfDtz = new SimpleDateTimeZoneFormat();
        sdfDtz.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT);

        Calendar cal = Calendar.getInstance();
        cal.setTime(incident.getStartdate());
        if (incident.getStartdatetimezone() != null) {
            cal.setTimeZone(TimeZone.getTimeZone(incident.getStartdatetimezone()));
            incTime = formatZonaWaktu(sdfDtz.format(cal), incident.getStartdatetimezone());
        }
        lazyDataModelJPA = new LazyDataModelJPA(driftCalcWorksheet1Repo) {
            @Override
            protected Page getDatas(PageRequest request) {
                return driftCalcWorksheet1Repo.findAll(request);
            }
        };
    }

    public void initCoordinate() {
        coordinateLat = new Coordinate();
        coordinateLong = new Coordinate();
        coordinateLat.setType(true);
        coordinateLong.setType(false);
    }

    public void viewCoordinate() {
        if (incident.getLatitude() != null
                || StringUtils.isNotBlank(incident.getLatitude())) {
            coordinateLat.setType(true);
            coordinateLat.setCounter(1);
            Double latitude = Double.parseDouble(incident.getLatitude());
            coordinateLat.setDecimalDegrees(latitude);
            coordinateLat.converter();
            coordinateLat.setCounter(3);
        }
        if (incident.getLongitude() != null
                || StringUtils.isNotBlank(incident.getLongitude())) {
            coordinateLong.setType(false);
            coordinateLong.setCounter(1);
            Double longitude = Double.parseDouble(incident.getLongitude());
            coordinateLong.setDecimalDegrees(longitude);
            coordinateLong.converter();
            coordinateLong.setCounter(3);
        }
    }

    public void actionLatitude() {
        coordinateLat.converter();
        coordinateLat.setCounter(coordinateLat.getCounter() + 1);
        if (coordinateLat.getCounter() > 3) {
            coordinateLat.setCounter(1);
        }
    }

    public void actionLongitude() {
        coordinateLong.converter();
        coordinateLong.setCounter(coordinateLong.getCounter() + 1);
        if (coordinateLong.getCounter() > 3) {
            coordinateLong.setCounter(1);
        }
    }

    public String getUserFromReport() {
        return getCurrentUser().getParty().getFullName();
    }

    public void doPrint() {
        if (objectValidasi() == false) {
            return;
        }
        calculateWS1();
        String user = getCurrentUser().getParty().getFullName();
        //       log.info(driftCalcWorksheet1.toString()+"  : "+incTime);
        ReportUtilMBean reportUtilMBean = new ReportUtilMBean();
        System.out.println("");
        reportController.setParams(reportUtilMBean.generateWorksheet1Report(driftCalcWorksheet1, incTime, user));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/tools/open-report.xhtml?"
                + "path=%2Freport%2Freport_jasper%2Fworksheet1.jrxml')");
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
