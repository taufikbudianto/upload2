/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import lombok.Data;
import prosia.basarnas.constant.ProsiaConstant;

/**
 *
 * @author PROSIA
 */
public class LatitudeLongitude {

    public static String latitideFormatted(String latitude) {
        try {
            String latitiudeFormat = "";
            if (latitude != null && !latitude.equals("")) {
                latitiudeFormat = decimalToTimeConvert(latitude);
                String[] y = latitiudeFormat.split("'");
                if (y.length < 3) {
                    latitiudeFormat = y[0] + "\u00b0 " + y[1] + "' 00\"";
                } else if (y.length == 3) {
                    latitiudeFormat = y[0] + "\u00b0 " + y[1] + "' " + y[2] + "\" ";
                }
                if (latitiudeFormat.split("-").length > 1) {
                    latitiudeFormat = latitiudeFormat.split("-")[1] + "LS";
                } else {
                    latitiudeFormat = latitiudeFormat + "LU";
                }
            } else {
                return "-";
            }
            return latitiudeFormat;
        } catch (Exception e) {
            return "#Format Salah#";
        }
    }

    public static String longitudeFormatted(String string) {
        try {
            String longitude = "";
            if (string != null && !string.equalsIgnoreCase("")) {
                longitude = decimalToTimeConvert(string);
                String[] y = longitude.split("'");
                if (y.length < 3) {
                    longitude = y[0] + "\u00b0 " + y[1] + "' 00\"";
                } else if (y.length == 3) {
                    longitude = y[0] + "\u00b0 " + y[1] + "' " + y[2] + "\" ";
                }
                if (longitude.split("-").length > 1) {
                    longitude = longitude.split("-")[1] + "BB";
                } else {
                    longitude = longitude + "BT";
                }
            } else {
                return "-";
            }
            return longitude;
        } catch (Exception e) {
            return "#Format Salah#";
        }
    }

    public static String convertTo(int format, String value) {
        String result = "";
        String z;
        if(!value.contains(".")){
            z = value + ".0";
        }else{
            z = value;
        }
        double x;
        String[] a;
        if (format == ProsiaConstant.TIME_DEGREE) {
            for (int i = 0; i < 3; ++i) {
                a = z.split("\\.");
                result += a[0] + "'";
                z = "0." + a[1];
                x = Double.valueOf(z) * 60;
                z = String.valueOf(x);
            }
            result = result.substring(0, result.length() - 1);
        } else if (format == ProsiaConstant.DECIMAL_DEGREE) {
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

    private static String decimalToTimeConvert(String decimal) {
        LatLangConvertResult result = convertTo(new LatLangConvertResult(decimal, 0.0, ProsiaConstant.DECIMAL_DEGREE));
        return result.getDegree();
    }

    public static LatLangConvertResult convertTo(LatLangConvertResult value) {
        double dfDegree, dfMinute, dfSecond, residual;
        double dfFrac, dfSec;
        double dfDecimal;
        if (value.getType() == ProsiaConstant.TIME_DEGREE) {
            String[] dms = value.getDegree().split("'");
            dfDegree = Double.parseDouble(dms[0]);
            dfMinute = Double.parseDouble(dms[1]);
            dfSecond = Double.parseDouble(dms[2]);
            dfSecond += value.getResedual();
            dfFrac = dfMinute / 60 + dfSecond / 360;
            if (dfDegree < 0) {
                dfDecimal = dfDegree - dfFrac;
            } else {
                dfDecimal = dfDegree + dfFrac;
            }
            return new LatLangConvertResult(String.valueOf(dfDecimal), 0, ProsiaConstant.DECIMAL_DEGREE);
        } else if (value.getType() == ProsiaConstant.DECIMAL_DEGREE) {
            if (value.getDegree().startsWith("+")) {
                value.setDegree(value.getDegree().substring(1));
            }
            double deg = Double.valueOf(value.getDegree());
            boolean isMinus = false;
            if (value.getDegree().startsWith("-")) {
                isMinus = true;
            }
            dfDecimal = Math.abs(deg);
            if (isMinus) {
                dfDegree = Math.ceil(deg);
            } else {
                dfDegree = Math.floor(deg);
            }
            dfFrac = Math.abs(deg - dfDegree);
            dfSec = dfFrac * 3600;
            dfMinute = Math.floor(dfSec / 60);
            dfSecond = dfSec - dfMinute * 60;

            if (Math.rint(dfSecond) == 60) {
                dfMinute = dfMinute + 1;
                dfSecond = 0;
            }
            if (Math.rint(dfMinute) == 60) {
                dfDecimal = dfDecimal + 1;
                dfMinute = 0;
            }
            residual = dfSecond - Math.floor(dfSecond);

            String degree;
            if (isMinus) {
                degree = "-" + String.valueOf((int) Math.ceil(--dfDecimal));
            } else {
                degree = String.valueOf((int) Math.floor(dfDecimal));
            }
            int minute = (int) dfMinute;
            int second = (int) Math.floor(dfSecond);
            LatLangConvertResult result = new LatLangConvertResult(degree + "'" + minute + "'" + second, residual, ProsiaConstant.TIME_DEGREE);
            return result;
        }
        return null;
    }

    public @Data
    static class LatLangConvertResult {

        private String degree;
        private double resedual;
        private Integer type;

        public LatLangConvertResult(String degree, double resedual, Integer type) {
            this.degree = degree;
            this.resedual = resedual;
            this.type = type;
        }
    }
}
