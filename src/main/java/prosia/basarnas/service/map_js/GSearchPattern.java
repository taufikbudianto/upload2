/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.Waypoint;
import prosia.basarnas.web.controller.map.googleapi.FreeDefinePattern;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.PararelPattern;
import prosia.basarnas.web.controller.map.googleapi.SectorPattern;
import prosia.basarnas.web.controller.map.googleapi.SquarePattern;
import prosia.basarnas.web.controller.map.googleapi.TSNPattern;
import prosia.basarnas.web.controller.map.googleapi.TSRPattern;
import prosia.basarnas.web.controller.map.googleapi.TrapeziumPararelPattern;

/**
 *
 * @author Aris
 */
public abstract class GSearchPattern extends GPoly<IncidentSearchPattern> {
    protected ConstructorType constructorType;
    protected Color color;
    protected String hexColor;        
    private String parrentID;
    private String searchPatternID;
    
    
    public static enum ConstructorType{
        CREATE_NEW,
        LOAD_FROM_DATABASE
    }
    
    
    public GSearchPattern(String name, List<GLatLng> l, Object browser, String parrentID) {
       //super(browser);
       this.name = name;
       this.l = l;
       this.parrentID = parrentID;
    }

    public GSearchPattern(String name, Object browser, String parrentID) {
        //super(browser); 
        this.name = name;
        this.parrentID = parrentID;
    }

    
    
    
    
    /**
     * sangat diperlukan ketika semua Search Pattern pada map dihapus
     * untuk diset menjadi 0
     */
    protected static int AUTO_INCEREMENT;
    
    public GLatLng startPosition;
    protected List<GLatLng> l;
    protected GLatLng pivot;
    private ImageIcon icon;
    private String name;
    private String type;
    private IncidentAsset incidentAsset;
    
    public static final String SQUARE = "Expanding Square Search";
    public static final String SECTOR = "Sector Search";
    public static final String PARAREL = "Pararel Search";
    public static final String FREE_DEFINE = "Free Define Search";
    public static final String TSR = "Track Search Return Search";
    public static final String TSN = "Track Search not-Return Search";
    public static final String TRAPEZIUM = "Trapezium Pararel Search";
    public static final String CUSTOM_AREA = "Custom Search Area";
    public static final String DESC_HEADING = "Heading";
    public static final String DESC_HEX_COLOR = "Hex_Color";
    
    
    

    @Override
    public String removeScript() {
        return "removeSearchPattern("+ ID +")";
    }
    
    public void updatePropertyWhenSwicthing(){
        System.out.println("updating search pattern " + getType());
        this.getStartPosition();
    }

    
    public static GSearchPattern createGSearchPatternFromSearchPatern(IncidentSearchPattern searchPattern, Object browser){
        GSearchPattern result = null;
//        SearchBean searchBean = new SearchBean();
//        searchBean.setSearchColumn("searchPattern.searchPatternID");
//        searchBean.setSearchValue(searchPattern.getSearchPatternID());
//        searchBean.setOrderBy("sequence");
//        List<Waypoint> waypoints = WaypointES.findWaypointByField(searchBean).getList();
//        List<Waypoint> waypoints = searchPattern.getWaypoints();
        List<GLatLng> gLatLngs = new ArrayList<GLatLng>();
        //Convert List Waypoint to List GLatLng
//        for(Waypoint waypoint : waypoints){
//            GLatLng gLatLng = Serializable.waypointToLatLng(waypoint);
//            gLatLngs.add(gLatLng);
//        }
        
        if(searchPattern.getType().equals(SQUARE)){
            result = SquarePattern.createGSearchPatternFromSearchPatern(searchPattern, gLatLngs, browser);
        }else if(searchPattern.getType().equals(SECTOR)){
            result = SectorPattern.createGSearchPatternFromSearchPatern(searchPattern, gLatLngs, browser);
        }else if(searchPattern.getType().equals(PARAREL)){
            result = PararelPattern.createGSearchPatternFromSearchPatern(searchPattern, gLatLngs, browser);
        }else if(searchPattern.getType().equals(FREE_DEFINE)){
            result = FreeDefinePattern.createGSearchPatternFromSearchPatern(searchPattern,  gLatLngs, browser);
        }else if(searchPattern.getType().equals(TSR)){
            result = TSRPattern.createGSearchPatternFromSearchPatern(searchPattern,  gLatLngs, browser);
        }else if(searchPattern.getType().equals(TSN)){
            result = TSNPattern.createGSearchPatternFromSearchPatern(searchPattern,  gLatLngs, browser);
        }else if(searchPattern.getType().equals(TRAPEZIUM)){
            result = TrapeziumPararelPattern.createGSearchPatternFromSearchPatern(searchPattern,  gLatLngs, browser);
        }
        return result;
    }
    
    
    
    
    public String createDescription(){return "";}

    @Override
    public String newCollectionScript() {
        return "setNewPatterns()";
    }
    
    
    
    
    
    public static String clearElementScript() {
        return "removeAllObjectMarker(PATTERN)";
    }
    
    public String inFocusScript(){
        return "SEARCH_PATTERN["+ ID +"].inFocus()";
    }

    public IncidentSearchPattern createSearchPattern(){
        IncidentSearchPattern pattern = new IncidentSearchPattern();
        pattern.setName(this.getName());
        pattern.setType(this.getType());
        pattern.setIncidentAsset(this.getIncidentAsset()); 
        pattern.setDescription(this.createDescription());
        return pattern;
    }
    
    
    
    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public IncidentAsset getIncidentAsset() {
        return incidentAsset;
    }

    public void setIncidentAsset(IncidentAsset incidentAsset) {
        this.incidentAsset = incidentAsset;
    }


    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
   

    public synchronized List<GLatLng> getL() {
        String x = "toString(SEARCH_PATTERN["+ ID +"].l)";
        List<GLatLng> result = Serializable.toLatlngs("[" + x + "]");
        setL(result);
        return result;
    }

    public void setL(List<GLatLng> l) {
        this.l = l;
    }

    public GLatLng getStartPosition() {
        String hasil = "toString(SEARCH_PATTERN["+ ID +"].start)";
        GLatLng result  = GLatLng.toJava(hasil);
        System.out.println("hasil update properties : " +  hasil );
        if(result != null){
            setStartPosition(result);
        }
        return result;
    }

    public void setStartPosition(GLatLng startPosition) {
        this.startPosition = startPosition;
    }



    
    public synchronized GLatLng getPivot() {
        String hasil = "toString(SEARCH_PATTERN["+ ID +"].pivot)";
        GLatLng result =  GLatLng.toJava(hasil);
        setPivot(result);
        return result;
    }
    
    
    public GLatLng getOrginalPivot(){
        return pivot;
    }
    
    
    public void setPivot(GLatLng pivot) {
        this.pivot = pivot;
    }

    public static int getAUTO_INCEREMENT() {
        return AUTO_INCEREMENT;
    }

    public static void setAUTO_INCEREMENT(int AUTO_INCEREMENT) {
        GSearchPattern.AUTO_INCEREMENT = AUTO_INCEREMENT;
    }

    public String variableScript(){
        return "SEARCH_PATTERN["+ ID +"]";
    }

    public ConstructorType getConstructorType() {
        return constructorType;
    }

    public void setConstructorType(ConstructorType constructorType) {
        this.constructorType = constructorType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getParrentID() {
        return parrentID;
    }

    public void setParrentID(String parrentID) {
        this.parrentID = parrentID;
    }

    public String getSearchPatternID() {
        return searchPatternID;
    }

    public void setSearchPatternID(String searchPatternID) {
        this.searchPatternID = searchPatternID;
    }
}
