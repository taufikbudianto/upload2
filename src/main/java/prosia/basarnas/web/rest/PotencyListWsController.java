/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.model.PotencyImages;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.PotencyImagesRepo;
import prosia.basarnas.web.rest.dto.PotencyListWs;
import prosia.basarnas.web.rest.dto.PotencyContactList;
import prosia.basarnas.web.rest.dto.PotencyPicturesListWs;

/**
 *
 * @author PROSIA
 */
@RestController
public class PotencyListWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResPotencyRepo potencyRepo;
    @Autowired
    private ResPotencyContactRepo potencyContactRepo;
    @Autowired
    private PotencyImagesRepo potencyImageRepo;
    @Value("${url.domain}")
    private String domain;

    @RequestMapping(value = "/potency", method = RequestMethod.GET)
    public ResponseEntity<?> getDataPotencyList(@RequestParam Map<String, String> mapParam) {
//        Integer limit=6000;
//        Integer page=1;
//        for(Map.Entry<String,String> map :mapParam.entrySet()){    
//            if(map.getKey().equals("limit")){
//                limit=Integer.valueOf(map.getValue());
//            }
//            if(map.getKey().equals("page")){
//                page=Integer.valueOf(map.getValue());
//            }
//        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        User user1 = (User) authentication.getPrincipal();
        String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();
        try {
          if(String.valueOf(userSite).equals("BSN")){
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<PotencyListWs> listWsPotency = new ArrayList<>();
            //Page<ResPotency> listPotency = potencyRepo.findAll(whereQuery(),new PageRequest(page,limit));
            List<ResPotency> listPotency = potencyRepo.findAll(whereQuery());
            //List<ResPotency> listPotency = potencyRepo.findAllByKansarID(userSite);
            if (listPotency.isEmpty()) {
                responseData.setResponseCode(ResponseCode.ZERO_RESULT);
                responseData.setResponseMessage(BodyUtil.X_RESPONSE_CODE);
                return new ResponseEntity<ResponseData>(responseData, HttpStatus.NOT_FOUND);
            }
            for (ResPotency potency : listPotency) {
                User user = (User) authentication.getPrincipal();
                PotencyListWs potencyListWs = new PotencyListWs();
                System.out.println(user.getResPersonnel().getOfficeSar().getKantorsarid());
                potencyListWs.setPotencyname(potency.getPotencyname());
                potencyListWs.setPotencyaddress(potency.getAddress());
                potencyListWs.setLatitude(potency.getLatitude());
                potencyListWs.setLongitude(potency.getLongitude());
                potencyListWs.setPotencyphonenumber(potency.getPhonenumber1());
                if (potency.getRegionid() != null) {
                    potencyListWs.setProvince(potency.getRegionid().getName());
                } else {
                    potencyListWs.setProvince(null);
                }
                List<PotencyContactList> Listcontact = new ArrayList<>();
                List<ResPotencyContact> listPotencyContact = potencyContactRepo.findAllBypotencyid(potency);
                for (ResPotencyContact potencyContact : listPotencyContact) {
                    PotencyContactList contact = new PotencyContactList();
                    contact.setPic(potencyContact.getContactname());
                    contact.setEmail(potencyContact.getEmail());
                    contact.setPotencycellphonenumber(potencyContact.getPhonenumber2());
                    Listcontact.add(contact);
                }
                potencyListWs.setContactList(Listcontact);

                List<PotencyPicturesListWs> listpicture = new ArrayList<>();
                List<PotencyImages> listPotencyimage = potencyImageRepo.findAllBypotencyid(potency);
                for (PotencyImages potencyImage : listPotencyimage) {
                    PotencyPicturesListWs picture = new PotencyPicturesListWs();
                    picture.setPicture(domain+"sarcore/document/potency/" + potencyImage.getImageID());
                    listpicture.add(picture);
                }
                potencyListWs.setPictureList(listpicture);

                listWsPotency.add(potencyListWs);
            }
            responseData.setData(listWsPotency);
            }
        else{
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<PotencyListWs> listWsPotency = new ArrayList<>();
            //Page<ResPotency> listPotency = potencyRepo.findAll(whereQuery(),new PageRequest(page,limit));
            //List<ResPotency> listPotency = potencyRepo.findAll(whereQuery());
            List<ResPotency> listPotency = potencyRepo.findAllByKansarID(userSite);
            if (listPotency.isEmpty()) {
                responseData.setResponseCode(ResponseCode.ZERO_RESULT);
                responseData.setResponseMessage(BodyUtil.X_RESPONSE_CODE);
                return new ResponseEntity<ResponseData>(responseData, HttpStatus.NOT_FOUND);
            }
            for (ResPotency potency : listPotency) {
                User user = (User) authentication.getPrincipal();
                PotencyListWs potencyListWs = new PotencyListWs();
                System.out.println(user.getResPersonnel().getOfficeSar().getKantorsarid());
                potencyListWs.setPotencyname(potency.getPotencyname());
                potencyListWs.setPotencyaddress(potency.getAddress());
                potencyListWs.setLatitude(potency.getLatitude());
                potencyListWs.setLongitude(potency.getLongitude());
                potencyListWs.setPotencyphonenumber(potency.getPhonenumber1());
                if (potency.getRegionid() != null) {
                    potencyListWs.setProvince(potency.getRegionid().getName());
                } else {
                    potencyListWs.setProvince(null);
                }
                List<PotencyContactList> Listcontact = new ArrayList<>();
                List<ResPotencyContact> listPotencyContact = potencyContactRepo.findAllBypotencyid(potency);
                for (ResPotencyContact potencyContact : listPotencyContact) {
                    PotencyContactList contact = new PotencyContactList();
                    contact.setPic(potencyContact.getContactname());
                    contact.setEmail(potencyContact.getEmail());
                    contact.setPotencycellphonenumber(potencyContact.getPhonenumber2());
                    Listcontact.add(contact);
                }
                potencyListWs.setContactList(Listcontact);

                List<PotencyPicturesListWs> listpicture = new ArrayList<>();
                List<PotencyImages> listPotencyimage = potencyImageRepo.findAllBypotencyid(potency);
                for (PotencyImages potencyImage : listPotencyimage) {
                    PotencyPicturesListWs picture = new PotencyPicturesListWs();
                    picture.setPicture(domain+"sarcore/document/potency/" + potencyImage.getImageID());
                    listpicture.add(picture);
                }
                potencyListWs.setPictureList(listpicture);

                listWsPotency.add(potencyListWs);
            }
            responseData.setData(listWsPotency);
            }
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setResponseCode(ResponseCode.GENERAL_ERROR);
            responseData.setResponseMessage(e.getMessage());
            log.error(e.getMessage());
            return new ResponseEntity<ResponseData>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    private Specification<ResPotency> whereQuery() {

        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResPotency>() {
            @Override
            public Predicate toPredicate(Root<ResPotency> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }
}
