/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

/**
 *
 * @author Aris
 */
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static int hourDifferent;

    static {
        String strHourDifferent = ApplicationPropertiesHandler.getProperty(
                ApplicationPropertiesHandler.DATE_HOUR_DIFFERENCE_KEY);

        // if start with +, should be remove, if start with -, no problemo
        if (strHourDifferent.startsWith("+")) {
            strHourDifferent = strHourDifferent.substring(1);
        }

        hourDifferent = Integer.parseInt(strHourDifferent);
    }

    public static Date getSynchronizedCurrentDate() {
        return addHours(new Date(), hourDifferent);
    }

    public static Date getSynchronizedDate(Date date) {
        return addHours(date, hourDifferent);
    }

    public static Date getUnsynchronizedDate(Date date) {
        return addHours(date, hourDifferent * -1);
    }
}
