/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import lombok.Data;
import org.primefaces.context.RequestContext;
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
import prosia.basarnas.constant.LabelValue;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.constant.StatusConstant;
import prosia.basarnas.constant.ValueMapConstant;
import prosia.basarnas.model.DriftCalcWorksheet3;
import prosia.basarnas.model.Incident;
import prosia.basarnas.repo.DriftCalcWorksheet3Repo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IndexSweepWidthLandRepo;
import prosia.basarnas.repo.IndexTerrainRepo;
import prosia.basarnas.web.util.DecimalUtil;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class DriftCalcWorksheet3MBean extends AbstractManagedBean implements InitializingBean {

    private final double DEFAULT_KM = 0.53961;
    private final double PRACTICAL_TRACK_SPACING_MULTIPLE = 0.25;
    private String currentIncidentID;
    private String worksheetID;
    private Incident incident;
    private String searchObject;
    private String worksheetName;
    private double meteorologicalVisibility;
    private double searchHeight;
    private String searchRepeat;
    private String vegetation;
    private boolean fatigueFactor;
    private double uncorrectedSweepWidth;
    private double terrainCorrectionFactor;
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
    private String user;
    private double unit;
    private DriftCalcWorksheet3 driftCalcWorksheet3;
    private String meteorological;
    private String repeat;
    private boolean isPrint;

    private List<String> listSearchObject;
    private List<SelectItem> listItemSearchObject;
    private List<LabelValue> listMeteorological;
    private List<SelectItem> listItemMeteorological;
    private Double[] listHeight;
    private List<SelectItem> listItemHeight;
    private List<String> listRepeat;
    private List<SelectItem> listItemRepeat;
    private List<LabelValue> listVegetation;
    private List<SelectItem> listItemVegetation;
    private List<SelectItem> listItemWorksheet3;
    private List<String> listWorksheet3;
    private List<DriftCalcWorksheet3> listWorksheet3Model;
    private LazyDataModelJPA<DriftCalcWorksheet3> lazyDataModelJPA;
    private List<Double> listWidth;
    private List<Double> listTerrain;
    private boolean isNew3 = false;
    private boolean isCalculated = false;
    private String LastWorksheetID;

    @Autowired
    private ReportController reportController;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private DriftCalcWorksheet3Repo driftCalcWorksheet3Repo;

    @Autowired
    private IndexSweepWidthLandRepo indexSweepWidthLandRepo;

    @Autowired
    private IndexTerrainRepo indexTerrainRepo;
    /*
    @Autowired
    private IncidentMBean incidentMBean;
     */
    @Autowired
    private IncidentDriftCalculation incidentDriftCalculation;

    public void newWS3() {
        isNew3 = true;
        worksheetID = null;
        worksheetName = null;
        user = null;
        clearData();
    }

    public void openWS3() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs3').show()");
    }

    public void deleteWS3() {
        if (incident != null) {
            driftCalcWorksheet3.setWorksheetID(LastWorksheetID);
            driftCalcWorksheet3.setIncident(incident);
            driftCalcWorksheet3.setDeleted(true);
            driftCalcWorksheet3Repo.save(driftCalcWorksheet3);
            newWS3();
            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Sukses", "Worksheet berhasil di hapus !");
        }
    }

    public void pilihWS3() {
        driftCalcWorksheet3 = driftCalcWorksheet3Repo.findByworksheetID(worksheetID);
        if (driftCalcWorksheet3 != null) {
            isNew3 = false;
            searchObject = driftCalcWorksheet3.getSearchObject();
            unit = driftCalcWorksheet3.getUnit();
            meteorological = driftCalcWorksheet3.getMeteorologicalVisibility().toString();
            vegetation = driftCalcWorksheet3.getVegetation();
            searchHeight = driftCalcWorksheet3.getSearchHeight();
            searchArea = driftCalcWorksheet3.getSearchArea();
            repeat = driftCalcWorksheet3.getSearchRepeat();
            //fatigueFactor = driftCalcWorksheet3.getFatigueFactor();
            uncorrectedSweepWidth = driftCalcWorksheet3.getUncorrectedSweepWidth();
            practicalTrackSpacing = driftCalcWorksheet3.getPracticalTrackSpacing();
            terrainCorrectionFactor = driftCalcWorksheet3.getTerrainCorrectionFactor();
            fatigueCorrectionFactor = driftCalcWorksheet3.getFatigueCorrectionFactor();
            sweepWidthFactor = driftCalcWorksheet3.getSweepWidthFactor();
            coverageFactor = driftCalcWorksheet3.getCoverageFactor();
            probabilityOfDetection = driftCalcWorksheet3.getProbabilityOfDetection();
            searchhours = driftCalcWorksheet3.getSearchhours();
        }
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs3').hide()");
        RequestContext.getCurrentInstance().update("incidentdetail:tabplanning-content:form-ws3");
    }

    private void clearOpenDialog() {
        worksheetID = null;
        createdBy = null;
        lastModified = null;
    }

    public void openSaveDialog() {
        //validasi
        if (isCalculated == false) {
            calculateWS3();
        } else {
            if (objectValidasi() == false) {
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs3').show()");
    }

    public void simpanWS3() throws CloneNotSupportedException {

        DriftCalcWorksheet3 cloneDriftCalcWorksheet3 = (DriftCalcWorksheet3) driftCalcWorksheet3.clone();

        driftCalcWorksheet3.setCoverageFactor(coverageFactor);
        driftCalcWorksheet3.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        driftCalcWorksheet3.setDateCreated(new Date());
        driftCalcWorksheet3.setDeleted(false);
        driftCalcWorksheet3.setFatigueCorrectionFactor(fatigueCorrectionFactor);
        driftCalcWorksheet3.setFatigueFactor(fatigueFactor);
        driftCalcWorksheet3.setIncident(incident);
        driftCalcWorksheet3.setLastModified(null);
        driftCalcWorksheet3.setMeteorologicalVisibility(meteorologicalVisibility);
        driftCalcWorksheet3.setModifiedBy(null);
        driftCalcWorksheet3.setPracticalTrackSpacing(practicalTrackSpacing);
        driftCalcWorksheet3.setProbabilityOfDetection(probabilityOfDetection);
        driftCalcWorksheet3.setSearchArea(searchArea);
        driftCalcWorksheet3.setSearchHeight(searchHeight);
        driftCalcWorksheet3.setSearchObject(searchObject);
        driftCalcWorksheet3.setSearchRepeat(repeat);
        driftCalcWorksheet3.setSearchhours(searchhours);
        driftCalcWorksheet3.setSweepWidthFactor(sweepWidthFactor);
        driftCalcWorksheet3.setTerrainCorrectionFactor(terrainCorrectionFactor);
        driftCalcWorksheet3.setUncorrectedSweepWidth(uncorrectedSweepWidth);
        driftCalcWorksheet3.setUnit(unit);
        driftCalcWorksheet3.setUserSiteID(ProsiaConstant.SITES);
        driftCalcWorksheet3.setVegetation(vegetation);
        driftCalcWorksheet3.setWorksheetID(formatclassId());
        driftCalcWorksheet3.setWorksheetName(worksheetName);
        if (isNew3 == true) {
            driftCalcWorksheet3.setLastModified(null);
            driftCalcWorksheet3.setDeleted(false);
            driftCalcWorksheet3Repo.save(driftCalcWorksheet3);
            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Sukses", "Berhasil di simpan");
        } else {
            cloneDriftCalcWorksheet3.setWorksheetID(LastWorksheetID);
            cloneDriftCalcWorksheet3.setDeleted(true);
            cloneDriftCalcWorksheet3.setLastModified(new Date());
            cloneDriftCalcWorksheet3.setModifiedBy(menuNavMBean.getUserSession().getUsername());
            driftCalcWorksheet3Repo.save(cloneDriftCalcWorksheet3);

            driftCalcWorksheet3.setDeleted(false);
            driftCalcWorksheet3.setLastModified(null);
            driftCalcWorksheet3.setModifiedBy(null);
            driftCalcWorksheet3Repo.save(driftCalcWorksheet3);

            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Sukses", "Berhasil di update");
        }

        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs3').hide()");
    }

    public void batalWS3() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetOpenWs3').hide()");
    }

    public void batalSaveWS3() {
        clearOpenDialog();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveWs3').hide()");
    }
    
    public void calculateWS3() {
        //validasi
        if (objectValidasi() == false) {
            return;
        }
        isCalculated = true;
        //driftCalcWorksheet3 = new DriftCalcWorksheet3();

        uncorrectedSweepWidth = this.getUncorrectedSweepWidthLand(searchObject,
                searchHeight, meteorologicalVisibility);
        uncorrectedSweepWidth = DecimalUtil.rounding(uncorrectedSweepWidth);

        practicalTrackSpacing = this.validatePracticalTrackSpacing(
                practicalTrackSpacing, uncorrectedSweepWidth);

        terrainCorrectionFactor = this.getTerrainCorrectionFactor(
                searchObject, vegetation);
        terrainCorrectionFactor = DecimalUtil.rounding(terrainCorrectionFactor);

        fatigueCorrectionFactor = getFatigueCorrectionFactor(
                fatigueFactor);
        fatigueCorrectionFactor = DecimalUtil.rounding(fatigueCorrectionFactor);

        sweepWidthFactor = uncorrectedSweepWidth * terrainCorrectionFactor
                * fatigueCorrectionFactor;
        sweepWidthFactor = DecimalUtil.rounding(sweepWidthFactor);

        coverageFactor = sweepWidthFactor / practicalTrackSpacing;
        coverageFactor = DecimalUtil.rounding(coverageFactor);

        probabilityOfDetection = this.getProbabilityOfDetection(
                repeat, coverageFactor);
        probabilityOfDetection = DecimalUtil.rounding(probabilityOfDetection);

        if (probabilityOfDetection < 60) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Informasi", "Nilai Probability Of Detection lebih kecil dari 60 %");
        }

        searchhours = searchArea / (120 * practicalTrackSpacing);
        searchhours = DecimalUtil.rounding(searchhours);

        //-----penampakan----
        if (unit != 0) {
            if (isPrint == false) {
                uncorrectedSweepWidth = DecimalUtil.rounding(uncorrectedSweepWidth / DEFAULT_KM);
                practicalTrackSpacing = DecimalUtil.rounding(practicalTrackSpacing / DEFAULT_KM);
            }
        }
    }

    private double getUncorrectedSweepWidthLand(String searchObject, double searchHeight,
            double meteorologicalVisibility) {
        double result = 0.0;
        listWidth = indexSweepWidthLandRepo.findWidth(searchObject, searchHeight, meteorologicalVisibility);
        if (listWidth.size() > 0) {
            result = listWidth.get(0);
        }
        return result;
    }

    private double validatePracticalTrackSpacing(
            double currentPracticalTrackSpacing, double uncorrectedSweepWidth) {
        double result = 0;

        if (currentPracticalTrackSpacing == 0) {
            result = DecimalUtil.roundingUpPerMultiple(uncorrectedSweepWidth,
                    PRACTICAL_TRACK_SPACING_MULTIPLE);
        } else {
            result = currentPracticalTrackSpacing;
        }

        if (result == 0.0) {
            result = PRACTICAL_TRACK_SPACING_MULTIPLE;
        }

        return result;
    }

    private double getTerrainCorrectionFactor(String searchObject, String vegetation) {
        double result = 0;
        listTerrain = indexTerrainRepo.findCorrection(searchObject, vegetation);
        if (listTerrain.size() > 0) {
            result = listTerrain.get(0);
        }
        return result;
    }

    private double getFatigueCorrectionFactor(boolean fatigueFactor) {
        double result = fatigueFactor ? 0.9 : 1.0;
        return result;
    }

    private double getProbabilityOfDetection(String searchRepeat,
            double coverageFactor) {

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

    private void clearData() {
        searchObject = null;
        meteorological = null;
        meteorologicalVisibility = 0;
        searchHeight = 0;
        searchRepeat = null;
        repeat = "First";
        vegetation = null;
        fatigueFactor = false;
        uncorrectedSweepWidth = 0;
        terrainCorrectionFactor = 0;
        fatigueCorrectionFactor = 0;
        sweepWidthFactor = 0;
        practicalTrackSpacing = 0;
        coverageFactor = 0;
        probabilityOfDetection = 0;
        searchArea = 0;
        searchhours = 0;
    }

    private boolean objectValidasi() {
        boolean result = true;

        if (searchObject == null || searchObject.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Search Object tidak boleh kosong !");
            result = false;
        }

        if (vegetation == null || vegetation.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Vegetation tidak boleh kosong !");
            result = false;
        }
        if (meteorological == null || meteorological.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Meteorological tidak boleh kosong !");
            result = false;
        }
        if (searchArea == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Search Area tidak boleh kosong !");
            result = false;
        }
        if (searchHeight == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Search Height tidak boleh kosong !");
            result = false;
        }
        if (practicalTrackSpacing == 0.0) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,"Warning", "Practical TrackSpacing tidak boleh kosong !");
            result = false;
        }
        return result;
    }

    public void changeWorksheetName() {
        if (worksheetID.equals("")) {
            worksheetID = null;
        } else {
            driftCalcWorksheet3 = driftCalcWorksheet3Repo.findByworksheetID(worksheetID);
            LastWorksheetID = worksheetID;
            worksheetName = driftCalcWorksheet3.getWorksheetName();
            createdBy = driftCalcWorksheet3.getIncident().getCreatedby();
            lastModified = driftCalcWorksheet3.getLastModified();
            if (lastModified == null) {
                lastModified = driftCalcWorksheet3.getDateCreated();
            }
        }

    }

    public List<SelectItem> getListWorksheetName() {

        try {
            incident = incidentRepo.findOne(currentIncidentID);
            if (listItemWorksheet3 == null) {
                listItemWorksheet3 = new ArrayList<>();
                listWorksheet3Model = driftCalcWorksheet3Repo.findAllByIncidentAndDeletedFalse(incident);
                listItemWorksheet3.add(new SelectItem("0", " "));

                for (DriftCalcWorksheet3 lst : listWorksheet3Model) {
                    listItemWorksheet3.add(new SelectItem(lst.getWorksheetID(), lst.getWorksheetName()));
                }

            }
        } catch (Exception ex) {
            listItemWorksheet3 = null;
        }
        return listItemWorksheet3;
    }

    public void changeSearchObject() {
        if (searchObject.equals("")) {
            searchObject = null;
        } else {

        }
        meteorological = null;
        vegetation = null;
        searchHeight = 0;
    }

    public List<SelectItem> getListSearchObject() {
        if (listSearchObject == null) {
            listItemSearchObject = new ArrayList<>();
            listSearchObject = ValueMapConstant.SEARCH_OBJ_OVER_LAND;
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
            meteorologicalVisibility = Double.parseDouble(meteorological);
        }
    }

    public List<SelectItem> getListMeteorogical() {
        if (listMeteorological == null) {
            listItemMeteorological = new ArrayList<>();
            listMeteorological = ValueMapConstant.SEARCH_OBJ_OVER_LAND_VISIBILITY;
            listItemMeteorological.add(new SelectItem(""));
            for (LabelValue labelValue : listMeteorological) {
                listItemMeteorological.add(new SelectItem(labelValue.getLabel()));
            }
        }
        return listItemMeteorological;
    }

    public List<SelectItem> getListHeight() {
        if (searchObject != null) {
            listHeight = ValueMapConstant.SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.get(searchObject);
            if (listHeight != null) {
                if (listHeight.length > 0) {
                    listItemHeight = new ArrayList<>();
                    listItemHeight.add(new SelectItem(""));
                    for (int x = 0; x < listHeight.length; x++) {
                        listItemHeight.add(new SelectItem(listHeight[x]));
                    }
                }
            }
            return listItemHeight;
        } else {
            return null;
        }
    }

    public List<SelectItem> getListVegetation() {
        if (listVegetation == null) {
            listItemVegetation = new ArrayList<>();
            listVegetation = ValueMapConstant.SEARCH_OBJ_OVER_LAND_VEGETATION;
            listItemVegetation.add(new SelectItem(""));
            for (LabelValue labelValue : listVegetation) {
                listItemVegetation.add(new SelectItem(labelValue.getValue(), labelValue.getLabel()));
            }
        }
        return listItemVegetation;
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<DriftCalcWorksheet3> lis = driftCalcWorksheet3Repo.findAllByWorksheetIDContainingAndIncident(ProsiaConstant.SITES, incident);
        //System.out.println("--lis--"+lis);
        if (lis.isEmpty()) {
            System.out.println("A");
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            System.out.println("B");
            String maxId = driftCalcWorksheet3Repo.findClassByMaxId(ProsiaConstant.SITES);
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
        //currentIncidentID = incidentMBean.getIncident().getIncidentid();
        currentIncidentID = incidentDriftCalculation.constIncidentID();
        incident = incidentRepo.findAllByIncidentid(currentIncidentID);
        System.out.println("--incident--" + incident);
        driftCalcWorksheet3 = new DriftCalcWorksheet3();
        isNew3 = true;
        lazyDataModelJPA = new LazyDataModelJPA(driftCalcWorksheet3Repo) {
            @Override
            protected Page getDatas(PageRequest request) {
                return driftCalcWorksheet3Repo.findAll(request);
            }
        };
    }

    public void doPrint() throws CloneNotSupportedException, CloneNotSupportedException {
        calculateWS3();
        String user = getCurrentUser().getParty().getFullName();

        if (objectValidasi() == false) {
            return;
        }
        String SearchObject = driftCalcWorksheet3.getSearchObject();
        Double[] eyeHeightArr = ValueMapConstant.SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.get(searchObject);
        DriftCalcWorksheet3[] worksheet3s = new DriftCalcWorksheet3[eyeHeightArr.length];
        DriftCalcWorksheet3 worksheet3 = null;
        int indexSelected = 0;

        for (int i = 0; i < eyeHeightArr.length; i++) {
            if (driftCalcWorksheet3.getSearchHeight().doubleValue() == eyeHeightArr[i].
                    doubleValue()) {
                worksheet3s[i] = driftCalcWorksheet3;
                indexSelected = i;
            } else {
                worksheet3 = new DriftCalcWorksheet3();
                worksheet3 = (DriftCalcWorksheet3) driftCalcWorksheet3.clone();
                worksheet3.setSearchHeight(eyeHeightArr[i]);
                worksheet3.setSearchObject(searchObject);
                worksheet3.setMeteorologicalVisibility(meteorologicalVisibility);
                worksheet3.setSearchHeight(searchHeight);
                worksheet3.setSearchRepeat(searchRepeat);
                worksheet3.setVegetation(vegetation);
                worksheet3.setFatigueFactor(fatigueFactor);
                worksheet3.setUncorrectedSweepWidth(uncorrectedSweepWidth);
                worksheet3.setTerrainCorrectionFactor(terrainCorrectionFactor);
                worksheet3.setFatigueCorrectionFactor(fatigueCorrectionFactor);
                worksheet3.setSweepWidthFactor(sweepWidthFactor);
                worksheet3.setPracticalTrackSpacing(practicalTrackSpacing);
                worksheet3.setCoverageFactor(coverageFactor);
                worksheet3.setProbabilityOfDetection(probabilityOfDetection);
                worksheet3.setSearchArea(searchArea);
                worksheet3.setSearchhours(searchhours);
                worksheet3.setUnit(unit);
                worksheet3s[i] = worksheet3;
            }
        }

        // TODO sent all worksheet to jasper report
        DriftCalcWorksheet3 dwc1 = null;
        DriftCalcWorksheet3 dwc2 = null;
        DriftCalcWorksheet3 dwc3 = null;
        DriftCalcWorksheet3 dwc4 = null;

        if (worksheet3s.length >= 1) {
            dwc1 = worksheet3s[0];
            System.out.println("DCW1 : " + dwc1.getIncident().toString());
        }
        if (worksheet3s.length >= 2) {
            dwc2 = worksheet3s[1];
        }
        if (worksheet3s.length >= 3) {
            dwc3 = worksheet3s[2];
        }
        if (worksheet3s.length >= 4) {
            dwc4 = worksheet3s[3];
        }
        ReportUtilMBean reportUtilMBean = new ReportUtilMBean();
        reportController.setParams(reportUtilMBean.returnMapWorksheet3Report(dwc1, dwc2, dwc3, dwc4, user));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/tools/open-report.xhtml?"
                + "path=%2Freport%2Freport_jasper%2Fworksheet3.jrxml')");
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
