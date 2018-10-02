/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.LogImagesRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.web.rest.dto.DataJsonError;
import prosia.basarnas.web.rest.dto.IncidentListWs;
import prosia.basarnas.web.rest.dto.IncidentListPictureWs;
import prosia.basarnas.web.rest.dto.KantorSarList;

/**
 *
 * @author PROSIA
 */
@RestController
public class IncidentListWsController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IncidentNationalRepo nationalRepo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private MstKantorSarRepo kantorsarRepo;
    @Autowired
    private LogImagesRepo logImg;
    @Autowired
    private IncidentLogRepo incidentLogRepo;
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

    @RequestMapping(value = "/incident", method = RequestMethod.GET)
    public ResponseEntity<?> getDataIncident(@RequestParam Map<String, String> mapParam) {
        Integer limit = 10;
        Integer page = 1;
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("limit")) {
                limit = Integer.valueOf(map.getValue());
            }
            if (map.getKey().equals("page")) {
                page = Integer.valueOf(map.getValue());
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        User user1 = (User) authentication.getPrincipal();
        String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();
        try {
            if(String.valueOf(userSite).equals("BSN")){
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<IncidentListWs> listWsIncident = new ArrayList<>();
            List<Incident> listIncident = incidentRepo.findAll(whereQuery(mapParam));
            //List<Incident> listIncident = incidentRepo.findAllByKansarID(userSite);
            //List<IncidentPersonnel> listIncidentPersonnel = incidentPersonelRepo.findAll(whereQuery(mapParam));
            if (listIncident.isEmpty()) {
                responseData.setResponseCode(ResponseCode.ZERO_RESULT);
                responseData.setResponseMessage(BodyUtil.X_RESPONSE_CODE);
                return new ResponseEntity<ResponseData>(responseData, HttpStatus.NOT_FOUND);
            }
            int no = 0;
            for (Incident incident : listIncident) {
                IncidentListWs incidentListws = new IncidentListWs();
                incidentListws.setIncidentid(incident.getIncidentid());
                incidentListws.setIncidentname(incident.getIncidentname());
                incidentListws.setIncidentnumber(incident.getIncidentnumber());
                incidentListws.setLocation(incident.getLocation());
                incidentListws.setLatitude(incident.getLatitude());
                incidentListws.setLongitude(incident.getLongitude());
                incidentListws.setIncidenttime(String.valueOf(incident.getDatecreated()));
                incidentListws.setGmt(incident.getAlertedattimezone());
                incidentListws.setIncidenttype(getStringTipeIncident(incident.getIncidenttype()));
                List<KantorSarList> listKantorSar = new ArrayList<>();

                if (incident.getIncidentScala() == null || incident.getIncidentScala() == 0) {
                    MstKantorSar kansar = kantorsarRepo.findByKantorsarid(incident.getUsersiteid());
                    if (kansar == null) {
                        incidentListws.setSarofficesar(null);

                    } else {
                        KantorSarList kansarList = new KantorSarList();
                        kansarList.setName(kansar.getKantorsarname() != null
                                ? kansar.getKantorsarname() : null);
                        kansarList.setAddress(kansar.getAddress() != null
                                ? kansar.getAddress() : null);
                        kansarList.setNumber(kansar.getPhonenumber1() != null
                                ? kansar.getPhonenumber1() : null);
                        listKantorSar.add(kansarList);
                        incidentListws.setSarofficesar(listKantorSar);
                    }

                } else {
                    List<IncidentNational> kantorSarNational = nationalRepo.findByIncident(incident);
                    if (kantorSarNational.isEmpty()) {
                        incidentListws.setSarofficesar(null);
                    } else {
                        for (IncidentNational national : kantorSarNational) {
                            MstKantorSar kansar = kantorsarRepo.findByKantorsarid(national.getKantorSarId());
                            KantorSarList kansarList = new KantorSarList();
                            kansarList.setName(kansar.getKantorsarname() != null
                                    ? kansar.getKantorsarname() : null);
                            kansarList.setAddress(kansar.getAddress() != null
                                    ? kansar.getAddress() : null);
                            kansarList.setNumber(kansar.getPhonenumber1() != null
                                    ? kansar.getPhonenumber1() : null);
                            listKantorSar.add(kansarList);

                        }
                        incidentListws.setSarofficesar(listKantorSar);
                    }
                }
                //Get images
                List<IncidentListPictureWs> listPicture = new ArrayList<>();
                List<IncidentLog> listIncidentLog = incidentLogRepo.findByIncidentId(incident.getIncidentid());
                if (listIncidentLog.isEmpty()) {

                } else {
                    for (IncidentLog incLog : listIncidentLog) {
                        List<LogImages> listLogImages = logImg.findByLogIDAndDeleted(incLog, false);
                        if (!listLogImages.isEmpty()) {
                            for (LogImages logImg : listLogImages) {
                                IncidentListPictureWs pict = new IncidentListPictureWs();//"picture" : "sarcore/document/log/1",
                                pict.setPicture(domain+"sarcore/document/log/"+logImg.getImageID());
                                listPicture.add(pict);
                            }

                        }
                    }
                }

                incidentListws.setPicturelist(listPicture);

                listWsIncident.add(incidentListws);
            }
            responseData.setData(listWsIncident);
            }
        else{
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<IncidentListWs> listWsIncident = new ArrayList<>();
            //List<Incident> listIncident = incidentRepo.findAll(whereQuery(mapParam));
            List<Incident> listIncident = incidentRepo.findAllByKansarID(userSite);
            //List<IncidentPersonnel> listIncidentPersonnel = incidentPersonelRepo.findAll(whereQuery(mapParam));
            if (listIncident.isEmpty()) {
                responseData.setResponseCode(ResponseCode.ZERO_RESULT);
                responseData.setResponseMessage(BodyUtil.X_RESPONSE_CODE);
                return new ResponseEntity<ResponseData>(responseData, HttpStatus.NOT_FOUND);
            }
            int no = 0;
            for (Incident incident : listIncident) {
                IncidentListWs incidentListws = new IncidentListWs();
                incidentListws.setIncidentid(incident.getIncidentid());
                incidentListws.setIncidentname(incident.getIncidentname());
                incidentListws.setIncidentnumber(incident.getIncidentnumber());
                incidentListws.setLocation(incident.getLocation());
                incidentListws.setLatitude(incident.getLatitude());
                incidentListws.setLongitude(incident.getLongitude());
                incidentListws.setIncidenttime(String.valueOf(incident.getDatecreated()));
                incidentListws.setGmt(incident.getAlertedattimezone());
                incidentListws.setIncidenttype(getStringTipeIncident(incident.getIncidenttype()));
                List<KantorSarList> listKantorSar = new ArrayList<>();

                if (incident.getIncidentScala() == null || incident.getIncidentScala() == 0) {
                    MstKantorSar kansar = kantorsarRepo.findByKantorsarid(incident.getUsersiteid());
                    if (kansar == null) {
                        incidentListws.setSarofficesar(null);

                    } else {
                        KantorSarList kansarList = new KantorSarList();
                        kansarList.setName(kansar.getKantorsarname() != null
                                ? kansar.getKantorsarname() : null);
                        kansarList.setAddress(kansar.getAddress() != null
                                ? kansar.getAddress() : null);
                        kansarList.setNumber(kansar.getPhonenumber1() != null
                                ? kansar.getPhonenumber1() : null);
                        listKantorSar.add(kansarList);
                        incidentListws.setSarofficesar(listKantorSar);
                    }

                } else {
                    List<IncidentNational> kantorSarNational = nationalRepo.findByIncident(incident);
                    if (kantorSarNational.isEmpty()) {
                        incidentListws.setSarofficesar(null);
                    } else {
                        for (IncidentNational national : kantorSarNational) {
                            MstKantorSar kansar = kantorsarRepo.findByKantorsarid(national.getKantorSarId());
                            KantorSarList kansarList = new KantorSarList();
                            kansarList.setName(kansar.getKantorsarname() != null
                                    ? kansar.getKantorsarname() : null);
                            kansarList.setAddress(kansar.getAddress() != null
                                    ? kansar.getAddress() : null);
                            kansarList.setNumber(kansar.getPhonenumber1() != null
                                    ? kansar.getPhonenumber1() : null);
                            listKantorSar.add(kansarList);

                        }
                        incidentListws.setSarofficesar(listKantorSar);
                    }
                }
                //Get images
                List<IncidentListPictureWs> listPicture = new ArrayList<>();
                List<IncidentLog> listIncidentLog = incidentLogRepo.findByIncidentId(incident.getIncidentid());
                if (listIncidentLog.isEmpty()) {

                } else {
                    for (IncidentLog incLog : listIncidentLog) {
                        List<LogImages> listLogImages = logImg.findByLogIDAndDeleted(incLog, false);
                        if (!listLogImages.isEmpty()) {
                            for (LogImages logImg : listLogImages) {
                                IncidentListPictureWs pict = new IncidentListPictureWs();//"picture" : "sarcore/document/log/1",
                                pict.setPicture(domain+"sarcore/document/log/"+logImg.getImageID());
                                listPicture.add(pict);
                            }

                        }
                    }
                }

                incidentListws.setPicturelist(listPicture);

                listWsIncident.add(incidentListws);
            }
            responseData.setData(listWsIncident);
            }
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response response = new Response();
            response.setResponseCode(ResponseCode.GENERAL_ERROR);
            response.setResponseMessage(e.getMessage());
            logger.error(e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }

    private Specification<Incident> whereQuery(Map<String, String> mapFilter) {
        if (mapFilter.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<Incident>() {
                @Override
                public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                    predicates.add(cb.equal(root.<String>get("status"), "Open"));
                    return andTogether(predicates, cb);
                }
            };
        } else {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<Incident>() {
                @Override
                public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    String incidentName = null;
                    String incidentNumber = null;
                    for (Map.Entry<String, String> map : mapFilter.entrySet()) {
                        if (map.getKey().equals("incident_name")) {
                            incidentName = map.getValue();
                        } else if (map.getKey().equals("incident_number")) {
                            incidentNumber = map.getValue();
                        }
                    }
                    if (incidentName != null) {
                        predicates.add(cb.like(cb.lower(root.<String>get("incidentname")), getLikePattern(incidentName)));

                    }
                    if (incidentNumber != null) {
                        predicates.add(cb.like(cb.lower(root.<String>get("incidentnumber")), getLikePattern(incidentNumber)));
                    }
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                    predicates.add(cb.equal(root.<String>get("status"), "Open"));
                    return andTogether(predicates, cb);
                }
            };
        }
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
    
    @RequestMapping(value = "/incident/count", method = RequestMethod.GET)
    public long countData(@RequestParam Map<String, String> mapParam) {
        
    return incidentRepo.count(whereQuery(mapParam));
    }
}
