/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.UtiBeaconElemental;
import prosia.basarnas.repo.UtiBeaconElementalsRepo;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class DistressElementalMBean implements InitializingBean {

    private LazyDataModelJPA<UtiBeaconElemental> lazyDataModelJPA;

    private UtiBeaconElemental elemental;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UtiBeaconElementalsRepo utiBeaconElementalsRepo;
    private String field;
    private String value;
    private Date valueDate;
    private Integer valueInput;
    private Boolean showButton;
    private Boolean showCombo;
    private Boolean showButtonInput;
    private Boolean disabled;
    private Boolean isShowDetailElemental;
    private Coordinate latitude;
    private Coordinate longitude;
    private Coordinate gpsLatitude;
    private Coordinate gpsLongitude;

    public DistressElementalMBean() {
        initCoordinate();
        initGpsCoordinate();
        elemental = new UtiBeaconElemental();
        isShowDetailElemental = false;
        valueInput = 2;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(utiBeaconElementalsRepo) {

            @Override
            protected long getDataSize() {
                return utiBeaconElementalsRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return utiBeaconElementalsRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    private Specification<UtiBeaconElemental> whereQuery(
            final String field,
            final String value) {

        List<Predicate> predicates = new ArrayList<>();

        return new Specification<UtiBeaconElemental>() {

            @Override
            public Predicate toPredicate(Root<UtiBeaconElemental> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value) || valueDate != null) {

                    switch (field) {
                        case "msgNumber":
                            predicates.add(cb.like(cb.lower(root.<String>get("msgNumber")),
                                    getLikePattern(value.trim())));
                            break;
                        //return orTogether(predicates, cb);

                        case "beaconID":
                            predicates.add(cb.like(cb.lower(root.<String>get("beaconID")),
                                    getLikePattern(value.trim())));
                            break;
                         case "transmitDtg":
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String tanggal = formatter.format(valueDate);
                                String firstDate = Tanggal.dateTimeStringConvert(Tanggal.stringDateConvert(tanggal));
                                String secondDate = Tanggal.dateTimeStringConvert(Tanggal.addDayToDate(Tanggal.stringDateConvert(tanggal), 1));
                                predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(field), formatter.parse(firstDate)));
                                predicates.add(cb.lessThan(root.<Date>get(field), formatter.parse(secondDate)));
                            } catch (ParseException ex) {
                                logger.error("Parse Exception : {}", ex.getMessage());
                            }
                            break;
                        case "satID":
                            predicates.add(cb.like(cb.lower(root.<String>get("spacecraftID")),
                                    getLikePattern(value.trim())));
                            break;
                        case "repMcc":
                            predicates.add(cb.like(cb.lower(root.<String>get("reportMcc")),
                                    getLikePattern(value.trim())));
                            break;
                        case "destMcc":
                            predicates.add(cb.like(cb.lower(root.<String>get("destMcc")),
                                    getLikePattern(value.trim())));
                            break;
                    }

                }
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public String transmitDtgFormat(UtiBeaconElemental bc) {
        String transmitDtg = "";
        SimpleDateTimeZoneFormat sdf = new SimpleDateTimeZoneFormat();
        sdf.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT_X);
        Calendar cal = Calendar.getInstance();
        if (bc.getTransmitDtg() != null) {
            cal.setTime(bc.getTransmitDtg());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        transmitDtg = sdf.format(cal);
        return transmitDtg;
    }

    public String TCAFormat(UtiBeaconElemental bc) {
        String TCA = "";
        SimpleDateTimeZoneFormat sdf = new SimpleDateTimeZoneFormat();
        sdf.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT_X);
        Calendar cal = Calendar.getInstance();
        if (bc.getTransmitDtg() != null) {
            cal.setTime(bc.getTca());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        TCA = sdf.format(cal);
        return TCA;
    }

    public void chengeToCombo() {
        value = "";

    }

    public void reset() throws Exception {
        field = "ownerName";
        value = "";
        showCombo = false;
        valueInput = 2;
        afterPropertiesSet();
        RequestContext.getCurrentInstance().execute("PF('listWidget2').getPaginator().setPage(0)");
    }
    
    public void changeValueSearch() {
        if (field.equals("transmitDtg")) {
            valueInput = 1;
        } else {
            valueInput = 2;
        }
    }

    public void viewDetailElemental(UtiBeaconElemental elementalID, Boolean edited) {
        isShowDetailElemental = true;
        this.elemental = elementalID;

        logger.debug("Elemental ID : {}", elemental.getElementalID());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        initCoordinate();
        viewCoordinate();
//        RequestContext.getCurrentInstance().reset("idDetailElementals");
//        RequestContext.getCurrentInstance().update("idDetailElementals");
//        RequestContext.getCurrentInstance().execute("PF('widgetDetailElementals').show()");
    }

    public void hideForm() {
        isShowDetailElemental = false;
    }

    public void initCoordinate() {
        latitude = new Coordinate();
        longitude = new Coordinate();
        latitude.setType(true);
        longitude.setType(false);
    }

    public void initGpsCoordinate() {
        gpsLatitude = new Coordinate();
        gpsLongitude = new Coordinate();
        gpsLatitude.setType(true);
        gpsLongitude.setType(false);
    }

    public void viewCoordinate() {
        if (elemental.getLatitude() != null
                || StringUtils.isNotBlank(elemental.getLatitude())) {
            latitude.setType(true);
            latitude.setCounter(1);
            Double lat = Double.parseDouble(elemental.getLatitude());
            latitude.setDecimalDegrees(lat);
            latitude.converter();
            latitude.setCounter(3);
        }
        if (elemental.getLongitude() != null
                || StringUtils.isNotBlank(elemental.getLongitude())) {
            longitude.setType(false);
            longitude.setCounter(1);
            Double longi = Double.parseDouble(elemental.getLongitude());
            longitude.setDecimalDegrees(longi);
            longitude.converter();
            longitude.setCounter(3);
        }
    }
}
