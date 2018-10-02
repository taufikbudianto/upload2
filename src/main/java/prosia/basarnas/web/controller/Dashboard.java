/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.ReverseGeocodeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.UtiBeaconCompositeRepo;
import prosia.basarnas.web.model.InsidenEntity;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class Dashboard extends AbstractManagedBean implements Serializable {

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private UtiBeaconCompositeRepo utiBeaconCompositeRepo;

    private List<InsidenEntity> insidenEntitys;
    private MapModel mapModel;
    private Marker marker;
    private String centerRevGeoMap;
    private Integer zoomRevGeoMap;
    private Coordinate coordinateLat;
    private Coordinate coordinateLng;

    public Dashboard() {
        coordinateLat = new Coordinate();
        coordinateLng = new Coordinate();
        coordinateLat.setType(Boolean.TRUE);
        coordinateLng.setType(Boolean.FALSE);
    }

    @PostConstruct
    public void init() {
        insidenEntitys = new ArrayList<>();
        mapModel = new DefaultMapModel();
        setDataInsiden();
        setSampleModel();
        setDistressModel();
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        //addPopUpMessage("", marker.getTitle());
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_INFO, "", marker.getTitle()));
    }

    public void onReverseGeocode(ReverseGeocodeEvent event) {
        List<String> addresses = event.getAddresses();
        LatLng coord = event.getLatlng();
        mapModel = new DefaultMapModel();
        if (addresses != null && !addresses.isEmpty()) {
            centerRevGeoMap = coord.getLat() + "," + coord.getLng();
            mapModel.addOverlay(new Marker(coord, addresses.get(0)));
        }
    }

    private void setDataInsiden() {
        InsidenEntity ie = new InsidenEntity();
        ie.setSubyek("Longsor di Trans Sulawesi");
        ie.setKansar("Kansar Surabaya");
        insidenEntitys.add(ie);
        ie = new InsidenEntity();
        ie.setSubyek("Banjir Bandang Kab. Luwu");
        ie.setKansar("Kansar Pusat");
        insidenEntitys.add(ie);
        ie = new InsidenEntity();
        ie.setSubyek("Kapal Nelayan terbalik Selat Madura");
        insidenEntitys.add(ie);
    }

    private void setSampleModel() {
        centerRevGeoMap = "-1.105428, 120.0475485";
        zoomRevGeoMap = 4;
        List<Incident> listIncident = incidentRepo.findByStatus("Open");
        if (!listIncident.isEmpty()) {
            for (Incident incident : listIncident) {
                if (incident.getLatitude() != null && incident.getLongitude() != null) {
                    String decription = new StringBuilder("<b><u>Detail Insiden</u></b>")
                            .append("<br><b>Nama Insiden</b> : ").append(
                            incident.getIncidentname())
                            .append("<br><b>Latitude</b> : ").append(
                            coordinateLat.getConvertDdToDms(String.valueOf(incident.getLatitude()), Boolean.TRUE))
                            .append("<br><b>Longitude</b> : ").append(
                            coordinateLng.getConvertDdToDms(String.valueOf(incident.getLongitude()), Boolean.FALSE))
                            .toString();
                    LatLng coord = new LatLng(Double.valueOf(incident.getLatitude()), Double.valueOf(incident.getLongitude()));
                    String iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/icon/redlist.png");
                    mapModel.addOverlay(new Marker(coord, "Incident Number : " + incident.getIncidentnumber(),
                            decription, iconPath));
                }
            }
        }
    }

    private void setDistressModel() {
        centerRevGeoMap = "-1.105428, 120.0475485";
        zoomRevGeoMap = 4;
        for (UtiBeaconComposite utibeaconcomposite : utiBeaconCompositeRepo.findAllByIsDeletedDateCreatedIncidentId()) {
            if (StringUtils.isNotBlank(utibeaconcomposite.getLatitude())
                    || StringUtils.isNotBlank(utibeaconcomposite.getLongitude())) {
                LatLng coord = new LatLng(Double.valueOf(utibeaconcomposite.getLatitude()), Double.valueOf(utibeaconcomposite.getLongitude()));
                mapModel.addOverlay(new Marker(coord, "Beacon ID : " + utibeaconcomposite.getBeaconid(), "", "http://maps.google.com/mapfiles/ms/micons/red-pushpin.png"));
            }
        }
    }

    public void showMap() {
        RequestContext.getCurrentInstance().execute("PF('idnGmap').reverseGeocode(-5.0405666,112.6851581)");
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
