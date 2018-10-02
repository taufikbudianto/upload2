/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.CPNotifTanggapan;
import prosia.basarnas.model.CPNotifikasi;
import prosia.basarnas.model.CPPerpustakaan;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.NotifikasiPersonilRepo;
import prosia.basarnas.repo.NotifikasiRepo;
import prosia.basarnas.repo.NotifikasiTanggapanRepo;
import prosia.basarnas.repo.PerpustakkanRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.web.rest.dto.DataJsonError;
import prosia.basarnas.web.rest.dto.NotifikasiTanggapanWs;
import prosia.basarnas.web.rest.dto.NotifikasiWs;

/**
 *
 * @author Taufik
 */
@RestController
public class NotifikasiWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private NotifikasiRepo notifRepo;
    @Autowired
    private NotifikasiPersonilRepo notifPersonelRepo;
    @Autowired
    private NotifikasiTanggapanRepo notifTanggapanRepo;

    @Autowired
    private ResPersonnelRepo personelRepo;
    @Autowired
    private PerpustakkanRepo libraryImg;
    @Value("${url.domain}")
    private String domain;

    @RequestMapping(value = "/notif", method = RequestMethod.GET)
    public ResponseEntity<?> getDataNotifikasi(@RequestParam Map<String, String> mapParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer limit = 10;
        Integer page = 1;
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("limit")) {
                limit = Integer.parseInt(map.getValue());
            }
            if (map.getKey().equals("start_from_id")) {
                page = Integer.parseInt(map.getValue());
            }
        }
        ResponseData responseData = new ResponseData();
        try {
            User user = (User) authentication.getPrincipal();
            ResPersonnel personel = personelRepo.findByPersonnelCode(user.getUsername());
            List<Integer> listnotifId = notifPersonelRepo.getNotifId(personel.getPersonnelID());
            if (listnotifId.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            Page<CPNotifikasi> listCpNotif = notifRepo.findAll(whereQuery(listnotifId), new PageRequest(page - 1, limit));
            List<NotifikasiWs> listNotifikasiWs = new ArrayList<>();
            if (listCpNotif.getContent().isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (CPNotifikasi notif : listCpNotif.getContent()) {
                NotifikasiWs notifWs = new NotifikasiWs();
                notifWs.setNotifId(notif.getNotifId());
                notifWs.setCreatedBy(notif.getCreatedBy());
                notifWs.setDateCreated(String.valueOf(notif.getCreatedDate()));
                notifWs.setMessage(notif.getNotifKonten());
                notifWs.setFlagComment(notif.getTanggapi());
                List<CPNotifTanggapan> notifTanggapan = notifTanggapanRepo.getDataByNotifIdAndIdPersonel(notif.getNotifId());
                List<NotifikasiTanggapanWs> listTanggapan = new ArrayList<>();
                for (CPNotifTanggapan notifTanggapanData : notifTanggapan) {
                    NotifikasiTanggapanWs tanggapanWs = new NotifikasiTanggapanWs();
                    tanggapanWs.setComment(notifTanggapanData.getTanggapan());
                    tanggapanWs.setCreatedBy(notifTanggapanData.getCreatedBy());
                    tanggapanWs.setGender(personel.getGender());
                    tanggapanWs.setDateCreated(String.valueOf(notifTanggapanData.getCreatedDate()));
                    listTanggapan.add(tanggapanWs);
                }
                notifWs.setCommentList(listTanggapan);
                listNotifikasiWs.add(notifWs);
            }
            responseData.setData(listNotifikasiWs);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            DataJsonError error = new DataJsonError();
            error.setCode(ResponseCode.GENERAL_ERROR);
            error.setMessage("Error : " + e.getMessage());
            log.error(e.getMessage());
            return new ResponseEntity<DataJsonError>(error, HttpStatus.BAD_REQUEST);
        }
    }

    private Specification<CPNotifikasi> whereQuery(List<Integer> list) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPNotifikasi>() {
            @Override
            public Predicate toPredicate(Root<CPNotifikasi> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Expression<Integer> exp = root.get("notifId");
                predicates.add(exp.in(list));
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

    @RequestMapping(value = "/notif/count", method = RequestMethod.GET)
    public long countData(@RequestParam Map<String, String> mapParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ResPersonnel personel = user.getResPersonnel();
        List<Integer> listnotifId = notifPersonelRepo.getNotifId(personel.getPersonnelID());
        if (listnotifId.isEmpty()) {
            return 0;
        }
        return listnotifId.size();
    }
}
