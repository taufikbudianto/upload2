/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import org.primefaces.model.map.LatLng;

/**
 *
 * @author Aris
 */
public class TrapeziumSearchAreaUtil {
    private Double bigLatitude;
    private Double bigLongitude;
    private Double bigRadius;
    private Double smallLatitude;
    private Double smallLongitude;
    private Double smallRadius;
    private static double[] arrbigLatitude = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] arrbigLongitude = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] arrbigRadius = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] arrsmallLatitude = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] arrsmallLongitude = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] arrsmallRadius = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int size;
    private static double[] result_arrHeading = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; 
    private static Double result_arrGradient;
    private static Double result_arrDegree;
//    private static List<Double> result_arrAngle = new ArrayList<Double>();
//    private static List<Double> result_arrBigRotationDegree = new ArrayList<Double>();
//    private static List<Double> result_arrSmallRotationDegree = new ArrayList<Double>();
    private static Double result_arrSmallRotationDegree;
    private static Double result_arrBigRotationDegree;
    private static double[] result_arrGetBigL = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] result_arrGetSmallL = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static LatLng[] result_arrBigSection = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static LatLng[] result_arrSmallSection = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static LatLng[] result_arrGetBigCoord1 = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static LatLng[] result_arrGetBigCoord2 = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static LatLng[] result_arrGetSmallCoord1 = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static LatLng[] result_arrGetSmallCoord2 = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private static double[] result_arrLuasArea = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] result_arrWidth = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] result_arrMinSearchLeg = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static double[] result_arrMaxSearchLeg = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private boolean isUpdateCoordinat;                                            
    
    public TrapeziumSearchAreaUtil(){
        
    }
    public TrapeziumSearchAreaUtil(Double bigLatitude, Double bigLongitude, Double bigRadiusInDegree, Double smallLatitude, Double smallLongitude, Double smallRadiusInDegree) {
        this.bigLatitude = bigLatitude;
        this.bigLongitude = bigLongitude;
        this.bigRadius = bigRadiusInDegree;
        this.smallLatitude = smallLatitude;
        this.smallLongitude = smallLongitude;
        this.smallRadius = smallRadiusInDegree;
    }

    public TrapeziumSearchAreaUtil(LatLng bigLatLng, Double bigRadiusInNauticalMile, LatLng smallLatLng, Double smallRadiusInNauticalMile) {
        this.bigLatitude = bigLatLng.getLat();
        this.bigLongitude = bigLatLng.getLng();
        this.bigRadius = bigRadiusInNauticalMile / 60;
        this.smallLatitude = smallLatLng.getLat();
        this.smallLongitude = smallLatLng.getLng();
        this.smallRadius = smallRadiusInNauticalMile / 60;
    }

    public TrapeziumSearchAreaUtil(Double bigLatitude, Double bigLongitude, Double smallLatitude, Double smallLongitude, Double smallRadiusInNauticalMile) {
        this.bigLatitude = bigLatitude;
        this.bigLongitude = bigLongitude;
        this.smallLatitude = smallLatitude;
        this.smallLongitude = smallLongitude;
        this.smallRadius = smallRadiusInNauticalMile / 60;
        this.bigRadius = (smallRadius * 60 + 0.1 * getDistance()) / 60;
    }

    public TrapeziumSearchAreaUtil(double[] arrbigLatitude, double[] arrbigLongitude, double[] arrsmallLatitude, double[] arrsmallLongitude, double[] arrsmallRadiusInNauticalMile, int size, boolean isInsert) {
        this.size = size;
        int i = size;
        if(isInsert == true){
            this.arrbigLatitude[i] = arrbigLatitude[i];
            this.arrbigLongitude[i] = arrbigLongitude[i];
            this.arrsmallLatitude[i] = arrsmallLatitude[i];
            this.arrsmallLongitude[i] = arrsmallLongitude[i];
            this.arrsmallRadius[i] = arrsmallRadiusInNauticalMile[i] / 60;
            this.arrbigRadius[i] = (getArrsmallRadius()[i] * 60 + 0.1 * getArrDistance(i)) / 60;   
       }else{
            isUpdateCoordinat = true;
            this.arrbigLatitude[i] = arrbigLatitude[i];
            this.arrbigLongitude[i] = arrbigLongitude[i];
            this.arrsmallLatitude[i] = arrsmallLatitude[i];
            this.arrsmallLongitude[i] = arrsmallLongitude[i];
            this.arrsmallRadius[i] = arrsmallRadiusInNauticalMile[i] / 60;
            this.arrbigRadius[i] = (getArrsmallRadius()[i] * 60 + 0.1 * getArrDistance(i)) / 60;
        }
    }
    
    public TrapeziumSearchAreaUtil(LatLng bigLatLng, LatLng smallLatLng, Double smallRadiusInNauticalMile) {
        this.bigLatitude = bigLatLng.getLat();
        this.bigLongitude = bigLatLng.getLng();
        this.smallLatitude = smallLatLng.getLat();
        this.smallLongitude = smallLatLng.getLng();
        this.smallRadius = smallRadiusInNauticalMile / 60;
        this.bigRadius = (smallRadius * 60 + 0.1 * getDistance()) / 60;
    }

    public final Double getDistance() {
        return MapCalculation.calculateDistanceInNm(bigLatitude, bigLongitude, smallLatitude, smallLongitude);
    }
    
    public final Double getArrDistance(int i){
//        int i = 0;
        return MapCalculation.calculateDistanceInNm(arrbigLatitude[i], arrbigLongitude[i], arrsmallLatitude[i], arrsmallLongitude[i]);
    }

    private Double gradient() {
        return (bigLongitude - smallLongitude) / (bigLatitude - smallLatitude);
    }
    
    public Double arrGradient(){
        int i = size;
           return result_arrGradient = (arrbigLongitude[i] - arrsmallLongitude[i]) / (arrbigLatitude[i] - arrsmallLatitude[i]);
//        System.out.println("arrGradient:"+ result_arrGradient.size());
//        return result_arrGradient;
    }

    public Double degree() {
        return Math.toDegrees(Math.atan(gradient()));
    }
    
    public Double arrDegree(){
//        int i = size;
            return result_arrDegree = Math.toDegrees(Math.atan(arrGradient()));
//            System.out.println("arrDegree:"+result_arrDegree.size());
//        return result_arrDegree;
        
    }

    public Double angle() {
        return 90 - 2 * (Math.toDegrees(Math.acos(60 * (bigRadius - smallRadius) / getDistance())));
    }
    
    public Double arrAngle(){
        int i = size;
        double nilai = 0;
//        for(int i = 0;i <= size; i++){
//            nilai += (arrbigRadius[i] - arrsmallRadius[i]) / getArrDistance(i);
//        }

            nilai = (arrbigRadius[i] - arrsmallRadius[i]) / getArrDistance(i);
        
            return 90 - 2 * (Math.toDegrees(Math.acos(60 * nilai)));
   }

    public Double bigRotationDegree() {
        if (bigLatitude >= smallLatitude) {
            return 270 + degree();
        } else {
            return 90 + degree();
        }
    }

    public Double arrBigRotationDegree() {
        int i = size;
            if (arrbigLatitude[i] >= arrsmallLatitude[i]) {
                result_arrBigRotationDegree = 270 + arrDegree();
            } else {
                result_arrBigRotationDegree = 90 + arrDegree();
            }
        return result_arrBigRotationDegree;
    }
    
    public Double smallRotationDegree() {
        if (smallLatitude > bigLatitude) {
            return 270 + degree();
        } else {
            return 90 + degree();
        }
    }
    
    public Double arrSmallRotationDegree() {
        int i = size;
            if (arrsmallLatitude[i] > arrbigLatitude[i]) {
                result_arrSmallRotationDegree = 270 + arrDegree();
            } else {
                result_arrSmallRotationDegree = 90 + arrDegree();
            }
        return result_arrSmallRotationDegree;
    }

    public Double getBigL() {
//        return bigRadius / Math.tan(Math.toRadians(angle() / 2));
        return Math.abs(bigRadius / Math.tan(Math.toRadians(angle() / 2)));
    }
    
    public double[] arrGetBigL() {
//        int i = size;
         double nilai = 0;
         for(int i = 0;i <= size; i++){
             if(i == 0) {
                 nilai = Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2)));
//                 nilai += Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2)));
             }else{
//                 nilai += Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2))) * (getArrDistance(i - 1) + getArrDistance(i));
                 nilai += Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2))) * 2;
             }
         }
            result_arrGetBigL[size] = nilai;
        return result_arrGetBigL;
    }

    public Double updateArrGetBigL(int i){
        double nilai = 0;
        for(i = 0;i <= size; i++){
            if(i == 0) {
                 nilai = Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2)));
             }else{
                 nilai = Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2))) * (getArrDistance(i - 1));
             }
        }
        result_arrGetBigL[size] = nilai;
        return result_arrGetBigL[i];
    }
    
    public Double getSmallL() {
//        return smallRadius / Math.tan(Math.toRadians(90 - angle() / 2));
        return Math.abs(smallRadius / Math.tan(Math.toRadians(180 - angle() / 2)));
    }

    public double[] arrGetSmallL() {
//        int i = size;
         double nilai = 0;
         
         for(int i = 0;i <= size; i++){
             if(i == 0){
//                 nilai += Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2)));
                 nilai = Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2)));
             }else{
//                 nilai += Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2))) * (getArrDistance(i - 1) + getArrDistance(i));
                 nilai += Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2))) +
                         Math.abs(arrbigRadius[i] / Math.tan(Math.toRadians(arrAngle() / 2)));
             }
            
         }
            result_arrGetSmallL[size] = nilai;
        return result_arrGetSmallL;
    }
    
    public Double updateArrGetSmallL(int i){
        double nilai = 0;
        for(i = 0;i <= size; i++){
            if(i == 0){
                 nilai = Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2)));
             }else{
                 nilai = Math.abs(arrsmallRadius[i] / Math.tan(Math.toRadians(180 - arrAngle() / 2))) * (getArrDistance(i - 1));
             }
        }
        result_arrGetSmallL[size] = nilai;
        return result_arrGetSmallL[i];
    }
    
    private LatLng bigSection() {
        LatLng center = new LatLng(bigLatitude, bigLongitude);
        LatLng point = new LatLng(bigLatitude, bigLongitude + bigRadius);
        return MapCalculation.rotation(center, point, bigRotationDegree());
    }
    
    public LatLng[] arrBigSection() {
        int i = size;
            LatLng center = new LatLng(arrbigLatitude[i], arrbigLongitude[i]);
            LatLng point = new LatLng(arrbigLatitude[i], arrbigLongitude[i] + arrbigRadius[i]);
            if(result_arrBigSection.length ==0){
                result_arrBigSection[i] = MapCalculation.rotation(center, point, arrBigRotationDegree());
            }else{
                if(isUpdateCoordinat==false){
                    result_arrBigSection[i] = MapCalculation.rotation(center, point, arrBigRotationDegree());
                }else{
                    result_arrBigSection[i] = MapCalculation.rotation(center, point, arrBigRotationDegree());
                }
            }
        return result_arrBigSection;
    }

    public LatLng smallSection() {
        LatLng center = new LatLng(smallLatitude, smallLongitude);
        LatLng point = new LatLng(smallLatitude, smallLongitude + smallRadius);
        return MapCalculation.rotation(center, point, smallRotationDegree());
    }
    
    public LatLng[] arrSmallSection() {
        int i = size;
            LatLng center = new LatLng(arrsmallLatitude[i], arrsmallLongitude[i]);
            LatLng point = new LatLng(arrsmallLatitude[i], arrsmallLongitude[i] + arrsmallRadius[i]);
            if(isUpdateCoordinat==false){
                result_arrSmallSection[i] = MapCalculation.rotation(center, point, arrSmallRotationDegree());
            }else{
                result_arrSmallSection[i] = MapCalculation.rotation(center, point, arrSmallRotationDegree());
            }
        return result_arrSmallSection;
    }

    public LatLng getBigCoord1() {
        LatLng center = bigSection();
        LatLng point = new LatLng(bigSection().getLat(), bigSection().getLng() + getBigL());
        return MapCalculation.rotation(center, point, bigRotationDegree() - 90);
    }
    
    public LatLng[] arrGetBigCoord1() {
        int i = size;
            LatLng center = arrBigSection()[i];
            LatLng point = new LatLng(getResult_arrBigSection()[i].getLat(), getResult_arrBigSection()[i].getLng() + getResult_arrGetBigL()[i]);
            result_arrGetBigCoord1[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() - 90);
//            System.out.println("arrGetBigCoord1:"+result_arrGetBigCoord1[i]);
        return result_arrGetBigCoord1;
    }

    public LatLng[] updateArrGetBigCoord1() {
        int i = size;
            isUpdateCoordinat = true;
            LatLng center = arrBigSection()[i];
            LatLng point = new LatLng(getResult_arrBigSection()[i].getLat(), getResult_arrBigSection()[i].getLng() + getResult_arrGetBigL()[i]);
            if(result_arrGetBigCoord1.length ==0){
                result_arrGetBigCoord1[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() - 90);
//                System.out.println("updateArrGetBigCoord1:"+result_arrGetBigCoord1.get(0));
            }else{
                result_arrGetBigCoord1[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() - 90);
//                System.out.println("updateArrGetBigCoord1:"+result_arrGetBigCoord1[i]);
            }
        return result_arrGetBigCoord1;
    }
    
    public LatLng getBigCoord2() {
        LatLng center = bigSection();
        LatLng point = new LatLng(bigSection().getLat(), bigSection().getLng() + getBigL());
        return MapCalculation.rotation(center, point, bigRotationDegree() + 90);
    }
    
    public LatLng[] arrGetBigCoord2() {
        int i = size;
            LatLng center = getResult_arrBigSection()[i];
            LatLng point = new LatLng(getResult_arrBigSection()[i].getLat(), getResult_arrBigSection()[i].getLng() + getResult_arrGetBigL()[i]);
            result_arrGetBigCoord2[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() + 90);
        return result_arrGetBigCoord2;
    }
    
    public LatLng[] updateArrGetBigCoord2() {
        int i = size;
            LatLng center = getResult_arrBigSection()[i];
            LatLng point = new LatLng(getResult_arrBigSection()[i].getLat(), getResult_arrBigSection()[i].getLng() + getResult_arrGetBigL()[i]);
            if(result_arrGetBigCoord2.length==0){
                result_arrGetBigCoord2[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() + 90);
//                System.out.println("updateArrGetBigCoord2:"+result_arrGetBigCoord2.get(0));
            }else{
                result_arrGetBigCoord2[i] = MapCalculation.rotation(center, point, getResult_arrBigRotationDegree() + 90);
//                System.out.println("updateArrGetBigCoord2:"+result_arrGetBigCoord2[i]);
            }
        return result_arrGetBigCoord2;
    } 

    public LatLng getSmallCoord1() {
        LatLng center = smallSection();
        LatLng point = new LatLng(smallSection().getLat(), smallSection().getLng() + getSmallL());
        return MapCalculation.rotation(center, point, smallRotationDegree() + 90);
    }
    
    public LatLng[] arrGetSmallCoord1() {
        int i = size;
            LatLng center = getResult_arrSmallSection()[i];
            LatLng point = new LatLng(getResult_arrSmallSection()[i].getLat(), getResult_arrSmallSection()[i].getLng() + getResult_arrGetSmallL()[i]);
            if(isUpdateCoordinat == false){
                result_arrGetSmallCoord1[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() + 90);
            }else{
                result_arrGetSmallCoord1[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() + 90);
            }
//            System.out.println("arrGetSmallCoord1:"+result_arrGetSmallCoord1[i]);
        return result_arrGetSmallCoord1;
    }

    public LatLng[] updateArrGetSmallCoord1() {
        int i = size;
            LatLng center = getResult_arrSmallSection()[i];
            LatLng point = new LatLng(getResult_arrSmallSection()[i].getLat(), getResult_arrSmallSection()[i].getLng() + getResult_arrGetSmallL()[i]);
            if(result_arrGetSmallCoord1.length==0){
                result_arrGetSmallCoord1[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() + 90);
//                System.out.println("updateArrGetSmallCoord1:"+result_arrGetSmallCoord1.get(0));
            }else{
                result_arrGetSmallCoord1[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() + 90);
//                System.out.println("updateArrGetSmallCoord1:"+result_arrGetSmallCoord1[i]);
            }           
        return result_arrGetSmallCoord1;
    }
    
    public LatLng getSmallCoord2() {
        LatLng center = smallSection();
        LatLng point = new LatLng(smallSection().getLat(), smallSection().getLng() + getSmallL());
        return MapCalculation.rotation(center, point, smallRotationDegree() - 90);
    }
    
    public LatLng[] arrGetSmallCoord2() {
        int i = size;
          LatLng center = getResult_arrSmallSection()[i];
            LatLng point = new LatLng(getResult_arrSmallSection()[i].getLat(), getResult_arrSmallSection()[i].getLng() + getResult_arrGetSmallL()[i]);
            result_arrGetSmallCoord2[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() - 90);
        return result_arrGetSmallCoord2;
    }

    public LatLng[] updateArrGetSmallCoord2() {
        int i = size;
            LatLng center = getResult_arrSmallSection()[i];
            LatLng point = new LatLng(getResult_arrSmallSection()[i].getLat(), getResult_arrSmallSection()[i].getLng() + getResult_arrGetSmallL()[i]);
            if(result_arrGetSmallCoord2.length==0){
                result_arrGetSmallCoord2[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() - 90);
//                System.out.println("updateArrGetSmallCoord2:"+result_arrGetSmallCoord2.get(0));
            }else{
                result_arrGetSmallCoord2[i] = MapCalculation.rotation(center, point, getResult_arrSmallRotationDegree() - 90);
//                System.out.println("updateArrGetSmallCoord2:"+result_arrGetSmallCoord2[i]);
            }
        return result_arrGetSmallCoord2;
    }

       public Double luasArea() {
        return width() * 60 * (getSmallL() * 2 + getBigL() * 2) / 2;
    }
    
    public double[] arrLuasArea() {
        int i = size;
//        for(int i = 0;i <= size; i++){
//            if(i == 0){
                result_arrLuasArea[i] = result_arrWidth[i] * 60 * (arrGetSmallL()[i] * 2 + arrGetBigL()[i] * 2) / 2;
//            }else{
//                result_arrLuasArea[i] += result_arrWidth[i] * 60 * (arrGetSmallL()[i] * 2 + arrGetBigL()[i] * 2) * (getArrDistance(i-1) + getArrDistance(i)) / 2;
//            }
//            
//        }

        return result_arrLuasArea;
    }
    
    public double[] updateArrLuasArea(){
        int i = size;
        updateArrGetSmallL(i);
        updateArrGetBigL(i);
            result_arrLuasArea[i] = result_arrWidth[i] * 60 * (result_arrGetSmallL[i] * 2 + result_arrGetBigL[i] * 2) / 2;
//            System.out.println("updateArrLuasArea:"+result_arrLuasArea[i]);
        return result_arrLuasArea;
    }
    //Properti yang dibutuhkan untuk parallel search pattern

    public Double width() {
        return getDistance() + (smallRadius * 60) + (bigRadius * 60);
    }

    public double[] arrWidth() {
        int i = size;
        for(i = 0;i <= size; i++){
//            if(i == 0){
                //* (getArrDistance(i - 1) + getArrDistance(i)
                result_arrWidth[i] = getArrDistance(i) + (arrsmallRadius[i] * 60) + (arrbigRadius[i] * 60);
//            }else{
//                result_arrWidth[i] = (getArrDistance(i-1) + getArrDistance(i)) * (arrsmallRadius[i] * 60) + (arrbigRadius[i] * 60);
//            }
            
        }
        return result_arrWidth;
    }
    
    public double[] updateArrWIdth(){
        int i = size;
            result_arrWidth[i] = getArrDistance(i) + (arrsmallRadius[i] * 60) + (arrbigRadius[i] * 60);
//            System.out.println("updateArrWIdth:"+result_arrWidth[i]);
        return result_arrWidth;
    }
    
    public Double heading() {
        Double heading = MapCalculation.calculateAngle(smallLongitude, smallLatitude, bigLongitude, bigLatitude);
        if (heading < 0) {
            return 360 + heading;
        } else {
            return heading;
        }
    }
    
    public double[] arrHeading() {
        Double heading;
        int i = size;
            heading = MapCalculation.calculateAngle(arrsmallLongitude[i], arrsmallLatitude[i], 
                     arrbigLongitude[i], arrbigLatitude[i]);
            if (heading < 0) {
                result_arrHeading[i] = 360 + heading;
            } else {
                result_arrHeading[i] = heading;
            }
//            System.out.println("arrHeading:"+result_arrHeading.get(0));
        return result_arrHeading;
    }

    public double[] updateArrHeading(){
        Double heading;
        int i = size;
            heading = MapCalculation.calculateAngle(arrsmallLongitude[i], arrsmallLatitude[i], 
                     arrbigLongitude[i], arrbigLatitude[i]);
            if (heading < 0) {
                result_arrHeading[i] = 360 + heading;
            } else {
                result_arrHeading[i] = heading;
            }
//            System.out.println("updateArrHeading:"+result_arrHeading[i]);
        return result_arrHeading;
    }
    
    public Double minSearchLeg(Double trackSpacing) {
        Double min = getSmallL() * 2 * 60 - trackSpacing;
        if (min <= 0) {
            return trackSpacing/2;
        } else {
            return min;
        }
    }

    public double[] arrMinSearchLeg(Double trackSpacing) {
        int i = size;
        Double min;
        min = getResult_arrGetSmallL()[i] * 2 * 60 - trackSpacing;           
        if (min <= 0) {
                result_arrMinSearchLeg[i] = trackSpacing/2;
            } else {
                result_arrMinSearchLeg[i] = min;
        }
//        System.out.println("arrMinSearchLeg:"+result_arrMinSearchLeg[i]);
        return result_arrMinSearchLeg;
    }
    
    public double[] updateArrMinSearchLeg(Double trackSpacing) {
        int i = size;
        Double min;
        min = getResult_arrGetSmallL()[i] * 2 * 60 - trackSpacing;           
        if (min <= 0) {
            if(result_arrMinSearchLeg.length == 0){
                result_arrMinSearchLeg[i] = trackSpacing/2;
//                System.out.println("updateArrMinSearchLeg:"+result_arrMinSearchLeg.get(0));
            }else{
                result_arrMinSearchLeg[i] = trackSpacing/2;
//                System.out.println("updateArrMinSearchLeg:"+result_arrMinSearchLeg[i]);
            }
        } else {
            if(result_arrMinSearchLeg.length == 0){
                result_arrMinSearchLeg[i] = min;
//                System.out.println("updateArrMinSearchLeg:"+result_arrMinSearchLeg.get(0));
            }else{
                result_arrMinSearchLeg[i] = min;
//                System.out.println("updateArrMinSearchLeg:"+result_arrMinSearchLeg[i]);
            }
        }
        return result_arrMinSearchLeg;
    }
    
    public Double maxSearchLeg(Double trackSpacing) {
        return getBigL() * 2 * 60 - trackSpacing;
    }
    
    public double[] arrMaxSearchLeg(Double trackSpacing) {
        int i = size;
        result_arrMaxSearchLeg[i] = getResult_arrGetBigL()[i] * 2 * 60 - trackSpacing;
//        System.out.println("arrMaxSearchLeg:"+result_arrMaxSearchLeg[i]);
        return result_arrMaxSearchLeg;
    }
    
    public double[] updateArrMaxSearchLeg(Double trackSpacing) {
        int i = size;
        if(result_arrMaxSearchLeg.length == 0){
            result_arrMaxSearchLeg[i] = getResult_arrGetBigL()[i] * 2 * 60 - trackSpacing;
//            System.out.println("updateArrMaxSearchLeg:"+result_arrMaxSearchLeg.get(0));
        }else{
            result_arrMaxSearchLeg[i] = getResult_arrGetBigL()[i] * 2 * 60 - trackSpacing;
//            System.out.println("updateArrMaxSearchLeg:"+result_arrMaxSearchLeg[i]);
        }
        return result_arrMaxSearchLeg;
    }

    public void cleanMemory(){
        for(int i=0; i < 20; i++){
            arrbigLatitude[i] = 0;
            arrbigLongitude[i] = 0;
            arrbigRadius[i] = 0;
            arrsmallLatitude[i] = 0;
            arrsmallLongitude[i] = 0;
            arrsmallRadius[i] = 0;
            result_arrHeading[i] = 0; 
            result_arrGetBigL[i] = 0;
            result_arrGetSmallL[i] = 0;
            result_arrLuasArea[i] = 0;
            result_arrWidth[i] = 0;
            result_arrMinSearchLeg[i] = 0;
            result_arrMaxSearchLeg[i] = 0;
            result_arrBigSection[i] = null;
            result_arrSmallSection[i] = null;
            result_arrGetBigCoord1[i] = null;
            result_arrGetBigCoord2[i] = null;
            result_arrGetSmallCoord1[i] = null;
            result_arrGetSmallCoord2[i] = null;
        }
        
        result_arrGradient = 0.0;
        result_arrDegree = 0.0;
        result_arrSmallRotationDegree = 0.0;
        result_arrBigRotationDegree = 0.0;
        
        System.gc();
        
    }
    
    private Double calculateGradient(Double lat1, Double long1, Double lat2, Double long2) {
        return (long2 - long1) / (lat2 - lat1);
    }

    private Double calculateGradient(LatLng latLong1, LatLng latLong2) {
        return calculateGradient(latLong1.getLat(), latLong1.getLng(), latLong2.getLat(), latLong2.getLng());
    }

    public Double getBigLatitude() {
        return bigLatitude;
    }

    public Double getBigLongitude() {
        return bigLongitude;
    }

    public Double getBigRadius() {
        return bigRadius;
    }

    public Double getSmallLatitude() {
        return smallLatitude;
    }

    public Double getSmallLongitude() {
        return smallLongitude;
    }

    public Double getSmallRadius() {
        return smallRadius;
    }

    public static double[] getArrbigLatitude() {
        return arrbigLatitude;
    }

    public void setArrbigLatitude(double[] arrbigLatitude) {
        this.arrbigLatitude = arrbigLatitude;
    }

    public static double[] getArrbigLongitude() {
        return arrbigLongitude;
    }

    public void setArrbigLongitude(double[] arrbigLongitude) {
        this.arrbigLongitude = arrbigLongitude;
    }

    public static double[] getArrbigRadius() {
        return arrbigRadius;
    }

    public void setArrbigRadius(double[] arrbigRadius) {
        this.arrbigRadius = arrbigRadius;
    }

    public static double[] getArrsmallLatitude() {
        return arrsmallLatitude;
    }

    public void setArrsmallLatitude(double[] arrsmallLatitude) {
        this.arrsmallLatitude = arrsmallLatitude;
    }

    public static double[] getArrsmallLongitude() {
        return arrsmallLongitude;
    }

    public void setArrsmallLongitude(double[] arrsmallLongitude) {
        this.arrsmallLongitude = arrsmallLongitude;
    }

    public static double[] getArrsmallRadius() {
        return arrsmallRadius;
    }

    public void setArrsmallRadius(double[] arrsmallRadius) {
        this.arrsmallRadius = arrsmallRadius;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

//    public static List<Double> getResult_arrAngle() {
//        return result_arrAngle;
//    }
//
//    public static void setResult_arrAngle(List<Double> result_arrAngle) {
//        TrapeziumSearchAreaUtil.result_arrAngle = result_arrAngle;
//    }

    //GETTER SETTER DI TAMBAHKAN STATIC
    
    public static Double getResult_arrBigRotationDegree() {
        return result_arrBigRotationDegree;
    }

    public static void setResult_arrBigRotationDegree(Double result_arrBigRotationDegree) {
        TrapeziumSearchAreaUtil.result_arrBigRotationDegree = result_arrBigRotationDegree;
    }

    public static LatLng[] getResult_arrBigSection() {
        return result_arrBigSection;
    }

    public static void setResult_arrBigSection(LatLng[] result_arrBigSection) {
        TrapeziumSearchAreaUtil.result_arrBigSection = result_arrBigSection;
    }

    public Double getResult_arrDegree() {
        return result_arrDegree;
    }

    public  void setResult_arrDegree(Double result_arrDegree) {
        TrapeziumSearchAreaUtil.result_arrDegree = result_arrDegree;
    }

    public static LatLng[] getResult_arrGetBigCoord1() {
        return result_arrGetBigCoord1;
    }

    public static void setResult_arrGetBigCoord1(LatLng[] result_arrGetBigCoord1) {
        TrapeziumSearchAreaUtil.result_arrGetBigCoord1 = result_arrGetBigCoord1;
    }

    public static LatLng[] getResult_arrGetBigCoord2() {
        return result_arrGetBigCoord2;
    }

    public static void setResult_arrGetBigCoord2(LatLng[] result_arrGetBigCoord2) {
        TrapeziumSearchAreaUtil.result_arrGetBigCoord2 = result_arrGetBigCoord2;
    }

    public static double[] getResult_arrGetBigL() {
        return result_arrGetBigL;
    }

    public static void setResult_arrGetBigL(double[] result_arrGetBigL) {
        TrapeziumSearchAreaUtil.result_arrGetBigL = result_arrGetBigL;
    }

    public static LatLng[] getResult_arrGetSmallCoord1() {
        return result_arrGetSmallCoord1;
    }

    public static void setResult_arrGetSmallCoord1(LatLng[] result_arrGetSmallCoord1) {
        TrapeziumSearchAreaUtil.result_arrGetSmallCoord1 = result_arrGetSmallCoord1;
    }

    public static LatLng[] getResult_arrGetSmallCoord2() {
        return result_arrGetSmallCoord2;
    }

    public static void setResult_arrGetSmallCoord2(LatLng[] result_arrGetSmallCoord2) {
        TrapeziumSearchAreaUtil.result_arrGetSmallCoord2 = result_arrGetSmallCoord2;
    }

    public static double[] getResult_arrGetSmallL() {
        return result_arrGetSmallL;
    }

    public static void setResult_arrGetSmallL(double[] result_arrGetSmallL) {
        TrapeziumSearchAreaUtil.result_arrGetSmallL = result_arrGetSmallL;
    }

    public Double getResult_arrGradient() {
        return result_arrGradient;
    }

    public  void setResult_arrGradient(Double result_arrGradient) {
        TrapeziumSearchAreaUtil.result_arrGradient = result_arrGradient;
    }

    public static double[] getResult_arrHeading() {
        return result_arrHeading;
    }

    public static void setResult_arrHeading(double[] result_arrHeading) {
        TrapeziumSearchAreaUtil.result_arrHeading = result_arrHeading;
    }

    public static double[] getResult_arrLuasArea() {
        return result_arrLuasArea;
    }

    public static void setResult_arrLuasArea(double[] result_arrLuasArea) {
        TrapeziumSearchAreaUtil.result_arrLuasArea = result_arrLuasArea;
    }

    public static double[] getResult_arrMaxSearchLeg() {
        return result_arrMaxSearchLeg;
    }

    public static void setResult_arrMaxSearchLeg(double[] result_arrMaxSearchLeg) {
        TrapeziumSearchAreaUtil.result_arrMaxSearchLeg = result_arrMaxSearchLeg;
    }

    public static double[] getResult_arrMinSearchLeg() {
        return result_arrMinSearchLeg;
    }

    public static void setResult_arrMinSearchLeg(double[] result_arrMinSearchLeg) {
        TrapeziumSearchAreaUtil.result_arrMinSearchLeg = result_arrMinSearchLeg;
    }

    public static Double getResult_arrSmallRotationDegree() {
        return result_arrSmallRotationDegree;
    }

    public static void setResult_arrSmallRotationDegree(Double result_arrSmallRotationDegree) {
        TrapeziumSearchAreaUtil.result_arrSmallRotationDegree = result_arrSmallRotationDegree;
    }

    public static LatLng[] getResult_arrSmallSection() {
        return result_arrSmallSection;
    }

    public static void setResult_arrSmallSection(LatLng[] result_arrSmallSection) {
        TrapeziumSearchAreaUtil.result_arrSmallSection = result_arrSmallSection;
    }

    public static double[] getResult_arrWidth() {
        return result_arrWidth;
    }

    public static void setResult_arrWidth(double[] result_arrWidth) {
        TrapeziumSearchAreaUtil.result_arrWidth = result_arrWidth;
    }
    

}
