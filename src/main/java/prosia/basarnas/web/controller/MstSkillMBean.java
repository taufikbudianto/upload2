/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstSkill;
import prosia.basarnas.repo.MstSkillRepo;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstSkillMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstSkill skill;

    private LazyDataModelJPA<MstSkill> lazyDataModelJPA;
    @Autowired
    private MstSkillRepo mstSkillRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;

    private String field;
    private String value;

    private String skillname;
    private BigInteger category;

    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean disabled;

    private Boolean showCombo;
    private Boolean isReadOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstSkillRepo) {
            @Override
            protected long getDataSize() {
                return mstSkillRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstSkillRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public MstSkillMBean() {
        skill = new MstSkill();
        skillname = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        isReadOnly = true;
    }

    public String formatskillId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String skillId = "";

        List<MstSkill> lis = mstSkillRepo.findAllBySkillidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            skillId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxId = mstSkillRepo.findSkillByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        break;
                    case 2:
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        nextval = "" + next;
                        break;
                    default:
                        break;
                }
                skillId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                skillId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return skillId;
    }

    private Specification<MstSkill> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstSkill>() {

            @Override
            public Predicate toPredicate(Root<MstSkill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "category":
                            predicates.add(cb.equal(root.<Integer>get(field), value));
                            break;

                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                    getLikePattern(value.trim())));
                            break;
                    }
                }

                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
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
        MstSkill namecategory = mstSkillRepo.findTopOneMstSkillBySkillnameAndCategory(skill.getSkillname(), skill.getCategory());

        if (namecategory != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Nama Kualifikasi dan Kategori Kualifikasi Sudah Ada ");
            return;
        } else {
            if (skill.getSkillid() == null) {
                skill.setSkillid(formatskillId());
            }
            //System.out.println("skill :" + skill.getKategori().getValue());
            skill.setCategory(skill.getCategory());
            skill.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            skill.setUsersiteid(ProsiaConstant.SITES);
            skill.setDatecreated(new Date());
            skill.setIsdeleted(BigInteger.ZERO);
            mstSkillRepo.save(skill);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil d isimpan" : "Negara berhasil diubah");
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Kualifikasi berhasil disimpan");
            refresh();
            RequestContext.getCurrentInstance().execute("PF('widgetKualifikasi').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetKualifikasiInput').hide()");
        }
    }

public void add() {
        isReadOnly = true;
        showButton = true;
        refresh();
        RequestContext.getCurrentInstance().reset("idKualifikasiInput");
        RequestContext.getCurrentInstance().update("idKualifikasiInput");
        RequestContext.getCurrentInstance().execute("PF('widgetKualifikasiInput').show()");
    }

    public void viewKualifikasi(String skillid, Boolean edited) {
        isReadOnly = false;
        this.skill = mstSkillRepo.findOne(skillid);
        skillname = skill.getSkillname();
        skill.getCategory();
        logger.debug("skill ID : {}", skill.getSkillid());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("idKualifikasi");
        RequestContext.getCurrentInstance().update("idKualifikasi");
        RequestContext.getCurrentInstance().execute("PF('widgetKualifikasi').show()");
    }

    public void batal() {
        refresh();
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        skill = new MstSkill();
        skillname = new String();
    }

    public void hapus(MstSkill i) {
        logger.debug("Unit : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstSkillRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Kualifikasi berhasil dihapus");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void chengeToCombo() {
        value = "";
        showCombo = field.equals("category");
    }

    @Override
        protected List<SecureItem> getSecureItems() {
        return null;
    }
}
