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
public enum Jabatan {
//    Kualifikasi_Dasar(0), Kualifikasi_Lanjutan(1), Kualifikasi_Spesialis(2), Kualifikasi_Manajerial(3);

    Struktural("Struktural", 0),
    Fungsional("Fungsional", 1),
    Operational("Operational", 2),
    Siaga("Siaga", 3);
    
    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private Jabatan(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
