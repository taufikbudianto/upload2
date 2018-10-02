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
public class TSNPattern extends GSearchPattern{
    public String trackSpacing;
    public String searchLeg;
    public String heading;
    private static final String DESC_TRACK_SPACING = "Trace_Spacing";
    private static final String DESC_SEARCH_LEG = "Search_Leg";
    private static final String DESC_PIVOT = "Pivot";
    
    //helper desc
    private static final String DESC_START = "Start";
    
    

    public TSNPattern(String name, GLatLng start, String trackSpacing, String searchLeg, String heading, GLatLng pivot, Object browser, List<GLatLng> l, Color color, String hexColor, String parrentID) {
        super(name, l, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.trackSpacing = trackSpacing;
        this.searchLeg = searchLeg;
        this.startPosition = start;
        this.heading = heading;
        this.pivot = pivot;
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.TSN);
        //setIcon(NavigatorIconCollection.TSN_ICON_32);
        setConstructorType(ConstructorType.CREATE_NEW);
    }
    
    
    public TSNPattern(String name, GLatLng start, String trackSpacing, String searchLeg, String heading, Object browser, Color color, String hexColor, String parrentID) {
        super(name, browser, parrentID); 
        this.color = color;
        this.hexColor = hexColor;
        this.trackSpacing = trackSpacing;
        this.searchLeg = searchLeg;
        this.startPosition = start;
        this.heading = heading;
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.TSN);
        //setIcon(NavigatorIconCollection.TSN_ICON_32);
        setConstructorType(ConstructorType.CREATE_NEW);
    }
    
    

    @Override
    public synchronized String writeScript() {
        if(this.constructorType == ConstructorType.CREATE_NEW){
            return new StringBuilder("createTSN(")
                .append(ID).append(",")
                .append(startPosition.toJavaScript()).append(",")
                .append(trackSpacing).append(",")
                .append(searchLeg).append(",")
                .append(heading).append(",null,null,'")
                .append(hexColor).append("')").toString();
        }else{
            return new StringBuilder("createTSN(")
                .append(ID).append(",")
                .append(startPosition.toJavaScript()).append(",")
                .append(trackSpacing).append(",")
                .append(searchLeg).append(",")
                .append(heading).append(",")
                .append(pivot.toJavaScript()).append(",")
                .append(JavaScriptBase.<GLatLng>toArrayJavaScript(l)).append(",")
                .append("'").append(hexColor).append("'") 
                .append(")").toString();
        }
    }

    @Override
    public synchronized String createDescription() {
        this.getStartPosition();
        this.getPivot();
        String des = new StringBuilder(DESC_OPEN)
                .append(DESC_TRACK_SPACING).append(" ").append(DESC_VALUE_SEPARATOR)
                .append(" ").append(this.getTraceSpacing()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_SEARCH_LEG).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                .append(this.getSearchLeg()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEADING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                .append(this.getHeading()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE)
                .append(pivot.getLat()).append(",").append(pivot.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_START).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE)
                .append(startPosition.getLat()).append(",").append(startPosition.getLng()).append(DESC_END_VALUE_COUPLE)
                .append(DESC_FIELD_SEPARATOR).append(DESC_HEX_COLOR).append(" ")
                .append(DESC_VALUE_SEPARATOR).append(" ").append(this.hexColor).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PARRENT_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID()).append(DESC_CLOSE).toString();
        return des;
    }
    
    

    public static TSNPattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser) {
        String description = pattern.getDescription();
        if (description == null || description.equalsIgnoreCase(CommonConstant.EMPTY_STRING)) {
            return null;
        }
        description = description.substring(1, description.length() - 1);
        description = description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] splitField = description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String trackSpacing = splitField[0].replaceAll(DESC_TRACK_SPACING + DESC_VALUE_SEPARATOR, "");
        String searchLeg = splitField[1].replaceAll(DESC_SEARCH_LEG + DESC_VALUE_SEPARATOR, "");
        String heading = splitField[2].replaceAll(DESC_HEADING + DESC_VALUE_SEPARATOR, "");
        String sPivot = splitField[3].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, "");
        String sStart = splitField[4].replaceAll(DESC_START + DESC_VALUE_SEPARATOR, "");
        String hexColor = splitField[5].replaceAll(DESC_HEX_COLOR + DESC_VALUE_SEPARATOR, "");
        String parrentID = splitField[6].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, "");
        GLatLng pivot = GLatLng.toJava(sPivot);
        GLatLng start = GLatLng.toJava(sStart);
        String name = pattern.getName();
        Color color;
        try {
            //color = Controller.hexToColor(hexColor);
            color = Color.YELLOW;
        } catch (Exception e) {
            color = Color.BLUE;
            hexColor = "ff0000ff";
        }
        //parameter Start tidak diperlukan ketika ingin menampilkan Search Pattern pada Map
        TSNPattern tsnPattern = new TSNPattern(name, start, trackSpacing, searchLeg, heading, pivot, browser, gLatLngs, color, hexColor, parrentID);
        tsnPattern.setSearchPatternID(pattern.getSearchPatternId());
        return tsnPattern;
    }

    

    public synchronized String getTraceSpacing() {
        String result = "toString(SEARCH_PATTERN[" + ID + "].trackSpacing)";
        setTrackSpacing(result);
        return result;
    }

    public synchronized String getSearchLeg() {
        String result = "toString(SEARCH_PATTERN[" + ID + "].searchLeg)";
        setSearchLeg(result);
        return result;
    }

    public synchronized String getHeading() {
        String result = "toString(SEARCH_PATTERN[" + ID + "].heading)";
        return result;
    }

    public void setSearchLeg(String searchLeg) {
        this.searchLeg = searchLeg;
    }

    public void setTrackSpacing(String trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    @Override
    public String toJSON(){
        if(this.constructorType == ConstructorType.CREATE_NEW){
            return "{"
                + "id:" + ID + ","
                + "start:" + startPosition.toJavaScript() + ","
                + "trackSpacing:" + trackSpacing + ","
                + "searchLeg:" + searchLeg + ","
                + "heading:" + heading + ","
                + "pivot:null,"
                + "vertexs:null," 
                + "color:'"+ hexColor + "'" 
                + "}";
        }else{
            return "{"
                + "id:" + ID + ","
                + "start:" + startPosition.toJavaScript() + ","
                + "trackSpacing:" + trackSpacing + ","
                + "searchLeg:" + searchLeg + ","
                + "heading:" + heading + ","
                + "pivot:" + pivot.toJavaScript() + ","
                + "vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(l) +","
                + "color:'" + hexColor + "'" 
                + "}";
        }
    }

    @Override
    public String createConcurrent() {
        return "{object:"+ toJSON() +", gen:generateTSNPattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeSearchPattern}";
    }
}
