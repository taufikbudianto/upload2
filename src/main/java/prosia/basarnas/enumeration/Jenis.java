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
public enum Jenis {
    PELAYARAN("Musibah Pelayaran"),
    PENERBANGAN("Musibah Penerbangan"),
    LAINLAIN("Musibah Lain-Lain"),
    BENCANA("Bencana");
    
    @Getter
    private String capitalize;

    private Jenis(String capitalize) {
        this.capitalize = capitalize;
    }
}
