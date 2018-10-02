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
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstKantorSarContact;
import prosia.basarnas.model.MstKantorSarImages;
import prosia.basarnas.repo.MstKantorSarContactRepo;
import prosia.basarnas.repo.MstKantorSarImagesRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.web.rest.dto.ContactListMstOfficeWs;
import prosia.basarnas.web.rest.dto.OfficeWs;

/**
 *
 * @author Taufik
 */
@RestController
public class OfficeSarWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MstKantorSarRepo kantorSarRepo;

    @Autowired
    private MstKantorSarContactRepo kantorsarContactRepo;
    @Autowired
    private MstKantorSarImagesRepo officeImg;
    @Value("${url.domain}")
    private String domain;

    @RequestMapping(value = "/office", method = RequestMethod.GET)
    public ResponseEntity<?> getDataOffice(@RequestParam Map<String, String> mapParam) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user1 = (User) authentication.getPrincipal();
    String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();

        try {
            if(String.valueOf(userSite).equals("BSN")){
            List<MstKantorSar> listKantorSar = kantorSarRepo.findAll(whereQuery(mapParam));
            List<OfficeWs> listOfficeWs = new ArrayList<>();
            if (listKantorSar.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (MstKantorSar mstSar : listKantorSar) {
                OfficeWs officeWs = new OfficeWs();
                officeWs.setOfficeCode(mstSar.getKantorsarid());
                officeWs.setLatitude(mstSar.getLatitude());
                officeWs.setLongitude(mstSar.getLongitude());
                officeWs.setOfficeAddress(mstSar.getAddress());
                officeWs.setOfficeName(mstSar.getKantorsarname());
                officeWs.setPhoneNumber(mstSar.getPhonenumber1());
                officeWs.setPic(mstSar.getContactname());
                List<MstKantorSarImages> listImageKansar = officeImg.findByKanSarAndDeleted(mstSar, false);
                List<String> listKansarImage = new ArrayList<>();
                for (MstKantorSarImages mstKansarImg : listImageKansar) {
                    listKansarImage.add(domain + "sarcore/document/office/" + mstKansarImg.getImageID());
                }
                officeWs.setPicture(listKansarImage);
                List<ContactListMstOfficeWs> listMstOffice = new ArrayList<>();
                // System.out.println(kantorsarContactRepo.findByKantorsarid(mstSar).size());
                for (MstKantorSarContact contact : kantorsarContactRepo.findByKantorsarid(mstSar)) {
                    ContactListMstOfficeWs contactOff = new ContactListMstOfficeWs();
                    contactOff.setName(contact.getName());
                    contactOff.setEmail(contact.getEmail());
                    contactOff.setNumber(contact.getPhonenumber1());
                    contactOff.setPosition(contact.getPosition());
                    listMstOffice.add(contactOff);
                }
                officeWs.setContact(listMstOffice);
                listOfficeWs.add(officeWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(listOfficeWs);
            return ResponseEntity.ok(responseData);
            }
        else{
            List<MstKantorSar> listKantorSar = kantorSarRepo.findAllByKansarID(userSite);
            List<OfficeWs> listOfficeWs = new ArrayList<>();
            if (listKantorSar.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (MstKantorSar mstSar : listKantorSar) {
                OfficeWs officeWs = new OfficeWs();
                officeWs.setOfficeCode(mstSar.getKantorsarid());
                officeWs.setLatitude(mstSar.getLatitude());
                officeWs.setLongitude(mstSar.getLongitude());
                officeWs.setOfficeAddress(mstSar.getAddress());
                officeWs.setOfficeName(mstSar.getKantorsarname());
                officeWs.setPhoneNumber(mstSar.getPhonenumber1());
                officeWs.setPic(mstSar.getContactname());
                List<MstKantorSarImages> listImageKansar = officeImg.findByKanSarAndDeleted(mstSar, false);
                List<String> listKansarImage = new ArrayList<>();
                for (MstKantorSarImages mstKansarImg : listImageKansar) {
                    listKansarImage.add(domain + "sarcore/document/office/" + mstKansarImg.getImageID());
                }
                officeWs.setPicture(listKansarImage);
                List<ContactListMstOfficeWs> listMstOffice = new ArrayList<>();
                // System.out.println(kantorsarContactRepo.findByKantorsarid(mstSar).size());
                for (MstKantorSarContact contact : kantorsarContactRepo.findByKantorsarid(mstSar)) {
                    ContactListMstOfficeWs contactOff = new ContactListMstOfficeWs();
                    contactOff.setName(contact.getName());
                    contactOff.setEmail(contact.getEmail());
                    contactOff.setNumber(contact.getPhonenumber1());
                    contactOff.setPosition(contact.getPosition());
                    listMstOffice.add(contactOff);
                }
                officeWs.setContact(listMstOffice);
                listOfficeWs.add(officeWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(listOfficeWs);
            return ResponseEntity.ok(responseData);
            }
            
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Error : " + e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
        }
        
    }

    private Specification<MstKantorSarContact> whereQuery2(final String data) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstKantorSarContact>() {
            @Override
            public Predicate toPredicate(Root<MstKantorSarContact> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                // if("kansarname".equals(root))
                predicates.add(cb.like(cb.lower(root.<String>get("name")), getLikePattern(data)));
                return andTogether(predicates, cb);
            }

        };
    }

    private Specification<MstKantorSar> whereQuery(Map<String, String> mapFilter) {
        if (mapFilter.isEmpty()) {
            return null;
        } else {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<MstKantorSar>() {
                @Override
                public Predicate toPredicate(Root<MstKantorSar> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    for (Map.Entry<String, String> map : mapFilter.entrySet()) {
                        if (map.getKey().equals("r")) {
                            // predicates.add(cb.like(cb.lower(root.<ResAssetContact>get("officeSar").<String>get("kantorsarname")), getLikePattern(map.getValue())));
                        }
                        if (map.getKey().equals("office_name")) {
                            predicates.add(cb.like(cb.lower(root.<String>get("kantorsarname")), getLikePattern(map.getValue())));
                        }
                        if (map.getKey().equals("office_code")) {
                            predicates.add(cb.like(cb.lower(root.<String>get("kantorsarid")), getLikePattern(map.getValue())));
                        }
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
