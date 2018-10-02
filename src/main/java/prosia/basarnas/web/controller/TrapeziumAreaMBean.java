/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.LatLng;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.TrapeziumArea;
import prosia.basarnas.model.TrapeziumTaskArea;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.TrapeziumAreaRepo;
import prosia.basarnas.repo.TrapeziumTaskAreaRepo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.DriftCalculationTableList;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.service.TrapeziumSearchAreaUtil;
import prosia.basarnas.service.TrapeziumTaskAreaUtil;
import prosia.basarnas.service.TrapeziumTaskAreaUtil.TaskArea;
import prosia.basarnas.service.map_js.GPoly;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.model.map.TaskSearchArea;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
@Data
public class TrapeziumAreaMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private IncidentPlanningMBean incidentPlanningMBean;
    //<editor-fold defaultstate="collapsed" desc="Declarasi Variable">
    //dipakai di xhtml
    private String deskripsi;
    private Date waktuoperasi;
    private TimeZone waktuOperasiTimezone;
    private String incTime;
    private double unit;
    private double latLkp;
    private double longLkp;
    private double latDest;
    private double longDest;
    private double safetyFactor;
    private double distressError;
    private double searchError;
    private double luasArea;
    private double luasAreainTask;
    private double distressCraftError;
    private double searchCraftError;
    private double kepala;
    private double lebar;

    private double lebarTotalTaskArea;
    private String panjangTaskArea;
    private List<String> listPanjangTaskArea;
    private List<SelectItem> listItemPanjangTaskArea;
//    private List<Double> listWaypoint;
    private List<TrapeziumArea> listWaypointModel;
    private List<SelectItem> listItemWaypoint;
    private double lebarTaskArea;
    private double lebarSearchArea;
    private double luasTaskArea;
    private String wayPoint;

    //end
    //tambahan aris
    private SimpleDateTimeZoneFormat sdfDtz;
    private String trapeziumAreaID;
    private String lastTrapeziumAreaID;
    private String parentID;
    private String worksheetName;
    private String createdBy;
    private Date lastModified;
    private LazyDataModelJPA<TrapeziumArea> model;
    private List<TrapeziumTaskArea> listTrapeziumTaskAreaModel;
    private List<TrapeziumArea> listTrapeziumArea;
    private List<SelectItem> listItemTrapeziumArea;
    private List<SelectItem> listItemTrapeziumTaskArea;
    private List<String> listTrapezium;
    private String numWayPoint;
    private String labelMap[] = {"Clear Map", "View in Map"};
    private String viewLabelMap;
    private String currentIncidentID;
//    private boolean isNewDataRow;
    private boolean isYesAnswer;
    //end
    private Incident incident;
    private double[] arrlatLkp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrlongLkp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrlatDest = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrlongDest = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrsafetyFactor = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrdistressCraftError = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrsearchCraftError = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrsmallRadius = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String warning = "";
    private TrapeziumSearchAreaUtil trapezUtil;
    private TrapeziumTaskAreaUtil taskUtil;
    private TrapeziumArea trapeziumArea;
    private List<TrapeziumTaskArea> listTrapeziumTaskArea;
    private TrapeziumTaskArea trapeziumTaskArea;
    //private Table table;
    //private DefaultTableModel model;
    private HashMap<String, TrapeziumTaskArea> trapeziumTaskAreaHashMap = new HashMap<>();
    private final double DEFAULT_KM = 0.53961;
    private DriftCalculationTableList driftCalculationTableList;
    private int sizeTableList = 0;
    private boolean isInsert;
    private int iCursor;
    private int Position;
    private int LastPosition = -1;
    private boolean isLoadForm;
    private boolean isNewForm;
    private boolean hasCalculate;
    private boolean hasPrev;
    private boolean isViewMap;
    private boolean isViewMapPlanTrapezium;
    private boolean isClearMap;
    private int[] intViewMapPosition = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private LatLng[] smallRoundPivot = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private LatLng[] largeRoundPivot = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private double trackSpacing = 1;
    private double[] max = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] min = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double smallTrapezAreaLeg;
    private double[] width = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] heading = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] smallRoundRadiusInNm = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] largeRoundRadiusInNm = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private LatLng[] startLatLng = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private LatLng[] lkpAsPivot = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private String[] parrentID = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
//    GTrapeziumSearchArea currentTrapeziumSearchArea;
    private double[] latGetBigCoord1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] longGetBigCoord1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] latGetBigCoord2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] longGetBigCoord2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] latGetSmallCoord1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] longGetSmallCoord1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] latGetSmallCoord2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] longGetSmallCoord2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private List<LatLng> l;
    private String tempParentID;
    private int LastMapPosition;
    private double curLatDest = 0;
    private double curLongDest = 0;
    private Coordinate coordinateLat1;
    private Coordinate coordinateLong1;
    private Coordinate coordinateLat2;
    private Coordinate coordinateLong2;
    public static double[] statheading = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static double[] statwidth = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static double[] statmin = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static double[] statmax = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static LatLng[] statlatlong = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
//    private boolean isVoidMap;
    private double[] arrCboHeading = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrCboWidth = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] arrCboLuas = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Autowired">
    @Autowired
    private MapMBean mapMBean;

    @Autowired
    private TrapeziumAreaRepo trapeziumAreaRepo;

    @Autowired
    private TrapeziumTaskAreaRepo trapeziumTaskAreaRepo;

    @Autowired
    private DriftCalcTrapeziumMBean driftCalcTrapeziumMBean;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;
//</editor-fold>

    private TrapeziumAreaMBean() {
        initCoordinate1();
        initCoordinate2();
        isViewMapPlanTrapezium = false;
    }

    //<editor-fold defaultstate="collapsed" desc="Button Handler">
    public void btnBatalTaskArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetSaveTaskArea').show()");
    }

    public void btnHitungSearchArea() {
        hitungSearchArea();
    }

    public void btnHitungTaskArea() {
        calculateTaskArea();
    }

    public void changeWayPoint() {
        int tempWaypoint;
        if (wayPoint.equals("")) {
            wayPoint = null;
        } else {
            tempWaypoint = Integer.parseInt(wayPoint) - 1;
            luasAreainTask = arrCboHeading[tempWaypoint];
            lebarSearchArea = arrCboWidth[tempWaypoint];
            lebarTotalTaskArea = arrCboLuas[tempWaypoint];
        }
    }

    public List<SelectItem> getListWayPoint() {
        if (listWaypointModel == null || listWaypointModel.isEmpty()) {
            listItemWaypoint = new ArrayList<>();
            listItemWaypoint.add(new SelectItem(""));
            listWaypointModel = trapeziumAreaRepo.findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(incident, parentID);
            for (TrapeziumArea lst : listWaypointModel) {
                listItemWaypoint.add(new SelectItem(lst.getWaypoint().intValue()));
            }
        }
        return listItemWaypoint;
    }

    public void btnSimpanSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetSaveSearchAreaTrapesium').show()");
    }

    public void simpanSearchArea() {
        saveFormSearchArea();
        saveSearchAreaToTaskArea();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveSearchAreaTrapesium').hide()");
    }

    public void batalSimpanSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetSaveSearchAreaTrapesium').hide()");
    }

    public void btnSimpanTaskArea() {
        if (trapeziumArea != null) {
            RequestContext.getCurrentInstance().execute("PF('widgetSaveTaskArea').show()");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Simpan perhitungan trapesium area Anda terlebih dahulu");
        }
    }

    public void simpanTaskArea() {
        saveFormTaskArea();
        RequestContext.getCurrentInstance().execute("PF('widgetSaveTaskArea').hide()");
    }

    public void loadSearchArea() {
        isLoadForm = true;
        isClearMap = false;
        for (int i = 0; i < 20; i++) {
            intViewMapPosition[i] = 0;
        }
        loadDataSearchArea();

        RequestContext.getCurrentInstance().execute("PF('widgetOpenSearchAreaTrapesium').hide()");
    }

    public void btnCloseSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenSearchAreaTrapesium').hide()");
    }

    public void btnLoadSearchArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenSearchAreaTrapesium').show()");
    }

    public void btnLoadTaskArea() {
        RequestContext.getCurrentInstance().execute("PF('widgetOpenTaskArea').show()");
    }

    public void loadTaskArea() {
        if (trapeziumArea != null) {
            loadFormTaskArea();
        }
    }

    public void btnNext() {
        beforeSearchAreaNext();
    }

    public void btnPrev() {
        SearchAreaPrev();
    }

    public void btnMapSearchArea() {
        if (isClearMap == false) {
            hitungSearchArea();
            showSearchAreaOnMapArr();
        } else {
            clearSearchAreaOnMapArr();
        }
    }

    public void viewMapPlanTrapezium() {
        try{
        this.isViewMapPlanTrapezium = (Boolean) getRequestParam("isViewMapPlanTrapezium");
        viewPlanTrapezium((String) getRequestParam("trapezId"));
        btnMapSearchArea();
        }catch(Exception e){
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Gagal", "View Peta Trapezium Bermasalah");
        }

    }

    public void btnMapTaskArea(TrapeziumTaskArea area) {
        System.out.println(area.toString());
        try {
//        GTaskSearchArea.interfaceCreateTaskAreaComeFromTrapezium(area);
            List<TaskSearchArea> taskAreas = mapMBean.getListTaskSearchArea();
            if (!taskAreas.isEmpty()) {
                for (TaskSearchArea tsa : taskAreas) {
                    if (tsa.getTypeTaskArea().equals(MapCalculationType.DRIFT_CALCULATION)) {
                        mapMBean.getListTaskSearchArea().remove(tsa);
                    }
                }
            }
            TaskSearchArea tsa = new TaskSearchArea();
            List<LatLng> ll = new ArrayList<>();
            ll.add(new LatLng(
                    Double.valueOf(area.getSmallCoord1().split(",")[0].replace("Lat:", "").trim()),
                    Double.valueOf(area.getSmallCoord1().split(",")[1].replace("Lng:", "").trim())));
            ll.add(new LatLng(
                    Double.valueOf(area.getSmallCoord2().split(",")[0].replace("Lat:", "").trim()),
                    Double.valueOf(area.getSmallCoord2().split(",")[1].replace("Lng:", "").trim())));
            ll.add(new LatLng(
                    Double.valueOf(area.getBigCoord2().split(",")[0].replace("Lat:", "").trim()),
                    Double.valueOf(area.getBigCoord2().split(",")[1].replace("Lng:", "").trim())));
            ll.add(new LatLng(
                    Double.valueOf(area.getBigCoord1().split(",")[0].replace("Lat:", "").trim()),
                    Double.valueOf(area.getBigCoord1().split(",")[1].replace("Lng:", "").trim())));
//        ll.add(area.getSmallCoord2());
//        ll.add(area.getBigCoord2());
//        ll.add(area.getBigCoord1());
            tsa.setVertexs(ll);
            tsa.setUnrotateStart(new LatLng(
                    Double.valueOf(area.getSmallCoord1().split(",")[0].replace("Lat:", "").trim()),
                    Double.valueOf(area.getSmallCoord1().split(",")[1].replace("Lng:", "").trim())));

            LatLng pivot = prosia.basarnas.web.util.map.MapCalculation.getPivotFromMuchCoodninate(ll);
            tsa.setUnrotatePivot(pivot);
////        tsa.setIncidentAsset(area.get);
            tsa.setHeight(area.getTaskAreaLength());
            tsa.setWidth(area.getLuasTaskArea() / tsa.getHeight());
            tsa.setTiltDrift(heading[Position]);
            tsa.setPivotDatumPoint(pivot);
            tsa.setParrentID(parrentID[Position]);
//        tsa.setChildID(childID);
            tsa.setTrackSpacing(trackSpacing);
//        tsa.setRadiusType(area.getr);
            tsa.setName(area.getName());
            tsa.setTypeTaskArea(MapCalculationType.TRAPEZIUM);
            mapMBean.setIncident(incident);
            mapMBean.getListTaskSearchArea().add(tsa);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("window.open('/map/map/list.xhtml?search=TRAPEZIUM"
                    + "&incident="
                    + incident.getIncidentid() + "', "
                    + "'_newtab')");
        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Konfirmasi",
                    "GIS tidak dapat ditampilkan");
        }
    }

    public void btnHapusSearchArea() {
        if (trapeziumArea != null) {
            deleteSearchArea(trapeziumArea);
            trapeziumArea = null;
            clearFormSearchArea();
        }
    }

    public void btnHapusTaskArea() {
        if (listTrapeziumTaskArea != null) {
            deleteTaskArea(trapeziumTaskArea);
            trapeziumTaskArea = null;
            clearFormTaskArea();
        }
    }

    public void btnBaruSearchArea() {
        trapeziumArea = null;
        isNewForm = true;
        isClearMap = false;
        LastPosition = 0;
        Position = 0;
        hasCalculate = false;
        isInsert = false;
        hasPrev = true;
        isViewMap = false;
        wayPoint = "0";
        for (int i = 0; i < 20; i++) {
            intViewMapPosition[i] = 0;
        }
        clearFormSearchArea();
        destroyObject();
        viewLabelMap = labelMap[1];
        numWayPoint = String.valueOf("1 / 1");
        listTrapeziumTaskArea = null;
        btnBaruTaskArea();
        listItemWaypoint = null;
        luasAreainTask = 0;
        lebarSearchArea = 0;
        lebarTotalTaskArea = 0;
    }

    public void btnBaruTaskArea() {
        trapeziumTaskArea = null;
        clearFormTaskArea();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Search Area Functions">
    private void loadFirstSearchArea() {
        try {
            if (sizeTableList >= 0) {
                for (int i = sizeTableList - 1; i >= 0; i--) {
                    arrlatLkp[i] = Double.valueOf(driftCalculationTableList.getLatlkp()[i]);
                    arrlongLkp[i] = Double.valueOf(driftCalculationTableList.getLonglkp()[i]);

                    arrlatDest[i] = Double.valueOf(driftCalculationTableList.getLatdest()[i]);
                    arrlongDest[i] = Double.valueOf(driftCalculationTableList.getLongdest()[i]);
                    //untuk lolos validasi
                    latLkp = arrlatLkp[0];
                    longLkp = arrlongLkp[0];
                    latDest = arrlatDest[0];
                    longDest = arrlongDest[0];

                    viewCoordinate1(String.valueOf(arrlatLkp[i]), String.valueOf(arrlongLkp[i]));
                    viewCoordinate2(String.valueOf(arrlatDest[i]), String.valueOf(arrlongDest[i]));

                    arrsafetyFactor[i] = Double.valueOf(driftCalculationTableList.getSafetyfactor()[i]);
                    arrdistressCraftError[i] = Double.valueOf(driftCalculationTableList.getDistresserror()[i]);
                    arrsearchCraftError[i] = Double.valueOf(driftCalculationTableList.getSearcherror()[i]);

                    arrsmallRadius[i] = arrsafetyFactor[i] * Math.sqrt(Math.pow(arrsearchCraftError[i], 2)
                            + Math.pow(arrdistressCraftError[i], 2));

                    trapezUtil = new TrapeziumSearchAreaUtil(arrlatDest, arrlongDest, arrlatLkp, arrlongLkp, arrsmallRadius, i, true);
                    //masukkan hasil calculasi ke arrayList
                    kepala = DecimalUtil.rounding(trapezUtil.arrHeading()[i]);
                    arrCboHeading[i] = DecimalUtil.rounding(trapezUtil.arrHeading()[i]);
                    if (unit == 0) {
                        lebar = DecimalUtil.rounding(trapezUtil.arrWidth()[i]);
                        arrCboWidth[i] = DecimalUtil.rounding(trapezUtil.arrWidth()[i]);
                    } else {
                        lebar = DecimalUtil.rounding(trapezUtil.arrWidth()[i] / DEFAULT_KM);
                        arrCboWidth[i] = DecimalUtil.rounding(trapezUtil.arrWidth()[i] / DEFAULT_KM);
                    }

                    if (unit == 0) {
//                    luasArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[i]);
//                    arrCboLuas[i] = DecimalUtil.rounding(trapezUtil.arrLuasArea()[i]);
                        luasArea = DecimalUtil.rounding(Double.valueOf(driftCalculationTableList.getLuasarea()[i]));
                        arrCboLuas[i] = DecimalUtil.rounding(Double.valueOf(driftCalculationTableList.getLuasarea()[i]));
                    } else {
//                    luasArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[i] / DEFAULT_KM);
//                    arrCboLuas[i] = DecimalUtil.rounding(trapezUtil.arrLuasArea()[i] / DEFAULT_KM);
                        luasArea = DecimalUtil.rounding(Double.valueOf(driftCalculationTableList.getLuasarea()[i]) / DEFAULT_KM);
                        arrCboLuas[i] = DecimalUtil.rounding(Double.valueOf(driftCalculationTableList.getLuasarea()[i]) / DEFAULT_KM);
                    }

                    driftCalculationTableList.getCalculateTableList(String.valueOf(kepala), String.valueOf(lebar), String.valueOf(luasArea));

                    unit = Double.parseDouble(driftCalculationTableList.getUnit()[i]);
                    distressCraftError = Double.parseDouble(driftCalculationTableList.getDistresserror()[i]);
                    safetyFactor = Double.parseDouble(driftCalculationTableList.getSafetyfactor()[i]);
                    searchError = Double.parseDouble(driftCalculationTableList.getSearcherror()[i]);
                    taskUtil = new TrapeziumTaskAreaUtil(trapezUtil, i, true);

                }

                //hitung untuk map
                if (isLoadForm == false) {
                    CalculateMapArr();
                }

            }
        } catch (Exception ex) {
            System.out.println("--ex--" + ex.getMessage());
        }

    }

    private boolean validateInputSearchArea() {
        boolean success = true;
        warning = "";

        if (latLkp == 0) {
            warning = "Latitude LKP harus diisi";
            success = false;
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", warning);
        }
        if (longLkp == 0) {
            warning = "Longitude LKP harus diisi";
            success = false;
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", warning);
        }
        if (latDest == 0) {
            warning = "Latitude Tujuan harus diisi";
            success = false;
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", warning);
        }
        if (longDest == 0) {
            warning = "Longitude Tujuan harus diisi";
            success = false;
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", warning);
        }

        return success;
    }

    private void hitungSearchArea() {
        hasCalculate = true;
        if (isLoadForm == true) {
            try {
                curLatDest = Double.valueOf(driftCalculationTableList.getLatdest()[Position]);
                curLongDest = Double.valueOf(driftCalculationTableList.getLongdest()[Position]);
            } catch (Exception ex) {
                curLatDest = latDest;
                curLongDest = longDest;
            }
        } else {
            curLatDest = latDest;
            curLongDest = longDest;
        }

        if (LastPosition == Position || LastPosition >= Position) {
            //update
            //Cek apakah ada perubahan longitude & latitude Destination jika ya update
            isInsert = false;
            UpdateTableList(1);
            UpdateSearchArea();
            driftCalculationTableList.setSize(driftCalculationTableList.getActualSize());

        } else {
            //save current longitude & latitude Destination
            curLatDest = latDest;
            curLongDest = longDest;
            isInsert = true;
            InsertTabelList();
            addSearchArea();
            LastPosition = Position;
            if (isLoadForm == true) {
                isLoadForm = false;
            }
        }

    }

    private void getArrObjectFromFormSearchArea(TrapeziumArea area, String worksheetname) {
        String strParentID = "";
        for (int i = 0; i < sizeTableList; i++) {
            area.setWaktuoperasi(driftCalculationTableList.getWaktuoperasi()[i]);
            area.setDeskripsi(driftCalculationTableList.getDeskripsi()[i]);
            area.setWaktuOperasiTimezone(waktuOperasiTimezone);
            area.setLatLkp(Double.valueOf(driftCalculationTableList.getLatlkp()[i]));
            area.setLongLkp(Double.valueOf(driftCalculationTableList.getLonglkp()[i]));
            area.setLatDest(Double.valueOf(driftCalculationTableList.getLatdest()[i]));
            area.setLongDest(Double.valueOf(driftCalculationTableList.getLongdest()[i]));
            area.setSafetyFactor(Double.valueOf(driftCalculationTableList.getSafetyfactor()[i]));
            area.setDistressError(Double.valueOf(driftCalculationTableList.getDistresserror()[i]));
            area.setSearchError(Double.valueOf(driftCalculationTableList.getSearcherror()[i]));
            if (unit == 0) {
                area.setLuasArea(trapezUtil.arrLuasArea()[i]);

                arrCboHeading[i] = trapezUtil.arrHeading()[i];
                arrCboWidth[i] = trapezUtil.arrWidth()[i];
                arrCboLuas[i] = trapezUtil.arrLuasArea()[i];
            } else {
                area.setLuasArea(trapezUtil.arrLuasArea()[i] / DEFAULT_KM);

                arrCboHeading[i] = trapezUtil.arrHeading()[i] / DEFAULT_KM;
                arrCboWidth[i] = trapezUtil.arrWidth()[i] / DEFAULT_KM;
                arrCboLuas[i] = trapezUtil.arrLuasArea()[i] / DEFAULT_KM;
            }

            area.setUnit(unit);
            area.setIncident(incident);
            area.setUnit(unit);
            area.setDateCreated(new Date());
            area.setTrapeziumAreaID(formatclassId());

            area.setWorksheetName(worksheetname);
            area.setWaypoint(Double.valueOf(i) + 1.0);
            area.setCreatedBy(menuNavMBean.getUserSession().getUsername());
            area.setLastModified(new Date());
            area.setUserSiteID(ProsiaConstant.SITES);
            area.setTotalTaskAreaLength(0.0);

            trapeziumArea = trapeziumAreaRepo.save(area);
            //trapeziumArea = TrapeziumAreaES.insertTrapeziumArea(area);
            if (i == 0) {
                strParentID = trapeziumArea.getTrapeziumAreaID();
            }
            trapeziumArea.setParentID(strParentID);
            trapeziumArea = trapeziumAreaRepo.save(trapeziumArea);
            tempParentID = trapeziumArea.getParentID();
        }
        //return area;
    }

    private void UpdateGetArrObjectFromFormSearchArea(TrapeziumArea area, String worksheetname) {

        List<TrapeziumArea> taList = trapeziumAreaRepo.findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(incident, tempParentID);

        for (TrapeziumArea area1 : taList) {
            area1.setDeleted(true);
            trapeziumAreaRepo.save(area1);
        }
        for (int i = 0; i < sizeTableList; i++) {
            area.setDeskripsi(driftCalculationTableList.getDeskripsi()[i]);
            area.setWaktuoperasi(driftCalculationTableList.getWaktuoperasi()[i]);
            area.setWaktuOperasiTimezone(waktuOperasiTimezone);
            area.setLatLkp(Double.valueOf(driftCalculationTableList.getLatlkp()[i]));
            area.setLongLkp(Double.valueOf(driftCalculationTableList.getLonglkp()[i]));
            area.setLatDest(Double.valueOf(driftCalculationTableList.getLatdest()[i]));
            area.setLongDest(Double.valueOf(driftCalculationTableList.getLongdest()[i]));
            area.setSafetyFactor(Double.valueOf(driftCalculationTableList.getSafetyfactor()[i]));
            area.setDistressError(Double.valueOf(driftCalculationTableList.getDistresserror()[i]));
            area.setSearchError(Double.valueOf(driftCalculationTableList.getSearcherror()[i]));
            if (unit == 0) {
                area.setLuasArea(trapezUtil.arrLuasArea()[i]);
                arrCboHeading[i] = trapezUtil.arrHeading()[i];
                arrCboWidth[i] = trapezUtil.arrWidth()[i];
                arrCboLuas[i] = trapezUtil.arrLuasArea()[i];
            } else {
                area.setLuasArea(trapezUtil.arrLuasArea()[i] / DEFAULT_KM);
                arrCboHeading[i] = trapezUtil.arrHeading()[i] / DEFAULT_KM;
                arrCboWidth[i] = trapezUtil.arrWidth()[i] / DEFAULT_KM;
                arrCboLuas[i] = trapezUtil.arrLuasArea()[i] / DEFAULT_KM;
            }
            area.setUnit(unit);
            area.setIncident(incident);

            area.setWorksheetName(worksheetname);
            area.setWaypoint(Double.valueOf(i) + 1.0);
            area.setCreatedBy(menuNavMBean.getUserSession().getUsername());
            area.setTrapeziumAreaID(formatclassId());
            area.setParentID(driftCalculationTableList.getParentid()[0]);
            area.setUserSiteID(ProsiaConstant.SITES);
            area.setDateCreated(new Date());
            area.setTotalTaskAreaLength(0.0);
            //trapeziumArea = TrapeziumAreaES.insertTrapeziumArea(area);
            trapeziumAreaRepo.save(area);
        }

    }

    private void loadTabelList(Incident incident) {
        System.out.println("incident= " + incident);
        System.out.println("tempParentID= " + tempParentID);
        List<TrapeziumArea> taList = trapeziumAreaRepo.findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(incident, tempParentID);
        String strTotalTaskAreaLength = null;
        for (TrapeziumArea area : taList) {
            if (area.getTotalTaskAreaLength() == null) {
                strTotalTaskAreaLength = "0";
            }
            driftCalculationTableList.setTabelList(area.getTrapeziumAreaID(), area.getDeskripsi(), area.getWaktuoperasi(),
                    area.getLatLkp().toString(), area.getLongLkp().toString(), area.getLatDest().toString(),
                    area.getLongDest().toString(), area.getSafetyFactor().toString(), area.getDistressError().toString(),
                    area.getSearchError().toString(), area.getLuasArea().toString(), area.getIncident().getIncidentid(),
                    area.getDateCreated().toString(), area.getCreatedBy(), String.valueOf(area.isDeleted()),
                    area.getUserSiteID(), area.getWorksheetName(), area.getTaskAreaLastPoint(),
                    strTotalTaskAreaLength, area.getUnit().toString(), area.getWaypoint().toString(),
                    area.getParentID());
            driftCalculationTableList.setSize(++sizeTableList);
            driftCalculationTableList.setActualSize(driftCalculationTableList.getSize());
        }
        sizeTableList = driftCalculationTableList.getSize();
    }

    private void loadTabelListPlanTrapez(Incident incident, String ID) {
        trapeziumArea = trapeziumAreaRepo.findByTrapeziumAreaID(ID);
        System.out.println("tempParentID= " + trapeziumArea.getParentID());
        List<TrapeziumArea> taList = trapeziumAreaRepo.findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(incident, trapeziumArea.getParentID());
        String strTotalTaskAreaLength = null;
        for (TrapeziumArea area : taList) {
            if (area.getTotalTaskAreaLength() == null) {
                strTotalTaskAreaLength = "0";
            }
            driftCalculationTableList.setTabelList(area.getTrapeziumAreaID(), area.getDeskripsi(), area.getWaktuoperasi(),
                    area.getLatLkp().toString(), area.getLongLkp().toString(), area.getLatDest().toString(),
                    area.getLongDest().toString(), area.getSafetyFactor().toString(), area.getDistressError().toString(),
                    area.getSearchError().toString(), area.getLuasArea().toString(), area.getIncident().getIncidentid(),
                    area.getDateCreated().toString(), area.getCreatedBy(), String.valueOf(area.isDeleted()),
                    area.getUserSiteID(), area.getWorksheetName(), area.getTaskAreaLastPoint(),
                    strTotalTaskAreaLength, area.getUnit().toString(), area.getWaypoint().toString(),
                    area.getParentID());
            driftCalculationTableList.setSize(++sizeTableList);
            driftCalculationTableList.setActualSize(driftCalculationTableList.getSize());
        }
        sizeTableList = driftCalculationTableList.getSize();

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Task Area Functions">
    public void changeTaskWorksheetName() {
        if (trapeziumAreaID.equals("")) {
            trapeziumAreaID = null;
        } else {
            trapeziumTaskArea = trapeziumTaskAreaRepo.findByTrapeziumTaskAreaID(trapeziumAreaID);
            lastTrapeziumAreaID = trapeziumAreaID;
            createdBy = trapeziumTaskArea.getCreatedBy();
            lastModified = trapeziumTaskArea.getDateCreated();
        }
    }

    public List<SelectItem> getListTaskWorksheetName() {
        try {
            if (listTrapeziumTaskAreaModel == null) {
                listItemTrapeziumTaskArea = new ArrayList<>();
                listItemTrapeziumTaskArea.add(new SelectItem(""));
                listTrapeziumTaskAreaModel = trapeziumTaskAreaRepo.findByTrapeziumAreaAndWaypoint(trapeziumArea, 1.0);
                for (TrapeziumTaskArea lst : listTrapeziumTaskAreaModel) {
                    listItemTrapeziumTaskArea.add(new SelectItem(lst.getTrapeziumTaskAreaID(), lst.getName()));
                }
            }
        } catch (Exception ex) {
            listTrapeziumTaskAreaModel = null;
        }
        return listItemTrapeziumTaskArea;
    }

    private UUID calculateTaskArea() {
        hasCalculate = true;
        if (validateInputTaskArea()) {
            UUID taskUUID = UUID.randomUUID();
            int tempWaypoint = Integer.parseInt(wayPoint) - 1;
            taskUUID = taskUtil.addTaskArea(lebarTaskArea, tempWaypoint);
            if (taskUUID != null) {
                if (!panjangTaskArea.equals("1")) {
                    taskUtil.splitTaskArea(taskUtil.getTaskAreaHashMap().get(taskUUID));
                }

                if (taskUtil.getLuasTaskArea() != null) {
                    if (unit == 0) {
                        luasTaskArea = taskUtil.getLuasTaskArea();
                    } else {
                        luasTaskArea = DecimalUtil.rounding(taskUtil.getLuasTaskArea() / DEFAULT_KM);
                    }

                }
            }
            return taskUUID;
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", warning);
        }
        return null;
    }

    private boolean validateInputTaskArea() {
        //TODO validate panjang task area total tidak boleh melebihi panjang search area
        boolean success = true;
        warning = "";
        if (trapezUtil == null) {
            warning = "Silahkan lakukan perhitungan search area terlebih dahulu";
            success = false;
        } else if (lebarTaskArea == 0) {
            warning = "Lebar task area harus diisi";
            success = false;
        } else {
            try {

            } catch (Exception e) {
                warning = "Lebar task area harus berupa angka";
                success = false;
            }
        }
        return success;
    }

    private void clearFormTaskArea() {
        lebarTaskArea = 0;
        listPanjangTaskArea = null;
        luasTaskArea = 0;
    }

    private TrapeziumTaskArea getObjectFromTaskArea(TrapeziumTaskArea trapeziumTaskArea, TaskArea taskArea) {
        String tempSplitPanjang = null;

        trapeziumTaskArea.setTaskAreaLength(taskArea.getTaskAreaLength());
        if (unit == 0) {
            trapeziumTaskArea.setLuasTaskArea(taskArea.getTaskAreaZone());
        } else {
            trapeziumTaskArea.setLuasTaskArea(taskArea.getTaskAreaZone() / DEFAULT_KM);
        }
        if (panjangTaskArea.equals("1")) {
            tempSplitPanjang = "1x Panjang";
        } else {
            tempSplitPanjang = "1/2x Panjang";
        }
        trapeziumTaskArea.setSplit(tempSplitPanjang);
        trapeziumTaskArea.setSmallCoord1(taskArea.getSmallCoord1().toString());
        trapeziumTaskArea.setSmallCoord2(taskArea.getSmallCoord2().toString());
        trapeziumTaskArea.setBigCoord1(taskArea.getBigCoord1().toString());
        trapeziumTaskArea.setBigCoord2(taskArea.getBigCoord2().toString());
        trapeziumTaskArea.setUnit(unit);
        trapeziumTaskArea.setTrapeziumArea(trapeziumArea);
        trapeziumTaskArea.setUnit(unit);
        trapeziumTaskArea.setWaypoint(Double.parseDouble(wayPoint));
        return trapeziumTaskArea;
    }

    public void loadFormTaskArea() {
        if (listTrapeziumTaskAreaModel != null) {
            setDataToFormTaskArea(trapeziumTaskArea);
        }
    }

    public void deleteTaskArea(TrapeziumTaskArea area) {
        if (area != null) {
            area.setDeleted(true);
            area.setTrapeziumTaskAreaID(area.getTrapeziumTaskAreaID());
            area.setModifiedBy(menuNavMBean.getUserSession().getUsername());
            trapeziumTaskAreaRepo.save(area);

            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Task Area berhasil dihapus");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Task Area yang ingin hapus tidak ada");
        }
    }

    private void setDataToFormTaskArea(TrapeziumTaskArea area) {
        if (area.getUnit() != null) {
            unit = area.getUnit().intValue();
        }

        lebarTaskArea = area.getTaskAreaLength();
        if (area.getSplit().equalsIgnoreCase("1x Panjang")) {
            panjangTaskArea = "1";
        } else {
            panjangTaskArea = "2";
        }
//        panjangTaskArea = area.getSplit();
        if (unit == 0) {
            luasTaskArea = DecimalUtil.rounding(area.getLuasTaskArea());
        } else {
            luasTaskArea = DecimalUtil.rounding(area.getLuasTaskArea() / DEFAULT_KM);
        }

    }

    private void saveFormTaskArea() {
        if (validateInputTaskArea()) {
            List<TaskArea> tempTaskList;
            UUID uuid = calculateTaskArea();
            if (uuid != null) {
                tempTaskList = new ArrayList<>(taskUtil.getTaskAreaHashMap().values());

                for (TaskArea taskArea : tempTaskList) {
                    TrapeziumTaskArea tempTrapeziumTaskArea;

                    if (worksheetName != null) {
                        if (trapeziumTaskArea == null) {
                            tempTrapeziumTaskArea = getObjectFromTaskArea(new TrapeziumTaskArea(), taskArea);
                            tempTrapeziumTaskArea.setName(worksheetName);
                            tempTrapeziumTaskArea.setCreatedBy(menuNavMBean.getUserSession().getUsername());
                            tempTrapeziumTaskArea.setTrapeziumTaskAreaID(formatTaskclassId());
                            tempTrapeziumTaskArea.setUserSiteID(ProsiaConstant.SITES);
                            tempTrapeziumTaskArea.setDateCreated(new Date());
                            trapeziumTaskAreaRepo.save(tempTrapeziumTaskArea);
                            addSingleRow(tempTrapeziumTaskArea);
                            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Simpan data berhasil !");
                            trapeziumTaskArea = tempTrapeziumTaskArea;
                            System.out.println("--trapeziumTaskArea--" + trapeziumTaskArea.toString());
                        } else {
                            tempTrapeziumTaskArea = getObjectFromTaskArea(trapeziumTaskArea, taskArea);
                            tempTrapeziumTaskArea.setName(worksheetName);
                            tempTrapeziumTaskArea.setCreatedBy(menuNavMBean.getUserSession().getUsername());
                            trapeziumTaskAreaRepo.save(tempTrapeziumTaskArea);
                            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Update data berhasil !");

                        }
                    }
                }
                clearFormTaskArea();
                taskUtil.setNextTaskArea();
                trapeziumArea.setTaskAreaLastPoint(taskUtil.getCurrentStartPoint().toString());
                trapeziumArea.setTotalTaskAreaLength(taskUtil.getTotalTaskAreaLengthInNm());
                trapeziumAreaRepo.save(trapeziumArea);

                //prepare data to datagrid model
                listTrapeziumTaskArea = trapeziumTaskAreaRepo.findByTrapeziumAreaAndDeletedFalse(trapeziumArea);
            }
        }

    }

    private void addSingleRow(TrapeziumTaskArea tta) {
        TrapeziumTaskArea modelTask = new TrapeziumTaskArea();
        modelTask.setTrapeziumTaskAreaID(tta.getTrapeziumTaskAreaID());
        modelTask.setName(tta.getName());
        modelTask.setTaskAreaLength(tta.getTaskAreaLength());
        modelTask.setSplit(tta.getSplit());
        modelTask.setLuasTaskArea(tta.getLuasTaskArea());
        trapeziumTaskAreaHashMap.put(tta.getTrapeziumTaskAreaID(), tta);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Other Function">
    private String formatZonaWaktu(String nilai, String gmt) {
        String result = null;
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

    private void setData() {
        if (incident != null) {
            arrlatLkp[Position] = Double.parseDouble(incident.getLatitude());
            arrlongLkp[Position] = Double.parseDouble(incident.getLongitude());
            Calendar cal = Calendar.getInstance();
            cal.setTime(incident.getStartdate());
            cal.setTimeZone(TimeZone.getTimeZone(incident.getStartdatetimezone()));
            sdfDtz = new SimpleDateTimeZoneFormat();
            sdfDtz.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT);
            incTime = formatZonaWaktu(sdfDtz.format(cal), incident.getStartdatetimezone());

        }
    }

    private void clearFormSearchArea() {
        latLkp = 0;
        longLkp = 0;
        latDest = 0;
        longDest = 0;
        waktuoperasi = null;
        safetyFactor = 1.1;
        distressCraftError = 0.1;
        searchCraftError = 0.1;
        searchError = 0.0;
//        width = null;
        lebar = 0.0;
        kepala = 0.0;
        luasArea = 0.0;
        viewCoordinate1(incident.getLatitude(), incident.getLongitude());
        viewCoordinate2(String.valueOf(latDest), String.valueOf(longDest));
    }

    private void destroyObject() {
        for (int i = 0; i < 20; i++) {
            arrlatLkp[i] = 0;
            arrlongLkp[i] = 0;
            arrlatDest[i] = 0;
            arrlongDest[i] = 0;
            arrsafetyFactor[i] = 0;
            arrdistressCraftError[i] = 0;
            arrsearchCraftError[i] = 0;
            arrsmallRadius[i] = 0;
        }
        trapezUtil = new TrapeziumSearchAreaUtil();
        trapezUtil.cleanMemory();

//        trapeziumArea = new TrapeziumArea();
        driftCalculationTableList = new DriftCalculationTableList();
        driftCalculationTableList.resetSize();
        driftCalculationTableList.setActualSize(0);

        sizeTableList = 0;
        isInsert = false;
        iCursor = 0;
        Position = 0;
        LastPosition = -1;
        isLoadForm = false;
        //form.getLblNumWaypoint().setText("1 / 1");
        trackSpacing = 1;
        smallTrapezAreaLeg = 0.0;

        for (int i = 0; i < 20; i++) {
            smallRoundPivot[i] = null;
            largeRoundPivot[i] = null;
            max[i] = 0;
            min[i] = 0;
            width[i] = 0;
            heading[i] = 0;
            smallRoundRadiusInNm[i] = 0;
            largeRoundRadiusInNm[i] = 0;
            startLatLng[i] = null;
            lkpAsPivot[i] = null;
            parrentID[i] = "";
            latGetBigCoord1[i] = 0;
            longGetBigCoord1[i] = 0;
            latGetBigCoord2[i] = 0;
            longGetBigCoord2[i] = 0;
            latGetSmallCoord1[i] = 0;
            longGetSmallCoord1[i] = 0;
            latGetSmallCoord2[i] = 0;
            longGetSmallCoord2[i] = 0;
            statheading[i] = 0;
            statwidth[i] = 0;
            statmin[i] = 0;
            statmax[i] = 0;
            statlatlong[i] = null;
        }
        l = new ArrayList<>();
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);

        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<TrapeziumArea> lis = trapeziumAreaRepo.findAllByTrapeziumAreaIDContaining(ProsiaConstant.SITES);

        if (lis.isEmpty()) {

            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = trapeziumAreaRepo.findClassByMaxId(ProsiaConstant.SITES);
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
        }
        return classId;
    }

    public String formatTaskclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);

        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<TrapeziumTaskArea> lis = trapeziumTaskAreaRepo.findAllByTrapeziumTaskAreaIDContaining(ProsiaConstant.SITES);

        if (lis.isEmpty()) {

            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {

            String maxId = trapeziumTaskAreaRepo.findClassByMaxId(ProsiaConstant.SITES);
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
        }
        return classId;
    }

    private void InsertTabelList() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        setCoordinate();
        driftCalculationTableList.setTabelList("",
                deskripsi,
                waktuoperasi,
                String.valueOf(latLkp),
                String.valueOf(longLkp),
                String.valueOf(latDest),
                String.valueOf(longDest),
                String.valueOf(safetyFactor),
                String.valueOf(distressCraftError),
                String.valueOf(searchCraftError),
                String.valueOf(luasArea),
                incident.getIncidentid(),
                dateFormat.format(date),
                menuNavMBean.getUserSession().getUsername(),
                String.valueOf(0),
                ProsiaConstant.SITES,
                "",
                "0",
                "0",
                "0",
                "0",
                "");

    }

    private void UpdateTableList(int index) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        driftCalculationTableList.setSize(Position);
        for (int i = 0; i < index; i++) {
            driftCalculationTableList.UpdateTabelList("",
                    deskripsi,
                    waktuoperasi,
                    String.valueOf(latLkp),
                    String.valueOf(longLkp),
                    String.valueOf(latDest),
                    String.valueOf(longDest),
                    String.valueOf(safetyFactor),
                    String.valueOf(distressCraftError),
                    String.valueOf(searchCraftError),
                    String.valueOf(luasArea),
                    incident.getIncidentid(),
                    dateFormat.format(date),
                    menuNavMBean.getUserSession().getUsername(),
                    String.valueOf(0),
                    ProsiaConstant.SITES,
                    "",
                    "0",
                    "0",
                    "0",
                    LastPosition);
        }
    }

    private void UpdateSearchArea() {
        if (validateInputSearchArea()) {

            if (driftCalculationTableList.getSize() >= 0) {
                arrlatLkp[LastPosition] = Double.valueOf(driftCalculationTableList.getLatlkp()[Position]);
                arrlongLkp[LastPosition] = Double.valueOf(driftCalculationTableList.getLonglkp()[Position]);
                arrlatDest[LastPosition] = Double.valueOf(driftCalculationTableList.getLatdest()[Position]);
                arrlongDest[LastPosition] = Double.valueOf(driftCalculationTableList.getLongdest()[Position]);
                arrsafetyFactor[LastPosition] = Double.valueOf(driftCalculationTableList.getSafetyfactor()[Position]);
                arrdistressCraftError[LastPosition] = Double.valueOf(driftCalculationTableList.getDistresserror()[Position]);
                arrsearchCraftError[LastPosition] = Double.valueOf(driftCalculationTableList.getSearcherror()[Position]);

                arrsmallRadius[LastPosition] = arrsafetyFactor[Position] * Math.sqrt(Math.pow(arrsearchCraftError[Position], 2)
                        + Math.pow(arrdistressCraftError[Position], 2));

                trapezUtil = new TrapeziumSearchAreaUtil(arrlatDest, arrlongDest, arrlatLkp, arrlongLkp, arrsmallRadius, LastPosition, isInsert);

                kepala = DecimalUtil.rounding(trapezUtil.updateArrHeading()[Position]);
                if (unit == 0) {
                    lebar = DecimalUtil.rounding(trapezUtil.updateArrWIdth()[Position]);
                } else {
                    lebar = DecimalUtil.rounding(trapezUtil.updateArrWIdth()[Position] / DEFAULT_KM);
                }
                if (unit == 0) {
                    luasArea = DecimalUtil.rounding(trapezUtil.updateArrLuasArea()[Position]);
                } else {
                    luasArea = DecimalUtil.rounding(trapezUtil.updateArrLuasArea()[Position] / DEFAULT_KM);
                }

                taskUtil = new TrapeziumTaskAreaUtil(trapezUtil, LastPosition, isInsert);

                //hitung untuk map
                if (isLoadForm == false) {
                    CalculateMapArr();
                }
            }

            //update hasil calculasi di arrayList
            driftCalculationTableList.updateCalculateTableList(Position, String.valueOf(kepala), String.valueOf(lebar), String.valueOf(luasArea));
        }
    }

    private void addSearchArea() {
        if (validateInputSearchArea()) {

            if (driftCalculationTableList.getSize() >= 0) {
                arrlatLkp[Position] = Double.valueOf(driftCalculationTableList.getLatlkp()[Position]);
                arrlongLkp[Position] = Double.valueOf(driftCalculationTableList.getLonglkp()[Position]);
                arrlatDest[Position] = Double.valueOf(driftCalculationTableList.getLatdest()[Position]);
                arrlongDest[Position] = Double.valueOf(driftCalculationTableList.getLongdest()[Position]);
                arrsafetyFactor[Position] = Double.valueOf(driftCalculationTableList.getSafetyfactor()[Position]);
                arrdistressCraftError[Position] = Double.valueOf(driftCalculationTableList.getDistresserror()[Position]);
                arrsearchCraftError[Position] = Double.valueOf(driftCalculationTableList.getSearcherror()[Position]);

                arrsmallRadius[Position] = arrsafetyFactor[Position] * Math.sqrt(Math.pow(arrsearchCraftError[Position], 2)
                        + Math.pow(arrdistressCraftError[Position], 2));

                trapezUtil = new TrapeziumSearchAreaUtil(arrlatDest, arrlongDest, arrlatLkp, arrlongLkp, arrsmallRadius, Position, isInsert);

                kepala = DecimalUtil.rounding(trapezUtil.arrHeading()[Position]);
                if (unit == 0) {
                    lebar = DecimalUtil.rounding(trapezUtil.arrWidth()[Position]);
                } else {
                    lebar = DecimalUtil.rounding(trapezUtil.arrWidth()[Position] / DEFAULT_KM);
                }
                if (unit == 0) {
                    luasArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[Position]);
                } else {
                    luasArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[Position] / DEFAULT_KM);
                }

                taskUtil = new TrapeziumTaskAreaUtil(trapezUtil, Position, isInsert);

                //masukkan hasil calculasi ke arrayList
                driftCalculationTableList.getCalculateTableList(String.valueOf(kepala),
                        String.valueOf(lebar), String.valueOf(luasArea));
                sizeTableList = driftCalculationTableList.getActualSize() + 1;
                driftCalculationTableList.setSize(sizeTableList);
                driftCalculationTableList.setActualSize(sizeTableList);
                //hitung untuk map
                if (isLoadForm == false) {
                    CalculateMapArr();
                }
            }

        } else {

        }
    }

    public void changeWorksheetName() {
        if (trapeziumAreaID.equals("")) {
            trapeziumAreaID = null;
        } else {
            trapeziumArea = trapeziumAreaRepo.findByTrapeziumAreaID(trapeziumAreaID);
            lastTrapeziumAreaID = trapeziumAreaID;
            createdBy = trapeziumArea.getCreatedBy();
            lastModified = trapeziumArea.getDateCreated();
            tempParentID = trapeziumArea.getParentID();
        }
    }

    public List<SelectItem> getListWorksheetName() {

        try {
            incident = incidentRepo.findOne(currentIncidentID);
            if (listItemTrapeziumArea == null) {
                listItemTrapeziumArea = new ArrayList<>();
                listTrapeziumArea = trapeziumAreaRepo.findAllByIncidentAndWaypointAndDeletedFalse(incident, 1.0);
                listItemTrapeziumArea.add(new SelectItem("0", " "));

                for (TrapeziumArea lst : listTrapeziumArea) {
                    listItemTrapeziumArea.add(new SelectItem(lst.getTrapeziumAreaID(), lst.getWorksheetName()));
                }

            }
        } catch (Exception ex) {
            listItemTrapeziumArea = null;
        }
        return listItemTrapeziumArea;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Save dan Get Function">
    private void saveFormSearchArea() {

        if (validateInputSearchArea()) {

            if (isLoadForm == false) {

                //jika belum ada perhitungan
                if (hasCalculate == false) {

                    hitungSearchArea();
                }
                //lihat apakah sudah terjadi perhitungan
                if (kepala == 0) {

                    addTaskArea();
                }
            } else {

                UpdateTableList(driftCalculationTableList.getSize());
                isLoadForm = false;
            }

            if (worksheetName != null) {

                if (trapeziumArea == null) {
                    getArrObjectFromFormSearchArea(new TrapeziumArea(), worksheetName);
                    addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan !");
                } else {
                    UpdateGetArrObjectFromFormSearchArea(new TrapeziumArea(), worksheetName);
                    addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil diupdate !");
                }
            }
            //setSearchAreaDataToTaskAreaForm();
            //loadWaypointFromDB();

        }
    }

    private void saveSearchAreaToTaskArea() {
        if (sizeTableList > 0) {
            luasAreainTask = DecimalUtil.rounding(trapezUtil.arrHeading()[0]);
            if (unit == 0) {
                lebarSearchArea = DecimalUtil.rounding(trapezUtil.arrWidth()[0]);
            } else {
                lebarSearchArea = DecimalUtil.rounding(trapezUtil.arrWidth()[0] / DEFAULT_KM);
            }
            if (unit == 0) {
                lebarTotalTaskArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[0]);
            } else {
                lebarTotalTaskArea = DecimalUtil.rounding(trapezUtil.arrLuasArea()[0] / DEFAULT_KM);
            }
        }
        parentID = trapeziumArea.getParentID();
        wayPoint = "1";
//        setTaskAreaTableModelBy(trapeziumArea);
        if (trapeziumArea.getTotalTaskAreaLength() != null) {
            taskUtil.setTotalTaskAreaLengthInNm(trapeziumArea.getTotalTaskAreaLength());
        }
        String lastPoint = trapeziumArea.getTaskAreaLastPoint();
        if (lastPoint != null && !lastPoint.equalsIgnoreCase("")) {
            lastPoint = lastPoint.trim();
            lastPoint = lastPoint.replace(")", "");
            lastPoint = lastPoint.replace("(", "");
            lastPoint = lastPoint.replace("Lat:-", "");
            lastPoint = lastPoint.replace("Lng:", "");
            try {
                String[] temp = lastPoint.split(",");
                Double tempLat = Double.valueOf(temp[0]);
                Double tempLong = Double.valueOf(temp[1]);
                taskUtil.setCurrentStartPoint(new LatLng(tempLat, tempLong));
            } catch (Exception e) {
                System.out.println("err -" + e.getMessage());
            }
        }
    }

    public void loadDataSearchArea() {
        destroyObject();
        loadTabelList(incident);
        LastPosition = 0;
        isInsert = true;
        loadFirstSearchArea();
        numWayPoint = String.valueOf("1 / " + sizeTableList);
        parentID = trapeziumArea.getParentID();
        wayPoint = "1";

        SearchToTaskArea();
    }

    public void viewPlanTrapezium(String ID) {
        System.out.println("trapeziumArea : " + ID);
        if (isViewMapPlanTrapezium) {
        } else {
            incidentPlanningMBean.setIsTrapesium(true);
        }
        driftCalculationTableList.resetSize();
        loadTabelListPlanTrapez(incident, ID);
        LastPosition = 0;
        isInsert = true;
        loadFirstSearchArea();
        numWayPoint = String.valueOf("1 / " + sizeTableList);
        parentID = trapeziumArea.getParentID();
        wayPoint = "1";
        SearchToTaskArea();
    }

    private void SearchToTaskArea() {
        setSearchAreaDataToTaskAreaForm();
        if (trapeziumArea.getTotalTaskAreaLength() != null) {
            taskUtil.setTotalTaskAreaLengthInNm(trapeziumArea.getTotalTaskAreaLength());
        }
        String lastPoint = trapeziumArea.getTaskAreaLastPoint();
        if (lastPoint != null && !lastPoint.equalsIgnoreCase("")) {
            lastPoint = lastPoint.trim();
            lastPoint = lastPoint.replace(")", "");
            lastPoint = lastPoint.replace("(", "");
            lastPoint = lastPoint.replace("Lat:-", "");
            lastPoint = lastPoint.replace("Lng:", "");
            try {
                String[] temp = lastPoint.split(",");
                Double tempLat = Double.valueOf(temp[0]);
                Double tempLong = Double.valueOf(temp[1]);
                taskUtil.setCurrentStartPoint(new LatLng(tempLat, tempLong));
            } catch (Exception e) {
                System.out.println("err -" + e.getMessage());
            }
        }
        //prepare data to datagrid model
        listTrapeziumTaskArea = trapeziumTaskAreaRepo.findByTrapeziumAreaAndDeletedFalse(trapeziumArea);
    }

    private void deleteSearchArea(TrapeziumArea area) {
        if (area != null) {
            List<TrapeziumArea> taList = trapeziumAreaRepo.findByIncidentAndParentIDAndDeletedFalseOrderByWaypoint(incident, tempParentID);

            for (TrapeziumArea area1 : taList) {
                area1.setDeleted(true);
                trapeziumAreaRepo.save(area1);
            }
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Hapus data berhasil");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Search Area yang ingin hapus tidak ada");
        }
    }

    private void setSearchAreaDataToTaskAreaForm() {
        if (unit == 0) {
            luasAreainTask = kepala;
            lebarSearchArea = lebar;
            lebarTotalTaskArea = luasArea;
        } else {
            luasAreainTask = DecimalUtil.rounding(trapeziumArea.getLuasArea() / DEFAULT_KM);
            lebarTotalTaskArea = DecimalUtil.rounding(trapeziumArea.getTotalTaskAreaLength() / DEFAULT_KM);
            if (sizeTableList == 0) {
                lebarSearchArea = DecimalUtil.rounding(trapezUtil.width() / DEFAULT_KM);
            } else {
                lebarSearchArea = DecimalUtil.rounding(trapezUtil.arrWidth()[0] / DEFAULT_KM);
            }
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Prev and Next Function">
    public void yesAnswerTA() {
        isYesAnswer = true;
        SearchAreaNext();
        refreshPage();
    }

    public void noAnswerTA() {
        isYesAnswer = false;
        SearchAreaNext();
        refreshPage();
    }

    private void beforeSearchAreaNext() {
//        hitungSearchArea();
        if (Position >= sizeTableList - 1) {
            RequestContext.getCurrentInstance().execute("PF('widgetConfirmTA').show()");
        } else {
            SearchAreaNext();
        }
    }

    private void refreshPage() {
        //RequestContext.getCurrentInstance().update("numWayPoint");
    }

    private void SearchAreaNext() {
        if (validateInputSearchArea()) {
            if (hasCalculate == true) {
                if (isYesAnswer == true) {
                    if (hasPrev == true) {
                        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi",
                                "Data baru hanya bisa dibuat saat waypoint lebih besar dari " + sizeTableList + "");
                    } else {

                        //caption map jadi tampilkan
                        intViewMapPosition[Position + 1] = 0;
                        //form.getBtnShowOnMap().setText("Tampilkan Pada Peta");
                        viewLabelMap = labelMap[1];
                        isClearMap = false;
                    }
                } else if (driftCalculationTableList.getSize() > 0 && (sizeTableList - 1) > Position) {
                    ListNext(Position);

                } else {

                    return;
                }
            }
            iCursor++;
            Position = iCursor;

            if (Position > LastPosition) {
                if (LastPosition - Position < 0 && Position > sizeTableList - 1) {
                    hasCalculate = false;
                    String oldDesc = deskripsi;
                    setNewForm();
                    deskripsi = oldDesc;
                    numWayPoint = String.valueOf(Position + 1) + " / " + String.valueOf(sizeTableList + 1);
                } else {
                    ListNext(Position);
                    numWayPoint = String.valueOf(Position + 1) + " / " + String.valueOf(sizeTableList);

                    LastPosition = Position;
                    if (intViewMapPosition[Position] == 1) {

                        viewLabelMap = labelMap[0];
                        isClearMap = true;
                    } else {

                        viewLabelMap = labelMap[1];
                        isClearMap = false;
                    }
                }
            }
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", warning);
        }
        RequestContext.getCurrentInstance().execute("PF('widgetConfirmTA').hide()");
    }

    private void SearchAreaPrev() {
        try {
//            hitungSearchArea();
            if (Position > 0) {
                iCursor--;
                Position = iCursor;
            } else {
                return;
            }
            if (Position > -1 && Position <= LastPosition) {
                ListPrev(Position);
                hasPrev = true;
                LastPosition = Position;
                numWayPoint = String.valueOf(LastPosition + 1) + " / " + String.valueOf(sizeTableList);
                if (intViewMapPosition[Position] == 1) {
                    viewLabelMap = labelMap[0];
                    isClearMap = true;
                } else {
                    viewLabelMap = labelMap[1];
                    isClearMap = false;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception SearchAreaPrev:" + ex);
        }
    }

    private void addTaskArea() {
        isInsert = true;
        InsertTabelList();
        addSearchArea();
        LastPosition = Position;
    }

    private void setNewForm() {
        if (validateInputSearchArea()) {
            latLkp = latDest;
            longLkp = longDest;
            latDest = 0;
            longDest = 0;
            viewCoordinate1(String.valueOf(latLkp), String.valueOf(longLkp));
            viewCoordinate2(String.valueOf(latDest), String.valueOf(longDest));
            unit = 0;
            distressCraftError = 0.1;
            panjangTaskArea = "1";
            safetyFactor = 1.1;
            searchCraftError = 0.1;
            deskripsi = "";
            kepala = 0;
            lebar = 0;
            luasArea = 0;
        }
    }

    private void ListNext(int index) {
        try {
            latLkp = Double.parseDouble(driftCalculationTableList.getLatlkp()[index]);
            longLkp = Double.parseDouble(driftCalculationTableList.getLonglkp()[index]);
            latDest = Double.parseDouble(driftCalculationTableList.getLatdest()[index]);
            longDest = Double.parseDouble(driftCalculationTableList.getLongdest()[index]);

            viewCoordinate1(String.valueOf(latLkp), String.valueOf(longLkp));
            viewCoordinate2(String.valueOf(latDest), String.valueOf(longDest));

            safetyFactor = indexSafetyFactor(driftCalculationTableList.getSafetyfactor()[index]);
            distressCraftError = indexDistressSearch(driftCalculationTableList.getDistresserror()[index]);
            searchCraftError = indexDistressSearch(driftCalculationTableList.getSearcherror()[index]);

            if (arrCboHeading[index] != 0.0) {
                kepala = DecimalUtil.rounding(arrCboHeading[index]);
            } else {
                kepala = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getHeading()[index]));
            }
            if (arrCboWidth[index] != 0.0) {
                lebar = DecimalUtil.rounding(arrCboWidth[index]);
            } else {
                lebar = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getWidth()[index]));
            }
            if (arrCboLuas[index] != 0.0) {
                luasArea = DecimalUtil.rounding(arrCboLuas[index]);
            } else {
                luasArea = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getLuasAreaTrapezium()[index]));
            }
        } catch (Exception ex) {
            System.out.println("ListNext:" + ex);
        }
    }

    private void ListPrev(int index) {
        latLkp = Double.parseDouble(driftCalculationTableList.getLatlkp()[index]);
        longLkp = Double.parseDouble(driftCalculationTableList.getLonglkp()[index]);
        latDest = Double.parseDouble(driftCalculationTableList.getLatdest()[index]);
        longDest = Double.parseDouble(driftCalculationTableList.getLongdest()[index]);

        viewCoordinate1(String.valueOf(latLkp), String.valueOf(longLkp));
        viewCoordinate2(String.valueOf(latDest), String.valueOf(longDest));

        safetyFactor = indexSafetyFactor(driftCalculationTableList.getSafetyfactor()[index]);
        distressCraftError = indexDistressSearch(driftCalculationTableList.getDistresserror()[index]);
        searchCraftError = indexDistressSearch(driftCalculationTableList.getSearcherror()[index]);

        if (arrCboHeading[index] != 0.0) {
            kepala = DecimalUtil.rounding(arrCboHeading[index]);
        } else {
            kepala = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getHeading()[index]));
        }
        if (arrCboWidth[index] != 0.0) {
            lebar = DecimalUtil.rounding(arrCboWidth[index]);
        } else {
            lebar = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getWidth()[index]));
        }
        if (arrCboLuas[index] != 0.0) {
            luasArea = DecimalUtil.rounding(arrCboLuas[index]);
        } else {
            luasArea = DecimalUtil.rounding(Double.parseDouble(driftCalculationTableList.getLuasAreaTrapezium()[index]));
        }
    }

    private double indexSafetyFactor(String obj) {
        double convert = Double.valueOf(obj);
        if (convert == 2) {
            return 2.0;
        } else {
            return convert;
        }

    }

    private double indexDistressSearch(String obj) {
        double convert = Double.valueOf(obj);
        if (convert == 1) {
            return 1.0;
        } else if (convert == 2) {
            return 2.0;
        } else if (convert == 4) {
            return 4.0;
        } else if (convert == 5) {
            return 5.0;
        } else if (convert == 10) {
            return 10.0;
        } else if (convert == 15) {
            return 15.0;
        } else {
            return convert;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Show On Map">
    private boolean CalculateMapArr() {
        try {
            isInsert = true;
            MapController.removeCurrentDriftCalculation();
            if (sizeTableList > 0) {

                if (isLoadForm == true) {
                    trapezUtil.setSize(Position);
                    if (LastMapPosition < Position) {
                        addPopUpMessage(FacesMessage.SEVERITY_INFO, "View Map",
                                "View map harus di lakukan secara berurutan!");
                        return false;
                    } else {
                        LastMapPosition = Position + 1;
                    }
                }

                smallRoundPivot[Position] = new LatLng(trapezUtil.getArrsmallLatitude()[Position], trapezUtil.getArrsmallLongitude()[Position]);
                largeRoundPivot[Position] = new LatLng(trapezUtil.getArrbigLatitude()[Position], trapezUtil.getArrbigLongitude()[Position]);

                try {
                    if (isInsert == true) {
                        max[Position] = trapezUtil.arrMaxSearchLeg(trackSpacing)[Position];
                    } else {
                        max[Position] = trapezUtil.updateArrMaxSearchLeg(trackSpacing)[Position];
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {
                        max[Position] = trapezUtil.arrMaxSearchLeg(trackSpacing)[0];
                    } else {
                        max[Position] = trapezUtil.updateArrMaxSearchLeg(trackSpacing)[0];
                    }
                }
                try {
                    if (isInsert == true) {
                        min[Position] = trapezUtil.arrMinSearchLeg(trackSpacing)[Position];
                    } else {
                        min[Position] = trapezUtil.updateArrMinSearchLeg(trackSpacing)[Position];
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {
                        min[Position] = trapezUtil.arrMinSearchLeg(trackSpacing)[0];
                    } else {
                        min[Position] = trapezUtil.updateArrMinSearchLeg(trackSpacing)[0];
                    }
                }

                if (min[Position] < 0) {
                    smallTrapezAreaLeg = trapezUtil.arrGetSmallL()[Position] * 60 * 2;
                    trackSpacing = smallTrapezAreaLeg * 0.15;
                    min[Position] = smallTrapezAreaLeg - trackSpacing;
                }
                width[Position] = trapezUtil.arrWidth()[Position];
                heading[Position] = trapezUtil.arrHeading()[Position];
                smallRoundRadiusInNm[Position] = trapezUtil.getArrsmallRadius()[Position] * 60;
                largeRoundRadiusInNm[Position] = trapezUtil.getArrbigRadius()[Position] * 60;
                try {
                    if (isInsert == true) {
                        startLatLng[Position] = trapezUtil.arrGetSmallCoord1()[Position];
                    } else {
                        startLatLng[Position] = trapezUtil.updateArrGetSmallCoord1()[Position];
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {
                        startLatLng[Position] = trapezUtil.arrGetSmallCoord1()[0];
                    } else {
                        startLatLng[Position] = trapezUtil.updateArrGetSmallCoord1()[0];
                    }
                }

                lkpAsPivot[Position] = new LatLng(trapezUtil.getArrsmallLatitude()[Position], trapezUtil.getArrsmallLongitude()[Position]);
                startLatLng[Position] = MapCalculation.rotation(lkpAsPivot[Position], startLatLng[Position], (360 - heading[Position]));
                startLatLng[Position] = new LatLng(startLatLng[Position].getLat() + ((trackSpacing * 0.5) * (0.016666666666666666666666666666667)), startLatLng[Position].getLng() + ((trackSpacing * 0.5) * (0.016666666666666666666666666666667)));
                startLatLng[Position] = MapCalculation.rotation(lkpAsPivot[Position], startLatLng[Position], heading[Position]);
                try {
                    if (isInsert == true) {

                        latGetBigCoord1[Position] = trapezUtil.arrGetBigCoord1()[Position].getLat();

                    } else {
                        latGetBigCoord1[Position] = trapezUtil.updateArrGetBigCoord1()[Position].getLat();
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {

                        latGetBigCoord1[Position] = trapezUtil.arrGetBigCoord1()[0].getLat();

                    } else {
                        latGetBigCoord1[Position] = trapezUtil.updateArrGetBigCoord1()[0].getLat();
                    }
                }
                try {

                    longGetBigCoord1[Position] = trapezUtil.arrGetBigCoord1()[Position].getLng();

                } catch (Exception ex) {
                    longGetBigCoord1[Position] = trapezUtil.arrGetBigCoord1()[0].getLng();
                }
                try {
                    if (isInsert == true) {

                        latGetBigCoord2[Position] = trapezUtil.arrGetBigCoord2()[Position].getLat();
                    } else {
                        latGetBigCoord2[Position] = trapezUtil.updateArrGetBigCoord2()[Position].getLat();
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {
                        latGetBigCoord2[Position] = trapezUtil.arrGetBigCoord2()[0].getLat();
                    } else {
                        latGetBigCoord2[Position] = trapezUtil.updateArrGetBigCoord2()[0].getLat();
                    }
                }
                try {

                    longGetBigCoord2[Position] = trapezUtil.arrGetBigCoord2()[Position].getLng();
                } catch (Exception ex) {
                    longGetBigCoord2[Position] = trapezUtil.arrGetBigCoord2()[0].getLng();
                }
                try {

                    latGetSmallCoord1[Position] = trapezUtil.arrGetSmallCoord1()[Position].getLat();
                } catch (Exception ex) {
                    latGetSmallCoord1[Position] = trapezUtil.arrGetSmallCoord1()[0].getLat();
                }
                try {

                    longGetSmallCoord1[Position] = trapezUtil.arrGetSmallCoord1()[Position].getLng();
                } catch (Exception ex) {
                    longGetSmallCoord1[Position] = trapezUtil.arrGetSmallCoord1()[0].getLng();
                }
                try {
                    if (isInsert == true) {

                        latGetSmallCoord2[Position] = trapezUtil.arrGetSmallCoord2()[Position].getLat();
                    } else {
                        latGetSmallCoord2[Position] = trapezUtil.updateArrGetSmallCoord2()[Position].getLat();
                    }
                } catch (Exception ex) {
                    if (isInsert == true) {
                        latGetSmallCoord2[Position] = trapezUtil.arrGetSmallCoord2()[0].getLat();
                    } else {
                        latGetSmallCoord2[Position] = trapezUtil.updateArrGetSmallCoord2()[0].getLat();
                    }
                }
                try {

                    longGetSmallCoord2[Position] = trapezUtil.arrGetSmallCoord2()[Position].getLng();
                } catch (Exception ex) {
                    longGetSmallCoord2[Position] = trapezUtil.arrGetSmallCoord2()[0].getLng();
                }

                parrentID[Position] = GPoly.generateParrentID();
                return true;
            }
        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Warning", "Gagal menampilkan Trapezium Search Area");
            return false;
        }
        return false;
    }

    private void showSearchAreaOnMapArr() {
//        if(isVoidMap == true) return;
        try {
            if (intViewMapPosition[Position - 1] == 0) {
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Lakukan pembukaan Peta secara berurutan !");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            addPopUpMessage(FacesMessage.SEVERITY_ERROR, "Error", "GIS bermasalah");
//            return;
        }
        if (CalculateMapArr() == false) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Lakukan perhitungan terlebih dahulu");
            return;
        }

        isViewMap = true;
        intViewMapPosition[Position] = 1;
        LatLng v1 = new LatLng(latGetBigCoord1[Position], longGetBigCoord1[Position]);
        LatLng v2 = new LatLng(latGetSmallCoord1[Position], longGetSmallCoord1[Position]);
        LatLng v3 = new LatLng(latGetSmallCoord2[Position], longGetSmallCoord2[Position]);
        LatLng v4 = new LatLng(latGetBigCoord2[Position], longGetBigCoord2[Position]);
        l = new ArrayList<>();
        l.add(v1);
        l.add(v2);
        l.add(v3);
        l.add(v4);

        TrapeziumSearchArea trapeziumSearchArea
                = new TrapeziumSearchArea(smallRoundPivot[Position], largeRoundPivot[Position],
                        smallRoundRadiusInNm[Position], largeRoundRadiusInNm[Position],
                        min[Position], max[Position],
                        width[Position], trackSpacing,
                        heading[Position], trapezUtil.arrGetSmallL()[Position],
                        trapezUtil.arrGetBigL()[Position],
                        startLatLng[Position],
                        trapezUtil.arrGetSmallCoord1()[Position],
                        lkpAsPivot[Position], l,
                        "Current" + Position, parrentID[Position]);
        mapMBean.setListTaskSearchArea(new ArrayList<>());
        mapMBean.setIncident(incident);
        int index = 0;
        boolean match = false;
        for (TrapeziumSearchArea tsa : mapMBean.getListTrapeziumSearchArea()) {
            if (tsa.getId().equals("Current" + Position)) {
                match = true;
                break;
            }
            index += 1;
        }
        if (match) {
            mapMBean.getListTrapeziumSearchArea().set(index, trapeziumSearchArea);
        } else {
            mapMBean.getListTrapeziumSearchArea().add(trapeziumSearchArea);
        }
//        mapMBean.getMapModel().addOverlay(new Polyline(l));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/map/map/list.xhtml?search=TRAPEZIUM"
                + "&incident="
                + incident.getIncidentid() + "', "
                + "'_blank')");

        isClearMap = true;

        //masukkan ke static variable
        statheading[Position] = heading[Position];
        statwidth[Position] = width[Position];
        statmin[Position] = min[Position];
        statmax[Position] = max[Position];
        statlatlong[Position] = startLatLng[Position];

    }

    private void clearSearchAreaOnMapArr() {
        if (CalculateMapArr() == false) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Lakukan perhitungan terlebih dahulu!");
            return;
        }

        isViewMap = true;
        if (intViewMapPosition[Position + 1] != 0) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Lakukan Pembersihan Peta secara berurutan dari waypoint terakhir!");
            return;
        }
        intViewMapPosition[Position] = 0;
        LatLng v1 = new LatLng(latGetBigCoord1[Position], longGetBigCoord1[Position]);
        LatLng v2 = new LatLng(latGetSmallCoord1[Position], longGetSmallCoord1[Position]);
        LatLng v3 = new LatLng(latGetSmallCoord2[Position], longGetSmallCoord2[Position]);
        LatLng v4 = new LatLng(latGetBigCoord2[Position], longGetBigCoord2[Position]);
        l = new ArrayList<>();
        l.add(v1);
        l.add(v2);
        l.add(v3);
        l.add(v4);

        MapController.subtractJumArea();
        // Main.getBrowser() --> null
        TrapeziumSearchArea trapeziumSearchArea
                = new TrapeziumSearchArea(smallRoundPivot[Position], largeRoundPivot[Position],
                        smallRoundRadiusInNm[Position], largeRoundRadiusInNm[Position],
                        min[Position], max[Position],
                        width[Position], trackSpacing,
                        heading[Position], trapezUtil.arrGetSmallL()[Position],
                        trapezUtil.arrGetBigL()[Position],
                        startLatLng[Position],
                        trapezUtil.arrGetSmallCoord1()[Position],
                        lkpAsPivot[Position], l,
                        "Current" + Position, parrentID[Position]);

        viewLabelMap = labelMap[1];
        isClearMap = false;

        //masukkan ke static variable
        statheading[Position] = 0;
        statwidth[Position] = 0;
        statmin[Position] = 0;
        statmax[Position] = 0;
        statlatlong[Position] = null;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Coordinat">
    public void setCoordinate() {
        coordinateLat1.setType(true);
        coordinateLong1.setType(false);
        coordinateLat2.setType(true);
        coordinateLong2.setType(false);
        coordinateLat1.converter();
        coordinateLong1.converter();
        coordinateLat2.converter();
        coordinateLong2.converter();

        if (coordinateLat1.getValidDecimalDegrees() != null) {
            latLkp = Double.parseDouble(coordinateLat1.getValidDecimalDegrees().toString());
        } else {
            latLkp = 0.0;
        }
        if (coordinateLong1.getValidDecimalDegrees() != null) {
            longLkp = Double.parseDouble(coordinateLong1.getValidDecimalDegrees().toString());
        } else {
            longLkp = 0.0;
        }
        if (coordinateLat2.getValidDecimalDegrees() != null) {
            latDest = Double.parseDouble(coordinateLat2.getValidDecimalDegrees().toString());
        } else {
            latDest = 0.0;
        }
        if (coordinateLong2.getValidDecimalDegrees() != null) {
            longDest = Double.parseDouble(coordinateLong2.getValidDecimalDegrees().toString());
        } else {
            longDest = 0.0;
        }
    }

    public void initCoordinate1() {
        coordinateLat1 = new Coordinate();
        coordinateLong1 = new Coordinate();
        coordinateLat1.setType(true);
        coordinateLong1.setType(false);
    }

    public void viewCoordinate1(String lat, String lng) {
        if (lat != null
                || StringUtils.isNotBlank(lat)) {
            coordinateLat1.setType(true);
            coordinateLat1.setCounter(1);
            Double latitude = Double.parseDouble(lat);
            coordinateLat1.setDecimalDegrees(latitude);
            coordinateLat1.converter();
            coordinateLat1.setCounter(3);
        }
        if (lng != null
                || StringUtils.isNotBlank(lng)) {
            coordinateLong1.setType(false);
            coordinateLong1.setCounter(1);
            Double longitude = Double.parseDouble(lng);
            coordinateLong1.setDecimalDegrees(longitude);
            coordinateLong1.converter();
            coordinateLong1.setCounter(3);
        }
    }

    public void actionLatitude1() {
        coordinateLat1.converter();
        coordinateLat1.setCounter(coordinateLat1.getCounter() + 1);
        if (coordinateLat1.getCounter() > 3) {
            coordinateLat1.setCounter(1);
        }
    }

    public void actionLongitude1() {
        coordinateLong1.converter();
        coordinateLong1.setCounter(coordinateLong1.getCounter() + 1);
        if (coordinateLong1.getCounter() > 3) {
            coordinateLong1.setCounter(1);
        }
    }

    //////
    public void initCoordinate2() {
        coordinateLat2 = new Coordinate();
        coordinateLong2 = new Coordinate();
        coordinateLat2.setType(true);
        coordinateLong2.setType(false);
    }

    public void viewCoordinate2(String lat, String lng) {
        if (lat != null
                || StringUtils.isNotBlank(lat)) {
            coordinateLat2.setType(true);
            coordinateLat2.setCounter(1);
            Double latitude = Double.parseDouble(lat);
            coordinateLat2.setGaris(latitude < 0 ? 1 : 0);
            coordinateLat2.setDecimalDegrees(Math.abs(latitude));
            coordinateLat2.converter();
            coordinateLat2.setCounter(3);
        }
        if (lng != null
                || StringUtils.isNotBlank(lng)) {
            coordinateLong2.setType(false);
            coordinateLong2.setCounter(1);
            Double longitude = Double.parseDouble(lng);
            coordinateLong2.setGaris(longitude < 0 ? 1 : 0);
            coordinateLong2.setDecimalDegrees(Math.abs(longitude));
            coordinateLong2.converter();
            coordinateLong2.setCounter(3);
        }
    }

    public void actionLatitude2() {
        coordinateLat2.converter();
        coordinateLat2.setCounter(coordinateLat2.getCounter() + 1);
        if (coordinateLat2.getCounter() > 3) {
            coordinateLat2.setCounter(1);
        }
    }

    public void actionLongitude2() {
        coordinateLong2.converter();
        coordinateLong2.setCounter(coordinateLong2.getCounter() + 1);
        if (coordinateLong2.getCounter() > 3) {
            coordinateLong2.setCounter(1);
        }
    }
    //</editor-fold>

    @Override
    public void afterPropertiesSet() throws Exception {
        mapMBean.getListTrapeziumSearchArea().clear();
        currentIncidentID = driftCalcTrapeziumMBean.constIncidentID();
        incident = incidentRepo.findAllByIncidentid(currentIncidentID);
        viewLabelMap = labelMap[1];
        Position = 0;
        numWayPoint = "1 / 1";
        destroyObject();
        setData();
        viewCoordinate1(incident.getLatitude(), incident.getLongitude());

    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
