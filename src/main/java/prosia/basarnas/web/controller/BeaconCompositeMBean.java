/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.enumeration.BeaconType;
import prosia.basarnas.model.UtiAirvessel;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.repo.BeaconCompositeRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.RegistrasiBeaconRepo;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class BeaconCompositeMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<UtiBeaconComposite> lazyDataModelJPA;
    private String field;
    private String value;
    private String where;
    private UtiBeaconComposite composite;
    private UtiBeaconComposite currentsComposite;
    private UtiAirvessel airvessel;

    private Coordinate coordinateLat;
    private Coordinate coordinateLong;
    /*private String lat1;
    private String lat2;
    private String lat3;
    private String lat4;
    private String long1;
    private String long2;
    private String long3;
    private String long4;*/

    private Boolean detailPlb;
    private Boolean detailElt;
    private Boolean detailEpirb;
    private Boolean disabled;

    private String[] radio;
    private String[] toolsCom;

    @Autowired
    private BeaconCompositeRepo compositeRepo;
    @Autowired
    private RegistrasiBeaconRepo beaconRepo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private IncidentMBean incidentMBean;

    public BeaconCompositeMBean() {
        detailPlb = false;
        detailElt = false;
        detailEpirb = false;
        airvessel = new UtiAirvessel();
        disabled = false;
        initCoordinate();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //untuk menangkap composite id jika tambah insiden 
        //dari menu utilities->distress composite
        String compositeId = incidentMBean.getCompositeId();
        System.out.println("compositeId = " + compositeId);
        if (compositeId != null) {
            currentsComposite = compositeRepo.findOne(Integer.valueOf(compositeId));
            //composite = currentsComposite;
            currentsComposite.setIncidentid(incidentMBean.getIncident());
            compositeRepo.save(currentsComposite);
        }
        if (StringUtils.isNotBlank(incidentMBean.getIncident().getIncidentid())) {
            composite = compositeRepo.findByIncidentid(incidentMBean.getIncident());
            setData();
        }

        disabled = incidentMBean.getDisableForm();
        lazyDataModelJPA = new LazyDataModelJPA(compositeRepo) {
            @Override
            protected long getDataSize() {
                return compositeRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return compositeRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    private Specification<UtiBeaconComposite> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return (Root<UtiBeaconComposite> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (StringUtils.isNotBlank(value)) {
                predicates.add(cb.like(cb.lower(root.<String>get(field)),
                        getLikePattern(value)));
            }
            cb.notEqual(root.<Integer>get("isdeleted"), 1);
            //query.orderBy(cb.asc(root.get("approved")));
            return andTogether(predicates, cb);
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public String transmitDtgFormat(UtiBeaconComposite bc) {
        String transmitDtg = "";
        SimpleDateTimeZoneFormat sdf = new SimpleDateTimeZoneFormat();
        sdf.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT_X);
        Calendar cal = Calendar.getInstance();
        if (bc.getTransmitdtg() != null) {
            cal.setTime(bc.getTransmitdtg());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        transmitDtg = sdf.format(cal);
        return transmitDtg;
    }

//    public String latitudeFormat(String format) {
//        return LatitudeLongitude.latitideFormatted(format);
//    }
//
//    public String longitudeFormat(String format) {
//        return LatitudeLongitude.longitudeFormatted(format);
//    }
    public void showBeaconComposite() {
        RequestContext.getCurrentInstance().execute("PF('wgComposite').show()");
    }

    public void reset() {
        value = "";
    }

    public void getComposite(UtiBeaconComposite comp) {
        logger.debug("COMPOSITE_ID : {}", comp.getCompositeid());
        if (composite != null) {
            composite.setIncidentid(null);
            compositeRepo.save(composite);
        }
        composite = comp;
        composite.setIncidentid(incidentMBean.getIncident());
        compositeRepo.save(composite);
        setData();
        reset();
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Beacon berhasil ditautkan");
        RequestContext.getCurrentInstance().execute("PF('wgComposite').hide()");
    }

    public void setData() {
        if (composite != null) {
            logger.debug("BEACON_ID : {}", composite.getBeaconid());
            airvessel = beaconRepo.findOne(composite.getBeaconid());
            logger.debug("airvessel : {}", airvessel);
            if (airvessel == null) {
                airvessel = new UtiAirvessel();
                detailPlb = false;
                detailElt = false;
                detailEpirb = false;
            } else {
                logger.debug("BEACON_TYPE : {}", airvessel.getBeaconType());
                if (airvessel.getBeaconType().equals(BeaconType.PLB.name())) {
                    detailPlb = false;
                    detailElt = true;
                    detailEpirb = true;
                } else if (airvessel.getBeaconType().equals(BeaconType.ELT.name())) {
                    detailPlb = true;
                    detailElt = false;
                    detailEpirb = true;
                    if (airvessel.getComequipment() != null) {
                        String[] str = airvessel.getComequipment().replace(" ", "").split(",");
                        radio = str;
                    }
                } else {
                    detailPlb = true;
                    detailElt = true;
                    detailEpirb = false;
                    if (airvessel.getComequipment() != null) {
                        String[] str = airvessel.getComequipment().replace(" ", "").split(",");
                        toolsCom = str;
                    }
                }
            }
//            String[] lat = latitudeFormat(composite.getLatitude()).split(" ");
//            lat1 = lat[0].replace("°", "");
//            lat2 = lat[1].replace("'", "");
//            lat3 = lat[2].replace("\"", "");
//            lat4 = lat[3];
//            String[] longg = longitudeFormat(composite.getLongitude()).split(" ");
//            long1 = longg[0].replace("°", "");
//            long2 = longg[1].replace("'", "");
//            long3 = longg[2].replace("\"", "");
//            long4 = longg[3];
            initCoordinate();
            viewCoordinate();
        }
    }

    public void deleteComposite(UtiBeaconComposite com) {
        logger.debug("Hilangkan tautan beacon...");
        if (com != null) {
            com.setIncidentid(null);
            compositeRepo.save(com);
            clear();
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Tautan beacon berhasil dihilangkan");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan", "Insiden tidak memiliki beacon");
        }
    }

    public void clear() {
        composite = new UtiBeaconComposite();
        airvessel = new UtiAirvessel();
//        lat1 = "";
//        lat2 = "";
//        lat3 = "";
//        lat4 = "";
//        long1 = "";
//        long2 = "";
//        long3 = "";
//        long4 = "";
        initCoordinate();
        viewCoordinate();
        radio = null;
        toolsCom = null;
        detailPlb = false;
        detailElt = false;
        detailEpirb = false;
    }

    public void addIncidentFromComposites() {
        composite = (UtiBeaconComposite) getRequestParam("objList");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
                    .getCurrentInstance().getExternalContext().getRequestContextPath()
                    + "/incident/incident/list.xhtml?i=2&compositeId=" + composite.getCompositeid());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initCoordinate() {
        coordinateLat = new Coordinate();
        coordinateLong = new Coordinate();
        coordinateLat.setType(true);
        coordinateLong.setType(false);
    }

    public void viewCoordinate() {
        if (composite.getLatitude() != null
                || StringUtils.isNotBlank(composite.getLatitude())) {
            coordinateLat.setType(true);
            coordinateLat.setCounter(1);
            Double latitude = Double.parseDouble(composite.getLatitude());
            coordinateLat.setDecimalDegrees(latitude);
            coordinateLat.converter();
            coordinateLat.setCounter(3);
        }
        if (composite.getLongitude() != null
                || StringUtils.isNotBlank(composite.getLongitude())) {
            coordinateLong.setType(false);
            coordinateLong.setCounter(1);
            Double longitude = Double.parseDouble(composite.getLongitude());
            coordinateLong.setDecimalDegrees(longitude);
            coordinateLong.converter();
            coordinateLong.setCounter(3);
        }
    }

}
