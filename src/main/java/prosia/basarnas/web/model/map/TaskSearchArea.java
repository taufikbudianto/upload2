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
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.model.IncidentAsset;

/**
 *
 * @author Ismail
 */
@Data
public class TaskSearchArea implements Serializable {

    private Integer id;
    private LatLng unrotateStart;
    private LatLng unrotatePivot;
    private IncidentAsset incidentAsset;
    private Double width;
    private Double height;
    private Double trackSpacing;
    private Integer radiusType;
    private String name;
    private String parrentID;
    private String childID;
    private String driftTaskAreaID;
    private Double tiltDrift;
    private LatLng pivotDatumPoint;
    private MapCalculationType typeTaskArea;
    private List<LatLng> vertexs;

}
