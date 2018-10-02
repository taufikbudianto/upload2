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
public class SquarePattern extends GSearchPattern {
    public String trackSpacing;
    public String searchRadius;
    
    public String heading;
    //arah rotasi Square Pattern pertama kali nilai dalam bentuk derajat
    
    private static final String DESC_TRACK_SPACING = "Trace_Spacing";
    private static final String DESC_SEARCH_RADIUS = "Search_Radius";
    private static final String DESC_PIVOT = "Pivot";
     
    public SquarePattern(String name, Object browser, String parrentID) {
        super(name, browser, parrentID);
    }
    public SquarePattern(String name, String trackSpacing, String searchRadius, GLatLng pivot, String heading, Object browser, List<GLatLng> l, Color color, String hexColor, String parrentID) {
        super(name, l, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.trackSpacing = trackSpacing;
        this.searchRadius = searchRadius;
        this.pivot = pivot;
        this.startPosition = pivot;
        this.heading = heading; 
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.SQUARE);
        //setIcon(NavigatorIconCollection.SQUARE_ICON_32);
        setConstructorType(ConstructorType.LOAD_FROM_DATABASE);
    }
    
    
    public SquarePattern(String name, GLatLng pivot, String trackSpacing, String searchRadius, String heading, Object browser, Color color, String hexColor, String parrentID) {
        super(name, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.trackSpacing = trackSpacing;
        this.searchRadius = searchRadius;
        this.pivot = pivot;
        this.startPosition = pivot;
        this.heading = heading; 
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.SQUARE);
        //setIcon(NavigatorIconCollection.SQUARE_ICON_32);
        setConstructorType(ConstructorType.CREATE_NEW);
    }

    
    @Override
    public synchronized String writeScript() {
        if(this.constructorType == ConstructorType.CREATE_NEW){
            return new StringBuilder("createSquare(")
                .append(ID).append(",")
                .append(trackSpacing).append(",")
                .append(searchRadius).append(",")
                .append(pivot.toJavaScript()).append(",")
                .append(heading).append(",null,'").append(hexColor).append("')").toString();
        }else{
            return new StringBuilder("createSquare(").append(ID).append(",")
               .append(trackSpacing).append(",")
                .append(searchRadius).append(",")
                .append(pivot.toJavaScript()).append(",")
                .append(heading).append(",")
                .append(JavaScriptBase.<GLatLng>toArrayJavaScript(l)).append(",'").append(hexColor).append("')").toString();
        }
    }

    @Override
    public void updatePropertyWhenSwicthing() {
        System.out.println("update " + getType());
        this.getPivot();
    }

    
    
    
    
    @Override
    public synchronized String createDescription() {
        this.getPivot();
        String des = new StringBuilder(DESC_OPEN)
                .append(DESC_TRACK_SPACING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getTraceSpacing())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_SEARCH_RADIUS).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getSearchRadius())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEADING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getHeading()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE).append(pivot.getLat()).append(",")
                .append(pivot.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEX_COLOR).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.hexColor).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PARRENT_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID())
                .append(DESC_CLOSE).toString();
        return des;
    }

    public static SquarePattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser) {
        String description = pattern.getDescription();
        if (description == null || description.equalsIgnoreCase(CommonConstant.EMPTY_STRING)) {
            return null;
        }
        description = description.substring(1, description.length() - 1);
        description = description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] splitField = description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String trackSpacing = splitField[0].replaceAll(DESC_TRACK_SPACING + DESC_VALUE_SEPARATOR, "");
        String searchRadius = splitField[1].replaceAll(DESC_SEARCH_RADIUS + DESC_VALUE_SEPARATOR, "");
        String heading = splitField[2].replaceAll(DESC_HEADING + DESC_VALUE_SEPARATOR, "");
        String sPivot = splitField[3].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, "");
        String hexColor = splitField[4].replaceAll(DESC_HEX_COLOR + DESC_VALUE_SEPARATOR, "");
        String parrentID = splitField[5].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, "");
        Color color;
        try {
            System.out.println(hexColor);
            //color = Controller.hexToColor(hexColor);
            color = Color.YELLOW;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            color = Color.BLUE;
            hexColor = "ff0000ff";
        }
        GLatLng pivot = GLatLng.toJava(sPivot);
        String name = pattern.getName();
        SquarePattern sqPattern = new SquarePattern(name, trackSpacing, searchRadius, pivot, heading, browser, gLatLngs, color, hexColor, parrentID);
        sqPattern.setSearchPatternID(pattern.getSearchPatternId());
        return sqPattern;
    }
    

    public static void main(String[] args) {
        String des = DESC_OPEN
                + DESC_TRACK_SPACING + " " + DESC_VALUE_SEPARATOR + " " + 2 + DESC_FIELD_SEPARATOR
                + DESC_SEARCH_RADIUS + " " + DESC_VALUE_SEPARATOR + " " + 20 + DESC_FIELD_SEPARATOR
                + DESC_PIVOT + " " + DESC_VALUE_SEPARATOR + " " + 20.090 + "," + 30.08992
                + DESC_CLOSE;
        System.out.println(des);
        IncidentSearchPattern pattern = new IncidentSearchPattern();
        pattern.setDescription(des);
        SquarePattern squarePattern = createGSearchPatternFromSearchPatern(pattern, null, null);
    }

    /**
     * @return the smallLine
     */
    public synchronized String getTraceSpacing() {
        String result = "toString(SEARCH_PATTERN[" + ID + "].trackSpacing)";
        setTrackSpacing(result);
        return result;
    }

    /**
     * @return the largeLine
     */
    public synchronized String getSearchRadius() {
        String result = "toString(SEARCH_PATTERN[" + ID + "].radius)";
        double sRadius = Double.valueOf(result) / 2;
        setSearchRadius(sRadius+"");
        return result;
    }

    
    
    @Override
    public GLatLng getStartPosition() {
        return this.getPivot();
    }
    

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
    }

    public void setTrackSpacing(String trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    
    
    //Setter Getter helper for disable rotate SearchPattern
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    @Override
    public String toJSON() {
        if(this.constructorType == ConstructorType.CREATE_NEW){
            return "{" +
                "id:" + ID + ","
                + "trackSpacing:" + trackSpacing + "," 
                + "radius:" + searchRadius + ","
                + "pivot:" + pivot.toJavaScript()+ ","
                + "heading:" + heading + ","
                + "vertexs:null,"
                + "color:'" + hexColor + "'"
                + "}";
        }else{
            return "{" +
                "id:" + ID + ","
                + "trackSpacing:" + trackSpacing + "," 
                + "radius:" + searchRadius + ","
                + "pivot:" +pivot.toJavaScript()+ ","
                + "heading:" + heading + ","
                + "vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(l) + ","
                + "color:'" + hexColor + "'" 
                + "}";
        }
    }

    @Override
    public String createConcurrent() {
        return "{object:"+ toJSON() +",gen:generateSquarePattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +",gen:removeSearchPattern}";
    }
}
