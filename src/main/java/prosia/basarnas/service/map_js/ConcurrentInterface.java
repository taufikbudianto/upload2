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
public interface ConcurrentInterface {
    /**
     * this method as interface between java and java script side for creating a lot of overlays in
     * same time or concurrent
     */
    public String createConcurrent();
    
    /**
     * this method as interface between java and java script side for removing a lot of overlays in
     * same time or concurrent
     */
    public String removeConcurrent();
}
