package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import lombok.Cleanup;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.service.SightingService;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.SightingConverter;
import prosia.basarnas.web.util.Tanggal;

@Controller
@Scope("view")
public @Data
class IncidentSightingMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<UtiSighting> dMSighting;
    private LazyDataModelJPA<UtiSighting> dMSightingIncident;
    private UtiSighting sighting;
    private UtiSighting currentsSighting;
    private IncidentLog incidentLog;
    private String field;
    private String value;
    private Date valueDate;
    private Boolean showCombo;
    private Boolean disableOtherObjectType;
    private Date responPertama;
    private String responPertamaTimeZone;
    private String waktuRespon;
    private String headerDetail;
    private Boolean disableTab;
    private Integer valueInput;
    private Boolean isEdited;
    private Boolean disableForm;
    private Boolean isShowDetail;
    private Integer activeTabIndex;
    private Coordinate latitudePelapor;
    private Coordinate longitudePelapor;
    private Coordinate latitudeObject;
    private Coordinate longitudeObject;
    private String SIGHTINGID;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private IncidentMBean incidentMBean;
    @Autowired
    private SightingAndHearingRepo sightingRepo;
    @Autowired
    private SightingService sightingService;
    @Autowired
    private IncidentLogRepo incidentLogRepo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private MenuNavMBean navMBean;
    @Autowired
    private MapMBean mapMBean;

    public IncidentSightingMBean() {
        latitudePelapor = new Coordinate();
        longitudePelapor = new Coordinate();
        latitudeObject = new Coordinate();
        longitudeObject = new Coordinate();
        latitudePelapor.setType(true);
        longitudePelapor.setType(false);
        latitudeObject.setType(true);
        longitudeObject.setType(false);
        sighting = new UtiSighting();
        sighting.setObjectDtg(new Date());
        showCombo = false;
        valueInput = 3;
        disableOtherObjectType = true;
        responPertama = null;
        responPertamaTimeZone = null;
        waktuRespon = null;
        disableTab = true;
        isEdited = false;
        activeTabIndex = 0;
        isShowDetail = true;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        disableForm = incidentMBean.getDisableForm();
        //coordinateMBean2.setDisabled(incidentMBean.getDisableForm());
        String sightingId = incidentMBean.getSightingId();
        logger.debug("Current Incident for incident sighting : {}", incidentMBean.getIncident().getIncidentid());

        //untuk menangkap sighting id jika tambah insiden 
        //dari menu utilities->sighting & hearing
        if (sightingId != null) {
            logger.debug("sightingId : {}", sightingId);
            currentsSighting = sightingRepo.findOne(sightingId);
            if (currentsSighting != null) {
                currentsSighting.setIncident(incidentMBean.getIncident());
                sightingRepo.save(currentsSighting);
            }
        }

        dMSightingIncident = new LazyDataModelJPA(sightingRepo) {
            @Override
            protected long getDataSize() {
                return sightingRepo.count(whereQueryForIncident());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return sightingRepo.findAll(whereQueryForIncident(), request);
            }
        };

        dMSighting = new LazyDataModelJPA(sightingRepo) {
            @Override
            protected long getDataSize() {
                return sightingRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return sightingRepo.findAll(whereQuery(), request);
            }
        };
    }

    public void deleteSighting() {
        sighting = (UtiSighting) getRequestParam("objList");
        sighting.setIsDeleted(true);
        sightingRepo.save(sighting);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Sighting Berhasil dihapus.");
    }

    public void addSightingToIncident(UtiSighting sighting) {
        logger.debug("Sighting : {}", sighting);
        if (sighting != null) {
            sighting.setIncident(incidentMBean.getIncident());
            sightingRepo.save(sighting);
        }
        reset();
        RequestContext.getCurrentInstance().execute("PF('wgBrowseSighting').hide()");
    }

    public void addSighting() {
        if (StringUtils.isBlank(incidentMBean.getIncident().getIncidentid()) && sighting.getIncident() == null) {
            sighting.setIncident(null);
        } else if (StringUtils.isNotBlank(incidentMBean.getIncident().getIncidentid()) && sighting.getIncident() == null) {
            sighting.setIncident(incidentMBean.getIncident());
        }
        if (StringUtils.isBlank(sighting.getSightingId())) {
            sighting.setCreatedBy(navMBean.getUserSession().getUserId().toString());
        } else {
            sighting.setModifiedBy(navMBean.getUserSession().getUserId().toString());
        }
        //untuk set latitude & longitude
        setCoordinate();
        sightingService.saveSighting(sighting);
        if (isEdited) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Update sighting berhasil");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Tambah sighting berhasil");
        }
        if (StringUtils.isNotBlank(incidentMBean.getIncident().getIncidentid())) {
            RequestContext.getCurrentInstance().execute("PF('wgDetailSighting').hide()");
        } else {
            isShowDetail = true;
            //RequestContext.getCurrentInstance().execute("PF('detailSighting').hide()");
        }
    }

    public void setCoordinate() {
        latitudePelapor.setType(true);
        longitudePelapor.setType(false);
        latitudeObject.setType(true);
        longitudeObject.setType(false);
        latitudePelapor.converter();
        longitudePelapor.converter();
        latitudeObject.converter();
        longitudeObject.converter();
        sighting.setObserverLatitude(latitudePelapor.getValidDecimalDegrees() != null
                ? latitudePelapor.getValidDecimalDegrees().toString() : null);
        sighting.setObserverLongitude(longitudePelapor.getValidDecimalDegrees() != null
                ? longitudePelapor.getValidDecimalDegrees().toString() : null);
        sighting.setObjectLatitude(latitudeObject.getValidDecimalDegrees() != null
                ? latitudeObject.getValidDecimalDegrees().toString() : null);
        sighting.setObjectLongitude(longitudeObject.getValidDecimalDegrees() != null
                ? longitudeObject.getValidDecimalDegrees().toString() : null);
    }

    public void reset() {
        value = "";
    }

    public void clearForm() {
        sighting = new UtiSighting();
        sighting.setObjectDtg(new Date());
    }

    public void chengeToCombo() {
        value = "";
        showCombo = field.equals("objectType");
    }

    private Specification<UtiSighting> whereQueryForIncident() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<UtiSighting>() {
            @Override
            public Predicate toPredicate(Root<UtiSighting> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<Incident>get("incident"), incidentMBean.getIncident()));
                cq.orderBy(cb.desc(root.get("dateCreated")));
                predicates.add(cb.notEqual(root.<Boolean>get("isDeleted"), true));
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<UtiSighting> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<UtiSighting>() {

            @Override
            public Predicate toPredicate(Root<UtiSighting> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value) || valueDate != null) {
                    switch (field) {
                        case "objectType":
                        case "status":
                            predicates.add(cb.equal(root.<Integer>get(field), value));
                            break;
                        case "objectDtg":
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
                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                    getLikePattern(value.trim())));
                            break;
                    }
                }
                predicates.add(cb.notEqual(root.<Boolean>get("isDeleted"), true));
                query.orderBy(cb.desc(root.get("dateCreated")));
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

    public void showBrowseSighting() {
        RequestContext.getCurrentInstance().execute("PF('wgBrowseSighting').show()");
        disableForm = false;
    }

    public void showSightingDetail() {
        sighting = (UtiSighting) getRequestParam("objList");
        activeTabIndex = 0;
        latitudePelapor = new Coordinate();
        longitudePelapor = new Coordinate();
        latitudeObject = new Coordinate();
        longitudeObject = new Coordinate();
        latitudePelapor.setType(true);
        longitudePelapor.setType(false);
        latitudeObject.setType(true);
        longitudeObject.setType(false);
        if (sighting == null) {
            clearForm();
            isEdited = false;
            disableTab = true;
            headerDetail = "Tambah Sighting and Hearing";
        } else {
            changeObjectType();
            viewCoordinate();
            setDataStatus();
            isEdited = true;
            disableTab = false;
            headerDetail = "Ubah Sighting and Hearing";
        }
        if (StringUtils.isNotBlank(incidentMBean.getIncident().getIncidentid())) {
            RequestContext.getCurrentInstance().reset("incidentdetail:sighting-dtl");
            RequestContext.getCurrentInstance().update("incidentdetail:sighting-dtl");
            RequestContext.getCurrentInstance().execute("PF('wgDetailSighting').show()");
        } else {
            isShowDetail = false;
            RequestContext.getCurrentInstance().reset("sighting-content:sighting-detail");
            RequestContext.getCurrentInstance().update("sighting-content:sighting-detail");
            //RequestContext.getCurrentInstance().execute("PF('detailSighting').show()");
        }
    }

    public void viewInMap() {
        System.out.println("masuk");
        LatLng coord = new LatLng(Double.valueOf(
                sighting.getObjectLatitude()),
                Double.valueOf(sighting.getObjectLongitude()));
        String iconPath = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestContextPath()
                .concat("/resources/basarnas/images/sarcoreapp/sighting.png");
        mapMBean.setIsBatasKansar(false);
        mapMBean.setIsIconInsiden(false);
        mapMBean.setMapModel(new DefaultMapModel());
        mapMBean.getMapModel().addOverlay(new Marker(coord, "Nama Observer : "
                + sighting.getObserverName() + " Keterangan Objek : " 
                + sighting.getObjectRemarks(), "", iconPath));

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("window.open('/map/map/list.xhtml',"
                + "'_blank')");
    }

    public void viewCoordinate() {
        if (sighting.getObserverLatitude() != null
                || StringUtils.isNotBlank(sighting.getObserverLatitude())) {
            latitudePelapor.setType(true);
            latitudePelapor.setCounter(1);
            Double observerLatitude = Double.parseDouble(sighting.getObserverLatitude());
            latitudePelapor.setDecimalDegrees(observerLatitude);
            latitudePelapor.converter();
            latitudePelapor.setCounter(3);
        }
        if (sighting.getObserverLongitude() != null
                || StringUtils.isNotBlank(sighting.getObserverLongitude())) {
            longitudePelapor.setType(false);
            longitudePelapor.setCounter(1);
            Double observerLongitude = Double.parseDouble(sighting.getObserverLongitude());
            longitudePelapor.setDecimalDegrees(observerLongitude);
            longitudePelapor.converter();
            longitudePelapor.setCounter(3);
        }
        if (sighting.getObjectLatitude() != null
                || StringUtils.isNotBlank(sighting.getObjectLatitude())) {
            latitudeObject.setType(true);
            latitudeObject.setCounter(1);
            Double objectLatitude = Double.parseDouble(sighting.getObjectLatitude());
            latitudeObject.setDecimalDegrees(objectLatitude);
            latitudeObject.converter();
            latitudeObject.setCounter(3);
        }
        if (sighting.getObjectLongitude() != null
                || StringUtils.isNotBlank(sighting.getObjectLongitude())) {
            longitudeObject.setType(false);
            longitudeObject.setCounter(1);
            Double objectLongitude = Double.parseDouble(sighting.getObjectLongitude());
            longitudeObject.setDecimalDegrees(objectLongitude);
            longitudeObject.converter();
            longitudeObject.setCounter(3);
        }
    }

    public void showDetailLog(IncidentLog incLog) {
        if (incLog != null) {
            logger.debug("incLog : {}", incLog.getLogID());
            incidentLog = incLog;
        }
        RequestContext.getCurrentInstance().execute("PF('wgLogDetail').show();");
    }

    public void hideDetailLog() {
        if (incidentLog != null) {
            incidentLog = null;
        }
        RequestContext.getCurrentInstance().execute("PF('wgLogDetail').hide();");
    }

    public void hideDetailSighting(Boolean fromIncident) {
        clearForm();
        if (fromIncident) {
            RequestContext.getCurrentInstance().execute("PF('wgDetailSighting').hide();");
        } else {
            isShowDetail = true;
            //RequestContext.getCurrentInstance().execute("PF('detailSighting').hide();");
        }
    }

    public void changeObjectType() {
        disableOtherObjectType = sighting.getObjectType() != 7;
    }

    public void setDataStatus() {
        if (sighting != null) {
            List<IncidentLog> iLog = incidentLogRepo.findBySighting(sighting);
            if (iLog != null && iLog.size() > 0 && iLog.get(0).getLogDate() != null && iLog.get(0).getLogDateTimeZone() != null) {
                responPertama = iLog.get(0).getLogDate();
                responPertamaTimeZone = iLog.get(0).getLogDateTimeZone();
                //Perhitungan waktu respon
                Date wktCatat = sighting.getDateCreated();
                Date wktRespon = iLog.get(0).getLogDate();
                long diff = 0;
                long diffHours = 0;
                long diffMinutes = 0;
                long diffDays = 0;
                String hari = "";
                String jam = "";
                String menit = "";
                if (wktRespon.after(wktCatat)) {
                    diff = wktRespon.getTime() - wktCatat.getTime();
                    diffHours = diff / (60 * 60 * 1000);
                    diffDays = diffHours / 24;
                    diffMinutes = diff / (1000 * 60) - diffHours * 60;
                    diffHours = diffHours - (24 * diffDays);
                    if (diffDays != 0) {
                        hari = diffDays + " hari ";
                    }
                    if (diffHours != 0) {
                        jam = diffHours + " jam ";
                    }
                    if (diffMinutes != 0) {
                        menit = diffMinutes + " menit";
                    }
                    waktuRespon = hari + jam + menit;
                }
            } else {
                responPertama = null;
                responPertamaTimeZone = null;
                waktuRespon = null;
            }
        }
    }

    public String dateTimeStringConvert(Date date) {
        return Tanggal.dateTimeStringConvert(date);
    }

    public String getObjectTypeString(Integer objectType) {
        return SightingConverter.objectTypeToString(objectType);
    }

    public String getStatusString(Integer status) {
        return SightingConverter.getStatusString(status);
    }

    public String getEntryTypeString(Boolean crm) {
        return SightingConverter.getStringEntryType(crm);
    }

    public void changeValueSearch() {
        if (field.equals("observerLocation") || field.equals("remarks")) {
            valueInput = 1;
        } else if (field.equals("objectType") || field.equals("status")) {
            valueInput = 2;
        } else {
            valueInput = 3;
        }
    }

    public void resetSearch() {
        field = "";
        value = "";
        valueDate = null;
        valueInput = 3;
    }

    public void addIncidentFromSighting() {
        sighting = (UtiSighting) getRequestParam("objList");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
                    .getCurrentInstance().getExternalContext().getRequestContextPath()
                    + "/incident/incident/list.xhtml?i=2&sightId=" + sighting.getSightingId());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getIncident(Incident i) {
        if (i != null) {
            sighting.setIncident(i);
            RequestContext.getCurrentInstance().execute("PF('pilihInsiden').hide();");
        }
    }

    public void chooseIncident() {
        RequestContext.getCurrentInstance().execute("PF('pilihInsiden').show();");
    }

    public void removeIncident() {
        sighting.setIncident(null);
    }

    public void actionLatitudePelapor() {
        System.out.println("actionLatitudePelapor");
        latitudePelapor.converter();
        latitudePelapor.setCounter(latitudePelapor.getCounter() + 1);
        if (latitudePelapor.getCounter() > 3) {
            latitudePelapor.setCounter(1);
        }
    }

    public void actionLongitudePelapor() {
        System.out.println("actionLongitudePelapor");
        longitudePelapor.converter();
        longitudePelapor.setCounter(longitudePelapor.getCounter() + 1);
        if (longitudePelapor.getCounter() > 3) {
            longitudePelapor.setCounter(1);
        }
    }

    public void actionLatitudeObject() {
        System.out.println("actionLatitudeObject");
        latitudeObject.converter();
        latitudeObject.setCounter(latitudeObject.getCounter() + 1);
        if (latitudeObject.getCounter() > 3) {
            latitudeObject.setCounter(1);
        }
    }

    public void actionLongitudeObject() {
        System.out.println("actionLatitudeObject");
        longitudeObject.converter();
        longitudeObject.setCounter(longitudeObject.getCounter() + 1);
        if (longitudeObject.getCounter() > 3) {
            longitudeObject.setCounter(1);
        }
    }

    public String getUserFromReport() {
        return getCurrentUser().getParty().getFullName();
    }

    public void reportPrint() throws ParseException {
        try {
            System.out.println("----masuk print--");
            String id = (String) getRequestParam("obj");
            String user = getCurrentUser().getParty().getFullName();
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
            HashMap hm = new HashMap();
            // String jrxml = "/report/report_jasper/laporanincident.jrxml";
            String jrxml = "/report/report_jasper/Insiden_singlesighting.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");
            File fileJrxml = new File(pathJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            //parameter atau field yang akan dijadikan filter
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("SightingId", id);
            hm.put("user", user);
            hm.put("currentDate", now.format(new Date()));

            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"CetakSightingAndHearing.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();

            facescontext.responseComplete();
        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
}
