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
public enum DetailUsage {

    BERKEMAH("Berkemah", 1),
    BERBURU("Berburu", 2),
    MEMANCING("Memancing", 3),
    LAIN("Lain-lain", 4);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private DetailUsage(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
