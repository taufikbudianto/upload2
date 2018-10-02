/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import lombok.Data;
import prosia.basarnas.web.controller.map.googleapi.JavaScriptBase;

/**
 *
 * @author Aris
 */
public @Data class GPoint extends JavaScriptBase<GPoint> {
    private String x;
    private String y;


    
    public GPoint() {
    }

    public GPoint(String x, String y) {
        this.x = x;
        this.y = y;
    }

 

    public String toJavaScript() {
        return "new GPoint("+ getX() +","+ getY() +")";
    }


/**
 * @param x (String hasil execute Script dari Java Script)
 * @return Object Google Map API GPoint atau GLatLng dari x yang dipresentasikan kedalam Java GLatLng
 */
    public static GPoint toJava(String x){
        x = x.substring(1, x.length()-1);
        String[] x2 = x.split(",");
        return new GPoint(x2[0], x2[1]);
    }
}
