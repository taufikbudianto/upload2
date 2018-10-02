/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentTabPOBRepo;
import prosia.basarnas.repo.ResPersonnelRepo;

/**
 *
 * @author Taufik AB
 */
@RestController
public class LogIncidentWs {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IncidentTabPOBRepo incidentTabPOBRepo;

    @Autowired
    private IncidentLogRepo incidentLogRepo;

    @Autowired
    private ResPersonnelRepo personelRepo;

    @Autowired
    private IncidentRepo incidentRepo;

    private final String TMP_FOLDER = "C:\\Basarnas\\mobile\\log";

    @RequestMapping(value = "/incident/{incidentid}/log", method = RequestMethod.POST)
    public ResponseEntity<?> addDataIncidentLog(@PathVariable String incidentid,
            @RequestParam("uploadfile") MultipartFile[] uploadfile,
            @RequestParam Map<String, String> mapParam) {
        DataJsonRequest request = new DataJsonRequest();
        System.out.println("upload file : "+uploadfile.length);
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("content")) {
                request.setContent(map.getValue());
            }
            if (map.getKey().equals("date")) {
                request.setDate(map.getValue());
            }
            if (map.getKey().equals("time")) {
                request.setTime(map.getValue());
            }
            if (map.getKey().equals("initiator")) {
                request.setInitiator(map.getValue());
            }
            if (map.getKey().equals("latitude")) {
                request.setLatitude(map.getValue());
            }
            if (map.getKey().equals("longitude")) {
                request.setLongitude(map.getValue());
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            User user = (User) authentication.getPrincipal();
            ResPersonnel resPersonel = personelRepo.findByPersonnelCode(user.getUsername());
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Save Succes");
            IncidentLog logIncident = new IncidentLog();
            logIncident.setLogID(formatclassId());
            logIncident.setLogType("UMUM");
            Incident inc = incidentRepo.findAllByIncidentid(incidentid);
            if (inc == null) {
                logIncident.setIncident(null);
                Response gagalNull = new Response();
                gagalNull.setResponseCode(ResponseCode.SUCCESS);
                gagalNull.setResponseMessage("Gagal Save");
                return new ResponseEntity<Response>(gagalNull, HttpStatus.BAD_REQUEST);
            }
            logIncident.setIncident(inc);
            logIncident.setSubject(request.getSubject_log());
            logIncident.setLongitude(request.getLongitude());
            logIncident.setLatitude(request.getLatitude());
            logIncident.setRemarks(request.getContent());
            logIncident.setPersonnel(resPersonel);
            logIncident.setIncidentId(inc.getIncidentid());
            String tanggal = request.getDate()+" "+request.getTime();
            Date tgl = sdf.parse(tanggal);
            logIncident.setLogDate(tgl);
            logIncident.setDeleted(false);
            logIncident.setStatus("Ws");
            StringBuilder sb = new  StringBuilder();
            if(uploadfile.length>0){
                for (MultipartFile file : uploadfile) {
                    sb.append(file.getOriginalFilename()).append(";");
                }
                logIncident.setAttachment(sb.toString());
            }
            incidentLogRepo.save(logIncident);
            if(uploadfile.length>0){
                saveUploadedFiles(uploadfile, logIncident.getLogID());
            }
            log.info(logIncident.toString());
            return new ResponseEntity<Response>(succes, HttpStatus.OK);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
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

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        log.debug("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<IncidentLog> lis = incidentLogRepo.findAllBylogIDContaining(ProsiaConstant.SITES);

        if (lis.isEmpty()) {
            log.debug("A");
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            log.debug("B");
            String maxId = incidentLogRepo.findClassByMaxId(ProsiaConstant.SITES);
            //String[] splitId = i.getIncidentid().split("-");
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
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return classId;
    }

    @Data
    public static class DataJsonRequest implements Serializable {

        private String subject_log;
        private String content;
        private String date;
        private String time;
        private String initiator;
        private String latitude;
        private String longitude;
    }
}
