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
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.repo.MstEmployeeClassRepo;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstEmployeeClassMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstEMployeeClass employeeClass;
    private LazyDataModelJPA<MstEMployeeClass> lazyDataModelJPA;
    @Autowired
    private MstEmployeeClassRepo mstEmployeeClassRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;
    private String field;
    private String value;
    private String employmentclassid;
    private String employmentclasscode;
    private String employmentclassname;
    private Date twLaporan;
    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean disabled;
    private Boolean isReadOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstEmployeeClassRepo) {
            @Override
            protected long getDataSize() {
                return mstEmployeeClassRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstEmployeeClassRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public MstEmployeeClassMBean() {
        employeeClass = new MstEMployeeClass();
        employmentclassid = new String();
        employmentclassname = new String();
        employmentclasscode = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        isReadOnly = true;
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";
        //System.out.println("USER SITE ID : " +employeeClass.getUsersiteid());
        List<MstEMployeeClass> lis = mstEmployeeClassRepo.findAllByEmploymentclassidContaining("CGK");
        if (lis.isEmpty()) {
            System.out.println("A");
            classId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            System.out.println("BSSSS");
            String maxId = mstEmployeeClassRepo.findClassByMaxId("CGK");
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
                classId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                classId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return classId;
    }

    private Specification<MstEMployeeClass> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstEMployeeClass>() {

            @Override
            public Predicate toPredicate(Root<MstEMployeeClass> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(cb.like(cb.lower(root.<String>get(field)),
                            getLikePattern(value.trim())));
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
        MstEMployeeClass code = mstEmployeeClassRepo.findTopOneMstEMployeeClassByEmploymentclasscodeAndEmploymentclassname(employeeClass.getEmploymentclasscode(), employeeClass.getEmploymentclassname());

        if (code != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Kode Golongan dan Nama Golongan Sudah Ada ");
            return;
        } else {
            if (employeeClass.getEmploymentclassid() == null) {
                employeeClass.setEmploymentclassid(formatclassId());
            }
            employeeClass.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            employeeClass.setUsersiteid(ProsiaConstant.SITES);
            employeeClass.setDatecreated(new Date());
            employeeClass.setIsdeleted(BigInteger.ZERO);
            mstEmployeeClassRepo.save(employeeClass);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Golongan berhasil disimpan");
            refresh();
            RequestContext.getCurrentInstance().execute("PF('widgetGolongan').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetGolonganInput').hide()");
        }

    }

    public void add() {
        isReadOnly = true;
        showButton = true;
        refresh();
        RequestContext.getCurrentInstance().execute("PF('widgetGolonganInput').show()");
        RequestContext.getCurrentInstance().reset("idGolonganInput");
        RequestContext.getCurrentInstance().update("idGolonganInput");
    }

    public void viewGolongan(String employmentclassid, Boolean edited) {
        isReadOnly = false;
        this.employeeClass = mstEmployeeClassRepo.findOne(employmentclassid);
        employmentclassname = employeeClass.getEmploymentclassname();
        employmentclasscode = employeeClass.getEmploymentclasscode();

        logger.debug("POSSAR ID : {}", employeeClass.getEmploymentclassid());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);

        RequestContext.getCurrentInstance().reset("idGolongan");
        RequestContext.getCurrentInstance().update("idGolongan");
        RequestContext.getCurrentInstance().execute("PF('widgetGolongan').show()");
    }

    public void batal() {
        refresh();
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        employeeClass = new MstEMployeeClass();
        employmentclassname = new String();
        employmentclasscode = new String();

    }

    public void hapus(MstEMployeeClass i) {
        logger.debug("Golongan : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstEmployeeClassRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Golongan berhasil dihapus");
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
