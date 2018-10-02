/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import prosia.basarnas.enumeration.ECoordinate;
import prosia.basarnas.enumeration.MapPattern;
import prosia.basarnas.model.Waypoint;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.model.map.SearchPatternParameter;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.web.util.map.poly.PararelPattern;
import prosia.basarnas.web.util.map.poly.PararelTrapeziumPattern;
import prosia.basarnas.web.util.map.poly.SectorPattern;
import prosia.basarnas.web.util.map.poly.SquarePattern;
import prosia.basarnas.web.util.map.poly.TSNPattern;
import prosia.basarnas.web.util.map.poly.TSRPattern;

/**
 *
 * @author Ismail
 */
public class ProcessPattern implements java.io.Serializable {

    private GisCalcES gisCalcES;

    public ProcessPattern() {
        gisCalcES = new GisCalcES();
    }

    public Coordinate convCoordinate(Double latlng, ECoordinate eCoordinate) {
        Coordinate coordinate = new Coordinate();
        coordinate.setType(eCoordinate.isType());
        coordinate.setDecimalDegrees(latlng);
        coordinate.setCounter(1);
        coordinate.converter();
        coordinate.setCounter(3);
        return coordinate;
    }

    public SearchPatternParameter setPatternTypeTrapez(TrapeziumSearchArea searchArea) {
        SearchPatternParameter patternParameter = new SearchPatternParameter();
        patternParameter = gisCalcES.generatePararelValue(searchArea);
        patternParameter.setType(MapPattern.TRAPEZIUM_PARAREL_SEARCH);
        return patternParameter;
    }

    public SearchPatternParameter setPatternTypeChange(GenerateValueRequestBean generateValueRequestBean, MapPattern type) {
        SearchPatternParameter patternParameter = new SearchPatternParameter();
        if (type.equals(MapPattern.SECTOR_SEARCH)) {
            patternParameter = gisCalcES.generateSectorValue(generateValueRequestBean);
        } else if (type.equals(MapPattern.EXPANDING_SQUARE_SEARCH)) {
            patternParameter = gisCalcES.generateSquareValue(generateValueRequestBean);
        } else if (type.equals(MapPattern.PARAREL_SEARCH)) {
            patternParameter = gisCalcES.generatePararelValue(generateValueRequestBean);
        } else if (type.equals(MapPattern.TRACK_SEARCH_RETURN_SEARCH)) {
            patternParameter = gisCalcES.generateTSRValue(generateValueRequestBean);
        } else if (type.equals(MapPattern.TRACK_SEARCH_NON_RETURN_SEARCH)) {
            patternParameter = gisCalcES.generateTSNValue(generateValueRequestBean);
        } else if (type.equals(MapPattern.TRAPEZIUM_PARAREL_SEARCH)) {
            patternParameter = gisCalcES.generatePararelValue(generateValueRequestBean);
        }
        patternParameter.setType(type);
        return patternParameter;
    }

    public SearchPatternParameter createPattern(SearchPatternParameter patternParameter,
            Double startLatitude, Double startLongitude) {
        patternParameter.setStart(new LatLng(startLatitude, startLongitude));

        JSONObject json = new JSONObject();
        switch (patternParameter.getType()) {
            case SECTOR_SEARCH:
                SectorPattern sectorPattern = new SectorPattern(
                        new LatLng(patternParameter.getStart().getLat(), patternParameter.getStart().getLng()),
                        patternParameter.getSearchRadius(), patternParameter.getHeading(),
                        new LatLng(patternParameter.getPivot().getLat(),
                                patternParameter.getPivot().getLng()),
                        null, "#" + patternParameter.getHexColor());
                patternParameter.setPolyline(sectorPattern.getPolyline());
                patternParameter.setMarkers(sectorPattern.getMarkers());
                patternParameter.setCenterGeoMap(sectorPattern.inFocus());
                patternParameter.setZoomGeoMap(sectorPattern.inZoom());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Search_Radius", patternParameter.getSearchRadius());
                json.put("Heading", patternParameter.getHeading());
                json.put("Pivot", new JSONObject(patternParameter.getPivot()));
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;
            case EXPANDING_SQUARE_SEARCH:
                SquarePattern squarePattern = new SquarePattern(
                        patternParameter.getTrackSpacing(), patternParameter.getSearchRadius(),
                        patternParameter.getStart(),
                        patternParameter.getHeading(), null, "#" + patternParameter.getHexColor());
                patternParameter.setPolyline(squarePattern.getPolyline());
                patternParameter.setCenterGeoMap(squarePattern.inFocus());
                patternParameter.setZoomGeoMap(squarePattern.inZoom());
                json.put("Track_Spacing", patternParameter.getTrackSpacing());
                json.put("Search_Radius", patternParameter.getSearchRadius());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Heading", patternParameter.getHeading());
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;
            case PARAREL_SEARCH:
                PararelPattern pararelPattern
                        = new PararelPattern(
                                patternParameter.getStart(),
                                patternParameter.getSearchLeg(), patternParameter.getWidth(),
                                patternParameter.getTrackSpacing(), patternParameter.getHeading(),
                                patternParameter.getPivot(),
                                null, "#" + patternParameter.getHexColor());
                patternParameter.setPolyline(pararelPattern.getPolyline());
                patternParameter.setCenterGeoMap(pararelPattern.inFocus());
                patternParameter.setZoomGeoMap(pararelPattern.inZoom());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Search_Leg", patternParameter.getSearchLeg());
                json.put("Width", patternParameter.getWidth());
                json.put("Track_Spacing", patternParameter.getTrackSpacing());
                json.put("Heading", patternParameter.getHeading());
                json.put("Pivot", new JSONObject(patternParameter.getPivot()));
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;
            case TRACK_SEARCH_RETURN_SEARCH:
                TSRPattern tsrPattern = new TSRPattern(
                        patternParameter.getStart(),
                        patternParameter.getTrackSpacing(), patternParameter.getSearchLeg(),
                        patternParameter.getHeading(),
                        patternParameter.getPivot(),
                        null, "#" + patternParameter.getHexColor());
                patternParameter.setPolyline(tsrPattern.getPolyline());
                patternParameter.setCenterGeoMap(tsrPattern.inFocus());
                patternParameter.setZoomGeoMap(tsrPattern.inZoom());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Track_Spacing", patternParameter.getTrackSpacing());
                json.put("Search_Leg", patternParameter.getSearchLeg());
                json.put("Heading", patternParameter.getHeading());
                json.put("Pivot", new JSONObject(patternParameter.getPivot()));
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;

            case TRACK_SEARCH_NON_RETURN_SEARCH:
                TSNPattern tsnPattern = new TSNPattern(
                        patternParameter.getStart(),
                        patternParameter.getTrackSpacing(), patternParameter.getSearchLeg(),
                        patternParameter.getHeading(),
                        patternParameter.getPivot(),
                        null, "#" + patternParameter.getHexColor());
                patternParameter.setPolyline(tsnPattern.getPolyline());
                patternParameter.setCenterGeoMap(tsnPattern.inFocus());
                patternParameter.setZoomGeoMap(tsnPattern.inZoom());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Track_Spacing", patternParameter.getTrackSpacing());
                json.put("Search_Leg", patternParameter.getSearchLeg());
                json.put("Heading", patternParameter.getHeading());
                json.put("Pivot", new JSONObject(patternParameter.getPivot()));
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;
            case TRAPEZIUM_PARAREL_SEARCH:
                PararelTrapeziumPattern ptPattern = new PararelTrapeziumPattern(
                        patternParameter.getStart(), patternParameter.getMinSearchLeg(),
                        patternParameter.getMaxSearchLeg(), patternParameter.getWidth(),
                        patternParameter.getTrackSpacing(), patternParameter.getHeading(),
                        "#" + patternParameter.getHexColor(), null);
                patternParameter.setPolyline(ptPattern.getPolyline());
                patternParameter.setCenterGeoMap(ptPattern.inFocus());
                patternParameter.setZoomGeoMap(ptPattern.inZoom());
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Min_Search_Leg", patternParameter.getMinSearchLeg());
                json.put("Max_Search_Leg", patternParameter.getMaxSearchLeg());
                json.put("Width", patternParameter.getWidth());
                json.put("Track_Spacing", patternParameter.getTrackSpacing());
                json.put("Heading", patternParameter.getHeading());
                json.put("Hex_Color", patternParameter.getHexColor());
                patternParameter.setDescription(json.toString());
                break;
            case SEARCH_PATTERN_FREE_DEFINE:
                json.put("Start", new JSONObject(patternParameter.getStart()));
                json.put("Pivot", new JSONObject(patternParameter.getPivot()));
//                json.put("ParentID", new JSONObject("Dont Have Parrent"));
                patternParameter.setDescription(json.toString());
                break;
        }
        return patternParameter;
    }

}
