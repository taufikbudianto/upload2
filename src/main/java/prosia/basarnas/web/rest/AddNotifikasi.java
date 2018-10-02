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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.CPNotifikasi;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.NotifikasiRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.service.NotifikasiService;

/**
 *
 * @author Taufik
 */
@RestController
public class AddNotifikasi {

    @Autowired
    private NotifikasiService notifikasiService;
    @Autowired
    private NotifikasiRepo notifikasiRepo;
    @Autowired
    private ResPersonnelRepo personelRepo;

    @RequestMapping(value = "/notifikasi", method = RequestMethod.POST)
    public ResponseEntity<?> addNotifikasi(@RequestParam("uploadfile") MultipartFile[] uploadfile,
            @RequestParam Map<String, String> mapParam) {
        DataJson data = new DataJson();
        data.setTanggapi(false);
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("notifjudul")) {
                data.setNotifJudul(map.getValue());
            }
            if (map.getKey().equals("notifkonten")) {
                data.setNotifKonten(map.getValue());
            }
            if (map.getKey().equals("tanggapi")) {
                try {
                    data.setTanggapi(Boolean.valueOf(map.getValue()));
                } catch (Exception e) {
                    data.setTanggapi(false);
                }
            }
            if (map.getKey().equals("listpersonilid")) {
                List<String> listPersonil = new ArrayList<>();
                String[] parts = map.getValue().split(",");
                for (String personil : parts) {
                    listPersonil.add(personil);
                }
                data.setListpersonilid(listPersonil);
            }
        }

        try {
            List<ResPersonnel> respersonelList = new ArrayList<>();
            CPNotifikasi cp = new CPNotifikasi();
            cp.setNotifJudul(data.getNotifJudul());
            cp.setNotifKonten(data.getNotifKonten());
            cp.setTanggapi(data.getTanggapi());
            cp.setNotifTipe(1);
            for (String id : data.getListpersonilid()) {
                if (personelRepo.findByPersonnelID(id) != null) {
                    respersonelList.add(personelRepo.findByPersonnelID(id));
                }
            }
            notifikasiService.saveNotifikasi(cp, respersonelList);
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Save Succes");
            if(uploadfile.length>0){
                saveUploadedFiles(uploadfile);
            }
            return new ResponseEntity<Response>(succes, HttpStatus.OK);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    private void saveUploadedFiles(MultipartFile[] files) throws IOException {
        System.out.println("masuk ke temp save ");
        String TMP_FOLDER ="C:\\\\Basarnas\\\\upload_potensi\\\\LibUploaded";
        File fileSave = new File(TMP_FOLDER + "\\");
        for (MultipartFile file : files) {
            if (!fileSave.exists()) {
                if (fileSave.mkdir());
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(TMP_FOLDER + "\\" + file.getOriginalFilename());
            Files.write(path, bytes);

        }
    }

    @Data
    public static class DataJson implements Serializable {

        private String notifJudul;
        private String notifKonten;
        private Boolean tanggapi;
        private List<String> listpersonilid;
    }
    /*
    Integer notifTipe;
    String notifAttachment;
     */

}
