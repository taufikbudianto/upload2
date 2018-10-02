/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.primefaces.json.JSONObject;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Ismail
 */
@FacesConverter(value = "jsonToStrMapConverter")
public class JsonToStrMapConverter implements Converter {

    private Coordinate c = new Coordinate();

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) throws ConverterException {
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) throws ConverterException {
        if (o == null || ((String) o).isEmpty()) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        JSONObject json = new JSONObject((String) o);
        if (json.has("Start")) {
            JSONObject start = json.getJSONObject("Start");
            sb.append("Start : (").append(start.getDouble("lat")).append(", ").append(start.getDouble("lng")).append(")</br>");
        }
        if (json.has("Search_Radius")) {
            sb.append("Search_Radius : ").append(json.getDouble("Search_Radius")).append("</br>");
        }
        if (json.has("Heading")) {
            sb.append("Heading : ").append(json.getDouble("Heading")).append("</br>");
        }
        if (json.has("Search_Leg")) {
            sb.append("Search_Leg : ").append(json.getDouble("Search_Leg")).append("</br>");
        }
        if (json.has("Width")) {
            sb.append("Width : ").append(json.getDouble("Width")).append("</br>");
        }
        if (json.has("Track_Spacing")) {
            sb.append("Track_Spacing : ").append(json.getDouble("Track_Spacing")).append("</br>");
        }
        if (json.has("Min_Search_Leg")) {
            sb.append("Min_Search_Leg : ").append(json.getDouble("Min_Search_Leg")).append("</br>");
        }
        if (json.has("Max_Search_Leg")) {
            sb.append("Max_Search_Leg : ").append(json.getDouble("Max_Search_Leg")).append("</br>");
        }
        if (json.has("Pivot")) {
            JSONObject pivot = json.getJSONObject("Pivot");
            sb.append("Pivot : (").append(pivot.getDouble("lat")).append(", ").append(pivot.getDouble("lng")).append(")</br>");
        }
        if (json.has("Hex_Color")) {
            sb.append("Hex_Color : ").append(json.getString("Hex_Color")).append("</br>");
        }
        return sb.toString();
    }

}
