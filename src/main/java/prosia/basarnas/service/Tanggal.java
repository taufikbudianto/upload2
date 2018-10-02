/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 *
 * @author Fauzi
 */
public class Tanggal extends Date {

//    private static String datePattern = "MM/dd/yyyy";
//    private static String dateTimePattern = "MM/dd/yyyy hh:mm:ss";
//    private static String datePattern = CommonConstant.DATE_PATTERN;
//    private static String dateTimePattern = CommonConstant.DATE_TIME_PATTERN;
    private static String datePattern = "";
    private static String dateTimePattern = "";
    
    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOUR_MILLIS * 24;
    public final static long YEAR_MILLIS = DAY_MILLIS * 365;

    /**
     *  Method untuk mengubah Date menjadi String dengan format YYYYMMDDHHMMZ
     * <br>
     * @param date (variabel Date yang ingin diconvert ke String)
     * @param timeZone (timeZone date)
     */
    public static String sarDateTimeStringConvert(Date date, TimeZone timeZone) {
        String dateString = "";
        if (date != null) {
            String tz = "";
            if (timeZone != null) {
                if (timeZone.getRawOffset() == TimeZone.getTimeZone("GMT+00:00").getRawOffset()) {
                    tz = "Z";
                } else if (timeZone.getRawOffset() == TimeZone.getTimeZone("GMT+08:00").getRawOffset()) {
                    tz = "H";
                } else if (timeZone.getRawOffset() == TimeZone.getTimeZone("GMT+09:00").getRawOffset()) {
                    tz = "I";
                } else {
                    tz = "G";
                }
            } else {
                tz = "G";
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String year = Integer.valueOf(cal.get(Calendar.YEAR)).toString();
            String month = Integer.valueOf(cal.get(Calendar.MONTH) + 1).toString();
            String tgl = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH)).toString();
            String hour = Integer.valueOf(cal.get(Calendar.HOUR_OF_DAY)).toString();
            String minute = Integer.valueOf(cal.get(Calendar.MINUTE)).toString();
            if (month.length() != 2) {
                month = "0" + month;
            }
            if (tgl.length() != 2) {
                tgl = "0" + tgl;
            }
            if (hour.length() != 2) {
                hour = "0" + hour;
            }
            if (minute.length() != 2) {
                minute = "0" + minute;
            }
            dateString = year + month + tgl + hour + minute + tz;
        }
        return dateString;
    }

    /**
     *  Method untuk mengubah Date menjadi String dengan format YYYYMMDDHHMMZ timezone mengambil dari properties
     * <br>
     * @param date (variabel Date yang ingin diconvert ke String)
     * @param dateHourDiff (string perbedaan waktu dari properties)
     */
    public static String sarDateTimeStringConvert(Date date, String dateHourDiff) {
        String dateString = "";
        String tz = "";
        if (dateHourDiff.equalsIgnoreCase("-1")) {
            tz = "H";
        } else if (dateHourDiff.equalsIgnoreCase("-2")) {
            tz = "I";
        } else {
            tz = "G";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = Integer.valueOf(cal.get(Calendar.YEAR)).toString();
        String month = Integer.valueOf(cal.get(Calendar.MONTH) + 1).toString();
        String tgl = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH)).toString();
        String hour = Integer.valueOf(cal.get(Calendar.HOUR_OF_DAY)).toString();
        String minute = Integer.valueOf(cal.get(Calendar.MINUTE)).toString();
        if (month.length() != 2) {
            month = "0" + month;
        }
        if (tgl.length() != 2) {
            tgl = "0" + tgl;
        }
        if (hour.length() != 2) {
            hour = "0" + hour;
        }
        if (minute.length() != 2) {
            minute = "0" + minute;
        }
        dateString = year + month + tgl + hour + minute + tz;
        return dateString;
    }

    /**
     *  Method untuk mengubah Date menjadi String dengan format MM/dd/yyyy
     * <br>
     * @param date (variabel Date yang ingin diconvert ke String)
     */
    public static String dateStringConvert(Date date) {
        if (date == null) {
            return "Never";
        }
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.format(new Date(date.getTime()));
    }

    /**
     *  Method untuk mengubah Date menjadi String dengan format MM/dd/yyyy hh:mm:ss
     * <br>
     * @param date (variabel Date yang ingin diconvert ke String)
     */
    public static String dateTimeStringConvert(Date date) {
        if (date == null) {
            return "Never";
        }
        SimpleDateFormat format = new SimpleDateFormat(dateTimePattern);
        return format.format(new Date(date.getTime()));
    }

    /**
     *  Method untuk mengubah Date menjadi String dengan format MM/dd/yyyy hh:mm:ss
     * <br>
     * @param date (variabel Date yang ingin diconvert ke String)
     */
    public static String monthYearStringConvert(Date date) {
        String month = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer mo = cal.get(Calendar.MONTH);
        Integer year = cal.get(Calendar.YEAR);
        if (mo == 0) {
            month = "Januari";
        } else if (mo == 1) {
            month = "Februari";
        } else if (mo == 2) {
            month = "Maret";
        } else if (mo == 3) {
            month = "April";
        } else if (mo == 4) {
            month = "Mei";
        } else if (mo == 5) {
            month = "Juni";
        } else if (mo == 6) {
            month = "Juli";
        } else if (mo == 7) {
            month = "Agustus";
        } else if (mo == 8) {
            month = "September";
        } else if (mo == 9) {
            month = "Oktober";
        } else if (mo == 10) {
            month = "November";
        } else if (mo == 11) {
            month = "Desember";
        }
        return month + " " + year;
    }

    /**
     *  Method untuk mengubah String dengan format MM/dd/yyyy menjadi Date 
     * <br>
     * @param string (variabel String yang ingin diconvert ke Date)
     */
    public static Date stringDateConvert(String string) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.parse(string);
    }

    /**
     *  Method untuk mengubah String dengan format MM/dd/yyyy hh:mm:ss menjadi Date 
     * <br>
     * @param string (variabel String yang ingin diconvert ke Date)
     */
    public static Date stringDateTimeConvert(String string) throws ParseException {
        if (string.equalsIgnoreCase("Never")) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateTimePattern);
            return format.parse(string);
        }
    }

    /**
     *  Method untuk menambahkan hari pada Date 
     * <br>
     * @param date (Data Date yang ingin ditambah harinya)
     * @param day (Integer jumlah hari yang ingin ditambah)
     */
    public static Date addDayToDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     *  Method untuk menambahkan jam pada Date 
     * <br>
     * @param date (Data Date yang ingin ditambah harinya)
     * @param hour (Integer jumlah jam yang ingin ditambah)
     */
    public static Date addHourToDate(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * Method for resetting hours, minutes, seconds, milliseconds become 0
     * @param date want to be trimmed
     * @return date result of trimmed date
     */
    public static Date trimTime(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Method for resetting seconds, milliseconds become 0
     * @param date want to be trimmed
     * @return date result of trimmed date
     */
    public static Date trimSeconds(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Get the seconds difference
     */
    public static double secondsDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (((double) laterDate.getTime() / SECOND_MILLIS) - ((double) earlierDate.getTime() / SECOND_MILLIS));
    }

    /**
     * Get the minutes difference
     */
    public static double minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (((double) laterDate.getTime() / MINUTE_MILLIS) - ((double) earlierDate.getTime() / MINUTE_MILLIS));
    }

    /**
     * Get the hours difference
     */
    public static double hoursDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (((double) laterDate.getTime() / HOUR_MILLIS) - ((double) earlierDate.getTime() / HOUR_MILLIS));
    }

    /**
     * Get the days difference
     */
    public static double daysDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (((double) laterDate.getTime() / DAY_MILLIS) - ((double) earlierDate.getTime() / DAY_MILLIS));
    }

//    public static void main(String[] args) {
//        System.out.println(Tanggal.sarDateTimeStringConvert(new Date(), ""));
//    }
    
    /**
     * give null to parameter if you want not change date properties
     */
    public static Date set(Integer sec, Integer minute, Integer hour, Integer day, Integer mounth, Integer year, Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);   // assigns calendar to given date 
        
        
        if(year != null){
            calendar.set(Calendar.YEAR, year);
        }
        if(mounth != null){
            calendar.set(Calendar.MONTH, mounth);
        }
        if(day != null){
            calendar.set(Calendar.DAY_OF_MONTH, mounth);
        }
        if(hour != null){
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if(minute != null){
            calendar.set(Calendar.MINUTE, minute); 
        }
        if(sec != null){
            calendar.set(Calendar.SECOND, sec);
        }
        return calendar.getTime();
    }
    
//    public static void main(String[] args) {
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
////        calendar.setTime(date);   // assigns calendar to given date
//        System.out.println(Tanggal.dateTimeStringConvert(date));
//        Date am5 = new Date();
//        calendar.setTime(am5);
////        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 5, 5, 0);
//        calendar.set(Calendar.HOUR_OF_DAY, 5);
//        calendar.set(Calendar.MINUTE, 5);
//        calendar.set(Calendar.SECOND, 0);
//        System.out.println(Tanggal.dateTimeStringConvert(calendar.getTime()));
//        System.out.println(calendar.get(Calendar.MINUTE));
//        if(date.before(calendar.getTime())){
//            System.out.println("ya");
//        }else{
//            System.out.println("tidak");
//        }
//    }
    
    
}