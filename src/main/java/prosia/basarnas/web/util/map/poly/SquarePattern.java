/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map.poly;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Ismail
 */
public class SquarePattern implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private double searchTrackSpacing;
    private double searchRadius;
    private double searchHeading;
    private double diameter;
    private LatLng pivot;
    private List<LatLng> latlngs;
    private String color;
    private boolean n;
    private boolean s;
    @Getter
    private Polyline polyline = new Polyline();
    @Getter
    private String contentString;

    public SquarePattern(double searchTrackSpacing, double searchRadius,
            LatLng pivot, double searchHeading, List<LatLng> latlngs, String color) {
        this.searchTrackSpacing = searchTrackSpacing;
        this.searchRadius = searchRadius;
        this.searchHeading = searchHeading;
        this.latlngs = latlngs;
        this.color = color;
        this.pivot = pivot;
        if (latlngs == null || latlngs.isEmpty()) {
            init();
            List<LatLng> rotation
                    = commonUtil.rotationLatLngs(
                            polyline.getPaths(), pivot, searchHeading);
            polyline.setPaths(rotation);
        } else {
            this.latlngs = latlngs;
        }

        contentString = "<b class='title'><u>Square Search</u></b>"
                + "<br><b class='field'>Search Radius</b>      : <font class='value'>" + this.searchRadius + " NM</font>"
                + "<br><b class='field'>Max Leg</b>     : <font class='value'>" + this.searchRadius * 2 + " NM</font>"
                + "<br><b class='field'>Heading</b>     : <font class='value'>" + this.searchHeading + " °</font>"
                + "<br><b class='field'>Track Spacing</b> : <font class='value'>" + this.searchTrackSpacing + " NM</font>";
//    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";

    }

    private void init() {
        n = true;
        s = false;
        create();
    }

    private void create() {
        int loopCount = 0;
        double tolerancyLeg;
        double halfLeg = searchRadius - (searchTrackSpacing / 2);
        if (halfLeg % searchTrackSpacing == 0) {
            BigDecimal bd = new BigDecimal((halfLeg * 2) / searchTrackSpacing);
            loopCount = bd.setScale(0, BigDecimal.ROUND_DOWN).intValue();
        } else {
            tolerancyLeg = searchRadius - (searchTrackSpacing / 10);
            double sisaToleransi = tolerancyLeg % searchTrackSpacing;
            if (sisaToleransi == 0) {
                BigDecimal bd = new BigDecimal((tolerancyLeg * 2) / searchTrackSpacing);
                loopCount = bd.setScale(0, BigDecimal.ROUND_DOWN).intValue();
            } else {
                BigDecimal bd = new BigDecimal(((tolerancyLeg * 2) - sisaToleransi) / searchTrackSpacing);
                loopCount = bd.setScale(0, BigDecimal.ROUND_DOWN).intValue();
            }
        }
        prosesDrawing(loopCount, searchTrackSpacing, pivot, color);

    }

    private void prosesDrawing(int loopCount, double trackSpacing, LatLng gLatLng, String color) {
        LatLng nextPoint = null;
        double nextLeg = 0.0;
        List<LatLng> listLatLngs = new ArrayList<>();
        listLatLngs.add(gLatLng);
        for (int i = 1; i <= loopCount; ++i) {
            nextLeg = i * trackSpacing;
            if (n) {
                nextPoint = listLatLngs.get(listLatLngs.size() - 1);
                nextPoint = new LatLng(nextPoint.getLat()
                        + (CommonConstant.ONE_NM_TO_DEG * nextLeg), nextPoint.getLng());
                listLatLngs.add(nextPoint);
                nextPoint = new LatLng(nextPoint.getLat(), nextPoint.getLng()
                        + (CommonConstant.ONE_NM_TO_DEG * nextLeg));
                listLatLngs.add(nextPoint);

                n = false;
                s = true;
            } else if (s) {
                nextPoint = listLatLngs.get(listLatLngs.size() - 1);
                nextPoint = new LatLng(nextPoint.getLat()
                        - (CommonConstant.ONE_NM_TO_DEG * nextLeg), nextPoint.getLng());
                listLatLngs.add(nextPoint);
                nextPoint = new LatLng(nextPoint.getLat(), nextPoint.getLng()
                        - (CommonConstant.ONE_NM_TO_DEG * nextLeg));
                listLatLngs.add(nextPoint);
                n = true;
                s = false;
            }
        }
        if (s) {
            listLatLngs.add(new LatLng(nextPoint.getLat()
                    - (CommonConstant.ONE_NM_TO_DEG * nextLeg), nextPoint.getLng()));
        } else {
            // jika direction n(NORTH)
            listLatLngs.add(new LatLng(nextPoint.getLat()
                    + (CommonConstant.ONE_NM_TO_DEG * nextLeg), nextPoint.getLng()));
        }

        createAllPolyLine(listLatLngs, color);
    }

    private void createAllPolyLine(
            List<LatLng> listLatLng, String color) {
        for (LatLng gLatLng : listLatLng) {
            polyline.getPaths().add(new LatLng(gLatLng.getLat(),
                    gLatLng.getLng()));
        }
        polyline.setStrokeColor(color);
        polyline.setStrokeWeight(3);
        polyline.setStrokeOpacity(1);
    }

    public String inFocus() {
        return pivot.getLat() + "," + pivot.getLng();
    }

    public Integer inZoom() {
        return 10;
    }
}
