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
import prosia.basarnas.web.model.CalculationEntity;

/**
 *
 * @author PROSIA
 */
@ManagedBean
@ViewScoped
public @Data
class CalculationResultMBean implements Serializable {
    private List<CalculationEntity> calculationEntity;
    
    public CalculationResultMBean() {
        calculationEntity = new ArrayList<>();
        setCalculation();
    }
    
    private void setCalculation() {
        CalculationEntity ce = new CalculationEntity();
        ce.setSru("S01");
        ce.setSpeed("45");
        ce.setTrackSpacing("0.25");
        ce.setDuration("18");
        ce.setSearchArea("32 nm2");
        calculationEntity.add(ce);
        ce = new CalculationEntity();
        ce.setSru("K90");
        ce.setSpeed("25");
        ce.setTrackSpacing("2.25");
        ce.setDuration("20");
        ce.setSearchArea("22 nm2");
        calculationEntity.add(ce);
    }
    
}
