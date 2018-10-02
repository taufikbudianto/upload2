/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.util.List;
import lombok.Data;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class MeasureMBean extends AbstractManagedBean implements InitializingBean {

//    public String getinstance() {
//        if (MeasurePolyline.getInstance()
//                != null) {
//            //Redraw measure
//            exec(MeasurePolyline.getInstance().writeScript());
//        }
//    }
    
    public synchronized String exec(String argument) {
        try {
//            String resultExec = getBrowserComponent().getBrowser().executeScript(argument);
//            return resultExec;
            return null;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
