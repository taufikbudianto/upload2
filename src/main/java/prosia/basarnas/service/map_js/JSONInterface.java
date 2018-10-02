/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

/**
 *
 * @author Aris
 */
public interface JSONInterface {
    /**
     * Mengubah Object Java kedalam bentuk JSON yang sangat diperlukan
     * saja. diperlukan untuk proess Mapping Google Map api dalam
     * mempercepat execute proses
     */
    public String toJSON();
}
