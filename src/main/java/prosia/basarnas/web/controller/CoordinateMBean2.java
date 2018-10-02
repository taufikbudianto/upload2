package prosia.basarnas.web.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.basarnas.constant.ProsiaConstant;

@Controller
@Scope("view")
public @Data
class CoordinateMBean2 {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Integer counterLat;
    private Integer counterLong;
    private String lastSystemValue;
    private Boolean userDefine;
    private Double residual;
    private String lastSystemValueLong;
    private Boolean userDefineLong;
    private Double residualLong;
    private String lintang;
    private String bujur;
    private String degreeLat;
    private String minuteLat;
    private String secondLat;
    private String hourLat;
    private String decimalMinuteLat;
    private String decimalLat;
    private String degreeLong;
    private String minuteLong;
    private String secondLong;
    private String hourLong;
    private String decimalMinuteLong;
    private String decimalLong;
    private String latitude;
    private String longitude;
    private Boolean disabled;

    public CoordinateMBean2() {
        refresh();
    }

    public void refresh() {
        counterLat = 1;
        counterLong = 1;
        lastSystemValue = "";
        userDefine = true;
        lastSystemValueLong = "";
        userDefineLong = true;
        hourLat = "";
        decimalMinuteLat = "";
        decimalLat = "";
        hourLong = "";
        decimalMinuteLong = "";
        decimalLong = "";
        latitude = null;
        longitude = null;
        disabled = false;
        setToDefaultUI();
        setToDefaultUILong();
    }

    public void nextCounterLat() {
        logger.debug("counterLat : {}", counterLat);
        try {
            if (counterLat != null) {
                switch (counterLat) {
                    case 1: {
                        String str = getValueDegreeTime();
                        latitude = str;
                        setFieldText(str);
                        break;
                    }
                    case 2: {
                        String str = getValueDecimalDegree();
                        latitude = str;
                        setValueDecimalDegree(str);
                        break;
                    }
                    case 3: {
                        String str = getValueTimeDecimal();
                        latitude = str;
                        setValueDegreeTime(str);
                        break;
                    }
                    default:
                        break;
                }
            }
            if (counterLat > 2) {
                counterLat = 0;
            }
            counterLat++;
        } catch (Exception e) {
            logger.debug("Latitude Exception : {}", e.getMessage());
        }
    }

    public void nextCounterLong() {
        try {
            if (counterLong != null) {
                switch (counterLong) {
                    case 1: {
                        String str = getValueDegreeTimeLong();
                        longitude = str;
                        setFieldTextLong(str);
                        break;
                    }
                    case 2: {
                        String str = getValueDecimalDegreeLong();
                        longitude = str;
                        setValueDecimalDegreeLong(str);
                        break;
                    }
                    case 3: {
                        String str = getValueTimeDecimalLong();
                        longitude = str;
                        setValueDegreeTimeLong(str);
                        break;
                    }
                    default:
                        break;
                }
            }
            if (counterLong > 2) {
                counterLong = 0;
            }
            counterLong++;
        } catch (Exception e) {
            logger.debug("Longitude Exception : {}", e.getMessage());
        }
    }

    public String getValueDegreeTime() {
        try {
            if (getTextDegreeUI() == null) {
                return null;
            }
            if (lastSystemValue == null || !lastSystemValue.equalsIgnoreCase(getTextDegreeUI()) && userDefine) {
                residual = Double.valueOf(0);
            }
            String degree = convertTo(new LatLngConvertResult(getTextDegreeUI(), residual, ProsiaConstant.TIME_DEGREE)).getDegree();
            if (lintang.equals(ProsiaConstant.LINTANG_SELATAN)) {
                degree = ProsiaConstant.MINUS_PREFIX + degree;
            }
            logger.debug("degree : {}", degree);
            return degree;
        } catch (Exception e) {
            logger.debug("E : {}", e.getMessage());
            return null;
        }
    }

    public String getValueDegreeTimeLong() {
        try {
            if (getTextDegreeUILong() == null) {
                return null;
            }
            if (lastSystemValueLong == null || !lastSystemValueLong.equalsIgnoreCase(getTextDegreeUILong()) && userDefineLong) {
                residualLong = Double.valueOf(0);
            }
            String degree = convertTo(new LatLngConvertResult(getTextDegreeUILong(), residualLong, ProsiaConstant.TIME_DEGREE)).getDegree();
            if (bujur.equals(ProsiaConstant.BUJUR_BARAT)) {
                degree = ProsiaConstant.MINUS_PREFIX + degree;
            }
            logger.debug("degree : {}", degree);
            return degree;
        } catch (Exception e) {
            logger.debug("E : {}", e.getMessage());
            return null;
        }
    }

    public void setFieldText(String string) {
        if (string == null && lintang.equals(ProsiaConstant.LINTANG_SELATAN)) {
            lintang = ProsiaConstant.LINTANG_UTARA;
        }
        if (string != null && string.contains(ProsiaConstant.MINUS_PREFIX)) {
            lintang = ProsiaConstant.LINTANG_SELATAN;
        }
        if (string != null) {
            decimalLat = string.replace(ProsiaConstant.MINUS_PREFIX, ProsiaConstant.EMPTY_STRING);
        }
    }

    public void setFieldTextLong(String string) {
        if (string == null && bujur.equals(ProsiaConstant.BUJUR_BARAT)) {
            bujur = ProsiaConstant.BUJUR_TIMUR;
        }
        if (string != null && string.contains(ProsiaConstant.MINUS_PREFIX)) {
            bujur = ProsiaConstant.BUJUR_BARAT;
        }
        if (string != null) {
            decimalLong = string.replace(ProsiaConstant.MINUS_PREFIX, ProsiaConstant.EMPTY_STRING);
        }
    }

    public String getValueDecimalDegree() {
        String out = decimalLat;
        if (lintang.equals(ProsiaConstant.LINTANG_SELATAN)) {
            out = ProsiaConstant.MINUS_PREFIX + out;
        }
        return out;
    }

    public String getValueDecimalDegreeLong() {
        String out = decimalLong;
        if (bujur.equals(ProsiaConstant.BUJUR_BARAT)) {
            out = ProsiaConstant.MINUS_PREFIX + out;
        }
        return out;
    }

    public void setValueDecimalDegree(String string) {
        if (string != null && !string.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING) && !string.equals(ProsiaConstant.MINUS_PREFIX)) {
            String temp = convertTo(ProsiaConstant.TIME_DEGREE, string).replace(ProsiaConstant.MINUS_PREFIX, ProsiaConstant.EMPTY_STRING);
            String[] tempSplit = temp.split("'");
            if (tempSplit.length == 3) {
                String decimal = tempSplit[2];
                Double d = Double.valueOf(decimal);
                d = d / 60;
                decimal = d.toString().replace("0.", ProsiaConstant.EMPTY_STRING);
                hourLat = tempSplit[0];
                decimalMinuteLat = tempSplit[1] + "." + decimal;
            }
            if (string.contains(ProsiaConstant.MINUS_PREFIX)) {
                lintang = ProsiaConstant.LINTANG_SELATAN;
            }
        } else {
            hourLat = ProsiaConstant.EMPTY_STRING;
            decimalMinuteLat = ProsiaConstant.EMPTY_STRING;
            lintang = ProsiaConstant.LINTANG_UTARA;
        }
    }

    public void setValueDecimalDegreeLong(String string) {
        if (string != null && !string.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING) && !string.equals(ProsiaConstant.MINUS_PREFIX)) {
            String temp = convertTo(ProsiaConstant.TIME_DEGREE, string).replace(ProsiaConstant.MINUS_PREFIX, ProsiaConstant.EMPTY_STRING);
            String[] tempSplit = temp.split("'");
            if (tempSplit.length == 3) {
                String decimal = tempSplit[2];
                Double d = Double.valueOf(decimal);
                d = d / 60;
                decimal = d.toString().replace("0.", ProsiaConstant.EMPTY_STRING);
                hourLong = tempSplit[0];
                decimalMinuteLong = tempSplit[1] + "." + decimal;
            }
            if (string.contains(ProsiaConstant.MINUS_PREFIX)) {
                bujur = ProsiaConstant.BUJUR_BARAT;
            }
        } else {
            hourLong = ProsiaConstant.EMPTY_STRING;
            decimalMinuteLong = ProsiaConstant.EMPTY_STRING;
            bujur = ProsiaConstant.BUJUR_TIMUR;
        }
    }

    public String getValueTimeDecimal() {
        if (hourLat.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING) && decimalMinuteLat.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            return null;
        }
        String decimalMinute = decimalMinuteLat;
        String[] split = decimalMinute.split("\\.");
        String hour = "0";
        String minutes = "0";
        String second = "0";
        if (hourLat != null && !hourLat.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            hour = hourLat;
        }
        if (split.length > 0 && !split[0].equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            minutes = split[0];
        }
        if (split.length > 1) {
            Double d = Double.valueOf("0." + split[1]);
            d = d * 60;
            String[] temp = d.toString().split("\\.");
            if (temp.length > 0) {
                second = temp[0];
            } else {
                second = d.toString();
            }
        }
        String input = hour + "'" + minutes + "'" + second;
        String val = convertTo(ProsiaConstant.DECIMAL_DEGREE, input);
        if (lintang.equals(ProsiaConstant.LINTANG_SELATAN)) {
            val = ProsiaConstant.MINUS_PREFIX + val;
        }
        return val;
    }

    public String getValueTimeDecimalLong() {
        if (hourLong.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING) && decimalMinuteLong.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            return null;
        }
        String decimalMinute = decimalMinuteLong;
        String[] split = decimalMinute.split("\\.");
        String hour = "0";
        String minutes = "0";
        String second = "0";
        if (hourLong != null && !hourLong.equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            hour = hourLong;
        }
        if (split.length > 0 && !split[0].equalsIgnoreCase(ProsiaConstant.EMPTY_STRING)) {
            minutes = split[0];
        }
        if (split.length > 1) {
            Double d = Double.valueOf("0." + split[1]);
            d = d * 60;
            String[] temp = d.toString().split("\\.");
            if (temp.length > 0) {
                second = temp[0];
            } else {
                second = d.toString();
            }
        }
        String input = hour + "'" + minutes + "'" + second;
        String val = convertTo(ProsiaConstant.DECIMAL_DEGREE, input);
        if (bujur.equals(ProsiaConstant.BUJUR_BARAT)) {
            val = ProsiaConstant.MINUS_PREFIX + val;
        }
        return val;
    }

    public void setValueDegreeTime(String value) {
        if (value == null || value.equals(ProsiaConstant.EMPTY_STRING)) {
            clearTextDegreeUI();
            return;
        }
        userDefine = false;
        LatLngConvertResult capital = new LatLngConvertResult(value, Double.valueOf(0), ProsiaConstant.DECIMAL_DEGREE),
                result = convertTo(capital);
        setTimeDegreeValue(result);
        lastSystemValue = getTextDegreeUI();
    }

    public void setValueDegreeTimeLong(String value) {
        if (value == null || value.equals(ProsiaConstant.EMPTY_STRING)) {
            clearTextDegreeUILong();
            return;
        }
        userDefineLong = false;
        LatLngConvertResult capital = new LatLngConvertResult(value, Double.valueOf(0), ProsiaConstant.DECIMAL_DEGREE),
                result = convertTo(capital);
        setTimeDegreeValueLong(result);
        lastSystemValueLong = getTextDegreeUILong();
    }

    public String getTextDegreeUI() {
        if (degreeLat.equals(ProsiaConstant.EMPTY_STRING)
                || minuteLat.equals(ProsiaConstant.EMPTY_STRING)
                || secondLat.equals(ProsiaConstant.EMPTY_STRING)) {
            return null;
        }
        return degreeLat + ProsiaConstant.DEGREE_TIME_SEPARATOR
                + minuteLat + ProsiaConstant.DEGREE_TIME_SEPARATOR
                + secondLat;
    }

    public String getTextDegreeUILong() {
        if (degreeLong.equals(ProsiaConstant.EMPTY_STRING)
                || minuteLong.equals(ProsiaConstant.EMPTY_STRING)
                || secondLong.equals(ProsiaConstant.EMPTY_STRING)) {
            return null;
        }
        return degreeLong + ProsiaConstant.DEGREE_TIME_SEPARATOR
                + minuteLong + ProsiaConstant.DEGREE_TIME_SEPARATOR
                + secondLong;
    }

    public static String convertTo(int format, String value) {
        String result = ProsiaConstant.EMPTY_STRING;
        String z;
        if (!value.contains(".")) {
            z = value + ".0";
        } else {
            z = value;
        }
        double x;
        String[] a;
        if (format == ProsiaConstant.TIME_DEGREE) {
            for (int i = 0; i < 3; ++i) {
                a = z.split("\\.");
                result += a[0] + ProsiaConstant.DEGREE_TIME_SEPARATOR;
                z = "0." + a[1];
                x = Double.valueOf(z) * 60;
                z = String.valueOf(x);
            }
            result = result.substring(0, result.length() - 1);
        } else if (format == ProsiaConstant.DECIMAL_DEGREE) {
            a = value.split(ProsiaConstant.DEGREE_TIME_SEPARATOR);
            x = Double.valueOf(a[2]);
            for (int i = 2; i >= 1; --i) {
                x = x / 60;
                x = Double.valueOf(a[i - 1]) + x;
            }
            result = String.valueOf(x);
        }
        return result;
    }

    private static LatLngConvertResult convertTo(LatLngConvertResult value) {
        double dfDegree, dfMinute, dfSecond, residual;
        double dfFrac, dfSec;
        double dfDecimal;
        if (value.getType() == ProsiaConstant.TIME_DEGREE) {
            String[] dms = value.getDegree().split("'");
            dfDegree = Double.parseDouble(dms[0]);
            dfMinute = Double.parseDouble(dms[1]);
            dfSecond = Double.parseDouble(dms[2]);
            dfSecond += value.getResedual();
            dfFrac = dfMinute / 60 + dfSecond / 3600;
            if (dfDegree < 0) {
                dfDecimal = dfDegree - dfFrac;
            } else {
                dfDecimal = dfDegree + dfFrac;
            }
            return new LatLngConvertResult(String.valueOf(dfDecimal), Double.valueOf(0), ProsiaConstant.DECIMAL_DEGREE);
        } else if (value.getType() == ProsiaConstant.DECIMAL_DEGREE) {
            if (value.getDegree().startsWith("+")) {
                value.setDegree(value.getDegree().substring(1));
            }
            double deg = Double.valueOf(value.getDegree());
            boolean isMinus = false;
            if (value.getDegree().startsWith(ProsiaConstant.MINUS_PREFIX)) {
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
                degree = ProsiaConstant.MINUS_PREFIX + String.valueOf((int) Math.ceil(--dfDecimal));
            } else {
                degree = String.valueOf((int) Math.floor(dfDecimal));
            }
            int minute = (int) dfMinute;
            int second = (int) Math.floor(dfSecond);
            LatLngConvertResult result = new LatLngConvertResult(degree + ProsiaConstant.DEGREE_TIME_SEPARATOR + minute + ProsiaConstant.DEGREE_TIME_SEPARATOR + second, residual, ProsiaConstant.TIME_DEGREE);
            return result;
        }
        return null;
    }

    public void clearTextDegreeUI() {
        setToDefaultUI();
        residual = Double.valueOf(0);
        lastSystemValue = null;
        userDefine = true;
    }

    public void clearTextDegreeUILong() {
        setToDefaultUILong();
        residualLong = Double.valueOf(0);
        lastSystemValueLong = null;
        userDefineLong = true;
    }

    private void setTimeDegreeValue(LatLngConvertResult convertResult) {
        String degree = convertResult.getDegree();
        residual = convertResult.getResedual();
        if (degree.startsWith(ProsiaConstant.MINUS_PREFIX)) {
            degree = degree.substring(1, degree.length());
            lintang = ProsiaConstant.LINTANG_SELATAN;
        } else {
            lintang = ProsiaConstant.LINTANG_UTARA;
        }
        setTextDegreeUI(degree);
    }

    private void setTimeDegreeValueLong(LatLngConvertResult convertResult) {
        String degree = convertResult.getDegree();
        residualLong = convertResult.getResedual();
        if (degree.startsWith(ProsiaConstant.MINUS_PREFIX)) {
            degree = degree.substring(1, degree.length());
            bujur = ProsiaConstant.BUJUR_BARAT;
        } else {
            bujur = ProsiaConstant.BUJUR_TIMUR;
        }
        setTextDegreeUILong(degree);
    }

    private void setToDefaultUI() {
        degreeLat = ProsiaConstant.EMPTY_STRING;
        minuteLat = ProsiaConstant.EMPTY_STRING;
        secondLat = ProsiaConstant.EMPTY_STRING;
        lintang = ProsiaConstant.LINTANG_UTARA;
    }

    private void setToDefaultUILong() {
        degreeLong = ProsiaConstant.EMPTY_STRING;
        minuteLong = ProsiaConstant.EMPTY_STRING;
        secondLong = ProsiaConstant.EMPTY_STRING;
        bujur = ProsiaConstant.BUJUR_TIMUR;
    }

    public void setTextDegreeUI(String m) {
        if (m == null) {
            degreeLat = ProsiaConstant.EMPTY_STRING;
            minuteLat = ProsiaConstant.EMPTY_STRING;
            secondLat = ProsiaConstant.EMPTY_STRING;
        }
        if (!m.equals(ProsiaConstant.EMPTY_STRING));
        String[] y = m.split(ProsiaConstant.DEGREE_TIME_SEPARATOR);
        if (y.length < 3) {
            degreeLat = y[0];
            minuteLat = y[1];
        } else if (y.length == 3) {
            degreeLat = y[0];
            minuteLat = y[1];
            secondLat = y[2];
        }
    }

    public void setTextDegreeUILong(String m) {
        if (m == null) {
            degreeLong = ProsiaConstant.EMPTY_STRING;
            minuteLong = ProsiaConstant.EMPTY_STRING;
            secondLong = ProsiaConstant.EMPTY_STRING;
        }
        if (!m.equals(ProsiaConstant.EMPTY_STRING));
        String[] y = m.split(ProsiaConstant.DEGREE_TIME_SEPARATOR);
        if (y.length < 3) {
            degreeLong = y[0];
            minuteLong = y[1];
        } else if (y.length == 3) {
            degreeLong = y[0];
            minuteLong = y[1];
            secondLong = y[2];
        }
    }

    static @Data
    class LatLngConvertResult {

        private String degree;
        private Double resedual;
        private Integer type;

        public LatLngConvertResult(String degree, Double resedual, Integer type) {
            this.degree = degree;
            this.resedual = resedual;
            this.type = type;
        }
    }
}
