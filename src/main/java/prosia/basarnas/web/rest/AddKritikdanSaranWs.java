/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.KritikdanSaran;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.KritikdanSaranRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
/**
 *
 * @author PROSIA
 */
@RestController
public class AddKritikdanSaranWs {
   @Autowired
   private KritikdanSaranRepo kritikdansaranRepo;
   
   @Autowired
   private ResPersonnelRepo respersonnelRepo;
   
   
   @RequestMapping(value = "/kritiksaran", method = RequestMethod.POST)
   public ResponseEntity<?> addData (@RequestBody DataJson dataJson) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       ResponseData responseData = new ResponseData();
       try{
           Response succes = new Response();
           succes.setResponseCode(ResponseCode.SUCCESS);
           succes.setResponseMessage("Save Succes");
           User user = (User) authentication.getPrincipal();
           ResPersonnel resPersonel = respersonnelRepo.findByPersonnelCode(user.getUsername());
           KritikdanSaran kritiksaran = new KritikdanSaran();
           if (kritiksaran.getPersonnelID() != null){
                    kritiksaran.setPersonnelID(respersonnelRepo.findOne(resPersonel.getPersonnelID()).getPersonnelID());
                } else{
                    kritiksaran.setPersonnelID(null);
                }
           kritiksaran.setKritikdansaran(dataJson.getComment());
           kritikdansaranRepo.save(kritiksaran);
           return new ResponseEntity<Response>(succes, HttpStatus.OK);
       }catch (Exception e){
           Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            System.out.println(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
       }
   }
   
   
   @Data
    public static class DataJson implements Serializable {
        private String comment;

    }
}
