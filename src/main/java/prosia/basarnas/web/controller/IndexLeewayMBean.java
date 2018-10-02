/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.model.SelectItem;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.DriftCalcWorksheet1;
import prosia.basarnas.model.IndexLeeway;
import prosia.basarnas.repo.IncidentLeewayRepo;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class IndexLeewayMBean implements InitializingBean {

    private LazyDataModelJPA<IndexLeeway> lazyDataModelJPA;
    private IndexLeeway indexLeeway;
    private List<IndexLeeway> listLeewayModel;
    private Boolean showButton;
    private Boolean disabled;
    private String indexLeewayID;
    private String category;
    private String subCategory;
    private String leewayDescription;
    private double multiplier;
    private double modifier;
    private double angle;
    private List<String> listCategory;
    private List<String> listSubCategory;
    private List<String> listDescription;
    private List<String> listMultiplier;
    private List<SelectItem> listItemCategory;
    private List<SelectItem> listItemSubCategory;
    private List<SelectItem> listItemDescription;
    private DriftCalcWorksheet1 driftCalcWorksheet1;
    private DriftCalcWorksheet1MBean driftCalcWorksheet1MBean;
    
    @Autowired
    private IncidentLeewayRepo incidentLeewayRepo;

    public IndexLeewayMBean() {
        //driftCalcWorksheet1 = new DriftCalcWorksheet1();
        driftCalcWorksheet1MBean = new DriftCalcWorksheet1MBean();
    }

    public void batal() {

    }

    public void changeCategory() {
        if (category.equals("")) {
            category = null;
        } else {
            listSubCategory = incidentLeewayRepo.findAllBySubCategory(category);
        }
        leewayDescription = null;
        multiplier = 0;
        modifier = 0;
        angle = 0;
    }

    public List<SelectItem> getListCategori(){
        if (listCategory == null) {
            listItemCategory = new ArrayList<>();
            listCategory = incidentLeewayRepo.findAllByCategory();
            listItemCategory.add(new SelectItem(""));
            for (String lst : listCategory) {
                listItemCategory.add(new SelectItem(lst));
            }
        }
        return listItemCategory;
    }
    
    public void changeSubCategory() {
        if (subCategory.equals("")) {
            subCategory = null;
        }else{
            listDescription = incidentLeewayRepo.findAllByLeewayDescription(subCategory);
        }
    }

    public List<SelectItem> getListSubCategori() {

        if (listSubCategory != null) {
            listItemSubCategory = new ArrayList<>();
            listSubCategory = incidentLeewayRepo.findAllBySubCategory(category);
            listItemSubCategory.add(new SelectItem(""));
            for (String lst : listSubCategory) {
                listItemSubCategory.add(new SelectItem(lst));
            }
        }

        return listItemSubCategory;
    }

    public void changeDescription(){
        if(leewayDescription.equals("")){
            leewayDescription = null;
        }else{
            indexLeewayID = incidentLeewayRepo.findAllByIndexLeewayID(category,subCategory,leewayDescription);
            IndexLeeway idxLeeway = incidentLeewayRepo.findOne(indexLeewayID);
            multiplier = idxLeeway.getMultiplier();
            modifier = idxLeeway.getModifier();
            angle = idxLeeway.getAngle();
            
            //copikan ke Workseheet1MBean
            //driftCalcWorksheet1MBean.setIndexLeewayID(idxLeeway.getIndexLeewayID());
        }
    }
    
    public List<SelectItem> getListDescription() {

        if (listDescription != null) {
            listItemDescription = new ArrayList<>();
            listDescription = incidentLeewayRepo.findAllByLeewayDescription(subCategory);
            listItemDescription.add(new SelectItem(""));
            for (String lst : listDescription) {
                listItemDescription.add(new SelectItem(lst));
            }
        }

        return listItemDescription;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //nilai pertama yg di tampilkan sesuai dengan sarcore lama
        //listItemCategory.add(new SelectItem(""));
        //listCategory = incidentLeewayRepo.findAllByCategory();
       
        lazyDataModelJPA = new LazyDataModelJPA(incidentLeewayRepo) {
            @Override
            protected Page getDatas(PageRequest request) {
                return incidentLeewayRepo.findAll(request);
            }
        };
    }

}
