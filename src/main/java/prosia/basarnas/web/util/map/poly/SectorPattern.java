/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map.poly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
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
public class SectorPattern implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private LatLng start;
    private double searchRadius;
    private double searchHeading;
    private double diameter;
    private LatLng staticAngle;
    private LatLng dynamicAngle;
    private LatLng pivot;
    private List<LatLng> latlngs;
    private String color;
    @Getter
    private Polyline polyline = new Polyline();
    @Getter
    private List<Marker> markers;
    @Getter
    private String contentString;

    public SectorPattern(LatLng start, double searchRadius, double searchHeading,
            LatLng pivot, List<LatLng> latlngs, String color) {
        this.start = start;
        this.searchRadius = searchRadius;
        this.searchHeading = searchHeading;

        this.latlngs = latlngs;
        this.color = color;
        this.diameter = searchRadius * 2;
        if (latlngs == null || latlngs.isEmpty()) {
            init();
            List<LatLng> rotation = commonUtil.rotationLatLngs(
                    polyline.getPaths(), this.pivot, this.searchHeading);
            polyline.setPaths(rotation);
            initWaypointToolTip();
        } else {
            this.pivot = pivot;
            polyline.setPaths(latlngs);
        }

        contentString = "<b class='title'><u>Sector Search</u></b>"
                + "<br><b class='field'>Search Radius</b>      : <font class='value'>" + this.searchRadius + " NM</font>"
                + "<br><b class='field'>Heading </b> : <font class='value'>" + this.searchHeading + " °</font>";
//                + "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength()
        //                +"</font>"

    }

    private void init() {
        double staticAngleLat = this.start.getLat()
                + (this.diameter * CommonConstant.ONE_NM_TO_DEG); //Number(float)
        double pivotLat = this.start.getLat()
                + ((this.diameter / 2) * CommonConstant.ONE_NM_TO_DEG); //Number(float)
        staticAngle = new LatLng(staticAngleLat, this.start.getLng()); //LatLng
        pivot = new LatLng(pivotLat, this.start.getLng()); //LatLng
        dynamicAngle = this.staticAngle; //nilai default dari Dinamic angle adalah staticAngle
        this.create();
    }

    private void create() {
        List<LatLng> listLatLngs = new ArrayList<>();
        listLatLngs.add(start);
        listLatLngs.add(staticAngle);
        dynamicAngle = commonUtil.rotationLatLng(this.pivot, this.dynamicAngle, 60);
        listLatLngs.add(dynamicAngle);
        dynamicAngle = commonUtil.rotationLatLng(this.pivot, this.dynamicAngle, 180);
        listLatLngs.add(dynamicAngle);
        dynamicAngle = commonUtil.rotationLatLng(this.pivot, this.dynamicAngle, 60);
        listLatLngs.add(dynamicAngle);
        dynamicAngle = commonUtil.rotationLatLng(this.pivot, this.dynamicAngle, 180);
        listLatLngs.add(dynamicAngle);
        dynamicAngle = commonUtil.rotationLatLng(this.pivot, this.dynamicAngle, 60);
        listLatLngs.add(dynamicAngle);

        createAllPolyLine(listLatLngs);
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

    private void initWaypointToolTip() {
        markers = new ArrayList<>();
        String iconLkpPath = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestContextPath().concat("/resources/basarnas/images/icon/transparant.png");

        for (int i = 0; i < this.polyline.getPaths().size() - 1; i++) {
            markers.add(new Marker(polyline.getPaths().get(i), "Waypoint " + (i + 1),null,iconLkpPath));
        }
    }

    public String inFocus() {
        return pivot.getLat() + "," + pivot.getLng();
    }

    public Integer inZoom() {
        return 10;
    }
}
