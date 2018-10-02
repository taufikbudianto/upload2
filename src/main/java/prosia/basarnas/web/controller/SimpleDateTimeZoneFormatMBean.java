package prosia.basarnas.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import prosia.basarnas.service.TimeZoneConstant;


public class SimpleDateTimeZoneFormatMBean extends SimpleDateFormat{
    private static final long serialVersionUID = 1L;
    private String pattern;
    private String datePattern;
    private String source;
    private String dateSource;
    private List<Integer> timeZoneIndexList = new ArrayList<Integer>();
    public static final String TIME_ZONE_PATTERN = "x";

    public SimpleDateTimeZoneFormatMBean(Locale locale) {
        super("dd/MM/yyyy", locale);
    }

    public SimpleDateTimeZoneFormatMBean(String pattern) {
        this.applyPattern(pattern);
    }
    
    public SimpleDateTimeZoneFormatMBean() {
    }

    @Override
    public void applyPattern(String pattern) {
        this.setPattern(pattern);
        super.applyPattern(datePattern);
    }

    @Override
    public String toPattern() {
        return pattern;
    }

    @Override
    public Date parse(String source) throws ParseException {
        this.setSource(source);
        return super.parse(dateSource);
    }

    public Calendar parseCalendar(String source) throws ParseException {
        this.setSource(source);
        System.out.println("dateSource = " + dateSource);
        Date dateResult = super.parse(dateSource);
        System.out.println("dateResult = " + dateResult);
        return this.getResult(dateResult);
    }

    /*
    public final String format(Calendar calendar) {
        // System.out.println(
        //        "SimpleDateTimeZoneFormat.format calendar = " + calendar);
        // System.out.println("SimpleDateTimeZoneFormat.format date = " + calendar.
        //        getTime());
        int gap = calendar.getTimeZone().getRawOffset() - TimeZone.getDefault().
                getRawOffset();

        String result = super.format(new Date(calendar.getTimeInMillis() + gap));
        // System.out.println("SimpleDateTimeZoneFormat.format result = " + result);
        return this.getString(result, calendar);
    }
*/
    public String format(Calendar calendar) {
        int gap = calendar.getTimeZone().getRawOffset() - TimeZone.getDefault().
                getRawOffset();

        String result = super.format(new Date(calendar.getTimeInMillis() + gap));
        // System.out.println("SimpleDateTimeZoneFormat.format result = " + result);
        return this.getString(result, calendar);
    }
    
    public void setPattern(String pattern) {
        timeZoneIndexList.clear();
        if (pattern == null) {
            this.pattern = null;
            this.datePattern = null;
        }

        this.pattern = pattern;
        this.datePattern = pattern.replaceAll(TIME_ZONE_PATTERN, "");
        if (pattern.contains(TIME_ZONE_PATTERN)) {
            int startIndex = -1;
            int index = 0;
            while (index != -1) {
                index = pattern.indexOf(TIME_ZONE_PATTERN, startIndex + 1);
                startIndex = index;
                if (index != -1) {
                    timeZoneIndexList.add(index);
                }
            }
        }
    }

    private void setSource(String source) {
        if (source == null) {
            this.source = null;
            this.dateSource = null;
        }

        this.source = source;
        this.dateSource = source;
        String start = null;
        String end = null;
        int trimmedCount = 0;
        if (!source.isEmpty()) {
            for (Integer index : timeZoneIndexList) {
                index -= trimmedCount;
                // System.out.println("index = " + index);
                // System.out.println("this.dateSource.length() = " + this.dateSource.
                //        length());
                if (index >= this.dateSource.length()) {
                    break;
                }
                start = dateSource.substring(0, index);
                end = (index + 1 >= dateSource.length()) ? "" : dateSource.
                        substring(index + 1, dateSource.length());
                // System.out.println("start = " + start);
                // System.out.println("end = " + end);
                this.dateSource = start + end;
                trimmedCount++;
            }
        }
    }

    private Calendar getResult(Date dateResult) throws ParseException {
        if (dateResult == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateResult);
        if (source != null) {
            TimeZone tz = null;
            for (Integer index : timeZoneIndexList) {
                //tz = TimeZoneConstant.MAP.get(source.substring(index, index + 1));

                if (tz == null) {
                    throw new ParseException(
                            "Unparseable date: \"" + source + "\"", index);
                }
                
                int gap = cal.getTimeZone().getRawOffset() - tz.getRawOffset();
                cal.setTimeInMillis(cal.getTimeInMillis() + gap);
                cal.setTimeZone(tz);
                break;
            }
        }

        return cal;
    }

    private String getString(String dateResult, Calendar cal) {
        String result = dateResult;
        TimeZone tz = cal.getTimeZone();
        Iterator it = TimeZoneConstant.MAP.keySet().iterator();
        String key = null;
        while (it.hasNext()) {
            key = (String) it.next();
            if (TimeZoneConstant.MAP.get(key).getRawOffset() == tz.getRawOffset()) {
                break;
            }
        }

        if (key == null) {
            key = " ";
        }

        for (Integer index : timeZoneIndexList) {
            result = result.substring(0, index) + key + result.substring(
                    index, result.length());
        }

        return result;
    }
}

