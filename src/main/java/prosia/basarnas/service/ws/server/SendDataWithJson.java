/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.ws.server;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ws.cilent.SsSiagaLaut;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.ws.cilent.SsSiagaLautRepo;

/**
 *
 * @author Taufik
 */
//@RestController
@RequestMapping("/basarnas")
public class SendDataWithJson {

    @Autowired
    ResPersonnelRepo personelrepo;

    @Autowired
    SsSiagaLautRepo siagaLautRepo;

//    @Value("${sar.ws.username}")
    private String username;
    private LazyDataModelJPA<ResPersonnel> dataModelLogImages;
//    @Value("${sar.ws.password}")
    private String password;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "/getdata", method = RequestMethod.GET)
    public ResponseEntity<List<SsSiagaLaut>> getAllDataPotensi(@RequestHeader Map<String, String> headers) {
        String authorization=null;
        for (String elem : headers.keySet()) {
            if(elem.equals("authorization")){
                authorization=headers.get(elem);
            }
        }

        List<ResPersonnel> list;
        List<SsSiagaLaut> listSiagaLaut;
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            System.out.println("Decoded\t: username [" + values[0]
                    + "], password [" + values[1] + "]");
            if (values[0].equals(username) && values[1].equals(password)) {
                try {
                    list = personelrepo.findAll();
                    listSiagaLaut=siagaLautRepo.findAll();
                    if (list.isEmpty()) {
                        return new ResponseEntity(HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<List<SsSiagaLaut>>(listSiagaLaut, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("HTTP Status 401 : Bad credentials", HttpStatus.UNAUTHORIZED);
            }

        } else {
            return new ResponseEntity("HTTP Status 401 : Bad credentials (Harus Ada Username Dan Password WS)", HttpStatus.BAD_REQUEST);
        }
    }

}
