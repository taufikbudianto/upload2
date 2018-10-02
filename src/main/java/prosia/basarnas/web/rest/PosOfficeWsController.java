/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.MstPosSarContact;
import prosia.basarnas.model.MstPosSarImages;
import prosia.basarnas.repo.MstKantorSarImagesRepo;
import prosia.basarnas.repo.MstPosSarContactRepo;
import prosia.basarnas.repo.MstPosSarImagesRepo;
import prosia.basarnas.repo.MstPosSarRepo;
import prosia.basarnas.web.rest.dto.PosOfficeWs;

/**
 *
 * @author Taufik
 */
@RestController
public class PosOfficeWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MstPosSarRepo posSarRepo;

    @Autowired
    private MstPosSarContactRepo possarContactRepo;

    @Autowired
    private MstPosSarImagesRepo posImg;

    @Value("${url.domain}")
    private String domain;

    @RequestMapping(value = "/sarpost", method = RequestMethod.GET)
    public ResponseEntity<?> getDataPosOffice(@RequestParam Map<String, String> mapParam) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user1 = (User) authentication.getPrincipal();
    String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();
        
        try {
            if(userSite.equals("BSN")){
            List<MstPosSar> listPosSar = posSarRepo.findAll(whereQuery(mapParam));
            //List<MstPosSar> listPosSar = posSarRepo.findAllByKansarID(userSite);
            List<PosOfficeWs> listPosSarWs = new ArrayList<>();
            if (listPosSar.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (MstPosSar mstPosSar : listPosSar) {
                PosOfficeWs posOfficeWs = new PosOfficeWs();
                posOfficeWs.setOfficename(mstPosSar.getPossarname());
                posOfficeWs.setLatitude(mstPosSar.getLatitude());
                posOfficeWs.setLongitude(mstPosSar.getLongitude());
                posOfficeWs.setOfficecode(mstPosSar.getKantorsarid() == null
                        ? null : mstPosSar.getKantorsarid().getKantorsarname());
                List<PosOfficeWs.ContactList> listContactPossarWs = new ArrayList<>();
                for (MstPosSarContact contact : possarContactRepo.findByPossarid(mstPosSar)) {
                    PosOfficeWs.ContactList contactList = new PosOfficeWs.ContactList();
                    contactList.setPic(contact.getContactname());
                    contactList.setEmail(contact.getEmail());
                    contactList.setCellphone_number(contact.getPhonenumber1());
                    listContactPossarWs.add(contactList);
                }

                posOfficeWs.setContactList(listContactPossarWs);
                List<MstPosSarImages> listImgPosar = posImg.findByPosSarAndDeleted(mstPosSar, false);
                List<String>listStrImg = new ArrayList<>();
                for(MstPosSarImages possarImg : listImgPosar){
                    listStrImg.add(domain+"sarcore/document/pos/"+possarImg.getImageID());
                }
                posOfficeWs.setPicture(null);
                listPosSarWs.add(posOfficeWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(listPosSarWs);
            return ResponseEntity.ok(responseData);
            }
        else{
            List<MstPosSar> listPosSar = posSarRepo.findAllByKansarID(userSite);
            List<PosOfficeWs> listPosSarWs = new ArrayList<>();
            if (listPosSar.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (MstPosSar mstPosSar : listPosSar) {
                PosOfficeWs posOfficeWs = new PosOfficeWs();
                posOfficeWs.setOfficename(mstPosSar.getPossarname());
                posOfficeWs.setLatitude(mstPosSar.getLatitude());
                posOfficeWs.setLongitude(mstPosSar.getLongitude());
                posOfficeWs.setOfficecode(mstPosSar.getKantorsarid() == null
                        ? null : mstPosSar.getKantorsarid().getKantorsarname());
                List<PosOfficeWs.ContactList> listContactPossarWs = new ArrayList<>();
                for (MstPosSarContact contact : possarContactRepo.findByPossarid(mstPosSar)) {
                    PosOfficeWs.ContactList contactList = new PosOfficeWs.ContactList();
                    contactList.setPic(contact.getContactname());
                    contactList.setEmail(contact.getEmail());
                    contactList.setCellphone_number(contact.getPhonenumber1());
                    listContactPossarWs.add(contactList);
                }

                posOfficeWs.setContactList(listContactPossarWs);
                List<MstPosSarImages> listImgPosar = posImg.findByPosSarAndDeleted(mstPosSar, false);
                List<String>listStrImg = new ArrayList<>();
                for(MstPosSarImages possarImg : listImgPosar){
                    listStrImg.add(domain+"sarcore/document/pos/"+possarImg.getImageID());
                }
                posOfficeWs.setPicture(null);
                listPosSarWs.add(posOfficeWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(listPosSarWs);
            return ResponseEntity.ok(responseData); 
            }
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Error : " + e.getMessage());
            log.error(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
        }
    }

    private Specification<MstPosSar> whereQuery(Map<String, String> mapFilter) {
        if (mapFilter.isEmpty()) {
            return null;
        } else {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<MstPosSar>() {
                @Override
                public Predicate toPredicate(Root<MstPosSar> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    for (Map.Entry<String, String> map : mapFilter.entrySet()) {

                    }
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
}
