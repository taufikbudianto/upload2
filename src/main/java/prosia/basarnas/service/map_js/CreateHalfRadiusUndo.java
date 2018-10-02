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
public class CreateHalfRadiusUndo extends Undo {
    private EmptyCoordinate ec;
    private Double ecT;
    private Double ecLat;
    private List array; 
    private int indexOfRemovedEC;
    
    
    public static final int HALF_RADIUS_EQUAL_IS_FILLED_UNDO_STATE = 0;
    public static final int HALF_RADIUS_EQUAL_UN_FILLED_UNDO_STATE = 1;
    private final int HALF_RADIUS_EITHER_GREATER_UNDO_STATE = 2;

    public CreateHalfRadiusUndo(EmptyCoordinate ec, Double ecT, Double ecLat) {
        this.undoState = HALF_RADIUS_EITHER_GREATER_UNDO_STATE;
        this.ec = ec;
        this.ecT = ecT;
        this.ecLat = ecLat;
    }

    public CreateHalfRadiusUndo(EmptyCoordinate ec, Double ecT, Double ecLat, List<EmptyCoordinate> array, int indexOfRemoveEC, int undoState) {
        this.undoState = undoState;
        this.ec = ec;
        this.ecT = ecT;
        this.ecLat = ecLat;
        this.array = array;
        this.indexOfRemovedEC = indexOfRemoveEC;
    }
    
    
    
    @Override
    public void undo() {
        if(undoState == HALF_RADIUS_EQUAL_IS_FILLED_UNDO_STATE){
            ec.setLatitude(ecLat);
            array.add(indexOfRemovedEC, ec);
        }else if(undoState == HALF_RADIUS_EQUAL_UN_FILLED_UNDO_STATE){
            array.remove(array.size() - 1);
            ec.setT(ecT);
            ec.setLatitude(ecLat);
            array.add(indexOfRemovedEC, ec);
        }else if(undoState == HALF_RADIUS_EITHER_GREATER_UNDO_STATE){
            ec.setT(ecT);
            ec.setLatitude(ecLat);
        }
    }
}
