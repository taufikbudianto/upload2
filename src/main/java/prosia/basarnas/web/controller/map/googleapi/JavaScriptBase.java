/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author PROSIA
 */
public abstract class JavaScriptBase<A>  {
    public abstract String toJavaScript();

   /**
    * @param list List yang akan diconvert menjadi String Array
    * @param <X> type Object yang terkandung pada List harus turunan dari Class <code>JavaScriptObject</code>
    * @return String dengan format diawali dengan "[" dan diakhiri dengan "]" jika list tidak kosong
    * namun jika kosong maka yang akan dikembalikan adalah null String 
    * 
    * methode ini berbeda dengan Serializable.toArrayJavaScript(Collection[] collection)
    * yang tetap mengembalikan [] jika array jika collection kosong dan mengembalikan null jika collection kosong
    */
    public static <X extends JavaScriptBase> String toArrayJavaScript(List<X> list){
       if(list == null || list.isEmpty()) return "null";
       StringBuilder result = new StringBuilder("[");
       for(JavaScriptBase x : list){
           result.append(x.toJavaScript()).append(",");
       }
       String str = result.substring(0, result.length()-1) + "]";
       return str;
  }

    @Override
    protected void finalize() throws Throwable {
        Disposer.disposeObject(this);
        super.finalize();
    }
}
