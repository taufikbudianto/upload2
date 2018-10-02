/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import lombok.Data;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Aris
 */
public @Data class CalculationInterface {
    private GLatLng unrotateStart;
    private Double height;
    private Double width;

    public CalculationInterface(GLatLng unrotateStart, Double height, Double width) {
        this.unrotateStart = unrotateStart;
        this.height = height;
        this.width = width;
    }
    
    @Override
    public String toString() {
        String startUnrotateString = null;
        if(unrotateStart != null){
            startUnrotateString = unrotateStart.toString();
        }
        String result = "{"+
                "start :" + startUnrotateString +", width:"+ width +",height:"+ height
                +"}";
        return result;
    }
}
