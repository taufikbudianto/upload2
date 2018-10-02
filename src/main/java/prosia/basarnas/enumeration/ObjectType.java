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
public enum ObjectType {

    VESSEL("Kapal", 1),
    ORANG("Orang", 2),
    POLUSI("Polusi", 3),
    PESAWAT("Pesawat", 4),
    SINYAL("Sinyal", 5),
    KENDARAAN("Kendaraan", 6),
    LAIN("Lain-lain", 7);

    @Getter
    private final String itemLabel;
    @Getter
    private final Integer itemValue;

    private ObjectType(String itemLabel, Integer itemValue) {
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
    }

}
