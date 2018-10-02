/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.model.Incident;

/**
 *
 * @author Aris
 */
@Data
public class GenerateValueRequestBean implements Serializable {

    private String shape;
    private LatLng lkpPoint;
    private LatLng driftLeftPoint;
    private LatLng driftRightPoint;
    private Double lkpToDatumAngle;
    private Double lkpToDatumDistance;
    private LatLng datumPoint;
    private Double tiltDrift;
    private Double radiusDrift;
    private Double trackSpacing;
    private Boolean drawLeewayLine;
    private String parrentId;

    private GenerateValueState state;
    private List<LatLng> vertexs = new ArrayList<>();

    //JIKA STATE DRIFT_SEARCH_AREA MAKA FIELD DIBAWAH TIDAK WAJIB MEMILIKI NILAI KECUALI JIKA STATE TASK_SEARCH_AREA
    private Double tiltTaskSearchArea;
    private Double widthTaskSearchArea;
    private Double heightTaskSearchArea;
    private LatLng pivotTaskSearchArea;
    private LatLng startTaskSearchArea;

    public static enum GenerateValueState {
        DRIFT_SEARCH_AREA,
        TASK_SEARCH_AREA
    }

    public GenerateValueRequestBean() {
    }

    public GenerateValueRequestBean(LatLng lkpPoint, LatLng driftLeftPoint, LatLng driftRightPoint, Double lkpToDatumAngle, Double lkpToDatumDistance, LatLng datumPoint, Double tiltDrift, Double radiusDrift, GenerateValueState state, Double tiltTaskSearchArea, Double widthTaskSearchArea, Double heightTaskSearchArea, LatLng pivotTaskSearchArea, Double trackSpacing) {
        this.lkpPoint = lkpPoint;
        this.driftLeftPoint = driftLeftPoint;
        this.driftRightPoint = driftRightPoint;
        this.lkpToDatumAngle = lkpToDatumAngle;
        this.lkpToDatumDistance = lkpToDatumDistance;
        this.datumPoint = datumPoint;
        this.tiltDrift = tiltDrift;
        this.radiusDrift = radiusDrift;
        this.state = state;
        this.tiltTaskSearchArea = tiltTaskSearchArea;
        this.widthTaskSearchArea = widthTaskSearchArea;
        this.heightTaskSearchArea = heightTaskSearchArea;
        this.pivotTaskSearchArea = pivotTaskSearchArea;
        this.trackSpacing = trackSpacing;
    }
}
