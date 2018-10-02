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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.basarnas.model.KritikdanSaran;
import prosia.basarnas.model.ResPersonnel;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.repo.KritikdanSaranRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.web.rest.dto.DataJsonError;
import prosia.basarnas.web.rest.dto.KritikdanSaranWs;
/**
 *
 * @author PROSIA
 */
@RestController
public class KritikdanSaranWsController {
    
    @Autowired
    private KritikdanSaranRepo kritikdansaranRepo;
    @Autowired
    private ResPersonnelRepo resPersonnelRepo;
    
    @RequestMapping(value = "/kritikdansaran", method = RequestMethod.GET)
    public ResponseEntity<?> getDataKritikSaran(@RequestParam Map<String, String> mapParam){
        
        ResponseData responseData = new ResponseData();
        try {
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<KritikdanSaranWs> kritikdanksaran = new ArrayList<>();
            List<KritikdanSaran> listkritikdansaran = kritikdansaranRepo.findAll();
            if (listkritikdansaran.isEmpty()) {
                responseData.setResponseCode(ResponseCode.ZERO_RESULT);
                responseData.setResponseMessage(BodyUtil.X_RESPONSE_CODE);
                return new ResponseEntity<ResponseData>(responseData, HttpStatus.NOT_FOUND);
            }
            for (KritikdanSaran kritiksaran : listkritikdansaran) {
                KritikdanSaranWs kritikdansaranws = new KritikdanSaranWs();
                if (kritiksaran.getKritikdansaranID() != null) {
                    kritikdansaranws.setKritikdansaranId(String.valueOf(kritiksaran.getKritikdansaranID()));
                } else {
                    kritikdansaranws.setKritikdansaranId(null);
                }
                if (kritiksaran.getKritikdansaran() != null){
                    kritikdansaranws.setKritikdansaran(kritiksaran.getKritikdansaran());
                } else {
                    kritikdansaranws.setKritikdansaran(null);
                }
                if (kritiksaran.getPersonnelID() != null){
                    kritikdansaranws.setPersonnelId(resPersonnelRepo.findOne(kritiksaran.getPersonnelID()).getPersonnelID());
                }
                    kritikdansaranws.setPersonnelId(null);
                    
                kritikdanksaran.add(kritikdansaranws);
            }
            responseData.setData(kritikdanksaran);
            return ResponseEntity.ok(responseData);
        }catch (Exception e){
            Response response = new Response();
            response.setResponseCode(ResponseCode.GENERAL_ERROR);
            response.setResponseMessage(e.getMessage());
            //logger.error(e.getMessage());
            System.out.println(e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
