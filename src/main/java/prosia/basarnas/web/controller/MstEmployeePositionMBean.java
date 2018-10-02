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
import javax.faces.model.SelectItem;
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
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.service.EmployeePositionService;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstEmployeePositionMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstEmployeePosition employeePosition;
    private MstEmployeePosition employeeRes;

    private LazyDataModelJPA<MstEmployeePosition> lazyDataModelJPA;
    @Autowired
    private MstEmployeePositionRepo mstEmployeePositionRepo;
    @Autowired
    private EmployeePositionService EmployeePositionService;
    @Autowired
    private MenuNavMBean menuNavMBean;
    private String field;
    private String value;
    private String where;
    private Boolean showCombo;
    private Boolean showButtonInput;

    private String employmentpositionname;

    private List<SelectItem> listEmployeePosition;
    private Boolean showButton;
    private Boolean disabled;
    private Boolean isReadOnly;

    public void chengeToCombo() {
        value = "";
        showCombo = field.equals("category");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstEmployeePositionRepo) {
            private boolean showCombo;

            @Override
            protected long getDataSize() {
                return mstEmployeePositionRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstEmployeePositionRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public MstEmployeePositionMBean() {
        employeePosition = new MstEmployeePosition();
        employmentpositionname = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        isReadOnly = true;
    }

    public String formatpositionId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String unitId = "";

        List<MstEmployeePosition> lis = mstEmployeePositionRepo.findAllByEmploymentpositionidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            unitId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxId = mstEmployeePositionRepo.findPositionByMaxId("CGK");
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
                unitId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                unitId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return unitId;
    }

    private Specification<MstEmployeePosition> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstEmployeePosition>() {

            @Override
            public Predicate toPredicate(Root<MstEmployeePosition> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        MstEmployeePosition namecategory = mstEmployeePositionRepo.findTopOneMstEmployeePositionByEmploymentpositionnameAndCategory(employeePosition.getEmploymentpositionname(), employeePosition.getCategory());

        if (namecategory != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Nama Jabatan dan Kategori Jabatan Sudah Ada ");
            return;
        } else {
            employeePosition.setCategory(employeePosition.getCategory());
            if (employeePosition.getEmploymentpositionid() == null) {
                employeePosition.setEmploymentpositionid(formatpositionId());
            }
            employeePosition.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            employeePosition.setUsersiteid(ProsiaConstant.SITES);
            employeePosition.setDatecreated(new Date());
            MstEmployeePosition name = mstEmployeePositionRepo.findByEmploymentpositionname(employmentpositionname);
            System.out.println("jabatan id= " + employmentpositionname);
            if (!employmentpositionname.isEmpty()) {
                employeePosition.setResponsibleto(name.getEmploymentpositionid());
            }
            employeePosition.setIsdeleted(BigInteger.ZERO);
            mstEmployeePositionRepo.save(employeePosition);

            //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Jabatan berhasil disimpan");
            refresh();
            RequestContext.getCurrentInstance().execute("PF('widgetJabatan').hide()");
            RequestContext.getCurrentInstance().execute("PF('widgetJabatanInput').hide()");
        }
    }

public void add() {
        isReadOnly = true;
        showButton = true;
        refresh();
        RequestContext.getCurrentInstance().reset("idJabatanInput");
        RequestContext.getCurrentInstance().update("idJabatanInput");
        RequestContext.getCurrentInstance().execute("PF('widgetJabatanInput').show()");
    }

    public void viewJabatan(String employmentpositionid, String responsibleto, Boolean edited) {
        isReadOnly = false;
        this.employeePosition = mstEmployeePositionRepo.findOne(employmentpositionid);

        if (!responsibleto.isEmpty()) {
            this.employeeRes = mstEmployeePositionRepo.findOne(responsibleto);
            System.out.println("employeeRes :" + employeeRes);
            employmentpositionname = employeeRes.getEmploymentpositionname();
            System.out.println("employmentpositionname :" + employmentpositionname);
        }
        employeePosition.getCategory();
        logger.debug("Position ID : {}", employeePosition.getEmploymentpositionid());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("idJabatan");
        RequestContext.getCurrentInstance().update("idJabatan");
        RequestContext.getCurrentInstance().execute("PF('widgetJabatan').show()");
    }

    public void batal() {
        refresh();
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        employeePosition = new MstEmployeePosition();
        employmentpositionname = new String();
    }

    public void hapus(MstEmployeePosition i) {
        logger.debug("Unit : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstEmployeePositionRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Jabatan berhasil dihapus");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /*public void openFormTambah() {
        position = mstEmployeePositionRepo.findAllByEmploymentpositionidContaining(employeePosition.getEmploymentpositionname());
        //System.out.println("nama employee :" + employeePosition.getEmploymentpositionname());
    }*/
//     public String getPositionByEmploymentpositionid(String employmentpositionid) {
//        return mstEmployeePositionRepo.findOne(employmentpositionid).getEmploymentpositionname();
//    }
//    
    public List<SelectItem> getListEmployeePosition() {
        if (listEmployeePosition == null) {
            listEmployeePosition = new ArrayList<>();
            EmployeePositionService.getEmployeePosition().stream().forEach((EmployeePosition) -> {
                listEmployeePosition.add(new SelectItem(EmployeePosition.getEmploymentpositionname(), EmployeePosition.getEmploymentpositionname()));
            });
        }
        return listEmployeePosition;
    }

    @Override
        protected List<SecureItem> getSecureItems() {
        return null;
    }

}
