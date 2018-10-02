/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.ws.cilent.SsSiagaLaut;
import prosia.basarnas.repo.ws.cilent.SsSiagaLautRepo;

/**
 *
 * @author RANDY
 */
@RestController
public class PersonilController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SsSiagaLautRepo siagaLautRepo;
    
    @RequestMapping(value = "/getdata", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDataPotensi() {
        List<SsSiagaLaut> listSiagaLaut = siagaLautRepo.findAll();
        
        ResponseData responseData = new ResponseData();
        responseData.setResponseCode(ResponseCode.SUCCESS);
        responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
        responseData.setData(listSiagaLaut);
        
        return ResponseEntity.ok(responseData);
    }
    
}
