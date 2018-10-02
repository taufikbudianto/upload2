/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.repo.IncidentPlanningRepo;

/**
 *
 * @author Taufik
 */
@RestController
public class PlanningWs {

    @Autowired
    private IncidentPlanningRepo planRepo;

    @RequestMapping(value = "/planning/{incidentid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataIncidentDetail(@PathVariable String incidentid) {
        List<Object[]> listObj = planRepo.findAllView(incidentid);
        if(listObj.isEmpty()){
            Response resp = new Response();
            resp.setResponseCode(ResponseCode.GENERAL_ERROR);
            resp.setResponseMessage("Tidak Ada Planning di incident ini");
        }
        ResponseData respData = new ResponseData();
        List<RespData> listresp = new ArrayList<>();
        for(Object[] obj :listObj){
            RespData data = new RespData();
            data.setName(String.valueOf(obj[1]));
            data.setDeskripsi(String.valueOf(obj[2]));
            data.setTanggal_operasi(String.valueOf(obj[3]));
            data.setMetode(String.valueOf(obj[4]));
            data.setDibuat_oleh(String.valueOf(obj[5]));
            data.setTanggal_modifikasi(String.valueOf(obj[6]));
            data.setDriftcalculation("http://apps.prosia.co.id:8072/sarcore/planning/drift/"+incidentid);
            data.setTrapezium("http://apps.prosia.co.id:8072/sarcore/planning/trapezium/"+incidentid);
            listresp.add(data);
            
        }
        respData.setResponseCode(ResponseCode.SUCCESS);
        respData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
        respData.setData(listresp);
        return ResponseEntity.ok(respData);
    }
    
    @Data
    public static class RespData {
        private String name;
        private String deskripsi;
        private String tanggal_operasi;
        private String metode;
        private String dibuat_oleh;
        private String tanggal_modifikasi;
        private String driftcalculation;
        private String trapezium;
    }
}
    
