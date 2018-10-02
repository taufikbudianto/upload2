/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author PROSIA
 */
@FacesConverter(value = "objectListConverter")
public class ObjectListConverter implements Converter{
    
    private static Map<Object,String> entities = new WeakHashMap<Object, String>();
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) throws ConverterException {
      for(Map.Entry<Object, String> entry :entities.entrySet()){
          if(entry.getValue().equals(string)){
              return entry.getKey();
          }
      }
      return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) throws ConverterException {
        synchronized(entities){
            if(!entities.containsKey(o)){
                String uuid = UUID.randomUUID().toString();
                entities.put(o, uuid);
                return uuid;
            }else{
                return entities.get(o);
            }
        }
    }
    
}
