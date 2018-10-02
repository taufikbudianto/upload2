/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.util.ArrayList;
import java.util.List;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.service.CommonConstant;
import prosia.basarnas.service.map_js.GSearchPattern;
import prosia.basarnas.service.map_js.Serializable;

/**
 *
 * @author Aris
 */
public class FreeDefinePattern extends GSearchPattern {
    public final static String CURRENT_FREE_DEFINE_SCRIPT = "current_SearchArea.polygon";
    
    
    private static String DESC_START = "Start";
    private static String DESC_PIVOT = "Pivot";
    
    public FreeDefinePattern(String name, GLatLng start, GLatLng pivot, Object browser, String parrentID) {
        super(name, browser, parrentID);
        this.l = new ArrayList();
        this.l.add(start);
        ID = ++AUTO_INCEREMENT;
        this.startPosition = start;
        this.pivot = pivot;
        setType(GSearchPattern.FREE_DEFINE);
        //setIcon(NavigatorIconCollection.FREE_DEFINE_ICON_32);
    }
    
    
    
    public FreeDefinePattern(String name, GLatLng start, GLatLng pivot, Object browser, List<GLatLng> gLatLngs, String parrentID) {
        super(name, browser, parrentID);
        ID = ++AUTO_INCEREMENT;
        this.startPosition = start;
        this.pivot = pivot;
        this.l = gLatLngs;
        setType(GSearchPattern.FREE_DEFINE);
        //setIcon(NavigatorIconCollection.FREE_DEFINE_ICON_32);
    }
    
    public FreeDefinePattern(String name, GLatLng start, Object browser, String patternID) {
        super(name, browser, patternID);
        ID = ++AUTO_INCEREMENT;
        this.startPosition = start;
        setType(GSearchPattern.FREE_DEFINE);
        //setIcon(NavigatorIconCollection.FREE_DEFINE_ICON_32);
    }

    public String addVertexScript(GLatLng latlng){
        this.l.add(latlng);
        return variableScript() + ".add("+ latlng.toJavaScript() +")";
    }
    
    

    @Override
    public String writeScript() {
        StringBuilder result = new StringBuilder("createFreeDefine(").append(this.startPosition.toJavaScript()).append(", ").append(ID);
        if(!this.l.isEmpty()){
            result.append(", ").append(JavaScriptBase.<GLatLng>toArrayJavaScript(l));
        }
        if(this.pivot != null){
            result.append(", ").append(this.pivot.toJavaScript());
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public void updatePropertyWhenSwicthing() {
        this.getStartPosition();
        this.getPivot();
        this.getL();
    }
    
    
    public static String setCurrentFreeDefineScript(FreeDefinePattern fds){
        if(fds == null){
            return "setCurrentFreeDefine(null)";
        }else{
            return "setCurrentFreeDefine(SEARCH_PATTERN["+fds.getID()+"])";
        }
    }
    
    
    
    
    @Override
    public String removeScript() {
        return variableScript() + ".remove()";
    }


    public static String currentFreeDefinePatternScript(Object freeDefine){
        return "setCurrentFreeDefine("+ null +")";
    }
    
    @Override
    public String createDescription() {
            this.getPivot();
            this.getStartPosition();
            return new StringBuilder(DESC_OPEN)
                    .append(DESC_START).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                    .append(DESC_BEGIN_VALUE_COUPLE).append(startPosition.getLat()).append(",")
                    .append(startPosition.getLng()).append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR)
                    .append(DESC_PIVOT).append(" ").append(DESC_VALUE_SEPARATOR).append(" ")
                    .append(DESC_BEGIN_VALUE_COUPLE).append(pivot.getLat()).append(",").append(pivot.getLng())
                    .append(DESC_END_VALUE_COUPLE).append(DESC_FIELD_SEPARATOR).append(DESC_PARRENT_ID)
                    .append(" ").append(DESC_VALUE_SEPARATOR).append(" ").append(this.getParrentID()).append(
                    DESC_CLOSE).toString();
    }

 public static FreeDefinePattern createGSearchPatternFromSearchPatern(IncidentSearchPattern pattern, List<GLatLng> gLatLngs, Object browser){
        String description = pattern.getDescription(); 
        if(description == null || description.equals(CommonConstant.EMPTY_STRING)) return null;
        description = description.substring(1, description.length()-1);
        description = description.replaceAll(CommonConstant.ONE_SPACE_STRING, CommonConstant.EMPTY_STRING);
        String[] spliteField = description.split(Serializable.toStringRegex(DESC_FIELD_SEPARATOR));
        String sStart = spliteField[0].replaceAll(DESC_START + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String sPivot = spliteField[1].replaceAll(DESC_PIVOT + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String parrentID = spliteField[1].replaceAll(DESC_PARRENT_ID + DESC_VALUE_SEPARATOR, CommonConstant.EMPTY_STRING);
        String name = pattern.getName();
        GLatLng start = GLatLng.toJava(sStart);
        GLatLng pivot = GLatLng.toJava(sPivot);
        FreeDefinePattern fdPattern = new FreeDefinePattern(name, start, pivot, browser, gLatLngs, parrentID);;
        fdPattern.setSearchPatternID(pattern.getSearchPatternId());
        return  fdPattern;
    }
    

    @Override
    public GLatLng getPivot() {
        GLatLng result = GLatLng.toJava("toString(SEARCH_PATTERN["+ ID +"].pivot)");
        setPivot(result); 
        return result;      
    }

    @Override
    public String toJSON() {
        String result = "{start:"+ this.startPosition.toJavaScript() +",id:"+ ID;
        if(!this.l.isEmpty()){
            result += ",vertexs:" + JavaScriptBase.<GLatLng>toArrayJavaScript(l);
        }
        if(this.pivot != null){
            result += ",pivot:" + this.pivot.toJavaScript();
        }
        result += "}";
        return result;
    }

    @Override
    public String createConcurrent() {
        return "{object:'"+ toJSON() +"', gen:generateFreeDefinePattern}";
    }

    @Override
    public String removeConcurrent() {
        return "{id:"+ ID +", gen:removeConcurrentFreeDefinePattern}";
    }
}
