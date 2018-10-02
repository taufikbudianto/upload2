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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.TaskEmail;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.repo.TaskEmailRepo;
import prosia.basarnas.service.IncidentPeralatanService;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class IncidentPeralatanMBean extends AbstractManagedBean implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MstAssetType assetType;
    private MstAssetType currAssetType;
    private ResAssetContact assetContact;
    private String currType;
//    private List<AssetTypeTemp> listTypeTemp;
//    private List<IncAssetTemp> listAssetTemp;
    private MstAssetType mstTipeAsset;
    private LazyDataModelJPA<MstAssetType> listTipeAset;
    private LazyDataModelJPA<ResAsset> listAsset;
    private IncidentAsset incidentAsset;
    private List<IncidentAsset> listIncAsset;
    private List<IncidentAsset> listIncidentAssetAll;
    private Boolean disabled;
    private Incident currIncident;
    private List<String> assetTypeId;
    private Map<MstAssetType, Boolean> checkedPilihTipe;
    private Map<MstAssetType, Boolean> checkedKirimTipe;
    private Map<ResAsset, Boolean> checkedPilihAset;
    private Map<ResAsset, Boolean> checkedKirimAset;
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
    private GtwEmailOut emailOut;
    private GtwSmsOut gtwSmsOut;

    @Autowired
    private MstAssetTypeRepo assetTypeRepo;
    @Autowired
    private IncidentAssetRepo incidentAssetRepo;
    @Autowired
    private ResAssetRepo resAssetRepo;
    @Autowired
    private IncidentMBean incidentMBean;
    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private IncidentPeralatanService peralatanService;
    @Autowired
    private GtwSmsOutRepo gtwSmsOutRepo;
    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;
    @Autowired
    private TaskEmailRepo taskEmailRepo;
    @Autowired
    private ResPotencyRepo resPotencyRepo;
    @Autowired
    private IncidentNationalRepo incidentNationalRepo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private ResAssetContactRepo assetContactRepo;

    public IncidentPeralatanMBean() {
        disabled = false;
    }

    @Data
    public class TempCheck {

        private Boolean kirim;
        private Boolean pilih;
        private ResAsset resAsset;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userSiteId = null;
        userSiteId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        incidentAsset = new IncidentAsset();
        checkedPilihAset = new HashMap<ResAsset, Boolean>();
        checkedKirimAset = new HashMap<ResAsset, Boolean>();
        checkedPilihTipe = new HashMap<MstAssetType, Boolean>();
        checkedKirimTipe = new HashMap<MstAssetType, Boolean>();
        isPilihAll = false;
        isKirimAll = false;
        disabled = incidentMBean.getDisableForm();
        currIncident = new Incident();
        currIncident = incidentMBean.getIncident();
        List<String> idkansar = new ArrayList<>();

        if (currIncident.getIncidentScala() == 0 || currIncident.getIncidentScala() == null) {
            idkansar.add(currIncident.getUsersiteid());
        } else {
            for (IncidentNational n : incidentNationalRepo.findByIncident(currIncident)) {
                idkansar.add(n.getKantorSarId());
            }
        }
        assetTypeId = new ArrayList<>();
        assetTypeId = assetTypeRepo.listAssetTypeId(idkansar);

        listTipeAset = new LazyDataModelJPA(assetTypeRepo) {
            @Override
            protected Page getDatas(PageRequest request) {
                if (!assetTypeId.isEmpty()) {
                    Page<MstAssetType> page = assetTypeRepo.findAll(whereQuery(assetTypeId), request);
                    checkedPilihTipe = preparePilihChecked(page, checkedPilihTipe);
                    return page;
                } else {
                    return null;
                }
            }

            @Override
            protected long getDataSize() {
                if (!assetTypeId.isEmpty()) {
                    return assetTypeRepo.count(whereQuery(assetTypeId));
                } else {
                    return 0l;
                }

            }
        };
//        listAsset = new LazyDataModelJPA(resAssetRepo) {
//
//            @Override
//            protected Page getDatas(PageRequest request) {
//                Page<ResAsset> page = resAssetRepo.findAll(request);
//                checkedPilihAset = preparePilihAssetChecked(page, checkedPilihAset);
//                return page;
//            }
//
//            @Override
//            protected long getDataSize() {
//                return resAssetRepo.count(whereQuery1());
//            }
//        };

        disabled = incidentMBean.getDisableForm();
        logger.debug("Incident without send parameter : {}", incidentMBean.getIncident());
    }

    private Specification<MstAssetType> whereQuery(List<String> assetList) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstAssetType>() {

            @Override
            public Predicate toPredicate(Root<MstAssetType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.desc(root.get("datecreated")));
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                predicates.add(cb.notEqual(root.<Integer>get("issru"), 0));
                Expression<String> exp = root.<String>get("assettypeid");
                predicates.add(exp.in(assetList));
                //predicates.add(cb.isNotNull(root.<String>get("usersiteid")));
                return andTogether(predicates, cb);
            }
        };
    }

    public void checkallpilih() {
        isPilihAll = true;
        checkedPilihTipe.clear();
        checkedPilihAset.clear();

        for (ResAsset ra : resAssetRepo.findAll(whereQuery1())) {
            if (!checkedPilihAset.isEmpty() && checkedPilihAset.containsKey(ra)) {
                checkedPilihAset.replace(ra, true);
            } else {
                checkedPilihAset.put(ra, true);
            }
        }

        for (Map.Entry<ResAsset, Boolean> map : checkedPilihAset.entrySet()) {
            if (map.getValue() == true) {
                if (!checkedPilihTipe.isEmpty()
                        && checkedPilihTipe.containsKey(map.getKey().getAssettypeid())) {
                    checkedPilihTipe.replace(map.getKey().getAssettypeid(), true);
                } else {
                    checkedPilihTipe.put(map.getKey().getAssettypeid(), true);
                }
            }
        }

//        for (MstAssetType ma : assetTypeRepo.findAll(whereQuery(assetTypeId))) {
//            checkedPilihTipe.put(ma, true);
//        }
//
//        for (Map.Entry<MstAssetType, Boolean> map : checkedPilihTipe.entrySet()) {
//            if (map.getValue() == true) {
//                for (ResAsset ra : resAssetRepo.findAll(whereQuery1())) {
//                    checkedPilihAset.put(ra, true);
//                }
//            }
//        }
    }

    public void uncheckallpilih() {
        isPilihAll = false;
        checkedPilihAset.clear();
        checkedPilihTipe.clear();

    }

    public void uncheckallkirim() {
        isPilihAll = false;
        checkedKirimAset.clear();
        checkedKirimTipe.clear();
    }

    public void checkallKirim() {
        isKirimAll = true;
        checkedKirimTipe.clear();
        checkedKirimAset.clear();

        for (ResAsset ra : resAssetRepo.findAll(whereQuery1())) {
            if (!checkedKirimAset.isEmpty() && checkedKirimAset.containsKey(ra)) {
                checkedKirimAset.replace(ra, true);
            } else {
                checkedKirimAset.put(ra, true);
            }
        }

        for (Map.Entry<ResAsset, Boolean> map : checkedKirimAset.entrySet()) {
            if (map.getValue() == true) {
                if (!checkedKirimTipe.isEmpty()
                        && checkedKirimTipe.containsKey(map.getKey().getAssettypeid())) {
                    checkedKirimTipe.replace(map.getKey().getAssettypeid(), true);
                } else {
                    checkedKirimTipe.put(map.getKey().getAssettypeid(), true);
                }
            }
        }

    }

    public void showDialogNotif() {
        kirimEmail = false;
        kirimSms = false;
        org.primefaces.context.RequestContext.getCurrentInstance().reset("incidentdetail:kirim-notif-peralatan");
        org.primefaces.context.RequestContext.getCurrentInstance().update("incidentdetail:kirim-notif-peralatan");
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('kirim-notif-peralatan').show()");
    }

    private Map<MstAssetType, Boolean> preparePilihChecked(Page<MstAssetType> page, Map<MstAssetType, Boolean> checked) {
        List<String> idkansar = new ArrayList<>();

        if (currIncident.getIncidentScala() == 0 || currIncident.getIncidentScala() == null) {
            idkansar.add(currIncident.getUsersiteid());
        } else {
            for (IncidentNational n : incidentNationalRepo.findByIncident(currIncident)) {
                idkansar.add(n.getKantorSarId());
            }
        }
        for (MstAssetType ma : page.getContent()) {
            try {
                if (incidentAssetRepo.findAssetTypeByIncidentCount(incidentMBean.getIncident(), ma, false)
                        == resPotencyRepo.findAllAssetType(ma.getAssettypeid(), idkansar)) {
                    if (!checked.isEmpty() && checked.containsKey(ma)) {
                        checked.replace(ma, true);
                    } else {
                        checked.put(ma, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checked;
    }

    private Map<ResAsset, Boolean> preparePilihAssetChecked(Page<ResAsset> page, Map<ResAsset, Boolean> checked) {
        for (ResAsset ra : page.getContent()) {
            try {
                /* Cek dari database asset sudah ada atau belum. kalau sudah ada, asset tsb akan di ceklist */
                if (incidentAssetRepo.findAllByIncident(incidentMBean.getIncident(), ra, false) != null) {
                    if (!checked.isEmpty() && checked.containsKey(ra)) {
                        checked.replace(ra, true);
                    } else {
                        checked.put(ra, true);
                    }

                } else {
                    /* cek asset tipe, jika di ceklist nama aset tipe status true, maka aseet yang tergabung 
                    dalam tipe asset tersebut akan diceklist*/
                    if (checkedPilihTipe.containsKey(ra.getAssettypeid())) {
                        if (checkedPilihTipe.get(ra.getAssettypeid())) {
                            if (!checked.isEmpty() && checked.containsKey(ra)) {
                                checked.replace(ra, true);
                            } else {
                                checked.put(ra, true);
                            }
                        } else {
                            if (!checked.isEmpty() && checked.containsKey(ra)) {
                                checked.replace(ra, false);
                            } else {
                                checked.put(ra, false);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checked;
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void onRowSelect(SelectEvent event) {
        logger.debug("type : " + ((MstAssetType) event.getObject()).getAssettypeid());
        currType = null;
        currType = ((MstAssetType) event.getObject()).getAssettypeid();

        listAsset = new LazyDataModelJPA(resAssetRepo) {

            @Override
            protected Page getDatas(PageRequest request) {
                Page<ResAsset> page = resAssetRepo.findAll(whereQuery1(), request);
                checkedPilihAset = preparePilihAssetChecked(page, checkedPilihAset);
                return page;
            }

            @Override
            protected long getDataSize() {
                return resAssetRepo.count(whereQuery1());
            }
        };
    }

    private Specification<ResAsset> whereQuery1() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAsset>() {
            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                if (currIncident.getIncidentScala() == 1) {
                    List<String> kansar = new ArrayList<>();
                    for (IncidentNational n : incidentNationalRepo.findByIncident(currIncident)) {
                        kansar.add(n.getKantorSarId());
                    }
                    Expression<String> exp = root.<ResPotency>get("potencyid").
                            <MstKantorSar>get("kantorsarid").get("kantorsarid");
                    predicates.add(exp.in(kansar));
                } else if (currIncident.getIncidentScala() == null || currIncident.getIncidentScala() == 0) {
                    predicates.add(cb.equal(root.<ResPotency>get("potencyid").<MstKantorSar>get("kantorsarid").
                            get("kantorsarid"), currIncident.getUsersiteid()));
                }
                if (StringUtils.isNotBlank(currType)) {
                    predicates.add(cb.equal(root.<MstAssetType>get("assettypeid").get("assettypeid"), currType));
                }
                predicates.add(cb.notEqual(root.<MstAssetType>get("assettypeid").get("issru"), 0));
                return andTogether(predicates, cb);
            }

        };
    }

    private void save(Map<ResAsset, Boolean> checked) {
        Integer index = 0;
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
                index++;
            }
        }
        addPopUpMessage("Sukses", "Peralatan berhasil disimpan");
    }

    private IncidentAsset setIncAsset(ResAsset resAsset, boolean isDelete) {
        incidentAsset.setAssetType(resAsset.getAssettypeid());
        incidentAsset.setOfficeSar(resAsset.getPotencyid().getKantorsarid());
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
        incidentAsset.setPotency(resAsset.getPotencyid());

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

    public void action() {
        save(checkedPilihAset);
        delete(checkedPilihAset);
    }

    public void actionKirimNotif() {
        if (kirimSms == false && kirimEmail == false) {
            addPopUpMessage("Perhatian", "Silahkan pilih Salah Satu");
        }
        kirim(checkedKirimAset);
        addPopUpMessage("Perhatian", "Kirim berhasil");
        RequestContext.getCurrentInstance().execute("PF('kirim-notif-peralatan').hide()");
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
            System.out.println("ID =      " + formatEmailOutId());
            emailOut.setRecipientname(assetContact.getContactname());
            emailOut.setEmailaddress(assetContact.getEmail());
            emailOut.setSubject(subyekEmail);
            emailOut.setTextmessage(isiEmail);
            System.out.println(emailOut.toString());
            gtwEmailOutRepo.save(emailOut);

            taskEmail = new TaskEmail();
            taskEmail.setTaskName("Kirim Email dari Menu Incident >> Tab Peralatan");
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

    //
    //    public void onRowCheckKirim(AssetTypeTemp temp) {
    //        logger.debug("AssetType : {}", temp.getAssetType().getAssettypeid());
    //        logger.debug("Kirim : {}", temp.getKirim());
    //        Boolean isCheckKirim = temp.getKirim();
    //        Boolean isCheckPilih = temp.getPilih();
    //        if (temp != null) {
    //            //List<IncidentAsset> listAsset = incidentAssetRepo.findByAssetType(temp.getAssetType());
    //            List<IncidentAsset> listAsset = (List<IncidentAsset>) listIncidentAssetAll.stream()
    //                    .filter(u -> u.getAssetType().getAssettypeid().equals(temp.getAssetType().getAssettypeid()))
    //                    .collect(Collectors.toList());
    //            listAssetTemp = new ArrayList<>();
    //            if (listAsset.size() > 0) {
    //                for (IncidentAsset asset : listAsset) {
    //                    IncAssetTemp t = new IncAssetTemp();
    //                    t.setPilih(isCheckPilih);
    //                    t.setKirim(isCheckKirim);
    //                    t.setIncAsset(asset);
    //                    listAssetTemp.add(t);
    //                }
    //            }
    //        }
    //    }
    //
    //    public void onRowCheckPilih(AssetTypeTemp temp) {
    //        logger.debug("AssetType : {}", temp.getAssetType().getAssettypeid());
    //        logger.debug("Pilih : {}", temp.getPilih());
    //        Boolean isCheckKirim = temp.getKirim();
    //        Boolean isCheckPilih = temp.getPilih();
    //        //List<IncidentAsset> listAsset = incidentAssetRepo.findByAssetType(temp.getAssetType());
    //        List<IncidentAsset> listAsset = (List<IncidentAsset>) listIncidentAssetAll.stream()
    //                .filter(u -> u.getAssetType().getAssettypeid().equals(temp.getAssetType().getAssettypeid()))
    //                .collect(Collectors.toList());
    //        listAssetTemp = new ArrayList<>();
    //        if (listAsset.size() > 0) {
    //            for (IncidentAsset asset : listAsset) {
    //                IncAssetTemp t = new IncAssetTemp();
    //                t.setPilih(isCheckPilih);
    //                t.setKirim(isCheckKirim);
    //                t.setIncAsset(asset);
    //                listAssetTemp.add(t);
    //            }
    //        }
    //    }
    //
    //    public void onRowCheckPilihIncAsset(IncAssetTemp iat) {
    //        AssetTypeTemp tempAsset = listTypeTemp.stream()
    //                .filter((p) -> iat.getIncAsset().getAssetType().getAssettypeid()
    //                .equals(p.assetType.getAssettypeid()))
    //                .findAny()
    //                .orElse(null);
    //        long count = listAssetTemp.stream()
    //                .filter(u -> u.getPilih().equals(true))
    //                .collect(Collectors.counting());
    //        if (count == 0) {
    //            tempAsset.setPilih(false);
    //        } else {
    //            tempAsset.setPilih(true);
    //        }
    //    }
    //
    //    public void onRowCheckIncAsset(IncAssetTemp iat) {
    //        int statusChecked = 0;
    //        AssetTypeTemp tempAsset = listTypeTemp.stream()
    //                .filter((p) -> iat.getIncAsset().getAssetType().getAssettypeid()
    //                .equals(p.assetType.getAssettypeid()))
    //                .findAny()
    //                .orElse(null);
    //        long count = listAssetTemp.stream()
    //                .filter(u -> u.getKirim().equals(true))
    //                .collect(Collectors.counting());
    //        if (count == 0) {
    //            tempAsset.setKirim(false);
    //        } else {
    //            tempAsset.setKirim(true);
    //        }
    //
    //        //
    //        for (int i = 0; i < listAssetTemp.size(); i++) {
    //            if (listAssetTemp.get(i).getKirim()) {
    //                statusChecked++;
    //                break;
    //            }
    //        }
    //        for (int i = 0; i < listTypeTemp.size(); i++) {
    //            if (listTypeTemp.get(i).getAssetType() == iat.getIncAsset().getAssetType() && statusChecked > 0) {
    //                listTypeTemp.get(i).setKirim(true);
    //            } else if (listTypeTemp.get(i).getAssetType() == iat.getIncAsset().getAssetType() && statusChecked == 0) {
    //                listTypeTemp.get(i).setKirim(false);
    //                listTypeTemp.set(i, listTypeTemp.get(i));
    //            }
    //        }
    //    }
    //
    public String getKansarOrPotency(ResAsset ass) {
        if (ass.getKantorsarid() != null) {
            return ass.getKantorsarid().getKantorsarname();
        } else if (ass.getPotencyid() != null) {
            return ass.getPotencyid().getPotencyname();
        }
        return "-";
    }

    //
    //    public String getGoodQty(IncidentAsset aset) {
    //        if (aset.getGoodQty() != null) {
    //            return String.valueOf(aset.getAsset().getGoodqty());
    //        }
    //        return "0";
    //    }
    //
//        public String getUsageQty(IncidentAsset aset) {
//            if (aset.getUsageQty() != null) {
//                return String.valueOf(aset.getUsageQty());
//            }
//            return "0";
//        }
    //
}
