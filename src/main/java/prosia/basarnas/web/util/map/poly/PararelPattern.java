/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map.poly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.web.util.map.CommonUtil;

/**
 *
 * @author Ismail
 */
public class PararelPattern implements Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private LatLng start;
    private double searchLeg;
    private double searchWidth;
    private double searchTrackSpacing;
    private double searchHeading;
    private LatLng pivot;
    private List<LatLng> latlngs;
    private String color;
    private Integer iterationCount;
    private Integer rotateState;
    private Boolean e;
    private Boolean w;
    private Boolean s;
    private LatLng end;
    private LatLng pos;
    private double LJ;
    private int ARROW;
    private int WEST = 1;
    private int EAST = 0;
    private Boolean isAllowProsesLastVertex;
    private List<LatLng> listLatLngs;
    @Getter
    private Polyline polyline = new Polyline();
    @Getter
    private String contentString;

    public PararelPattern(LatLng start, double searchLeg, double searchWidth,
            double searchTrackSpacing, double searchHeading,
            LatLng pivot, List<LatLng> latlngs, String color) {
        this.start = start;
        this.searchLeg = searchLeg;
        this.searchWidth = searchWidth;
        this.searchTrackSpacing = searchTrackSpacing;
        this.searchHeading = searchHeading;
        this.iterationCount = 0;
        this.rotateState = 0;
        this.latlngs = latlngs;
        this.color = color;

        if (latlngs == null || latlngs.isEmpty()) {
            init();
            List<LatLng> rotation = commonUtil.rotationLatLngs(
                    polyline.getPaths(), this.pivot, this.searchHeading);
            polyline.setPaths(rotation);

        } else {
            this.pivot = pivot;
            polyline.setPaths(latlngs);
        }
        contentString = "<b class='title'><u>Pararel Search</u></b>"
                + "<br><b class='field'>Search Leg</b>      : <font class='value'>" + searchLeg + " NM</font>"
                + "<br><b class='field'>Width </b> : <font class='value'>" + searchWidth + " NM</font>"
                + "<br><b class='field'>Track Spacing </b> : <font class='value'>" + searchTrackSpacing + " NM</font>"
                + "<br><b class='field'>Heading </b> : <font class='value'>" + searchHeading + " °</font>";
// + "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() 
        //                + "</font>"

    }

    private void init() {
        e = true;
        w = false;
        s = false;
        double h = searchWidth * CommonConstant.ONE_NM_TO_DEG, //Number
                w = this.searchLeg * CommonConstant.ONE_NM_TO_DEG, //Number
                endLat = this.start.getLat() - h, //Number
                endLng = this.start.getLng() + w; //Number

        pivot = new LatLng((this.start.getLat() - h / 2), (this.start.getLng() + w / 2)); //LatLng
        end = new LatLng(endLat, endLng); //LatLng
        create();
    }

    private void create() {
        listLatLngs = new ArrayList<>();
        pos = start;  //nilai this.pos akan dinamic maka start harus direference menjadi pos
        isAllowProsesLastVertex = false;
        listLatLngs.add(pos);
        LJ = 0.0; //Number
        ARROW = WEST;
        if (iterationCount == 0) {
            while (searchWidth > LJ) {
                pos = generateAngle();
                if (pos != null) {
                    listLatLngs.add(pos);
                    ++iterationCount;
                }
            }
            if (isAllowProsesLastVertex) {
                addLastVertex();
                ++iterationCount;
            }
        } else {
            int o;
            for (o = 0; o < iterationCount; ++o) {
                this.pos = generateAngle();
                listLatLngs.add(pos);
            }
        }
        createAllPolyLine(listLatLngs);
    }

    private LatLng generateAngle() {
        LatLng latlng = null;
        LatLng g = pos;
        double southLat = g.getLat() - (searchTrackSpacing * CommonConstant.ONE_NM_TO_DEG);
        if (e) {
            latlng = new LatLng(g.getLat(), end.getLng());
        } else if (s) { // pengechekan apakah kondisi pengambaran pattern di hentikan atau tidak
            if ((LJ + searchTrackSpacing) > searchWidth) { // tidak langsung ditambah saat conditional karna jumlah LJ menentukan garis terakhir
                double selisih = searchWidth - LJ;
                if (selisih > 1 / 2 * searchTrackSpacing) {
                    isAllowProsesLastVertex = true;
                    southLat = g.getLat() - (selisih * CommonConstant.ONE_NM_TO_DEG);
                    LJ += searchTrackSpacing; //hanya untuk membuat looping berhenti karna nilai LJ lebih besar
                } else {
                    this.isAllowProsesLastVertex = false;
                    LJ += searchTrackSpacing; //hanya untuk membuat looping berhenti karna nilai LJ lebih besar
                    return null;
                }
            } else {
                southLat = g.getLat() - (searchTrackSpacing * CommonConstant.ONE_NM_TO_DEG);
                LJ += searchTrackSpacing;
                isAllowProsesLastVertex = true;
            }
            latlng = new LatLng(southLat, g.getLng());
        } else if (w) {
            latlng = new LatLng(g.getLat(), this.start.getLng());
        }
        flowDirection();
        return latlng;
    }

    private void flowDirection() {
        if (e) {
            e = false;
            w = false;
            s = true;
        } else if (s) {
            if (ARROW == EAST) {
                e = true;
                w = false;
                s = false;
                ARROW = WEST;
            } else if (ARROW == WEST) {
                e = false;
                w = true;
                s = false;
                ARROW = EAST;
            }
        } else if (w) {
            e = false;
            w = false;
            s = true;
        }
    }

    private void addLastVertex() {
        double nextLng;
        LatLng next;
        if (this.ARROW == EAST) {

            nextLng = listLatLngs.get(listLatLngs.size() - 1).getLng()
                    - (CommonConstant.ONE_NM_TO_DEG * this.searchLeg);
            next = new LatLng(listLatLngs.get(listLatLngs.size() - 1).getLat(), nextLng);
            listLatLngs.add(next);
        } else if (this.ARROW == WEST) {
            nextLng = listLatLngs.get(listLatLngs.size() - 1).getLng()
                    + (CommonConstant.ONE_NM_TO_DEG * this.searchLeg);
            next = new LatLng(listLatLngs.get(listLatLngs.size() - 1).getLat(), nextLng);
            listLatLngs.add(next);
        }
    }

    public String inFocus() {
        return pivot.getLat() + "," + pivot.getLng();
    }

    public Integer inZoom() {
        return 10;
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
