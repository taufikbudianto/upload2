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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.service.PersonnelService;

/**
 *
 * @author Taufik
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class DaftarPersonel {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    ResPersonnelRepo personelRepo;
    @Autowired
    private PersonnelService personnelService;
    @Autowired
    MstKantorSarRepo kantorSarRepo;
    @Autowired
    RestTemplate restTemplate;
    private List<ResPersonnelHistory> listPersonelHistory;
    private List<ResPersonnelTraining> listPersonnelTraining;

    public void getDataJsonDaftarPersonel(String url) throws IOException {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> res
                = restTemplate.exchange(url, HttpMethod.GET, entity,
                        String.class);

        if (res.getStatusCodeValue() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> maps = new ArrayList<>();
            maps = mapper.readValue(res.getBody(), new TypeReference<List<Map<String, Object>>>() {
            });

            logger.info("------------------------Mulai Syncronize data personel at" + new Date() + "------------------------");
            for (Map<String, Object> map : maps) {
                String daftarPersonelVal = null;
                listPersonelHistory = new ArrayList<>();
                listPersonnelTraining = new ArrayList<>();
                String namaPersonel = String.valueOf(map.get("N_PERSONAL"));
                String tempatLahir = String.valueOf(map.get("N_TEMPAT_LAHIR"));
                String tanggalLahir = String.valueOf(map.get("D_LAHIR"));
                String jenisKelamin = null;
                if (String.valueOf(map.get("C_JENIS_KELAMIN")).trim().equals("P")) {
                    jenisKelamin = "M";
                } else {
                    jenisKelamin = "F";
                }
                Date valid = null;
                Date mulaiMasuk = null;
                try {
                    valid = (String.valueOf(map.get("D_VALID")).trim().equals("null")
                            ? null : dt.parse(String.valueOf(map.get("D_VALID")).trim()));
                    mulaiMasuk = (String.valueOf(map.get("D_MULAI_MASUK")).trim().equals("null")
                            ? null : dt.parse(String.valueOf(map.get("D_MULAI_MASUK")).trim()));

                } catch (ParseException ex) {
                    valid = null;
                    mulaiMasuk = null;
                }
                if ((!namaPersonel.equals("N/A") || !namaPersonel.equals("null"))
                        && (!tempatLahir.equals("N/A") || tempatLahir.equals("null"))
                        && (!String.valueOf(String.valueOf(map.get("N_TEMPAT_LAHIR"))).equals("N/A") || !String.valueOf(String.valueOf(map.get("N_TEMPAT_LAHIR"))).equals("null"))
                        && (!String.valueOf(map.get("C_JENIS_KELAMIN")).trim().equals("N/A") || !String.valueOf(map.get("C_JENIS_KELAMIN")).trim().equals("null"))) {
                    //System.out.println("Masuk");
                    Date dtLahir = null;
                    Date dtCreate = null;
                    try {
                        dtLahir = dt.parse(tanggalLahir);
                        dtCreate = dt2.parse(map.get("D_ENTRY").toString());
                    } catch (ParseException ex) {
                        //java.util.logging.Logger.getLogger(DaftarPersonel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException e) {
                        dtCreate = new Date();
                    }
                    //ResPersonnel personel = personelRepo.findOne(personelRepo.getPersonelId(dtLahir, namaPersonel, tempatLahir, jenisKelamin));
                    Map<String,String> qry = new HashMap<String, String>();
                    qry.put("nama", namaPersonel);
                    qry.put("tempatlahir", tempatLahir);
                    qry.put("kalamin", jenisKelamin);
                    ResPersonnel personel = personelRepo.findOne(whereQuery(qry, dtLahir));
                    if (personel == null) {
                        daftarPersonelVal = "Tambah Personel Baru";
                        personel = new ResPersonnel();
                        personel.setPersonnelID(createidtempPersonel(0));
                        System.out.println(createidtempPersonel(0));
                    } else {
                        daftarPersonelVal = "Update Personel";
                        personel.setLastModifiedBy(String.valueOf(map.get("I_ENTRY")).trim().equals("null") ? null
                                : String.valueOf(map.get("I_ENTRY")).trim());
                        personel.setCreatedBy(String.valueOf(map.get("I_ENTRY")).trim().equals("null") ? null
                                : String.valueOf(map.get("I_ENTRY")).trim());
                        personel.setLastModifiedDate(dtCreate);
                    }
                    personel.setPersonnelType(0);
                    personel.setEmploymentType(0);
                    personel.setPersonnelName(namaPersonel);
                    personel.setBirthDate(dtLahir);
                    personel.setBirthPlace(tempatLahir);
                    personel.setGender(jenisKelamin);
                    personel.setReligion(String.valueOf(map.get("C_AGAMA")));
                    personel.setBloodtype(String.valueOf(map.get("C_GOL_DARAH")));
                    personel.setPersonheight(String.valueOf(map.get("Q_TINGGI_BADAN")));
                    personel.setPersonweight(String.valueOf(map.get("Q_BERAT_BADAN")));
                    personel.setHomeAddress(String.valueOf(map.get("N_ALAMAT")));
                    personel.setPhoneNumber(String.valueOf(map.get("Q_NOHP_A")));
                    personel.setMobilePhoneNumber(String.valueOf(map.get("Q_NOHP_B")));
                    personel.setEmail(String.valueOf(map.get("N_EMAIL")));
                    personel.setOfficeSar(kantorSarRepo.findBySatker(String.valueOf(map.get("C_SATKER")).trim()) == null
                            ? null : kantorSarRepo.findBySatker(String.valueOf(map.get("C_SATKER")).trim()));
                    personel.setSatker(String.valueOf(map.get("C_SATKER")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_SATKER")).trim()));
                    personel.setIsdeleted(new BigInteger("0"));
                    personel.setIKtp(String.valueOf(map.get("I_KTP")).trim().equals("null") ? null
                            : String.valueOf(map.get("I_KTP")).trim());
                    personel.setIdPersonil(String.valueOf(map.get("ID_PERSONIL")).trim().equals("null") ? null
                            : String.valueOf(map.get("ID_PERSONIL")).trim());
                    personel.setIdInstansi(String.valueOf(map.get("ID_INSTANSI")).trim().equals("null") ? null
                            : String.valueOf(map.get("ID_INSTANSI")).trim());
                    personel.setIdProvinsi(String.valueOf(map.get("C_PROPINSI")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_PROPINSI")).trim()));
                    personel.setIdKota(String.valueOf(map.get("C_KOTA")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_KOTA")).trim()));
                    personel.setKodePos(String.valueOf(map.get("Q_KODEPOS")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("Q_KODEPOS")).trim()));
                    personel.setTglEntry(dtCreate);
                    personel.setMulaiMasuk(mulaiMasuk);
                    personel.setValidDate(valid);
                    personel.setTmtDate(mulaiMasuk);
                    personel.setKualifikasi(String.valueOf(map.get("C_KUALIFIKASI")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_KUALIFIKASI")).trim()));
                    personel.setEntryBy(String.valueOf(map.get("I_ENTRY")).trim().equals("null") ? null
                            : String.valueOf(map.get("I_ENTRY")).trim());
                    personel.setStatPers(String.valueOf(map.get("C_STAT_PERS")).trim().equals("null")
                            ? null : Integer.parseInt(String.valueOf(map.get("C_STAT_PERS")).trim()));
                    personel.setDatecreated(dtCreate);
                    personel.setCreatedBy(String.valueOf(map.get("I_ENTRY")).trim().equals("null") ? null
                            : String.valueOf(map.get("I_ENTRY")).trim());
                    personel.setIsdeleted(new BigInteger("0"));
                    logger.info("Keterangan : " + daftarPersonelVal);
                    logger.info("Personel : " + personel.toString());
                    personelRepo.save(personel);
                } else {
                    logger.error("Nama,Tempat Lahir, Tanggal Lahir dan Jenis Kelamin ==>Ada yang null (Tidak Save) ");
                }
            }
            logger.info("------------------------Selesai Syncronize data personel------------------------");

        } else {
            logger.error("Gagal Mendapatkan value Json dari url. Error code : " + res.getStatusCodeValue());
        }
    }

    public String createidtempPersonel(int i) {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPersonnel> ias = personelRepo.findAllBypersonnelIDLike("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personelRepo.findPersonnelByMaxId("CGK");
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
    
    private Specification<ResPersonnel> whereQuery(Map<String,String> map , Date lahir) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResPersonnel>() {
            @Override
            public Predicate toPredicate(Root<ResPersonnel> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                predicates.add(cb.like(cb.lower(root.get("personnelName")), getLikePattern(map.get("nama"))));
                predicates.add(cb.like(cb.lower(root.get("birthPlace")), getLikePattern(map.get("tempatlahir"))));
                predicates.add(cb.like(cb.lower(root.get("gender")), getLikePattern(map.get("kalamin"))));
                predicates.add(cb.equal(root.<Date>get("birthDate"), lahir));
                return andTogether(predicates, cb);
            }

        };

    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }


}
