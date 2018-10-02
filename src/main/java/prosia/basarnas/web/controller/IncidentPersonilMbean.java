/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.json.JSONObject;
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
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentBudgetPersonil;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.Ss_SiapSiaga;
import prosia.basarnas.model.TaskEmail;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;
import prosia.basarnas.repo.IncidentBudgetPersonnelRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.SsSiapSiagaRepo;
import prosia.basarnas.repo.TaskEmailRepo;

/**
 *
 * @author Owner
 */
@Component
@Scope("view")
public @Data
class IncidentPersonilMbean extends AbstractManagedBean implements InitializingBean {

    private ResPersonnel selectedPresonil;
    private IncidentPersonnel selectedIncidentPersonnel;
    private Incident currIncident;
    private ResPersonnel resPersonnel;
    private IncidentPersonnel incidentPersonnel;
    private List<IncidentPersonnel> delPersonel = new ArrayList<>();
    private List<IncidentPersonilMbean.TempCheck> listTempCheck;
    private List<IncidentPersonilMbean.TempCheck> listSelectCheckbox;
    private List<IncidentPersonnel> listPersonelIncident;
    private List<Ss_SiapSiaga> listSiapSiaga;
    private LazyDataModelJPA<ResPersonnel> listPersonelRes;
    private List<MstEMployeeClass> listEmployeClass;
    private TempCheck tempCheck;
    private TaskEmail taskEmail;
    private GtwSmsOut gtwSmsOut;
    private GtwEmailOut emailOut;
    private Boolean selectionAllowed;
    private Boolean disabled;
    private Boolean disabledTab;
    private Boolean kirimSms;
    private Boolean kirimEmail;
    private String isisms;
    private String isiEmail;
    private String subyekEmail;
    private Collection<String> kansar;
    private String cari;
    private Integer jenis;
    private String userSiteId;
    Date date = new Date();
    private String tglhariini;

    private String personelCode;
    private String personelName;
    private String position;
    private String respons;
    private String employmentClassId;
    private Date assignmentDatee;
    private Date assignmentEndDatee;

    @Getter
    @Setter
    private String responsibility;
    @Getter
    @Setter
    private String posisition;
    @Getter
    @Setter
    private Date assignmentDate;
    @Getter
    @Setter
    private Date assignmentEndDate;

    @Autowired
    private IncidentPersonnelRepo personnelRepo;
    @Autowired
    private ResPersonnelRepo resPersonnelRepo;
    @Autowired
    private MstEmployeeClassRepo empClassRepo;
    @Autowired
    private IncidentMBean incidentMBean;
    @Autowired
    private IncidentNationalRepo incidentNationalRepo;
    @Autowired
    private SsSiapSiagaRepo siapSiagaRepo;
    @Autowired
    private GtwSmsOutRepo gtwSmsOutRepo;
    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;
    @Autowired
    private IncidentBudgetPersonilMBean budgetPersonil;
    @Autowired
    private IncidentBudgetPersonnelRepo incBudgetPersonelRepo;
    @Autowired
    private TaskEmailRepo taskEmailRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userSiteId = null;
        userSiteId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        gtwSmsOut = new GtwSmsOut();
        taskEmail = new TaskEmail();
        listSelectCheckbox = new ArrayList<>();
        listTempCheck = new ArrayList<>();
        tempCheck = new TempCheck();
        kansar = new ArrayList<>();
        jenis = 0;
        kirimSms = false;
        kirimEmail = false;
        currIncident = incidentMBean.getIncident();
        disabledTab = incidentMBean.getDisableForm();
        if (incidentMBean.getIncident().getIncidentScala() == 0 || incidentMBean.getIncident().getIncidentScala() == null) {
            kansar.add(incidentMBean.getIncident().getUsersiteid());
        } else {
            for (IncidentNational incNational : incidentNationalRepo.findByIncident(incidentMBean.getIncident())) {
                kansar.add(incNational.getKantorSarId());
            }
        }
        listPersonelIncident = new ArrayList<>();
        listPersonelIncident = personnelRepo.findAll(whereQuery());
        for (IncidentPersonnel personnel : listPersonelIncident) {
            tempCheck = new TempCheck();
            tempCheck.setPersonnelDetails(personnel);
            listTempCheck.add(tempCheck);
        }

        this.listSiapSiaga = new ArrayList<>();
        listSiapSiaga = siapSiagaRepo.findAll();

        this.listPersonelRes = new LazyDataModelJPA(resPersonnelRepo) {

            @Override
            protected long getDataSize() {
                if (StringUtils.isNotBlank(cari)) {
                    return resPersonnelRepo.findAllByKansarIdAndName(kansar, cari.toUpperCase());
                } else {
                    return resPersonnelRepo.findAllByKansarId(kansar);
                }

            }

            @Override
            protected Page getDatas(PageRequest request) {
                if (StringUtils.isNotBlank(cari)) {
                    return resPersonnelRepo.findAllByKansarIdAndName(kansar, cari.toUpperCase(), request);
                } else {
                    return resPersonnelRepo.findAllByKansarId(kansar, request);
                }
            }
        };

    }

    private Specification<IncidentPersonnel> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<IncidentPersonnel>() {

            @Override
            public Predicate toPredicate(Root<IncidentPersonnel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<Incident>get("incident"), currIncident));
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public void checkallpilih() {
        for (int i = 0; i < listTempCheck.size(); i++) {
            listTempCheck.get(i).setPilih(true);
        }
    }

    public void uncheckallpilih() {
        for (int i = 0; i < listTempCheck.size(); i++) {
            listTempCheck.get(i).setPilih(false);
        }
    }

    public void checkallkirim() {
        for (int i = 0; i < listTempCheck.size(); i++) {
            listTempCheck.get(i).setKirim(true);
        }
    }

    public void uncheckallkirim() {
        for (int i = 0; i < listTempCheck.size(); i++) {
            listTempCheck.get(i).setKirim(false);
        }
    }

    public void kirimNotifikasi() {

        try {
            if (listTempCheck.isEmpty()) {
                error2();
            } else {
                long count = listTempCheck.stream().filter(x -> x.getKirim()).count();
                if (count > 0) {
                    RequestContext.getCurrentInstance().execute("PF('kirim-notif-coba').show()");
                } else {
                    error();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void reset() {
        cari = "";
        RequestContext.getCurrentInstance().execute("PF('listWidgetPsnl').getPaginator().setPage(0)");
//        showCombo = false;
    }

    public void refresh() {
        gtwSmsOut = new GtwSmsOut();
        taskEmail = new TaskEmail();
        isisms = "";
        subyekEmail = "";
        isiEmail = "";

        kirimEmail = false;
        kirimSms = false;
    }

    public void kirim() {
        List<TaskEmail> taskEmails = new ArrayList<>();
        for (int i = 0; i < listTempCheck.size(); i++) {
            if (listTempCheck.get(i).getKirim()) {
                log.debug("Personel Name Kirim : " + listTempCheck.get(i).personnelDetails.getPersonnelName());
                if (kirimSms == true) {
                    gtwSmsOut = new GtwSmsOut();
                    System.out.println("gtwSmsOut.getSmsoutgoingid()= " + gtwSmsOut.getSmsoutgoingid());
                    if (gtwSmsOut.getSmsoutgoingid() == null) {
                        gtwSmsOut.setSmsoutgoingid(formatSmsoutgoingId());
                    }

                    gtwSmsOut.setRecipientname(listTempCheck.get(i).personnelDetails.getPersonnelName());
                    gtwSmsOut.setPhonenumber(listTempCheck.get(i).personnelDetails.getPhoneNumber());
                    gtwSmsOut.setTextmessage(isisms);
                    gtwSmsOut.setDatecreated(date);
                    gtwSmsOut.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                    gtwSmsOut.setUsersiteid("CGK");
                    gtwSmsOut.setIsdeleted(BigInteger.ZERO);
                    gtwSmsOut.setPrioritas(BigInteger.ONE);
                    gtwSmsOutRepo.save(gtwSmsOut);
                }

                if (kirimEmail == true) {

                    emailOut = new GtwEmailOut();
                    emailOut.setEmailoutgoingid(formatEmailOutId());
                    System.out.println("ID =      " + formatEmailOutId());
                    emailOut.setRecipientname(listTempCheck.get(i).personnelDetails.getPersonnelName());
                    emailOut.setEmailaddress(listTempCheck.get(i).personnelDetails.getEmail());
                    emailOut.setSubject(subyekEmail);
                    emailOut.setTextmessage(isiEmail);
                    gtwEmailOutRepo.save(emailOut);

                    taskEmail = new TaskEmail();
                    taskEmail.setTaskName("Kirim Email dari Menu Incident >> Tab Personil");
                    taskEmail.setTaskType("prosia.task.sendemail.SendEmailFromTemplateTask");
                    taskEmail.setTaskTypeId(1);
                    JSONObject json = new JSONObject();
                    json.put("to", listTempCheck.get(i).personnelDetails.getEmail());
                    json.put("subyek", subyekEmail);
                    json.put("isi", isiEmail);
                    json.put("nama", listTempCheck.get(i).personnelDetails.getPersonnelName());

                    taskEmail.setParameters(json.toString());
                    taskEmail.setStatus("new");
                    taskEmail.setTimeSubmitted(date);
                    taskEmails.add(taskEmail);

                }
            }
        }

        if (kirimSms == true) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Sms berhasil dikirim");
        }

        if (kirimEmail == true) {
            taskEmailRepo.save(taskEmails);
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Email berhasil dikirim");
        }

        refresh();
        RequestContext.getCurrentInstance().execute("PF('kirim-notif-coba').hide()");
    }

    private String formatEmailOutId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String emailOutid = "";
        List<GtwEmailOut> ias = gtwEmailOutRepo.findTopOneByEmailoutgoingidContaining(userSiteId);
        if (ias.isEmpty()) {
            emailOutid = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxemailOutId = gtwEmailOutRepo.findEmailByMaxId(userSiteId);
            String[] splitId = maxemailOutId.split("-");
            if (maxemailOutId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                emailOutid = userSiteId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
                System.out.println("INCASSETID : " + emailOutid);
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                emailOutid = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
                System.out.println("INCASSETID : " + emailOutid);
            }
        }
        return emailOutid;

    }

    public String formatSmsoutgoingId() {
        tglhariini = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = tglhariini.split("-");
        String nextval = "";
        String smsOutId = "";
        List<GtwSmsOut> lis = gtwSmsOutRepo.findTopOneBySmsoutgoingidContaining("CGK");
        if (lis.isEmpty()) {
            smsOutId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            System.out.println("smsoutid1 : " + smsOutId);
        } else {
            String maxId = gtwSmsOutRepo.findGtwSmsOutByMaxId("CGK");
            String[] splitId = maxId.split("-");
            System.out.println("SPLIT ID 1 : " + splitId[1]);
            System.out.println("SPLIT ID 2 : " + splitId[2]);
            System.out.println("SPLIT DATE 0 : " + splitDate[0]);
            System.out.println("SPLIT DATE 1 : " + splitDate[1]);
            if (maxId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                System.out.println("next = " + next);
                System.out.println("length = " + length);
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
                smsOutId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
                System.out.println("smsoutid2 : " + smsOutId);
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                System.out.println("3");
                smsOutId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
                System.out.println("smsoutid3 : " + smsOutId);
            }
        }
        System.out.println("SMS OUT ID " + smsOutId);
        return smsOutId;
    }

    public void simpan() {

        try {
            /*Insert Incident Id Di Sini*/

            for (int i = 0; i < listTempCheck.size(); i++) {

                this.selectedIncidentPersonnel = new IncidentPersonnel();
                if (listTempCheck.get(i).getPersonnelDetails().getIncidentPersonnelID() == null) {
                    selectedIncidentPersonnel.setIncidentPersonnelID(formatIncidentPersonnelId());
                    selectedIncidentPersonnel.setPersonnelCode(listTempCheck.get(i).personnelDetails.getPersonnelCode());
                    selectedIncidentPersonnel.setPersonnelName(listTempCheck.get(i).personnelDetails.getPersonnelName());
                    selectedIncidentPersonnel.setIncident(incidentMBean.getIncident());
                    selectedIncidentPersonnel.setResponsibility(this.responsibility);
                    selectedIncidentPersonnel.setPosition(this.posisition);
                    selectedIncidentPersonnel.setEmploymentClass(listTempCheck.get(i).personnelDetails.getEmploymentClass());
                    selectedIncidentPersonnel.setAssignmentDate(listTempCheck.get(i).personnelDetails.getAssignmentDate());
                    selectedIncidentPersonnel.setAssignmentEndDate(listTempCheck.get(i).personnelDetails.getAssignmentEndDate());
                    selectedIncidentPersonnel.setPersonnel(selectedPresonil);
                    personnelRepo.save(selectedIncidentPersonnel);
                    System.out.println("berhasil simpan personel " + listTempCheck.get(i).personnelDetails.getPersonnelCode() + " dengan nama "
                            + listTempCheck.get(i).personnelDetails.getPersonnelName() + " di insiden " + incidentMBean.getIncident().getIncidentid());
                }
            }

            for (IncidentPersonnel personel : delPersonel) {
                personel.setDeleted(true);
                personnelRepo.save(personel);
            }
            addPopUpMessage(FacesMessage.SEVERITY_INFO,"Sukses","Data berhasil disimpan");
            delPersonel.clear();

        } catch (Exception e) {
            e.printStackTrace();
            addPopUpMessage(FacesMessage.SEVERITY_ERROR,"Gagal","Data gagal disimpan");
        }

        budgetPersonil.checkDataIntable(currIncident);
        List<IncidentBudgetPersonil> listBudgetPersonil = new ArrayList<>();
        for (IncidentBudgetPersonil budgetPersonel : incBudgetPersonelRepo.findAllByNotDeleted(currIncident.getIncidentid())) {
            long betweenDays = daysBetween(budgetPersonel.getTglMulai(), budgetPersonel.getTglAkhir());
            Double total = (budgetPersonel.getMstBiayaPersonnel().getBiayaMakan() + budgetPersonel.getMstBiayaPersonnel().getBiayaSuplemen()) * betweenDays;
            budgetPersonel.setTotal(total);
            listBudgetPersonil.add(budgetPersonel);
        }
        budgetPersonil.setListBudgetPersonil(listBudgetPersonil);
        RequestContext.getCurrentInstance().update("incidentdetail:formBiayaPersonil:tableBudgetPersonil");
    }

    public void changeJenis() {
        if (jenis.equals(0) == true) {
            System.out.println("personel");
            jenis = 0;
        } else {
            System.out.println("Siaga");
            jenis = 1;
        }
    }

    public void cari() {
        if (jenis == 0) {
            RequestContext.getCurrentInstance().update("incidentdetail:form:idDtDlgPersonel");
        } else {
            RequestContext.getCurrentInstance().update("incidentdetail:form:idDtDlgSiaga");
        }
    }

    public String formatIncidentPersonnelId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incpotencyid = "";
        List<IncidentPersonnel> ias = personnelRepo.findTopOneByIncidentPersonnelIDContaining("CGK");
        if (ias.isEmpty()) {
            incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personnelRepo.findPotencyByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incpotencyid;
    }

    public void closeSaveInformationDialog() {
        RequestContext.getCurrentInstance().execute("PF('saveNotification').hide()");
    }

    public void loadIncidentPersonnel() {
        this.selectedIncidentPersonnel = new IncidentPersonnel();
        try {

            if (selectedPresonil != null) {
                TempCheck tcek = new TempCheck();
                this.selectedIncidentPersonnel.setPersonnelName(selectedPresonil.getPersonnelName());
                this.selectedIncidentPersonnel.setPersonnelCode(selectedPresonil.getPersonnelCode());
                this.selectedIncidentPersonnel.setEmploymentClass(empClassRepo.findOne(selectedPresonil.getEmploymentClass().getEmploymentclassid()));
                this.selectedIncidentPersonnel.setResponsibility(responsibility);
                this.selectedIncidentPersonnel.setAssignmentDate(assignmentDate);

                this.selectedIncidentPersonnel.setAssignmentEndDate(assignmentEndDate);
                tcek.setKirim(false);
                tcek.setPilih(false);
                tcek.setPersonnelDetails(selectedIncidentPersonnel);
                long count = listTempCheck.stream()
                        .filter((p) -> p.getPersonnelDetails().getPersonnelCode().equals(selectedPresonil.getPersonnelCode()))
                        .collect(Collectors.counting());
                if (count == 0) {
                    this.listTempCheck.add(tcek);
                }
            } else {
                selectedPresonil = new ResPersonnel();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hapus() {
        try {
            if (listTempCheck.isEmpty()) {
                error2();
            } else {
                delPersonel = new ArrayList<>();
                for (TempCheck temp : listTempCheck) {
                    if (temp.getPilih()) {
                        System.out.println("test " + temp.getPersonnelDetails().getPersonnelName());
                        delPersonel.add(temp.getPersonnelDetails());
                    }
                }
                for (int i = 0; i < listTempCheck.size(); i++) {

                    listTempCheck.removeIf((t) -> {
                        if (!t.getPilih()) {
                            return false;
                        }
                        return true;
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data gagal Dihapus!", null));
        }

    }

    public void lihat(Boolean readOnly, String incidentPersonnelID,String codePersonel) {
        TempCheck temp = null;
        try {
            temp = listTempCheck.stream()
                    .filter(x -> x.getPersonnelDetails().getIncidentPersonnelID().equals(incidentPersonnelID))
                    .findAny().orElse(null);
        } catch (NullPointerException e) {
            temp = listTempCheck.stream()
                    .filter(x -> x.getPersonnelDetails().getPersonnelCode().equals(codePersonel))
                    .findAny().orElse(null);
        }
        disabled = readOnly;
        personelCode = temp.personnelDetails.getPersonnelCode();
        personelName = temp.personnelDetails.getPersonnelName();
        employmentClassId = temp.personnelDetails.getEmploymentClass().getEmploymentclassname();
        position = temp.personnelDetails.getPosition();
        respons = temp.personnelDetails.getResponsibility();
        assignmentDatee = temp.personnelDetails.getAssignmentDate();
        assignmentEndDatee = temp.personnelDetails.getAssignmentEndDate();
        RequestContext.getCurrentInstance().reset("incidentdetail:form3:idpersonel");
        RequestContext.getCurrentInstance().update("incidentdetail:form3:idpersonel");
        RequestContext.getCurrentInstance().execute("PF('dlg-IncPersonelDetail').show()");

    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Personel Selected", ((ResPersonnel) event.getObject()).getPersonnelID());
        this.listEmployeClass = empClassRepo.findAll();
        responsibility = "";
        posisition = "";
        assignmentDate = null;
        assignmentEndDate = null;
        RequestContext.getCurrentInstance().execute("PF('dlg-PersonelDetail').show()");
    }

    public Boolean isSelectionAllowed() {

        return selectionAllowed;
    }

    public void setSelectionAllowed(Boolean selectionAllowed) {
        this.selectionAllowed = selectionAllowed;
    }

    public void error() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pilih personil di kolom kirim", null));
    }

    public void error2() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tidak Ada Personil", null));
    }

    private static long daysBetween(Date mulai, Date akhir) {
        if (mulai.compareTo(akhir) > 0 || mulai.compareTo(akhir) == 0) {
            return 0;
        }
        long difference = (mulai.getTime() - akhir.getTime()) / 86400000;
        return Math.abs(difference);
    }

    @Data
    public class TempCheck {

        private Boolean kirim;
        private Boolean pilih;
        private IncidentPersonnel personnelDetails;
    }

}
