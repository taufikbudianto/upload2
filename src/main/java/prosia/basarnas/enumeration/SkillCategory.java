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

public enum SkillCategory {    
    Kualifikasi_Dasar("Kualifikasi Dasar", 0),
    Kualifikasi_Lanjutan("Kualifikasi Lanjutan", 1),
    Kualifikasi_Spesialis("Kualifikasi Spesialis", 2),
    Kualifikasi_Manajerial("Kualifikasi Manajerial", 3);
    
    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private SkillCategory(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }
}



