/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Data;
import prosia.basarnas.web.model.RescuePlanningEntity;

/**
 *
 * @author Ismail
 */
@ManagedBean
@ViewScoped
public @Data class GeneralInfoMBean implements Serializable {

    private List<RescuePlanningEntity> planningEntitys;
    private Boolean isDetailProses;
    
    public GeneralInfoMBean() {
        planningEntitys = new ArrayList<>();
        setRescuePlanning();
    }

    private void setRescuePlanning() {
        RescuePlanningEntity rpe = new RescuePlanningEntity();
        rpe.setDatetime(new Date());
        rpe.setMetode("Drift Calculation");
        rpe.setCreateBy("Agung S");
        planningEntitys.add(rpe);
        rpe = new RescuePlanningEntity();
        rpe.setDatetime(new Date());
        rpe.setMetode("Drift Calculation");
        rpe.setCreateBy("Agung S");
        planningEntitys.add(rpe);
        rpe = new RescuePlanningEntity();
        rpe.setDatetime(new Date());
        rpe.setMetode("Drift Calculation");
        rpe.setCreateBy("Admin 1");
        planningEntitys.add(rpe);
    }
    
    public void showFormDriftCal(){
        isDetailProses = true;
    }
    
    public void hideFormDriftCal(){
        isDetailProses = false;
    }
}
