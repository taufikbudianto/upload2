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
public class SectorPattern extends GSearchPattern{
    public String searchRadius;
    public String heading;
    private final static String DESC_SEARCH_RADIUS = "Search_Radius";
    private final static String DESC_START = "Start";
    private final static String DESC_PIVOT = "Pivot";

    public SectorPattern(String name, Object browser, String parrentID) {
        super(name, browser, parrentID);
    }

    public SectorPattern(String name, GLatLng start, String searchRadius, String heading, GLatLng pivot, Object browser, List<GLatLng> l, Color color, String hexColor, String parrentID) {
        super(name, l, browser, parrentID);
        setStartPosition(start);
        this.color = color;
        this.hexColor = hexColor;
        this.searchRadius = searchRadius;
        this.heading = heading;
        this.pivot = pivot;
        this.ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.SECTOR);
        //setIcon(NavigatorIconCollection.SECTOR_ICON_32);
        setConstructorType(ConstructorType.LOAD_FROM_DATABASE);
    }

    public SectorPattern(String name, GLatLng start, String searchRadius, String heading, Object browser, Color color, String hexColor, String parrentID) {
        super(name, browser, parrentID);
        setStartPosition(start);
        this.color = color;
        this.hexColor = hexColor;
        this.searchRadius = searchRadius;
        this.heading = heading;
        this.ID = ++AUTO_INCEREMENT;
        setType(GSearchPattern.SECTOR);
        //setIcon(NavigatorIconCollection.SECTOR_ICON_32);
        setConstructorType(ConstructorType.CREATE_NEW);
    }

    @Override
    public String writeScript() {
        if (this.constructorType == ConstructorType.CREATE_NEW) {
            return new StringBuilder("createSector(")
                    .append(ID)
                    .append(",").append(startPosition.toJavaScript())
                    .append(",").append(searchRadius)
                    .append(",").append(heading)
                    .append(",null,null,'").append(hexColor).append("')").toString();
        } else {
            return new StringBuilder("createSector(")
                    .append(ID)
                    .append(",").append(startPosition.toJavaScript())
                    .append(",").append(searchRadius)
                    .append(",").append(heading)
                    .append(",").append(pivot.toJavaScript())
                    .append(",").append(JavaScriptBase.<GLatLng>toArrayJavaScript(l))
                    .append(",'").append(hexColor).append("')").toString();
        }
    }

    /**
     * Membuat String discription yang valid untuk search pattern berType Sector
     *[Diameter : value#Start : (value.latitude,value.longitude)#Pivot : (value.latitude,value.longitude)] 
     */
    @Override
    public synchronized String createDescription() {
        this.getStartPosition();
        this.getPivot();
        return new StringBuilder(DESC_OPEN)
                .append(DESC_SEARCH_RADIUS).append(" ").append(DESC_VALUE_SEPARATOR)
                .append(" ").append(this.getSearchRadius()).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEADING).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getHeading())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_START).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                .append(DESC_BEGIN_VALUE_COUPLE).append(startPosition.getLat()).append(",").append(startPosition.getLng())
                .append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(DESC_BEGIN_VALUE_COUPLE)
                .append(pivot.getLat()).append(",").append(pivot.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                .append(DESC_HEX_COLOR).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getHexColor())
                .append(DESC_FIELD_SEPARATOR)
                .append(DESC_PARRENT_ID).append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID())
                .append(DESC_CLOSE).toString();
    }

    public static SectorPattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser) {
        String description = pattern.getDescription();
        if (description == null || description.equalsIgnoreCase(CommonConstant.EMPTY_STRING)) {
            return null;
        }
        description = description.substring(1, description.length() - 1);
        description = description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] splitField = description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String searchRadius = splitField[0].replaceAll(DESC_SEARCH_RADIUS + DESC_VALUE_SEPARATOR, "");
        String heading = splitField[1].replaceAll(DESC_HEADING + DESC_VALUE_SEPARATOR, "");
        String sStart = splitField[2].replaceAll(DESC_START + DESC_VALUE_SEPARATOR, "");
        String sPivot = splitField[3].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, "");
        String hexColor = splitField[4].replaceAll(DESC_HEX_COLOR + DESC_VALUE_SEPARATOR, "");
        String parrentID = splitField[5].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, "");
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
        SectorPattern scPattern = new SectorPattern(name, start, searchRadius, heading, pivot, browser, gLatLngs, color, hexColor, parrentID);
        scPattern.setSearchPatternID(pattern.getSearchPatternId());
        return scPattern;
    }

    public static void main(String[] args) {
        String des = DESC_OPEN
                + DESC_SEARCH_RADIUS + " " + DESC_VALUE_SEPARATOR + " " + 21 + DESC_FIELD_SEPARATOR
                + DESC_START + " " + DESC_VALUE_SEPARATOR + " " + DESC_BEGIN_VALUE_COUPLE + 0.09992 + "," + 0.9898 + DESC_END_VALUE_COUPLE + DESC_FIELD_SEPARATOR
                + DESC_PIVOT + " " + DESC_VALUE_SEPARATOR + " " + DESC_BEGIN_VALUE_COUPLE + 0.87328328 + "," + 0.77374374 + DESC_END_VALUE_COUPLE
                + DESC_CLOSE;
        IncidentSearchPattern pattern = new IncidentSearchPattern();
        pattern.setDescription(des);
        SectorPattern sp = createGSearchPatternFromSearchPatern(pattern, null, null);
        System.out.println(sp.searchRadius);
        System.out.println(sp.startPosition.getLat() + "," + sp.startPosition.getLng());
        System.out.println(sp.pivot.getLat() + "," + sp.pivot.getLng());
    }

    public synchronized String getSearchRadius() {
        double sRadius = Double.valueOf("toString(SEARCH_PATTERN[" + ID + "].diameter)");
        sRadius = sRadius / 2;
        searchRadius = sRadius + "";
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
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
                    + ",radius:" + searchRadius
                    + ",heading:" + heading
                    + ",pivot:null"
                    + ",vertexs:null"
                    + ",color:'" + hexColor + "'}";
        } else {
            return "{"
                    + "id:" + ID
                    + ",start:" + startPosition.toJavaScript()
                    + ",radius:" + searchRadius
                    + ",heading:" + heading
                    + ",pivot:" + pivot.toJavaScript()
                    + ",vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(l)
                    + ",color:" + "'" + hexColor + "')";
        }
    }

    @Override
    public String createConcurrent() {
        return "{object:"+ toJSON() +", gen:generateSectorPattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeSearchPattern}";
    }
}
