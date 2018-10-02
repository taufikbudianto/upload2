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
public enum SightingStatus {
    BELUM("Belum Diproses", 1),
    SEDANG("Sedang Diproses", 2),
    SELESAI("Selesai Diproses", 3);
    
    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private SightingStatus(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
