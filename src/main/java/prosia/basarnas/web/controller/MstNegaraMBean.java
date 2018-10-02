/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.MstNegara;
import prosia.basarnas.repo.MstNegaraRepo;
import prosia.basarnas.constant.ProsiaConstant;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstNegaraMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstNegara negara;

    private LazyDataModelJPA<MstNegara> lazyDataModelJPA;
    @Autowired
    private MstNegaraRepo mstNegaraRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    private String field;
    private String value;
    private String isdel;

    private String countryID;
    private String countryname;
    private String mccid;
    private String rccid;
    private String rccphone;
    private String rccfax;

    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean isReadOnly;
    private Boolean disabled;
    private Boolean disabled1;

    public MstNegaraMBean() {
        negara = new MstNegara();
        countryID = new String();
        countryname = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        disabled1 = true;
        isReadOnly = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstNegaraRepo) {
            @Override
            protected long getDataSize() {
                return mstNegaraRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstNegaraRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    private Specification<MstNegara> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<MstNegara>() {

            @Override
            public Predicate toPredicate(Root<MstNegara> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(
                            cb.like(cb.lower(root.<String>get(field)), getLikePattern(value.trim()))
                    );
                }
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
//                query.orderBy(cb.desc(root.get("dateCreated")));
                return andTogether(predicates, cb);
            }

        };

    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void simpan() {
        if (isReadOnly == true) {
            MstNegara id = mstNegaraRepo.findTopOneMstNegaraByCountryID(negara.getCountryID());
            if (id != null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN,
                        "Peringatan",
                        "ID Negara Sudah Ada ");
                return;
            } else {
                negara.setIsdeleted(BigInteger.ZERO);
                negara.setCreatedBy(menuNavMBean.getUserSession().getUserId().toString());
                negara.setDateCreated(new Date());
                negara.setUserSiteID(ProsiaConstant.SITES);
                mstNegaraRepo.save(negara);

                //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
                refresh();
                RequestContext.getCurrentInstance().execute("PF('widgetNegaraInput').hide()");
                RequestContext.getCurrentInstance().execute("PF('widgetNegara').hide()");
//        addMessage("Sukses", "Negara berhasil disimpan");
                addPopUpMessage(FacesMessage.SEVERITY_INFO,
                        "Sukses", "Negara berhasil disimpan");
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Negara berhasil disimpan"));
            }
        } else {
            negara.setIsdeleted(BigInteger.ZERO);
            negara.setCreatedBy(menuNavMBean.getUserSession().getUserId().toString());
            negara.setDateCreated(new Date());
            negara.setUserSiteID(ProsiaConstant.SITES);
            mstNegaraRepo.save(negara);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
            refresh();
            RequestContext.getCurrentInstance().execute("PF('widgetNegaraInput').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetNegara').hide()");
//        addMessage("Sukses", "Negara berhasil disimpan");
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Negara berhasil disimpan");
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Negara berhasil disimpan"));
        }
    }

    public void viewNegara(String countryID, Boolean edited) {
        isReadOnly = false;
        this.negara = mstNegaraRepo.findOne(countryID);
        countryname = negara.getCountryName();
        mccid = negara.getMccID();
        rccid = negara.getRccID();
        rccphone = negara.getRccPhone();
        rccfax = negara.getRccFax();
        logger.debug("Country ID : {}", negara.getCountryID());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("idNegara");
        RequestContext.getCurrentInstance().update("idNegara");
        RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void batal() {
        refresh();
    }

    public void add() {
        isReadOnly = true;
        showButton = true;
        refresh();
        RequestContext.getCurrentInstance().execute("PF('widgetNegaraInput').show()");
        RequestContext.getCurrentInstance().reset("idNegaraInput");
        RequestContext.getCurrentInstance().update("idNegaraInput");

    }

    public void refresh() {
        negara = new MstNegara();
        countryID = new String();
        countryname = new String();
        mccid = new String();
        rccid = new String();
        rccphone = new String();
        rccfax = new String();
    }

    public void hapus(MstNegara i) {
        logger.debug("Negara : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstNegaraRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Negara berhasil dihapus");
        //addMessage("Sukses", "Negara berhasil dihapus");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
