/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;


import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.JavaScriptOverlay;
import prosia.basarnas.service.map_js.Serializable;


/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public class MeasurePolyline implements JavaScriptOverlay {

    private static MeasurePolyline instance;
    private List<GLatLng> vertexs;
    
    public static MeasurePolyline create(){
        try {
//            String array = Main.mapForm.exec("getMeasureVertexs()");
//            return new MeasurePolyline(Serializable.toLatlngs("["+ array +"]"));
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public MeasurePolyline(List<GLatLng> vertexs) {
        this.vertexs = vertexs;
    }
    
    
    
    public static MeasurePolyline getInstance() {
        return instance;
    }

    public static void setInstance(MeasurePolyline instance) {
        MeasurePolyline.instance = instance;
    }

    public List<GLatLng> getVertexs() {
        return vertexs;
    }

    public void setVertexs(List<GLatLng> vertexs) {
        this.vertexs = vertexs;
    }

    
    
    
    
    @Override
    public String writeScript() {
        return "createMeasurePolyline("+ GLatLng.toArrayJavaScript(vertexs) +")";
    }

    @Override
    public String removeScript() {
        return "removeMeasure()";
    }
}
