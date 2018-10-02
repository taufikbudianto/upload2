/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.util.List;
import org.primefaces.model.map.LatLng;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author PROSIA
 */
public class GLatLng extends JavaScriptBase<GLatLng>{
    private String lat;
    private String lng;
    
    @Override
    public String toString() {
        return "("+ lat + ","+ lng +")";
    }
    
    public GLatLng() {

    }
    
    public static void main(String[] args) {
    }

    public GLatLng(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public GLatLng(double lat, double lng){
        this.lat = String.valueOf(lat);
        this.lng = String.valueOf(lng);
    }

    /**
     * @return mengembalikan pasangan nilai lat dan lng kedalam bentuk Double array
     */
    public static Double[] toDouble(GLatLng latlng){
        double lat = Double.valueOf(latlng.getLat());
        double lng = Double.valueOf(latlng.getLng());
        return new Double[]{lat,lng};
    }

/**
 * @param x (String hasil execute Script dari Java Script)
 * @return Object Google Map API GPoint atau GLatLng dari x yang dipresentasikan kedalam Java GLatLng
 */
    
    public static GLatLng toJava(String x){
        try {
            x = x.substring(1, x.length()-1);
            String[] x2 = x.split(",");
            return new GLatLng(x2[0], x2[1]);
        } catch (Exception e) {
            return null;
        }
    }

/**
 * Jika ingin dijadikan parameter function di JavaScript class ini bisa langsung
 * memanggil method ini.
 */
    public String toJavaScript(){
        return "new GLatLng("+ getLat() +","+ getLng() +")";
    }


    /**
     * Mendapatkan Jarak dari dua Object GLatLng dalam satuan meter
     */
    public static double getDistance(GLatLng latLng1, GLatLng latLng2){
        double lat1 = Double.valueOf(latLng1.getLat());
        double lng1 = Double.valueOf(latLng1.getLng());
        double lat2 = Double.valueOf(latLng2.getLat());
        double lng2 = Double.valueOf(latLng2.getLng());

        return  MapCalculation.getDistance(lat1, lng1, lat2, lng2);
    }
    
    
    /**
     * convert to GLatLng
     * @see gLatLngToLatLong(GLatLng latLng)
     */
    public static GLatLng latLngToGLatLng(LatLng latLng){
        return new GLatLng(latLng.getLat(), latLng.getLng());
    }

    /**
     * convert to LatLng
     * @see latLngToGLatLng(LatLng latLng)
     */
    public static LatLong gLatLngToLatLong(GLatLng latLng) throws Exception{
        return new LatLong(latLng.getLatNumb(), latLng.getLngNumb());
    }
    
    
    
    /**
     * @param glatlngStrings kumpulan String dengan format (lat,long)
     * @return String berformat array GLatLng dalam bentuk java Script (e.i) [new GLatLng(6.0,110.0), new GLatLng(4.234,110.013), new GLatLng(3.092, 110.382932), new GLatLng(11.283922, 110.198293)]
     */
    public static String joinToArrayGLatLng(String... glatlngStrings){
        String result = "[";
        for(String glatlngString : glatlngStrings){
            result += "new GLatLng" + glatlngString +","; 
        }
        result = result.substring(0, result.length()-1) + "]";
        return result;
    }
    
    /**
     * methode ini untuk mendapatkan titik pusat dari titik-titik coordinat sphasial
     * @param points titik-titik coordinat yang akan
     * @return titik pusat dari titik-titik coordinat
     */
    public static GLatLng getPivotFromMuchCoodninate(List<GLatLng> points){
        Double topLat = points.get(0).getLatNumb();
        Double bottomLat = topLat;
        Double rightLng = points.get(0).getLngNumb();
        Double leftLng = rightLng;
        for(int i = 1;i<points.size(); ++i){
            Double z = points.get(i).getLatNumb();
            if(z > topLat){
                topLat = z;
            }else if(z < bottomLat){
                bottomLat = z;
            }
            z = points.get(i).getLngNumb();
            if(z > rightLng){
                rightLng = z;
            }else if(z < leftLng){
                leftLng = z;
            }
        }
        Double lat = (topLat - bottomLat) / 2 + bottomLat;
        Double lng = (rightLng - leftLng) / 2 + leftLng;
        return new GLatLng(lat, lng);
    }
    
    
    
    
    /**
     * @return the lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public String getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    
    public Double getLatNumb(){
        return Double.valueOf(lat);
    }
    
    public Double getLngNumb(){
        return Double.valueOf(lng);
    }
    
    

}
