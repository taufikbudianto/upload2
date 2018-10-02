/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.util.List;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.service.map_js.GPoly;
import prosia.basarnas.service.map_js.Serializable;

/**
 *
 * @author Aris
 */
public class GSearchArea extends GPoly<SearchArea>{
    public static String CURRENT_SEARCH_AREA_POLYGON_SCRIPT = "current_SearchArea.polygon";
    public static String CURRENT_SEARCH_AREA_IS_NULL ="0";
    public static String CURRENT_SEARCH_AREA_HAS_NOT_POLYGON ="1";
    public static String CURRENT_SEARCH_AREA_HAS_POLYGON ="2";
    
    public final static String DESC_AREA = "Area";
    public final static String DESC_START = "Start";
    
    
    private String patternID;
    private List<GLatLng> l;
    private GLatLng start;
    private String area;
    private static int AUTO_INCEREMENT;
    private String shape;
    private Double radius;
    private String name = "Untitled Search Area";

    
    
    public static GLatLng getStartFromSearchAreas(SearchArea searchArea){
        String area = searchArea.getArea();
        if(area == null || area.equals("")){
            return null;
        }else{
            area = area.substring(1, area.length() -1);
            area = area.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR))[1]; 
            area = area.replace(DESC_START + " " + DESC_VALUE_SEPARATOR + " ", "");
            area = area.substring(1, area.length()-1);     // hilangkan tanda ( dan ) 
            String[] stringLatlng = area.split(","); // separating latitude and longitude value 
            return  new GLatLng(stringLatlng[0], stringLatlng[1]);
        }
    } 
    
    public static List<GLatLng> getVertexsFromSearchArea(SearchArea searchArea){
        String area = searchArea.getArea();
        if(area == null || area.equals("")){
            return null;
        }else{
            area = area.split(Serializable.toStringRegex(DESC_BEGIN_COLLECTION))[1];
            area = area.split(Serializable.toStringRegex(DESC_END_COLLECTION))[0];
            List<GLatLng> latLngs = Serializable.toLatlngs(Serializable.ARRAY_BEGIN + area + Serializable.ARRAY_END); 
            return latLngs;
        }
    }
    
    public GSearchArea() {
        ID = ++AUTO_INCEREMENT;
    }
   /* 
    public GSearchArea(Browser browser) {
        super(browser);
        ID = ++AUTO_INCEREMENT;
    }
    */
    
    /**
     *  metode yang saat ini dipakai sebagai interface ke JavaScript 
     */
    public static String removeAndCreateCurrentSearchArea(SearchArea searchArea){
        List<GLatLng> vertexs = getVertexsFromSearchArea(searchArea);
        StringBuilder script = new StringBuilder("removeAndCreateCurrentSearchArea(").append(getStartFromSearchAreas(searchArea).toJavaScript())
                .append(","); 
        script.append(JavaScriptBase.<GLatLng>toArrayJavaScript(vertexs)).append(",")
                .append("'").append(searchArea.getParrentID()).append("')");
        return script.toString();
    }
    
    
    
    public static String removeCurrentSearchAreaScript(){
        return "removeCurrentSearchArea()";
    }
    
    public static String getCurrentSearchAreaVertexsScript(){
        return "toString(current_SearchArea.l)";
    }
    
    public static String getCurrentSearchAreaStartLatitudeScript(){
        return "current_SearchArea.start.lng()";
    }
    
    public static String getCurrentSearchAreaStartLongitudeScript(){
        return "current_SearchArea.start.lat()";
    }
    
    public static String getCurrentSearchAreaParrentIDScript(){
        return "toString(current_SearchArea.parrentID)";
    }
    
    public static String getCurrentSearchAreaShapeScript(){
        return "toString(current_SearchArea.shape)";
    }
    
    public static String getCurrentSearchAreaRadiusScript(){
        return "toString(current_SearchArea.radius)";
    }
    
    
    @Override
    public String writeScript() {
            return new StringBuilder("createSearchArea(").append(start.toJavaScript()).append(",")
                    .append(ID).append(",")
                    .append(JavaScriptBase.<GLatLng>toArrayJavaScript(this.l)).append(",")
                    .append("'").append(this.getPatternID()).append("',")
                    .append("'").append(this.getShape()).append("',")
                    .append(this.getRadius()).append(")").toString();
    }

    @Override
    public String removeScript() {
        return "removeSearchArea(" + ID + ")";
    }
    
    
    public static String clearScript(){
        return "clearSearchArea()";
    }
    
    
    //public static String createAreaFormCurrentSearchArea(Browser browser){
    public static String createAreaFormCurrentSearchArea(){
        //String vertexs = Main.mapForm.exec(getCurrentSearchAreaVertexsScript());
        String vertexs = getCurrentSearchAreaVertexsScript();
        //start is first of vertexs
        String start = vertexs.replaceAll(Serializable.toStringRegex("),"), ")" + DESC_FIELD_SEPARATOR).split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR))[0];
        String area = new StringBuilder(DESC_OPEN).append( 
                DESC_AREA).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_COLLECTION)
                .append(vertexs).append(DESC_END_COLLECTION).append(DESC_FIELD_SEPARATOR)
                .append(DESC_START).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(start).append(
                DESC_CLOSE).toString();
        return area;
    }
    
    
    /**
     * Membuat Object GSearchArea baru berdasarkan String area yang mendeskripsikan 
     * Bentuknya dan Posisinya
     */
    public static GSearchArea searchAreaToGSearchArea(SearchArea searchArea){
        GSearchArea gSearchArea = new GSearchArea();
        gSearchArea.setStart(GSearchArea.getStartFromSearchAreas(searchArea));
        gSearchArea.setArea(searchArea.getArea()); 
        gSearchArea.setShape(searchArea.getShape());
        gSearchArea.setRadius(searchArea.getRadius());
        gSearchArea.setL(GSearchArea.getVertexsFromSearchArea(searchArea)); 
        gSearchArea.setName(searchArea.getName());
        return gSearchArea;
    }
    

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public synchronized List<GLatLng> getL() {
        //String x = Main.mapForm.exec("toString(SEARCH_AREA["+ ID +"].l)");
        String x = "toString(SEARCH_AREA["+ ID +"].l)";
        List<GLatLng> result = Serializable.toLatlngs(Serializable.ARRAY_BEGIN + x + Serializable.ARRAY_END);
        setL(result);
        return result;
    }
    
    
    //public static synchronized List<GLatLng> getCurrentSearchAreaL(Browser browser) {
    public static synchronized List<GLatLng> getCurrentSearchAreaL() {
        System.out.println("Current Search Area ID ");
        //String x = Main.mapForm.exec("toString(current_SearchArea.l)");
        String x = "toString(current_SearchArea.l)";
        List<GLatLng> result = Serializable.toLatlngs(Serializable.ARRAY_BEGIN + x + Serializable.ARRAY_END);
        return result;
    }
    
    
    public void setL(List<GLatLng> l) {
        this.l = l;
    }

    public GLatLng getStart() {
        return start;
    }

    public void setStart(GLatLng start) {
        this.start = start;
    }

    

    @Override
    public String newCollectionScript() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPatternID() {
        return patternID;
    }

    public void setPatternID(String patternID) {
        this.patternID = patternID;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
    
    

    @Override
    public String toJSON() {
        return "{start:"+ start.toJavaScript() 
                +",id:" + ID
                +",vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(this.l) 
                +",parrentID:'" + this.getPatternID() +"'}";
    }

    @Override
    public String createConcurrent() {
        return "{object:"+ toJSON() +", gen:generateSearchArea}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeSearchArea}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
