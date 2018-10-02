/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import prosia.basarnas.constant.ProsiaConstant;

/**
 *
 * @author Aris
 */
public class DecimalUtil implements Serializable{
    public static Double rounding(Double value) {
        return rounding(value, ProsiaConstant.DEFAULT_DECIMAL_PRECISION,
                RoundingMode.HALF_UP);
    }
    
    public static Double rounding(Double value, int scale) {
        return rounding(value, scale, RoundingMode.HALF_UP);
    }

    public static Double rounding(Double value, int scale,
            RoundingMode roundingMode) {
        if (value == null) {
            return null;
        }

        BigDecimal result = BigDecimal.valueOf(value).setScale(scale,
                roundingMode).stripTrailingZeros();

        return result.doubleValue();
    }
    
    public static Double roundingUpPerMultiple(Double value, double multiple) {
        if (value == null) {
            return null;
        }

        Double result = null;
        Double remain = value % multiple;
        if (remain.equals(0.0)) {
            result = value;
        } else {
            result = value + (multiple - remain);
        }

        return result;
    }

    public static Double roundingDownPerMultiple(Double value, double multiple) {
        if (value == null) {
            return null;
        }

        Double result = null;
        Double remain = value % multiple;
        if (remain.equals(0.0)) {
            result = value;
        } else {
            result = value - remain;
        }

        return result;
    }
}
