/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import lombok.Data;
import org.springframework.stereotype.Controller;
import prosia.basarnas.constant.MapConstant;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.service.map_js.DescriptionConstant;
import prosia.basarnas.service.map_js.Disposable;
import prosia.basarnas.service.map_js.TaskSearchAreaController;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;

/**
 *
 * @author Aris
 */
@Controller
public @Data class MapController implements Disposable{
    private static String[] area51 = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    public static int jumArea;
    DriftCalcWorksheet1Result currentDriftCalculation;
    public static String mapFormExec;
    TaskSearchAreaController taskSearchAreaController;
    
    public static String removeAndCreateCurrentDriftCalculationScript(DriftCalcWorksheet1Result worksheet1Result) {
        return new StringBuilder("removeAndCreateCurrentDriftCalculation(").append(worksheet1Result.getLkpLatLng().toJavaScript()).append(",").append(worksheet1Result.getDatumLatLng().toJavaScript()).append(",").append(worksheet1Result.getDriftLeftLatLng().toJavaScript()).append(",").append(worksheet1Result.getDriftRightLatLng().toJavaScript()).append(",").append(worksheet1Result.getRadius()).append(",").append(worksheet1Result.getTilt()).append(",'").append(worksheet1Result.getParrentID()).append("',").append(worksheet1Result.getLkpToDatumDistance()).append(",").append(worksheet1Result.getLkpToDatumAngle()).append(",'").append(worksheet1Result.getShape()).append("',").append(worksheet1Result.getDrawLeewayLine()).append(")").toString();
    }
    public static void removeAndCreateCurrentDriftCalculation(DriftCalcWorksheet1Result worksheet1Result) {
        mapFormExec = removeAndCreateCurrentDriftCalculationScript(worksheet1Result);    
        //setCurrentDriftCalculation(worksheet1Result);
    }
    
    public void setCurrentDriftCalculation(DriftCalcWorksheet1Result currentDriftCalculation) {
        this.currentDriftCalculation = currentDriftCalculation;
    }
    
    public static String checkDriftCalculation(String value){
        return mapFormExec = value;
    }
    
    public static String removeCurrentDriftCalculationScript() {
        return "removeCurrentDriftCalculation()";
    }
    public static void removeCurrentDriftCalculation() {
        mapFormExec = removeCurrentDriftCalculationScript();
        //Main.mapForm.getController().setCurrentDriftCalculation(null);
    }
    public synchronized SearchArea getSearchAreaFormMap(int index) {
        String statusSearchArea = MapConstant.CHECKING_SEARCH_AREA;
        try {
            if (!statusSearchArea.equalsIgnoreCase(GSearchArea.CURRENT_SEARCH_AREA_HAS_POLYGON)) {
                return null;
            }
            SearchArea searchArea = new SearchArea();
            String area = "";
            if (jumArea == 0) {
                //area = GSearchArea.createAreaFormCurrentSearchArea(form.getBrowserComponent().getBrowser());
                area = GSearchArea.createAreaFormCurrentSearchArea();
            } else {
                area = getArrayArea(index);
            }
            searchArea.setArea(area);
            String parrentID = GSearchArea.getCurrentSearchAreaParrentIDScript();
            searchArea.setShape(GSearchArea.getCurrentSearchAreaShapeScript());
            if (!searchArea.getShape().equals("Trapezium")) {
                searchArea.setRadius(Double.valueOf(GSearchArea.getCurrentSearchAreaRadiusScript()));
            }
            if (MapConstant.isNull(parrentID)) {
                parrentID = getCurrentDriftCalculation().getParrentID();
            }
            searchArea.setParrentID(parrentID);
            return searchArea;

        } catch (Exception e) {
        }
        return null;
    }
    
    public static String createArrayforArea(GLatLng v1, GLatLng v2, GLatLng v3, GLatLng v4, int index) {
        String DESC_AREA = "Area";
        String DESC_START = "Start";

        GLatLng[] arrV1 = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        GLatLng[] arrV2 = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        GLatLng[] arrV3 = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        GLatLng[] arrV4 = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        String[] arrLKP = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        String[] arrCoordinat = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

        arrV1[index] = v1;
        arrV2[index] = v2;
        arrV3[index] = v3;
        arrV4[index] = v4;

        arrLKP[index] = arrV1[index].toString();
        arrCoordinat[index] = arrV1[index].toString() + ", " + arrV2[index].toString() + ", "
                + arrV3[index].toString() + ", " + arrV4[index].toString();

        area51[index] = new StringBuilder(DescriptionConstant.DESC_OPEN)
                .append(DESC_AREA).append(" ")
                .append(DescriptionConstant.DESC_VALUE_SEPARATOR)
                .append(" ").append(DescriptionConstant.DESC_BEGIN_COLLECTION)
                .append(arrCoordinat[index])
                .append(DescriptionConstant.DESC_END_COLLECTION)
                .append(DescriptionConstant.DESC_FIELD_SEPARATOR)
                .append(DESC_START)
                .append(" ")
                .append(DescriptionConstant.DESC_VALUE_SEPARATOR)
                .append(" ")
                .append(arrLKP[index])
                .append(DescriptionConstant.DESC_CLOSE).toString();

        return area51[index];
    }
    
    private String getArrayArea(int index) {
        return area51[index];
    }
    
    public static void addJumArea() {
        MapController.jumArea = ++jumArea;
    }
    
    public static void subtractJumArea() {
        MapController.jumArea = --jumArea;
    }
    
    @Override
    public void dumpResources() throws Exception {
        
    }
    
}
