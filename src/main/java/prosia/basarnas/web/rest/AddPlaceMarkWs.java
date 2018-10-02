/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncidentPlacemark;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.PlacemarkRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.ResPersonnelRepo;

/**
 *
 * @author PROSIA
 */
@RestController
public class AddPlaceMarkWs {

    @Autowired
    private PlacemarkRepo placemarkRepo;
    @Autowired
    private IncidentRepo incidentRepo;

    private final String TMP_FOLDER = "C:\\Basarnas\\mobile\\placemark";

    @ExceptionHandler(MultipartException.class)
    @RequestMapping(value = "/placemark", method = RequestMethod.POST)
    public ResponseEntity<?> addDataPlacemark(
            @RequestParam("uploadfile") MultipartFile[] uploadfile,
            @RequestParam Map<String, String> mapParam) {
        DataJson dataJson = new DataJson();
        System.out.println("dataaaaaaaaaaaaa : "+uploadfile.length);
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("name")) {
                dataJson.setName(map.getValue());
            }
            if (map.getKey().equals("description")) {
                dataJson.setDescription(map.getValue());
            }
            if (map.getKey().equals("latitude")) {
                dataJson.setLatitude(map.getValue());
            }
            if (map.getKey().equals("longitude")) {
                dataJson.setLongitude(map.getValue());
            }
            if (map.getKey().equals("incident_id")) {
                dataJson.setIncident_id(map.getValue());
            }
            if (map.getKey().equals("image_icon")) {
                dataJson.setImage_icon(map.getValue());
            }
            if (map.getKey().equals("telepon")) {
                dataJson.setTelepon(map.getValue());
            }
            if (map.getKey().equals("website")) {
                dataJson.setWebsite(map.getValue());
            }
            if (map.getKey().equals("jam")) {
                dataJson.setJam(map.getValue());
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        try {
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Save Succes");
            User user = (User) authentication.getPrincipal();
            IncidentPlacemark incplacemark = new IncidentPlacemark();
            //System.out.println(user.getResPersonnel().getOfficeSar().getKantorsarid());
            incplacemark.setPlacemarkID(generateIdSeq());
            incplacemark.setPlacemarkName(dataJson.getName());
            incplacemark.setDescription(dataJson.getDescription());
            incplacemark.setLatitude(dataJson.getLatitude());
            incplacemark.setLongitude(dataJson.getLongitude());
            try {
                incplacemark.setIncident(incidentRepo.findAllByIncidentid(dataJson.getIncident_id()));
            } catch (NullPointerException e) {
                incplacemark.setIncident(null);
            }
            incplacemark.setImage(dataJson.getImage_icon());
            try {
                incplacemark.setUserSiteID(user.getResPersonnel().getOfficeSar().getKantorsarid());
            } catch (NullPointerException e) {
                incplacemark.setUserSiteID(null);
            }
            System.out.println("inv "+incplacemark.toString());
            placemarkRepo.save(incplacemark);
            if (uploadfile.length > 0) {
                saveUploadedFiles(uploadfile, incplacemark.getPlacemarkID());
            }
            return new ResponseEntity<Response>(succes, HttpStatus.OK);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            System.out.println(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class DataJson implements Serializable {

        private String name;
        private String description;
        private String latitude;
        private String longitude;
        private String incident_id;
        private String image_icon;
        private String telepon;
        private String website;
        private String jam;

    }

    private void saveUploadedFiles(MultipartFile[] files, String id) throws IOException {
        System.out.println("masuk ke temp save ");
        File fileSave = new File(TMP_FOLDER + "\\" + id);
        for (MultipartFile file : files) {
            if (!fileSave.exists()) {
                if (fileSave.mkdir());
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(TMP_FOLDER + "\\" + id + "\\" + file.getOriginalFilename());
            Files.write(path, bytes);

        }
    }

    private String generateIdSeq() {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<IncidentPlacemark> ias = placemarkRepo.findByPlacemarkIDContaining("CGK");
        String id = null;
        if (ias.isEmpty()) {
            id = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = placemarkRepo.findByMaxId("CGK");
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        break;
                    case 2:
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        nextval = "" + next;
                        break;
                    default:
                        break;
                }
                id = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }
}
