/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import lombok.Data;
import prosia.basarnas.model.Incident;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Aris
 */
@Data
public class DriftCalcWorksheet1Result {

    private GLatLng lkpLatLng;
    private GLatLng driftLeftLatLng;
    private GLatLng driftRightLatLng;
    private Double lkpToDatumAngle;
    private Double lkpToDatumDistance;
    private GLatLng datumLatLng;
    private Double tilt;
    private Double radius;
    private TaskSearchAreaController taskSearchAreaController;
    private String parrentID;
    private String shape;
    private Boolean drawLeewayLine;
    private Double lng1;
    private Double lng2;
    private Double startLat;
    
    public final static String CURRENT_DRIFTCALCULATION_AVAILABLE = "1";
    public final static String CURRENT_DRIFTCALCULATION_NOT_AVAILABLE = "0";

    public DriftCalcWorksheet1Result() {
    }

    private void initInsideTaskSearchAreaController() {
        lng1 = this.datumLatLng.getLngNumb() - (radius / 60);
        lng2 = this.datumLatLng.getLngNumb();
        startLat = this.datumLatLng.getLatNumb() + (radius / 60);
        this.taskSearchAreaController = new TaskSearchAreaController(radius, lng1, lng2, startLat);
    }

    public void initOutsideTaskSearchAreaController() {
        initInsideTaskSearchAreaController();
    }

    public static String checkingCurrentDriftCalculationScript() {
        return "checkingCurrentDriftCalculation()";
    }
}
