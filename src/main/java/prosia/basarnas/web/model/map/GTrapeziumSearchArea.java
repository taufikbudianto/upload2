/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import prosia.basarnas.service.map_js.*;
import java.util.List;
import org.apache.log4j.Logger;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.constant.MapConstant;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.JavaScriptBase;

/**
 *
 * @author Aris
 */
public class GTrapeziumSearchArea extends GPoly<Object> {
    private static Logger logger = Logger.getLogger(GTrapeziumSearchArea.class);
    private GLatLng smallRoundPivot;
    private GLatLng largeRoundPivot;
    private double smallRoundRadius;
    private double largeRoundRadius;
    private double minSearchLeg;
    private double maxSearchLeg;
    private double width;
    private double trackSpacing;
    private double heading;
    private Double smallL;
    private Double bigL;
    private LatLng startTrapeziumPararelPattern;
    private LatLng smallCoordinat1;
    private LatLng lkp;
    private List<GLatLng> l;
    private String id;
    private String parrentID;
    private static int intID;

    //tidak memakai lagi ID properties
    public GTrapeziumSearchArea(Object browser, String id, GLatLng smallRoundPivot, GLatLng largeRoundPivot, double smallRoundRadius, double largeRoundRadius, double min, double max, double width, double trackSpacing, double heading, LatLng startTrapezPararelPattern, List<GLatLng> l, LatLng lkp, LatLng smallCoordinat1, Double smallL, Double bigL, String parrentID) {
        //super(browser);
        this.smallRoundPivot = smallRoundPivot;
        this.largeRoundPivot = largeRoundPivot;
        this.smallRoundRadius = smallRoundRadius;
        this.largeRoundRadius = largeRoundRadius;
        this.minSearchLeg = min;
        this.maxSearchLeg = max;
        this.width = width;
        this.trackSpacing = trackSpacing;
        this.heading = heading;
        this.startTrapeziumPararelPattern = startTrapezPararelPattern;
        this.l = l;
        this.id = id;
        this.lkp = lkp;
        this.smallCoordinat1 = smallCoordinat1;
        this.smallL = smallL;
        this.bigL = bigL;
        this.parrentID = parrentID;
    }
    public static GTrapeziumSearchArea currentGTrapezium;

    public static GTrapeziumSearchArea getInstance() {
        return currentGTrapezium;
    }
    private static GTrapeziumSearchArea[] arrCurrentGTrapezium = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    
    public static void setInstance(GTrapeziumSearchArea trapeziumSearchArea) {
        if (trapeziumSearchArea == null) {
            //ORIGINAL : trapeziumSearchArea != null
//            trapeziumSearchArea.setId("Current");
        }else{
            intID = Integer.parseInt(trapeziumSearchArea.id.replace("Current", ""));
            arrCurrentGTrapezium[intID] = trapeziumSearchArea;
            currentGTrapezium = trapeziumSearchArea;
        }
    }

    public static void createAndRemoveCurrent() {
        
        //Browser browser = currentGTrapezium.getBrowser();
        String writeScript = currentGTrapezium.writeScript();
        String removeScript = currentGTrapezium.removeScript();
        //browser.executeJavaScript(removeScript);
        //browser.executeJavaScript(writeScript);
        String newCenterOfMap = MapConstant.setCenter(currentGTrapezium.getSmallRoundPivot(), 10);
        //browser.executeJavaScript(newCenterOfMap);
        logger.info(removeScript);
        logger.info(writeScript);
        logger.info(newCenterOfMap);

    }

    public static void removeCurrentTrapeziumArea() {
        GTrapeziumSearchArea trapeziumSearchArea = getInstance();
        if (trapeziumSearchArea != null) {
            //Browser browser = trapeziumSearchArea.getBrowser();
            String removeScript = trapeziumSearchArea.removeScript();
            //browser.executeJavaScript(removeScript);
            if(intID == 0){
                setInstance(null);
            }else{
                setInstance(arrCurrentGTrapezium[intID - 1]);
            }
            
            logger.info(removeScript);
        }
    }

    public static void removeCurrentTrapeziumAreaAll() {
        GTrapeziumSearchArea trapeziumSearchArea = getInstance();
        if (trapeziumSearchArea != null) {
           // Browser browser = trapeziumSearchArea.getBrowser();
            String[] strid = trapeziumSearchArea.getId().split("Current");
            String removeScript = trapeziumSearchArea.removeScript();
            String[]scID = removeScript.split("Current");
            int i = Integer.parseInt(strid[1]);
            while(i >= 0)
            {
               // browser.executeJavaScript(removeScript);
                i--;
                scID[1] = "Current" + i + "')";
                removeScript = scID[0] + scID[1];
            }
            
            setInstance(null);
            
            logger.info(removeScript);
        }
    }
 
    
    @Override
    public String writeScript() {
        return new StringBuilder("createTrapeziumSearchArea('")
                .append(id).append("', ")
                .append(smallRoundPivot.toJavaScript()).append(", ")
                .append(largeRoundPivot.toJavaScript()).append(", ")
                .append(smallRoundRadius).append(", ")
                .append(largeRoundRadius).append(", ")
                .append(JavaScriptBase.<GLatLng>toArrayJavaScript(l)).append(",")
                .append("'").append(this.getParrentID()).append("'")
                .append(")").toString();
    }

    @Override
    public String removeScript() {
        return "removeTrapeziumSearchArea('" + id + "')";
    }

    @Override
    public String newCollectionScript() {
        return "";
    }

    public List<GLatLng> getL() {
        return l;
    }

    public void setL(List<GLatLng> l) {
        this.l = l;
    }

    public GLatLng getLargeRoundPivot() {
        return largeRoundPivot;
    }

    public void setLargeRoundPivot(GLatLng largeRoundPivot) {
        this.largeRoundPivot = largeRoundPivot;
    }

    public GLatLng getSmallRoundPivot() {
        return smallRoundPivot;
    }

    public void setSmallRoundPivot(GLatLng smallRoundPivot) {
        this.smallRoundPivot = smallRoundPivot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLargeRoundRadius() {
        return largeRoundRadius;
    }

    public void setLargeRoundRadius(double largeRoundRadius) {
        this.largeRoundRadius = largeRoundRadius;
    }

    public double getSmallRoundRadius() {
        return smallRoundRadius;
    }

    public void setSmallRoundRadius(double smallRoundRadius) {
        this.smallRoundRadius = smallRoundRadius;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getMaxSearchLeg() {
        return maxSearchLeg;
    }

    public void setMaxSearchLeg(double maxSearchLeg) {
        this.maxSearchLeg = maxSearchLeg;
    }

    public double getMinSearchLeg() {
        return minSearchLeg;
    }

    public void setMinSearchLeg(double minSearchLeg) {
        this.minSearchLeg = minSearchLeg;
    }

    public LatLng getStartTrapeziumPararelPattern() {
        return startTrapeziumPararelPattern;
    }

    public void setStartTrapeziumPararelPattern(LatLng startTrapeziumPararelPattern) {
        this.startTrapeziumPararelPattern = startTrapeziumPararelPattern;
    }

    public Double minSearchLeg(Double trackSpacing) {
        return getSmallL() * 2 * 60 - trackSpacing;
    }

    public Double maxSearchLeg(Double trackSpacing) {
        return getBigL() * 2 * 60 - trackSpacing;
    }

    public double getTrackSpacing() {
        return trackSpacing;
    }

    public void setTrackSpacing(double trackSpacing) {
        this.trackSpacing = trackSpacing;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public LatLng getLkp() {
        return lkp;
    }

    public void setLkp(LatLng lkp) {
        this.lkp = lkp;
    }

    public LatLng getSmallCoordinat1() {
        return smallCoordinat1;
    }

    public void setSmallCoordinat1(LatLng smallCoordinat1) {
        this.smallCoordinat1 = smallCoordinat1;
    }

    public Double getBigL() {
        return bigL;
    }

    public void setBigL(Double bigL) {
        this.bigL = bigL;
    }

    public Double getSmallL() {
        return smallL;
    }

    public void setSmallL(Double smallL) {
        this.smallL = smallL;
    }

    public String getParrentID() {
        return parrentID;
    }

    public void setParrentID(String parrentID) {
        this.parrentID = parrentID;
    }

    
    @Override
    public String toJSON() {
        return "{id:'" + id
                + "',smallRounfPivot:" + smallRoundPivot.toJavaScript()
                + ",largeRoundPivot:" + largeRoundPivot.toJavaScript()
                + ",smallRoundRadius:" + smallRoundRadius
                + ",largeRoundRadius:" + largeRoundRadius
                + ",vertexs" + JavaScriptBase.<GLatLng>toArrayJavaScript(l) + ","
                + "parrentID:'" + this.getParrentID() + "'"
                + "}";
    }

    @Override
    public String createConcurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String removeConcurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
