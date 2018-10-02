/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map.poly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Ismail
 */
public class TSRPattern implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private LatLng start;
    private double searchLeg;
    private double searchWidth;
    private double searchTrackSpacing;
    private double searchHeading;
    private LatLng pivot;
    private List<LatLng> latlngs;
    private String color;
    private int rotateState;

    @Getter
    private Polyline polyline = new Polyline();
    @Getter
    private List<Marker> markersWaypointToolTip;
    @Getter
    private String contentString;

    public TSRPattern(LatLng start, double searchTrackSpacing, double searchLeg,
            double searchHeading, LatLng pivot, List<LatLng> latlngs, String color) {
        this.color = color;
        this.start = start;
        this.searchTrackSpacing = searchTrackSpacing;
        this.searchLeg = searchLeg;
        this.searchHeading = searchHeading;
        if (pivot == null) {
            this.pivot = new LatLng((start.getLat()
                    + ((searchLeg / 2) * CommonConstant.ONE_NM_TO_DEG)),
                    (start.getLng() + ((searchTrackSpacing / 2) * CommonConstant.ONE_NM_TO_DEG)));
        } else {
            this.pivot = pivot;
        }
        if (latlngs == null || latlngs.isEmpty()) {
            init();
            firstHeadings();
            initWaypointToolTip();
        } else {
            this.latlngs = latlngs;
        }

        contentString = "<b class='title'><u>Track Search Return Search</u></b>"
                + "<br><b class='field'>Search Leg</b>      : <font class='value'>" + searchLeg + " NM</font>"
                + "<br><b class='field'>Heading </b> : <font class='value'>" + searchHeading + " °</font>"
                + "<br><b class='field'>Track Spacing </b> : <font class='value'>" + searchTrackSpacing + " NM</font>";
        //+   "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";

    }

    private void init() {
        List<LatLng> listLatLngs = new ArrayList<>();
        listLatLngs.add(start);
        LatLng point1 = new LatLng(start.getLat()
                + (searchLeg * CommonConstant.ONE_NM_TO_DEG), this.start.getLng());
        LatLng point2 = new LatLng(point1.getLat(), point1.getLng()
                + (searchTrackSpacing * CommonConstant.ONE_NM_TO_DEG));
        LatLng point3 = new LatLng(this.start.getLat(), point2.getLng());
        rotateState = 0;
        listLatLngs.add(point1);
        listLatLngs.add(point2);
        listLatLngs.add(point3);
        createAllPolyLine(listLatLngs);
    }

    public void firstHeadings() {
        LatLng firstHeadingPivot = new LatLng(start.getLat(), (start.getLng()
                + (searchTrackSpacing / 2 * CommonConstant.ONE_NM_TO_DEG)));
        List<LatLng> rotation = commonUtil.rotationLatLngs(polyline.getPaths(), firstHeadingPivot, searchHeading);
        polyline.setPaths(rotation);
        pivot = commonUtil.rotationLatLng(firstHeadingPivot, pivot, searchHeading);
    }

    public void headings() {
        List<LatLng> rotation = commonUtil.rotationLatLngs(polyline.getPaths(), pivot, searchHeading);
        polyline.setPaths(rotation);
    }
    
    public String inFocus() {
        return pivot.getLat() + "," + pivot.getLng();
    }

    public Integer inZoom() {
        return 10;
    }

    public void initWaypointToolTip() {
        markersWaypointToolTip = new ArrayList<>();
        for (int i = 0; i < this.polyline.getPaths().size() - 1; i++) {
            markersWaypointToolTip.add(new Marker(polyline.getPaths().get(i), "Waypoint " + (i + 1)));
        }
    }

    private void createAllPolyLine(List<LatLng> listLatLng) {
        polyline = new Polyline();
        for (LatLng gLatLng : listLatLng) {
            polyline.getPaths().add(new LatLng(gLatLng.getLat(),
                    gLatLng.getLng()));
        }
        polyline.setStrokeColor(color);
        polyline.setStrokeWeight(3);
        polyline.setStrokeOpacity(1);
    }
}
