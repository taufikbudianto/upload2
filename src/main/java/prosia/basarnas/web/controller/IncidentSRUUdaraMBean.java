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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.ResAssetRepo;
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
class IncidentSRUUdaraMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private ResAssetRepo resAssetRepo;
    private List<TempCheck> listTempCheck;
    private List<TempCheck> listSelectCheckbox;
    private IncidentAsset incidentAsset;
    private Incident inc;
    private Incident currIncident;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    private ResAsset resAsset;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private IncidentMBean incidentMBean;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        inc = new Incident();
        listSelectCheckbox = new ArrayList<>();
        currIncident = incidentMBean.getIncident();
        log.debug("CURR INCIDENT : " + currIncident);
        String id = null;
        if (currIncident != null) {
            id = currIncident.getIncidentid();
        }

        listTempCheck = new ArrayList<>();
        Double dist = 0.0;
        Double angle = 0.0;

        if (id != null) {
            inc = incidentRepo.findOne(id);
            List<ResAsset> resAssets = new ArrayList<>();
            if (currIncident.getLongitude() != null && currIncident.getLatitude() != null) {
                List<String> nearestKansar = new ArrayList<>();
                for (Object[] obj : incidentRepo.getNearestKansar(
                        inc.getLongitude(), inc.getLatitude())){
                    nearestKansar.add(obj[0].toString());
                }
                //resAssets= resAssetRepo.findSruNearestKansar("1", nearestKansar);
                resAssets = resAssetRepo.findAll(whereQuery());
            } else {
                resAssets = resAssetRepo.findAll(whereQuery());
            }

            for (ResAsset ra : resAssets) {
                TempCheck tempcheck = new TempCheck();
                if (ra.getLongitude() != null && ra.getLatitude() != null
                        && inc.getLongitude() != null && inc.getLatitude() != null) {
                    Map<String, Double> mapDisAngle = this.setDistanceAndAngle(ra.getLongitude(), ra.getLatitude(), inc.getLongitude(), inc.getLatitude());
                    dist = mapDisAngle.get("distance");
                    angle = mapDisAngle.get("angle");
                } else {
                    dist = 0.0;
                    angle = 0.0;
                }
                tempcheck.setDistance(dist);
                tempcheck.setAngle(angle);
                incidentAsset = incidentAssetRepo.findAllByIncidentAndAsset(inc, ra);
                if (incidentAsset != null) {
                    tempcheck.setPilih(true);
                } else {
                    incidentAsset = new IncidentAsset();
                    tempcheck.setPilih(false);
                }
                tempcheck.setKirim(false);

                tempcheck.setResAsset(ra);
                listTempCheck.add(tempcheck);

            }
        } else {
            resAsset = new ResAsset();
            incidentAsset = new IncidentAsset();
            List<ResAsset> resAssets = resAssetRepo.findAll(whereQuery());
            for (ResAsset ra : resAssets) {
                TempCheck tempcheck = new TempCheck();
                if (ra.getLongitude() != null && ra.getLatitude() != null) {
                    Map<String, Double> mapDisAngle = this.setDistanceAndAngle(ra.getLongitude(), ra.getLatitude(), "0", "0");
                    dist = mapDisAngle.get("distance");
                    angle = mapDisAngle.get("angle");
                } else {
                    dist = 0.0;
                    angle = 0.0;
                }
                tempcheck.setKirim(false);
                tempcheck.setPilih(false);
                tempcheck.setResAsset(ra);
                tempcheck.setDistance(dist);
                tempcheck.setAngle(angle);
                listTempCheck.add(tempcheck);
            }
        }
    }

    private Specification<ResAsset> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAsset>() {
            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<BigInteger>get("op_type"), BigInteger.valueOf(1))); //UDARA
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                query.orderBy(cb.asc(root.get("name")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public String formatIncidentAssetId() {
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
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
                log.debug("INCASSETID : " + incassetid);
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
                log.debug("INCASSETID : " + incassetid);
            }
        }
        return incassetid;

    }

    public void initialize(ResAsset resAsset) {
    }

    public void viewCheck(TempCheck tempcheck) {
        resAsset = new ResAsset();
        incidentAsset = new IncidentAsset();
        resAsset = resAssetRepo.findOne(tempcheck.getResAsset().getAssetid());
        initCoordinate();
        viewCoordinate();

        RequestContext.getCurrentInstance().reset("incidentdetail:incident-sru-tab:idviewsruudara");
        RequestContext.getCurrentInstance().update("incidentdetail:incident-sru-tab:idviewsruudara");
        RequestContext.getCurrentInstance().execute("PF('widgetViewSruUdara').show()");
    }

    public void action() {
        for (TempCheck tempcheck : listTempCheck) {
            if (tempcheck.getPilih()) {
                IncidentAsset incAss = new IncidentAsset();
                incidentAsset = new IncidentAsset();
                incAss = incidentAssetRepo.findAllByIncidentAndAsset(inc, tempcheck.getResAsset());
                //log.debug(tempcheck.resAsset.getAssetid() + " = " + tempcheck.pilih + " : " + tempcheck.kirim);
                //log.debug("ASSET TYPE : " + tempcheck.getResAsset().getAssettypeid());
//                incidentAsset = new IncidentAsset();
//                if (incidentAsset.getIncidentAssetID() == null) {

                if (incAss == null) {
                    incidentAsset.setIncidentAssetID(formatIncidentAssetId());
                    incidentAsset.setAssetType(tempcheck.getResAsset().getAssettypeid());
                    incidentAsset.setOfficeSar(tempcheck.getResAsset().getKantorsarid());
                    incidentAsset.setPosSar(tempcheck.getResAsset().getPossarid());
                    incidentAsset.setCode(tempcheck.getResAsset().getCode());
                    incidentAsset.setName(tempcheck.getResAsset().getName());
                    incidentAsset.setGoodQty(tempcheck.getResAsset().getGoodqty());
                    incidentAsset.setRejectedQty(tempcheck.getResAsset().getRejectedqty());
                    incidentAsset.setServicedQty(tempcheck.getResAsset().getServicedqty());
                    incidentAsset.setOtherQty(tempcheck.getResAsset().getOtherqty());
                    incidentAsset.setAssetCondition(tempcheck.getResAsset().getAssetcondition());
                    incidentAsset.setCargo(tempcheck.getResAsset().getCargo());
                    incidentAsset.setLongitude(tempcheck.getResAsset().getLongitude());
                    incidentAsset.setLatitude(tempcheck.getResAsset().getLatitude());
                    incidentAsset.setFunctional(tempcheck.getResAsset().getFunctional());
                    incidentAsset.setAsset(tempcheck.getResAsset());

                    incidentAsset.setIncident(inc);

//                  incidentAsset.setUsageQty(tempcheck.getResAsset().get);
//                  incidentAsset.setUsageDate(tempcheck.getResAsset().get);
//                  incidentAsset.setAssignmentDate(tempcheck.getResAsset().getA);
//                  incidentAsset.setAssignmentEndDate(tempcheck.getResAsset().getA);
                    incidentAsset.setDateCreated(new Date());
//                  incidentAsset.setLastModified();
//                  incidentAsset.setModifiedBy();
                    incidentAsset.setUserSiteID(tempcheck.getResAsset().getUsersiteid());
                    incidentAsset.setCreatedBy(menuNavMBean.getUserSession().getUsername());
                    incidentAsset.setDeleted(false);
                    incidentAsset.setStatus(tempcheck.getResAsset().getStatus());

                    incidentAsset.setEndurance(tempcheck.getResAsset().getEndurance());
                    incidentAsset.setSpeed(tempcheck.getResAsset().getSpeed());
                    incidentAsset.setVehicleType(tempcheck.getResAsset().getVehicletype());
                    incidentAsset.setRemarks(tempcheck.getResAsset().getRemarks());

//                log.debug("incident asset id " + incidentAsset.getIncidentAssetID());
//                log.debug("ASSET TYPE ID : " + tempcheck.getResAsset().getAssettypeid());
//                log.debug("Kantor Sar id : " + tempcheck.getResAsset().getKantorsarid());
//                log.debug(tempcheck.getResAsset().getPossarid());
//                log.debug(tempcheck.getResAsset().getCode());
//                log.debug(tempcheck.getResAsset().getName());
//                log.debug(tempcheck.getResAsset().getGoodqty());
//                log.debug(tempcheck.getResAsset().getRejectedqty());
//                log.debug(tempcheck.getResAsset().getServicedqty());
//                log.debug(tempcheck.getResAsset().getOtherqty());
//                log.debug(tempcheck.getResAsset().getAssetcondition());
//                log.debug(tempcheck.getResAsset().getCargo());
//                log.debug(tempcheck.getResAsset().getLongitude());
//                log.debug(tempcheck.getResAsset().getLatitude());
//                log.debug(tempcheck.getResAsset().getUsersiteid());
//                log.debug(menuNavMBean.getUserSession().getUsername());
//                log.debug(tempcheck.getResAsset().getStatus());
//                log.debug(tempcheck.getResAsset().getEndurance());
//                log.debug(tempcheck.getResAsset().getSpeed());
//                log.debug(tempcheck.getResAsset().getVehicletype());
//                log.debug(tempcheck.getResAsset().getRemarks());
//                log.debug(tempcheck.getResAsset());
                    incidentAssetRepo.save(incidentAsset);
                    addMessage("Sukses", "Incident Asset Udara berhasil disimpan dengan Asset : " + tempcheck.getResAsset().getName());
                }
//                }
            } else {
                IncidentAsset incAss = new IncidentAsset();
                incAss = incidentAssetRepo.findAllByIncidentAndAsset(inc, tempcheck.getResAsset());
                if (incAss != null) {
                    incidentAssetRepo.delete(incAss);
                    addMessage("Sukses", "Incident Asset Udara berhasil didelete (" + tempcheck.getResAsset().getName() + " )");
                }
            }
        }
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
        private ResAsset resAsset;
    }

    public void simpan(ResAsset ra) {
        IncidentAsset incAssId = new IncidentAsset();
        incAssId = incidentAssetRepo.findAllByIncidentAndAsset(inc, ra);
        if (incAssId != null) {
            resAssetRepo.save(ra);
            //incidentAsset.setDeleted(false);
            //incidentAsset.setCreatedBy(menuNavMBean.getUserSession().getUserId().toString());
            //incidentAsset.setDateCreated(new Date());
            //incidentAsset.setUserSiteID(ProsiaConstant.SITES);
            incAssId.setLastModified(new Date());
            incAssId.setModifiedBy(menuNavMBean.getUserSession().getUserId().toString());
            incAssId.setAssignmentDate(incidentAsset.getAssignmentDate());
            incAssId.setAssignmentEndDate(incidentAsset.getAssignmentEndDate());
            incidentAssetRepo.save(incAssId);

        } else {

            incidentAsset.setIncidentAssetID(formatIncidentAssetId());
            incidentAsset.setAssetType(ra.getAssettypeid());
            incidentAsset.setOfficeSar(ra.getKantorsarid());
            incidentAsset.setPosSar(ra.getPossarid());
            incidentAsset.setCode(ra.getCode());
            incidentAsset.setName(ra.getName());
            incidentAsset.setGoodQty(ra.getGoodqty());
            incidentAsset.setRejectedQty(ra.getRejectedqty());
            incidentAsset.setServicedQty(ra.getServicedqty());
            incidentAsset.setOtherQty(ra.getOtherqty());
            incidentAsset.setAssetCondition(ra.getAssetcondition());
            incidentAsset.setCargo(ra.getCargo());
            incidentAsset.setLongitude(ra.getLongitude());
            incidentAsset.setLatitude(ra.getLatitude());
            incidentAsset.setFunctional(ra.getFunctional());
            incidentAsset.setAsset(ra);
            incidentAsset.setIncident(inc);

//                  incidentAsset.setUsageQty(tempcheck.getResAsset().get);
//                  incidentAsset.setUsageDate(tempcheck.getResAsset().get);
            incidentAsset.setAssignmentDate(incidentAsset.getAssignmentDate());
            incidentAsset.setAssignmentEndDate(incidentAsset.getAssignmentEndDate());
            incidentAsset.setDateCreated(new Date());
//                  incidentAsset.setLastModified();
//                  incidentAsset.setModifiedBy();
            incidentAsset.setUserSiteID(ra.getUsersiteid());
            incidentAsset.setCreatedBy(menuNavMBean.getUserSession().getUserId().toString());
            incidentAsset.setDeleted(false);
            incidentAsset.setStatus(ra.getStatus());

            incidentAsset.setEndurance(ra.getEndurance());
            incidentAsset.setSpeed(ra.getSpeed());
            incidentAsset.setVehicleType(ra.getVehicletype());
            incidentAsset.setRemarks(ra.getRemarks());
            incidentAssetRepo.save(incidentAsset);
        }
        addMessage("Sukses", "Incident Asset berhasil disimpan");

        Double dist = 0.0;
        Double angle = 0.0;
        listTempCheck = new ArrayList<>();
        List<ResAsset> resAssets = resAssetRepo.findAll(whereQuery());
        for (ResAsset resA : resAssets) {
            TempCheck tempcheck = new TempCheck();
            if (resA.getLongitude() != null && resA.getLatitude() != null
                    && inc.getLongitude() != null && inc.getLatitude() != null) {
                Map<String, Double> mapDisAngle = this.setDistanceAndAngle(resA.getLongitude(), resA.getLatitude(), inc.getLongitude(), inc.getLatitude());
                dist = mapDisAngle.get("distance");
                angle = mapDisAngle.get("angle");
            } else {
                dist = 0.0;
                angle = 0.0;
            }
            tempcheck.setDistance(dist);
            tempcheck.setAngle(angle);

            incidentAsset = incidentAssetRepo.findAllByIncidentAndAsset(inc, resA);
            if (incidentAsset != null) {
                tempcheck.setPilih(true);
            } else {
                incidentAsset = new IncidentAsset();
                tempcheck.setPilih(false);
            }
            tempcheck.setKirim(false);

            tempcheck.setResAsset(resA);
            listTempCheck.add(tempcheck);

        }
        RequestContext.getCurrentInstance().execute("PF('widgetViewSruUdara').hide()");

    }

    public void initCoordinate() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void viewCoordinate() {
        if (resAsset.getLatitude() != null
                || StringUtils.isNotBlank(resAsset.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(resAsset.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (resAsset.getLongitude() != null
                || StringUtils.isNotBlank(resAsset.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(resAsset.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }
}
