/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.LatLng;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.controller.ReportController;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.constant.LabelValue;
import prosia.basarnas.constant.MapConstant;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.constant.StatusConstant;
import prosia.basarnas.constant.ValueMapConstant;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.repo.DriftCalcWorksheet8Repo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IndexSpeedRepo;
import prosia.basarnas.repo.IndexSweepWidthRepo;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.service.map_js.GPoly;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.model.map.TaskSearchArea;
import prosia.basarnas.web.util.CalculationInterface;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class DriftCalcWorksheet8MBean extends AbstractManagedBean implements InitializingBean {

    private final double DEFAULT_KM = 0.53961;
    private final double PRACTICAL_TRACK_SPACING_MULTIPLE = 0.25;
    private String currentIncidentID;
    private Incident incident;
    private String worksheetID;
    private String searchObject;
    private String searchPlatform;
    private ResAsset asset;
    private String worksheetName;
    private double meteorologicalVisibility;
    private double searchHeight;
    private String searchRepeat;
    private double windSpeed;
    private Boolean fatigueFactor;
    private double uncorrectedSweepWidth;
    private double weatherCorrectionFactor;
    private double speedCorrectionFactor;
    private double fatigueCorrectionFactor;
    private double sweepWidthFactor;
    private double practicalTrackSpacing;
    private double coverageFactor;
    private double probabilityOfDetection;
    private double searchArea;
    private double searchhours;
    private Date dateCreated;
    private String createdBy;
    private Date lastModified;
    private String modifiedBy;
    private boolean deleted;
    private String userSiteID;
    private double unit;

    private List<IncidentAsset> listSRU;
    private List<SelectItem> listItemSRU;
    private List<String> listSearchObject;
    private List<SelectItem> listItemSearchObject;
    private List<LabelValue> listMeteorological;
    private List<SelectItem> listItemMeteorological;
    private Double[] listHeight;
    private List<SelectItem> listItemHeight;
    private List<String> listRepeat;
    private List<SelectItem> listItemRepeat;
    private List<SelectItem> listItemWorksheet8;
    private List<String> listWorksheet8;
    private List<DriftCalcWorksheet8> listWorksheet8Model;
    private List<Double> listWidth;
    private IncidentAsset incidentAsset;
    private List<IncidentAsset> lstIncidentAsset;
    private String rescuer;
    private String user;
    private String meteorological;
    private String repeat;
    private DriftCalcWorksheet8 driftCalcWorksheet8;
    private LazyDataModelJPA<DriftCalcWorksheet8> lazyDataModelJPA;
    private double windSpeedInKmph;
    private Double sruSpeed = null;
    private List<Double> listIndexSpeed;
    private String cboLatitude;
    private String cboLongitude;
    private boolean isNew4 = false;
    private boolean isCalculated = false;
    private String LastWorksheetID;
    private Set<AssetSearchAreaBean> listAssetSearchAreaBean;
    private Coordinate coordinateLat;
    private Coordinate coordinateLong;
    private Boolean isPrint = false;
    private double luasTaskSearchArea;
    private double practicalTrackSpacingTSA;
    private String worksheetIDTaskSearchArea;
    private double luasArea;
    private String worksheetTaskSearchArea;
    private int radiusTypeTaskSearchArea;
    private prosia.basarnas.web.util.TaskSearchAreaController taskSearchAreaControllerUtil;
    private double radiusTaskSearchArea;
    private double lng1TaskSearchArea;
    private double lng2TaskSearchArea;
    private double startLatTaskSearchArea;
    private DriftCalcWorksheet1Result worksheetResult;

    @Autowired
    private DriftCalcWorksheet8Repo driftCalcWorksheet8Repo;

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private MstAssetTypeRepo mstAssetTypeRepo;

    @Autowired
    private IndexSweepWidthRepo indexSweepWidthRepo;

    @Autowired
    private IndexSpeedRepo indexSpeedRepo;

    @Autowired
    private ResAssetRepo resAssetRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;
    /*
    @Autowired
    private IncidentMBean incidentMBean;
     */
    @Autowired
    private IncidentDriftCalculation incidentDriftCalculation;

    @Autowired
    private MapController mapController;

    @Autowired
    private ReportController reportController;

    @Autowired
    private MapMBean mapMBean;

    public DriftCalcWorksheet8MBean() {
        listAssetSearchAreaBean = new HashSet<>();
        initCoordinate();
        coordinateLat.setCounter(3);
        coordinateLong.setCounter(3);
    }

    public void newWS8() {
        isNew4 = true;
        rescuer = null;
        user = null;
        unit = 0.0;
        searchObject = null;
        searchPlatform = null;
        windSpeed = 0;
        meteorological = null;
        searchArea = 0;
        searchHeight = 0;
        fatigueFactor = false;
        repeat = "First";
        uncorrectedSweepWidth = 0;
        practicalTrackSpacing = 0;
        weatherCorrectionFactor = 0;
        speedCorrectionFactor = 0;
        fatigueCorrectionFactor = 0;
        sweepWidthFactor = 0;
        coverageFactor = 0;
        probabilityOfDetection = 0;
        searchhours = 0;
    }

    private boolean objectValidasi() {
        boolean result = true;
        if (rescuer == null || rescuer.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "SRU tidak boleh kosong !");
            result = false;
        }
        if (searchObject == null || searchObject.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Search Object tidak boleh kosong !");
            result = false;
        }
        if (windSpeed == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Wind Speed tidak boleh kosong !");
            result = false;
        }
        if (meteorological == null || meteorological.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Meteorological tidak boleh kosong !");
            result = false;
        }
        if (searchArea == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Search Area tidak boleh kosong !");
            result = false;
        }
        if (searchHeight == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Search Height tidak boleh kosong !");
            result = false;
        }
        if (practicalTrackSpacing == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Practical TrackSpacing tidak boleh kosong !");
            result = false;
        }
        return result;
    }

    public void openWS8() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs8').show()");
    }

    public void deleteWS8() {
        if (incident != null) {
            driftCalcWorksheet8.setWorksheetID(LastWorksheetID);
            driftCalcWorksheet8.setIncident(incident);
            driftCalcWorksheet8.setDeleted(true);
            driftCalcWorksheet8Repo.save(driftCalcWorksheet8);
            newWS8();
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Worksheet berhasil di hapus !");
        }
    }

    public void pilihWS8() {
        driftCalcWorksheet8 = driftCalcWorksheet8Repo.findByworksheetID(worksheetID);

        if (driftCalcWorksheet8 != null) {
            isNew4 = false;
            asset = resAssetRepo.findAllByAssetid(driftCalcWorksheet8.getAsset().getAssetid());
            rescuer = asset.getAssetid();
            listSearchObject = indexSweepWidthRepo.findSearchObject("VESSEL");
            for (int x = 0; x < listSearchObject.size(); x++) {
                if (listSearchObject.get(x).equals(driftCalcWorksheet8.getSearchObject())) {
                    searchObject = driftCalcWorksheet8.getSearchObject();
                }
            }

            listMeteorological = ValueMapConstant.RESCUER_VISIBILITY_LIST.get("Vessel");
            for (LabelValue labelValue : listMeteorological) {
                if (labelValue.getValue().equals(driftCalcWorksheet8.getMeteorologicalVisibility())) {
                    meteorological = driftCalcWorksheet8.getMeteorologicalVisibility().toString();
                }
            }

            listHeight = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get("Vessel");
            for (int x = 0; x < listHeight.length; x++) {
                if (listHeight[x].doubleValue() == driftCalcWorksheet8.getSearchHeight()) {
                    searchHeight = driftCalcWorksheet8.getSearchHeight();
                }
            }
            repeat = driftCalcWorksheet8.getSearchRepeat();
            unit = driftCalcWorksheet8.getUnit();
            windSpeed = driftCalcWorksheet8.getWindSpeed();
            searchArea = driftCalcWorksheet8.getSearchArea();
            fatigueFactor = driftCalcWorksheet8.getFatigueFactor();
            uncorrectedSweepWidth = driftCalcWorksheet8.getUncorrectedSweepWidth();
            practicalTrackSpacing = driftCalcWorksheet8.getPracticalTrackSpacing();
            weatherCorrectionFactor = driftCalcWorksheet8.getWeatherCorrectionFactor();
            speedCorrectionFactor = driftCalcWorksheet8.getSpeedCorrectionFactor();
            fatigueCorrectionFactor = driftCalcWorksheet8.getFatigueCorrectionFactor();
            sweepWidthFactor = driftCalcWorksheet8.getSweepWidthFactor();
            coverageFactor = driftCalcWorksheet8.getCoverageFactor();
            probabilityOfDetection = driftCalcWorksheet8.getProbabilityOfDetection();
            searchhours = driftCalcWorksheet8.getSearchhours();

        }
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs8').hide()");
        RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws8");
    }

    public void openSaveDialog() {
        //validasi
        if (isCalculated == false) {
            calculateWS8();
        } else {
            if (objectValidasi() == false) {
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs8').show()");
    }

    public void simpanWS8() throws CloneNotSupportedException {

        DriftCalcWorksheet8 cloneDriftCalcWorksheet8 = (DriftCalcWorksheet8) driftCalcWorksheet8.clone();

        asset = resAssetRepo.findAllByAssetid(rescuer);
        driftCalcWorksheet8.setAsset(asset);
        driftCalcWorksheet8.setCoverageFactor(coverageFactor);
        driftCalcWorksheet8.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        driftCalcWorksheet8.setDateCreated(new Date());
        driftCalcWorksheet8.setDeleted(false);
        driftCalcWorksheet8.setFatigueCorrectionFactor(fatigueCorrectionFactor);
        driftCalcWorksheet8.setFatigueFactor(fatigueFactor);
        driftCalcWorksheet8.setIncident(incident);
        driftCalcWorksheet8.setLastModified(null);
        driftCalcWorksheet8.setMeteorologicalVisibility(meteorologicalVisibility);
        driftCalcWorksheet8.setModifiedBy(null);
        driftCalcWorksheet8.setPracticalTrackSpacing(practicalTrackSpacing);
        driftCalcWorksheet8.setProbabilityOfDetection(probabilityOfDetection);
        driftCalcWorksheet8.setSearchArea(searchArea);
        driftCalcWorksheet8.setSearchHeight(searchHeight);
        driftCalcWorksheet8.setSearchObject(searchObject);
        driftCalcWorksheet8.setSearchPlatform("Vessel");
        driftCalcWorksheet8.setSearchRepeat(repeat);
        driftCalcWorksheet8.setSearchhours(searchhours);
        driftCalcWorksheet8.setSpeedCorrectionFactor(speedCorrectionFactor);
        driftCalcWorksheet8.setSweepWidthFactor(sweepWidthFactor);
        driftCalcWorksheet8.setUncorrectedSweepWidth(uncorrectedSweepWidth);
        driftCalcWorksheet8.setUnit(unit);
        driftCalcWorksheet8.setUserSiteID(ProsiaConstant.SITES);
        driftCalcWorksheet8.setWeatherCorrectionFactor(weatherCorrectionFactor);
        driftCalcWorksheet8.setWindSpeed(windSpeed);
        driftCalcWorksheet8.setWorksheetID(formatclassId());
        driftCalcWorksheet8.setWorksheetName(worksheetName);

        if (isNew4 == true) {
            driftCalcWorksheet8.setLastModified(null);
            driftCalcWorksheet8.setDeleted(false);
            driftCalcWorksheet8Repo.save(driftCalcWorksheet8);
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Berhasil di simpan");
        } else {
            cloneDriftCalcWorksheet8.setWorksheetID(LastWorksheetID);
            cloneDriftCalcWorksheet8.setDeleted(true);
            cloneDriftCalcWorksheet8.setLastModified(new Date());
            cloneDriftCalcWorksheet8.setModifiedBy(menuNavMBean.getUserSession().getUsername());
            driftCalcWorksheet8Repo.save(cloneDriftCalcWorksheet8);

            driftCalcWorksheet8.setDeleted(false);
            driftCalcWorksheet8.setLastModified(null);
            driftCalcWorksheet8.setModifiedBy(null);
            driftCalcWorksheet8Repo.save(driftCalcWorksheet8);

            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Berhasil di update");
        }

        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs8').hide()");
    }

    public void batalWS8() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs8').hide()");
    }

    public void batalSaveWS8() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs8').hide()");
    }

    private void clearOpenDialog() {
        worksheetID = null;
        createdBy = null;
        lastModified = null;
    }

    public void changeWorksheetName() {

        if (worksheetID.equals("")) {
            worksheetID = null;
        } else {
            driftCalcWorksheet8 = driftCalcWorksheet8Repo.findByworksheetID(worksheetID);
            LastWorksheetID = worksheetID;
            worksheetName = driftCalcWorksheet8.getWorksheetName();
            createdBy = driftCalcWorksheet8.getIncident().getCreatedby();
            lastModified = driftCalcWorksheet8.getLastModified();
            if (lastModified == null) {
                lastModified = driftCalcWorksheet8.getDateCreated();
            }
        }

    }

    public List<SelectItem> getListWorksheetName() {
        try {
            incident = incidentRepo.findOne(currentIncidentID);
            if (listItemWorksheet8 == null) {
                listItemWorksheet8 = new ArrayList<>();
                listWorksheet8Model = driftCalcWorksheet8Repo.findAllByIncidentAndDeletedFalse(incident);
                listItemWorksheet8.add(new SelectItem("0", " "));
                for (DriftCalcWorksheet8 lst : listWorksheet8Model) {
                    listItemWorksheet8.add(new SelectItem(lst.getWorksheetID(), lst.getWorksheetName()));
                }

            }
        } catch (Exception ex) {
            listItemWorksheet8 = null;
        }
        return listItemWorksheet8;
    }

    public void changeSRUName() {
        if (rescuer.equals("")) {
            rescuer = null;
        } else {
            listSearchObject = indexSweepWidthRepo.findSearchObject("VESSEL");
        }
        searchObject = null;
        searchPlatform = null;
        meteorological = null;
        searchHeight = 0;
    }

    public void changeSearchObject() {
        if (searchObject.equals("")) {
            searchObject = null;
        } else {
            listMeteorological = ValueMapConstant.RESCUER_VISIBILITY_LIST.get("Vessel");
        }
    }

    public List<SelectItem> getListSearchObject() {
        if (listSearchObject != null) {
            listItemSearchObject = new ArrayList<>();
            //listSearchObject = driftCalcWorksheet2Repo.findSearchObject("Fixed Wing");
            listItemSearchObject.add(new SelectItem(""));
            for (String lst : listSearchObject) {
                listItemSearchObject.add(new SelectItem(lst));
            }
        }
        return listItemSearchObject;
    }

    public void changeMeteorogical() {
        if (meteorological.equals("")) {
            meteorological = null;
        } else {
            listHeight = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get("Vessel");
            meteorologicalVisibility = Double.parseDouble(meteorological);
        }
    }

    public List<SelectItem> getListMeteorogical() {
        if (listMeteorological != null) {
            listItemMeteorological = new ArrayList<>();
            listItemMeteorological.add(new SelectItem(""));
            for (LabelValue labelValue : listMeteorological) {
                listItemMeteorological.add(new SelectItem(labelValue.getLabel()));
            }
        }
        return listItemMeteorological;
    }

    public List<SelectItem> getListHeight() {
        if (listHeight != null) {
            listItemHeight = new ArrayList<>();
            listItemHeight.add(new SelectItem(""));
            for (Double nilai : listHeight) {
                listItemHeight.add(new SelectItem(nilai));
            }
        }
        return listItemHeight;
    }

    public void calculateWS8() {
        //validasi
        if (objectValidasi() == false) {
            return;
        }
        isCalculated = true;
        //driftCalcWorksheet8 = new DriftCalcWorksheet8();

        uncorrectedSweepWidth = getUncorrectedSweepWidth("VESSEL", searchObject, searchHeight, meteorologicalVisibility);
        uncorrectedSweepWidth = DecimalUtil.rounding(uncorrectedSweepWidth);

        practicalTrackSpacing = validatePracticalTrackSpacing(practicalTrackSpacing, uncorrectedSweepWidth);

        weatherCorrectionFactor = getWeatherCorrectionFactor(searchObject, windSpeedInKmph);

        speedCorrectionFactor = getSpeedCorrectionFactor("VESSEL", searchObject, 0.0);
        speedCorrectionFactor = DecimalUtil.rounding(speedCorrectionFactor);

        fatigueCorrectionFactor = getFatigueCorrectionFactor(fatigueFactor);
        fatigueCorrectionFactor = DecimalUtil.rounding(fatigueCorrectionFactor);

        sweepWidthFactor = uncorrectedSweepWidth * weatherCorrectionFactor
                * speedCorrectionFactor * fatigueCorrectionFactor;
        sweepWidthFactor = DecimalUtil.rounding(sweepWidthFactor);
        coverageFactor = sweepWidthFactor / practicalTrackSpacing;
        coverageFactor = DecimalUtil.rounding(coverageFactor);

        probabilityOfDetection = getProbabilityOfDetection(repeat, coverageFactor);
        probabilityOfDetection = DecimalUtil.rounding(probabilityOfDetection);

        if (probabilityOfDetection < 60) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Nilai Probability Of Detection lebih kecil dari 60 %");
        }

        sruSpeed = incidentAssetRepo.findSpeed(currentIncidentID, rescuer);
        if (sruSpeed == null || sruSpeed == 0) {
            sruSpeed = 1.0;
        }

        searchhours = searchArea / (sruSpeed * practicalTrackSpacing);
        searchhours = DecimalUtil.rounding(searchhours);
    }

    private double getUncorrectedSweepWidth(String rescuer, String searchObject, double searchHeight, double meteorologicalVisibility) {
        double result = 0.0;
        listWidth = indexSweepWidthRepo.findWidth(rescuer, searchObject, searchHeight, meteorologicalVisibility);
        if (listWidth.size() > 0) {
            result = listWidth.get(0);
        }
        return result;
    }

    private double validatePracticalTrackSpacing(double practicalTrackSpacing, double uncorrectedSweepWidth) {
        double result = 0.0;
        if (practicalTrackSpacing == 0.0 || practicalTrackSpacing == 0.0) {
            result = DecimalUtil.roundingUpPerMultiple(uncorrectedSweepWidth,
                    PRACTICAL_TRACK_SPACING_MULTIPLE);
        } else {
            result = practicalTrackSpacing;
        }

        if (result == 0.0) {
            result = PRACTICAL_TRACK_SPACING_MULTIPLE;
        }
        return result;
    }

    private double getWeatherCorrectionFactor(String searchObject, double windSpeedInKmph) {
        double result = 0.0;
        if (windSpeedInKmph < 28.0) {
            // windSpeedInKmph < 28
            if (ValueMapConstant.SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.contains(
                    searchObject)) {
                result = 1.0;
            } else {
                result = 1.0;
            }
        } else if (28 <= windSpeedInKmph && windSpeedInKmph <= 46) {
            // windSpeedInKmph 28 - 46
            if (ValueMapConstant.SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.contains(
                    searchObject)) {
                result = 0.5;
            } else {
                result = 0.8;
            }
        } else {
            // windSpeedInKmph > 46
            if (ValueMapConstant.SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.contains(
                    searchObject)) {
                result = 0.25;
            } else {
                result = 0.5;
            }
        }

        return result;
    }

    private double getSpeedCorrectionFactor(String rescuer, String searchObject, double searchPlatformTas) {
        double koreksi = 1.0;
        if (StatusConstant.RESCUER_HELICOPTER.equalsIgnoreCase(rescuer)
                || StatusConstant.RESCUER_FIXED_WING.equalsIgnoreCase(rescuer)) {

            listIndexSpeed = indexSpeedRepo.findByRescuerAndSearchObjectAndSpeed(rescuer, searchObject, searchPlatformTas);
            if (listIndexSpeed.size() > 0) {
                koreksi = listIndexSpeed.get(0);
            } else {
                koreksi = 1.0;
            }
        }
        return koreksi;
    }

    private double getFatigueCorrectionFactor(boolean fatigueFactor) {
        double result = fatigueFactor ? 0.9 : 1.0;
        return result;
    }

    private double getProbabilityOfDetection(String searchRepeat, double coverageFactor) {
        double result = 0.0;
        if (StatusConstant.SEARCH_REPEAT_FIRST.equals(searchRepeat)) {
            result = 18.25 * Math.pow(coverageFactor, 4) - 42.02 * Math.pow(
                    coverageFactor, 3) + 7.213 * Math.pow(coverageFactor, 2) + 98.06 * coverageFactor;
        } else if (StatusConstant.SEARCH_REPEAT_SECOND.equals(searchRepeat)) {
            result = 41.10 * Math.pow(coverageFactor, 4) - 28.88 * Math.pow(
                    coverageFactor, 3) - 105.0 * Math.pow(coverageFactor, 2) + 198.1 * coverageFactor;
        } else if (StatusConstant.SEARCH_REPEAT_THIRD.equals(searchRepeat)) {
            result = -58.86 * Math.pow(coverageFactor, 4) + 212.5 * Math.pow(
                    coverageFactor, 3) - 366.8 * Math.pow(coverageFactor, 2) + 306.3 * coverageFactor;
        } else if (StatusConstant.SEARCH_REPEAT_FOURTH.equals(searchRepeat)) {
            result = -152.7 * Math.pow(coverageFactor, 4) + 425.6 * Math.pow(
                    coverageFactor, 3) - 579.1 * Math.pow(coverageFactor, 2) + 384.7 * coverageFactor;
        } else if (StatusConstant.SEARCH_REPEAT_FIFTH.equals(searchRepeat)) {
            // TODO 1025.0 ???
            result = 176.7 * Math.pow(coverageFactor, 5) - 589.7 * Math.pow(
                    coverageFactor, 4) + 1025.0 * Math.pow(coverageFactor, 3) - 979.4 * Math.
                    pow(coverageFactor, 2) + 488.7 * coverageFactor;
        }

        return result;
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<DriftCalcWorksheet8> lis = driftCalcWorksheet8Repo.findAllByWorksheetIDContainingAndIncident(ProsiaConstant.SITES, incident);
        //System.out.println("--lis--"+lis);
        if (lis.isEmpty()) {
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            String maxId = driftCalcWorksheet8Repo.findClassByMaxId(ProsiaConstant.SITES);
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

    public void simpanTaskSearchArea() {
        if (worksheetTaskSearchArea.isEmpty() || worksheetTaskSearchArea.equals(null)) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Worksheet tidak boleh kosong !");
        }
        taskSearchAreaControllerUtil = new prosia.basarnas.web.util.TaskSearchAreaController(radiusTaskSearchArea, lng1TaskSearchArea, lng2TaskSearchArea, startLatTaskSearchArea);
        createNewAndAddToNavigator(incidentAsset, luasTaskSearchArea, practicalTrackSpacingTSA, radiusTypeTaskSearchArea, worksheetTaskSearchArea, worksheetIDTaskSearchArea, taskSearchAreaControllerUtil, worksheetResult);
        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchAreaWs8').hide()");
    }

    public void closeTaskSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchAreaWs8').hide()");
    }

    public void createNewAndAddToNavigator(IncidentAsset incidentAsset, double luas, double s,
            int radiusType, String name, String driftTaskAreaID, prosia.basarnas.web.util.TaskSearchAreaController taskSearchAreaControllerUtil,
            DriftCalcWorksheet1Result worksheetResult) {

        if (radiusType == 0) {
            CalculationInterface ci = taskSearchAreaControllerUtil.insertHalfRadius(luas);
            if (ci == null) {
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Tidak dapat menampilkan Task Search Area. Luas Task Search Area melebihi kapasitas Search Area");
            } else {
                createGTaskByCI(ci, incidentAsset, s, name, radiusType, driftTaskAreaID, worksheetResult);
            }

        } else {
            CalculationInterface ci = taskSearchAreaControllerUtil.insertOneRadius(luas);

            if (ci == null) {
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Tidak dapat menampilkan Task Search Area. Luas Task Search Area melebihi kapasitas Search Area");
            } else {
                createGTaskByCI(ci, incidentAsset, s, name, radiusType, driftTaskAreaID, worksheetResult);
            }
        }

    }

    private void createGTaskByCI(CalculationInterface ci, IncidentAsset sru, Double s, String name, int radiusType, String driftTaskAreaID, DriftCalcWorksheet1Result worksheetResult) {

        String parrentID = worksheetResult.getParrentID();
        String childID = GPoly.generateParrentID();
        Double latPivot = ci.getUnrotateStart().getLatNumb() - (ci.getHeight() / 2) * CommonConstant.ONE_NM_TO_DEG;
        Double lngPivot = ci.getUnrotateStart().getLngNumb() + (ci.getWidth() / 2) * CommonConstant.ONE_NM_TO_DEG;
        LatLng unrotatePivot = new LatLng(latPivot, lngPivot);
        
//        GTaskSearchArea taskSearchArea = new GTaskSearchArea(
//                null, ci.getUnrotateStart(), unrotatePivot, 
//                sru, ci.getWidth(), ci.getHeight(), s, radiusType, name, parrentID, childID, driftTaskAreaID);
        TaskSearchArea tsa = new TaskSearchArea();
        tsa.setUnrotateStart(new LatLng(ci.getUnrotateStart().getLatNumb(), ci.getUnrotateStart().getLngNumb()));
        System.out.println("--setUnrotateStart--" + tsa.getUnrotateStart());
        tsa.setUnrotatePivot(unrotatePivot);
        tsa.setIncidentAsset(sru);
        tsa.setWidth(ci.getWidth());
        tsa.setHeight(ci.getHeight());
        tsa.setTrackSpacing(s);
        tsa.setRadiusType(radiusType);
        tsa.setName(name);
        CommonUtil commonUtil = new CommonUtil();

        LatLng zeroDegreeFromDatum = new LatLng(
                worksheetResult.getDatumLatLng().getLatNumb() + (worksheetResult.getRadius() * CommonConstant.ONE_NM_TO_DEG),
                worksheetResult.getDatumLatLng().getLngNumb() - (worksheetResult.getRadius() * CommonConstant.ONE_NM_TO_DEG));
        double nwDegree = 0.0;
        LatLng pivot = new LatLng(worksheetResult.getDatumLatLng().getLatNumb(), worksheetResult.getDatumLatLng().getLngNumb());

        for (LatLng ll : mapMBean.getGenerateValueRequestBean().getVertexs()) {
            if ((ll.getLat() > pivot.getLat()
                    && ll.getLng() < pivot.getLng())
                    || (ll.getLat() > pivot.getLat()
                    && ll.getLng() == pivot.getLng())) {
                nwDegree = commonUtil.toDeg(commonUtil.getAngle(pivot, zeroDegreeFromDatum, ll));
            }
        }

        tsa.setTiltDrift(nwDegree);
        tsa.setPivotDatumPoint(pivot);
        tsa.setParrentID(parrentID);
        tsa.setChildID(childID);
        tsa.setDriftTaskAreaID(driftTaskAreaID);
        tsa.setTypeTaskArea(MapCalculationType.DRIFT_CALCULATION);

        mapMBean.setIncident(incident);
        mapMBean.getListTaskSearchArea().add(tsa);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/map/map/list.xhtml?search=DRIFTCALCULATION"
                + "&incident="
                + incident.getIncidentid() + "', "
                + "'_newtab')");

    }

    public void resetGridSruSearchArea() {
        // List<DriftCalcWorksheet2> worksheetList = null;
        ResAsset asset2 = null;
        Double speed2 = null; // in knots
        Double endurance2 = null; // in hours
        Double practicalTrackSpacing2 = null;
        Double distanceAssetToDatum2 = null;
        Double searchArea2 = null;
        Double searchHours2 = null;
        Double latitudeTo2 = null;
        Double longitudeTo2 = null;
        Double latitudeFrom2 = null;
        Double longitudeFrom2 = null;

        AssetSearchAreaBean assetSearchAreaBean2 = null;
        List<IncidentAsset> incAssetList2 = null;
        IncidentAsset incAsset2 = null;
        List<DriftCalcWorksheet8> listWorksheet8SA;
        if (cboLatitude == null || cboLatitude.isEmpty()) {
            latitudeTo2 = 0.0;
        } else {
            latitudeTo2 = Double.parseDouble(cboLatitude);
        }
        if (cboLongitude == null || cboLongitude.isEmpty()) {
            longitudeTo2 = 0.0;
        } else {
            longitudeTo2 = Double.parseDouble(cboLongitude);
        }
        if (latitudeTo2 == 0.0 && longitudeTo2 == 0.0) {
            return;
        }

        listWorksheet8SA = driftCalcWorksheet8Repo.findAllByIncidentAndDeletedFalse(incident);
        if (listWorksheet8SA != null) {
            //incAssetList = incidentAssetRepo.findByIncidentAndAsset(incident, asset);
            for (DriftCalcWorksheet8 lst : listWorksheet8SA) {
                incAssetList2 = incidentAssetRepo.findByIncidentAndAssetAndDeletedFalse(lst.getIncident(), lst.getAsset());
//                System.out.println("--lst.getAsset()--"+lst.getAsset());
                if (incAssetList2 != null) {
                    incAsset2 = incAssetList2.get(0);
                    if (lst.getAsset() != null) {
                        asset2 = lst.getAsset();
                        speed2 = asset2.getSpeed(); // should be in knots
                        if (speed2 == null) {
                            speed2 = 0.0;
                        }
                        endurance2 = asset2.getEndurance(); // should be in hours
                        if (endurance2 == null) {
                            endurance2 = 0.0;
                        }
                        practicalTrackSpacing2 = lst.getPracticalTrackSpacing();
                        latitudeFrom2 = Double.parseDouble(asset2.getLatitude());
                        longitudeFrom2 = Double.parseDouble(asset2.getLongitude());
                        if (latitudeFrom2 != 0.0 && longitudeFrom2 != 0.0) {
                            distanceAssetToDatum2 = MapCalculation.calculateDistanceInNm(
                                    latitudeFrom2, longitudeFrom2, latitudeTo2, longitudeTo2);

                            searchHours2 = endurance2 - (distanceAssetToDatum2 * 2) / speed2;

                            if (Double.isNaN(searchHours2)) {
                                searchHours2 = 0.0;
                            } else if (searchHours2 > 0) {
                                searchHours2 = DecimalUtil.rounding(searchHours2);
                            }

                            searchArea2 = speed2 * practicalTrackSpacing2 * searchHours2;
                            if (Double.isNaN(searchArea2)) {
                                searchArea2 = 0.0;
                            } else if (searchArea2 > 0) {
                                searchArea2 = DecimalUtil.rounding(searchArea2);
                            }

                            assetSearchAreaBean2 = new AssetSearchAreaBean();
                            assetSearchAreaBean2.setWorksheetID(lst.getWorksheetID());
                            assetSearchAreaBean2.setWorksheet8(lst);
                            assetSearchAreaBean2.setIncidentAsset(incAsset2);
                            assetSearchAreaBean2.setLatitude(Double.parseDouble(asset2.getLatitude()));
                            assetSearchAreaBean2.setLongitude(Double.parseDouble(asset2.getLongitude()));
                            assetSearchAreaBean2.setSpeed(speed2);
                            assetSearchAreaBean2.setEndurance(endurance2);
                            assetSearchAreaBean2.setPracticalTrackSpacing(practicalTrackSpacing2);
                            assetSearchAreaBean2.setSearchHours(searchHours2);
                            assetSearchAreaBean2.setSearchArea(searchArea2);
                            listAssetSearchAreaBean.add(assetSearchAreaBean2);
                            
                        }
                    }
                }
            }
        }
    }

    public void deleteRowList(AssetSearchAreaBean areaBean) {
        listAssetSearchAreaBean.remove(areaBean);
    }

    public void doShowTaskSearchAreaOnMap(AssetSearchAreaBean assetSearchAreaBean) {
//        System.out.println("--doShowTaskSearchAreaOnMap--" + assetSearchAreaBean);
//        if (mapController.getCurrentDriftCalculation() == null) {
//            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Informasi", "Tidak bisa menampilkan Task Search Area. karna tidak perdapat Search Area pada Map");
//            return;
//        }
//        try {
//            String drift = MapController.checkDriftCalculation(MapConstant.CHECK_DRIFT_CALCULATION);
//            if (!drift.equals(MapConstant.DRIFT_CALCULATION_ACTIVE)) {
//                addPopUpMessage(FacesMessage.SEVERITY_INFO,"Informasi", "Drift Calculation tidak ditemukan pada Map, Mungkin anda belum menampilkannya pada map");
//                return;
//            }
//            if (assetSearchAreaBean.getSearchArea() < 0) {
//                addPopUpMessage(FacesMessage.SEVERITY_INFO,"Informasi", "Tidak bisa menampilkan Task Search Area pada Map nilai luas Search Area terlalu kecil.");
//                return;
//            }
//            doViewParameterDialog(assetSearchAreaBean);
//        } catch (Exception e) {
//            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Error", "Proses gagal");
//        }
        doViewParameterDialog(assetSearchAreaBean);
    }

    private void doViewParameterDialog(AssetSearchAreaBean asb) {
//        Double luasTaskSearchArea = asb.getSearchArea();
//        try {
//            if (searchArea < luasTaskSearchArea) {
//                luasTaskSearchArea = searchArea;
//            }
//        } catch (Exception e) {
//        }
        /*
        TaskSearchAreaParamDialog taskSADialog = new TaskSearchAreaParamDialog(Main.sarMDI, luasTaskSearchArea) {

            @Override
            protected void btnOKActionPerformed(ActionEvent e, int radiusType, String nama, Double luasArea) {
                AssetSearchAreaBean assetSearchAreaBean = (AssetSearchAreaBean) getInerObject()[0];
                mapController.getTaskSearchAreaController().createNewAndAddToNavigator(assetSearchAreaBean.getIncidentAsset(), luasArea, assetSearchAreaBean.getPracticalTrackSpacing(), radiusType, nama, assetSearchAreaBean.getWorksheetID());
            }
        };
         */
        luasTaskSearchArea = asb.getSearchArea();
        incidentAsset = asb.getIncidentAsset();
        practicalTrackSpacingTSA = asb.getPracticalTrackSpacing();
        worksheetIDTaskSearchArea = asb.getWorksheetID();

        try {
            if (searchArea < luasTaskSearchArea) {
                luasTaskSearchArea = searchArea;
            }
        } catch (Exception e) {
        }
        this.setLuasArea(0);

        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchAreaWs8').show()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //currentIncidentID = incidentMBean.getIncident().getIncidentid();
        currentIncidentID = incidentDriftCalculation.constIncidentID();
        incident = incidentRepo.findAllByIncidentid(currentIncidentID);
        driftCalcWorksheet8 = new DriftCalcWorksheet8();
        isNew4 = true;
        lazyDataModelJPA = new LazyDataModelJPA(driftCalcWorksheet8Repo) {
            @Override
            protected Page getDatas(PageRequest request) {
                return driftCalcWorksheet8Repo.findAll(request);
            }
        };
        loadSru();
        taskSearchAreaControllerUtil.countTaskSearchArea = 0;
        taskSearchAreaControllerUtil.nextLng2SA = null;
    }

    private void loadSru() {
        listItemSRU = new ArrayList<>();
        if (listSRU == null) {
            listSRU = incidentAssetRepo.findAllByIncident(incident);
            if (listSRU != null) {
                listItemSRU.add(new SelectItem(""));
                for (IncidentAsset asset : listSRU) {
                    if (StringUtils.isNotBlank(asset.getVehicleType())
                            && asset.getVehicleType().equalsIgnoreCase("VESSEL")
                            && asset.isDeleted() == false) {
                        listItemSRU.add(new SelectItem(asset.getAsset().getAssetid(), asset.getName()));
                    }
                }
            }
        }

    }

    public void initCoordinate() {
        coordinateLat = new Coordinate();
        coordinateLong = new Coordinate();
        coordinateLat.setType(true);
        coordinateLong.setType(false);
    }

    public void viewCoordinate() {
        if (cboLatitude != null
                || StringUtils.isNotBlank(cboLatitude)) {
            coordinateLat.setType(true);
            coordinateLat.setCounter(1);
            Double latitude = Double.parseDouble(cboLatitude);
            coordinateLat.setDecimalDegrees(latitude);
            coordinateLat.converter();
            coordinateLat.setCounter(3);
        }
        if (cboLongitude != null
                || StringUtils.isNotBlank(cboLongitude)) {
            coordinateLong.setType(false);
            coordinateLong.setCounter(1);
            Double longitude = Double.parseDouble(cboLongitude);
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

    public void doPrint() throws CloneNotSupportedException, CloneNotSupportedException {
        calculateWS8();

        if (objectValidasi() == false) {
            return;
        }
        String rescuer = driftCalcWorksheet8.getSearchPlatform();
        String user = getCurrentUser().getParty().getFullName();
        Double[] eyeHeightArr = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get(rescuer);
        DriftCalcWorksheet8[] worksheet8s = new DriftCalcWorksheet8[eyeHeightArr.length];
        DriftCalcWorksheet8 worksheet8 = null;
        int indexSelected = 0;

        for (int i = 0; i < eyeHeightArr.length; i++) {
            if (driftCalcWorksheet8.getSearchHeight().doubleValue() == eyeHeightArr[i].
                    doubleValue()) {
                worksheet8s[i] = driftCalcWorksheet8;
                indexSelected = i;
            } else {

                worksheet8 = new DriftCalcWorksheet8();
                worksheet8 = (DriftCalcWorksheet8) driftCalcWorksheet8.clone();
                worksheet8.setSearchHeight(eyeHeightArr[i]);
                worksheet8.setSearchObject(searchObject);
                worksheet8.setSearchPlatform(searchPlatform);
                worksheet8.setMeteorologicalVisibility(meteorologicalVisibility);
                worksheet8.setSearchHeight(searchHeight);
                worksheet8.setSearchRepeat(searchRepeat);
                worksheet8.setWindSpeed(windSpeedInKmph);
                worksheet8.setFatigueFactor(fatigueFactor);
                worksheet8.setUncorrectedSweepWidth(uncorrectedSweepWidth);
                worksheet8.setWeatherCorrectionFactor(weatherCorrectionFactor);
                worksheet8.setSpeedCorrectionFactor(speedCorrectionFactor);
                worksheet8.setFatigueCorrectionFactor(fatigueCorrectionFactor);
                worksheet8.setSweepWidthFactor(sweepWidthFactor);
                worksheet8.setPracticalTrackSpacing(practicalTrackSpacing);
                worksheet8.setCoverageFactor(coverageFactor);
                worksheet8.setProbabilityOfDetection(probabilityOfDetection);
                worksheet8.setSearchArea(searchArea);
                worksheet8.setSearchhours(searchhours);
                worksheet8s[i] = worksheet8;
            }
        }

        // TODO sent all worksheet to jasper report
        DriftCalcWorksheet8 dwc1 = null;
        DriftCalcWorksheet8 dwc2 = null;

        if (worksheet8s.length >= 1) {
            dwc1 = worksheet8s[0];
        }
        if (worksheet8s.length >= 2) {
            dwc2 = worksheet8s[1];
        }

        ReportUtilMBean reportUtilMBean = new ReportUtilMBean();
        reportController.setParams(reportUtilMBean.returnMapWorksheet8Report(dwc1, dwc2, user));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/tools/open-report.xhtml?" + "path=%2Freport%2Freport_jasper%2Fworksheet8.jrxml')");
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
