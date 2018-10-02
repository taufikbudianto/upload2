/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import lombok.Data;

/**
 *
 * @author Aris
 */
public @Data class EmptyCoordinate {
    private Double latitude;
    private Double t;

    public EmptyCoordinate(Double latitude, Double t) {
        this.latitude = latitude;
        this.t = t;
    }

    public EmptyCoordinate() {
    }
}
