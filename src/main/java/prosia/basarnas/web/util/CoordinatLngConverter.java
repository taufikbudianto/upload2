/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Ismail
 */
@FacesConverter(value = "coordinatLngConverter")
public class CoordinatLngConverter implements Converter {

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
        String string = (String) o;
        return c.getConvertDdToDms(string, Boolean.FALSE);
    }

}
