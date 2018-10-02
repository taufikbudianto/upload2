/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.UtiWeatherBmkg;
import prosia.basarnas.repo.UtiWeatherBmkgRepo;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author Angga
 */
@Controller
@Scope("view")
@Data
public class UtiWeatherBmkgMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private UtiWeatherBmkg cuaca;
    private List<UtiWeatherBmkg> listCuaca;
    private LazyDataModelJPA<UtiWeatherBmkg> lazyDataModelJPA;
    private Boolean showDetail;
    private Coordinate latitude;
    private Coordinate longitude;
    private Coordinate latitudeAkhir;
    private Coordinate longitudeAkhir;
    private Date currDate;
    private Date startDate;
    private Date endDate;
    private String field;
    private String value;

    @Autowired
    private UtiWeatherBmkgRepo cuacaRepo;

    public UtiWeatherBmkgMBean() {
        startDate = new Date();
        endDate = new Date();
        initCoordinate();
        initCoordinateAkhir();
//        initGpsCoordinate();
        cuaca = new UtiWeatherBmkg();
        showDetail = false;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(cuacaRepo) {
            @Override
            protected long getDataSize() {
                return cuacaRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return cuacaRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    private Specification<UtiWeatherBmkg> whereQuery(
            final String field,
            final String value) {
        System.out.println("whereQuery : " + value);
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<UtiWeatherBmkg>() {
            @Override
            public Predicate toPredicate(Root<UtiWeatherBmkg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //predicates.add(cb.equal(root.get("dateCreated"), currDate));
                if (endDate.before(startDate)) {
                    addPopUpMessage("Pesan", "Tanggal awal periode harus sebelum atau sama dengan tanggal akhir periode");
                } else {
                    if (StringUtils.isNotBlank(value)) {
                        switch (field) {
                            case "windDir":
                            case "windSpd":
                            case "wsc":
                            case "cuDir":
                            case "cuSpd":
                            case "ekp":
                            case "waveDir":
                            case "pTot":
                            case "hTot":
                            case "h1Per10":
                            case "h1Per100":
                            case "seaDir":
                            case "pSea":
                            case "hSea":
                            case "swellDir":
                            case "pSwell":
                            case "hSwell":
                                predicates.add(cb.equal(root.<Double>get(field), value));
                                break;

                            default:
                                predicates.add(
                                        cb.like(cb.lower(root.<String>get(field)), getLikePattern(value))
                                );
                                break;
                        }
                    }
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String tanggalAwal = formatter.format(startDate);
                        String tanggalAkhir = formatter.format(endDate);
                        String firstDate = Tanggal.dateTimeStringConvert(Tanggal.stringDateConvert(tanggalAwal));
                        String secondDate = Tanggal.dateTimeStringConvert(Tanggal.stringDateConvert(tanggalAkhir));
                        System.out.println("firstDate : " + firstDate);
                        System.out.println("secondDate : " + secondDate);
//                        predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("dateCreated"), formatter.parse(firstDate)));
//                        predicates.add(cb.lessThanOrEqualTo(root.<Date>get("dateCreated"), formatter.parse(secondDate)));
                        predicates.add(cb.between(root.<Date>get("dateCreated"),
                                 formatter.parse(firstDate),
                                 formatter.parse(secondDate)));
                    } catch (ParseException ex) {
                        logger.error("Parse Exception : {}", ex.getMessage());
                    }

                    latitude.setType(true);
                    latitude.converter();
                    latitudeAkhir.setType(true);
                    latitudeAkhir.converter();
                    if (latitude.getValidDecimalDegrees() != null && latitudeAkhir.getValidDecimalDegrees() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.<String>get("latitude"),
                                 (latitude.getValidDecimalDegrees().toString())));
                        predicates.add(cb.lessThanOrEqualTo(root.<String>get("latitude"),
                                 (latitudeAkhir.getValidDecimalDegrees().toString())));
                        System.out.println("latitude.getPrefix() + latitude.getDecimalDegrees().toString() : " 
                                + (latitude.getValidDecimalDegrees().toString()));
//                        predicates.add(
//                                cb.like(cb.lower(root.<String>get("latitude"))
//                                        , (latitude.getPrefix() + latitude.getDecimalDegrees().toString()))
//                        );
                    } else if (latitude.getValidDecimalDegrees() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.<String>get("latitude"),
                                 (latitude.getValidDecimalDegrees().toString())));
                    } else if (latitudeAkhir.getValidDecimalDegrees() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.<String>get("latitude"),
                                 (latitudeAkhir.getValidDecimalDegrees().toString())));
                    }

                    longitude.setType(true);
                    longitude.converter();
                    longitudeAkhir.setType(true);
                    longitudeAkhir.converter();
                    if (longitude.getValidDecimalDegrees() != null & longitudeAkhir.getValidDecimalDegrees() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.<String>get("longitude"),
                                 (longitude.getValidDecimalDegrees().toString())));
                        predicates.add(cb.lessThanOrEqualTo(root.<String>get("longitude"),
                                 (longitudeAkhir.getValidDecimalDegrees().toString())));
                    } else if (longitude.getValidDecimalDegrees() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.<String>get("longitude"),
                                 (longitude.getValidDecimalDegrees().toString())));
                    } else if (longitudeAkhir.getValidDecimalDegrees() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.<String>get("longitude"),
                                 (longitudeAkhir.getValidDecimalDegrees().toString())));
                    }
                }
                //predicates.add(cb.equal(root.get("dateCreated"), currDate));
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

    public void viewCuaca(UtiWeatherBmkg weatherId, Boolean lihat) {
        showDetail = lihat;
        this.cuaca = weatherId;
        initCoordinate();
        viewCoordinate();
        //RequestContext.getCurrentInstance().reset("j_id_u:detailcuaca");
        //RequestContext.getCurrentInstance().update("j_id_u:detailcuaca");
    }

    public void initCoordinate() {
        latitude = new Coordinate();
        longitude = new Coordinate();
        latitude.setType(true);
        longitude.setType(false);
    }

    public void initCoordinateAkhir() {
        latitudeAkhir = new Coordinate();
        longitudeAkhir = new Coordinate();
        latitudeAkhir.setType(true);
        longitudeAkhir.setType(false);
    }

    public void viewCoordinate() {
        if (cuaca.getLatitude() != null
                || StringUtils.isNotBlank(cuaca.getLatitude())) {
            latitude.setType(true);
            latitude.setCounter(1);
            Double lat = Double.parseDouble(cuaca.getLatitude());
            latitude.setDecimalDegrees(lat);
            latitude.converter();
            latitude.setCounter(3);
        }
        if (cuaca.getLongitude() != null
                || StringUtils.isNotBlank(cuaca.getLongitude())) {
            longitude.setType(false);
            longitude.setCounter(1);
            Double longi = Double.parseDouble(cuaca.getLongitude());
            longitude.setDecimalDegrees(longi);
            longitude.converter();
            longitude.setCounter(3);
        }
    }

    public void actionLatitude() {
        System.out.println("actionLatitude");
        latitude.converter();
        latitude.setCounter(latitude.getCounter() + 1);
        if (latitude.getCounter() > 3) {
            latitude.setCounter(1);
        }
    }

    public void actionLongitude() {
        System.out.println("actionLongitude");
        longitude.converter();
        longitude.setCounter(longitude.getCounter() + 1);
        if (longitude.getCounter() > 3) {
            longitude.setCounter(1);
        }
    }

    public void actionLatitudeAkhir() {
        System.out.println("actionLatitudeAkhir");
        latitudeAkhir.converter();
        latitudeAkhir.setCounter(latitudeAkhir.getCounter() + 1);
        if (latitudeAkhir.getCounter() > 3) {
            latitudeAkhir.setCounter(1);
        }
    }

    public void actionLongitudeAkhir() {
        System.out.println("actionLongitudeAkhir");
        longitudeAkhir.converter();
        longitudeAkhir.setCounter(longitudeAkhir.getCounter() + 1);
        if (longitudeAkhir.getCounter() > 3) {
            longitudeAkhir.setCounter(1);
        }
    }
    
    public void hideForm(){
        showDetail = false;
    }

}
