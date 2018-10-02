/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import lombok.Data;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author Ismail
 */
@Data
public class MonteCarlo {

    private LatLng datum;
    private Double radius;
    private Double seaCurrentAngle;
    private Double seaCurrentSpeed;
    private Double surfaceWindAngle;
    private Double windCurrentSpeed;
}
