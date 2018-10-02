/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.ws.cilent;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import prosia.basarnas.model.Ss_SiapSiaga;
import prosia.basarnas.repo.SsSiapSiagaRepo;

/**
 *
 * @author Riki
 */
@Service
@Transactional (readOnly = false, rollbackFor = {Exception.class})
public class SsSiapSiaga implements Serializable {
    @Autowired
    private SsSiapSiagaRepo siapSiagaRepo;
    
    public void saveSsSiapSiaga() throws IOException {
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-YY");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String url = "http://siap.basarnas.go.id/xmlmodule/siapsiaga?param=10-11-2017";
        ResponseEntity<String> res
                = restTemplate.exchange(url, HttpMethod.GET, entity,
                        String.class);
        if (res.getStatusCodeValue() == 200) {
            String xml = res.getBody();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            InputSource is;
            try {
               builder = factory.newDocumentBuilder();
               is = new InputSource(new StringReader(xml));
               Document doc =  builder.parse(is);
                NodeList list = doc.getElementsByTagName("satker");
                System.out.println("----------------------------Mulai----------------------------");
                for (int temp = 0; temp < list.getLength(); temp++) {
                    Node nNode = list.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        NodeList siapsiagaList = eElement.getElementsByTagName("siapsiaga");
                        for (int siapsiaga = 0; siapsiaga < siapsiagaList.getLength(); siapsiaga++) {
                            Node nodeSiapsiaga = siapsiagaList.item(siapsiaga);
                            if (nodeSiapsiaga.getNodeType() == nodeSiapsiaga.ELEMENT_NODE) {
                                prosia.basarnas.model.Ss_SiapSiaga ssSiapSiaga = new prosia.basarnas.model.Ss_SiapSiaga();
                                Element eSiapsiaga = (Element) nodeSiapsiaga;
                                
                                List<Ss_SiapSiaga> kode = siapSiagaRepo.findKodeByKode(String.valueOf(eElement.getElementsByTagName("kode").item(0).getTextContent()));
                                
                            if (kode != null) {
                                System.out.println("Update");
                                String result = java.net.URLDecoder.decode(eSiapsiaga.getElementsByTagName("detail").item(0).getTextContent(), "UTF-8");
                                String detail = String.valueOf(result.replaceAll("&nbsp;", "").replaceAll("\\<.*?\\>", ""));
                                String result2 = java.net.URLDecoder.decode(eSiapsiaga.getElementsByTagName("penempatan").item(0).getTextContent(), "UTF-8");
                                String penempatan = String.valueOf(result2.replaceAll("\\<.*?\\>", ""));
                                Date entry = null;
                                if (!String.valueOf(eSiapsiaga.getElementsByTagName("tglentry").item(0).getTextContent()).equals("")) {
                                    try {
                                        entry = date.parse(eSiapsiaga.getElementsByTagName("tglentry").item(0).getTextContent());
                                    } catch (ParseException ex) {
                                        entry = null;
                                    }
                                }
                                ssSiapSiaga.setKode(eElement.getElementsByTagName("kode").item(0).getTextContent());
                                ssSiapSiaga.setNama(eElement.getElementsByTagName("nama").item(0).getTextContent());
                                ssSiapSiaga.setDeleted(false);
                                ssSiapSiaga.setPenempatan(penempatan);
                                ssSiapSiaga.setDetail(detail);
                                ssSiapSiaga.setTglEntry(entry);        
                                System.out.println("Siap Siaga " + siapsiaga + ": " + ssSiapSiaga.toString());
                                //siapSiagaRepo.save(ssSiapSiaga);
                            } else {
                                System.out.println("data baru");
                                String result = java.net.URLDecoder.decode(eSiapsiaga.getElementsByTagName("detail").item(0).getTextContent(), "UTF-8");
                                String detail = String.valueOf(result.replaceAll("&nbsp;", "").replaceAll("\\<.*?\\>", ""));
                                String result2 = java.net.URLDecoder.decode(eSiapsiaga.getElementsByTagName("penempatan").item(0).getTextContent(), "UTF-8");
                                String penempatan = String.valueOf(result2.replaceAll("\\<.*?\\>", ""));
                                Date entry = null;
                                if (!String.valueOf(eSiapsiaga.getElementsByTagName("tglentry").item(0).getTextContent()).equals("")) {
                                    try {
                                        entry = date.parse(eSiapsiaga.getElementsByTagName("tglentry").item(0).getTextContent());
                                    } catch (ParseException ex) {
                                        entry = null;
                                    }
                                }
                                ssSiapSiaga.setKode(eElement.getElementsByTagName("kode").item(0).getTextContent());
                                ssSiapSiaga.setNama(eElement.getElementsByTagName("nama").item(0).getTextContent());
                                ssSiapSiaga.setDeleted(false);
                                ssSiapSiaga.setPenempatan(penempatan);
                                ssSiapSiaga.setDetail(detail);
                                ssSiapSiaga.setTglEntry(entry);   
                                System.out.println("Siap Siaga " + siapsiaga + ": " + ssSiapSiaga.toString());
                                //siapSiagaRepo.save(ssSiapSiaga);
                            }
                            }
                        }
                    }
                }
                System.out.println("----------------------------Selesai----------------------------");
            } catch (ParserConfigurationException | SAXException | IOException e) {
                
            }
        } else {

        
            
        }
    }
    
}
