/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.DateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.swing.JOptionPane;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
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
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IndexSweepWidth;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.repo.DriftCalcWorksheet2Repo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IndexSpeedRepo;
import prosia.basarnas.repo.IndexSweepWidthRepo;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.service.ApplicationPropertiesHandler;
//import prosia.basarnas.service.DecimalUtil;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.service.map_js.GPoly;
import prosia.basarnas.service.map_js.TaskSearchAreaController;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.model.map.GTaskSearchArea;
import prosia.basarnas.web.model.map.TaskSearchArea;
import prosia.basarnas.web.util.CalculationInterface;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
@Data
public class DriftCalcWorksheet2MBean extends AbstractManagedBean implements InitializingBean, Serializable {
    
    private final double PRACTICAL_TRACK_SPACING_MULTIPLE = 0.25;
    private DriftCalcWorksheet2 driftCalcWorksheet2;
    private LazyDataModelJPA<DriftCalcWorksheet2> lazyDataModelJPA;
    private String worksheetID;
    private Incident incident;
    private ResAsset asset;
    private MstAssetType mstAssetType;
    private IncidentAsset incidentAsset;
    private IndexSweepWidth indexSweepWidth;
    private String worksheetName;
    
    private String rescuer;
    private String searchObject;
    private String searchPlatform;
    private String meteorological;
    private String height;
    private String repeat;
    private double searchPlatformTas;
    
    private double meteorologicalVisibility;
    private double searchHeight;
    private double searchRepeat;
    private double windSpeed;
    private double windSpeedInKnots;
    private double windSpeedInKmph;
    private boolean fatigueFactor;
    private double practicalTrackSpacing;
    private double searchArea;
    private double uncorrectedSweepWidth;
    private double weatherCorrectionFactor;
    private double speedCorrectionFactor;
    private double fatigueCorrectionFactor;
    private double sweepWidthFactor;
    private double coverageFactor;
    private double probabilityOfDetection;
    private double searchHours;
    private Date dateCreated;
    private String createdBy;
    private Date lastModified;
    private String modifiedBy;
    private boolean deleted;
    private String userSiteID;
    private Double unit;
    private String user;
    private String currentIncidentID;
    private List<IncidentAsset> listSRU;
    private List<SelectItem> listItemSRU;
    private List<String> listSearchObject;
    private List<SelectItem> listItemSearchObject;
    private List<LabelValue> listSearchPlatform;
    private List<SelectItem> listItemSearchPlatform;
    private List<LabelValue> listMeteorological;
    private List<SelectItem> listItemMeteorological;
    private Double[] listHeight;
    private List<SelectItem> listItemHeight;
    private List<String> listRepeat;
    private List<SelectItem> listItemRepeat;
    private double assetSpeed;
    private List<String> listAssetSpeed;
    private List<Double> listIndexSpeed;
    private List<Double> listWidth;
    private List<MstAssetType> listMstAssetType;
    private List<IndexSweepWidth> listIndexSweepWidth;
    private List<SelectItem> listItemWorksheet2;
    private List<String> listWorksheet2;
    private List<DriftCalcWorksheet2> listWorksheet2Model;
    private String cboLatitude;
    private String cboLongitude;
    private boolean isNew2 = false;
    private boolean isCalculated = false;
    private String LastWorksheetID;
    private Set<AssetSearchAreaBean> listAssetSearchAreaBean;
    private int selectedRow;
    private Coordinate coordinateLat;
    private Coordinate coordinateLong;
    private Boolean isPrint = false;
    
    private String worksheetTaskSearchArea;
    private int radiusTypeTaskSearchArea;
    private double luasTaskSearchArea;
    private double luasArea;
    private double practicalTrackSpacingTSA;
    private String worksheetIDTaskSearchArea;
    
    private prosia.basarnas.web.util.TaskSearchAreaController taskSearchAreaControllerUtil;
    private double radiusTaskSearchArea;
    private double lng1TaskSearchArea;
    private double lng2TaskSearchArea;
    private double startLatTaskSearchArea;
    private DriftCalcWorksheet1Result worksheetResult;
       
    public static final String PDF_FILE_PATH
            = ApplicationPropertiesHandler.getProperty(ApplicationPropertiesHandler.REPORT_GENERATE_FOLDER) + "Worksheet2.pdf";
    
    @Autowired
    private DriftCalcWorksheet2Repo driftCalcWorksheet2Repo;
    
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
    
    private TaskSearchAreaController taskSearchAreaController;
    
    public DriftCalcWorksheet2MBean() {
        //incident = new Incident();
        //listSRU = null;
        //newWS2();
        taskSearchAreaController = new TaskSearchAreaController(mapController);
        listAssetSearchAreaBean = new HashSet<>();
        initCoordinate();
        coordinateLat.setCounter(3);
        coordinateLong.setCounter(3);
    }
    
    public void newWS2() {
        isNew2 = true;
        rescuer = null;
        unit = 0.0;
        searchObject = null;
        searchPlatform = null;
        windSpeed = 0;
        user = null;
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
        searchHours = 0;
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
        if (searchPlatform == null || searchPlatform.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Search Platform tidak boleh kosong !");
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
    
    public void openWS2() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs2').show()");
    }
    
    public void deleteWS2() {
        if (incident != null) {
            driftCalcWorksheet2.setWorksheetID(LastWorksheetID);
            driftCalcWorksheet2.setIncident(incident);
            driftCalcWorksheet2.setDeleted(true);
            driftCalcWorksheet2Repo.save(driftCalcWorksheet2);
            newWS2();
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Worksheet berhasil di hapus !");
        }
    }
    
    public void pilihWS2() {
        driftCalcWorksheet2 = driftCalcWorksheet2Repo.findByworksheetID(worksheetID);
        
        if (driftCalcWorksheet2 != null) {
            isNew2 = false;
            asset = resAssetRepo.findAllByAssetid(driftCalcWorksheet2.getAsset().getAssetid());
            rescuer = asset.getAssetid();
            listSearchObject = indexSweepWidthRepo.findSearchObject("FIXED WING");
            for (int x = 0; x < listSearchObject.size(); x++) {
                if (listSearchObject.get(x).equals(driftCalcWorksheet2.getSearchObject())) {
                    searchObject = driftCalcWorksheet2.getSearchObject();
                }
            }
            
            listHeight = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get("Fixed Wing");
            for (int x = 0; x < listHeight.length; x++) {
                if (listHeight[x].doubleValue() == driftCalcWorksheet2.getSearchHeight()) {
                    searchHeight = driftCalcWorksheet2.getSearchHeight();
                }
            }
            repeat = driftCalcWorksheet2.getSearchRepeat();
            unit = driftCalcWorksheet2.getUnit();
            windSpeed = driftCalcWorksheet2.getWindSpeed();
            searchArea = driftCalcWorksheet2.getSearchArea();
            //fatigueFactor = driftCalcWorksheet2.getFatigueFactor();
            uncorrectedSweepWidth = driftCalcWorksheet2.getUncorrectedSweepWidth();
            practicalTrackSpacing = driftCalcWorksheet2.getPracticalTrackSpacing();
            weatherCorrectionFactor = driftCalcWorksheet2.getWeatherCorrectionFactor();
            speedCorrectionFactor = driftCalcWorksheet2.getSpeedCorrectionFactor();
            fatigueCorrectionFactor = driftCalcWorksheet2.getFatigueCorrectionFactor();
            sweepWidthFactor = driftCalcWorksheet2.getSweepWidthFactor();
            coverageFactor = driftCalcWorksheet2.getCoverageFactor();
            probabilityOfDetection = driftCalcWorksheet2.getProbabilityOfDetection();
            searchHours = driftCalcWorksheet2.getSearchhours();
            searchPlatformTas = driftCalcWorksheet2.getSearchPlatformTas();
            
            listSearchPlatform = ValueMapConstant.RESCUER_TAS_LIST.get("Fixed Wing");
            for (LabelValue labelValue : listSearchPlatform) {
                if (labelValue.getValue().equals(searchPlatformTas)) {
                    if (searchPlatformTas == 150.0) {
                        searchPlatform = "<150.0";
                    } else if (searchPlatformTas == 60.0) {
                        searchPlatform = "<60.0";
                    } else {
                        searchPlatform = driftCalcWorksheet2.getSearchPlatformTas().toString();
                    }
                }
            }
            
            listMeteorological = ValueMapConstant.RESCUER_VISIBILITY_LIST.get("Fixed Wing");
            for (LabelValue labelValue : listMeteorological) {
                if (labelValue.getValue().equals(driftCalcWorksheet2.getMeteorologicalVisibility())) {
                    if (driftCalcWorksheet2.getMeteorologicalVisibility() == 40.0) {
                        meteorological = ">40.0";
                    } else {
                        meteorological = driftCalcWorksheet2.getMeteorologicalVisibility().toString();
                    }
                    
                }
            }
        }
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs2').hide()");
        RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws2");
    }
    
    public void openSaveDialog() {
        //validasi
        if (isCalculated == false) {
            calculateWS2();
        } else if (objectValidasi() == false) {
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs2').show()");
    }
    
    public void simpanWS2() throws CloneNotSupportedException {
        
        DriftCalcWorksheet2 cloneDriftCalcWorksheet2 = (DriftCalcWorksheet2) driftCalcWorksheet2.clone();
        
        asset = resAssetRepo.findAllByAssetid(rescuer);
        driftCalcWorksheet2.setAsset(asset);
        driftCalcWorksheet2.setCoverageFactor(coverageFactor);
        driftCalcWorksheet2.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        driftCalcWorksheet2.setDateCreated(new Date());
        driftCalcWorksheet2.setDeleted(false);
        driftCalcWorksheet2.setFatigueCorrectionFactor(fatigueCorrectionFactor);
        driftCalcWorksheet2.setFatigueFactor(fatigueFactor);
        driftCalcWorksheet2.setIncident(incident);
        driftCalcWorksheet2.setLastModified(null);
        driftCalcWorksheet2.setMeteorologicalVisibility(meteorologicalVisibility);
        driftCalcWorksheet2.setModifiedBy(null);
        driftCalcWorksheet2.setPracticalTrackSpacing(practicalTrackSpacing);
        driftCalcWorksheet2.setProbabilityOfDetection(probabilityOfDetection);
        driftCalcWorksheet2.setSearchArea(searchArea);
        driftCalcWorksheet2.setSearchHeight(searchHeight);
        driftCalcWorksheet2.setSearchObject(searchObject);
        driftCalcWorksheet2.setSearchPlatform("Fixed Wing");
        driftCalcWorksheet2.setSearchPlatformTas(searchPlatformTas);
        driftCalcWorksheet2.setSearchRepeat(repeat);
        driftCalcWorksheet2.setSearchhours(searchHours);
        driftCalcWorksheet2.setSpeedCorrectionFactor(speedCorrectionFactor);
        driftCalcWorksheet2.setSweepWidthFactor(sweepWidthFactor);
        driftCalcWorksheet2.setUncorrectedSweepWidth(uncorrectedSweepWidth);
        driftCalcWorksheet2.setUnit(unit);
        driftCalcWorksheet2.setUserSiteID(ProsiaConstant.SITES);
        driftCalcWorksheet2.setWeatherCorrectionFactor(weatherCorrectionFactor);
        driftCalcWorksheet2.setWindSpeed(windSpeed);
        driftCalcWorksheet2.setWorksheetID(formatclassId());
        driftCalcWorksheet2.setWorksheetName(worksheetName);
        
        if (isNew2 == true) {
            driftCalcWorksheet2.setLastModified(null);
            driftCalcWorksheet2.setDeleted(false);
            driftCalcWorksheet2Repo.save(driftCalcWorksheet2);
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Berhasil di simpan");
        } else {
            cloneDriftCalcWorksheet2.setWorksheetID(LastWorksheetID);
            cloneDriftCalcWorksheet2.setDeleted(true);
            cloneDriftCalcWorksheet2.setLastModified(new Date());
            cloneDriftCalcWorksheet2.setModifiedBy(menuNavMBean.getUserSession().getUsername());
            driftCalcWorksheet2Repo.save(cloneDriftCalcWorksheet2);
            
            driftCalcWorksheet2.setDeleted(false);
            driftCalcWorksheet2.setLastModified(null);
            driftCalcWorksheet2.setModifiedBy(null);
            driftCalcWorksheet2Repo.save(driftCalcWorksheet2);
            
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Berhasil di update");
        }
        
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs2').hide()");
    }
    
    public void batalWS2() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs2').hide()");
    }
    
    public void batalSaveWS2() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs2').hide()");
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
            driftCalcWorksheet2 = driftCalcWorksheet2Repo.findByworksheetID(worksheetID);
            LastWorksheetID = worksheetID;
            worksheetName = driftCalcWorksheet2.getWorksheetName();
            createdBy = driftCalcWorksheet2.getIncident().getCreatedby();
            lastModified = driftCalcWorksheet2.getLastModified();
            if (lastModified == null) {
                lastModified = driftCalcWorksheet2.getDateCreated();
            }
        }
        
    }
    
    public List<SelectItem> getListWorksheetName() {
        
        try {
            incident = incidentRepo.findOne(currentIncidentID);
            if (listItemWorksheet2 == null) {
                listItemWorksheet2 = new ArrayList<>();
                listWorksheet2Model = driftCalcWorksheet2Repo.findAllByIncidentAndDeletedFalse(incident);
                listItemWorksheet2.add(new SelectItem("0", " "));
                
                for (DriftCalcWorksheet2 lst : listWorksheet2Model) {
                    listItemWorksheet2.add(new SelectItem(lst.getWorksheetID(), lst.getWorksheetName()));
                }
                
            }
        } catch (Exception ex) {
            listItemWorksheet2 = null;
        }
        return listItemWorksheet2;
    }
    
    public void changeSRUName() {
        if (rescuer.equals("")) {
            rescuer = null;
        } else {
            listSearchObject = indexSweepWidthRepo.findSearchObject("FIXED WING");
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
            listSearchPlatform = ValueMapConstant.RESCUER_TAS_LIST.get("Fixed Wing");
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
    
    public void changeSearchPlatform() {
        if (searchPlatform.equals("")) {
            searchPlatform = null;
        } else {
            listMeteorological = ValueMapConstant.RESCUER_VISIBILITY_LIST.get("Fixed Wing");
            if (searchPlatform.contains("<150.0")) {
                searchPlatform = "150.0";
            } else if (searchPlatform.contains("<60.0")) {
                searchPlatform = "60.0";
            }
            searchPlatformTas = Double.parseDouble(searchPlatform);
        }
    }
    
    public List<SelectItem> getListSearchPlatform() {
        if (listSearchPlatform != null) {
            listItemSearchPlatform = new ArrayList<>();
            listItemSearchPlatform.add(new SelectItem(""));
            for (LabelValue labelValue : listSearchPlatform) {
                listItemSearchPlatform.add(new SelectItem(labelValue.getLabel()));
            }
            
        }
        return listItemSearchPlatform;
    }
    
    public void changeMeteorogical() {
        if (meteorological.equals("")) {
            meteorological = null;
        } else {
            listHeight = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get("Fixed Wing");
            if (meteorological.contains(">40.0")) {
                meteorological = "40.0";
            }
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
    
    public void calculateWS2() {
        //validasi
        if (objectValidasi() == false) {
            return;
        }
        isCalculated = true;
        //driftCalcWorksheet2 = new DriftCalcWorksheet2();
        uncorrectedSweepWidth = getUncorrectedSweepWidth("FIXED WING", searchObject, searchHeight, meteorologicalVisibility);
        uncorrectedSweepWidth = DecimalUtil.rounding(uncorrectedSweepWidth);
        
        practicalTrackSpacing = validatePracticalTrackSpacing(practicalTrackSpacing, uncorrectedSweepWidth);
        
        weatherCorrectionFactor = getWeatherCorrectionFactor(searchObject, windSpeedInKmph);
        
        speedCorrectionFactor = getSpeedCorrectionFactor("Fixed Wing", searchObject, searchPlatformTas);
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
        searchHours = searchArea / (searchPlatformTas * practicalTrackSpacing);
        searchHours = DecimalUtil.rounding(searchHours);
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
        } else // windSpeedInKmph > 46
        {
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
        if (StatusConstant.RESCUER_HELICOPTER.equals(rescuer)
                || StatusConstant.RESCUER_FIXED_WING.equals(rescuer)) {
            
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
        
        List<DriftCalcWorksheet2> lis = driftCalcWorksheet2Repo.findAllByWorksheetIDContainingAndIncident(ProsiaConstant.SITES, incident);
        //System.out.println("--lis--"+lis);
        if (lis.isEmpty()) {
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            String maxId = driftCalcWorksheet2Repo.findClassByMaxId(ProsiaConstant.SITES);
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
        List<DriftCalcWorksheet2> listWorksheet2SA;
        
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
        
        listWorksheet2SA = driftCalcWorksheet2Repo.findAllByIncidentAndDeletedFalse(incident);
        if (listWorksheet2SA != null) {
            //incAssetList = incidentAssetRepo.findByIncidentAndAsset(incident, asset);
            for (DriftCalcWorksheet2 lst : listWorksheet2SA) {
                incAssetList2 = incidentAssetRepo.findByIncidentAndAssetAndDeletedFalse(lst.getIncident(), lst.getAsset());
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
                        
                        if (latitudeFrom2 != 0.9 && longitudeFrom2 != 0.0) {
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
                            assetSearchAreaBean2.setWorksheet2(lst);
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

//        if (mapMBean.getGenerateValueRequestBean() != null) {
//            if (mapMBean.getGenerateValueRequestBean().getIncident() != null) {
//                if (mapMBean.getGenerateValueRequestBean().getIncident().getIncidentid() != null
//                        || !mapMBean.getGenerateValueRequestBean().getIncident().getIncidentid().isEmpty()) {
//                    addMessage("Informasi", "Tidak bisa menampilkan Task Search Area. karena tidak terdapat Search Area pada Map");
//                    return;
//                }
//            }
//        }
//        try {
//            if (assetSearchAreaBean.getSearchArea() < 0) {
//                addMessage("Informasi", "Tidak bisa menampilkan Task Search Area pada Map nilai luas Search Area terlalu kecil.");
//                return;
//            }
//            doViewParameterDialog(assetSearchAreaBean);
//        } catch (Exception e) {
//            warnMessage("Error", "Proses gagal");
//        }
        doViewParameterDialog(assetSearchAreaBean);
    }
    
    private void doViewParameterDialog(AssetSearchAreaBean asb) {
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
        
        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchArea').show()");
        
    }
    
    public void simpanTaskSearchArea() {
        if (worksheetTaskSearchArea.isEmpty() || worksheetTaskSearchArea.equals(null)) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Worksheet tidak boleh kosong !");
        }
        
        taskSearchAreaControllerUtil = new prosia.basarnas.web.util.TaskSearchAreaController(radiusTaskSearchArea, lng1TaskSearchArea, lng2TaskSearchArea, startLatTaskSearchArea);

        createNewAndAddToNavigator(incidentAsset, luasTaskSearchArea, practicalTrackSpacingTSA, radiusTypeTaskSearchArea, worksheetTaskSearchArea, worksheetIDTaskSearchArea, taskSearchAreaControllerUtil, worksheetResult);
        
        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchArea').hide()");
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
        System.out.println("--tsa.setUnrotateStart--"+tsa.getUnrotateStart());
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
    
    public void openTaskSearchArea() {
        
    }
    
    public void closeTaskSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetTaskSearchArea').hide()");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        //incident = new Incident();
        //currentIncidentID = incidentMBean.getIncident().getIncidentid();
        currentIncidentID = incidentDriftCalculation.constIncidentID();
        
        isNew2 = true;
        incident = incidentRepo.findAllByIncidentid(currentIncidentID);
        driftCalcWorksheet2 = new DriftCalcWorksheet2();
        
        lazyDataModelJPA = new LazyDataModelJPA(driftCalcWorksheet2Repo) {
            @Override
            protected Page getDatas(PageRequest request) {
                return driftCalcWorksheet2Repo.findAll(request);
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
                            && asset.getVehicleType().equalsIgnoreCase("FIXED WING")
                            && asset.isDeleted() == false) {
                        listItemSRU.add(new SelectItem(asset.getAsset().getAssetid(), asset.getName()));
                    }
                }
            }
        }
    }
    
    public static Object getRequestParam(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        Map<String, Object> map = component.getAttributes();
        return map.get(name);
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
    
    public void doPrint() throws CloneNotSupportedException, CloneNotSupportedException {
//        isPrint = true;
        calculateWS2();
        String user = getCurrentUser().getParty().getFullName();
        if (objectValidasi() == false) {
            return;
        }
        String rescuer = driftCalcWorksheet2.getSearchPlatform();
        Double[] eyeHeightArr = ValueMapConstant.RESCUER_EYE_HEIGHT_ARR.get(rescuer);
        DriftCalcWorksheet2[] worksheet2s = new DriftCalcWorksheet2[eyeHeightArr.length];
        DriftCalcWorksheet2 worksheet2 = null;
        int indexSelected = 0;
        
        for (int i = 0; i < eyeHeightArr.length; i++) {
            if (driftCalcWorksheet2.getSearchHeight().doubleValue() == eyeHeightArr[i].
                    doubleValue()) {
                worksheet2s[i] = driftCalcWorksheet2;
                indexSelected = i;
            } else {
                worksheet2 = new DriftCalcWorksheet2();
                //                DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
                worksheet2 = (DriftCalcWorksheet2) driftCalcWorksheet2.clone();
                worksheet2.setAsset(asset);
                worksheet2.setCoverageFactor(coverageFactor);
                worksheet2.setCreatedBy(createdBy);
                worksheet2.setDateCreated(dateCreated);
                worksheet2.setDeleted(true);
                worksheet2.setFatigueCorrectionFactor(fatigueCorrectionFactor);
                worksheet2.setFatigueFactor(fatigueFactor);
                worksheet2.setIncident(incident);
                worksheet2.setMeteorologicalVisibility(meteorologicalVisibility);
                worksheet2.setModifiedBy(modifiedBy);
                worksheet2.setPracticalTrackSpacing(practicalTrackSpacing);
                worksheet2.setProbabilityOfDetection(probabilityOfDetection);
                worksheet2.setSearchArea(searchArea);
                worksheet2.setSearchHeight(searchHeight);
                worksheet2.setSearchObject(searchObject);
                worksheet2.setSearchPlatform(searchPlatform);
                worksheet2.setSearchPlatformTas(searchPlatformTas);
                worksheet2.setSearchRepeat(searchObject);
                worksheet2.setSearchhours(searchHours);
                worksheet2.setSpeedCorrectionFactor(speedCorrectionFactor);
                worksheet2.setSweepWidthFactor(sweepWidthFactor);
                worksheet2.setUncorrectedSweepWidth(uncorrectedSweepWidth);
                worksheet2.setUnit(unit);
                worksheet2.setUserSiteID(userSiteID);
                worksheet2.setWeatherCorrectionFactor(weatherCorrectionFactor);
                worksheet2.setWindSpeed(windSpeed);
                worksheet2.setWorksheetID(worksheetID);
                worksheet2.setWorksheetName(worksheetName);
                worksheet2s[i] = worksheet2;
            }
        }
        
        DriftCalcWorksheet2 dwc1 = null;
        DriftCalcWorksheet2 dwc2 = null;
        DriftCalcWorksheet2 dwc3 = null;
        DriftCalcWorksheet2 dwc4 = null;
        
        if (worksheet2s.length >= 1) {
            dwc1 = worksheet2s[0];
        }
        if (worksheet2s.length >= 2) {
            dwc2 = worksheet2s[1];
        }
        if (worksheet2s.length >= 3) {
            dwc3 = worksheet2s[2];
            
        }
        if (worksheet2s.length >= 4) {
            dwc4 = worksheet2s[3];
        }
        
        ReportUtilMBean reportUtilMBean = new ReportUtilMBean();
        reportController.setParams(reportUtilMBean.returnMapWorksheet2Report(dwc1, dwc2, dwc3, dwc4, user));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/tools/open-report.xhtml?"
                + "path=%2Freport%2Freport_jasper%2Fworksheet2.jrxml')");
    }
    
    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
