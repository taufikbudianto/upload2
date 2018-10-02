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
import org.primefaces.model.map.Polyline;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Irfan Rofie
 */
public class PararelTrapeziumPattern implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private LatLng start;
    private Double minSearchLeg;
    private Double maxSearchLeg;
    private Double width;
    private Double trackSpacing;
    private Double heading;
    private String color;
    private List<LatLng> vertexs;
    private Boolean e;
    private Boolean w;
    private Boolean n;
    private Integer NEXT_DIRECTION_FOR_NORTH;
    private Integer WEST = 0;
    private Integer EAST = 1;
    private Double dynamicLeg;
    private LatLng pivot;
    @Getter
    private Polyline polyline = new Polyline();
    @Getter
    private String contentString;

    public PararelTrapeziumPattern(LatLng start, Double minSearchLeg,
            Double maxSearchLeg, Double width, Double trackSpacing,
            Double heading, String color, List<LatLng> vertexs) {
        this.start = start;
        this.minSearchLeg = minSearchLeg;
        this.maxSearchLeg = maxSearchLeg;
        this.width = width;
        this.trackSpacing = trackSpacing;
        this.heading = heading;
        this.color = color;
        this.vertexs = vertexs;
        if (vertexs == null || vertexs.isEmpty()) {
            this.vertexs = new ArrayList<>();
            this.vertexs.add(start);
            initDirection();
            this.vertexs = commonUtil.rotationLatLngs(this.vertexs, start, heading);
            initPivot();
        } else {
            this.start = vertexs.get(0);
            initPivot();
        }
        polyline.setPaths(this.vertexs);
        polyline.setStrokeColor(this.color);
        polyline.setStrokeWeight(3);
        polyline.setStrokeOpacity(1);
    }

    private void initDirection() {
        e = true;
        w = false;
        n = false;
        create();
    }

    private void create() {
        dynamicLeg = minSearchLeg;
        Double jumlahLooping = Math.floor(width / trackSpacing);
        Double perbedaanJarakPerGaris = (maxSearchLeg - minSearchLeg) / jumlahLooping;
        for (int i = 1; i <= jumlahLooping; i++) {
            createNextEastOrWestPoint(perbedaanJarakPerGaris);
            if (i != jumlahLooping) {
                createNorthPoint();
            }
        }
        Double modulWidthAndS = width % trackSpacing;
        if (modulWidthAndS > (0.5 * trackSpacing)) {
            createResidualNorthPoint(modulWidthAndS);
            createResidualEastOrWestPoint();
        } else {
            dynamicLeg -= perbedaanJarakPerGaris;
        }
    }

    private void createNextEastOrWestPoint(Double perbedaanJarakPerGaris) {
        LatLng lastPoint, nextPoint;
        lastPoint = vertexs.get(vertexs.size() - 1);
        if (e) {
            nextPoint = new LatLng(lastPoint.getLat(),
                    lastPoint.getLng() + (CommonConstant.ONE_NM_TO_DEG * dynamicLeg));
            vertexs.add(nextPoint);
            dynamicLeg += perbedaanJarakPerGaris;
            e = false;
            n = true;
            NEXT_DIRECTION_FOR_NORTH = WEST;
        } else if (w) {
            nextPoint = new LatLng(lastPoint.getLat(),
                    lastPoint.getLng() - (CommonConstant.ONE_NM_TO_DEG * dynamicLeg));
            vertexs.add(nextPoint);
            dynamicLeg += perbedaanJarakPerGaris;
            w = false;
            n = true;
            NEXT_DIRECTION_FOR_NORTH = EAST;
        }
    }

    private void createNorthPoint() {
        LatLng nextPoint, lastPoint;
        lastPoint = vertexs.get(vertexs.size() - 1);
        nextPoint = new LatLng(lastPoint.getLat() + (CommonConstant.ONE_NM_TO_DEG * trackSpacing),
                lastPoint.getLng());
        vertexs.add(nextPoint);
        n = false;
        if (NEXT_DIRECTION_FOR_NORTH == EAST) {
            e = true;
        } else if (NEXT_DIRECTION_FOR_NORTH == WEST) {
            w = true;
        }
    }

    private void createResidualNorthPoint(Double residual) {
        LatLng nextPoint, lastPoint;
        lastPoint = vertexs.get(vertexs.size() - 1);
        nextPoint = new LatLng(lastPoint.getLat() + (CommonConstant.ONE_NM_TO_DEG * residual),
                lastPoint.getLng());
        vertexs.add(nextPoint);
    }

    private void createResidualEastOrWestPoint() {
        LatLng nextPoint, lastPoint;
        lastPoint = vertexs.get(vertexs.size() - 1);
        if (NEXT_DIRECTION_FOR_NORTH == EAST) {
            nextPoint = new LatLng(lastPoint.getLat(),
                    lastPoint.getLng() + (CommonConstant.ONE_NM_TO_DEG * dynamicLeg));
            vertexs.add(nextPoint);
        } else if (NEXT_DIRECTION_FOR_NORTH == WEST) {
            nextPoint = new LatLng(lastPoint.getLat(),
                    lastPoint.getLng() - (CommonConstant.ONE_NM_TO_DEG * dynamicLeg));
            vertexs.add(nextPoint);
        }
    }

    private void initPivot() {
        LatLng lastPoint = commonUtil.
                rotationLatLng(start, vertexs.get(vertexs.size() - 1), (360 - heading));
        LatLng beforeLastPoint = commonUtil.
                rotationLatLng(start, vertexs.get(vertexs.size() - 2), (360 - heading));
        LatLng sw, ne;
        if (lastPoint.getLng() > beforeLastPoint.getLng()) {
            ne = lastPoint;
            sw = new LatLng(start.getLat(), beforeLastPoint.getLng());
        } else {
            ne = beforeLastPoint;
            sw = new LatLng(start.getLat(), lastPoint.getLng());
        }
        Double latPivot, lngPivot;
        latPivot = sw.getLat() + ((ne.getLat() - sw.getLat()) / 2);
        lngPivot = sw.getLat() + ((ne.getLng() - sw.getLng()) / 2);
        pivot = commonUtil.rotationLatLng(start, new LatLng(latPivot, lngPivot), heading);
    }

    public String inFocus() {
        return pivot.getLat() + "," + pivot.getLng();
    }

    public Integer inZoom() {
        return 5;
    }
}
