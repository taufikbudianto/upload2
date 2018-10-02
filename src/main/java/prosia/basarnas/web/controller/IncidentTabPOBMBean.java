/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentPOB;
import prosia.basarnas.model.MstNegara;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentTabPOBRepo;
import prosia.basarnas.repo.MstNegaraRepo;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;

/**
 *
 * @author TOMY
 */
@Component
@Scope("view")

public @Data
class IncidentTabPOBMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private IncidentPOB incidentPOB;
    private Incident incident;
    private MstNegara mstNegara;
    private LazyDataModelJPA<IncidentPOB> dataModel;
    @Autowired
    private IncidentTabPOBRepo incidentTabPOBRepo;
    @Autowired
    private MstNegaraRepo mstNegaraRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private IncidentMBean incidentMBean;
    private String field;
    private String value;
    private String negara;
    private String kondisi;
    private String latitude;
    private String longitude;
    private String destination;
    private String textbox;
    private String usia;
    private String radiogender;
    private Boolean isDisabled;
    private Boolean showButton;
    private Date twPOB;
    private List<SelectItem> listCountry;
    private String nama;
    private Integer umur;
    private String jeniskelamin;
    private String countryName;
    private Boolean disabled;
    private Boolean showButtonInput;
    private Incident currIncident;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean isShowIncPob;

    @Autowired
    private IncidentRepo incidentRepo;

    public IncidentTabPOBMBean() {
        initCoordinate();
        incidentPOB = new IncidentPOB();
        currIncident = new Incident();
        isShowIncPob = false;
        twPOB = new Date();
        showButton = true;
        disabled = false;
        showButtonInput = true;
        negara = "";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currIncident = incidentMBean.getIncident();
        disabled = incidentMBean.getDisableForm();
        System.out.println("Curr Incident1 :" + currIncident);
        if (currIncident != null) {
            System.out.println("Curr Incident2 :" + currIncident.getIncidentid());
            setData(currIncident);
        }
    }

    public void setData(Incident incidentChecklistID) {
        dataModel = new LazyDataModelJPA(incidentTabPOBRepo) {
            @Override
            protected long getDataSize() {
                return incidentTabPOBRepo.count(whereQuery(field, value, incidentChecklistID));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentTabPOBRepo.findAll(whereQuery(field, value, incidentChecklistID), request);
            }
        };
    }

    private Specification<IncidentPOB> whereQuery(
            final String field,
            final String value,
            Incident incidentID) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<IncidentPOB>() {

            @Override
            public Predicate toPredicate(Root<IncidentPOB> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(
                            cb.like(cb.lower(root.<String>get(field)), getLikePattern(value))
                    );
                }
                predicates.add(cb.equal(root.<IncidentPOB>get("incident"), incidentID));
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
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

    public void hapusPOB(IncidentPOB i) {
        logger.debug("Incident POB : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        incidentTabPOBRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
        "Sukses", "Incident POB berhasil dihapus");
        addMessage("Sukses", "Incident POB berhasil dihapus");
        //RequestContext.getCurrentInstance().execute("PF('widgetIncident').show()");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public List<SelectItem> getListCountry() {
        if (listCountry == null) {
            listCountry = new ArrayList<>();
            mstNegaraRepo.findAll().stream().forEach((Country) -> {
                listCountry.add(new SelectItem(Country.getCountryName(), Country.getCountryName()));
            });
        }
        return listCountry;
    }

    public void changeRadioGender() {
        if ("Pria".equals(radiogender)) {
            isDisabled = true;
        } else {
            isDisabled = false;
        }
    }

    public void simpan() {
        if (incidentPOB.getPobID() == null) {
            incidentPOB.setPobID(formatPOBId());
        }
        incidentPOB.setIncident(currIncident);
        incidentPOB.setTimeFound(twPOB);
        incidentPOB.setGender(radiogender);
        incidentPOB.setAgeunit(usia);
        if (negara != null) {
            MstNegara countryname = mstNegaraRepo.findByCountryName(negara);
            System.out.println("id :" + countryname);
            //if (incidentPOB.getCountry() == null) {
            //}
            incidentPOB.setCountry(countryname);
        }
        setCoordinate();
        incidentPOB.setCondition(kondisi);
        incidentPOB.setDestination(textbox);
        incidentPOB.setIsdeleted(BigInteger.ZERO);
        incidentPOB.setCreatedBy(menuNavMBean.getUserSession().getUsername());
        incidentPOB.setDateCreated(new Date());
        incidentTabPOBRepo.save(incidentPOB);
        isShowIncPob = false;
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Incident POB berhasil disimpan");
    }

    public void batal() {
        incidentPOB = new IncidentPOB();
        negara = "";
    }

    public String formatPOBId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String pobId = "";

        List<IncidentPOB> lis = incidentTabPOBRepo.findAllByPobIDContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            pobId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxPOBId = incidentTabPOBRepo.findPobByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxPOBId.split("-");
            if (maxPOBId.contains(splitDate[0] + splitDate[1])) {
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
                pobId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                pobId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return pobId;
    }

    public void viewPOB(String pobID, Boolean edited) {
        isShowIncPob = true;
        RequestContext.getCurrentInstance().reset("incidentdetail:tabpob-content");
        RequestContext.getCurrentInstance().update("incidentdetail:tabpob-content");
        this.incidentPOB = incidentTabPOBRepo.findOne(pobID);
        nama = incidentPOB.getName();
        incidentPOB.getAge();
        usia = incidentPOB.getAgeunit();
        radiogender = incidentPOB.getGender();
        incidentPOB.getAddress();
        incidentPOB.getDestPhone();
        incidentPOB.getDestination();
        if (incidentPOB.getCountry() != null) {
            negara = incidentPOB.getCountry().getCountryName();
        } else {
            negara = "";
        }
        initCoordinate();
        viewCoordinate();
        incidentPOB.getLatitude();
        incidentPOB.getLongitude();
        incidentPOB.getVehicle();
        kondisi = incidentPOB.getCondition();
        incidentPOB.getTimefoundtimezone();
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        //RequestContext.getCurrentInstance().execute("PF('widgetDetailPerson').show()");
    }

    public void refresh() {
        incidentPOB = new IncidentPOB();
        usia = new String();
        radiogender = new String();
        negara = new String();
        kondisi = new String();
        destination = new String();
    }

    public void add() {
        isShowIncPob = true;
        RequestContext.getCurrentInstance().reset("incidentdetail:tabpob-content");
        RequestContext.getCurrentInstance().update("incidentdetail:tabpob-content");
        refresh();
        disabled = false;
        showButton = true;
        initCoordinate();
    }

    public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }

    public void initCoordinate() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void setCoordinate() {
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        coordinateLatitude.converter();
        coordinateLongitude.converter();
        incidentPOB.setLatitude(coordinateLatitude.getValidDecimalDegrees() != null
                ? coordinateLatitude.getValidDecimalDegrees().toString() : null);
        incidentPOB.setLongitude(coordinateLongitude.getValidDecimalDegrees() != null
                ? coordinateLongitude.getValidDecimalDegrees().toString() : null);
    }

    public void viewCoordinate() {
        if (incidentPOB.getLatitude() != null
                || StringUtils.isNotBlank(incidentPOB.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(incidentPOB.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (incidentPOB.getLongitude() != null
                || StringUtils.isNotBlank(incidentPOB.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(incidentPOB.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }

    public void actionLatitude() {
        System.out.println("actionLatitude");
        coordinateLatitude.converter();
        coordinateLatitude.setCounter(coordinateLatitude.getCounter() + 1);
        if (coordinateLatitude.getCounter() > 3) {
            coordinateLatitude.setCounter(1);
        }
    }

    public void actionLongitude() {
        System.out.println("actionLongitude");
        coordinateLongitude.converter();
        coordinateLongitude.setCounter(coordinateLongitude.getCounter() + 1);
        if (coordinateLongitude.getCounter() > 3) {
            coordinateLongitude.setCounter(1);
        }
    }
    
    public void hideForm() {
        isShowIncPob = false;
        disabled = false;
    }
}
