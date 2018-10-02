/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.enumeration.MapPattern;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.Waypoint;

/**
 *
 * @author Ismail
 */
@Data
public class SearchPatternParameter implements Serializable {
//Search Drift Calculation

    private String id = UUID.randomUUID().toString();
    private String name;
    //Description Start
    private String hexColor = "0000FF";
    private Double trackSpacing;
    private Double width;
    private Double searchRadius;
    private Double searchLeg;
    private Double heading;
    private Double parrentID;
    private Double minSearchLeg;
    private Double maxSearchLeg;
    private Boolean isSearchTrackSpacing = false;
    private Boolean isSearchWidth = false;
    private Boolean isSearchRadius = false;
    private Boolean isSearchLeg = false;
    private Boolean isSearchHeading = false;
    private Boolean isSearchMinLeg = false;
    private Boolean isSearchMaxLeg = false;
    private LatLng start;
    private LatLng pivot;
    private IncTaskSearchArea incTaskSearchArea;
    private MapPattern type;
    //Description End
    private IncidentAsset incidentAsset;
    private Polyline polyline;
    private List<Marker> markers = new ArrayList<>();

    private String description;
    private String centerGeoMap;
    private Integer zoomGeoMap;
}
