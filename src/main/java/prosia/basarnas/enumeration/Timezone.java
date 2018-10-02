/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.enumeration;

import lombok.Getter;

/**
 *
 * @author PROSIA
 */
public enum Timezone {
    GMT_PLUS_7("G","GMT+07:00"),
    GMT_PLUS_8("H","GMT+08:00"),
    GMT_PLUS_9("I","GMT+09:00"),
    GMT_PLUS_0("Z","GMT+00:00");
    
    @Getter
    private String shorttimezone;
    
    @Getter
    private String timezone;

    private Timezone(String shorttimezone, String timezone) {
        this.shorttimezone = shorttimezone;
        this.timezone = timezone;
    }            
}
