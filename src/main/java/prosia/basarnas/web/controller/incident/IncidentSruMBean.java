/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.incident;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.enumeration.AssetMatra;
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentBudgetAsset;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.model.TaskEmail;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentBudgetAssetRepo;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.TaskEmailRepo;
import prosia.basarnas.web.controller.IncidentBudgetAssetMBean;
import prosia.basarnas.web.controller.IncidentMBean;
import prosia.basarnas.web.controller.IncidentSRUDaratMBean;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Ismail
 */
@Controller
@Scope("view")
@Data
public class IncidentSruMBean extends AbstractManagedBean implements InitializingBean {

    private AssetMatra sru;
    private ResAssetContact assetContact;
    private IncidentAsset incidentAsset;
    private Incident inc;
    private Map<ResAsset, Boolean> checkedPilihDarat;
    private Map<ResAsset, Boolean> checkedPilihUdara;
    private Map<ResAsset, Boolean> checkedPilihLaut;
    private Map<ResAsset, Boolean> checkedKirimDarat;
    private Map<ResAsset, Boolean> checkedKirimUdara;
    private Map<ResAsset, Boolean> checkedKirimLaut;
    private String assetType;
    private boolean isPilihAll;
    private boolean isKirimAll;
    private Boolean kirimSms;
    private Boolean kirimEmail;
    private String isisms;
    private String isiEmail;
    private String subyekEmail;
    private String userSiteId;
    Date date = new Date();
    private String tglhariini;
    private TaskEmail taskEmail;
    private GtwSmsOut gtwSmsOut;
    private GtwEmailOut emailOut;
    private boolean disabled;
    @Autowired
    private IncidentMBean incidentMBean;
    @Autowired
    private ResAssetRepo resAssetRepo;
    @Autowired
    private IncidentAssetRepo incidentAssetRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private GtwSmsOutRepo gtwSmsOutRepo;
    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;
    @Autowired
    private TaskEmailRepo taskEmailRepo;
    @Autowired
    private IncidentBudgetAssetMBean budgetAssetMbean;
    @Autowired
    private IncidentBudgetAssetRepo incBudgetAssetRepo;
    @Autowired
    private ResAssetContactRepo assetContactRepo;

    private LazyDataModelJPA<ResAsset> listAssetDarat;
    private LazyDataModelJPA<ResAsset> listAssetUdara;
    private LazyDataModelJPA<ResAsset> listAssetLaut;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private List<TempCheck> listTempCheck;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public IncidentSruMBean() {
        initCoordinate();
    }

    @Data
    public class TempCheck {

        private Double distance;
        private Double angle;
        private Boolean kirim;
        private Boolean pilih;
        private ResAsset resAsset;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userSiteId = null;
        userSiteId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        incidentAsset = new IncidentAsset();
        checkedPilihDarat = new HashMap<ResAsset, Boolean>();
        checkedPilihUdara = new HashMap<ResAsset, Boolean>();
        checkedPilihLaut = new HashMap<ResAsset, Boolean>();
        checkedKirimDarat = new HashMap<ResAsset, Boolean>();
        checkedKirimUdara = new HashMap<ResAsset, Boolean>();
        checkedKirimLaut = new HashMap<ResAsset, Boolean>();
        isPilihAll = false;
        isKirimAll = false;
        disabled = incidentMBean.getDisableForm();
        listAssetDarat = new LazyDataModelJPA(resAssetRepo) {
            @Override
            protected Page getDatas(PageRequest request) {
                Page<ResAsset> page = resAssetRepo.findAll(whereQuery(0), request);
                checkedPilihDarat = preparePilihChecked(page, checkedPilihDarat);
                return page;
            }

            @Override
            protected long getDataSize() {
                return resAssetRepo.count(whereQuery(0));
            }
        };
        listAssetUdara = new LazyDataModelJPA(resAssetRepo) {
            @Override
            protected Page getDatas(PageRequest request) {
                Page<ResAsset> page = resAssetRepo.findAll(whereQuery(1), request);
                checkedPilihUdara = preparePilihChecked(page, checkedPilihUdara);
                return page;
            }

            @Override
            protected long getDataSize() {
                return resAssetRepo.count(whereQuery(1));
            }
        };
        listAssetLaut = new LazyDataModelJPA(resAssetRepo) {
            @Override
            protected Page getDatas(PageRequest request) {
                Page<ResAsset> page = resAssetRepo.findAll(whereQuery(2), request);
                checkedPilihLaut = preparePilihChecked(page, checkedPilihLaut);
                return page;
            }

            @Override
            protected long getDataSize() {
                return resAssetRepo.count(whereQuery(2));
            }
        };
    }

    private Map<ResAsset, Boolean> preparePilihChecked(Page<ResAsset> page, Map<ResAsset, Boolean> checked) {
        for (ResAsset ra : page.getContent()) {
            try {
                if (incidentAssetRepo.findAllByIncident(incidentMBean.getIncident(), ra, false) != null) {
                    if (!checked.isEmpty() && checked.containsKey(ra)) {
                        checked.replace(ra, true);
                    } else {
                        checked.put(ra, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checked;
    }

    private Specification<ResAsset> whereQuery(Integer opType) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAsset>() {
            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<BigInteger>get("op_type"), BigInteger.valueOf(opType)));
                predicates.add(cb.equal(root.<MstAssetType>get("assettypeid").get("issru"), 0));
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                query.orderBy(cb.asc(root.get("name")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public Double distance(String lng, String lat) {
        try {
            if (lng != null && lat != null && incidentMBean.getIncident().getLongitude() != null
                    && incidentMBean.getIncident().getLatitude() != null) {
                Map<String, Double> mapDisAngle = setDistanceAndAngle(lng, lat,
                        incidentMBean.getIncident().getLongitude(), incidentMBean.getIncident().getLatitude());
                return mapDisAngle.get("distance");
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double angle(String lng, String lat) {
        try {
            if (lng != null && lat != null && incidentMBean.getIncident().getLongitude() != null
                    && incidentMBean.getIncident().getLatitude() != null) {
                Map<String, Double> mapDisAngle = setDistanceAndAngle(lng, lat,
                        incidentMBean.getIncident().getLongitude(), incidentMBean.getIncident().getLatitude());
                return mapDisAngle.get("angle");
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void checkallpilih() {
        isPilihAll = true;
        String value = (String) getRequestParam("sruCheckAllPilih");
        System.out.println("value = " + value);
        if (value.equals(AssetMatra.Darat.toString())) {
            checkedPilihDarat.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(0))) {
                checkedPilihDarat.put(ra, true);
                System.out.println("a" + ra.getName());
            }
        } else if (value.equals(AssetMatra.Udara.toString())) {
            checkedPilihUdara.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(1))) {
                checkedPilihUdara.put(ra, true);
                System.out.println("c" + ra.getName());
            }
        } else if (value.equals(AssetMatra.Laut.toString())) {
            checkedPilihLaut.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(2))) {
                checkedPilihLaut.put(ra, true);
                System.out.println("b" + ra.getName());
            }
        }

    }

    public void uncheckallpilih() {
        isPilihAll = false;
        String value = (String) getRequestParam("sruUncheckAllPilih");
        if (value.equals(AssetMatra.Darat.toString())) {
            checkedPilihDarat.clear();
        } else if (value.equals(AssetMatra.Laut.toString())) {
            checkedPilihLaut.clear();
        } else if (value.equals(AssetMatra.Udara.toString())) {
            checkedPilihUdara.clear();
        }
    }

    public void checkallkirim() {
        isKirimAll = true;
        String value = (String) getRequestParam("sruCheckAllKirim");
        if (value.equals(AssetMatra.Darat.toString())) {
            checkedKirimDarat.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(0))) {
                checkedKirimDarat.put(ra, true);
            }
        } else if (value.equals(AssetMatra.Udara.toString())) {
            checkedKirimUdara.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(1))) {
                checkedKirimUdara.put(ra, true);
            }
        } else if (value.equals(AssetMatra.Laut.toString())) {
            checkedKirimLaut.clear();
            for (ResAsset ra : resAssetRepo.findAll(whereQuery(2))) {
                checkedKirimLaut.put(ra, true);
            }
        }
    }

    public void uncheckallkirim() {
        isKirimAll = false;
        String value = (String) getRequestParam("sruUncheckAllKirim");
        if (value.equals(AssetMatra.Darat.toString())) {
            checkedKirimDarat.clear();
        } else if (value.equals(AssetMatra.Laut.toString())) {
            checkedKirimLaut.clear();
        } else if (value.equals(AssetMatra.Udara.toString())) {
            checkedKirimUdara.clear();
        }
    }

    public void showDetail() {
        System.out.println("Show Detail");
        ResAsset resAsset = (ResAsset) getRequestParam("resAsset");
        String value = (String) getRequestParam("sruAction");
        System.out.println("value = " + value);
        sru = null;
        if (value.equals(AssetMatra.Darat.toString())) {
            sru = AssetMatra.Darat;
        } else if (value.equals(AssetMatra.Udara.toString())) {
            sru = AssetMatra.Udara;
        } else if (value.equals(AssetMatra.Laut.toString())) {
            sru = AssetMatra.Laut;
        }

        System.out.println(">> " + sru.toString());
        incidentAsset
                = incidentAssetRepo.findAllByIncidentAndAsset(
                        incidentMBean.getIncident(), resAsset);
        if (incidentAsset == null) {
            incidentAsset = new IncidentAsset();
            incidentAsset.setIncidentAssetID(formatIncidentAssetId());
        }
        incidentAsset = setIncAsset(resAsset, false);

        initCoordinate();
        viewCoordinate();
        RequestContext.getCurrentInstance().reset("incidentdetail:idDialogSRU");
        RequestContext.getCurrentInstance().update("incidentdetail:idDialogSRU");
        RequestContext.getCurrentInstance().execute("PF('showDialogSRU').show()");
    }

    public void action() {
        String value = (String) getRequestParam("sruAction");

        if (value.equals(AssetMatra.Darat.toString())) {
            save(checkedPilihDarat);
            delete(checkedPilihDarat);
            addPopUpMessage("Sukses",
                    "Incident Asset Darat berhasil disimpan");
        } else if (value.equals(AssetMatra.Laut.toString())) {
            save(checkedPilihLaut);
            delete(checkedPilihLaut);
            addPopUpMessage("Sukses",
                    "Incident Asset Laut berhasil disimpan");
        } else if (value.equals(AssetMatra.Udara.toString())) {
            save(checkedPilihUdara);
            delete(checkedPilihUdara);
            addPopUpMessage("Sukses",
                    "Incident Asset Udara berhasil disimpan");
        }

        budgetAssetMbean.checkDataIntable(incidentMBean.getIncident());
        List<IncidentBudgetAsset> listAssetBudget = new ArrayList<>();
        for (IncidentBudgetAsset budget : incBudgetAssetRepo.findAllByNotDeleted(incidentMBean.getIncident().getIncidentid())) {
            budget.setTotal(budget.getBbm().getNilai() * budget.getBbbAssetPemakaian() * budget.getBudgetAssetDur());
            listAssetBudget.add(budget);
        }
        budgetAssetMbean.setListAssetBudget(listAssetBudget);
        RequestContext.getCurrentInstance().update("incidentdetail:formBiayaBBM:tableBBM");

    }

    public void showDialogNotif() {
        kirimEmail = false;
        kirimSms = false;
        assetType = (String) getRequestParam("sruActionKirim");
        RequestContext.getCurrentInstance().reset("incidentdetail:kirim-notif-sru");
        RequestContext.getCurrentInstance().update("incidentdetail:kirim-notif-sru");
        RequestContext.getCurrentInstance().execute("PF('wid-kirim-notif-sru').show()");
    }

    public void actionKirimNotif() {
        if (kirimSms == false && kirimEmail == false) {
            addPopUpMessage("Perhatian", "Silahkan pilih Salah Satu");
        }
        System.out.println("actionKirimNotif");
        if (assetType.equals(AssetMatra.Darat.toString())) {
            kirim(checkedKirimDarat);
            RequestContext.getCurrentInstance().update("incidentdetail:daratsruinc");
        } else if (assetType.equals(AssetMatra.Laut.toString())) {
            kirim(checkedKirimLaut);
            RequestContext.getCurrentInstance().update("incidentdetail:lautsruinc");
        } else if (assetType.equals(AssetMatra.Udara.toString())) {
            kirim(checkedKirimUdara);
            RequestContext.getCurrentInstance().update("incidentdetail:udarasruinc");
        }
        addPopUpMessage("Perhatian", "Kirim berhasil");
        RequestContext.getCurrentInstance().execute("PF('wid-kirim-notif-sru').hide()");
    }

    public void KirimSms(ResAsset resAsset, boolean isDelete) {
        assetContact = assetContactRepo.findByAssetIdDist(resAsset.getAssetid());
        try {
            System.out.println("gtwSmsOut.getSmsoutgoingid()= " + gtwSmsOut.getSmsoutgoingid());
            gtwSmsOut.setRecipientname(assetContact.getContactname());
            gtwSmsOut.setPhonenumber(assetContact.getPhonenumber1());
            gtwSmsOut.setTextmessage(isisms);
            gtwSmsOut.setDatecreated(date);
            gtwSmsOut.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            gtwSmsOut.setUsersiteid(resAsset.getUsersiteid());
            gtwSmsOut.setIsdeleted(BigInteger.ZERO);
            gtwSmsOut.setPrioritas(BigInteger.ONE);
            gtwSmsOutRepo.save(gtwSmsOut);
        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Gagal", "SMS Gagal Dikirim");
        }

    }

    public void KirimEmail(ResAsset resAsset, boolean isDelete) {
        try {

            emailOut = new GtwEmailOut();
            emailOut.setEmailoutgoingid(formatEmailOutId());
            emailOut.setRecipientname(assetContact.getContactname());
            emailOut.setEmailaddress(assetContact.getEmail());
            emailOut.setSubject(subyekEmail);
            emailOut.setTextmessage(isiEmail);
            System.out.println(emailOut.toString());
            gtwEmailOutRepo.save(emailOut);

            taskEmail = new TaskEmail();
            taskEmail.setTaskName("Kirim Email dari Menu Incident >> Tab SRU");
            taskEmail.setTaskType("prosia.task.sendemail.SendEmailFromTemplateTask");
            taskEmail.setTaskTypeId(1);
            JSONObject json = new JSONObject();
            json.put("to", assetContact.getEmail());
            json.put("subyek", subyekEmail);
            json.put("isi", isiEmail);
            json.put("nama", assetContact.getContactname());

            taskEmail.setParameters(json.toString());
            taskEmail.setStatus("new");
            taskEmail.setTimeSubmitted(date);
            taskEmailRepo.save(taskEmail);
        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Gagal", "Email Gagal Dikirim");
        }

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

    public void kirim(Map<ResAsset, Boolean> checked) {
        for (Map.Entry<ResAsset, Boolean> map : checked.entrySet()) {
            assetContact = new ResAssetContact();
            if (map.getValue()) {
                incidentAsset
                        = incidentAssetRepo.findAllByIncidentAndAsset(
                                incidentMBean.getIncident(), map.getKey());

                gtwSmsOut = new GtwSmsOut();
                gtwSmsOut.setSmsoutgoingid(formatSmsoutgoingId());
                if (kirimSms == true) {
                    KirimSms(map.getKey(), false);
                }
                if (kirimEmail == true) {
                    KirimEmail(map.getKey(), false);
                }
            }
        }

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

    private void save(Map<ResAsset, Boolean> checked) {
        for (Map.Entry<ResAsset, Boolean> entry : checked.entrySet()) {
            if (entry.getValue()) {
                incidentAsset
                        = incidentAssetRepo.findAllByIncidentAndAsset(
                                incidentMBean.getIncident(), entry.getKey());
                if (incidentAsset == null) {
                    incidentAsset = new IncidentAsset();
                    incidentAsset.setIncidentAssetID(formatIncidentAssetId());
                }
                incidentAssetRepo.save(setIncAsset(entry.getKey(), false));
            }
        }
    }

    private void delete(Map<ResAsset, Boolean> checked) {
        List<IncidentAsset> listIncidentAsset = incidentAssetRepo.findAllByIncident(incidentMBean.getIncident());
        for (IncidentAsset incidentAsset : listIncidentAsset) {
            if (checked.containsKey(incidentAsset.getAsset()) && !checked.get(incidentAsset.getAsset())) {
                incidentAsset.setDeleted(true);
                incidentAssetRepo.save(incidentAsset);
            }
        }
    }

    private IncidentAsset setIncAsset(ResAsset resAsset, boolean isDelete) {
        incidentAsset.setAssetType(resAsset.getAssettypeid());
        incidentAsset.setOfficeSar(resAsset.getKantorsarid());
        incidentAsset.setPosSar(resAsset.getPossarid());
        incidentAsset.setCode(resAsset.getCode());
        incidentAsset.setName(resAsset.getName());
        incidentAsset.setGoodQty(resAsset.getGoodqty());
        incidentAsset.setRejectedQty(resAsset.getRejectedqty());
        incidentAsset.setServicedQty(resAsset.getServicedqty());
        incidentAsset.setOtherQty(resAsset.getOtherqty());
        incidentAsset.setAssetCondition(resAsset.getAssetcondition());
        incidentAsset.setCargo(resAsset.getCargo());
        incidentAsset.setLongitude(resAsset.getLongitude());
        incidentAsset.setLatitude(resAsset.getLatitude());
        incidentAsset.setFunctional(resAsset.getFunctional());
        incidentAsset.setAsset(resAsset);

        incidentAsset.setIncident(incidentMBean.getIncident());

//                  incidentAsset.setUsageQty(resAsset.get);
//                  incidentAsset.setUsageDate(resAsset.get);
//                  incidentAsset.setAssignmentDate(resAsset.getA);
//                  incidentAsset.setAssignmentEndDate(resAsset.getA);
        incidentAsset.setUserSiteID(resAsset.getUsersiteid());
        incidentAsset.setDeleted(isDelete);
        incidentAsset.setStatus(resAsset.getStatus());

        incidentAsset.setEndurance(resAsset.getEndurance());
        incidentAsset.setSpeed(resAsset.getSpeed());
        incidentAsset.setVehicleType(resAsset.getVehicletype());
        incidentAsset.setRemarks(resAsset.getRemarks());
        return incidentAsset;
    }

    private String formatIncidentAssetId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<IncidentAsset> ias = incidentAssetRepo.findTopOneByIncidentAssetIDContaining("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = incidentAssetRepo.findAssetByMaxId("CGK");
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
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
                System.out.println("INCASSETID : " + incassetid);
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
                System.out.println("INCASSETID : " + incassetid);
            }
        }
        return incassetid;

    }

    public void initCoordinate() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void viewCoordinate() {
        if (incidentAsset.getLatitude() != null
                || StringUtils.isNotBlank(incidentAsset.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(incidentAsset.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (incidentAsset.getLongitude() != null
                || StringUtils.isNotBlank(incidentAsset.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(incidentAsset.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }

    public void saveDetail() {

        incidentAssetRepo.save(incidentAsset);

        addPopUpMessage("Sukses", "Incident Asset berhasil disimpan");
        switch (sru) {
            case Darat:
                if (!checkedPilihDarat.isEmpty() && checkedPilihDarat.containsKey(incidentAsset.getAsset())) {
                    checkedPilihDarat.replace(incidentAsset.getAsset(), true);
                } else {
                    checkedPilihDarat.put(incidentAsset.getAsset(), true);
                }
                RequestContext.getCurrentInstance().update("incidentdetail:daratsruinc");
                break;
            case Laut:
                if (!checkedPilihLaut.isEmpty() && checkedPilihLaut.containsKey(incidentAsset.getAsset())) {
                    checkedPilihLaut.replace(incidentAsset.getAsset(), true);
                } else {
                    checkedPilihLaut.put(incidentAsset.getAsset(), true);
                }
                RequestContext.getCurrentInstance().update("incidentdetail:lautsruinc");
                break;
            case Udara:
                if (!checkedPilihUdara.isEmpty() && checkedPilihUdara.containsKey(incidentAsset.getAsset())) {
                    checkedPilihUdara.replace(incidentAsset.getAsset(), true);
                } else {
                    checkedPilihUdara.put(incidentAsset.getAsset(), true);
                }
                RequestContext.getCurrentInstance().update("incidentdetail:udarasruinc");
                break;
            default:
                break;
        }
        RequestContext.getCurrentInstance().execute("PF('showDialogSRU').hide()");

    }
}
