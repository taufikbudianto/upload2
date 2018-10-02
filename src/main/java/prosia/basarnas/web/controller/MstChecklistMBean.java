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
import prosia.basarnas.model.MstChecklist;
import prosia.basarnas.model.MstChecklistdetail;
import prosia.basarnas.repo.MstChecklistRepo;
import prosia.basarnas.repo.MstChecklistdetailRepo;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstChecklistMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstChecklist checklist;
    private MstChecklistdetail checklistdetail;

    private LazyDataModelJPA<MstChecklist> lazyDataModelJPA;
    private LazyDataModelJPA<MstChecklistdetail> lazyDataModelDetailJPA;

    @Autowired
    private MstChecklistRepo mstChecklistRepo;
    @Autowired
    private MstChecklistdetailRepo checklistdetailRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;

    private String field;
    private String value;

    private String name;
    private String checklistid;

    private String checklistdetailid;
    private String category;

    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean showButtonDetail;
    private Boolean disabled;
    private Boolean isReadOnly;
    private Boolean isReadOnlyDetail;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstChecklistRepo) {
            @Override
            protected long getDataSize() {
                return mstChecklistRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstChecklistRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public void detail(String checklistId) {
        lazyDataModelDetailJPA = new LazyDataModelJPA(checklistdetailRepo) {
            @Override
            protected long getDataSize() {
                return checklistdetailRepo.count(whereQueryDetail(checklistId));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return checklistdetailRepo.findAll(whereQueryDetail(checklistId), request);
            }
        };
    }

    public MstChecklistMBean() {
        checklist = new MstChecklist();
        checklistdetail = new MstChecklistdetail();
        name = new String();
        category = new String();
        showButton = true;
        showButtonDetail = true;
        disabled = false;
        showButtonInput = true;
        isReadOnly = true;
        isReadOnlyDetail = true;
    }

    public String formatcheckId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String checkId = "";

        List<MstChecklist> lis = mstChecklistRepo.findAllByChecklistidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            checkId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxId = mstChecklistRepo.findCheckByMaxId("CGK");
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
                checkId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                checkId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return checkId;
    }

    public String formatcheckdetailId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String checkdetailId = "";

        List<MstChecklistdetail> lis = checklistdetailRepo.findAllByChecklistdetailidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            checkdetailId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxdetailId = checklistdetailRepo.findCheckdetailByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxdetailId.split("-");
            if (maxdetailId.contains(splitDate[0] + splitDate[1])) {
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
                checkdetailId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                checkdetailId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return checkdetailId;
    }

    private Specification<MstChecklist> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstChecklist>() {

            @Override
            public Predicate toPredicate(Root<MstChecklist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(cb.like(cb.lower(root.<String>get(field)),
                            getLikePattern(value.trim())));
                }
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<MstChecklistdetail> whereQueryDetail(final String detailId) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstChecklistdetail>() {

            @Override
            public Predicate toPredicate(Root<MstChecklistdetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<MstChecklist>get("checklistid").<String>get("checklistid"), detailId));
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
        MstChecklist name = mstChecklistRepo.findTopOneMstMstChecklistByName(checklist.getName());

        if (name != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Nama Checklist Sudah Ada ");
            return;
        } else {
            if (checklist.getChecklistid() == null) {
                checklist.setChecklistid(formatcheckId());
            }
            checklist.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            checklist.setUsersiteid(ProsiaConstant.SITES);
            checklist.setDatecreated(new Date());
            checklist.setIsdeleted(BigInteger.ZERO);
            mstChecklistRepo.save(checklist);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Data Checklist berhasil disimpan");
            refresh();
            RequestContext.getCurrentInstance().execute("PF('widgetChecklist').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetChecklistInput').hide()");
        }
    }

    public void simpandetail() {
        MstChecklistdetail categorydesc = checklistdetailRepo.findTopOneMstChecklistdetailByCategoryAndDescription(checklistdetail.getCategory(), checklistdetail.getDescription());

        if (categorydesc != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Kategori dan Deskripsi Checklist Detail Sudah Ada ");
            return;
        } else {
            System.out.println("checkdetailid = " + formatcheckdetailId());

            if (checklistdetail.getChecklistdetailid() == null) {
                checklistdetail.setChecklistdetailid(formatcheckdetailId());
            }

            checklistdetail.setUsersiteid("CGK");
            checklistdetail.setDatecreated(new Date());
            checklistdetail.setChecklistid(checklist);
            //checklistdetail.setChecklistid(checklist.getChecklistid());
            //checklist.setIsdeleted(BigInteger.ZERO);
            checklistdetailRepo.save(checklistdetail);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Data Checklist Detail berhasil disimpan");
            refreshdetail();
            RequestContext.getCurrentInstance().execute("PF('widgetChecklistDetail').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetChecklistDetailInput').hide()");
        }
    }

    public void add() {
        isReadOnly = true;
        showButton = true;
        refresh();
        RequestContext.getCurrentInstance().reset("idChecklistInput");
        RequestContext.getCurrentInstance().update("idChecklistInput");
        RequestContext.getCurrentInstance().execute("PF('widgetChecklistInput').show()");
    }

    public void viewCheck(String checklistid, Boolean edited) {
        isReadOnly = false;
        this.checklist = mstChecklistRepo.findOne(checklistid);
        name = checklist.getName();
        logger.debug("Checklist ID : {}", checklist.getChecklistid());
        detail(checklist.getChecklistid());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("idChecklist");
        RequestContext.getCurrentInstance().update("idChecklist");
        RequestContext.getCurrentInstance().execute("PF('widgetChecklist').show()");
    }

    public void resetDetail() {
        showButton = true;
        isReadOnlyDetail = true;
        refreshdetail();
        RequestContext.getCurrentInstance().reset("idChecklistDetailInput");
        RequestContext.getCurrentInstance().update("idChecklistDetailInput");
        RequestContext.getCurrentInstance().execute("PF('widgetChecklistDetailInput').show()");
    }

    public void viewDetail(String checklistdetailid) {
        isReadOnlyDetail = false;
        System.out.println("sccc");
        this.checklistdetail = checklistdetailRepo.findOne(checklistdetailid);
        logger.debug("Checklist ID : {}", checklist.getChecklistid());
        //detail(checklistdetail.getChecklistdetailid());

        RequestContext.getCurrentInstance().reset("idChecklistDetail");
        RequestContext.getCurrentInstance().update("idChecklistDetail");
        RequestContext.getCurrentInstance().execute("PF('widgetChecklistDetail').show()");
    }

    public void batal() {
        refresh();
        //RequestContext.getCurrentInstance().update(":idChecklist");
    }

    public void batalDetail() {
        refreshdetail();
        //RequestContext.getCurrentInstance().update(":idChecklist");
    }

    public void refresh() {
        checklist = new MstChecklist();
        name = new String();
    }

    public void refreshdetail() {
        checklistdetail = new MstChecklistdetail();
    }

    public void hapus(MstChecklist i) {
        logger.debug("Unit : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstChecklistRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Data Checklist berhasil dihapus");
    }

    public void hapusDetail(String id) {
        System.out.println("checklistdetailid adalah " + id);
        //checklistdetailRepo.deleteByChecklistdetailid(checklistdetail.getChecklistdetailid());
        checklistdetailRepo.delete(id);
//        i.setIsdeleted(BigInteger.ONE);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Data Checklist Detail berhasil disimpan");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getfindByChecklistdetailid(String checklistdetailid) {
        System.out.println("id= " + checklistdetailRepo.findOne(checklistdetailid).getChecklistdetailid());

        return checklistdetailRepo.findOne(checklistdetailid).getChecklistdetailid();

    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
