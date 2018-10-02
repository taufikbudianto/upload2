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
public enum Sail {

    YACH("Yach", 1),
    SCHOONER("Schooner", 2),
    SEKOCI("Sekoci", 3),
    LAIN("Lain", 4);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private Sail(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
