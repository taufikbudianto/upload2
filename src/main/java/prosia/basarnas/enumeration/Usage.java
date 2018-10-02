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
public enum Usage {

    KOMERSIAL("Komersial", 1),
    NON_KOMERSIAL("Non Komersial", 2),
    MILITER("Militer", 3),
    PEMERINTAHAN("Pemerintahan", 4);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private Usage(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
