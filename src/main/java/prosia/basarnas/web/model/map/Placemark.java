/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import lombok.Data;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author Irfan Rofie
 */
@Data
public class Placemark implements Serializable {

    public static String PLACEMARK_DEFAULT_ICON = "DEFAULT_PLACEMARK";
    private LatLng posisi;
    private String id;
    private String name;
    private String image;
    private String description;
    private Boolean initialState;
    /**
     * Propertise dari placemark saat pertama kali di load dari file akan di
     * simpan dalam properties ini. berfungsi jka placemark ingin diremove dari
     * file.
     */
    private String initialProperties;

    public Placemark() {
    }

    public Placemark(LatLng posisi, String id, String name, String image,
            String description, Boolean initialState, String initialProperties) {
        this.posisi = posisi;
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.initialState = initialState;
        this.initialProperties = initialProperties;
    }
}
