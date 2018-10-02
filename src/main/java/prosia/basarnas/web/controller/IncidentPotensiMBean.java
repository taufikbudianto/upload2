/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import prosia.basarnas.model.ResPotency;
import prosia.basarnas.repo.ResPotencyRepo;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static javafx.scene.input.KeyCode.T;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.CrmAreaCode;
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.IncidentPotency;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.MstRegion;
import prosia.basarnas.model.MstSkill;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.model.TaskEmail;
import prosia.basarnas.repo.CrmAreaRepo;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentPotencyRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.repo.MstEmployeeUnitRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstRegionRepo;
import prosia.basarnas.repo.MstSkillRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.TaskEmailRepo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class IncidentPotensiMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private ResPotencyRepo resPotencyRepo;

    private List<TempCheck> listTempCheck;
    private List<TempCheck> listSelectCheckbox;

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private IncidentPotencyRepo incidentPotencyRepo;

    @Autowired
    private IncidentMBean incidentMBean;

    private IncidentPotency incidentPotency;

    private Incident inc;
    private Incident currIncident;
    private ResPotencyContact potencyContact;
    private GtwEmailOut emailOut;

    private ResPotency resPotency;

    private List<MstKantorSar> listKantorSar;
    private List<MstPosSar> listPostSar;
    private List<ResAsset> listResAsset;
    private List<MstAssetType> listMstAset;
    private List<MstRegion> listPropinsi;
    private List<CrmAreaCode> listKota;
    private List<MstEMployeeClass> listGolongan;
    private List<MstEmployeePosition> listJabatanStruktural;
    private List<MstEmployeePosition> listJabatanFungsional;
    private List<MstEmployeePosition> listJabatanSiaga;
    private List<MstEmployeeUnit> listUnitKerja;
//    private List<String> kansar;

    private List<MstSkill> listSkill;
    private List<ResPersonnelTraining> listPersonelTraining;
    private List<ResPersonnelTraining> listPotensiPersonelTraining;
    private List<ResPersonnelHistory> listPersonelHistory;
    private List<ResPersonnelHistory> listPotensiPersonelHistory;
    private List<ResPotencyContact> listkontak;
    private List<ResPersonnel> listPersonel;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean disabledTab;
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

    @Autowired
    private ResAssetRepo resAssetRepo;

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private ResPotencyContactRepo kontakRepo;

    @Autowired
    private CrmAreaRepo kotaRepo;

    @Autowired
    private ResPersonnelRepo personnelRepo;

    @Autowired
    private MstRegionRepo propinsiRepo;

    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;

    @Autowired
    private MstEmployeeClassRepo mstEmployeeClassRepo;

    @Autowired
    private MstEmployeeUnitRepo unitRepo;

    @Autowired
    private MstEmployeePositionRepo mstEmployeePositionRepo;

    @Autowired
    private MstSkillRepo skillRepo;

    @Autowired
    private MstAssetTypeRepo mstAssetTypeRepo;

    @Autowired
    private GtwSmsOutRepo gtwSmsOutRepo;

    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private IncidentNationalRepo incidentNationalRepo;

    @Autowired
    private TaskEmailRepo taskEmailRepo;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userSiteId = null;
        userSiteId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        inc = new Incident();
        currIncident = incidentMBean.getIncident();
        disabledTab = incidentMBean.getDisableForm();
        log.debug("CURR INCIDENT : " + currIncident);
        String id = null;
        if (currIncident != null) {
            id = currIncident.getIncidentid();
        }

        listSelectCheckbox = new ArrayList<>();
        listTempCheck = new ArrayList<>();

        Double dist = 0.0;
        Double angle = 0.0;

        /*if (incidentMBean.getIncident().getIncidentScala() == null || incidentMBean.getIncident().getIncidentScala() == 0) {
            kansar.add(incidentMBean.getIncident().getUsersiteid());
        } else {
            for (IncidentNational incNational : incidentNationalRepo.findByIncident(incidentMBean.getIncident())) {
                kansar.add(incNational.getKantorSarId());
            }
        }*/
        if (id != null) {
            inc = incidentRepo.findOne(id);
            List<ResPotency> resPotencys = resPotencyRepo.findAll(whereQuery());
            System.out.println("isi = " + resPotencys.size());
            for (ResPotency ra : resPotencys) {
                TempCheck tempcheck = new TempCheck();
                if (ra.getLongitude() != null && ra.getLatitude() != null
                        && inc.getLongitude() != null && inc.getLatitude() != null) {
                    Map<String, Double> mapDisAngle = getDistance(ra.getLongitude(), ra.getLatitude(), inc.getLongitude(), inc.getLatitude());
                    dist = mapDisAngle.get("distance");
                    angle = mapDisAngle.get("angle");
                } else {
                    dist = 0.0;
                    angle = 0.0;
                }
                tempcheck.setDistance(dist);
                tempcheck.setAngle(angle);
                incidentPotency = incidentPotencyRepo.findAllByIncidentAndPotency(inc, ra);
                if (incidentPotency != null) {
                    tempcheck.setPilih(true);
                } else {
                    incidentPotency = new IncidentPotency();
                    tempcheck.setPilih(false);
                }
                tempcheck.setKirim(false);

                tempcheck.setResPotency(ra);
                listTempCheck.add(tempcheck);

            }
        } else {
            resPotency = new ResPotency();
            //incidentAsset = new IncidentAsset();
            List<ResPotency> resPotencys = resPotencyRepo.findAll(whereQuery());
            for (ResPotency ra : resPotencys) {
                TempCheck tempcheck = new TempCheck();
                if (ra.getLongitude() != null && ra.getLatitude() != null) {
                    Map<String, Double> mapDisAngle = getDistance(ra.getLongitude(), ra.getLatitude(), "0", "0");
                    dist = mapDisAngle.get("distance");
                    angle = mapDisAngle.get("angle");
                } else {
                    dist = 0.0;
                    angle = 0.0;
                }
                tempcheck.setKirim(false);
                tempcheck.setPilih(false);
                tempcheck.setResPotency(ra);
                tempcheck.setDistance(dist);
                tempcheck.setAngle(angle);
                listTempCheck.add(tempcheck);
            }
        }

//        List<ResPotency> resPotency = resPotencyRepo.findAll(whereQuery());
//        inc = incidentRepo.findOne(id);
//        for (ResPotency ra : resPotency) {
//            if (ra.getLongitude() != null && ra.getLatitude() != null
//                    && inc.getLongitude() != null && inc.getLatitude() != null) {
//                Map<String, Double> mapDisAngle = getDistance(ra.getLongitude(), ra.getLatitude(), inc.getLongitude(), inc.getLatitude());
//                dist = mapDisAngle.get("distance");
//                angle = mapDisAngle.get("angle");
//            } else {
//                dist = 0.0;
//                angle = 0.0;
//            }
//
//            TempCheck tempcheck = new TempCheck();
//            tempcheck.setDistance(dist);
//            tempcheck.setAngle(angle);
//            tempcheck.setKirim(false);
//            tempcheck.setPilih(false);
//            tempcheck.setResPotency(ra);
//            listTempCheck.add(tempcheck);
//        }
    }

    private Specification<ResPotency> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<ResPotency>() {
            @Override
            public Predicate toPredicate(Root<ResPotency> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                if (currIncident.getIncidentScala() == 1) {
                    List<String> kansar = new ArrayList<>();
                    for (IncidentNational n : incidentNationalRepo.findByIncident(currIncident)) {
                        kansar.add(n.getKantorSarId());
                    }
                    Expression<String> exp = root.<MstKantorSar>get("kantorsarid").get("kantorsarid");
                    System.out.println("kansar.size() : " + kansar.size());
                    predicates.add(exp.in(kansar));
                } else {
                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").
                            get("kantorsarid"), currIncident.getUsersiteid()));
                }

                query.orderBy(cb.asc(root.get("potencyname")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

        return cb.and(predicates.toArray(new Predicate[0]));
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

    @Data
    public class TempCheck {

        private Double distance;
        private Double angle;
        private Boolean kirim;
        private Boolean pilih;
        private ResPotency resPotency;
    }

    public Map<String, Double> getDistance(String long1, String lat1, String long2, String lat2) {
        Map<String, Double> map = new HashMap<>();
        Double longFrom = Double.valueOf(long2);
        Double latFrom = Double.valueOf(lat2);
        Double longTo = Double.valueOf(long1);
        Double latTo = Double.valueOf(lat1);
        Double distance = MapCalculation.calculateDistanceInNm(latFrom, longFrom, latTo, longTo);
        distance = DecimalUtil.rounding(distance);
        Double angle = MapCalculation.calculateAngle(longFrom, latFrom, longTo, latTo);
        angle = MapCalculation.convertDegreeToPositive(angle);
        angle = DecimalUtil.rounding(angle);
        map.put("distance", distance);
        map.put("angle", angle);
        return map;
    }

    public void viewCheck(TempCheck tempcheck) {
        resPotency = new ResPotency();
        incidentPotency = new IncidentPotency();
        resPotency = resPotencyRepo.findOne(tempcheck.getResPotency().getPotencyid());
        RequestContext.getCurrentInstance().reset("incidentdetail:inc-potensi-content");
        RequestContext.getCurrentInstance().update("incidentdetail:inc-potensi-content");
        RequestContext.getCurrentInstance().execute("PF('widgetViewPotensi').show()");
    }

    public void loadDetail() {
        resPotency = (ResPotency) getRequestParam("listRow");
        initCoordinate();
        if (resPotency != null) {
            viewCoordinate();
            listResAsset = resAssetRepo.findBypotencyidAndisdeleted(resPotency);
            listKota = kotaRepo.findByregion(resPotency.getRegionid());
            listkontak = kontakRepo.findAllBypotencyid(resPotency);
        }

        RequestContext.getCurrentInstance().reset("incidentdetail:inc-potensi-content:idviewpotensi");
        RequestContext.getCurrentInstance().update("incidentdetail:inc-potensi-content:idviewpotensi");
        RequestContext.getCurrentInstance().execute("PF('widgetViewPotensi').show()");

    }

    public String formatIncidentPotencyId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incpotencyid = "";
        List<IncidentPotency> ias = incidentPotencyRepo.findTopOneByIncidentPotencyIDContaining("CGK");
        if (ias.isEmpty()) {
            incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = incidentPotencyRepo.findPotencyByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        log.debug("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        log.debug("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        log.debug("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        log.debug("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        log.debug("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        log.debug("DEFAULT");
                        break;
                }
                incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incpotencyid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incpotencyid;

    }

    public void action() {
        for (TempCheck tempcheck : listTempCheck) {
            if (tempcheck.getPilih()) {
                IncidentPotency incAss = new IncidentPotency();
                incidentPotency = new IncidentPotency();
                incAss = incidentPotencyRepo.findAllByIncidentAndPotency(inc, tempcheck.getResPotency());

                if (incAss == null) {
                    incidentPotency.setIncidentPotencyID(formatIncidentPotencyId());
                    incidentPotency.setPotency(tempcheck.getResPotency());
                    incidentPotency.setIncident(inc);
                    incidentPotency.setOfficeSar(tempcheck.getResPotency().getKantorsarid());
                    incidentPotency.setPotencyName(tempcheck.getResPotency().getPotencyname());
                    incidentPotency.setPotencyType(tempcheck.getResPotency().getPotencytype().intValue());
                    incidentPotency.setAddress(tempcheck.getResPotency().getAddress());
                    incidentPotency.setLongitude(tempcheck.getResPotency().getLongitude());
                    incidentPotency.setLatitude(tempcheck.getResPotency().getLatitude());
                    incidentPotency.setCodeArea(tempcheck.getResPotency().getAreacodeid());
                    incidentPotency.setRegion(tempcheck.getResPotency().getRegionid());
                    incidentPotency.setPhoneNumber1(tempcheck.getResPotency().getPhonenumber1());
                    incidentPotency.setPhoneNumber2(tempcheck.getResPotency().getPhonenumber2());
                    incidentPotency.setPhoneNumber3(tempcheck.getResPotency().getPhonenumber3());
                    incidentPotency.setFaxNumber(tempcheck.getResPotency().getFaxnumber());
                    incidentPotency.setRadioFrequency(tempcheck.getResPotency().getRadiofrequency());
                    incidentPotency.setEmail(tempcheck.getResPotency().getEmail());
                    incidentPotency.setSocialNetwork(tempcheck.getResPotency().getSocialnetwork());
                    incidentPotency.setGisSymbol(tempcheck.getResPotency().getGissymbol());
                    incidentPotency.setRemarks(tempcheck.getResPotency().getRemarks());
                    incidentPotency.setDateCreated(new Date());
                    incidentPotency.setCreatedBy(menuNavMBean.getUserSession().getUserId().toString());
                    incidentPotency.setDeleted(false);
                    incidentPotency.setUserSiteID("CGK"); //MASIH HARDCODE

                    incidentPotencyRepo.save(incidentPotency);
                    addPopUpMessage("Sukses", "Insiden Potensi berhasil");
                }
//                }
            } else {
                IncidentPotency incAss = new IncidentPotency();
                incAss = incidentPotencyRepo.findAllByIncidentAndPotency(inc, tempcheck.getResPotency());
                if (incAss != null) {
                    incidentPotencyRepo.delete(incAss);
                    addPopUpMessage("Sukses", "Insiden Potensi berhasil didelete");
                }
            }
        }
    }

    public void kirimNotifikasi() {

        try {
            if (listTempCheck.isEmpty()) {
                /*long count = listTempCheck.stream().filter(x -> x.getKirim() == true).count();
                System.out.println(count);
                if (count == 0) {
                }*/
                error2();
            } else {
                long count = listTempCheck.stream().filter(x -> x.getKirim()).count();
                if (count > 0) {
                    RequestContext.getCurrentInstance().execute("PF('kirim-notif-potensi').show()");
                } else {
                    error();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        try {
            for (int i = 0; i < listTempCheck.size(); i++) {
                potencyContact = new ResPotencyContact();
                if (listTempCheck.get(i).getKirim()) {
                    potencyContact = resPotencyContactRepo.findByPotencyIdDist(listTempCheck.get(i).resPotency.getPotencyid());
                    log.debug("Personel Name Kirim : " + potencyContact.getContactname());
                    if (kirimSms == true) {
                        gtwSmsOut = new GtwSmsOut();
                        System.out.println("gtwSmsOut.getSmsoutgoingid()= " + gtwSmsOut.getSmsoutgoingid());
                        if (gtwSmsOut.getSmsoutgoingid() == null) {
                            gtwSmsOut.setSmsoutgoingid(formatSmsoutgoingId());
                        }

                        gtwSmsOut.setRecipientname(potencyContact.getContactname());
                        gtwSmsOut.setPhonenumber(potencyContact.getPhonenumber2());
                        gtwSmsOut.setTextmessage(isisms);
                        gtwSmsOut.setDatecreated(date);
                        gtwSmsOut.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                        gtwSmsOut.setUsersiteid(listTempCheck.get(i).resPotency.getUsersiteid());
                        gtwSmsOut.setIsdeleted(BigInteger.ZERO);
                        gtwSmsOut.setPrioritas(BigInteger.ONE);
                        gtwSmsOutRepo.save(gtwSmsOut);
                        //gtwSmsOuts.add(gtwSmsOut);
                    }

                    if (kirimEmail == true) {
                        emailOut = new GtwEmailOut();
                        emailOut.setEmailoutgoingid(formatEmailOutId());
                        System.out.println("ID =      " + formatEmailOutId());
                        emailOut.setRecipientname(potencyContact.getContactname());
                        emailOut.setEmailaddress(potencyContact.getEmail());
                        emailOut.setSubject(subyekEmail);
                        emailOut.setTextmessage(isiEmail);
                        gtwEmailOutRepo.save(emailOut);

                        taskEmail = new TaskEmail();
                        taskEmail.setTaskName("Kirim Email dari Menu Incident >> Tab Potency");
                        taskEmail.setTaskType("prosia.task.sendemail.SendEmailFromTemplateTask");
                        taskEmail.setTaskTypeId(1);
                        JSONObject json = new JSONObject();
                        json.put("to", potencyContact.getEmail());
                        json.put("subyek", subyekEmail);
                        json.put("isi", isiEmail);
                        json.put("nama", potencyContact.getContactname());

                        taskEmail.setParameters(json.toString());
                        taskEmail.setStatus("new");
                        taskEmail.setTimeSubmitted(date);
                        taskEmails.add(taskEmail);

                    }
                }
            }

            if (kirimSms == true) {
//            gtwSmsOutRepo.save(gtwSmsOuts);
                addPopUpMessage(FacesMessage.SEVERITY_INFO,
                        "Sukses", "Sms berhasil dikirim");
            }

            if (kirimEmail == true) {
                taskEmailRepo.save(taskEmails);
                addPopUpMessage(FacesMessage.SEVERITY_INFO,
                        "Sukses", "Email berhasil dikirim");
            }

        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Gagal", "Notifikasi Gagal Dikirim");
        }

        refresh();
        RequestContext.getCurrentInstance().execute("PF('kirim-notif-potensi').hide()");
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

    public void initCoordinate() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void viewCoordinate() {
        if (resPotency.getLatitude() != null
                || StringUtils.isNotBlank(resPotency.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(resPotency.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (resPotency.getLongitude() != null
                || StringUtils.isNotBlank(resPotency.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(resPotency.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }

    public void error() {
        addPopUpMessage("Error","Pilih personil di kolom kirim");
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pilih personil di kolom kirim", null));
    }

    public void error2() {
        addPopUpMessage("Error","Tidak Ada Personil");
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tidak Ada Personil", null));
    }
}
