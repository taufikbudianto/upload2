/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.CPKalender;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.repo.KalenderRepo;
import prosia.basarnas.web.rest.dto.CalenderWs;

/**
 *
 * @author Taufik
 */
@RestController
public class CalenderWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private KalenderRepo kalenderRepo;

    @RequestMapping(value = "/calendar/{year}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataCalender(@PathVariable Integer year) {
        ResponseData responseData = new ResponseData();
        try {
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<CalenderWs> listDataJson = new ArrayList<>();//
            List<CPKalender> listCpKal = kalenderRepo.getAllDataByYear(String.valueOf(year));
            if (listCpKal.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (CPKalender cpCal : listCpKal) {
                CalenderWs calWs = new CalenderWs();
                calWs.setTitle(cpCal.getCalJudul());
                calWs.setAll_day(cpCal.getCalSeharian());
                calWs.setStart_date(cpCal.getCalMulaiTimezone() != null
                        ? String.valueOf(cpCal.getCalMulai()) + "+" + cpCal.getCalMulaiTimezone().substring(4, 6) : String.valueOf(cpCal.getCalMulai()));
                calWs.setEnd_date(cpCal.getCalSampaiTimezone() != null
                        ? String.valueOf(cpCal.getCalSampai()) + "+" + cpCal.getCalSampaiTimezone().substring(4, 6) : String.valueOf(cpCal.getCalSampai()));
                calWs.setReminder(cpCal.getCalPengingat());
                if (cpCal.getCalPerulangan() == 0) {
                    calWs.setRepeat("no repeat");
                }
                if (cpCal.getCalPerulangan() == 1) {
                    calWs.setRepeat("once");
                }
                if (cpCal.getCalPerulangan() == 2) {
                    calWs.setRepeat("daily");
                }
                if (cpCal.getCalPerulangan() == 3) {
                    calWs.setRepeat("monthly");
                }
                if (cpCal.getCalPerulangan() == 4) {
                    calWs.setRepeat("yearly");
                }
                if (cpCal.getCalPerulangan() == 5) {
                    calWs.setRepeat("weekly");
                }

                listDataJson.add(calWs);
            }
            responseData.setData(listDataJson);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Ambil Data");
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public ResponseEntity<?> getDataCalenderBetween(@RequestParam Map<String, String> param) {
        ResponseData responseData = new ResponseData();
        try {
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<CalenderWs> listDataJson = new ArrayList<>();//
            List<CPKalender> listCpKal = kalenderRepo.findAll(whereQuery(param));
            if (listCpKal.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            for (CPKalender cpCal : listCpKal) {
                CalenderWs calWs = new CalenderWs();
                calWs.setTitle(cpCal.getCalJudul());
                calWs.setAll_day(cpCal.getCalSeharian());
                calWs.setStart_date(cpCal.getCalMulaiTimezone() != null
                        ? String.valueOf(cpCal.getCalMulai()) + "+" + cpCal.getCalMulaiTimezone().substring(4, 6) : String.valueOf(cpCal.getCalMulai()));
                calWs.setEnd_date(cpCal.getCalSampaiTimezone() != null
                        ? String.valueOf(cpCal.getCalSampai()) + "+" + cpCal.getCalSampaiTimezone().substring(4, 6) : String.valueOf(cpCal.getCalSampai()));
                calWs.setReminder(cpCal.getCalPengingat());
                if (cpCal.getCalPerulangan() == 0) {
                    calWs.setRepeat("no repeat");
                }
                if (cpCal.getCalPerulangan() == 1) {
                    calWs.setRepeat("once");
                }
                if (cpCal.getCalPerulangan() == 2) {
                    calWs.setRepeat("daily");
                }
                if (cpCal.getCalPerulangan() == 3) {
                    calWs.setRepeat("monthly");
                }
                if (cpCal.getCalPerulangan() == 4) {
                    calWs.setRepeat("yearly");
                }
                if (cpCal.getCalPerulangan() == 5) {
                    calWs.setRepeat("weekly");
                }

                listDataJson.add(calWs);
            }
            responseData.setData(listDataJson);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/calendar", method = RequestMethod.POST)
    public ResponseEntity<?> addDataCalender(@RequestBody DataJson data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:dd");

        try {
            CPKalender cpKalender = new CPKalender();
            if (("no repeat").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(0);
            }
            if (("once").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(1);
            }
            if (("daily").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(2);
            }
            if (("monthly").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(3);
            }
            if (("weekly").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(5);
            }
            if (("yearly").equals(String.valueOf(data.getRepeat()))) {
                cpKalender.setCalPerulangan(4);
            }
            String mulai = data.getStart_date();
            String selesai = data.getEnd_date();
            String[] partMulai = mulai.split("\\+");
            String[] partSelesai = selesai.split("\\+");

            cpKalender.setCalMulai(sdf.parse(partMulai[0]));
            cpKalender.setCalMulaiTimezone("GMT+" + partMulai[1] + ":00");
            cpKalender.setCalSampai(sdf.parse(partSelesai[0]));
            cpKalender.setCalSampaiTimezone("GMT+" + partSelesai[1] + ":00");
            cpKalender.setCalJudul(data.getTitle());
            cpKalender.setCalPengingat(data.getReminder());
            cpKalender.setCalSeharian(data.getAll_day());
            cpKalender.setDeleted(false);
            kalenderRepo.save(cpKalender);
            Response succes = new Response();
            succes.setResponseCode(ResponseCode.SUCCESS);
            succes.setResponseMessage("Save Succes");
            return new ResponseEntity<Response>(succes, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage("Gagal Save");
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class DataJson implements Serializable {

        private String title;
        private Boolean all_day;
        private String start_date;
        private String end_date;
        private String repeat;
        private Integer reminder;

    }

    private Specification<CPKalender> whereQuery(Map<String, String> mapFilter) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
        SimpleDateFormat sdfAfterCalMulai = new SimpleDateFormat("yyyy");
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPKalender>() {
            @Override
            public Predicate toPredicate(Root<CPKalender> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {//deleted
                Date from = null, to = null;
                String tanggal = null;
                String tanggal2 = null;
                for (Map.Entry<String, String> map : mapFilter.entrySet()) {
                    if (map.getKey().equals("year")) {

                        try {
                            Date calMulai = sdfAfterCalMulai.parse(String.valueOf(root.<Date>get("calMulai")));
                            System.out.println("data : " + calMulai);
                            //predicates.add(cb.notEqual(sdfAfterCalMulai(root.<Date>get("calMulai")), map.getKey()));
                        } catch (ParseException ex) {

                        }

                    }
                    if (map.getKey().equals("start")) {
                        try {
                            from = sdf.parse(map.getValue()+" 00:00:00");
                        } catch (ParseException ex) {
                            tanggal = "gagal";
                        }

                    }
                    if (map.getKey().equals("to")) {
                        try {
                            to = sdf.parse(map.getValue()+" 23:59:59");
                        } catch (ParseException ex) {
                            tanggal2 = "gagal";
                        }
                    }
                }
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));
                if (tanggal == null && tanggal2 == null) {
                    Path<Date> dateEntryPath = root.get("calMulai");
                    predicates.add(cb.between(dateEntryPath, from, to));
                }
                Path<Date> dateEntryPath = root.get("calMulai");
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
