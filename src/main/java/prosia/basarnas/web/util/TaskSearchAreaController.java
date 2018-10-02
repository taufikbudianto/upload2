/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import prosia.basarnas.service.map_js.Undo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import prosia.basarnas.service.map_js.Create1RadiusUndo;
import prosia.basarnas.service.map_js.CreateHalfRadiusUndo;
import prosia.basarnas.service.map_js.GTaskSearchArea;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Aris
 */
public class TaskSearchAreaController {
    private Double radiusSA;
    private Double sisiSA;
    private Double lng1SA;
    private Double lng2SA;
    private Double startLat;
    private List<Undo> undoList;
    private List<EmptyCoordinate> lng1Empties;
    private List<EmptyCoordinate> lng2Empties;
    private List<EmptyCoordinate> lngOneRadiusType;
    private double resedualLng1;
    private double resedualLng2;
    public static int countTaskSearchArea;
    public static GLatLng nextLng2SA;
    
    public TaskSearchAreaController(Double radiusSA, Double lng1SA, Double lng2SA, Double startLat) {
        System.out.println("--radiusSA--"+radiusSA);
        System.out.println("--lng1SA--"+lng1SA);
        System.out.println("--lng2SA--"+lng2SA);
        System.out.println("--startLat--"+startLat);
        this.radiusSA = radiusSA;
        this.lng1SA = lng1SA;
        this.lng2SA = lng2SA;
        this.startLat = startLat;
        init();
    }

    private void init() {
        this.sisiSA = this.radiusSA * 2;
        resedualLng1 = sisiSA;
        resedualLng2 = sisiSA;
        undoList = new ArrayList<>();
        lng1Empties = new ArrayList<>();
        lng2Empties = new ArrayList<>();
        lngOneRadiusType = new ArrayList<>();
        lng1Empties.add(new EmptyCoordinate(startLat, sisiSA));
        lng2Empties.add(new EmptyCoordinate(startLat, sisiSA));
    }

    public CalculationInterface insertOneRadius(Double endurance) {
        Double t = endurance / sisiSA;
        CalculationInterface ci = new CalculationInterface(null, t, sisiSA);
        calculateOneRadius(ci);
        if (ci.getUnrotateStart() != null) {
            return ci;
        } else {
            return null;
        }
    }

    public CalculationInterface insertHalfRadius(Double endurance) {
        Double t = endurance / radiusSA;
        System.out.println("--endurance--"+endurance);
        System.out.println("--t--"+t);
        CalculationInterface ci = new CalculationInterface(nextLng2SA, t, radiusSA);
        calculateHalfRadius(ci);
        countTaskSearchArea++;
        if (ci.getUnrotateStart() != null) {
            return ci;
        } else {
            return null;
        }
    }

    private void calculateOneRadius(CalculationInterface ci) {
        GLatLng result;
        EmptyCoordinate ec1 = lng1Empties.get(lng1Empties.size() - 1);
        EmptyCoordinate ec2 = lng2Empties.get(lng2Empties.size() - 1);
        if (ec1.getT() < ci.getHeight()) {
            if (isValueDoubleEquals(ec1.getT(), Double.valueOf(0))) {
                return;
            }
            ci.setHeight(ec1.getT());
        }
        if (ec2.getT() < ci.getHeight()) {
            if (isValueDoubleEquals(ec2.getT(), Double.valueOf(0))) {
                return;
            }
            ci.setHeight(ec2.getT());
        }
        if (ec1.getLatitude() < ec2.getLatitude()) {
            ci.setUnrotateStart(new GLatLng(ec1.getLatitude().toString(), lng1SA.toString()));
            lngOneRadiusType.add(new EmptyCoordinate(ec1.getLatitude(), ci.getHeight()));
            Double ec2T = ec2.getT();
            ec2.setT((ec2.getLatitude() - ec1.getLatitude()) * 60);
            Double ec1Lat = ec1.getLatitude();
            ec1.setLatitude(ec1.getLatitude() - (ci.getHeight() / 60));
            Double ec1T = ec1.getT();
            ec1.setT(ec1.getT() - ci.getHeight());
            lng2Empties.add(new EmptyCoordinate(ec1.getLatitude(), ec1.getT()));
            undoList.add(new Create1RadiusUndo(lngOneRadiusType, ec1, ec2, ec1T, ec1Lat, ec2T, lng2Empties));
        } else if (isValueDoubleEquals(ec1.getLatitude(), ec2.getLatitude())) {
            ci.setUnrotateStart(new GLatLng(ec1.getLatitude().toString(), lng1SA.toString()));
            lngOneRadiusType.add(new EmptyCoordinate(ec1.getLatitude(), ci.getHeight()));
            Double ec1Lat = ec1.getLatitude();
            ec1.setLatitude(ec1.getLatitude() - (ci.getHeight() / 60));
            Double ec1T = ec1.getT();
            ec1.setT(ec1.getT() - ci.getHeight());
            Double ec2Lat = ec2.getLatitude();
            ec2.setLatitude(ec1.getLatitude());
            Double ec2T = ec2.getT();
            ec2.setT(ec1.getT());
            undoList.add(new Create1RadiusUndo(lngOneRadiusType, ec1, ec2, ec1T, ec1Lat, ec2T, ec2Lat));
        } else {
            ci.setUnrotateStart(new GLatLng(ec2.getLatitude().toString(), lng1SA.toString()));
            lngOneRadiusType.add(new EmptyCoordinate(ec2.getLatitude(), ci.getHeight()));
            Double ec1T = ec1.getT();
            ec1.setT((ec1.getLatitude() - ec2.getLatitude()) * 60);
            Double ec2Lat = ec2.getLatitude();
            ec2.setLatitude(ec2.getLatitude() - (ci.getHeight() / 60));
            Double ec2T = ec2.getLatitude();
            ec2.setT(ec2.getT() - ci.getHeight());
            lng1Empties.add(new EmptyCoordinate(ec2.getLatitude(), ec2.getT()));
            undoList.add(new Create1RadiusUndo(lngOneRadiusType, ec1, ec2, ec1T, ec2T, ec2Lat, lng1Empties, true));
        }
    }

    private void calculateHalfRadius(CalculationInterface ci) {
        System.out.println("--lng1Empties--"+lng1Empties);
        System.out.println("--lng2Empties--"+lng2Empties);
        System.out.println("--countTaskSearchArea--"+countTaskSearchArea);
        for (EmptyCoordinate ec1 : lng1Empties) {
            for (EmptyCoordinate ec2 : lng2Empties) {
//                if (ec1.getLatitude() < ec2.getLatitude()) {
                if(countTaskSearchArea > 0){
                    validasiAndGetResult(ec2, ci, lng2Empties, lng2SA);
                    if (ci.getUnrotateStart() != null) {
                        return;
                    }
                }
            }
            validasiAndGetResult(ec1, ci, lng1Empties, lng1SA);
            if (ci.getUnrotateStart() != null) {
                return;
            }
        }
        return;
    }

    private void validasiAndGetResult(EmptyCoordinate ec, CalculationInterface ci, List array, Double lng) {
        GLatLng result;
        if (ec.getT() < ci.getHeight()) {
            if (isValueDoubleEquals(ec.getT(), Double.valueOf(0))) {
                return;
            }
            ci.setHeight(ec.getT());
        }
        if (isValueDoubleEquals(ec.getT(), ci.getHeight())) {
            ci.setUnrotateStart(new GLatLng(ec.getLatitude().toString(), lng.toString()));
            System.out.println("-0-ci.setUnrotateStart--"+ci.getUnrotateStart());
            int indexOfRemovedEC = array.indexOf(ec);
            array.remove(ec);
            Double ecLat = ec.getLatitude();
            ec.setLatitude(ec.getLatitude() - (ci.getHeight() / 60));
            System.out.println("-0-ec.setLatitude--"+ec.getLatitude());
            boolean isFilled = false;
            for (EmptyCoordinate ecOneRadiusType : lngOneRadiusType) {
                if (isValueDoubleEquals(ecOneRadiusType.getLatitude(), ec.getLatitude())) {
                    isFilled = true;
                    break;
                }
            }
            Double ecT = ec.getT();
            if (!isFilled) {
                ec.setT(sisiSA - ((startLat - ec.getLatitude()) * 60));
                array.add(ec);
                undoList.add(new CreateHalfRadiusUndo(ec, ecT, ecLat, array, indexOfRemovedEC, CreateHalfRadiusUndo.HALF_RADIUS_EQUAL_UN_FILLED_UNDO_STATE));
            } else {
                undoList.add(new CreateHalfRadiusUndo(ec, ecT, ecLat, array, indexOfRemovedEC, CreateHalfRadiusUndo.HALF_RADIUS_EQUAL_IS_FILLED_UNDO_STATE));
            }
        } else if (ci.getHeight() < ec.getT()) {
            ci.setUnrotateStart(new GLatLng(ec.getLatitude().toString(), lng.toString()));
            System.out.println("-1-ci.setUnrotateStart--"+ci.getUnrotateStart());
            Double ecT = ec.getT();
            ec.setT(ec.getT() - ci.getHeight());
            System.out.println("-1-ec.setT--"+ec.getT());
            Double ecLat = ec.getLatitude();
            System.out.println("-x-ec.getLatitude()--"+ec.getLatitude());
            nextLng2SA = ci.getUnrotateStart();
            System.out.println("-x-ci.getHeight()--"+ci.getHeight());
            ec.setLatitude(ec.getLatitude() - (ci.getHeight() / 60));
            System.out.println("-1-ec.setLatitude--"+ec.getLatitude());
            
            undoList.add(new CreateHalfRadiusUndo(ec, ecT, ecLat));
        }
    }

    public void clear() throws Exception {
        lng1Empties.clear();
        lng2Empties.clear();
        lng1Empties.add(new EmptyCoordinate(startLat, sisiSA));
        lng2Empties.add(new EmptyCoordinate(startLat, sisiSA));
        lngOneRadiusType.clear();
    }

    public void removeLast() throws Exception {
        int lastIndexUndo = undoList.size() - 1;
        undoList.get(lastIndexUndo).undo();
        undoList.remove(lastIndexUndo);
    }

    public TaskSearchAreaController() {
    }

    private boolean isValueDoubleEquals(Double d1, Double d2) {
        Double d3 = d1 - d2;
        if (d3.doubleValue() == 0 || d3 < 0.0000000000001 && d3 > 0.000000000000000000001 || d3 > -0.0000000000001 && d3 < -0.000000000000000000001) {
            return true;
        } else {
            return false;
        }
    }

    
}
