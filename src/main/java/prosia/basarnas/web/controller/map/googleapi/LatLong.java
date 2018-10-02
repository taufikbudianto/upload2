/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

/**
 *
 * @author PROSIA
 */

import java.io.Serializable;

public class LatLong implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double latitude;
    private Double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLong() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString() {
        return "("+ latitude + "," + longitude+ ")";
    }
    
    @Override
    protected void finalize() throws Throwable {
        Disposer.disposeObject(this);
        super.finalize();
    }
}
