/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.net.URL;
import java.util.Date;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class BmkgMBean implements InitializingBean{

    private String metrik;
    private String url12;
    private String url18;
    private String url24;
    private String url30;
    private String url36;
    private String url42;
    private String url48;
    private String url54;
    private String urlLegend;
    private boolean isMorningLabel;
    private int fecthImageBmkgSchedulerMorningHour = 5;
    private int fecthImageBmkgSchedulerNoonHour = 15;
    private int fecthImageBmkgSchedulerProcessingTime = 5;
    
    public BmkgMBean(){
        setBtnLabel();
        fecthImageBmkgSchedulerMorningHour = 5;
        fecthImageBmkgSchedulerNoonHour = 15;
        fecthImageBmkgSchedulerProcessingTime = 5;
    }
    
    public void setImage(){
        switch (urlLegend){
            case "1":
                //leg_wind.jpg
                urlLegend = "../../image/leg_wind.jpg";
                break;
            case "2":
                //leg_current.jpg
                urlLegend = "../../image/leg_current.jpg";
                break;
            case "3":
                //leg_max.jpg
                urlLegend = "../../image/leg_max.jpg";
                break;
            case "4":
                //leg_sig.jpg
                urlLegend = "../../image/leg_sig.jpg";
                break;
            case "5":
                //leg_swell.jpg
                urlLegend = "../../image/leg_swell.jpg";
                break;
        }
            
        
    }
    
    public void print(){
        
    }
    
    public void close(){
        
    }
    
    public void btnUrl12(){
        setBtnLabel();
    }
    
    public void btnUrl18(){
        setBtnLabel();
    }
    
    public void btnUrl24(){
        setBtnLabel();
    }
    
    public void btnUrl30(){
        setBtnLabel();
    }
    
    public void btnUrl36(){
        setBtnLabel();
    }
    
    public void btnUrl42(){
        setBtnLabel();
    }
    
    public void btnUrl48(){
        setBtnLabel();
    }
    
    public void btnUrl54(){
        setBtnLabel();
    }
    
    private void setBtnLabel() {
        Date curr = new Date();
        Date am5 = new Date();
        am5 = Tanggal.set(0, 5, 5, null, null, null, am5);
        Date pm3 = new Date();
        pm3 = Tanggal.set(0, 5, 15, null, null, null, pm3);
        Date tomm = Tanggal.addDayToDate(curr, 1);
        if (curr.after(am5) && curr.before(pm3)) {
            url12 = Tanggal.dateStringConvert(curr) + " | 00 GMT";
            url18 = Tanggal.dateStringConvert(curr) + " | 06 GMT";
            url24 = Tanggal.dateStringConvert(curr) + " | 12 GMT";
            url30 = Tanggal.dateStringConvert(curr) + " | 18 GMT";
            url36 = Tanggal.dateStringConvert(tomm) + " | 00 GMT";
            url42 = Tanggal.dateStringConvert(tomm) + " | 06 GMT";
            url48 = Tanggal.dateStringConvert(tomm) + " | 12 GMT";
            url54 = Tanggal.dateStringConvert(tomm) + " | 18 GMT";
        } else {
            Date afterTomm = Tanggal.addDayToDate(curr, 2);
            url12 = Tanggal.dateStringConvert(curr) + " | 12 GMT";
            url18 = Tanggal.dateStringConvert(curr) + " | 18 GMT";
            url24 = Tanggal.dateStringConvert(tomm) + " | 00 GMT";
            url30= Tanggal.dateStringConvert(tomm) + " | 06 GMT";
            url36 = Tanggal.dateStringConvert(tomm) + " | 12 GMT";
            url42 = Tanggal.dateStringConvert(tomm) + " | 18 GMT";
            url48 = Tanggal.dateStringConvert(afterTomm) + " | 00 GMT";
            url54 = Tanggal.dateStringConvert(afterTomm) + " | 06 GMT";
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        urlLegend = "../../image/leg_wind.jpg";
    }
    
}
