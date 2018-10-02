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
public enum UsageTypeELT {

    BALING_BALING_1("Baling-baling, Mesin Tunggal", 1),
    BALING_BALING_2("Baling-baling, Multi Mesin", 2),
    JET_1("Jet, Mesin Tunggal", 3),
    JET_2("Jet, Multi Mesin", 4),
    HELIKOPTER("Helikopter", 5),
    LAIN("Lain", 6);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private UsageTypeELT(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
