/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import lombok.Data;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Ismail
 */
@Data
public class ConstructData implements Serializable {

    private Double tilt;
    private GLatLng pivot;
    private double radius;
}
