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
public enum CategoryEPIRB {

    MANUAL("Manual", 1),
    OTOMATIS("Otomatis", 2);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private CategoryEPIRB(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
