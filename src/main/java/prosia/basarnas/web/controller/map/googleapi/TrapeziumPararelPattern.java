/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.awt.Color;
import java.util.List;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.service.CommonConstant;
import prosia.basarnas.service.map_js.GSearchPattern;
import prosia.basarnas.service.map_js.Serializable;

/**
 *
 * @author Aris
 */
public class TrapeziumPararelPattern extends GSearchPattern{
    private String minSearchLeg;
    private String width;
    private String heading;
    private String maxSearchLeg;
    private String trackSpacing;
    
    private static String DESC_MIN_SEARCH_LEG = "Minimum_Search_Leg"; 
    private static String DESC_MAX_SEARCH_LEG = "Maximum_Search_Leg"; 
    private static String DESC_TRACK_SPACING = "Track_Spacing"; 
    private static String DESC_WIDTH = "Width"; 
    private static String DESC_PIVOT = "Pivot"; 
    
    public static String _description = "";
    public static String _maxSearchLeg = "";
    public static String _minSearchLeg = "";
    public static String _width = "";
    public static String _trackSpacing = "";
    public static String _heading = "";
    public static String _sPivot = "";
    public static String _hexColor = "";
    public static String _parrentID = "";
    public static GLatLng _pivot = null;
    public static String _name = "";
    public static Color _color = null;
    
    public TrapeziumPararelPattern(String nama, Object browser, String minSearchLeg, String width, String heading, String maxSearchLeg, String trackSpacing, GLatLng pivot, List<GLatLng> vertexs, Color color, String hexColor, String parrentID) {
        super(nama, browser, parrentID);
        
        this.color = color;
        this.hexColor = hexColor;
        this.l = vertexs;
        this.startPosition = vertexs.get(0);  
        this.minSearchLeg = minSearchLeg;
        this.heading = heading;
        this.width = width;
        this.trackSpacing = trackSpacing;
        this.maxSearchLeg = maxSearchLeg;
        this.pivot = pivot;
        ID = ++ AUTO_INCEREMENT;
        this.constructorType = ConstructorType.LOAD_FROM_DATABASE;
        setType(GSearchPattern.TRAPEZIUM);
        //setIcon(NavigatorIconCollection.TRAPEZ_ICON_32);
    }
    
    public TrapeziumPararelPattern(String nama, Object browser, GLatLng startTrapezArea, String minSearchLeg, String width, String trackSpacing, String heading, String maxSearchLeg, Color color, String hexColor, String parrentID) {
        super(nama, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.startPosition = startTrapezArea;
        this.minSearchLeg = minSearchLeg;
        this.heading = heading;
        this.width = width;
        this.trackSpacing = trackSpacing;
        this.maxSearchLeg = maxSearchLeg;
        ID = ++ AUTO_INCEREMENT;
        this.constructorType = ConstructorType.CREATE_NEW;
        setType(GSearchPattern.TRAPEZIUM);
        //setIcon(NavigatorIconCollection.TRAPEZ_ICON_32);
    }
    
    
    @Override
    public String writeScript() {
        if(this.constructorType == ConstructorType.LOAD_FROM_DATABASE){
            return "createTrapeziumPararelPattern("+ ID +
                    ",'"+ getName() + 
                    "', null,"+ 
                    minSearchLeg +","+ 
                    width +","+ 
                    trackSpacing +","+ 
                    heading +","+ 
                    maxSearchLeg +","+
                    JavaScriptBase.<GLatLng>toArrayJavaScript(l) + "," +
                    "'" + hexColor + "'" +
                    ")";
        }else{
            return new StringBuilder("createTrapeziumPararelPattern(").append(ID).append(",'").append(getName())
                    .append("',").append(startPosition.toJavaScript()).append(",").append(minSearchLeg).append(",")
                    .append(width).append(",")
                    .append(trackSpacing).append(",")
                    .append(heading).append(",")
                    .append(maxSearchLeg).append(",null,'").append(hexColor).append("')").toString();
        }
    }

    @Override
    public void updatePropertyWhenSwicthing() {
        this.getStartPosition();
    }
    
    public String createDescription(){
        this.getPivot();
        return new StringBuilder(DESC_OPEN).append(DESC_MAX_SEARCH_LEG).append(" )").append(DESC_VALUE_SEPARATOR)
                .append(" ").append(this.maxSearchLeg).append(DESC_FIELD_SEPARATOR).append(DESC_MIN_SEARCH_LEG)
                .append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.minSearchLeg).append(DESC_FIELD_SEPARATOR)
                .append(DESC_WIDTH).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.width).append(DESC_FIELD_SEPARATOR)
                .append(DESC_TRACK_SPACING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.trackSpacing).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEADING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.heading).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                .append(DESC_BEGIN_VALUE_COUPLE).append(pivot.getLat()).append(",").append(pivot.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEX_COLOR).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.hexColor).append(DESC_FIELD_SEPARATOR).append(DESC_PARRENT_ID).append(" ")
                .append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID())
                .append(DESC_CLOSE).toString();
    }
    
    
    public static TrapeziumPararelPattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser) {
        _description = pattern.getDescription();
        if (_description == null || _description.equalsIgnoreCase(CommonConstant.EMPTY_STRING)) {
            return null;
        }
        _description = _description.substring(1, _description.length() - 1);
        _description = _description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] splitField = _description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        _maxSearchLeg = splitField[0].replaceAll(DESC_MAX_SEARCH_LEG + DESC_VALUE_SEPARATOR, "");
        _maxSearchLeg = _maxSearchLeg.split(":")[1];
        _minSearchLeg = splitField[1].replaceAll(DESC_MIN_SEARCH_LEG + DESC_VALUE_SEPARATOR, "");
        _width = splitField[2].replaceAll(DESC_WIDTH + DESC_VALUE_SEPARATOR, "");
        _trackSpacing = splitField[3].replaceAll(DESC_TRACK_SPACING + DESC_VALUE_SEPARATOR, "");
        
        _heading = splitField[4].replaceAll(DESC_HEADING + DESC_VALUE_SEPARATOR, "");
        _sPivot = splitField[5].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, "");
        _hexColor = splitField[6].replaceAll(DESC_HEX_COLOR + DESC_VALUE_SEPARATOR, "");
        _parrentID = splitField[7].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, "");
        _pivot = GLatLng.toJava(_sPivot);
        _name = pattern.getName();
        
        try {
            //_color = Controller.hexToColor(_hexColor);
            _color = Color.YELLOW;
        } catch (Exception e) {
            _color = Color.BLUE;
            _hexColor = "ff0000ff";
        }
        TrapeziumPararelPattern trapezPattern = new TrapeziumPararelPattern(pattern.getName(), browser, _minSearchLeg, _width, _heading, _maxSearchLeg, _trackSpacing, _pivot, gLatLngs, _color, _hexColor, _parrentID);
        trapezPattern.setSearchPatternID(pattern.getSearchPatternId());
        return trapezPattern;
    }
    
    public void clearStaticVariable()
    {   
        _description = "";
        _maxSearchLeg = "";
        _minSearchLeg = "";
        _width = "";
        _trackSpacing = "";
        _heading = "";
        _sPivot = "";
        _hexColor = "";
        _parrentID = "";
        _pivot = null;
        _name = "";
        _color = null;
    }
    
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMaxSearchLeg() {
        return maxSearchLeg;
    }

    public void setMaxSearchLeg(String maxSearchLeg) {
        this.maxSearchLeg = maxSearchLeg;
    }

    public String getMinSearchLeg() {
        return minSearchLeg;
    }

    public void setMinSearchLeg(String minSearchLeg) {
        this.minSearchLeg = minSearchLeg;
    }

    public String getTrackSpacing() {
        return trackSpacing;
    }

    public void setTrackSpacing(String trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    
    @Override
    public String toJSON() {
        if(this.constructorType == ConstructorType.LOAD_FROM_DATABASE){
            return "{id:"+ ID + "," +
                    "name:'"+ getName() +"',"+
                    "start:null,"+ 
                    "minSearchLeg:" + minSearchLeg +","+ 
                    "width:"+ width +","+ 
                    "trackSpacing:" + trackSpacing +","+ 
                    "heading:"+ heading +","+ 
                    "maxSearchLeg:"+maxSearchLeg +","+
                    "vertexs:"+ JavaScriptBase.<GLatLng>toArrayJavaScript(l) + "," +
                    "color:'" + hexColor + "'" +
                    "}";
        }else{
            return "{id:"+ ID + "," +
                    "name:'"+ getName() +"'," +
                    "start:"+ startPosition.toJavaScript() + "," + 
                    "minSearchLeg:" + minSearchLeg +","+ 
                    "width:"+ width +","+ 
                    "trackSpacing:" + trackSpacing +","+ 
                    "heading:"+ heading +","+ 
                    "maxSearchLeg:"+maxSearchLeg +","+
                    "vertexs:null," +
                    "color:'" + hexColor + "'" +
                    "}";
        }
        
        
    }

    @Override
    public String createConcurrent() {
        return "{object:'"+ toJSON() +"', gen:generateFreeDefinePattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeSearchPattern}";
    }
}
