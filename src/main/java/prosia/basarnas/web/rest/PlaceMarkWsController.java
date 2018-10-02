/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.basarnas.model.IncidentPlacemark;
import prosia.basarnas.model.Incident;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.repo.PlacemarkRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.web.rest.dto.DataJsonError;
import prosia.basarnas.web.rest.dto.PlaceMarkWs;
/**
 *
 * @author PROSIA
 */
@RestController
public class PlaceMarkWsController {
    
    @Autowired
    private PlacemarkRepo placemarkRepo;
   
    @RequestMapping(value = "/placemark", method = RequestMethod.GET)
    public ResponseEntity<?> getDataPlaceMark(@RequestParam Map<String, String> mapParam){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        User user1 = (User) authentication.getPrincipal();
        String userSite = user1.getResPersonnel().getOfficeSar().getKantorsarid();
        try {
            if(String.valueOf(userSite).equals("BSN")){
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<PlaceMarkWs> placemark = new ArrayList<>();
            List<IncidentPlacemark> listincidentplacemark = placemarkRepo.findAll();
            for(IncidentPlacemark incplacemark : listincidentplacemark) {
                User user = (User) authentication.getPrincipal();
                PlaceMarkWs placemarkWs = new PlaceMarkWs();
                System.out.println(user.getResPersonnel().getOfficeSar().getKantorsarid());
                placemarkWs.setName(incplacemark.getPlacemarkName());
                placemarkWs.setDescription(incplacemark.getDescription());
                placemarkWs.setLatitude(incplacemark.getLatitude());
                placemarkWs.setLongitude(incplacemark.getLongitude());
                try {
                    placemarkWs.setIncidentid(incplacemark.getIncident().getIncidentid()); 
                } catch (NullPointerException e) {
                    placemarkWs.setIncidentid(null); 
                }
                
                placemarkWs.setImage(incplacemark.getImage());
                
                placemark.add(placemarkWs);
            }
            responseData.setData(placemark);
            } else{
                responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<PlaceMarkWs> placemark = new ArrayList<>();
            List<IncidentPlacemark> listincidentplacemark = placemarkRepo.findAllByKansarID(userSite);
            for(IncidentPlacemark incplacemark : listincidentplacemark) {
                User user = (User) authentication.getPrincipal();
                PlaceMarkWs placemarkWs = new PlaceMarkWs();
                System.out.println(user.getResPersonnel().getOfficeSar().getKantorsarid());
                placemarkWs.setName(incplacemark.getPlacemarkName());
                placemarkWs.setDescription(incplacemark.getDescription());
                placemarkWs.setLatitude(incplacemark.getLatitude());
                placemarkWs.setLongitude(incplacemark.getLongitude());
                try {
                    placemarkWs.setIncidentid(incplacemark.getIncident().getIncidentid()); 
                } catch (NullPointerException e) {
                    placemarkWs.setIncidentid(null); 
                }
                
                placemarkWs.setImage(incplacemark.getImage());
                
                placemark.add(placemarkWs);
            }
            responseData.setData(placemark);
            }
            return ResponseEntity.ok(responseData);
        }catch(Exception e){
            Response response = new Response();
            response.setResponseCode(ResponseCode.GENERAL_ERROR);
            response.setResponseMessage(e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
}
