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
public interface JavaScriptOverlay {
    /**
     * @return mengembalikan String script yang akan dipassing menjadi parameter <code>Main.mapForm.exec()</code>
     * fungsinya untuk menambah object keMap
     */
    public String writeScript();


    /**
     * @return mengembalikan String script yang akan dipassing menjadi parameter <code>Main.mapForm.exec()</code>
     * fungsinya untuk menghapus object dari Map
     */
    public String removeScript();
    
}
