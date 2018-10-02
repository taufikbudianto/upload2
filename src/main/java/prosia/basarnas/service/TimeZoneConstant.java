/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author Aris
 */
public class TimeZoneConstant {
    public static final String KEY_GMT_PLUS_0700 = "G";
    public static final String KEY_GMT_PLUS_0800 = "H";
    public static final String KEY_GMT_PLUS_0900 = "I";
    public static final String KEY_GMT_PLUS_0000 = "Z";
    public static final Map<String, TimeZone> MAP = new LinkedHashMap<String, TimeZone>();

    static {
        MAP.put(KEY_GMT_PLUS_0700, TimeZone.getTimeZone("GMT+7:00"));
        MAP.put(KEY_GMT_PLUS_0800, TimeZone.getTimeZone("GMT+8:00"));
        MAP.put(KEY_GMT_PLUS_0900, TimeZone.getTimeZone("GMT+9:00"));
        MAP.put(KEY_GMT_PLUS_0000, TimeZone.getTimeZone("GMT+0:00"));
    }
}
