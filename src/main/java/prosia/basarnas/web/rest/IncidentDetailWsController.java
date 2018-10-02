/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.DocFlavor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentPOB;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.model.IncidentPotency;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.model.ResPersonnelImages;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelImages;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.IncidentPotencyRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentTabPOBRepo;
import prosia.basarnas.repo.LogImagesRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.repo.ResPersonnelImagesRepo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.web.rest.dto.IncidentDetailWs;
import prosia.basarnas.web.rest.dto.IncidentListPictureWs;
import prosia.basarnas.web.rest.dto.IncidentWs;
import prosia.basarnas.web.rest.dto.PersonelWs;
import prosia.basarnas.web.util.SightingConverter;

/**
 *
 * @author Taufik AB
 */
@RestController
public class IncidentDetailWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private IncidentPersonnelRepo incPersonelRepo;

    @Autowired
    private SightingAndHearingRepo sightingRepo;

    @Autowired
    private MstKantorSarRepo kansarRepo;

    @Autowired
    private ResAssetRepo resAssetRepo;

    @Autowired
    private IncidentAssetRepo assetRepo;
    @Autowired
    private IncidentPotencyRepo incPotencyRepo;
    @Autowired
    private IncidentLogRepo incidentLogRepo;
    @Autowired
    private IncidentTabPOBRepo incidentTabPOBRepo;
    @Autowired
    private ResPersonnelImagesRepo resPersonnelImagesRepo;
    @Value("${url.domain}")
    private String domain2;

    @Autowired
    private LogImagesRepo logImg;
    @Value("${url.domain}")
    private String domain;
    
    public String getStringTipeIncident(Integer incidentType) {
        String string = "";
        if (incidentType != null) {
            switch (incidentType) {
                case 1:
                    string = ProsiaConstant.KECELAKAAN_LAUT;
                    break;
                case 2:
                    string = ProsiaConstant.KECELAKAAN_UDARA;
                    break;
                case 3:
                    string = ProsiaConstant.MUSIBAH;
                    break;
                case 4:
                    string = ProsiaConstant.BENCANAA;
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    @RequestMapping(value = "/incident/{incidentid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataIncidentDetail(@PathVariable String incidentid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        try {
            Incident inc = incidentRepo.findAllByIncidentid(incidentid);
            if (inc == null) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            List<IncidentDetailWsController> listIncidentWs = new ArrayList<>();

            IncidentDetailWs incWs = new IncidentDetailWs();
            incWs.setIncedentName(inc.getIncidentname());
            incWs.setIncidentNumber(inc.getIncidentnumber());
            incWs.setIncidentTime(String.valueOf(inc.getAlertedat()));
            incWs.setSarOffice(inc.getUsersiteid() != null
                    ? kansarRepo.findByKantorsarid(inc.getUsersiteid()).getKantorsarname() : null);
            IncidentDetailWs.General general = new IncidentDetailWs.General();
            general.setCellPhone(inc.getPhonenumber());
            general.setCloseDate(String.valueOf(inc.getClosedate()));
            //general.setCloseOpsDate(String.valueOf(inc.get));
            general.setDeathNumber(inc.getDeathpeople());
            general.setEmail(inc.getOwneremail());
            general.setIncidenttype(getStringTipeIncident(inc.getIncidenttype()));
            general.setHeavyInjuredNumber(inc.getHeavyinjuredpeople());
            general.setIncidentNumber(inc.getIncidentnumber());
            general.setIncidentScala(String.valueOf(inc.getIncidentScala()));
            general.setLatitude(inc.getLatitude());
            general.setLightInjuredNumber(inc.getLightinjuredpeople());
            general.setLocation(inc.getLocation());
            general.setLongitude(inc.getLongitude());
            general.setLostNumber(inc.getLostpeople());
            general.setObjectCallSign(inc.getObjectcallsign());
            general.setObjectColor(inc.getObjectcolor());
            general.setObjectHomeBase(inc.getObjecthomebase());
            general.setObjectLength(inc.getObjectlength());
            general.setOwnerAddres(inc.getOwneraddress());
            general.setOwnerFax(inc.getOwnerfax());
            general.setOwnerName(inc.getOwnername());
            general.setPhoneNumber(inc.getOwnerphone());
            general.setSarOffice(inc.getUsersiteid() != null
                    ? kansarRepo.findByKantorsarid(inc.getUsersiteid()).getKantorsarname() : null);
            general.setSmc(inc.getSmc());
            general.setStartDate(String.valueOf(inc.getStartdate()));
            general.setStartOpsDate(String.valueOf(inc.getStartopsdate()));
            general.setSurvivornumber(inc.getSurvivedpeople());
            //general.setTimeZoneEndOpsDate(inc.getClosedatetimezone());
            general.setTimeZoneStartOpsDate(inc.getStartopsdatetimezone());
            general.setTimezoneEndDate(inc.getClosedatetimezone());
            general.setTimezoneStartDate(inc.getStartdatetimezone());
            incWs.setGeneralIncident(general);

            //SightingHearing
            List<UtiSighting> listUtiSighting = sightingRepo.findByIncident(inc);
            List<IncidentDetailWs.SightingHearing> listSightingWs = new ArrayList<>();
            for (UtiSighting uti : listUtiSighting) {
                IncidentDetailWs.SightingHearing sh = new IncidentDetailWs.SightingHearing();
                sh.setDate(String.valueOf(uti.getObjectDtg()));
                sh.setDescription(uti.getRemarks());
                sh.setLatitude(uti.getObjectLatitude());
                sh.setLongitude(uti.getObjectLongitude());
                sh.setLocation(uti.getObjectPosition());
                sh.setObject(uti.getObjectType() != null
                        ? SightingConverter.objectTypeToString(uti.getObjectType()) : null);
                sh.setStatus(inc.getStatus() != null
                        ? SightingConverter.getStatusString(uti.getStatus()) : null);
                listSightingWs.add(sh);
            }
            incWs.setSightingHearing(listSightingWs);

            //SRU
            List<String> nearestKansar = new ArrayList<>();
            if (StringUtils.isNotBlank(inc.getLongitude()) && (StringUtils.isNotBlank(inc.getLatitude()))) {

                for (Object[] obj : incidentRepo.getNearestKansar(inc.getLongitude(), inc.getLatitude())) {

                    nearestKansar.add(obj[0].toString());
                }
            }
            List<IncidentAsset> listAsset = assetRepo.findAll(whereQueryIncAsset(inc, "sru"));
            List<IncidentDetailWs.Sru> listAssetSru = new ArrayList<>();
            try {
                Double.valueOf(inc.getLongitude());
                Double.valueOf(inc.getLatitude());
            } catch (Exception e) {
                inc.setLongitude(null);
                inc.setLatitude(null);
            }

            for (IncidentAsset assetSRU : listAsset) {

                IncidentDetailWs.Sru sru = new IncidentDetailWs.Sru();
                sru.setName(assetSRU.getName());
                sru.setTanggal(String.valueOf(assetSRU.getDateCreated()));
                sru.setEndurance(String.valueOf(assetSRU.getAsset().getEndurance()));
                sru.setKecepatan(String.valueOf(assetSRU.getAsset().getSpeed()));
                sru.setPotency_name(assetSRU.getAsset().getPotencyid() != null
                        ? assetSRU.getAsset().getPotencyid().getPotencyname() : null);
                try {
                    Double.valueOf(assetSRU.getLongitude());
                    Double.valueOf(assetSRU.getLatitude());
                } catch (Exception e) {
                    assetSRU.setLongitude(null);
                    assetSRU.setLatitude(null);
                }
                if (assetSRU.getLongitude() != null && assetSRU.getLatitude() != null
                        && inc.getLongitude() != null && inc.getLatitude() != null) {
                    Map<String, Double> mapDisAngle = setDistanceAndAngle(assetSRU.getLongitude(), assetSRU.getLatitude(), inc.getLongitude(), inc.getLatitude());
                    sru.setDistance(String.valueOf(mapDisAngle.get("distance")));//in NM
                    sru.setAngle(String.valueOf(mapDisAngle.get("angle")));
                } else {
                    sru.setDistance(null);
                    sru.setAngle(null);
                }
                if (null == assetSRU.getAsset().getOp_type()) {
                    sru.setAsset_type(null);
                } else {
                    switch (assetSRU.getAsset().getOp_type()) {
                        case 0:
                            sru.setAsset_type("SRU Darat");
                            break;
                        case 1:
                            sru.setAsset_type("SRU Udara");
                            break;
                        case 2:
                            sru.setAsset_type("SRU Laut");
                            break;
                        default:
                            sru.setAsset_type("SRU");
                            break;
                    }
                }

                listAssetSru.add(sru);
            }
            incWs.setSru(listAssetSru);

            //Resource
            IncidentDetailWs.Resource resource = new IncidentDetailWs.Resource();
            List<IncidentPersonnel> listPersonel = incPersonelRepo.findAllByIncident(inc);
            List<IncidentDetailWs.Personel> listPersonelWs = new ArrayList<>();
            for (IncidentPersonnel personel : listPersonel) {
                IncidentDetailWs.Personel personelWs = new IncidentDetailWs.Personel();
                personelWs.setCode(personel.getPersonnelCode());
                personelWs.setName(personel.getPersonnelName());
                personelWs.setTanggal(String.valueOf(personel.getDateCreated()));
                personelWs.setOffice_name(personel.getPersonnel() != null
                        ? personel.getPersonnel().getOfficeSar().getKantorsarname() : null);
                personelWs.setEmployer_class(personel.getPersonnel() != null
                        ? personel.getPersonnel().getEmploymentClass().getEmploymentclassname() : null);
                ResPersonnelImages listPersonnelImages = resPersonnelImagesRepo.findImageByPersonnelIDAndDeleted(personel.getPersonnel().getPersonnelID());
             if (listPersonnelImages !=null){
                    personelWs.setImage(domain + "sarcore/document/imagepersonnel/" + listPersonnelImages.getImageID()); 
                } else{
                    personelWs.setImage(null);
                }  
                listPersonelWs.add(personelWs);
            }
            List<IncidentAsset> listIncidentAsset = assetRepo.findAll(whereQueryIncAsset(inc, "noSru"));
            List<IncidentDetailWs.Asset> listIncAsset = new ArrayList<>();
            for (IncidentAsset incAsset : listIncidentAsset) {
                IncidentDetailWs.Asset incAssetWs = new IncidentDetailWs.Asset();
                incAssetWs.setAsset_name(incAsset.getName());
                incAssetWs.setOffice_name(incAsset.getOfficeSar() != null
                        ? incAsset.getOfficeSar().getKantorsarname() : null);
                incAssetWs.setQuantity(incAsset.getUsageQty());
                listIncAsset.add(incAssetWs);
            }
            List<IncidentPotency> listIncPotency = incPotencyRepo.findByIncidentAndDeleted(inc, false);
            List<IncidentDetailWs.Potency> listPotencyWs = new ArrayList<>();
            for (IncidentPotency potensi : listIncPotency) {
                IncidentDetailWs.Potency potensiWs = new IncidentDetailWs.Potency();
                potensiWs.setPotency_name(potensi.getPotencyName());
                potensiWs.setOffice_name(potensi.getOfficeSar() != null
                        ? potensi.getOfficeSar().getKantorsarname() : null);
                potensiWs.setPhone_number(potensi.getPhoneNumber1());
                potensiWs.setPotency_type(String.valueOf(potensi.getPotencyType()));
                listPotencyWs.add(potensiWs);
            }
            resource.setAsset(listIncAsset);
            resource.setPersonnel(listPersonelWs);
            resource.setPotency(listPotencyWs);
            incWs.setResource(resource);

            //Log
            List<IncidentDetailWs.LogIncident> listLogInc = new ArrayList<>();
            List<IncidentLog> listLog = incidentLogRepo.findByIncidentAndDeleted(inc, false);
            for (IncidentLog incLog : listLog) {
                List<IncidentListPictureWs> listPicture = new ArrayList<>();
                IncidentDetailWs.LogIncident logIncidentWs = new IncidentDetailWs.LogIncident();
                logIncidentWs.setContent(incLog.getRemarks());
                logIncidentWs.setDate(String.valueOf(sdf.format(incLog.getLogDate())));
                logIncidentWs.setInitiator(incLog.getPersonnel() != null
                        ? incLog.getPersonnel().getPersonnelName() : null);
                logIncidentWs.setLatitude(incLog.getLatitude());
                logIncidentWs.setLongitude(incLog.getLongitude());
                logIncidentWs.setSubject_log(incLog.getSubject());
                logIncidentWs.setTime(String.valueOf(sdfTime.format(incLog.getLogDate())));
                List<LogImages> listLogImages = logImg.findByLogIDAndDeleted(incLog, false);
                if (!listLogImages.isEmpty()) {
                    for (LogImages logImg : listLogImages) {
                        IncidentListPictureWs pict = new IncidentListPictureWs();//"picture" : "sarcore/document/log/1",
                        pict.setPicture(domain + "sarcore/document/log/" + logImg.getImageID());
                        listPicture.add(pict);
                    }

                }
                logIncidentWs.setPicture(listPicture);
                listLogInc.add(logIncidentWs);
            }
            incWs.setLog(listLogInc);
          
            //POB
            List<IncidentPOB> listIncPob = incidentTabPOBRepo.findByIncidentAndIsdeleted(inc, new BigInteger("0"));
            List<IncidentDetailWs.Pob> listIncPobWs = new ArrayList<>();
            for (IncidentPOB incPob : listIncPob) {
                IncidentDetailWs.Pob pobWs = new IncidentDetailWs.Pob();
                pobWs.setAge(incPob.getAge());
                pobWs.setCondition(incPob.getCondition());
                pobWs.setCountry(incPob.getCountry() != null
                        ? incPob.getCountry().getCountryName() : null);
                pobWs.setGender(incPob.getGender());
                pobWs.setName(incPob.getName());
                listIncPobWs.add(pobWs);
            }
            incWs.setPob(listIncPobWs);
            //resource.s
            DataJsonResponse responseData = new DataJsonResponse();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(incWs);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Error : " + e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
        }
    }

    private Specification<Incident> whereQuery(final String id) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Incident>() {
            @Override
            public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                predicates.add(cb.like(cb.lower(root.<String>get("incidentid")), getLikePattern(id)));

                return andTogether(predicates, cb);
            }

        };

    }

    private Specification<ResAsset> whereQueryAsset(List<String> nearesKansar) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAsset>() {
            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {//MKW
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), "PGK"));
                //predicates.add(cb.greaterThan(cb.size(root.get("")),10));
//                for(String kansarId : nearesKansar){
//                    System.out.println("KansarId : "+kansarId);
//                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), kansarId));
//                    return orTogether(predicates, cb);
//                }
                return andTogether(predicates, cb);
            }

        };

    }

    private Specification<IncidentAsset> whereQueryIncAsset(Incident inc, String sru) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<IncidentAsset>() {
            @Override
            public Predicate toPredicate(Root<IncidentAsset> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {//MKW
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));//incident
                if (sru.equals("noSru")) {
                    predicates.add(cb.notEqual(root.<MstAssetType>get("assetType").<Integer>get("issru"), 0));
                } else if (sru.equals("sru")) {
                    predicates.add(cb.equal(root.<MstAssetType>get("assetType").<Integer>get("issru"), 0));
                }

                predicates.add(cb.equal(root.<Incident>get("incident"), inc));

                //predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), "PGK"));
                //predicates.add(cb.greaterThan(cb.size(root.get("")),10));
//                for(String kansarId : nearesKansar){
//                    System.out.println("KansarId : "+kansarId);
//                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), kansarId));
//                    return orTogether(predicates, cb);
//                }
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

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    @Data
    public static class DataJsonResponse extends Response implements Serializable {

        @JsonProperty(value = "data")
        private Object data;

    }

    protected Map<String, Double> setDistanceAndAngle(String long1, String lat1, String long2, String lat2) {
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
}
