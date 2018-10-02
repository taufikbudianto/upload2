/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author Ismail
 */
@Data
public class TrapeziumSearchArea implements Serializable {

    private LatLng smallRoundPivot;
    private LatLng largeRoundPivot;
    private Double smallRoundRadius;
    private Double largeRoundRadius;
    private Double minSearchLeg;
    private Double maxSearchLeg;
    private Double width;
    private Double trackSpacing;
    private Double heading;
    private Double smallL;
    private Double bigL;
    private LatLng startTrapeziumPararelPattern;
    private LatLng smallCoordinat1;
    private LatLng lkp;
    private List<LatLng> vertexs;
    private String id;
    private String parrentID;
    private static int intID;

    public TrapeziumSearchArea(LatLng smallRoundPivot, LatLng largeRoundPivot, 
            Double smallRoundRadius, Double largeRoundRadius, 
            Double minSearchLeg, Double maxSearchLeg, 
            Double width, Double trackSpacing, 
            Double heading, Double smallL, 
            Double bigL, LatLng startTrapeziumPararelPattern, 
            LatLng smallCoordinat1, LatLng lkp, 
            List<LatLng> vertexs, String id, 
            String parrentID) {
        this.smallRoundPivot = smallRoundPivot;
        this.largeRoundPivot = largeRoundPivot;
        this.smallRoundRadius = smallRoundRadius;
        this.largeRoundRadius = largeRoundRadius;
        this.minSearchLeg = minSearchLeg;
        this.maxSearchLeg = maxSearchLeg;
        this.width = width;
        this.trackSpacing = trackSpacing;
        this.heading = heading;
        this.smallL = smallL;
        this.bigL = bigL;
        this.startTrapeziumPararelPattern = startTrapeziumPararelPattern;
        this.smallCoordinat1 = smallCoordinat1;
        this.lkp = lkp;
        this.vertexs = vertexs;
        this.id = id;
        this.parrentID = parrentID;
    }

}
