/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author Aris
 */
public class MapCalculation {
    public final static int TIME_DEGREE = 0;
    public final static int DECIMAL_DEGREE = 1;
    public final static String KM = "KM";
    public final static String NAUTICAL_MILES = "NM";
    public final static String METER = "Meter";
    public final static String YARD = "Yard";

    /**
     * @param x1 double dari latitude object pertama
     * @param x2 double dari longitude object kedua
     * @param y1 double dari latitude object pertama
     * @param y2 double dari longitude object kedua
     * @return double jarak dari kedua object berdasarkan permukaan bumi yang rata
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371; // earth's mean radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLong / 2)
                * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }

    /**
     * Mengconvert bentuk Kilometer menjadi String dengan format "kmValue . mValue KM"
     * contoh : dengan parameter 192.9281934249 akan mengembalikan return 192.928 KM; 
     * @param d (Double Kilometer)
     */
    private static String formatKM(double d) {
        String m = String.valueOf(d);
        if (m.contains(".")) {
            int b = m.indexOf(".");
            String KM = m.substring(0, b),
                    M = m.substring(++b, b + 3);
            return KM + "." + M + " KM";
        }
        return m;
    }

    /**
     * untuk mengetahui apakah resource masuk dalam radius yang ditentukan dari incedent
     * @param r Object Resource
     * @param latInc (latitude dari Incident)
     * @param lngInc (longitude dari Incident)
     * @param rad (Radius Pencarian)
     *
     * @return true jika resource termasuk dalam radius yang ditentukan, false jika sebaliknya
     */
    /*
    public boolean valid(Resource r, double latInc, double lngInc, double rad) {
        double lat = Double.valueOf(
                r.getLat()),
                lng = Double.valueOf(
                r.getLng());
        if (rad >= getDistance(latInc, lngInc, lat, lng)) {
            return true;
        }

        return false;
    }
*/
    public static String convertTo(int format, String value) {
        String result = "",
                z = value;
        double x;
        String[] a;
        if (format == TIME_DEGREE) {
            for (int i = 0; i < 3; ++i) {
                a = z.split("\\.");
                result += a[0] + "'";
                z = "0." + a[1];
                x = Double.valueOf(z) * 60;
                z = String.valueOf(x);
            }
            result = result.substring(0, result.length() - 1);
        } else if (format == DECIMAL_DEGREE) {
            a = value.split("'");
            x = Double.valueOf(a[2]);
            for (int i = 2; i >= 1; --i) {
                x = x / 60;
                x = Double.valueOf(a[i - 1]) + x;
            }
            result = String.valueOf(x);
        }
        return result;
    }

    public static Double convertToKm(String satuan, double value) {
        if (satuan.equalsIgnoreCase(KM)) {
            return value;
        } else if (satuan.equalsIgnoreCase(NAUTICAL_MILES)) {
            return value * 1.85200;
        } else if (satuan.equalsIgnoreCase(METER)) {
            return value / 1000;
        } else if (satuan.equalsIgnoreCase(YARD)) {
            return value / 914400;
        }
        return null;
    }
    // [START]
//    public static final double EARTH_RADIUS_IN_KM = 6371;  // in km
//    public static final double EARTH_RADIUS_IN_NM = 3440.07;  // in nm (6371 / 1.852 = 3440.064794816415)
//    public static final double EARTH_RADIUS_IN_MI = 3959;  // in mile (6371 / 1.852 = 3440.064794816415) * 1.15077945
    public static final double EARTH_RADIUS_IN_KM = 6378.137;  // in km
    public static final double EARTH_RADIUS_IN_NM = 3443.92;  // in nm (6378.137 / 1.852 = 3443.918466522678)
    public static final double EARTH_RADIUS_IN_MI = 3963.19;  // in mile (6378.137 / 1.852 = 3443.918466522678) * 1.15077945 = 3963.190598749811   
    public static final double NAUTICAL_MILE_TO_KILOMETER = 1.852;
//    private static SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdfHHmmz = new SimpleDateFormat("HH:mm z");

    public static Double convertKnotsToKmph(Double valueInKnots) {
        // convert from knots to kilometers per hour
        return valueInKnots * NAUTICAL_MILE_TO_KILOMETER;
    }

    public static Double convertCmPerSecToKnots(Double valueInCmPerSec) {
        // convert from cm/s to knots
        Double valueInKmph = valueInCmPerSec * 3600 / 100000;
        return valueInKmph / NAUTICAL_MILE_TO_KILOMETER;
    }

    public static Double calculateX(Double distance, Double angle) {
        //x = [distance]*cos((90-[degree])*pi()/180)
        return distance * Math.cos((90 - angle) * Math.PI / 180);
    }

    public static Double calculateY(Double distance, Double angle) {
        //y = [distance]*sin((90-[degree])*pi()/180)
        return distance * Math.sin((90 - angle) * Math.PI / 180);
    }

    public static Double calculateDistance(Double x, Double y) {
        // distance = sqr((x*x) + (y*y))
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static Double calculateDistance(Double xFrom, Double yFrom,
            Double xTo, Double yTo) {
        if (xFrom == null || yFrom == null || xTo == null || yTo == null) {
            return null;
        }

        return calculateDistance(xTo - xFrom, yTo - yFrom);
    }

    public static Double calculateAngle(Double x, Double y) {
        // angle = 90 - (atan2(x, y) * 180 / pi)
        return ((double) 90) - (Math.atan2(y, x) * 180 / Math.PI);
    }

    public static Double calculateAngle(Double xFrom, Double yFrom, Double xTo,
            Double yTo) {
        if (xFrom == null || yFrom == null || xTo == null || yTo == null) {
            return null;
        }

        return calculateAngle(xTo - xFrom, yTo - yFrom);
    }

    public static Double subtractDegree(Double degree1, Double degree2) {
        Double result = degree1 - degree2;
        if (result < 0) {
            result = result + 360;
        }

        return result;
    }

    public static Double addDegree(Double degree1, Double degree2) {
        Double result = degree1 + degree2;
        if (result > 360) {
            result = result - 360;
        }

        return result;
    }

    public static Double convertDegreeToPositive(Double degree) {
        if (degree == null) {
            return null;
        }

        return degree < 0 ? (360 + degree) : degree;
    }

    public static Double oppositeDegree(Double degree) {
        if (degree == null) {
            return null;
        }

        degree = convertDegreeToPositive(degree);
        if (degree > 180) {
            return degree - 180;
        } else {
            return degree + 180;
        }
    }

    /*
     * @return Double{lat, long}
     */
    public static Double[] calculatePointInKm(Double latitude, Double longitude,
            Double distanceInKm, Double angle) {
        // where R is earth’s radius (mean radius = 6,371km);
        // note that angles need to be in radians to pass to trig functions!
        // lat2 = asin(sin(lat1)*cos(d/R) + cos(lat1)*sin(d/R)*cos(θ))
        // lon2 = lon1 + atan2(sin(θ)*sin(d/R)*cos(lat1), cos(d/R)−sin(lat1)*sin(lat2))        
        // θ is the bearing (in radians, clockwise from north);
        // d/R is the angular distance (in radians), where d is the distance travelled and R is the earth’s radius

        latitude = Math.toRadians(latitude); //latitude * Math.PI / 180; // convert to radians
        longitude = Math.toRadians(longitude); //longitude * Math.PI / 180; // convert to radians
        angle = Math.toRadians(angle); // convert to radian

//        System.out.println("latitude = " + latitude);
//        System.out.println("longitude = " + longitude);
//        System.out.println("angle = " + angle);
//        System.out.println("distanceInKm = " + distanceInKm);


        Double latitude2 = Math.asin(Math.sin(latitude) * Math.cos(
                distanceInKm / EARTH_RADIUS_IN_KM) + Math.cos(latitude) * Math.sin(
                distanceInKm / EARTH_RADIUS_IN_KM) * Math.cos(angle));
        Double longitude2 = longitude + Math.atan2(Math.sin(angle) * Math.sin(
                distanceInKm / EARTH_RADIUS_IN_KM) * Math.cos(latitude),
                Math.cos(distanceInKm / EARTH_RADIUS_IN_KM) - Math.sin(latitude) * Math.sin(latitude2));

//        System.out.println("latitude2 = " + latitude2);
//        System.out.println("longitude2 = " + longitude2);

        latitude2 = Math.toDegrees(latitude2); // latitude2 * 180 / Math.PI; // convert back to degree
        longitude2 = Math.toDegrees(longitude2); // longitude2 * 180 / Math.PI; // convert back to degree

//        System.out.println("latitude2 = " + latitude2);
//        System.out.println("longitude2 = " + longitude2);

        return new Double[]{latitude2, longitude2};
    }

    
    /*
     * @return Double{lat, long}
     */
    public static Double[] calculatePointInNm(Double latitude, Double longitude,
            Double distanceInNm, Double angle) {
        
        // where R is earth’s radius (mean radius = 6,371km);
        // note that angles need to be in radians to pass to trig functions!
        // lat2 = asin(sin(lat1)*cos(d/R) + cos(lat1)*sin(d/R)*cos(θ))
        // lon2 = lon1 + atan2(sin(θ)*sin(d/R)*cos(lat1), cos(d/R)−sin(lat1)*sin(lat2))        
        // θ is the bearing (in radians, clockwise from north);
        // d/R is the angular distance (in radians), where d is the distance travelled and R is the earth’s radius

        latitude = Math.toRadians(latitude); //latitude * Math.PI / 180; // convert to radians
        longitude = Math.toRadians(longitude); //longitude * Math.PI / 180; // convert to radians
        angle = Math.toRadians(angle); // convert to radian

//        System.out.println("latitude = " + latitude);
//        System.out.println("longitude = " + longitude);
//        System.out.println("angle = " + angle);
//        System.out.println("distanceInKm = " + distanceInKm);


        Double latitude2 = Math.asin(Math.sin(latitude) * Math.cos(
                distanceInNm / EARTH_RADIUS_IN_NM) + Math.cos(latitude) * Math.sin(
                distanceInNm / EARTH_RADIUS_IN_NM) * Math.cos(angle));
        Double longitude2 = longitude + Math.atan2(Math.sin(angle) * Math.sin(
                distanceInNm / EARTH_RADIUS_IN_NM) * Math.cos(latitude),
                Math.cos(distanceInNm / EARTH_RADIUS_IN_NM) - Math.sin(latitude) * Math.sin(latitude2));

//        System.out.println("latitude2 = " + latitude2);
//        System.out.println("longitude2 = " + longitude2);

        latitude2 = Math.toDegrees(latitude2); // latitude2 * 180 / Math.PI; // convert back to degree
        longitude2 = Math.toDegrees(longitude2); // longitude2 * 180 / Math.PI; // convert back to degree

//        System.out.println("latitude2 = " + latitude2);
//        System.out.println("longitude2 = " + longitude2);

        return new Double[]{latitude2, longitude2};
    }

    public static double calculateTotalHour(String value) {
        Date dateTimeRange = null;
        try {
            sdfHHmmz.setLenient(true);
            dateTimeRange = sdfHHmmz.parse(value + " GMT+00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dateTimeRange);

        Double totalHours = (double) (dateTimeRange.getTime() / (double) 3600000);

//        int hours = cal.get(Calendar.HOUR_OF_DAY);
//        int minutes = cal.get(Calendar.MINUTE);
//        int seconds = cal.get(Calendar.SECOND);
//        int milliseconds = cal.get(Calendar.MILLISECOND);
//
//        Double totalHours = hours + ((double) minutes / (double) 60) + ((double) seconds / (double) 3600) + ((double) milliseconds / (double) 3600000);

        return totalHours;

    }

    public static double calculateDistanceInKm(Double speedInKnot, Double totalHours) {

        return calculateDistanceInNm(speedInKnot, totalHours) * NAUTICAL_MILE_TO_KILOMETER;
    }

    public static double calculateDistanceInNm(Double speedInKnot, Double totalHours) {

        return speedInKnot * totalHours;
    }

    public static Double calculateDistanceInNm(Double latitude1, Double longitude1,
            Double latitude2, Double longitude2) {
        return calculateDistanceInKm(latitude1, longitude1, latitude2,
                longitude2) / NAUTICAL_MILE_TO_KILOMETER;
    }

    public static List<Double> arrCalculateDistanceInNm(List<Double> latitude1, List<Double> longitude1,
            List<Double> latitude2, List<Double> longitude2, int Size) {
        return arrCalculateDistanceInKm(latitude1, longitude1, latitude2, longitude2, Size);
    }
    
    public static Double calculateDistanceInKm(Double latitude1, Double longitude1,
            Double latitude2, Double longitude2) {
        // a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
        // c = 2.atan2(√a, √(1−a))
        // d = R.c
        // where R is earth’s radius (mean radius = 6,371km);
        // note that angles need to be in radians to pass to trig functions!

        if (latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
            return null;
        }

        Double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        Double deltaLongitude = Math.toRadians(longitude2 - longitude1);
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude1 = Math.toRadians(longitude1);
        longitude2 = Math.toRadians(longitude2);

        Double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2) * Math.cos(latitude1) * Math.cos(latitude2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = EARTH_RADIUS_IN_KM * c;

        return d;
    }

    public static List<Double> arrCalculateDistanceInKm(List<Double> latitude1, List<Double> longitude1,
            List<Double> latitude2, List<Double> longitude2, int Size) {
        // a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
        // c = 2.atan2(√a, √(1−a))
        // d = R.c
        // where R is earth’s radius (mean radius = 6,371km);
        // note that angles need to be in radians to pass to trig functions!

        List<Double> d = new ArrayList<Double>();
        
        if (latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
            return null;
        }
        int i = 0;
            Double deltaLatitude = Math.toRadians(latitude2.get(i) - latitude1.get(i));
            Double deltaLongitude = Math.toRadians(longitude2.get(i) - longitude1.get(i));
            latitude1.add(Math.toRadians(latitude1.get(i)));
            latitude2.add(Math.toRadians(latitude2.get(i)));
            longitude1.add(Math.toRadians(longitude1.get(i)));
            longitude2.add(Math.toRadians(longitude2.get(i)));

            Double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                    + Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2) * Math.cos(latitude1.get(i)) * Math.cos(latitude2.get(i));

            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            d.add((EARTH_RADIUS_IN_KM * c) / NAUTICAL_MILE_TO_KILOMETER);

        return d;
    }
    
    public static double otherwise(double value) {
        if (value > 0) {
            return value - (Math.abs(value) * 2);
        } else {
            return value + (Math.abs(value) * 2);
        }
    }

    public static LatLng rotation(LatLng poros, LatLng pi, double degree) {
        degree = otherwise(degree);
        double x_ = poros.getLng(), y_ = poros.getLat(),
                x = pi.getLng(), y = pi.getLat(),
                xGenerate, yGenerate,
                cos, sin;

        if ((degree / 90) % 2 == 1) {
            cos = 0;
        } else {
            cos = Math.cos(Math.toRadians(degree));
        }
        sin = Math.sin(Math.toRadians(degree));

        xGenerate = (x * cos) - (y * sin) + (x_) - (x_ * cos) + (y_ * sin);
        yGenerate = (x * sin) + (y * cos) + (y_) - (x_ * sin) - (y_ * cos);
        LatLng result = new LatLng(yGenerate, xGenerate);
        return result;
    }
    
    public static double getAngle(LatLng pivot, LatLng titikAwal, LatLng titikAkhir) {
        double dpoX = titikAwal.getLng() - pivot.getLng();
        double dpoY = titikAwal.getLat() - pivot.getLat();
        double dpiX = titikAkhir.getLng() - pivot.getLng();
        double dpiY = titikAkhir.getLat() - pivot.getLat();
        double angleRad = Math.atan2(dpoY, dpoX) - Math.atan2(dpiY, dpiX);
        double angleDeg = angleRad * 180 / Math.PI;
        return -angleRad;
    }
    
//    public static void main(String[] args) {
////        poros (0.109859,104.71578) pi : (0.02103072477048608,105.13064086525455) heading : 48.0
//          LatLong latlong = rotation(new LatLong(0.109859,104.71578), new LatLong(0.02103072477048608,105.13064086525455), 48.0);
//        System.out.println(latlong.getLat() +" , "+ latlong.getLng());
//          
//    }
//    
    // [END]
}
