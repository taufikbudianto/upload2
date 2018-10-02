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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.model.TrapeziumArea;
import prosia.basarnas.model.TrapeziumTaskArea;
import prosia.basarnas.repo.TrapeziumAreaRepo;
import prosia.basarnas.repo.TrapeziumTaskAreaRepo;
import prosia.basarnas.web.rest.dto.TrapeziumWs;

/**
 * trapezium
 *
 * @author Taufik
 */
@RestController
public class PlanningWsTrapezium {

    @Autowired
    private TrapeziumAreaRepo trapAreaRepo;
    @Autowired
    private TrapeziumTaskAreaRepo trapTaskarearepo;

    @RequestMapping(value = "/planning/trapezium/{incidentid}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataIncidentDetail(@PathVariable String incidentid) {
        try {
            ResponseData response = new ResponseData();
            List<TrapeziumArea> listTrapArea = trapAreaRepo.findTrapByIncident(incidentid);
            if (listTrapArea.isEmpty()) {
                response.setResponseCode(ResponseCode.INVALID_PARAMS);
                response.setResponseMessage("Search Area Null Pada incident ini");
                response.setData(listTrapArea);
            }
            TrapeziumWs trapWs = new TrapeziumWs();
            List<TrapeziumWs.TrapArea> listTrapAreaWs = new ArrayList<>();
            List<TrapeziumWs.TrapTaskArea> listTrapTaskWs = new ArrayList<>();
            for (TrapeziumArea trapArea : listTrapArea) {
                TrapeziumWs.TrapArea trapAreaws = new TrapeziumWs.TrapArea();
                trapAreaws.setArea_space(trapArea.getLuasArea());
                trapAreaws.setDescription(trapArea.getDeskripsi());
                trapAreaws.setDistress_error(trapArea.getDistressError());
                trapAreaws.setId(trapArea.getTrapeziumAreaID());
                trapAreaws.setLatitude_dest(trapArea.getLatDest());
                trapAreaws.setLatitude_kp(trapArea.getLatLkp());
                trapAreaws.setLongitude_dest(trapArea.getLongDest());
                trapAreaws.setLongitude_kp(trapArea.getLongLkp());
                trapAreaws.setOperation_time(String.valueOf(trapArea.getWaktuoperasi()));
                trapAreaws.setSafety_factor(trapArea.getSafetyFactor());
                trapAreaws.setSearch_error(trapArea.getSearchError());
                trapAreaws.setTask_area_last_point(trapArea.getTaskAreaLastPoint());
                trapAreaws.setTotal_task_area_length(trapArea.getTotalTaskAreaLength());
                trapAreaws.setUnit(trapArea.getUnit());
                trapAreaws.setWaypoint(trapArea.getWaypoint());
                trapAreaws.setWorksheetname(trapArea.getWorksheetName());

                listTrapAreaWs.add(trapAreaws);
                List<TrapeziumTaskArea> listTrapTaskArea = trapTaskarearepo.
                        findByTrapeziumAreaAndDeletedFalse(trapArea);
                for (TrapeziumTaskArea tsk : listTrapTaskArea) {
                    TrapeziumWs.TrapTaskArea newtsk = new TrapeziumWs.TrapTaskArea();
                    newtsk.setBig_coord_1(tsk.getBigCoord1());
                    newtsk.setBig_coord_2(tsk.getBigCoord2());
                    newtsk.setDescription(tsk.getDescription());
                    newtsk.setId(tsk.getTrapeziumTaskAreaID());
                    newtsk.setName(tsk.getName());
                    newtsk.setSmall_coord_1(tsk.getSmallCoord1());
                    newtsk.setSmall_coord_2(tsk.getSmallCoord2());
                    newtsk.setSplit(tsk.getSplit());
                    newtsk.setTask_area_length(tsk.getTaskAreaLength());
                    newtsk.setTask_area_space(tsk.getLuasTaskArea());
                    listTrapTaskWs.add(newtsk);
                }
            }
            trapWs.setTrapeziumArea(listTrapAreaWs);
            trapWs.setTrapezium_task_area(listTrapTaskWs);
            RespTrap resp = new RespTrap();
            resp.setResponseCode(ResponseCode.SUCCESS);
            resp.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Response respp = new Response();
            respp.setResponseCode(ResponseCode.GENERAL_ERROR);
            respp.setResponseMessage(e.getMessage());
            return new ResponseEntity<Response>(respp, HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class RespTrap extends Response {

        private TrapeziumWs trap;
    }
}
