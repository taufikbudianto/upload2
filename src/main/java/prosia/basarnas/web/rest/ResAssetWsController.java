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
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.AssetTypeImages;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.repo.AssetTypeImageRepo;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.web.rest.dto.DataJsonError;
import prosia.basarnas.web.rest.dto.ListContactAssetWs;
import prosia.basarnas.web.rest.dto.ResAssetWs;

/**
 *
 * @author Taufik
 */
@RestController
public class ResAssetWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ResAssetRepo assetRepo;
    @Autowired
    private ResAssetContactRepo assetContactRepo;
    @Autowired
    private AssetTypeImageRepo assetTypeImg;
    @Value("${url.domain}")
    private String domain;

    @RequestMapping(value = "/asset", method = RequestMethod.GET)
    public ResponseEntity<?> getDataAsset(@RequestParam Map<String, String> mapParam) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user1 = (User) authentication.getPrincipal();
    String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();

        try {
            if(String.valueOf(userSite).equals("BSN")){
            List<ResAsset> assetList = assetRepo.findAll(whereQuery(mapParam));
            List<ResAssetWs> assetWsList = new ArrayList<>();
            if (assetList.isEmpty()) {
                DataJsonError error = new DataJsonError();
                error.setCode(ResponseCode.ZERO_RESULT);
                error.setMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<DataJsonError>(error, HttpStatus.NOT_FOUND);
            }
            for (ResAsset resAsset : assetList) {
                List<ResAssetContact> listAssetContact = assetContactRepo.findAllByAssetid(resAsset);
                List<ListContactAssetWs> listContactWs = new ArrayList<>();
                ResAssetWs resAssetWs = new ResAssetWs();
                resAssetWs.setAssetName(resAsset.getName());
                resAssetWs.setLatitude(resAsset.getLatitude());
                resAssetWs.setLongitude(resAsset.getLongitude());
                if(resAsset.getAssettypeid()!=null){
                    List<AssetTypeImages> listAssetImage= assetTypeImg.findByAssettypeidAndDeleted(resAsset.getAssettypeid(), false);
                    List<String> listPicture =new ArrayList<>();
                    for(AssetTypeImages assetImg : listAssetImage){
                        listPicture.add(domain+"sarcore/document/asset/"+assetImg.getImageID());
                    }
                    resAssetWs.setPicture(listPicture);
                }
                
                
                resAssetWs.setSarOffice(resAsset.getKantorsarid() != null
                        ? resAsset.getKantorsarid().getKantorsarname() : null);
                if (listAssetContact.isEmpty()) {
                    resAssetWs.setData(null);
                } else {
                    for (ResAssetContact resAssetContact : listAssetContact) {
                        ListContactAssetWs contactAsset = new ListContactAssetWs();
                        contactAsset.setPic(resAssetContact.getContactname());
                        contactAsset.setEmail(resAssetContact.getEmail());
                        contactAsset.setCellPhone(resAssetContact.getPhonenumber1());
                        listContactWs.add(contactAsset);
                    }
                    resAssetWs.setData(listContactWs);
                }
                assetWsList.add(resAssetWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(assetWsList);
            return ResponseEntity.ok(responseData);
            }
       else {
            List<ResAsset> assetList = assetRepo.findAllByKansarID(userSite);
            List<ResAssetWs> assetWsList = new ArrayList<>();
            if (assetList.isEmpty()) {
                DataJsonError error = new DataJsonError();
                error.setCode(ResponseCode.ZERO_RESULT);
                error.setMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<DataJsonError>(error, HttpStatus.NOT_FOUND);
            }
            for (ResAsset resAsset : assetList) {
                List<ResAssetContact> listAssetContact = assetContactRepo.findAllByAssetid(resAsset);
                List<ListContactAssetWs> listContactWs = new ArrayList<>();
                ResAssetWs resAssetWs = new ResAssetWs();
                resAssetWs.setAssetName(resAsset.getName());
                resAssetWs.setLatitude(resAsset.getLatitude());
                resAssetWs.setLongitude(resAsset.getLongitude());
                if(resAsset.getAssettypeid()!=null){
                    List<AssetTypeImages> listAssetImage= assetTypeImg.findByAssettypeidAndDeleted(resAsset.getAssettypeid(), false);
                    List<String> listPicture =new ArrayList<>();
                    for(AssetTypeImages assetImg : listAssetImage){
                        listPicture.add(domain+"sarcore/document/asset/"+assetImg.getImageID());
                    }
                    resAssetWs.setPicture(listPicture);
                }
                
                
                resAssetWs.setSarOffice(resAsset.getKantorsarid() != null
                        ? resAsset.getKantorsarid().getKantorsarname() : null);
                if (listAssetContact.isEmpty()) {
                    resAssetWs.setData(null);
                } else {
                    for (ResAssetContact resAssetContact : listAssetContact) {
                        ListContactAssetWs contactAsset = new ListContactAssetWs();
                        contactAsset.setPic(resAssetContact.getContactname());
                        contactAsset.setEmail(resAssetContact.getEmail());
                        contactAsset.setCellPhone(resAssetContact.getPhonenumber1());
                        listContactWs.add(contactAsset);
                    }
                    resAssetWs.setData(listContactWs);
                }
                assetWsList.add(resAssetWs);
            }
            ResponseData responseData = new ResponseData();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            responseData.setData(assetWsList);
            return ResponseEntity.ok(responseData);
            }
        } catch (Exception e) {
            DataJsonError error = new DataJsonError();
            error.setCode(ResponseCode.GENERAL_ERROR);
            error.setMessage("Error : " + e.getMessage());
            log.error("Error Ws : " + e.getMessage());
            return new ResponseEntity<DataJsonError>(error, HttpStatus.NOT_FOUND);
        }
    }

    private Specification<ResAsset> whereQuery(Map<String, String> mapFilter) {
        if (mapFilter.isEmpty()) {
            return null;
        } else {
            List<Predicate> predicates = new ArrayList<>();
            return new Specification<ResAsset>() {
                @Override
                public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    for (Map.Entry<String, String> map : mapFilter.entrySet()) {//
                        if (map.getKey().equals("pic")) {
                            // predicates.add(cb.like(cb.lower(root.<ResAssetContact>get("officeSar").<String>get("kantorsarname")), getLikePattern(map.getValue())));
                        }
                        if (map.getKey().equals("assetname")) {
                            predicates.add(cb.like(cb.lower(root.<String>get("name")), getLikePattern(map.getValue())));
                        }
                        if (map.getKey().equals("kansar")) {
                            predicates.add(cb.like(cb.lower(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarname")), getLikePattern(map.getValue())));
                        }
                    }
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
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
