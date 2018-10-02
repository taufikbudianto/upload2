/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.enumeration;

import java.math.BigInteger;
import lombok.Getter;

/**
 *
 * @author PROSIA
 */
public enum AssetKategori {
    Sarana("Sarana", 0),
    Prasarana("Prasarana", 1),
    Peralatan("Peralatan", 2),
    Kelengkapan("Kelengkapan", 3);
    
    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private AssetKategori(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
