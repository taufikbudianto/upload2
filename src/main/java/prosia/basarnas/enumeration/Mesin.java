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
public enum Mesin {

    KAPAL_BARANG("Kapal Barang", 1),
    KAPAL_PENUMPANG("Kapal Penumpang", 2),
    TANKER("Tanker", 3),
    KAPAL_IKAN("Kapal Ikan", 4),
    KAPAL_TUNDA("Kapal Tunda", 5),
    LAIN("Lain", 6);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private Mesin(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
