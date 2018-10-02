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
public enum IncidentStatus {
    
    OPEN("Open"),
    CLOSE("Close");

    @Getter
    private final String item;

    private IncidentStatus(String item) {
        this.item = item;
    }
}
