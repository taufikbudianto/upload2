/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.ws.cilent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.ResPotencyRepo;

/**
 *
 * @author Taufik
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class DaftarInstansi {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private ResPotencyRepo potensyRepo;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;

    public @Data
    class DataTemp {

        Integer satker;
        String name;
    }

    public void getDataJsonDataInstansi() throws IOException {
        String url = "http://sibinpot.basarnas.go.id/api/daftarinstansi";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ResPotencyContact contact;
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> res
                = restTemplate.exchange(url, HttpMethod.GET, entity,
                        String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> maps = new ArrayList<>();
        maps = mapper.readValue(res.getBody(), new TypeReference<List<Map<String, Object>>>() {
        });
        List<DataTemp> lisPot = new ArrayList<>();
        logger.info("------------------------Mulai Syncronize data personel at" + new Date() + "------------------------");
        int n = 0;
        if (res.getStatusCodeValue() == 200) {
            int nooo = 0;
            for (Map<String, Object> map : maps) {
                try {
                    String updateOrCreate = null;
                    String updateOrCreateContact = null;
                    Double latitude = null;
                    Double longitude = null;
                    try {
                        latitude = Double.valueOf(String.valueOf(map.get("Q_LATITUDE")).trim());
                        longitude = Double.valueOf(String.valueOf(map.get("Q_LONGITUDE")).trim());
                    } catch (NumberFormatException e) {
                        nooo++;
                        latitude = 0.0;
                        longitude = 0.0;
                    }
                    ResPotency resPotency = potensyRepo.getPotencyByKansarAndPotencyName(
                            String.valueOf(map.get("N_INSTANSI")).trim(), String.valueOf(map.get("C_SATKER")).trim());
                    if (resPotency == null) {
                        updateOrCreate = "New Potensi";
                        updateOrCreateContact = "Sangat Baru Juga generate Potensi";
                        resPotency = new ResPotency();
                        resPotency.setPotencyid(createidPotency());
                        resPotency.setDatecreated(new Date());
                        resPotency.setCreatedBy("by_ws");
                        contact = new ResPotencyContact();
                        contact.setPotencycontactid(createidtempPersonelContact(0));
                        contact.setCreatedBy("by_ws");
                        contact.setCreatedDate(new Date());
                        contact.setPotencyid(resPotency);
                        contact.setContactPersonA(String.valueOf(map.get("N_CONTACT_PERSON_A")).trim().equals("") ? null
                                : String.valueOf(map.get("N_CONTACT_PERSON_A")).trim());
                        contact.setContactPersonB(String.valueOf(map.get("N_CONTACT_PERSON_B")).trim().equals("") ? null
                                : String.valueOf(map.get("N_CONTACT_PERSON_B")).trim());
                        contact.setNoHpA(String.valueOf(map.get("Q_NOHP_CONTACT_A")).trim().equals("") ? null
                                : String.valueOf(map.get("Q_NOHP_CONTACT_A")).trim());
                        contact.setNoHpB(String.valueOf(map.get("Q_NOHP_CONTACT_B")).trim().equals("") ? null
                                : String.valueOf(map.get("Q_NOHP_CONTACT_B")).trim());

                    } else {
                        updateOrCreate = "Update Potensi";
                        contact = resPotencyContactRepo.findByPotencyId(resPotency.getPotencyid());
                        if (contact == null) {
                            updateOrCreateContact = "Potensi Sudah ada Tp Contact Belum";
                            contact = new ResPotencyContact();
                            contact.setPotencycontactid(createidtempPersonelContact(0));
                            contact.setCreatedBy("by_ws");
                            contact.setCreatedDate(new Date());
                        } else {
                            updateOrCreateContact = "Potensi dan Contact Ada";
                            contact.setLastModifiedBy("by_ws");
                            contact.setLastModifiedDate(new Date());
                        }
                        contact.setPotencyid(resPotency);
                        contact.setContactPersonA(String.valueOf(map.get("N_CONTACT_PERSON_A")).trim().equals("") ? null
                                : String.valueOf(map.get("N_CONTACT_PERSON_A")).trim());
                        contact.setContactPersonB(String.valueOf(map.get("N_CONTACT_PERSON_B")).trim().equals("") ? null
                                : String.valueOf(map.get("N_CONTACT_PERSON_B")).trim());
                        contact.setNoHpA(String.valueOf(map.get("Q_NOHP_CONTACT_A")).trim().equals("") ? null
                                : String.valueOf(map.get("Q_NOHP_CONTACT_A")).trim());
                        contact.setNoHpB(String.valueOf(map.get("Q_NOHP_CONTACT_B")).trim().equals("") ? null
                                : String.valueOf(map.get("Q_NOHP_CONTACT_B")).trim());
                        resPotency.setModifiedby("by_ws");
                        resPotency.setLastmodified(new Date());
                        resPotency.setLastModifiedDate(new Date());
                    }
                    resPotency.setPotencyname(String.valueOf(map.get("N_INSTANSI")).trim().equals("") ? null
                            : String.valueOf(map.get("N_INSTANSI")).trim());
                    resPotency.setAddress(String.valueOf(map.get("N_ALAMAT")).trim());
                    resPotency.setLongitude(String.valueOf(longitude));
                    resPotency.setLatitude(String.valueOf(latitude));
                    resPotency.setKantorsarid(kantorSarRepo.findBySatker(String.valueOf(map.get("C_SATKER")).trim()) == null
                            ? null : kantorSarRepo.findBySatker(String.valueOf(map.get("C_SATKER")).trim()));
                    resPotency.setPhonenumber1(String.valueOf(map.get("Q_NOHP_A")).trim().equals("") ? null
                            : String.valueOf(map.get("Q_NOHP_A")).trim());
                    resPotency.setFaxnumber(String.valueOf(map.get("Q_NOFAX")).trim().equals("") ? null
                            : String.valueOf(map.get("Q_NOFAX")).trim());
                    resPotency.setEmail(String.valueOf(map.get("N_EMAIL")).trim().equals("") ? null
                            : String.valueOf(map.get("N_EMAIL")).trim());
                    resPotency.setSocialnetwork(String.valueOf(map.get("N_WEBSITE")).trim().equals("") ? null
                            : String.valueOf(map.get("N_WEBSITE")).trim());
                    resPotency.setNamaInstansi(String.valueOf(map.get("N_INSTANSI")).trim().equals("") ? null
                            : String.valueOf(map.get("N_INSTANSI")).trim());
                    resPotency.setJmlHewan(String.valueOf(map.get("JML_HEWAN")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_HEWAN")).trim()));
                    resPotency.setJmlIT(String.valueOf(map.get("JML_IT")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_IT")).trim()));
                    resPotency.setJmlDarat(String.valueOf(map.get("JML_DARAT")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_DARAT")).trim()));
                    resPotency.setJmlUdara(String.valueOf(map.get("JML_UDARA")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_UDARA")).trim()));
                    resPotency.setJmlLaut(String.valueOf(map.get("JML_LAUT")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_LAUT")).trim()));
                    resPotency.setJmlSdm(String.valueOf(map.get("JML_SDM")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_SDM")).trim()));
                    resPotency.setJmlKualifikasi(String.valueOf(map.get("JML_KUALIFIKASI")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("JML_KUALIFIKASI")).trim()));
                    resPotency.setCInstitusi(String.valueOf(map.get("C_INSTITUSI")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_INSTITUSI")).trim()));
                    resPotency.setStatInstitusi(String.valueOf(map.get("C_STAT_INST")).trim().equals("")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_STAT_INST")).trim()));
                    resPotency.setIsdeleted(new BigInteger("0"));
                    resPotency.setPotencylevel(new BigInteger("0"));
                    potensyRepo.save(resPotency);
                    resPotencyContactRepo.save(contact);
                    logger.info(updateOrCreate + " Value : " + resPotency.toString());
                    logger.info(updateOrCreateContact + " Contact : " + contact.toString());
                } catch (IncorrectResultSizeDataAccessException e) {
                    logger.error("Data ada yang sama pada satu satker -->Potensi : "
                            + String.valueOf(map.get("N_INSTANSI")).trim() + "id Satker" + String.valueOf(map.get("C_SATKER")).trim());
                }
            }
            logger.info("------------------------Selesai Syncronize data Instansi------------------------");

        } else {
            logger.error("URL Error (http://sibinpot.basarnas.go.id/api/daftarinstansi) :  " + res.getStatusCodeValue());

        }

    }

    //Generate ID Potensi
    public String createidPotency() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPotency> ias = potensyRepo.findAllpotencyid("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = potensyRepo.findPotencyByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
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
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    //Generate ID Contact
    private String createidtempPersonelContact(int i) {
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPotencyContact> ias = resPotencyContactRepo.findAllByPContactIdLike("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = resPotencyContactRepo.findPersonnelByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + i;
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
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }
}
