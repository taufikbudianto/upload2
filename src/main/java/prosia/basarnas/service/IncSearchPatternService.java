/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.model.Waypoint;
import prosia.basarnas.repo.SearchPatternRepo;
import prosia.basarnas.repo.WaypointRepo;
import prosia.basarnas.web.controller.MapMBean;

/**
 *
 * @author Ismail
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class IncSearchPatternService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SearchPatternRepo searchPatternRepo;

    @Autowired
    private WaypointRepo waypointRepo;

    public void saveIncSearchPattern(List<IncidentSearchPattern> listSearchPattern) {
        for (IncidentSearchPattern searchPattern : listSearchPattern) {
            if (searchPattern.getSearchPatternId() == null || StringUtils.isNumeric(searchPattern.getSearchPatternId())) {
                searchPattern.setSearchPatternId(generateIdSearchPatternSeq(searchPattern.getUserSiteID()));
            }
            searchPatternRepo.save(searchPattern);
        }
    }

    public void saveIncSearchPattern(IncidentSearchPattern searchPattern) {
        if (searchPattern.getSearchPatternId() == null || StringUtils.isNumeric(searchPattern.getSearchPatternId())) {
            searchPattern.setSearchPatternId(generateIdSearchPatternSeq(searchPattern.getUserSiteID()));
        }
        searchPatternRepo.save(searchPattern);

//        for (int i = 0; i < listWaypoint.size(); i++) {
//            Waypoint w = listWaypoint.get(i);
////            if (w.getWaypointID() == null) {
////                w.setWaypointID(generateIdWaypoinSeq());
////            }
//            w.setSearchPattern(searchPattern);
//            waypointRepo.save(w);
//        }
    }

    private String generateIdSearchPatternSeq(String userSiteId) {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<IncidentSearchPattern> ias = searchPatternRepo.findAllById(userSiteId);
        String id = null;
        if (ias.isEmpty()) {
            id = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = searchPatternRepo.findByMaxId(userSiteId);
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
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
                id = userSiteId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }

    private String generateIdWaypoinSeq(String userSiteId) {
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<Waypoint> ias = waypointRepo.findAllById(userSiteId);
        String id = null;
        if (ias.isEmpty()) {
            id = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = waypointRepo.findByMaxId(userSiteId);
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
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
                id = userSiteId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = userSiteId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }

}
