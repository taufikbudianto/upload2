/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.ws.cilent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import prosia.app.web.rest.util.HeaderUtil;

/**
 *
 * @author Ismail
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class RegistrasiPlb implements Serializable {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private RestTemplate restTemplate;

    public void saveRegistartionPlb() throws IOException {
        String url = "http://beacon.basarnas.go.id/Api/listRegistrasiPLB";
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, 
                HeaderUtil.getRequestHeaderEntity(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> maps = mapper.readValue(response.getBody(), 
                new TypeReference<List<Map<String, Object>>>() {});

        for (Map<String, Object> map : maps) {
            System.out.println("User : ID_PEMILIK=" + map.get("ID_PEMILIK")
                    + ", ID_BEACON = " + map.get("ID_BEACON")
                    + ", NAMA_OP = " + map.get("NAMA_OP")
                    + ", EMAIL_OP = " + map.get("EMAIL_OP"));
        }

    }
}
