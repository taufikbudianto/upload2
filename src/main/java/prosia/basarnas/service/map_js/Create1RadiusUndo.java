/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.util.List;
import prosia.basarnas.web.util.EmptyCoordinate;

/**
 *
 * @author Aris
 */
public class Create1RadiusUndo extends Undo {
    private List<EmptyCoordinate> lngOneRadiusType;
    private EmptyCoordinate ec1;
    private EmptyCoordinate ec2;
    private Double ec1T;
    private Double ec1Lat;
    private Double ec2T;
    private Double ec2Lat;
    private List<EmptyCoordinate> lng1Empties;
    private List<EmptyCoordinate> lng2Empties; 
    
    private final int RADIUS_LNG1_MORE_GREAT_UNDO_STATE = 0;
    private final int RADIUS_LNG2_MORE_GREAT_UNDO_STATE = 1;
    private final int RADIUS_LNG2_LNG1_EQUAL_UNDO_STATE = 2;

    
    /**
     * Untuk jika e1 Latitude libih kecil dibandingkan ddengan e2 Latitude
     */
    public Create1RadiusUndo(List<EmptyCoordinate> lngOneRadiusType, EmptyCoordinate ec1, EmptyCoordinate ec2, Double ec1T, Double ec1Lat, Double ec2T, List<EmptyCoordinate> lng2Empties) {
        this.undoState = RADIUS_LNG2_MORE_GREAT_UNDO_STATE;
        this.lngOneRadiusType = lngOneRadiusType;
        this.ec1 = ec1;
        this.ec2 = ec2;
        this.ec1T = ec1T;
        this.ec1Lat = ec1Lat;
        this.ec2T = ec2T;
        this.lng2Empties = lng2Empties;
    }

    /**
     * Untuk jika e1 Latitude sama dengan e2 Latitude
     */
    public Create1RadiusUndo(List<EmptyCoordinate> lngOneRadiusType, EmptyCoordinate ec1, EmptyCoordinate ec2, Double ec1T, Double ec1Lat, Double ec2T, Double ec2Lat) {
        this.undoState = RADIUS_LNG2_LNG1_EQUAL_UNDO_STATE;
        this.lngOneRadiusType = lngOneRadiusType;
        this.ec1 = ec1;
        this.ec2 = ec2;
        this.ec1T = ec1T;
        this.ec1Lat = ec1Lat;
        this.ec2T = ec2T;
        this.ec2Lat = ec2Lat;
    }
    
    
    
    /*
     * isLng1moreGreat hanya untuk editor tidak ambigu
     */
    /**
     * Untuk jika e1 Latitude libih besar dibandingkan ddengan e2 Latitude
     */
    public Create1RadiusUndo(List<EmptyCoordinate> lngOneRadiusType, EmptyCoordinate ec1, EmptyCoordinate ec2, Double ec1T, Double ec2T, Double ec2Lat, List<EmptyCoordinate> lng1Empties, boolean isLng1moreGreat) {
        this.undoState = RADIUS_LNG1_MORE_GREAT_UNDO_STATE;
        this.lngOneRadiusType = lngOneRadiusType;
        this.ec1 = ec1;
        this.ec2 = ec2;
        this.ec1T = ec1T;
        this.ec2T = ec2T;
        this.ec2Lat = ec2Lat;
        this.lng1Empties = lng1Empties;
    }

    
    
    
    
    @Override
    public void undo() {
        if(undoState == RADIUS_LNG1_MORE_GREAT_UNDO_STATE){
            lngOneRadiusType.remove(lngOneRadiusType.size() - 1);
             ec2.setLatitude(ec2Lat);
             ec2.setT(ec2T);
             ec1.setT(ec1T);
             lng1Empties.remove(lng1Empties.size() - 1); 
        }else if(undoState == RADIUS_LNG2_LNG1_EQUAL_UNDO_STATE){
             lngOneRadiusType.remove(lngOneRadiusType.size() - 1);
             ec2.setLatitude(ec2Lat);
             ec1.setLatitude(ec1Lat);
             ec2.setT(ec2T);
             ec1.setT(ec1T);
        }else if(undoState == RADIUS_LNG2_MORE_GREAT_UNDO_STATE){
             lngOneRadiusType.remove(lngOneRadiusType.size() - 1);
             ec1.setLatitude(ec1Lat);
             ec2.setT(ec2T);
             ec1.setT(ec1T);
             lng2Empties.remove(lng2Empties.size() - 1); 
        }
    }
    
}
