/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.TrapeziumTaskArea;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.JavaScriptBase;
import prosia.basarnas.web.util.map.MapCalculation;

/**
 *
 * @author Aris
 */
public class GTaskSearchArea extends GPoly<IncTaskSearchArea> {

    private final static Logger logger = Logger.getLogger(GTaskSearchArea.class);

//    public final static String DESC_AREA = "Area";
//    public final static String DESC_PIVOT = "Pivot";
//    public final static String DESC_WIDTH = "Width";
//    public final static String DESC_HEIGHT = "Height";
//    public final static String DESC_TILT = "Tilt";
    public final static String DESC_AREA = "Area";
    public final static String DESC_PIVOT = "Pusat";
    public final static String DESC_WIDTH = "Lebar";
    public final static String DESC_HEIGHT = "Panjang";
    public final static String DESC_TILT = "Heading";
    public final static String DESC_CHILD_ID = "Child_Id";
    //Field ID di set ketika Object sudah masuk kedalam navigator lihat add() method TaskSearchAreaController
    // id untuk di validasi ke search area
    private String parrentID;
    // id untuk di validasi ke task search pattern; 
    private String childID;
    private int radiusType;
    private String name;
    private List<GLatLng> l;
    private String area;
    private double width;
    private double tilt;
    private double height;
    private IncidentAsset incidentAsset;
    private GLatLng pivot;
    private InstantiateType state;
    private double trackSpacing;
    private GLatLng unrotateStart;
    private GLatLng unrotatePivot;
    //IMPORTANT !!, PROPERTIES INI SEBAGAI PENGHUBUNG ANTARA DRIFT WORKSHEET DAN TRAPEZIUM WORKSHEET
    private String driftTaskAreaID;
    private String trapeziumTaskAreaID;

    @Override
    public String createConcurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String removeConcurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum InstantiateType {
        CREATE_NEW,
        VIEW_FORM_DATABASE,
        TRAPEZIUM_COME_FROM
    }

    //CONSTRUCTOR VIEW DARI DATABASE
    public GTaskSearchArea(Object browser, List<GLatLng> l, GLatLng pivot, double width, double tilt, double height, String name, String parrentID, String childID) {
        //super(browser);
        this.l = l;
        this.childID = childID;
        this.width = width;
        this.height = height;
        this.pivot = pivot;
        this.tilt = tilt;
        this.name = name;
        this.parrentID = parrentID;
        this.state = InstantiateType.VIEW_FORM_DATABASE;
    }

    //CONSTRUCTOR PEMBUATAN BARU YANG BERASAL DARI DRIFT CALCULATION.
    public GTaskSearchArea(Object browser, GLatLng unrotateStart, GLatLng unrotatePivot, IncidentAsset incidentAsset, double width, double height, double trackSpacing, int radiusType, String name, String parrentID, String childID, String driftTaskAreaID) {
        //super(browser);
        this.childID = childID;
        this.radiusType = radiusType;
        this.width = width;
        this.height = height;
        this.incidentAsset = incidentAsset;
        this.name = name;
        this.trackSpacing = trackSpacing;
        this.unrotateStart = unrotateStart;
        this.unrotatePivot = unrotatePivot;
        this.parrentID = parrentID;
        this.driftTaskAreaID = driftTaskAreaID;
        this.state = InstantiateType.CREATE_NEW;
    }

    //Incident Asset jika memiliki sru ketika membuat task search area
    //CONSTRUCTOR PEMBUATAN BARU YANG BERASAL DARI TRAPEZIUM SEARCH AREA.
    public GTaskSearchArea(Object browser, List<GLatLng> l, GLatLng pivot, double width, double tilt, double height, String name, String parrentID, String childID, String trapeziumTaskAreaID) {
        //super(browser);
        this.l = l;
        this.childID = childID;
        this.width = width;
        this.height = height;
        this.pivot = pivot;
        this.tilt = tilt;
        this.name = name;
        this.parrentID = parrentID;
        this.trapeziumTaskAreaID = trapeziumTaskAreaID;
        this.state = InstantiateType.TRAPEZIUM_COME_FROM;
    }

    @Override
    public String writeScript() {
        //Trapezium TaskArea dan Database TaskArea memiliki function Untuk mengambar yang sama
        if (this.state == InstantiateType.VIEW_FORM_DATABASE || this.state == InstantiateType.TRAPEZIUM_COME_FROM) {
            return new StringBuilder("createTaskSearchAreaLoadDatabase(")
                    .append(ID).append(",")
                    .append(JavaScriptBase.<GLatLng>toArrayJavaScript(this.l)).append(",")
                    .append(this.pivot.toJavaScript()).append(",")
                    .append(this.width).append(",")
                    .append(this.height).append(",")
                    .append(this.tilt)
                    .append(")").toString();
        } else if (this.state == InstantiateType.CREATE_NEW) {
            return new StringBuilder("createTaskSearchAreaCreateNew(")
                    .append(ID).append(",")
                    .append(this.width).append(",")
                    .append(this.height).append(",")
                    .append(this.unrotateStart.toJavaScript()).append(",")
                    .append(this.unrotatePivot.toJavaScript())
                    .append(")").toString();
        }
        return null;
    }

    @Override
    public String removeScript() {
        return "removeTaskSearchArea(" + ID + ")";
    }

    public static String clearScript() {
        return "clearTaskSearchArea()";
    }

    public String getInFocusScript() {
        return "TASK_SEARCH_AREA.getItem(" + ID + ").inFocus()";
    }

    public String getTiltScript() {
        return "toString(TASK_SEARCH_AREA.getItem(" + ID + ").tilt)";
    }

    public String getVertexsScript() {
        return "toString(TASK_SEARCH_AREA.getItem(" + ID + ").l)";
    }

    public String getWidthScript() {
        return "toString(TASK_SEARCH_AREA.getItem(" + ID + ").width)";
    }

    public String getPivotScript() {
        return "toString(TASK_SEARCH_AREA.getItem(" + ID + ").pivot)";
    }

    public String getHeightScript() {
        return "toString(TASK_SEARCH_AREA.getItem(" + ID + ").height)";
    }

    public static String setSelectedGTaskSearchAreaScript(int ID) {
        if (ID < 0) {
            return "setSelectedTaskSearchArea('Nothing')";
        } else {
            return "setSelectedTaskSearchArea(" + ID + ")";
        }
    }

    //array pertama latlng
    //array kedua width
    //array ketiga height
    //array keempat pivot
    //array kelima tilt
    //format [Area : {(0,3),(0,2),(0,3)}#Width : 10#Height : 10#Pivot : (0,9)#Tilt : 45]
    public static String createAreaFormTaskSearchArea(GTaskSearchArea taskSearchArea) {
        //start is first of vertexs
        // update property dari luar sebelum memangil method ini
        String vertexs = Serializable.toStringJXBrowserReturnFormat(taskSearchArea.getL());
        StringBuilder area = new StringBuilder(DESC_OPEN)
                .append(DESC_AREA).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_COLLECTION).append(vertexs)
                .append(DESC_END_COLLECTION).append(DESC_FIELD_SEPARATOR)
                .append(DESC_WIDTH).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getWidth()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEIGHT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getHeight()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getPivot()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_TILT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getTilt()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PARRENT_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getParrentID()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_CHILD_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(taskSearchArea.getChildID())
                .append(DESC_CLOSE);
        return area.toString();
    }

    /**
     * @return parrentID ada code yang akan di cocokkan dengan search area
     * sesuai dengan yang ada pada pola pembuatan area
     */
    public static String getParrentID(IncTaskSearchArea taskSearchArea) throws Exception {
        String area = taskSearchArea.getArea();
        return area.split(DESC_PARRENT_ID + " " + DESC_VALUE_SEPARATOR + " ")[1].split(DESC_FIELD_SEPARATOR)[0];
    }

    /**
     * @return childID ada code yang akan di cocokkan dengan search pattern
     * sesuai dengan yang ada pada pola pembuatan area
     */
    public static String getChildID(IncTaskSearchArea taskSearchArea) throws Exception {
        String area = taskSearchArea.getArea();
        return area.split(DESC_CHILD_ID + " " + DESC_VALUE_SEPARATOR + " ")[1].split(DESC_CLOSE)[0];
    }

    /**
     * Membuat Object GTaskSearchArea baru berdasarkan String area yang
     * mendeskripsikan Bentuknya dan Posisinya
     */
    public static GTaskSearchArea taskSearchAreaToGTaskSearchArea(IncTaskSearchArea taskSearchArea) {
        String area = taskSearchArea.getArea();
        area = area.substring(1, area.length() - 1);
        /*
        area = area.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
         */
        String[] splitField = area.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String sVertexs = splitField[0].replaceAll(DESC_AREA + DESC_VALUE_SEPARATOR, "");
        String sWidth = splitField[1].replaceAll(DESC_WIDTH + DESC_VALUE_SEPARATOR, "");
        String sHeight = splitField[2].replaceAll(DESC_HEIGHT + DESC_VALUE_SEPARATOR, "");
        String sPivot = splitField[3].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, "");
        String sTilt = splitField[4].replaceAll(DESC_TILT + DESC_VALUE_SEPARATOR, "");
        String parrentID = splitField[5].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, "");
        String childID = splitField[6].replaceAll(DESC_CHILD_ID + DESC_VALUE_SEPARATOR, "");
        List<GLatLng> vertexs = Serializable.toLatlngs("[" + sVertexs.substring(1, sVertexs.length() - 1) + "]");
        double width = Double.valueOf(sWidth);
        GLatLng pivot = GLatLng.toJava(sPivot);
        double tilt = Double.valueOf(sTilt);
        double height = Double.valueOf(sHeight);
        //null = Main.Browser;
        GTaskSearchArea gTaskArea = new GTaskSearchArea(null, vertexs, pivot, width, tilt, height, taskSearchArea.getName(), parrentID, childID);
        gTaskArea.setArea(taskSearchArea.getArea());
        if (taskSearchArea.getTrapeziumTaskAreaID() != null && !taskSearchArea.getTrapeziumTaskAreaID().isEmpty()) {
            gTaskArea.setTrapeziumTaskAreaID(taskSearchArea.getTrapeziumTaskAreaID());
        } else if (taskSearchArea.getDriftTaskAreaID() != null && !taskSearchArea.getDriftTaskAreaID().isEmpty()) {
            gTaskArea.setDriftTaskAreaID(taskSearchArea.getDriftTaskAreaID());
        }
        return gTaskArea;
    }

    public synchronized void updatePivot() throws Exception {
        try {
            String result = null;
            //String result = Main.mapForm.exec(getPivotScript());
            this.pivot = GLatLng.toJava(result);
        } catch (Exception e) {
            throw new Exception("failed get GTaskSearchArea[" + ID + "] pivot");
        }
    }

    //unsafe method must validate in invoker method this
    public synchronized void updateVertexs() throws Exception {
        try {
            String result = null;
            //String result = Main.mapForm.exec(getVertexsScript());
            this.l = Serializable.toLatlngs("[" + result + "]");
        } catch (Exception e) {
            throw new Exception("failed get GTaskSearchArea[" + ID + "] Vertex");
        }
    }

    public synchronized void updateWidth() throws Exception {
        try {
            String result = null;
            //String result = Main.mapForm.exec(getWidthScript());
            this.width = Double.valueOf(result);
        } catch (Exception e) {
            throw new Exception("failed get GTaskSearchArea[" + ID + "] Width");
        }
    }

    public synchronized void updateTilt() throws Exception {
        try {
            String result = null;
            //String result = Main.mapForm.exec(getTiltScript());
            this.tilt = Double.valueOf(result);
        } catch (Exception e) {
            throw new Exception("failed get GTaskSearchArea[" + ID + "] Tilt");
        }
    }

    public synchronized void updateHeight() throws Exception {
        try {
            String result = null;
            //String result = Main.mapForm.exec(getHeightScript());
            this.height = Double.valueOf(result);
        } catch (Exception e) {
            throw new Exception("failed get GTaskSearchArea[" + ID + "] Height");
        }
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public synchronized List<GLatLng> getL() {
        return l;
    }

    public void setL(List<GLatLng> l) {
        this.l = l;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String newCollectionScript() {
        return "";
    }

    public IncidentAsset getIncidentAsset() {
        return incidentAsset;
    }

    public void setIncidentAsset(IncidentAsset incidentAsset) {
        this.incidentAsset = incidentAsset;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public GLatLng getPivot() {
        return pivot;
    }

    public void setPivot(GLatLng pivot) {
        this.pivot = pivot;
    }

    public int getRadiusType() {
        return radiusType;
    }

    public void setRadiusType(int radiusType) {
        this.radiusType = radiusType;
    }

    public double getTilt() {
        return tilt;
    }

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getTrackSpacing() {
        return trackSpacing;
    }

    public void setTrackSpacing(double trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    public String getParrentID() {
        return parrentID;
    }

    public void setParrentID(String parrentID) {
        this.parrentID = parrentID;
    }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public InstantiateType getState() {
        return state;
    }

    public void setState(InstantiateType state) {
        this.state = state;
    }

    public GLatLng getUnrotatePivot() {
        return unrotatePivot;
    }

    public void setUnrotatePivot(GLatLng unrotatePivot) {
        this.unrotatePivot = unrotatePivot;
    }

    public GLatLng getUnrotateStart() {
        return unrotateStart;
    }

    public void setUnrotateStart(GLatLng unrotateStart) {
        this.unrotateStart = unrotateStart;
    }

    public String getDriftTaskAreaID() {
        return driftTaskAreaID;
    }

    public void setDriftTaskAreaID(String driftTaskAreaID) {
        this.driftTaskAreaID = driftTaskAreaID;
    }

    public String getTrapeziumTaskAreaID() {
        return trapeziumTaskAreaID;
    }

    public void setTrapeziumTaskAreaID(String trapeziumTaskAreaID) {
        this.trapeziumTaskAreaID = trapeziumTaskAreaID;
    }

    private static LatLng toJava(String x) {
        try {
            x = x.substring(1, x.length() - 1);
            String[] x2 = x.split(",");
            return new LatLng(Double.parseDouble(x2[0]), Double.parseDouble(x2[1]));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Interface for creating task search area which come from Trapezium
     * TaskArea
     *
     * @see TrapeziumCalculationController
     */
    public static void interfaceCreateTaskAreaComeFromTrapezium(TrapeziumTaskArea tta) {
        //public static void interfaceCreateTaskAreaComeFromTrapezium(TrapeziumTaskArea tta){    
        try {
            List<LatLng> l = new ArrayList<>();
            l.add(toJava(tta.getSmallCoord1()));
            l.add(toJava(tta.getSmallCoord2()));
            l.add(toJava(tta.getBigCoord2()));
            l.add(toJava(tta.getBigCoord1()));
            
           
            //Mendapatkan titik tengah dari 4 Coordinate
            LatLng pivot = MapCalculation.getPivotFromMuchCoodninate(l);
            String parrentID = GTrapeziumSearchArea.getInstance().getParrentID();
            Double tilt = GTrapeziumSearchArea.getInstance().getHeading();
//            String childID = GPoly.generateParrentID();
            Double height = tta.getTaskAreaLength();
            Double width = tta.getLuasTaskArea() / height;
            //GTaskSearchArea taskSearchArea = new GTaskSearchArea(Main.getBrowser(), l, pivot, width, tilt, height, tta.getName(), parrentID, childID, tta.getTrapeziumTaskAreaID());
            //Main.mapForm.getController().getTaskSearchAreaController().removeExistingAndAddNew(taskSearchArea);
        } catch (Exception e) {
            logger.error(e);
            //JOptionPane.showMessageDialog(Main.sarMDI, "Proses Gagal", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * interface for remove task area one or more in navigator. which have
     * TrapeziumTaskArea id equals with some of Collection in <code>ids</code>
     * parameter
     *
     * @see TrapeziumCalculationController
     */
    public static void interfaceRemovesTaskSearchAreaSpecifiedByTrapeziumSearchAreaID(String[] ids) {
        /*
        if(Main.isMapFormReady()){
            Main.mapForm.getController().getTaskSearchAreaController().removeByTrapeziumTaskAreaID(ids);
        }
         */
    }

    @Override
    public String toJSON() {
        if (this.state == InstantiateType.VIEW_FORM_DATABASE || this.state == InstantiateType.TRAPEZIUM_COME_FROM) {
            return "{"
                    + "id:" + ID
                    + ",vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(this.l)
                    + ",pivot:" + this.pivot.toJavaScript()
                    + ",width:" + this.width
                    + ",height:" + this.height
                    + ",tilt:" + this.tilt
                    + "}";
        } else if (this.state == InstantiateType.CREATE_NEW) {
            return "{id:" + ID
                    + ",width:" + this.width
                    + ",height:" + this.height
                    + ",unrotateStart:" + this.unrotateStart.toJavaScript()
                    + ",unrotatePivot:" + this.unrotatePivot.toJavaScript()
                    + ")";
        }
        return null;
    }
}
