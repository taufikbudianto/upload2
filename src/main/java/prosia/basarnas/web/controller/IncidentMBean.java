package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
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
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.BeaconCompositeRepo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPersonnelHistoryRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.service.IncidentService;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class IncidentMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<Incident> dataModel;
    private Incident incident;
    private List<SelectItem> listKantorSar;
    private Boolean showButton;
    private Boolean disabled;
    private Integer showIncidentDetail;
    private MstKantorSar mstKantorSar;
    private Integer lamainsiden;
    private Integer lamaoperasi;
    private Integer totalKorban;
    private String field;
    private String value;
    private Boolean showCombo;
    private Boolean disableTab;
    private Boolean disableForm;
    private Boolean isEdit;
    private String message;
    private String sightingId;
    private String compositeId;
    private String currentId;
    private Integer activeIndex;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean isDriftCalculation;
    private String[] selectedKansar;
    private Boolean isNasional;

    @Autowired
    private IncidentService incidentService;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private IncidentNationalRepo incidentNationalRepo;
    @Autowired
    private OfficeSarService officeSarService;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;
//    @Autowired
//    private CoordinateMBean coordinateMBean;
    @Autowired
    private IncidentAssetRepo incidentAssetRepo;
    @Autowired
    private ResAssetRepo resAssetRepo;
    @Autowired
    private IncidentPersonnelRepo incidentpersonnelRepo;
    @Autowired
    private ResPersonnelRepo resPersonnelRepo;
    @Autowired
    private ResPersonnelHistoryRepo resPersonnelHistoryRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;
    @Autowired
    private SightingAndHearingRepo sightingRepo;
    @Autowired
    private BeaconCompositeRepo compositeRepo;

    @Autowired
    private DataSource dataSource;
    private String BEACONID;
    private String sId;
    private String i;
    private String bId;

    public IncidentMBean() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
//        kansar = 0;
        refresh();
        sId = "";
        i = "";
        bId = "";
    }

    public final void setMessageForDialog() {
        StringBuilder builder = new StringBuilder();
        builder.append("Apakah anda yakin menutup insiden pada tanggal ").append(getTanggalSekarang()).append(" ?");
        //builder.append("&lt;br/&gt;");
        //builder.append("Anda tidak dapat melakukan perubahan data pada insiden yang telah di-close");
        message = builder.toString();
    }

//    public void multiKansar() {
//        value = "";
//        if (skala.equals("nasional") == true) {
//            kansar = 1;
//        } else if (skala.equals("daerah") == true) {
//            kansar = 0;
//        }
//    }
    public final void totalKorban() {
        validateJumlahKorban();
        totalKorban = incident.getDeathpeople() + incident.getHeavyinjuredpeople()
                + incident.getLightinjuredpeople() + incident.getLostpeople()
                + incident.getSurvivedpeople();
    }

    public void validateJumlahKorban() {
        if (incident.getDeathpeople() == null) {
            incident.setDeathpeople(0);
        }
        if (incident.getHeavyinjuredpeople() == null) {
            incident.setHeavyinjuredpeople(0);
        }
        if (incident.getLightinjuredpeople() == null) {
            incident.setLightinjuredpeople(0);
        }
        if (incident.getLostpeople() == null) {
            incident.setLostpeople(0);
        }
        if (incident.getSurvivedpeople() == null) {
            incident.setSurvivedpeople(0);
        }
    }

    public List<SelectItem> getListKantorSar() {
        if (listKantorSar == null) {
            listKantorSar = new ArrayList<>();
            kantorSarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.ZERO).stream().forEach((kantorSar) -> {
                listKantorSar.add(new SelectItem(kantorSar.getKantorsarid(), kantorSar.getKantorsarname()));
            });
        }
        return listKantorSar;
    }

    public void setData() {
        isDriftCalculation = false;
        // get parameters
        Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        i = parameterMap.get("i");
        sId = parameterMap.get("sightId");
        logger.debug("sId : {}", sId);
        if (sId != null) {
            sightingId = sId;
            UtiSighting us = sightingRepo.findOne(sId);
            incident.setRemarks(us.getRemarks());
        }

        bId = parameterMap.get("compositeId");
        logger.debug("bId : {}", bId);
        if (bId != null) {
            compositeId = bId;
            UtiBeaconComposite uc = compositeRepo.findOne(Integer.valueOf(bId));
            incident.setUsersiteid(uc.getKantorsarid().getKantorsarid());
            incident.setLatitude(uc.getLatitude());
            incident.setLongitude(uc.getLongitude());
            incident.setStartdate(uc.getTransmitdtg());
            viewCoordinate();
            //incident.setIncidenttype(uc.);
        }
        showIncidentDetail = i == null ? 1 : Integer.parseInt(i);
//        if (incidentId != null) {
//            Incident getIncident = incidentRepo.findOne(incidentId);
//            initialize(getIncident);
//        }

        dataModel = new LazyDataModelJPA(incidentRepo) {
            @Override
            protected long getDataSize() {
                return incidentRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentRepo.findAll(whereQuery(), request);
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setData();
    }

    public String getStringTipeIncident(Integer incidentType) {
        String string = "";
        if (incidentType != null) {
            switch (incidentType) {
                case 1:
                    string = ProsiaConstant.KECELAKAAN_LAUT;
                    break;
                case 2:
                    string = ProsiaConstant.KECELAKAAN_UDARA;
                    break;
                case 3:
                    string = ProsiaConstant.MUSIBAH;
                    break;
                case 4:
                    string = ProsiaConstant.BENCANAA;
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStatusOperasi(String status, Incident inc) {
        return status + "[" + dayStatus(inc) + "]";
    }

    public String getTanggalSekarang() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(new Date());
    }

    public Date getMinDate() {
        return Calendar.getInstance().getTime();
    }

    //Method untuk mengembalikan string untuk label status
    private String dayStatus(Incident incident) {
        Calendar currCal = Calendar.getInstance();
        Calendar incCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date tgl = new Date();
        Date closeDate = new Date();
        long dayDiff = 0;
        if (incident.getStatus().equalsIgnoreCase(ProsiaConstant.OPEN) && incident.getStartdate() != null) {
            if (incident.getStartopsdate() == null) {
                tgl = new Date();
                dayDiff = 0;
            } else {
                tgl = incident.getStartopsdate();
                dayDiff = 1;
            }
            incCal.setTime(tgl);
            if (incident.getClosedate() == null) {
                closeDate = new Date();
            } else {
                closeDate = incident.getClosedate();
            }
            currCal.setTime(closeDate);
            while (incCal.before(currCal)) {
                incCal.add(Calendar.DAY_OF_MONTH, 1);
                dayDiff++;
            }
        } else if ((incident.getStatus().equalsIgnoreCase(ProsiaConstant.CLOSE) || incident.getStatus().equalsIgnoreCase(ProsiaConstant.SUSPEND)) && incident.getStartdate() != null && incident.getClosedate() != null) {
            //incCal.setTime(incident.getStartDate());
            if (incident.getStartopsdate() == null) {
                tgl = new Date();
                dayDiff = 0;
            } else {
                tgl = incident.getStartopsdate();
                dayDiff = 1;
            }
            incCal.setTime(tgl);
            currCal.setTime(incident.getClosedate());
            while (incCal.before(currCal)) {
                incCal.add(Calendar.DAY_OF_MONTH, 1);
                dayDiff++;
            }
        }
        return "H . " + dayDiff;
    }

    public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }

    public void showDriftCalculation() {
        isDriftCalculation = true;
        showIncidentDetail = 3;
    }

    public void prepareShowIncidentDetail(Integer status, Incident inc) {
        logger.debug("INCIDENT : {}", inc);
        logger.debug("STATUS : {}", status);
        //incident = new Incident();
        //disableTab = true;
        showIncidentDetail = status;
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        if (inc != null && showIncidentDetail == 2) {
            initialize(inc);
        } else if (showIncidentDetail == 1) {
            refresh();
        }
//        String param = null;
//        if (status == 1) {
//            param = "";
//        } else if (status == 2 && inc == null) {
//            param = "?i=" + status;
//        } else {
//            param = "?i=" + status + "&incId=" + inc.getIncidentid();
//        }
//
//        try {
//            logger.debug("param : {}", param);
//            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
//                    .getCurrentInstance().getExternalContext().getRequestContextPath()
//                    + "/incident/incident/list.xhtml" + param);
//        } catch (IOException ex) {
//            logger.warn("Failed to redirect page : {}", ex.getMessage());
//        }
    }

    public void initialize(Incident inc) {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        if (inc.getStatus().equals(ProsiaConstant.CLOSE)) {
            disableForm = true;
        }
        incident = inc;
        isEdit = true;
        disableTab = false;
        totalKorban();
        calculateDays();
        viewCoordinate();
        isNasional = inc.getIncidentScala() != null && inc.getIncidentScala() == 1;
        if (inc.getIncidentScala() != null && inc.getIncidentScala() == 1) {
            Integer next = 0;
            selectedKansar = new String[incidentNationalRepo.findByIncident(inc).size()];
            for (IncidentNational incNational : incidentNationalRepo.findByIncident(inc)) {
                selectedKansar[next] = incNational.getKantorSarId();
                next++;
            }
        }
        if (inc.getIncidentScala() == null) {
            inc.setIncidentScala(0);
        }
    }

    public void viewCoordinate() {
        if (incident.getLatitude() != null
                || StringUtils.isNotBlank(incident.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(incident.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (incident.getLongitude() != null
                || StringUtils.isNotBlank(incident.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(incident.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }

    private Specification<Incident> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<Incident>() {

            @Override
            public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "incidenttype":
                            predicates.add(cb.equal(root.<BigInteger>get(field), value));
                            break;
                        case "status":
                            predicates.add(cb.equal(root.<String>get(field), value));
                            break;
                        case "usersiteid":
                            if (!value.equals("ALL")) {
                                predicates.add(cb.equal(root.<String>get(field), value));
                            }
                            break;
                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                    getLikePattern(value.trim())));
                            break;
                    }
                }
                List<Order> sorts = new ArrayList<Order>();
                sorts.add(cb.desc(root.get("status")));
                sorts.add(cb.desc(root.get("createdDate")));

                if (currentId.equals("BSN")) {
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                    query.orderBy(sorts);
                } else {
                    predicates.add(cb.equal(root.<String>get("usersiteid"), currentId));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                    query.orderBy(sorts);
                }
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

    public void chengeToCombo() {
        logger.debug("Field : {}", field);
        value = "";
        showCombo = field.equals("incidenttype") || field.equals("status") || field.equals("usersiteid");
    }

    public void reset() {
        field = "incidentname";
        value = "";
        showCombo = false;
    }

    public void simpan() {
        calculateDays();
        setCoordinate();
        incident = incidentService.saveInsiden(incident, selectedKansar);
        if (incident != null) {
            if (isEdit) {
                addPopUpMessage("Sukses", "Insiden berhasil diubah");
            } else {
                addPopUpMessage("Sukses", "Insiden berhasil disimpan");
            }
            prepareShowIncidentDetail(2, incident);
            disableTab = false;
        } else {
            addPopUpMessage("Gagal", "Insiden gagal disimpan");
        }
    }

    public void setCoordinate() {
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        coordinateLatitude.converter();
        coordinateLongitude.converter();
        incident.setLatitude(coordinateLatitude.getValidDecimalDegrees() != null
                ? coordinateLatitude.getValidDecimalDegrees().toString() : null);
        incident.setLongitude(coordinateLongitude.getValidDecimalDegrees() != null
                ? coordinateLongitude.getValidDecimalDegrees().toString() : null);
    }

    public void hapusInsiden(Incident i) {
        logger.debug("InsidenId : {}", i.getIncidentid());
        incidentService.deleteIncident(i);
        addPopUpMessage("Sukses", "Insiden berhasil dihapus");
    }

    public void reOpenIncident(Incident inc) {
        incidentService.reOpenIncident(inc);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Re-Open Insiden Berhasil");
        refresh();
    }

    public void closingInsidenProcess(Incident inc) {
        logger.debug("INCIDENT : {}", inc.getIncidentid());
        disableForm = true;
        closingAsset(inc);
        closingPersonnel(inc);
        if (inc != null && inc.getIncidentid() != null) {
            inc.setStatus(ProsiaConstant.CLOSE);
            if (inc.getClosedate() == null) {
                inc.setClosedate(new Date());
                inc.setClosedatetimezone("G");
            }
            incidentRepo.save(inc);

            // update assignment end date at data incident personnel
            IncidentPersonnel personel = new IncidentPersonnel();
            personel.setIncident(inc);
            personel.setModifiedBy(menuNavMBean.getUserSession().getUserId().toString());
            List<IncidentPersonnel> list = incidentpersonnelRepo.findAllByIncident(personel.getIncident());
            for (IncidentPersonnel person : list) {
                person.setAssignmentEndDate(new Date());
                person.setModifiedBy(personel.getModifiedBy());
                incidentpersonnelRepo.save(person);
            }
        }
    }

    public void closingAsset(Incident inc) {
        List<IncidentAsset> assetList = incidentAssetRepo.findAllByIncident(inc);
        for (IncidentAsset asset : assetList) {
            System.out.println("asset.getAsset().getAssetid(): " + asset.getAsset().getAssetid());
            ResAsset resAsset = resAssetRepo.findOne(asset.getAsset().getAssetid());
            if (resAsset != null) {
                if (resAsset.getAssettypeid() != null && (resAsset.getAssettypeid().getIssru() != null && resAsset.getAssettypeid().getIssru() == 0)) {//JIKA SRU
                    resAsset.setGoodqty(resAsset.getGoodqty() == null
                            ? BigInteger.ONE : resAsset.getGoodqty().add(BigInteger.ONE));
                } else if (resAsset.getAssettypeid() != null && resAsset.getAssettypeid().getIssru() == 2) {
                    resAsset.setGoodqty(resAsset.getGoodqty() == null
                            ? asset.getGoodQty() : resAsset.getGoodqty().add(asset.getGoodQty()));
                    resAsset.setServicedqty(resAsset.getServicedqty() == null
                            ? asset.getServicedQty() : resAsset.getServicedqty().add(asset.getServicedQty()));
                }
                resAsset.setStatus(0);
                resAssetRepo.save(resAsset);
            }
        }
    }

    public void closingPersonnel(Incident inc) {
        List<IncidentPersonnel> personelList = incidentpersonnelRepo.findAllByIncident(inc);
        for (IncidentPersonnel personel : personelList) {
            ResPersonnel rp = resPersonnelRepo.findOne(personel.getPersonnel().getPersonnelID());
            if (rp != null) {
                rp.setStatus(0);
                resPersonnelRepo.save(rp);

                //personnel history
                ResPersonnelHistory ph = new ResPersonnelHistory();
                ph.setPersonnelHistoryID(formatHistoryId());
                ph.setIncident(inc);
                ph.setIncidentYear(Tanggal.dateStringConvert(inc.getStartdate()));
                ph.setIncidentName(inc.getIncidentname());
                ph.setIncidentDesc(inc.getRemarks());
                if (personel.getOperationalPosition() != null) {
                    ph.setIncidentPosition(personel.getOperationalPosition().getEmploymentpositionname());
                }
                ph.setPersonnel(rp);
                resPersonnelHistoryRepo.save(ph);

            }
        }
    }

    public String formatHistoryId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        String nextval = "";
        String historyId = "";

        List<ResPersonnelHistory> list = resPersonnelHistoryRepo.findAllByPersonnelHistoryIDContaining("CGK");
        if (list.isEmpty()) {
            //System.out.println("A");
            historyId = "CGK" + "-" + ProsiaConstant.SEQUENCE1_000000001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxassetcontactId = resPersonnelHistoryRepo.findPersonnelHistoryByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxassetcontactId.split("-");
            System.out.println("splitid[0] = " + splitId[0]);
            System.out.println("splitid[1] = " + splitId[1]);
            int next = Integer.valueOf(splitId[1]) + 1;
            int length = String.valueOf(next).length();
            System.out.println("next = " + next);
            System.out.println("\n length = " + length);
            switch (length) {
                case 1:
                    nextval = ProsiaConstant.SEQUENCE1_00000000 + next;
                    break;
                case 2:
                    nextval = ProsiaConstant.SEQUENCE1_0000000 + next;
                    break;
                case 3:
                    nextval = ProsiaConstant.SEQUENCE1_00000 + next;
                    break;
                case 4:
                    nextval = ProsiaConstant.SEQUENCE1_0000 + next;
                    break;
                case 5:
                    nextval = ProsiaConstant.SEQUENCE1_000 + next;
                    break;
                case 6:
                    nextval = ProsiaConstant.SEQUENCE1_00 + next;
                    break;
                case 7:
                    nextval = ProsiaConstant.SEQUENCE1_0 + next;
                    break;
                case 8:
                    nextval = "" + next;
                    break;
                default:
                    break;
            }
            historyId = "CGK" + "-" + nextval;

            //}
        }
        return historyId;
    }

    public void refresh() {
        incident = new Incident();
        incident.setStartdate(new Date());
        incident.setAlertedat(new Date());
        incident.setStatus(ProsiaConstant.OPEN);
        totalKorban();
        calculateDays();
        setMessageForDialog();
        showButton = true;
        disabled = false;
        showCombo = false;
        listKantorSar = null;
        showIncidentDetail = 1;
        disableTab = true;
        isEdit = false;
        disableForm = false;
        activeIndex = 0;
        isNasional = false;
    }

    public List<Incident> getIncidentOpen() {
        return incidentRepo.findByStatus(ProsiaConstant.OPEN);
    }

    public String getKantorSarByKantorSarId(String kantorSarId) {
        String kantorSar = "";
        MstKantorSar kansar = kantorSarRepo.findOne(kantorSarId);
        if (kansar != null) {
            kantorSar = kansar.getKantorsarname();
        }
        return kantorSar;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public final void calculateDays() {
        if (incident.getStartopsdate() != null
                && StringUtils.isBlank(incident.getStartopsdatetimezone())) {
            incident.setStartopsdatetimezone("GMT+07:00");
        }
        if (incident.getClosedate() != null
                && StringUtils.isBlank(incident.getClosedatetimezone())) {
            incident.setClosedatetimezone("GMT+07:00");
        }
        if (incident.getStartopsdate() == null && incident.getClosedate() != null) {
            lamaoperasi = 0;
            lamainsiden = calculateDifference(incident.getClosedate(), incident.getStartdate());
        }
        if ("Close".equals(incident.getStatus())) {
            //buat close insiden
            lamaoperasi = calculateDifference(incident.getClosedate(), incident.getStartdate());
            lamainsiden = calculateDifference(incident.getClosedate(), incident.getAlertedat());
            //insidenForm.getDtGenWaktuSelesai().setCalendar(tglSelesai, null);
        }
        if (incident.getStartopsdate() == null && incident.getClosedate() == null) {
            //jika pertama kali buat insiden
            lamaoperasi = 0;
            lamainsiden = calculateDifference(incident.getClosedate(), incident.getStartdate());
        } else {
            // buat status open
            lamaoperasi = calculateDifference(incident.getClosedate(), incident.getStartopsdate());
            lamainsiden = calculateDifference(incident.getClosedate(), incident.getStartdate());
        }
    }

    private int calculateDifference(Date a, Date b) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        if (a == null) {
            a = new Date();
        }
        if (b == null) {
            b = new Date();
        }
        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }
        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;
            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }
        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;
            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }
        return difference + 1;
    }

    public void reportPrint(Incident incident) throws ParseException {
        try {
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
            String user = getCurrentUser().getParty().getFullName();
            HashMap hm = new HashMap();
            String jrxml = null;
            if (incident.getIncidenttype() == 1) {
                jrxml = "/report/report_jasper/Inciden_Pelayaran.jrxml";
            } else if (incident.getIncidenttype() == 2) {
                jrxml = "/report/report_jasper/Inciden_Penerbangan.jrxml";
            } else {
                jrxml = "/report/report_jasper/Inciden_Lain.jrxml";
            }
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
            String dir = System.getProperty("user.dir").replace("\\", "\\\\");
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("INCIDENTID", incident.getIncidentid());
            hm.put("user", user);
            hm.put("dir", dir + "\\\\src\\\\main\\\\webapp\\\\report\\\\report_jasper\\\\");
            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"CetakPresensi.pdf\"");
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

    public void selectSkala() {
        isNasional = incident.getIncidentScala() == 1;
    }

    public void hideForm() {
        try {
           if (sId != null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
                        .getCurrentInstance().getExternalContext().getRequestContextPath()
                        + "/utilities/sighting_and_hearing/sighting/list.xhtml");
            }else if(bId != null){
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
                        .getCurrentInstance().getExternalContext().getRequestContextPath()
                        + "/utilities/distress/list.xhtml");
            }else {
                //showIncidentDetail = 1;
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext
                        .getCurrentInstance().getExternalContext().getRequestContextPath()
                        + "/incident/incident/list.xhtml");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
