/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.util.Random;
import prosia.basarnas.web.controller.map.googleapi.Disposer;
import prosia.basarnas.web.controller.map.googleapi.JavaScriptOverlay;

/**
 *
 * @author Aris
 */
public abstract class GPoly <A> implements JavaScriptOverlay, DescriptionConstant, JavaScriptCollection, JSONInterface, ConcurrentInterface {

    
    public final static String DESC_PARRENT_ID = "ParrentID";
   /* 
    public GPoly(Browser browser) {
        this.browser = browser;
    }
*/
    public GPoly() {
    }

    //protected Browser browser;
    
    
    /**
     * property yang mendefinisikan ID dari Class ini berdasarkan TYPE Search Patternnya 
     * yang merupakan reference(penampung) dari property <code>ID</code>(SubClass of GSearchPattern) yang bersifat
     * Auto - Incerement
     */
    protected int ID;
    
    
    /**
     * Convert orginal description of search pattern to description for display 
     * to text area
     */
    public static String descriptrionForDisplay(String description){
        /*
        if(description == null || description.equals(CommonConstant.EMPTY_STRING)){
            return null;
        }  
        description = description.replaceAll(Serializable.toStringRegex(DESC_OPEN), CommonConstant.EMPTY_STRING);
        description = description.replaceAll(Serializable.toStringRegex(DESC_CLOSE), CommonConstant.EMPTY_STRING);
        description = description.replaceAll(Serializable.toStringRegex(DESC_FIELD_SEPARATOR), CommonConstant.LINE_SEPARATOR); 
        */
        return description;
    }
    
    public static String generateParrentID(){
        return "" + new Random().nextDouble() + "";
    }

    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    protected void finalize() throws Throwable {
        Disposer.disposeObject(this);
        super.finalize();
    }
}
