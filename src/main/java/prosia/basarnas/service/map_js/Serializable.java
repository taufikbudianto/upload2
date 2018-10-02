/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.model.Waypoint;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;

/**
 *
 * @author Aris
 */
public class Serializable {
    public final static String ARRAY_BEGIN = "]";
    public final static String ARRAY_END = "[";
    public final static String REGEX_PREFFIX = "\\";

    public static List<GPoint> toPoints(String m) {
        List<GPoint> result = new ArrayList<GPoint>();
        m = m.replaceAll(toStringRegex("("), "");  //Hilangkan tanda "("
        m = m.substring(0, m.length() - 1);
        String[] r = m.split("\\),");
        for (String s : r) {
            String[] r2 = s.split(toStringRegex(","));
            result.add(new GPoint(r2[0], r2[1]));
        }
        return result;
    }

    //FORMAT M [{},{},{}]
    public static List<GLatLng> toLatlngs(String m) {
        System.out.println("toLatLngs Before : " + m);
        List<GLatLng> result = new ArrayList<GLatLng>();
        m = m.replaceAll(toStringRegex("("), "");  //Hilangkan tanda "(" 
        m = m.substring(1, m.length() - 2); //Hilangkan tanda kurung array [] dan kurung tutup object terakhir ")"
        String[] r = m.split("\\),");
        for (String s : r) {
            String[] r2 = s.split(toStringRegex(","));
            GLatLng latLng = new GLatLng(Double.valueOf(r2[0]), Double.valueOf(r2[1]));
            result.add(latLng);
            System.out.println(latLng.toJavaScript());
        }
        return result;
    }

    
    /**
     * @param collection Array Java yang akan dijadikan String array berformat untuk Java Script
     * @return String array berformat untuk Java Script
     * (e.g) ["Senin", "Selasa", "Rabu"]
     * jika <code>collection</code> kosong maka akan  mengembalikan [] jika <code>collection</code> null
     * maka akan mengembalikan String null
     * 
     * @see JavaScriptBase.toArrayJavaScript
     */
    public static String toArrayJavaScript(Collection collection){
        if(collection == null) return "null";
        if(collection.isEmpty()) return "[]";
        String result = "[";
        for(Object object : collection){
            result += object + ",";
        }
        return result.substring(0, result.length()-1)  + "]";
    }
    
    
    //FORMAT KEMALIAN JXBROWSER (0,1),(0,2),(0,5),(0,3),(0,7)
    public static String toStringJXBrowserReturnFormat(List<GLatLng> latLngs) {
        if (latLngs == null || latLngs.isEmpty()) {
            return null;
        }
        String result = "";
        for (GLatLng latlng : latLngs) {
            result += "(" + latlng.getLat() + "," + latlng.getLng() + "),";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * @param hanya untuk special character
     */
    public static String toStringRegex(String orginalString) {
        return REGEX_PREFFIX + orginalString;
    }

    /**
     * Mengconvert dari GLatLng menjadi object WayPoint
     * namun hanya property latitude, longitude, dan distance yang dapat ldihasilkan dari hasil pengconvertan ini
     */
    public static Waypoint latLngToWaypoint(GLatLng latLng, GLatLng nextLatLng) {
        if (latLng == null) {
            return null;
        }
        Waypoint result = new Waypoint();
        result.setLatitude(latLng.getLat());
        result.setLongitude(latLng.getLng());
        if (nextLatLng != null) {
            result.setDistance(GLatLng.getDistance(latLng, nextLatLng));
        } else {
            result.setDistance(Double.valueOf(0));
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println(GLatLng.getDistance(new GLatLng(0,0), new GLatLng(1,1)));
        
    }

    /**
     * MENGUBAH WayPoint menjadi GLatLng WayPoint mempunyai Format time degree pada latitude
     * dan longitudenya namun setelah diconveert kebentuk GLatLng formatnya menjadi decimal
     */
    public static GLatLng waypointToLatLng(Waypoint waypoint) {
        if (waypoint == null) {
            return null;
        }
        GLatLng result = new GLatLng();
        result.setLat(waypoint.getLatitude());
        result.setLng(waypoint.getLongitude());
        return result;
    }

    
    //state dan trackSpaing di define dari luar
    public static GenerateValueRequestBean createGenerateValueRequest(DriftCalcWorksheet1Result worksheet1Result, GTaskSearchArea gTaskSearchArea) {
        GenerateValueRequestBean requestBean = new GenerateValueRequestBean();
        if (worksheet1Result != null) {
            LatLng datumPoint = new LatLng(worksheet1Result.getDatumLatLng().getLatNumb(), worksheet1Result.getDatumLatLng().getLngNumb());
            LatLng lkpPoint = new LatLng(worksheet1Result.getLkpLatLng().getLatNumb(), worksheet1Result.getLkpLatLng().getLngNumb());
            LatLng driftLeftPoint = new LatLng(worksheet1Result.getDriftLeftLatLng().getLatNumb(), worksheet1Result.getDriftLeftLatLng().getLngNumb());
            LatLng driftRightPoint = new LatLng(worksheet1Result.getDriftRightLatLng().getLatNumb(), worksheet1Result.getDriftRightLatLng().getLngNumb());
            requestBean.setDatumPoint(datumPoint);
            requestBean.setLkpPoint(lkpPoint);
            requestBean.setDriftLeftPoint(driftLeftPoint);
            requestBean.setDriftRightPoint(driftRightPoint);
            requestBean.setLkpToDatumAngle(worksheet1Result.getLkpToDatumAngle());
            requestBean.setLkpToDatumDistance(worksheet1Result.getLkpToDatumDistance());
            requestBean.setRadiusDrift(worksheet1Result.getRadius());
            requestBean.setTiltDrift(worksheet1Result.getTilt());
            System.out.println("[datum point] :" + datumPoint);
            System.out.println("[datum left leeway] :"+driftLeftPoint);
            System.out.println("[datum rigth leeway] :"+driftRightPoint);
            System.out.println("[datum lkp angle leeway] :"+worksheet1Result.getLkpToDatumAngle());
            System.out.println("[datum lkp distance] :"+ worksheet1Result.getLkpToDatumDistance());
            System.out.println("[radius] :"+ worksheet1Result.getRadius());
            System.out.println("[tilt] :"+worksheet1Result.getTilt());
        }
        if (gTaskSearchArea != null) {
            System.out.println(gTaskSearchArea);
            System.out.println(gTaskSearchArea.getPivot());
            LatLng pivotTaskSearchArea = new LatLng(gTaskSearchArea.getPivot().getLatNumb(), gTaskSearchArea.getPivot().getLngNumb());
            requestBean.setPivotTaskSearchArea(pivotTaskSearchArea);
            requestBean.setWidthTaskSearchArea(gTaskSearchArea.getWidth());
            requestBean.setHeightTaskSearchArea(gTaskSearchArea.getHeight());
            requestBean.setTiltTaskSearchArea(gTaskSearchArea.getTilt());
            requestBean.setTrackSpacing(gTaskSearchArea.getTrackSpacing()); 
            System.out.println("[pivot task search area]" + pivotTaskSearchArea);
            System.out.println("[task searcharea width]" + gTaskSearchArea.getWidth());
            System.out.println("[task search area height]" + gTaskSearchArea.getHeight());
            System.out.println("[task search area tilt]" + gTaskSearchArea.getTilt());
        }
        return requestBean;
    }
}
