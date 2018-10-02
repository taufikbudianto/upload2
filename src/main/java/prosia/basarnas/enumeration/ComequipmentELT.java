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
public enum ComequipmentELT {

    VHF("VHF"),
    MF("MF"),
    HF("HF"),
    LAIN("Lain");

    @Getter
    private final String item;

    private ComequipmentELT(String item) {
        this.item = item;
    }
}
