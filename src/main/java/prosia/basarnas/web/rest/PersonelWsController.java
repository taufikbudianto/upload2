/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.CPNotifTanggapan;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.web.rest.dto.PersonelWs;

/**
 *
 * @author Taufik
 */
@RestController
public class PersonelWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResPersonnelRepo personnelRepo;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getDataProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataJsonResponse responseData = new DataJsonResponse();
        try {
            User user = (User) authentication.getPrincipal();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            ResPersonnel resPersonel = personnelRepo.findByPersonnelCode(user.getUsername());

            if (resPersonel == null) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            PersonelWs personelWs = new PersonelWs();
            personelWs.setPersonelId(resPersonel.getPersonnelID());
            personelWs.setPersonelNip(resPersonel.getPersonnelCode());
            personelWs.setPersonelName(resPersonel.getPersonnelName());
            personelWs.setEmail(resPersonel.getEmail());
            personelWs.setAgama(resPersonel.getReligion());
            personelWs.setAlamat(resPersonel.getHomeAddress());
            //personelWs.setDValid(personel.getValidDate());
            personelWs.setDivisi(resPersonel.getUnit()!=null?
                    resPersonel.getUnit().getEmploymentunitname():null);//belum
            personelWs.setKantorsar(resPersonel.getOfficeSar() != null
                    ? resPersonel.getOfficeSar().getKantorsarname() : null);
            personelWs.setJabatan(resPersonel.getStructuralPosition()!=null?
                    resPersonel.getStructuralPosition().getEmploymentpositionname():null);//belum
            personelWs.setNoKtp(resPersonel.getIKtp());
            //personelWs.setMulaiMasuk(personel.getTmtDate());
            personelWs.setNoTelp1(resPersonel.getPhoneNumber());
            personelWs.setNotelp2(resPersonel.getMobilePhoneNumber());
            //personelWs.setPassword(null);//belum
            personelWs.setGender(resPersonel.getGender());
            personelWs.setUsername(resPersonel.getPersonnelCode());//belum
            personelWs.setGolongan(resPersonel.getEmploymentClass()!=null?
                    resPersonel.getEmploymentClass().getEmploymentclassname():null);//belum
            personelWs.setNoPaspor(resPersonel.getPassportnumber());
            personelWs.setStatus(resPersonel.getStatus());
            //personelWs.setTempatLahir(personel.getBirthPlace());
            personelWs.setTanggalLahir(resPersonel.getBirthDate() != null
                    ? String.valueOf(resPersonel.getBirthDate()) : null);
            personelWs.setGolDarah(resPersonel.getBloodtype());
            //personelWs.setMulaiMasuk(personel.getTmtDate());

            responseData.setData(personelWs);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            log.error(e.getMessage());
            error.setResponseMessage(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }

    }
    
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ResponseEntity<?> saveDataProfile(@RequestBody PersonelWsController.DataJsonPersonnel dataJson) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataJsonResponse responseData = new DataJsonResponse();
        try {
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Edit Profile Succes");
            User user = (User) authentication.getPrincipal();
            ResPersonnel resPersonel = personnelRepo.findByPersonnelID(user.getResPersonnel().getPersonnelID());
            resPersonel.setHomeAddress(dataJson.getAddress());
            resPersonel.setMobilePhoneNumber(dataJson.getPhone());
            personnelRepo.save(resPersonel);
            return new ResponseEntity<Response>(succes, HttpStatus.OK);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }

    }
    
    @Data
    public static class DataJsonPersonnel implements Serializable {

        private String address;
        private String phone;

    }

    private Specification<ResPersonnel> whereQuery(Map<String, String> mapFilter) {
        if (mapFilter.isEmpty()) {
            return null;
        } else {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<ResPersonnel>() {
                @Override
                public Predicate toPredicate(Root<ResPersonnel> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    predicates.add(cb.like(cb.lower(root.<String>get("personnelCode")), getLikePattern("")));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
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

    @Data
    public static class DataJsonResponse extends Response implements Serializable {

        @JsonProperty(value = "data")
        private Object data;

    }

}
