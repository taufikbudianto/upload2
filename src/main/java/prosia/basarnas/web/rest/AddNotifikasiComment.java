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
import prosia.basarnas.model.CPNotifTanggapan;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.NotifikasiRepo;
import prosia.basarnas.repo.NotifikasiTanggapanRepo;
import prosia.basarnas.repo.ResPersonnelRepo;

/**
 *
 * @author Taufik
 */
@RestController
public class AddNotifikasiComment {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private NotifikasiTanggapanRepo notifTanggapanRepo;

    @Autowired
    private ResPersonnelRepo personelRepo;

    @Autowired
    private NotifikasiRepo notifikasiRepo;

    @RequestMapping(value = "/notif", method = RequestMethod.POST)
    public ResponseEntity<?> addDataNotifikasi(@RequestBody DataJson dataJson) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        try {
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Save Succes");
            User user = (User) authentication.getPrincipal();
            ResPersonnel resPersonel = personelRepo.findByPersonnelCode(user.getUsername());
            CPNotifTanggapan cpTanggapan = new CPNotifTanggapan();
            cpTanggapan.setPersonil(personelRepo.findByPersonnelID(resPersonel.getPersonnelID()));
            cpTanggapan.setNotifikasi(notifikasiRepo.findByNotifId(dataJson.getNotification_id()));
            cpTanggapan.setStatus(true);
            cpTanggapan.setTanggapan(dataJson.getComment());
            cpTanggapan.setCreatedDate(new Date());
            cpTanggapan.setCreatedBy(personelRepo.findByPersonnelID(resPersonel.getPersonnelID()).getPersonnelName());
            notifTanggapanRepo.save(cpTanggapan);
            return new ResponseEntity<Response>(succes, HttpStatus.OK);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class DataJson implements Serializable {

        private Integer notification_id;
        private String comment;

    }
}
