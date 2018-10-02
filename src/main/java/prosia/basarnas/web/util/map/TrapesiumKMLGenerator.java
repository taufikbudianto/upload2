/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import lombok.Getter;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Irfan Rofie
 */
public class TrapesiumKMLGenerator extends Serializable {

    private final CommonUtil commonUtil = new CommonUtil();
    private Coordinate coordinate;
    @Getter
    private List<Marker> listMarker;
    @Getter
    private List<Polyline> listPolyline;

    public TrapesiumKMLGenerator(List<TrapeziumSearchArea> listTrapeziumSearchArea) {
        listMarker = new ArrayList<>();
        listPolyline = new ArrayList<>();
        for (TrapeziumSearchArea tsa : listTrapeziumSearchArea) {
            createOverlay(tsa.getVertexs());
            createMarker(tsa);
        }
    }

    private void createMarker(TrapeziumSearchArea area) {
        coordinate = new Coordinate();
        String iconLkpPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/images/lkp.png");
        String iconDatumPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/images/datum.png");
        String descLkp = new StringBuilder("<b><u>Last Known Position</u></b>")
                .append("<br><b>Latitude</b> : ").append(
                coordinate.getConvertDdToDms(String.valueOf(area.getSmallRoundPivot().getLat()), Boolean.TRUE))
                .append("<br><b>Longitude</b> : ").append(
                coordinate.getConvertDdToDms(String.valueOf(area.getSmallRoundPivot().getLng()), Boolean.FALSE))
                .toString();
        String descDatum = new StringBuilder("<b><u>Datum Position</u></b>")
                .append("<br><b>Latitude</b> : ").append(
                coordinate.getConvertDdToDms(String.valueOf(area.getLargeRoundPivot().getLat()), Boolean.TRUE))
                .append("<br><b>Longitude</b> : ").append(
                coordinate.getConvertDdToDms(String.valueOf(area.getLargeRoundPivot().getLng()), Boolean.FALSE))
                .toString();
        listMarker.add(new Marker(area.getSmallRoundPivot(), null, descLkp, iconLkpPath));
        listMarker.add(new Marker(area.getLargeRoundPivot(), null, descDatum, iconDatumPath));
    }

    private void createOverlay(List<LatLng> paths) {
        Polyline polyline = new Polyline();
        polyline.setPaths(paths);
        polyline.setStrokeColor("#FF0000");
        polyline.setStrokeWeight(3);
        polyline.setStrokeOpacity(1);
        polyline.getPaths().add(paths.get(0));
        listPolyline.add(polyline);
    }
}
