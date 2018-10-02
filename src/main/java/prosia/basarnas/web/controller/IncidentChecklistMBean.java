/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import lombok.Cleanup;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang3.StringUtils;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.primefaces.context.RequestContext;
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
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentChecklist;
import prosia.basarnas.model.MstChecklist;
import prosia.basarnas.model.MstChecklistdetail;
import prosia.basarnas.repo.IncidentChecklistRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.MstChecklistRepo;
import prosia.basarnas.repo.MstChecklistdetailRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;

/**
 *
 * @author TOMY
 */
@Component
@Scope("view")

public @Data
class IncidentChecklistMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String name;
    private String field;
    private String value;
    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean disabled;
    private Boolean disabledInput;
    private List<SelectItem> listChecklist;
    private MstChecklist checklist;
    private MstChecklistdetail checklistdetail;
    private IncidentChecklist incidentChecklist;
    private Incident currIncident;
    private LazyDataModelJPA<MstChecklistdetail> lazyDataModelJPA;
    private LazyDataModelJPA<IncidentChecklist> lazyDataModelIncidentJPA;
    private String INCIDENTID;
    private String INCIDENTNUMBER;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private MstChecklistRepo mstChecklistRepo;

    @Autowired
    private MstChecklistdetailRepo checklistdetailRepo;

    @Autowired
    private IncidentChecklistRepo incidentChecklistRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private IncidentMBean incidentMBean;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public IncidentChecklistMBean() {
        field = "";
        showButton = true;
        disabled = false;
        disabledInput = false;
        showButtonInput = true;
        incidentChecklist = new IncidentChecklist();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currIncident = incidentMBean.getIncident();
        disabled = incidentMBean.getDisableForm();
        if (currIncident != null) {
            detail(currIncident);
        }

        lazyDataModelJPA = new LazyDataModelJPA(checklistdetailRepo) {
            @Override
            protected long getDataSize() {
                return checklistdetailRepo.count(whereQuery(field));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return checklistdetailRepo.findAll(whereQuery(field), request);
            }
        };
    }

    private Specification<MstChecklistdetail> whereQuery(
            final String field) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstChecklistdetail>() {

            @Override
            public Predicate toPredicate(Root<MstChecklistdetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(field)) {
                    System.out.println("a");
                    predicates.add(cb.equal(root.<MstChecklist>get("checklistid").<String>get("checklistid"), field));
                }
                //System.out.println("Hasil = " +andTogether(predicates, cb));
                return andTogether(predicates, cb);

            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void detail(Incident incidentChecklistID) {
        lazyDataModelIncidentJPA = new LazyDataModelJPA(incidentChecklistRepo) {
            @Override
            protected long getDataSize() {
                return incidentChecklistRepo.count(whereQueryInc(incidentChecklistID));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentChecklistRepo.findAll(whereQueryInc(incidentChecklistID), request);
            }
        };
    }

    private Specification<IncidentChecklist> whereQueryInc(Incident detailId) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<IncidentChecklist>() {

            @Override
            public Predicate toPredicate(Root<IncidentChecklist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<IncidentChecklist>get("incident"), detailId));
                return andTogether(predicates, cb);
            }
        };
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

        List<IncidentChecklist> list = incidentChecklistRepo.findAllByIncidentChecklistIDContaining("CGK");
        if (list.isEmpty()) {
            //System.out.println("A");
            checkId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxId = incidentChecklistRepo.findCheckByMaxId("CGK");
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

    public void simpan(String field) {
        showButton = true;
        this.checklist = mstChecklistRepo.findByChecklistid(field);
        List<MstChecklistdetail> lis = checklistdetailRepo.findByChecklistid(checklist);
        
        for (int i = 0; i < lis.size(); i++) {
            IncidentChecklist ck = new IncidentChecklist();
            ck.setIncident(currIncident);
            ck.setIncidentChecklistID(formatcheckId());
            ck.setCategory(lis.get(i).getCategory());
            ck.setChecklistName(lis.get(i).getDescription());
            incidentChecklistRepo.save(ck);
        }
        detail(currIncident);
        addPopUpMessage("Sukses", "Data Checklist berhasil disimpan");
        //refresh();
        RequestContext.getCurrentInstance().execute("PF('widgetInsidenChecklist').hide()");
    }

    public void add() {
        RequestContext.getCurrentInstance().reset("incidentdetail:idInsidenChecklist");
        RequestContext.getCurrentInstance().update("incidentdetail:idInsidenChecklist");
        RequestContext.getCurrentInstance().execute("PF('widgetInsidenChecklist').show()");
    }

    public void resetView() {
        disabled = false;
        showButton = true;
        incidentChecklist = new IncidentChecklist();
        RequestContext.getCurrentInstance().reset("incidentdetail:idViewInsidenChecklist");
        RequestContext.getCurrentInstance().update("incidentdetail:idViewInsidenChecklist");
        RequestContext.getCurrentInstance().execute("PF('widgetViewInsidenChecklist').show()");
    }

    public List<SelectItem> getListChecklist() {
        if (listChecklist == null) {
            //System.out.println("masuk");
            listChecklist = new ArrayList<>();
            mstChecklistRepo.findAllByIsdeleted(BigInteger.ZERO).stream().forEach((Checklist) -> {
                listChecklist.add(new SelectItem(Checklist.getChecklistid(), Checklist.getName()));
            });
        }
        return listChecklist;
    }

    public void viewIncChecklist(String incidentChecklistID, Boolean edited) {
        System.out.println("ID = " + incidentChecklistID);
        this.incidentChecklist = incidentChecklistRepo.findByIncidentChecklistID(incidentChecklistID);
        disabledInput = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("incidentdetail:idViewInsidenChecklist");
        RequestContext.getCurrentInstance().update("incidentdetail:idViewInsidenChecklist");
        RequestContext.getCurrentInstance().execute("PF('widgetViewInsidenChecklist').show()");
    }

    public void simpanIncidentChecklist() {
        if (incidentChecklist.getIncidentChecklistID() == null) {
            incidentChecklist.setIncidentChecklistID(formatcheckId());
            incidentChecklist.setIncident(currIncident);
            incidentChecklistRepo.save(incidentChecklist);
            RequestContext.getCurrentInstance().execute("PF('widgetViewInsidenChecklist').hide()");
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses","Insiden Checklist berhasil ditambah");
        } else {
        incidentChecklist.setIncident(currIncident);
        incidentChecklistRepo.save(incidentChecklist);
        RequestContext.getCurrentInstance().execute("PF('widgetViewInsidenChecklist').hide()");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Negara berhasil disimpan"));
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses","Insiden Checklist berhasil diubah");
    }}

    public void hapus(String id) {
        this.incidentChecklist = incidentChecklistRepo.findByIncidentChecklistID(id);
        incidentChecklistRepo.delete(incidentChecklist);
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Sukses","Insiden Checklist berhasil dihapus");
        addMessage("Sukses", "Insiden Checklist berhasil dihapus");
    }
    
    public String getUserFromReport(){
        return getCurrentUser().getParty().getFullName();
    }

    public void reportPrint() throws JRException, SQLException, IOException, IOException {
        System.out.println("sudah disni");
        try {
            String id = (String) getRequestParam("obj");
            String user = getCurrentUser().getParty().getFullName();
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
            HashMap hm = new HashMap();
            String jrxml = "/report/report_jasper/incident_checklist.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");

            File fileJrxml = new File(pathJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("INCIDENTNUMBER", field);
            hm.put("user", user);
         
           //parameter atau field yang akan dijadikan filter
            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"CetakPresensi.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();

            facescontext.responseComplete();

        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void batal() {
        refresh();
    }
    
    public void refresh() {
        disabled = false;
    }
}
