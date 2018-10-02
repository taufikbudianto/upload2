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
public enum BeaconType {
    PLB("PLB"),
    ELT("ELT"),
    EPIRB("EPIRB");

    @Getter
    private final String item;

    private BeaconType(String item) {
        this.item = item;
    }
}
