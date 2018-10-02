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
public class PararelPattern extends GSearchPattern{
    public String trackSpacing;
    public String heading;
    public String searchLeg;
    public String width;
    private final static String DESC_WIDTH = "Width";
    private final static String DESC_SEARCH_LEG = "Search_Leg";
    private final static String DESC_TRACK_SPACING = "Track_Spacing";
    private final static String DESC_PIVOT = "Pivot";
    private final static String DESC_START = "Start";

    public PararelPattern(String name, Object browser, String parrentID) {
        super(name, browser, parrentID);
    }

    public PararelPattern(String name, GLatLng start, String searchLeg, String width, String trackSpacing, String heading, GLatLng pivot, Object browser, List<GLatLng> l, Color color, String hexColor, String parrentID) {
        super(name, l, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.startPosition = start;
        this.pivot = pivot;
        this.trackSpacing = trackSpacing;
        this.searchLeg = searchLeg;
        this.heading = heading;
        this.width = width;
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.PARAREL);
        //setIcon(NavigatorIconCollection.PARAREL_ICON_32);
        setConstructorType(constructorType.LOAD_FROM_DATABASE);
    }

    public PararelPattern(String name, GLatLng start, String searchLeg, String width, String trackSpacing, String heading, Object browser, Color color, String hexColor, String parrentID) {
        super(name, browser, parrentID);
        this.color = color;
        this.hexColor = hexColor;
        this.startPosition = start;
        this.trackSpacing = trackSpacing;
        this.searchLeg = searchLeg;
        this.heading = heading;
        this.width = width;
        ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.PARAREL);
        //setIcon(NavigatorIconCollection.PARAREL_ICON_32);
        setConstructorType(constructorType.CREATE_NEW);
    }

    @Override
    public synchronized String writeScript() {
        if (this.constructorType == ConstructorType.CREATE_NEW) {

            return new StringBuilder("createPararel(")
                    .append(ID)
                    .append(",").append(startPosition.toJavaScript())
                    .append(",").append(searchLeg)
                    .append(",").append(width)
                    .append(",").append(trackSpacing)
                    .append(",").append(heading)
                    .append(", null, null,'").append(hexColor).append("')").toString();
        } else {
            return new StringBuilder("createPararel(")
                    .append(ID)
                    .append(",").append(startPosition.toJavaScript())
                    .append(",").append(searchLeg)
                    .append(",").append(width)
                    .append(",").append(trackSpacing)
                    .append(",").append(heading)
                    .append(",").append(pivot.toJavaScript())
                    .append(",").append(JavaScriptBase.<GLatLng>toArrayJavaScript(l))
                    .append(",'").append(hexColor).append("')").toString();
        }
    }

    @Override
    public synchronized String createDescription() {
        this.getStartPosition();
        this.getPivot();
        return new StringBuilder(DESC_OPEN)
                .append(DESC_SEARCH_LEG).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getSearchLeg())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_WIDTH).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getWidth())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_TRACK_SPACING) .append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                .append(this.getTrackSpacing()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEADING)
                .append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getHeading()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE).append(pivot.getLat()).append(",").append(pivot.getLng()).append(DESC_END_VALUE_COUPLE)
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_START).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE).append(startPosition.getLat())
                .append(",").append(startPosition.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEX_COLOR).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.hexColor).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PARRENT_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID())
                .append(DESC_CLOSE).toString();

    }

    public static PararelPattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser) {
        String description = pattern.getDescription();
        if (description == null || description.equalsIgnoreCase("")) {
            return null;
        }
        description = description.substring(1, description.length() - 1);
        description = description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] spliteField = description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String searchLeg = spliteField[0].replaceAll(DESC_SEARCH_LEG + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String width = spliteField[1].replaceAll(DESC_WIDTH + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String trackSpacing = spliteField[2].replaceAll(DESC_TRACK_SPACING + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String heading = spliteField[3].replaceAll(DESC_HEADING + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String sPivot = spliteField[4].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String sStart = spliteField[5].replaceAll(DESC_START + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String hexColor = spliteField[6].replaceAll(DESC_HEX_COLOR + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String parrentID = spliteField[7].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String name = pattern.getName();
        GLatLng start = GLatLng.toJava(sStart);
        GLatLng pivot = GLatLng.toJava(sPivot);
        Color color;
        try {
            //color = Controller.hexToColor(hexColor);
            color = Color.YELLOW;
        } catch (Exception e) {
            color = Color.BLUE;
            hexColor = "ff0000ff";
        }
        PararelPattern result = new PararelPattern(name, start, searchLeg, width, trackSpacing, heading, pivot, browser, gLatLngs, color, hexColor, parrentID);
        result.setSearchPatternID(pattern.getSearchPatternId());
        return result;
    }

    public static void main(String[] args) {
        String des = DESC_OPEN
                + DESC_SEARCH_LEG + " " + DESC_VALUE_SEPARATOR + " " + 10 + DESC_FIELD_SEPARATOR
                + DESC_WIDTH + " " + DESC_VALUE_SEPARATOR + " " + 30 + DESC_FIELD_SEPARATOR
                + DESC_TRACK_SPACING + " " + DESC_VALUE_SEPARATOR + " " + 2 + DESC_FIELD_SEPARATOR
                + DESC_PIVOT + " " + DESC_VALUE_SEPARATOR + " " + DESC_BEGIN_VALUE_COUPLE + 0.928732832 + "," + 0.727323 + DESC_END_VALUE_COUPLE + DESC_FIELD_SEPARATOR
                + DESC_START + " " + DESC_VALUE_SEPARATOR + " " + DESC_BEGIN_VALUE_COUPLE + 0.27323273 + "," + 0.9832932 + DESC_END_VALUE_COUPLE + DESC_FIELD_SEPARATOR
                + DESC_CLOSE;
        IncidentSearchPattern pattern = new IncidentSearchPattern();
        pattern.setDescription(des);
        System.out.println(des);
        PararelPattern pararelPattern = createGSearchPatternFromSearchPatern(pattern, null, null);
    }

    public String getSearchLeg() {
        searchLeg = "toString(SEARCH_PATTERN[" + ID + "].searchLeg)";
        return searchLeg;
    }

    public void setSearchLeg(String searchLeg) {
        this.searchLeg = searchLeg;
    }

    public String getTrackSpacing() {
        trackSpacing = "toString(SEARCH_PATTERN[" + ID + "].trackSpacing)";
        return trackSpacing;
    }

    public void setTrackSpacing(String trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    public String getWidth() {
        width = "toString(SEARCH_PATTERN[" + ID + "].width)";
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
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
        if (this.constructorType == ConstructorType.CREATE_NEW) {

            return "{"
                    + "id:" + ID
                    + ",start:" + startPosition.toJavaScript()
                    + ",searchLeg:" + searchLeg
                    + ",width:" + width
                    + ",trackSpacing:" + trackSpacing
                    + ",heading:" + heading
                    + ",pivot:null,vertexs:null"
                    + ",color:'" + hexColor + "'}";
        } else {
            return "{"
                    + "id:" + ID
                    + ",start:" + startPosition.toJavaScript()
                    + ",searchLeg:" + searchLeg
                    + ",width:" + width
                    + ",trackSpacing:" + trackSpacing
                    + ",heading:" + heading
                    + ",pivot:" + pivot.toJavaScript()
                    + ",vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(l)
                    + ",color:'" + hexColor + "'}";
        }
    }

    @Override
    public String createConcurrent() {
        return "{object:"+ toJSON() +", gen:generatePararelPattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeSearchPattern}";
    }
}
