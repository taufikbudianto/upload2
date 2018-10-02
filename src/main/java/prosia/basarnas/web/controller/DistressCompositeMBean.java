/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import com.sun.mail.handlers.message_rfc822;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
import prosia.basarnas.model.UtiBeaconComposite;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.repo.CompositeBeaconRepo;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class DistressCompositeMBean extends AbstractManagedBean implements InitializingBean {

    private LazyDataModelJPA<UtiBeaconComposite> lazyDataModelJPA;

    private UtiBeaconComposite composite;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompositeBeaconRepo compositeBeaconRepo;
    @Autowired
    private OfficeSarService officeSarService;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    private String field;
    private String value;
    private String kantorsarname;
    private String currentId;
    private Date valueDate;
    private Integer showCombo;
    private Boolean showButton;
    private Boolean showButtonAssign;
    private Boolean showButtonInput;
    private Boolean disabled;
    private List<SelectItem> listKantorSar;
    private Boolean isShowDetailComposite;
    private Boolean isShowDetailCompositeInput;
    private Coordinate latitude;
    private Coordinate longitude;
    private Coordinate gpsLatitude;
    private Coordinate gpsLongitude;

    public DistressCompositeMBean() {
        initCoordinate();
        initGpsCoordinate();
        composite = new UtiBeaconComposite();
        isShowDetailComposite = false;
        showCombo = 0;
        showButtonAssign = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(compositeBeaconRepo) {

            @Override
            protected long getDataSize() {
                return compositeBeaconRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return compositeBeaconRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    private Specification<UtiBeaconComposite> whereQuery(
            final String field,
            final String value) {

        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<UtiBeaconComposite>() {

            @Override
            public Predicate toPredicate(Root<UtiBeaconComposite> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value) || valueDate != null) {

                    switch (field) {
                        case "msgNumber":
                            predicates.add(cb.like(cb.lower(root.<String>get("msgnumber")),
                                    getLikePattern(value.trim())));
                            break;
                        //return orTogether(predicates, cb);

                        case "beaconID":
                            predicates.add(cb.like(cb.lower(root.<String>get("beaconid")),
                                    getLikePattern(value.trim())));
                            break;

                        case "transmitdtg":
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

                        case "incNumber":
                            predicates.add(cb.like(cb.lower(root.<Incident>get("incidentid")
                                    .<String>get("incidentnumber")),
                                    getLikePattern(value.trim())));
                            break;
                        case "kansarName":
                            predicates.add(
                                    cb.like(cb.lower(root.<MstKantorSar>get("kantorsarid")
                                            .<String>get("kantorsarname")),
                                            getLikePattern(value.trim())));
                            break;
                    }

                }
                if (currentId.equals("BSN")) {
                    predicates.add(cb.equal(root.<Integer>get("isdeleted"), 0));
                    query.orderBy(cb.desc(root.get("compositeid")));
                } else {
                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), currentId));
                    predicates.add(cb.equal(root.<Integer>get("isdeleted"), 0));
                    query.orderBy(cb.desc(root.get("compositeid")));
                }
                return andTogether(predicates, cb);
            }
        };
    }

    public void chengeToCombo() {
        value = "";
        if (field.equals("kansarName") == true) {
            System.out.println("KANSAR");
            showCombo = 2;
        } else if (field.equals("transmitdtg") == true) {
            System.out.println("Transmit DTG");
            showCombo = 1;
        } else {
            System.out.println("NORMAL");
            showCombo = 0;
        }
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

    public String TCAFormat(UtiBeaconComposite bc) {
        String TCA = "";
        SimpleDateTimeZoneFormat sdf = new SimpleDateTimeZoneFormat();
        sdf.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT_X);
        Calendar cal = Calendar.getInstance();
        if (bc.getTransmitdtg() != null) {
            cal.setTime(bc.getTca());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        TCA = sdf.format(cal);
        return TCA;
    }

    public void reset() {
        field = "ownerName";
        value = "";
        showCombo = 0;
        RequestContext.getCurrentInstance().execute("PF('listWidget').getPaginator().setPage(0)");
//        showCombo = false;
    }

    public void viewDetailComposite(UtiBeaconComposite compositeID, Boolean edited) {
        isShowDetailComposite = true;
        showButtonAssign = false;
        RequestContext.getCurrentInstance().reset("beacon-content-composites:form-detail-composite");
        RequestContext.getCurrentInstance().update("beacon-content-composites:form-detail-composite");
        this.composite = compositeID;
        kantorsarname = composite.getKantorsarid().getKantorsarname();
        disabled = edited;
        showButton = !edited;
        initCoordinate();
        initGpsCoordinate();
        viewCoordinate();
        viewGpsCoordinate();
    }

    public void tambahInsiden() {
        //  UtiBeaconComposite pUtiBeacon = (UtiBeaconComposite) getRequestPrama();
    }

    public List<SelectItem> getListKantorSar() {
        if (listKantorSar == null) {
            listKantorSar = new ArrayList<>();
            officeSarService.getOfficeSar().stream().forEach((kantorSar) -> {
                listKantorSar.add(new SelectItem(kantorSar.getKantorsarname(), kantorSar.getKantorsarname()));
            });
        }
        return listKantorSar;
    }

    public void tambahComposite(UtiBeaconComposite comp, Boolean input) {
        showButton = true;
        showButtonAssign = true;
        disabled = false;
        kantorsarname = new String();
        isShowDetailComposite = true;
        RequestContext.getCurrentInstance().reset("beacon-content-composites:form-detail-composite");
        RequestContext.getCurrentInstance().update("beacon-content-composites:form-detail-composite");
        composite = new UtiBeaconComposite();
        initCoordinate();
        initGpsCoordinate();
    }

    public void assign() {
        if (composite.getCompositeid() == null) {
            composite.setCompositeid(composite.getCompositeid());
        }
        MstKantorSar sar = kantorSarRepo.findByKantorsarname(kantorsarname);
        composite.setKantorsarid(sar);
        compositeBeaconRepo.save(composite);
        addPopUpMessage("Sukses", "Berhasil Assign");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void hideForm() {
        isShowDetailComposite = false;
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
        if (composite.getLatitude() != null
                || StringUtils.isNotBlank(composite.getLatitude())) {
            latitude.setType(true);
            latitude.setCounter(1);
            Double lat = Double.parseDouble(composite.getLatitude());
            latitude.setDecimalDegrees(lat);
            latitude.converter();
            latitude.setCounter(3);
        }
        if (composite.getLongitude() != null
                || StringUtils.isNotBlank(composite.getLongitude())) {
            longitude.setType(false);
            longitude.setCounter(1);
            Double longi = Double.parseDouble(composite.getLongitude());
            longitude.setDecimalDegrees(longi);
            longitude.converter();
            longitude.setCounter(3);
        }
    }

    public void simpan() {
        Date date = new Date();

        Integer compId = null;
        setCoordinate();
        compId = compositeBeaconRepo.findMaxId();
        MstKantorSar kantor = kantorSarRepo.findByKantorsarname(kantorsarname);
        if (composite.getCompositeid() == null) {
            composite.setCompositeid(compId + 1);
        }
        if (composite.getTransmitdtg() == null) {
            composite.setTransmitdtg(date);
        }
        if (composite.getTca() == null) {
            composite.setTca(date);
        }
        if (composite.getDatecreated() == null) {
            composite.setDatecreated(date);
        }

        composite.setIsdeleted(0);
        composite.setKantorsarid(kantor);
        compositeBeaconRepo.save(composite);

        isShowDetailComposite = false;
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Data berhasil disimpan");
    }

    public void hapus(UtiBeaconComposite comp) {
        comp.setIsdeleted(1);
        compositeBeaconRepo.save(comp);
        addPopUpMessage("Sukses", "Composite berhasil dihapus");
    }

    public void viewGpsCoordinate() {
        if (composite.getGpslatitude() != null
                || StringUtils.isNotBlank(composite.getGpslatitude())) {
            gpsLatitude.setType(true);
            gpsLatitude.setCounter(1);
            Double lat = Double.parseDouble(composite.getGpslatitude());
            gpsLatitude.setDecimalDegrees(lat);
            gpsLatitude.converter();
            gpsLatitude.setCounter(3);
        }
        if (composite.getGpslongitude() != null
                || StringUtils.isNotBlank(composite.getGpslongitude())) {
            gpsLongitude.setType(false);
            gpsLongitude.setCounter(1);
            Double longi = Double.parseDouble(composite.getGpslongitude());
            gpsLongitude.setDecimalDegrees(longi);
            gpsLongitude.converter();
            gpsLongitude.setCounter(3);
        }
    }

    public void setCoordinate() {
        latitude.setType(true);
        longitude.setType(false);
        latitude.converter();
        longitude.converter();
        composite.setLatitude(latitude.getValidDecimalDegrees() != null
                ? latitude.getValidDecimalDegrees().toString() : null);
        composite.setLongitude(longitude.getValidDecimalDegrees() != null
                ? longitude.getValidDecimalDegrees().toString() : null);
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

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
