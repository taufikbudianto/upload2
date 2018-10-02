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
public enum UsageTypePLB {
    
    TIDAK_ADA("Tidak Ada", 1),
    KENDARAAN_DARURAT("Kendaraan Darurat", 2),
    KAPAL("Kapal", 3),
    PESAWAT("Pesawat", 4),
    LAIN_LAIN("Lain-lain", 5);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private UsageTypePLB(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}
