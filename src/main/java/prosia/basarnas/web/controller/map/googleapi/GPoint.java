/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

/**
 *
 * @author PROSIA
 */
public class GPoint extends JavaScriptBase<GPoint>{

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

    /**
     * @return the x
     */
    public String getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public String getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(String y) {
        this.y = y;
    }

}
