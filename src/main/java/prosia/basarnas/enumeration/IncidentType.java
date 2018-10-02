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
public enum IncidentType {
    
    PELAYARAN("Kecelakaan Laut", 1),
    PENERBANGAN("Kecelakaan Udara", 2),
    LAINLAIN("Musibah [Lain-Lain]", 3),
    BENCANA("Bencana", 4);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private IncidentType(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
