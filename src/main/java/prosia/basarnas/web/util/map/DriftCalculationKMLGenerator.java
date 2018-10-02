/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import lombok.Getter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.constant.StatusConstant;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Ismail
 */
public class DriftCalculationKMLGenerator implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private Coordinate coordinate;
    @Getter
    private List<Marker> listMarkerArea;
    @Getter
    private List<Polyline> listLeewayPolylines;
    @Getter
    private List<Polyline> listShapeSquere;
    @Getter
    private Polyline lkpToDatum;
    @Getter
    private List<Circle> listShapeCircle;
   

    public DriftCalculationKMLGenerator(GenerateValueRequestBean requestBean) {
        listMarkerArea = new ArrayList<>();
        listLeewayPolylines = new ArrayList<>();
        listShapeSquere = new ArrayList<>();
        listShapeCircle = new ArrayList<>();
        if (requestBean.getState().equals(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA)) {
            coordinate = new Coordinate();

            String description = new StringBuilder("<b>Last Known Position</b><br>")
                    .append("<br>Latitude : ").append(
                    coordinate.getConvertDdToDms(String.valueOf(requestBean.getLkpPoint().getLat()), Boolean.TRUE))
                    .append("<br>Longitude : ").append(
                    coordinate.getConvertDdToDms(String.valueOf(requestBean.getLkpPoint().getLng()), Boolean.FALSE))
                    .append("<br>LKP-DATUM Distance : ").append(requestBean.getLkpToDatumDistance()).append(" NM")
                    .append("<br>LKP-DATUM Angle : ").append(requestBean.getLkpToDatumAngle()).append("°").toString();
            String iconLkpPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/images/lkp.png");
            createMarker(new LatLng(requestBean.getLkpPoint().getLat(), requestBean.getLkpPoint().getLng()),
                    "Insiden", description, iconLkpPath);
            if (requestBean.getDrawLeewayLine()) {
                description = new StringBuilder("<b>DATUM</b><br>")
                        .append("<br>Latitude : ").append(
                        coordinate.getConvertDdToDms(String.valueOf(requestBean.getDatumPoint().getLat()), Boolean.TRUE))
                        .append("<br>Longitude : ").append(
                        coordinate.getConvertDdToDms(String.valueOf(requestBean.getDatumPoint().getLng()), Boolean.FALSE))
                        .append("<br>DATUM-LKP Distance : ").append(requestBean.getLkpToDatumDistance()).append(" NM").toString();
                String iconDatumPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/images/datum.png");
                createMarker(new LatLng(requestBean.getDatumPoint().getLat(), requestBean.getDatumPoint().getLng()),
                        "Datum", description, iconDatumPath);

                createLeewayPolylines(requestBean.getLkpPoint(),
                        requestBean.getDriftLeftPoint(), "Left Leeway Line", "#FF0000");

                createLeewayPolylines(requestBean.getLkpPoint(),
                        requestBean.getDriftRightPoint(), "Right Leeway Line", "#FF0000");

                createLkpToDatum(requestBean.getLkpPoint(),
                        requestBean.getDatumPoint(), "LKP - Datum Line", "#F8F8FF");

                createLeewayPolylines(requestBean.getDriftLeftPoint(),
                        requestBean.getDriftRightPoint(), "Left Leeway - Right Leeway Line", "#FF0000");
            }
            searchAreaPlacemark(requestBean);
        } else if (requestBean.getState().equals(GenerateValueRequestBean.GenerateValueState.TASK_SEARCH_AREA)) {
           
        }
    }

    private void createMarker(LatLng poi, String name, String description, String icon) {
        listMarkerArea.add(new Marker(poi, name, description, icon));
    }

    private void createLeewayPolylines(
            LatLng firstPoint, LatLng lastPoint, String lineName, String color) {
        Polyline polyline = new Polyline();
        polyline.getPaths().add(new LatLng(firstPoint.getLat(),
                firstPoint.getLng()));
        polyline.getPaths().add(new LatLng(lastPoint.getLat(),
                lastPoint.getLng()));
        polyline.setStrokeColor(color);
        polyline.setStrokeWeight(3);
        polyline.setStrokeOpacity(1);
        listLeewayPolylines.add(polyline);
    }

    private void createLkpToDatum(LatLng firstPoint, LatLng lastPoint, String lineName, String color) {
        lkpToDatum = new Polyline();
        lkpToDatum.getPaths().add(new LatLng(firstPoint.getLat(),
                firstPoint.getLng()));
        lkpToDatum.getPaths().add(new LatLng(lastPoint.getLat(),
                lastPoint.getLng()));
        lkpToDatum.setStrokeColor(color);
        lkpToDatum.setStrokeWeight(3);
        lkpToDatum.setStrokeOpacity(1);
    }

    private void searchAreaPlacemark(GenerateValueRequestBean requestBean) {
        String strokeColor = null;
        if (requestBean.getState().equals(GenerateValueRequestBean.GenerateValueState.DRIFT_SEARCH_AREA)) {
            strokeColor = "#FF0000";
        } else if (requestBean.getState().equals(GenerateValueRequestBean.GenerateValueState.TASK_SEARCH_AREA)) {
            strokeColor = "#7FFF00";
        }

        List<LatLng> listSearchAreaPlacemark = SearchArea(requestBean);
        listSearchAreaPlacemark = commonUtil.rotationLatLngs(
                listSearchAreaPlacemark,
                new LatLng(requestBean.getDatumPoint().getLat(),
                        requestBean.getDatumPoint().getLng()),
                requestBean.getTiltDrift());
        Polyline polyline = new Polyline();
        int i = 0;
        requestBean.getVertexs().clear();
        for (LatLng poi : listSearchAreaPlacemark) {
            String name = "Point " + ++i;
            createMarker(poi, name, null, null);
            polyline.getPaths().add(poi);
            requestBean.getVertexs().add(poi);
        }
        LatLng poi = listSearchAreaPlacemark.get(0);
        if (requestBean.getShape() == null || requestBean.getShape().equals(StatusConstant.SEARCH_AREA_SQUARE_SHAPE)
                || requestBean.getShape().equals("MONTECARLO")) {
            polyline.getPaths().add(poi);
            polyline.setStrokeColor(strokeColor);
            polyline.setStrokeWeight(3);
            listShapeSquere.add(polyline);
        } else if (requestBean.getShape().equals(StatusConstant.SEARCH_AREA_CIRCLE_SHAPE)) {
            Circle circle = new Circle(
                    new LatLng(
                            requestBean.getLkpPoint().getLat(),
                            requestBean.getLkpPoint().getLng()),
                    (requestBean.getRadiusDrift() * 1.852) * 1000);
            circle.setStrokeColor("#FF0000");
            circle.setFillColor("#000000");
            circle.setFillOpacity(0.1);
            circle.setStrokeOpacity(1.0);
            circle.setStrokeWeight(5);
            listShapeCircle.add(circle);
        }
    }

    private List<LatLng> SearchArea(GenerateValueRequestBean requestBean) {
        List<LatLng> listGLatLng = new ArrayList<>();

        double radius = 0.0;
        double radToDec = requestBean.getRadiusDrift() * CommonConstant.ONE_NM_TO_DEG;
        listGLatLng.add(
                new LatLng(
                        requestBean.getDatumPoint().getLat() + radToDec,
                        requestBean.getDatumPoint().getLng() - radToDec));
        listGLatLng.add(
                new LatLng(
                        requestBean.getDatumPoint().getLat() + radToDec,
                        requestBean.getDatumPoint().getLng() + radToDec));
        listGLatLng.add(
                new LatLng(
                        requestBean.getDatumPoint().getLat() - radToDec,
                        requestBean.getDatumPoint().getLng() + radToDec));
        listGLatLng.add(
                new LatLng(
                        requestBean.getDatumPoint().getLat() - radToDec,
                        requestBean.getDatumPoint().getLng() - radToDec));

        return listGLatLng;
    }

   
}
