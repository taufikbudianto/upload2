/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author PROSIA
 */
@Data
public class Coordinate {

    private Integer degree;
    private Integer minute;
    private Integer second;
    private Double minutesM;
    private Double decimalDegrees;
    private Integer counter = 3;
    /*1 = LS/BB, 0 = LU/BT*/
    private Integer garis;
    /*true = latitude, false = longitude*/
    private Boolean type;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer degreeTemp = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer minuteTemp = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer secondTemp = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Double minutesMTemp = null;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Double decimalDegreesTemp = null;

    public void converter() {
        if (counter == 1 && decimalDegrees != null) {
            if (decimalDegrees < 0) {
                garis = 1;
            } else {
                garis = 0;
            }
            decimalDegrees = Math.abs(decimalDegrees);
            if (decimalDegreesTemp == null) {
                decimalDegreesTemp = decimalDegrees;
                degree = decimalDegrees.intValue();
                dms();
                dmm();
            } else if (!decimalDegreesTemp.equals(decimalDegrees)) {
                degree = decimalDegrees.intValue();
                dms();
                dmm();
                decimalDegreesTemp = decimalDegrees;
            }
        } else if (counter == 1 && decimalDegrees == null) {
            decimalDegrees = null;
            degree = null;
            decimalDegreesTemp = decimalDegrees;
            garis = 0;
            dms();
            dmm();
        }
        if (counter == 2 && degree != null && minutesM != null) {
            if (degreeTemp == null || minutesMTemp == null) {
                degreeTemp = degree;
                minutesMTemp = minutesM;
                dd();
                dms();
            } else if (!degreeTemp.equals(degree)) {
                degreeTemp = degree;
                dd();
                dms();
            } else if (!minutesMTemp.equals(minutesM)) {
                minutesMTemp = minutesM;
                dd();
                dms();
            }
        } else if (counter == 2 && (degree == null || minutesM == null)) {
            degree = null;
            minutesM = null;
            degreeTemp = degree;
            minutesMTemp = minutesM;
            garis = 0;
            dd();
            dms();
        }
        if (counter == 3 && (degree != null && minute != null && second != null)) {
            if (degreeTemp == null || minuteTemp == null || secondTemp == null) {
                degreeTemp = degree;
                minuteTemp = minute;
                secondTemp = second;
                dmm();
                dd();
            } else if (!degreeTemp.equals(degree)) {
                degreeTemp = degree;
                dmm();
                dd();
            } else if (!minuteTemp.equals(minute)) {
                minuteTemp = minute;
                dmm();
                dd();
            } else if (!secondTemp.equals(second)) {
                secondTemp = second;
                dmm();
                dd();
            }
        } else if (counter == 3 && (degree == null || minute == null || second == null)) {
            degree = null;
            minute = null;
            second = null;
            degreeTemp = degree;
            minuteTemp = minute;
            secondTemp = second;
            garis = 0;
            dmm();
            dd();
        }
    }

    private void dmm() {
        if (minute != null && second != null) {

//        degree = decimalDegrees.intValue();
//            double d = minute.doubleValue() + (second.doubleValue() / 60);
//        BigDecimal bd = new BigDecimal(d);
//        minutesM = bd.setScale(14, BigDecimal.ROUND_CEILING).doubleValue();
//            DecimalFormat conv = new DecimalFormat("#.##############");
//            minutesM = Double.valueOf(conv.format(d));
            minutesM = minute + ((double) second / 60.0);
            minutesMTemp = minutesM;
//        minutesM = d;
        } else {
            minutesM = null;
            minutesMTemp = minutesM;
        }
    }

    private void dms() {
        if (decimalDegrees != null && degree != null) {
//            second = (int) Math.round(decimalDegrees * 3600);
//            degree = second / 3600;
//            second = Math.abs(second % 3600);
//            minute = second / 60;
//            second %= 60;

            minute = Double.valueOf((decimalDegrees - degree) * 60).intValue();
            Double a = (minute.doubleValue() / 60);
            Double b = (decimalDegrees - degree - a) * 3600;
            BigDecimal bd = new BigDecimal(b);

            if (type) {
//                second = bd.setScale(0, RoundingMode.DOWN).intValue();
                second = bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            } else {
                second = bd.setScale(0, BigDecimal.ROUND_UP).intValue();
            }
            minuteTemp = minute;
            secondTemp = second;
        } else {
            minute = null;
            second = null;
            minuteTemp = minute;
            secondTemp = second;
        }
//        String min_sec = Double.toString(minutesM);
//        if (min_sec.equals("0.0")) {
//            String min = min_sec.substring(0, 1);
//            /* minutes */
//            String sec = min_sec.substring(0, 2);
//            /* seconds */
//            minute = Double.valueOf(min) / (double) 60;
//            /* minutes DD */
//            second = Double.valueOf(sec) / (double) 3600;
//            /* seconds DD */
//        } else if (!min_sec.equals("0.0")) {
//            String min = min_sec.substring(2, 4);
//            /* minutes */
//            String sec = min_sec.substring(4, 6);
//            /* seconds */
//            minute = Double.valueOf(min) / (double) 60;
//            /* minutes DD */
//            second = Double.valueOf(sec) / (double) 3600;
//            /* seconds DD */
//        }
    }

    private void dd() {
        if (degree != null && minutesM != null) {
            double d = (double) minutesM / 60.0;
            decimalDegrees = degree + d;

            BigDecimal bd = new BigDecimal(decimalDegrees);
            if (type) {
                if (bd.setScale(0, RoundingMode.DOWN).intValue() > 90) {
                    decimalDegrees -= 1;
                }
            } else if (bd.setScale(0, RoundingMode.DOWN).intValue() > 180) {
                decimalDegrees -= 1;
            }
            decimalDegreesTemp = decimalDegrees;
        } else {
            decimalDegrees = null;
            decimalDegreesTemp = decimalDegrees;
        }
    }

//    public String getPrefix() {
//        if (garis == 1) {
//            return "-";
//        }
//        return "";
//    }
    public String getConvertDdToDms(String paramDd, Boolean type) {
        try {
            int secondConv = (int) Math.round(Math.abs(Double.valueOf(paramDd)) * 3600);
            int degreeConv = secondConv / 3600;
            secondConv = Math.abs(secondConv % 3600);
            int minuteConv = secondConv / 60;
            secondConv %= 60;
            String conv = degreeConv + "\u00b0 " + minuteConv
                    + "' " + secondConv + "\" ";
            if (type) {
                conv += " " + (Double.valueOf(paramDd) < 0 ? "LS" : "LU");
            } else {
                conv += " " + (Double.valueOf(paramDd) < 0 ? "BB" : "BT");
            }

            return conv;
        } catch (NumberFormatException exception) {
            return "-";
        }
    }

    public Double getValidDecimalDegrees() {
        Double valid = null;
        if (garis == 1 && decimalDegrees != null) {
            valid = -Math.abs(decimalDegrees);
        } else if (garis == 0 && decimalDegrees != null) {
            valid = Math.abs(decimalDegrees);
        }
        return valid;
    }
}
