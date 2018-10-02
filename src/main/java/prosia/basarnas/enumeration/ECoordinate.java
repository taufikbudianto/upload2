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
public enum ECoordinate {
    Latitude(true),
    Longitude(false);

    @Getter
    private final boolean type;

    private ECoordinate(boolean type) {
        this.type = type;
    }
}
