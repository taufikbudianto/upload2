/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.model.IndexLeeway;
import prosia.basarnas.web.model.map.MonteCarlo;
import prosia.basarnas.web.util.DecimalUtil;

/**
 *
 * @author Ismail
 */
public class MonteCarloCalculation extends Serializable {

    private static final double valKnot = 0.0194384449;
    private static final double DEFAULT_KM = 0.53961;
    public static final double EARTH_RADIUS_IN_NM = 3443.92;  // in nm (6378.137 / 1.852 = 3443.918466522678)

    //monte karlo alternative 1
    public static List<MonteCarlo> calculatMc(
            String cboSatuan, double seaCurrentSpeed_knot, double driftHours,
            double seaCurrentAngle, double surfaceWindAngle,
            boolean incLatSideIsSouth, double incLatDegree, double incLatMinute, double incLatSecond,
            double windCurrentSpeed_knot, IndexLeeway indexLeeway,
            double incLatitude, double incLongitude, double driftErrorPctg,
            double distressCraftError, double searchCraftError, double safetyFactor,
            int jmlPencarian) {
        double unit = Double.parseDouble(cboSatuan);
        double seaCurrentDistance = 0.0;
        if (unit == 0) {
            seaCurrentDistance = seaCurrentSpeed_knot * driftHours;
        } else {
            seaCurrentDistance = seaCurrentSpeed_knot * driftHours / DEFAULT_KM;
        }
        seaCurrentDistance = DecimalUtil.rounding(seaCurrentDistance);
        double seaCurrentX = prosia.basarnas.service.MapCalculation.calculateX(seaCurrentDistance,
                seaCurrentAngle);
        double seaCurrentY = prosia.basarnas.service.MapCalculation.calculateY(seaCurrentDistance,
                seaCurrentAngle);
        double windCurrentAngle = surfaceWindAngle;
        if (incLatSideIsSouth) {
            if (incLatDegree > 10) {
                windCurrentAngle = surfaceWindAngle - 30;
            } else if (incLatDegree == 10 && (incLatMinute > 0 || incLatSecond > 0)) {
                windCurrentAngle = surfaceWindAngle - 30;
            }
        }
        windCurrentAngle = DecimalUtil.rounding(windCurrentAngle);
        double windCurrentSpeedForCalcDist_knot = windCurrentSpeed_knot / 28;
        windCurrentSpeedForCalcDist_knot = DecimalUtil.rounding(windCurrentSpeedForCalcDist_knot);
        double windCurrentSpeedForCalcDist = windCurrentSpeedForCalcDist_knot / valKnot;

        double windCurrentDistance = 0.0;
        if (unit == 0) {
            windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours;
        } else {
            windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours / DEFAULT_KM;
        }
        windCurrentDistance = DecimalUtil.rounding(windCurrentDistance);

        double windCurrentX = MapCalculation.calculateX(windCurrentDistance,
                windCurrentAngle);

        double windCurrentY = MapCalculation.calculateY(windCurrentDistance,
                windCurrentAngle);
        double lwyDivergenceAngle = indexLeeway.getAngle();
        lwyDivergenceAngle = DecimalUtil.rounding(lwyDivergenceAngle);
        double lwySpeed = indexLeeway.getMultiplier()
                * windCurrentSpeed_knot + indexLeeway.getModifier();
        lwySpeed = DecimalUtil.rounding(lwySpeed);
        double lwyDistance = 0.0;
        if (unit == 0) {
            lwyDistance = lwySpeed * driftHours;
        } else {
            lwyDistance = lwySpeed * driftHours / DEFAULT_KM;
        }
        lwyDistance = DecimalUtil.rounding(lwyDistance);

        double lwyLeftAngle = prosia.basarnas.service.MapCalculation.subtractDegree(windCurrentAngle,
                lwyDivergenceAngle);
        lwyLeftAngle = DecimalUtil.rounding(lwyLeftAngle);
        double lwyLeftX = MapCalculation.calculateX(lwyDistance, lwyLeftAngle);
        double lwyLeftY = MapCalculation.calculateY(lwyDistance, lwyLeftAngle);
        double lwyRightAngle = prosia.basarnas.service.MapCalculation.addDegree(windCurrentAngle,
                lwyDivergenceAngle);
        lwyRightAngle = DecimalUtil.rounding(lwyRightAngle);
        double lwyRightX = prosia.basarnas.service.MapCalculation.calculateX(lwyDistance, lwyRightAngle);
        double lwyRightY = prosia.basarnas.service.MapCalculation.calculateY(lwyDistance, lwyRightAngle);
        double driftLeftX = windCurrentX + seaCurrentX + lwyLeftX;
        double driftLeftY = windCurrentY + seaCurrentY + lwyLeftY;
        double driftLeftAngle = prosia.basarnas.service.MapCalculation.calculateAngle(driftLeftX,
                driftLeftY);
        double driftLeftDistanceInNm = 0.0;
        if (unit == 0) {
            driftLeftDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                    driftLeftX,
                    driftLeftY);
        } else {
            driftLeftDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                    driftLeftX,
                    driftLeftY) / DEFAULT_KM;
        }
        driftLeftDistanceInNm = DecimalUtil.rounding(driftLeftDistanceInNm);

        Double[] driftLeftLatLon = prosia.basarnas.service.MapCalculation.calculatePointInNm(incLatitude,
                incLongitude, driftLeftDistanceInNm, driftLeftAngle);
        BigDecimal driftLeftLat = new BigDecimal(driftLeftLatLon[0]).setScale(6,
                BigDecimal.ROUND_HALF_UP);

        BigDecimal driftLeftLon = new BigDecimal(driftLeftLatLon[1]).setScale(6,
                BigDecimal.ROUND_HALF_UP);
        double driftRightX = windCurrentX + seaCurrentX + lwyRightX;
        double driftRightY = windCurrentY + seaCurrentY + lwyRightY;
        Double driftRightAngle = prosia.basarnas.service.MapCalculation.calculateAngle(driftRightX,
                driftRightY);
        double driftRightDistanceInNm = 0.0;
        if (unit == 0) {
            driftRightDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                    driftRightX,
                    driftRightY);
        } else {
            driftRightDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                    driftRightX,
                    driftRightY) / DEFAULT_KM;
        }

        driftRightDistanceInNm = DecimalUtil.rounding(driftRightDistanceInNm);

        Double[] driftRightLatLon = MapCalculation.calculatePointInNm(
                incLatitude,
                incLongitude, driftRightDistanceInNm, driftRightAngle);

        BigDecimal driftRightLat = new BigDecimal(driftRightLatLon[0]).setScale(
                6, BigDecimal.ROUND_HALF_UP);

        BigDecimal driftRightLon = new BigDecimal(driftRightLatLon[1]).setScale(
                6, BigDecimal.ROUND_HALF_UP);
        double driftLeftToRightDistance = 0.0;
        if (unit == 0) {
            driftLeftToRightDistance = Math.sqrt(Math.pow(
                    lwyDistance,
                    2)
                    + Math.pow(lwyDistance, 2)
                    - 2 * lwyDistance * lwyDistance * Math.cos(
                            Math.toRadians(lwyDivergenceAngle * 2)));
        } else {
            driftLeftToRightDistance = Math.sqrt(Math.pow(
                    lwyDistance,
                    2)
                    + Math.pow(lwyDistance, 2)
                    - 2 * lwyDistance * lwyDistance * Math.cos(
                            Math.toRadians(lwyDivergenceAngle * 2))) / DEFAULT_KM;
        }
        driftLeftToRightDistance = DecimalUtil.rounding(driftLeftToRightDistance);

        double deDriftLeftToRightDistance = (driftErrorPctg * driftLeftDistanceInNm / 100)
                + (driftErrorPctg * driftRightDistanceInNm / 100) + driftLeftToRightDistance;
        double driftError = 0.0;
        if (unit == 0) {
            driftError = deDriftLeftToRightDistance / 2;
        } else {
            driftError = (deDriftLeftToRightDistance / 2) / DEFAULT_KM;
        }
        driftError = DecimalUtil.rounding(driftError);
        double totalProbableError = 0.0;
        if (unit == 0) {
            totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                    + Math.pow(distressCraftError, 2)
                    + Math.pow(searchCraftError, 2));
        } else {
            totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                    + Math.pow(distressCraftError, 2)
                    + Math.pow(searchCraftError, 2)) / DEFAULT_KM;
        }
        totalProbableError = DecimalUtil.rounding(totalProbableError);
        double searchRadius = 0.0;
        if (unit == 0) {
            searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                    setScale(0, BigDecimal.ROUND_UP).doubleValue();
        } else {
            searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                    setScale(0, BigDecimal.ROUND_UP).doubleValue() / DEFAULT_KM;
        }

        searchRadius = DecimalUtil.rounding(searchRadius);
        double searchArea = 0.0;
        if (unit == 0) {
            searchArea = Math.pow((2 * searchRadius), 2);
        } else {
            searchArea = Math.pow((2 * searchRadius), 2) / DEFAULT_KM;
        }
        searchArea = DecimalUtil.rounding(searchArea);

        double deLeftToDriftLeftDistance = driftErrorPctg * driftLeftDistanceInNm / 100;
        double driftLeftToDatumDistance = deDriftLeftToRightDistance / 2 - deLeftToDriftLeftDistance;
        double driftLeftToRightX = driftRightX - driftLeftX;
        double driftLeftToRightY = driftRightY - driftLeftY;
        double driftLeftToRightAngle = MapCalculation.calculateAngle(
                driftLeftToRightX, driftLeftToRightY);

        double datumX = (driftLeftToRightX / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftX;
        double datumY = (driftLeftToRightY / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftY;

        Double datumAngle = prosia.basarnas.service.MapCalculation.calculateAngle(datumX, datumY);
        Double datumDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(datumX,
                datumY);
        Double[] datumLatLon = MapCalculation.calculatePointInNm(
                incLatitude,
                incLongitude, datumDistanceInNm, datumAngle);

//        BigDecimal datumLat = new BigDecimal(datumLatLon[0]).setScale(
//                6, BigDecimal.ROUND_HALF_UP);
//
//        BigDecimal datumLon = new BigDecimal(datumLatLon[1]).setScale(
//                6, BigDecimal.ROUND_HALF_UP);
        //percobaan random untuk montecarlo
        Random random = new Random();
        double latitude = Math.toRadians(incLatitude); //latitude * Math.PI / 180; // convert to radians
        double longitude = Math.toRadians(incLongitude); //longitude * Math.PI / 180; // convert to radians
        double angle = Math.toRadians(datumAngle);

        double radiusInDegrees = DecimalUtil.rounding(searchRadius) * CommonConstant.ONE_NM_TO_DEG;
        List<MonteCarlo> listMonteCarlo = new ArrayList<>();
        for (int i = 0; i < jmlPencarian; i++) {
            MonteCarlo mc = new MonteCarlo();
            double randomGau = random.nextGaussian();
            System.out.println("randomGau " + randomGau);
            Double latitude2 = Math.asin(Math.sin(latitude) * Math.cos(
                    datumDistanceInNm / EARTH_RADIUS_IN_NM) + Math.cos(latitude) * Math.sin(
                    datumDistanceInNm / EARTH_RADIUS_IN_NM) * Math.cos(angle));
            Double longitude2 = longitude + Math.atan2(Math.sin(angle) * Math.sin(
                    datumDistanceInNm / EARTH_RADIUS_IN_NM) * Math.cos(latitude),
                    Math.cos(datumDistanceInNm / EARTH_RADIUS_IN_NM) - Math.sin(latitude) * Math.sin(latitude2));
            latitude2 = Math.toDegrees(latitude2); // latitude2 * 180 / Math.PI; // convert back to degree
            longitude2 = Math.toDegrees(longitude2); // longitude2 * 180 / Math.PI; // convert back to degree
            BigDecimal datumLat = new BigDecimal(latitude2).setScale(
                    6, BigDecimal.ROUND_HALF_UP);
            BigDecimal datumLng = new BigDecimal(longitude2).setScale(
                    6, BigDecimal.ROUND_HALF_UP);
//            double MEAN = Math.sin(datumDistanceInNm + randomGau / EARTH_RADIUS_IN_NM) * VARIANCE;
//            double lat = ((MEAN + randomGau) * -180.0) + 90;
//            double lng = ((MEAN + randomGau) * -360.0);
//            double MEAN_LAT = latitude2;
//            double randomDatumLat = MEAN_LAT + randomGau * VARIANCE;
//            double MEAN_LNG = longitude2;
//            double randomDatumLng = MEAN_LNG + randomGau * VARIANCE;

            double u = random.nextGaussian();
            double v = random.nextGaussian();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);
            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(Math.toRadians(latitude2));
            double foundLongitude = new_x + datumLng.doubleValue();
            double foundLatitude = y + datumLat.doubleValue();

            mc.setDatum(new LatLng(foundLatitude, foundLongitude));
            mc.setRadius(angle);
            listMonteCarlo.add(mc);
        }
        return listMonteCarlo;
    }

    //monte karlo alternative 2
    public static List<MonteCarlo> calculatMc(
            String cboSatuan, double seaCurrentSpeed_knot, double driftHours,
            double seaCurrentAngle, double surfaceWindAngle,
            boolean incLatSideIsSouth, double incLatDegree, double incLatMinute, double incLatSecond,
            double windCurrentSpeed_knot, IndexLeeway indexLeeway,
            double incLatitude, double incLongitude, double driftErrorPctg,
            double distressCraftError, double searchCraftError, double safetyFactor,
            int jmlPencarian, double varianceSea, double varianceWind) {
        Random random = new Random();
        List<MonteCarlo> listMonteCarlo = new ArrayList<>();
        for (int i = 0; i < jmlPencarian; i++) {
            MonteCarlo mc = new MonteCarlo();

            double unit = Double.parseDouble(cboSatuan);
            //random Gaussian
            double seaCurrentSpeed = seaCurrentSpeed_knot / valKnot;
            seaCurrentSpeed = Math.abs(seaCurrentSpeed + (random.nextGaussian() * varianceSea));
            seaCurrentSpeed_knot = valKnot * seaCurrentSpeed;
            surfaceWindAngle = Math.abs(surfaceWindAngle + (random.nextGaussian() * varianceSea));
            seaCurrentAngle = Math.abs(seaCurrentAngle + (random.nextGaussian() * varianceSea));
            double seaCurrentDistance = 0.0;
            if (unit == 0) {
                seaCurrentDistance = seaCurrentSpeed_knot * driftHours;
            } else {
                seaCurrentDistance = seaCurrentSpeed_knot * driftHours / DEFAULT_KM;
            }
            seaCurrentDistance = DecimalUtil.rounding(seaCurrentDistance);
            double seaCurrentX = prosia.basarnas.service.MapCalculation.calculateX(seaCurrentDistance,
                    seaCurrentAngle);
            double seaCurrentY = prosia.basarnas.service.MapCalculation.calculateY(seaCurrentDistance,
                    seaCurrentAngle);
            double windCurrentAngle = surfaceWindAngle;
            if (incLatSideIsSouth) {
                if (incLatDegree > 10) {
                    windCurrentAngle = surfaceWindAngle - 30;
                } else if (incLatDegree == 10 && (incLatMinute > 0 || incLatSecond > 0)) {
                    windCurrentAngle = surfaceWindAngle - 30;
                }
            }
            windCurrentAngle = DecimalUtil.rounding(windCurrentAngle);
            //random Gaussian
            double windCurrentSpeed = windCurrentSpeed_knot / valKnot;
            windCurrentSpeed = Math.abs(windCurrentSpeed + (random.nextGaussian() * varianceWind));
            windCurrentSpeed_knot = valKnot * windCurrentSpeed;
            double windCurrentSpeedForCalcDist_knot = windCurrentSpeed_knot / 28;
            windCurrentSpeedForCalcDist_knot = DecimalUtil.rounding(windCurrentSpeedForCalcDist_knot);
            double windCurrentSpeedForCalcDist = windCurrentSpeedForCalcDist_knot / valKnot;

            double windCurrentDistance = 0.0;
            if (unit == 0) {
                windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours;
            } else {
                windCurrentDistance = windCurrentSpeedForCalcDist_knot * driftHours / DEFAULT_KM;
            }
            windCurrentDistance = DecimalUtil.rounding(windCurrentDistance);

            double windCurrentX = MapCalculation.calculateX(windCurrentDistance,
                    windCurrentAngle);

            double windCurrentY = MapCalculation.calculateY(windCurrentDistance,
                    windCurrentAngle);
            double lwyDivergenceAngle = indexLeeway.getAngle();
            lwyDivergenceAngle = DecimalUtil.rounding(lwyDivergenceAngle);
            double lwySpeed = indexLeeway.getMultiplier()
                    * windCurrentSpeed_knot + indexLeeway.getModifier();
            lwySpeed = DecimalUtil.rounding(lwySpeed);
            double lwyDistance = 0.0;
            if (unit == 0) {
                lwyDistance = lwySpeed * driftHours;
            } else {
                lwyDistance = lwySpeed * driftHours / DEFAULT_KM;
            }
            lwyDistance = DecimalUtil.rounding(lwyDistance);

            double lwyLeftAngle = prosia.basarnas.service.MapCalculation.subtractDegree(windCurrentAngle,
                    lwyDivergenceAngle);
            lwyLeftAngle = DecimalUtil.rounding(lwyLeftAngle);
            double lwyLeftX = MapCalculation.calculateX(lwyDistance, lwyLeftAngle);
            double lwyLeftY = MapCalculation.calculateY(lwyDistance, lwyLeftAngle);
            double lwyRightAngle = prosia.basarnas.service.MapCalculation.addDegree(windCurrentAngle,
                    lwyDivergenceAngle);
            lwyRightAngle = DecimalUtil.rounding(lwyRightAngle);
            double lwyRightX = prosia.basarnas.service.MapCalculation.calculateX(lwyDistance, lwyRightAngle);
            double lwyRightY = prosia.basarnas.service.MapCalculation.calculateY(lwyDistance, lwyRightAngle);
            double driftLeftX = windCurrentX + seaCurrentX + lwyLeftX;
            double driftLeftY = windCurrentY + seaCurrentY + lwyLeftY;
            double driftLeftAngle = prosia.basarnas.service.MapCalculation.calculateAngle(driftLeftX,
                    driftLeftY);
            double driftLeftDistanceInNm = 0.0;
            if (unit == 0) {
                driftLeftDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                        driftLeftX,
                        driftLeftY);
            } else {
                driftLeftDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                        driftLeftX,
                        driftLeftY) / DEFAULT_KM;
            }
            driftLeftDistanceInNm = DecimalUtil.rounding(driftLeftDistanceInNm);

            Double[] driftLeftLatLon = prosia.basarnas.service.MapCalculation.calculatePointInNm(incLatitude,
                    incLongitude, driftLeftDistanceInNm, driftLeftAngle);
            BigDecimal driftLeftLat = new BigDecimal(driftLeftLatLon[0]).setScale(6,
                    BigDecimal.ROUND_HALF_UP);

            BigDecimal driftLeftLon = new BigDecimal(driftLeftLatLon[1]).setScale(6,
                    BigDecimal.ROUND_HALF_UP);
            double driftRightX = windCurrentX + seaCurrentX + lwyRightX;
            double driftRightY = windCurrentY + seaCurrentY + lwyRightY;
            Double driftRightAngle = prosia.basarnas.service.MapCalculation.calculateAngle(driftRightX,
                    driftRightY);
            double driftRightDistanceInNm = 0.0;
            if (unit == 0) {
                driftRightDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                        driftRightX,
                        driftRightY);
            } else {
                driftRightDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(
                        driftRightX,
                        driftRightY) / DEFAULT_KM;
            }

            driftRightDistanceInNm = DecimalUtil.rounding(driftRightDistanceInNm);

            Double[] driftRightLatLon = MapCalculation.calculatePointInNm(
                    incLatitude,
                    incLongitude, driftRightDistanceInNm, driftRightAngle);

            BigDecimal driftRightLat = new BigDecimal(driftRightLatLon[0]).setScale(
                    6, BigDecimal.ROUND_HALF_UP);

            BigDecimal driftRightLon = new BigDecimal(driftRightLatLon[1]).setScale(
                    6, BigDecimal.ROUND_HALF_UP);
            double driftLeftToRightDistance = 0.0;
            if (unit == 0) {
                driftLeftToRightDistance = Math.sqrt(Math.pow(
                        lwyDistance,
                        2)
                        + Math.pow(lwyDistance, 2)
                        - 2 * lwyDistance * lwyDistance * Math.cos(
                                Math.toRadians(lwyDivergenceAngle * 2)));
            } else {
                driftLeftToRightDistance = Math.sqrt(Math.pow(
                        lwyDistance,
                        2)
                        + Math.pow(lwyDistance, 2)
                        - 2 * lwyDistance * lwyDistance * Math.cos(
                                Math.toRadians(lwyDivergenceAngle * 2))) / DEFAULT_KM;
            }
            driftLeftToRightDistance = DecimalUtil.rounding(driftLeftToRightDistance);

            double deDriftLeftToRightDistance = (driftErrorPctg * driftLeftDistanceInNm / 100)
                    + (driftErrorPctg * driftRightDistanceInNm / 100) + driftLeftToRightDistance;
            double driftError = 0.0;
            if (unit == 0) {
                driftError = deDriftLeftToRightDistance / 2;
            } else {
                driftError = (deDriftLeftToRightDistance / 2) / DEFAULT_KM;
            }
            driftError = DecimalUtil.rounding(driftError);
            double totalProbableError = 0.0;
            if (unit == 0) {
                totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                        + Math.pow(distressCraftError, 2)
                        + Math.pow(searchCraftError, 2));
            } else {
                totalProbableError = Math.sqrt(Math.pow(driftError, 2)
                        + Math.pow(distressCraftError, 2)
                        + Math.pow(searchCraftError, 2)) / DEFAULT_KM;
            }
            totalProbableError = DecimalUtil.rounding(totalProbableError);
            double searchRadius = 0.0;
            if (unit == 0) {
                searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                        setScale(0, BigDecimal.ROUND_UP).doubleValue();
            } else {
                searchRadius = new BigDecimal(safetyFactor * totalProbableError).
                        setScale(0, BigDecimal.ROUND_UP).doubleValue() / DEFAULT_KM;
            }

            searchRadius = DecimalUtil.rounding(searchRadius);
            double searchArea = 0.0;
            if (unit == 0) {
                searchArea = Math.pow((2 * searchRadius), 2);
            } else {
                searchArea = Math.pow((2 * searchRadius), 2) / DEFAULT_KM;
            }
            searchArea = DecimalUtil.rounding(searchArea);

            double deLeftToDriftLeftDistance = driftErrorPctg * driftLeftDistanceInNm / 100;
            double driftLeftToDatumDistance = deDriftLeftToRightDistance / 2 - deLeftToDriftLeftDistance;
            double driftLeftToRightX = driftRightX - driftLeftX;
            double driftLeftToRightY = driftRightY - driftLeftY;
            double driftLeftToRightAngle = MapCalculation.calculateAngle(
                    driftLeftToRightX, driftLeftToRightY);

            double datumX = (driftLeftToRightX / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftX;
            double datumY = (driftLeftToRightY / driftLeftToRightDistance * driftLeftToDatumDistance) + driftLeftY;

            Double datumAngle = prosia.basarnas.service.MapCalculation.calculateAngle(datumX, datumY);
            Double datumDistanceInNm = prosia.basarnas.service.MapCalculation.calculateDistance(datumX,
                    datumY);
            Double[] datumLatLon = MapCalculation.calculatePointInNm(
                    incLatitude,
                    incLongitude, datumDistanceInNm, datumAngle);

            BigDecimal datumLat = new BigDecimal(datumLatLon[0]).setScale(
                    6, BigDecimal.ROUND_HALF_UP);

            BigDecimal datumLon = new BigDecimal(datumLatLon[1]).setScale(
                    6, BigDecimal.ROUND_HALF_UP);

            mc.setDatum(new LatLng(datumLat.doubleValue(), datumLon.doubleValue()));
            mc.setRadius(searchRadius);
            mc.setSeaCurrentAngle(seaCurrentAngle);
            mc.setSeaCurrentSpeed(seaCurrentSpeed);
            mc.setSurfaceWindAngle(surfaceWindAngle);
            mc.setWindCurrentSpeed(windCurrentSpeed);
            listMonteCarlo.add(mc);
        }
        return listMonteCarlo;
    }
}
