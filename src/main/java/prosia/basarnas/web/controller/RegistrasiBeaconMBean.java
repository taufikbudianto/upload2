/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
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
import prosia.basarnas.enumeration.BeaconType;
import prosia.basarnas.enumeration.ComequipmentELT;
import prosia.basarnas.enumeration.ComequipmentEPIRB;
import prosia.basarnas.model.BeaconModel;
import prosia.basarnas.model.Manufacturer;
import prosia.basarnas.model.UtiAirvessel;
import prosia.basarnas.repo.MstBeaconModelRepo;
import prosia.basarnas.repo.MstManufacturerRepo;
import prosia.basarnas.repo.RegistrasiBeaconRepo;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class RegistrasiBeaconMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<UtiAirvessel> lazyDataModelJPA;
    private String field;
    private String value;
    private String where;
    private Boolean showCombo;
    private Integer activeTabIndexBeacon;
    //private Integer activeTabIndexBeaconLast;
    private UtiAirvessel airvessel;
    private Boolean isDetailProses;
    private Boolean isUbah;
    private Boolean detailPlb;
    private Boolean detailElt;
    private Boolean detailEpirb;
    private Boolean disableIdLama;
    private Boolean disableOtherManufacturer;
    private Boolean disableOtherBeaconModel;
    private Boolean disableOtherDetailUsage;
    private Boolean disableOtherUsageType;
    private Boolean disableOtherSailType;
    private Boolean disableOtherMachineType;
    private Boolean disableOtherToolsCom;
    private Boolean disableOtherRadio;
    private Boolean disabledTab;
    private List<SelectItem> listItemManufacturer;
    private List<Manufacturer> listManufacturer;
    private List<BeaconModel> listBeaconModel;
    private String manufacturerId;
    private String beaconModelId;
    //string array utk disimpan di comequipment
    private String[] radio;
    private String[] toolsCom;
    private String passenger;

    @Autowired
    private DataSource dataSource;
    private String BEACONID;

    @Autowired
    private MenuNavMBean navMBean;
    @Autowired
    private RegistrasiBeaconRepo beaconRepo;
    @Autowired
    private MstManufacturerRepo manufacturerRepo;
    @Autowired
    private MstBeaconModelRepo beaconModelRepo;
    @Autowired
    private MstBeaconModelRepo modelRepo;

    public RegistrasiBeaconMBean() {
        airvessel = new UtiAirvessel();
        airvessel.setBeaconType(BeaconType.PLB.name());
        showCombo = false;
        isDetailProses = false;
        detailPlb = true;
        detailElt = false;
        detailEpirb = false;
        disableIdLama = true;
        disableOtherManufacturer = false;
        disableOtherBeaconModel = false;
        disableOtherDetailUsage = true;
        disableOtherUsageType = true;
        disableOtherSailType = true;
        disableOtherMachineType = true;
        disableOtherToolsCom = true;
        disableOtherRadio = true;
        disabledTab = true;
        isUbah = false;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(beaconRepo) {
            @Override
            protected long getDataSize() {
                return beaconRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return beaconRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public void saved() {
        if (!isUbah && beaconRepo.findOne(airvessel.getBeaconId()) != null) {
            addMessage("Peringatan", "ID Beacon telah terdaftar");
        } else {
            logger.debug("IS_UBAH : {}", isUbah);

            if (isUbah) {
                airvessel.setModifiedBy(navMBean.getUserSession().getUserId().toString());
            }
            airvessel.setApproved(false);
            logger.debug("USER_ID : {}", navMBean.getUserSession().getUserId().toString());
            //airvessel.setPassenger(passenger != null || !passenger.equals("") ? Integer.parseInt(passenger) : null);
            airvessel.setCreatedBy(navMBean.getUserSession().getUserId().toString());
            airvessel.setApproved(false);
            //----------------------------------------------------------------//
            Manufacturer manufacturer = manufacturerRepo.findOne(manufacturerId);
            airvessel.setManufacturer(manufacturer);
            BeaconModel model = modelRepo.findOne(beaconModelId);
            airvessel.setBeaconModel(model);
            airvessel.setIsDeleted(false);
            airvessel.setDateCreated(new Date());
            //----------------------------------------------------------------//
            String checkboxChecked = "";
            if (airvessel.getBeaconType().equals(BeaconType.ELT.name())) {
                checkboxChecked = Arrays.toString(radio).substring(1, Arrays.toString(radio).length() - 1).replace(" ", "");
            } else if (airvessel.getBeaconType().equals(BeaconType.EPIRB.name())) {
                checkboxChecked = Arrays.toString(toolsCom).substring(1, Arrays.toString(toolsCom).length() - 1).replace(" ", "");
            }
            airvessel.setComequipment(checkboxChecked);
            logger.debug("COMEQUIPMENT : {}", airvessel.getComequipment());
            //------SIMPAN DATA------//
            beaconRepo.save(airvessel);
            addPopUpMessage("Sukses", "Data berhasil disimpan");
            isDetailProses = false;
            airvessel = new UtiAirvessel();
        }
    }

    public void approved() {
        airvessel.setApproved(true);
        airvessel.setRegistrationDate(new Date());
        airvessel.setBeaconRegNo(generateRegistrationNo(airvessel));
        beaconRepo.save(airvessel);
        addPopUpMessage("Sukses", "Data Berhasil disetujui");
        //--- UNTUK KIRIM EMAIL NOTIFIKASI ---//
        //EmailOutgoingES.insertEmailOutgoing(createEmailOutgoing(currentBeacon));
        //RequestContext.getCurrentInstance().execute("PF('wgApprove').hide()");
    }

    private String generateRegistrationNo(UtiAirvessel airvessel) {
        //String kansar = ApplicationPropertiesES.getCurrentSite();
        //--- CGK SET DEFAULT DARI PROPERTIES ---//
        String kansar = "CGK";
        Date date = new Date();
        if (airvessel.getDateCreated() != null) {
            date = airvessel.getDateCreated();
        }
        //String datetime = Tanggal.sarDateTimeStringConvert(date, ApplicationPropertiesES.getTimeZone());
        String datetime = Tanggal.sarDateTimeStringConvert(date, Tanggal.getTimeZone());
        return "SAR-" + kansar + "-" + datetime;
    }

    public String dateStringConvert(Date date) {
        return Tanggal.dateStringConvert(date);
    }

    public String getNameRegistrationType(Integer registrationType) {
        String registrationName = "";
        if (registrationType != null) {
            switch (registrationType) {
                case 1:
                    registrationName = "Baru";
                    break;
                case 2:
                    registrationName = "Perubahan Informasi";
                    break;
                case 3:
                    registrationName = "Penggantian";
                    break;
                default:
                    registrationName = "Tipe Registrasi Salah";
                    break;
            }
        }
        return registrationName;
    }

    //Tidak Ada, Kendaraan Darat, Kapal, Pesawat, Lain-lain
    public String getPlbType(Integer usageType) {
        String string = "";
        if (usageType != null) {
            switch (usageType) {
                case 1:
                    string = "Tidak Ada";
                    break;
                case 2:
                    string = "Kendaraan Darat";
                    break;
                case 3:
                    string = "Kapal";
                    break;
                case 4:
                    string = "Pesawat";
                    break;
                case 5:
                    string = "Lain-Lain";
                    break;
                case 6:
                    string = "Lain";
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStringA_Usage(Integer a_usage) {
        String string = "";
        if (a_usage != null) {
            switch (a_usage) {
                case 1:
                    string = "Komersial";
                    break;
                case 2:
                    string = "Non Komersial";
                    break;
                case 3:
                    string = "Militer";
                    break;
                case 4:
                    string = "Pemerintah";
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStringDetailUsage(Integer detailUsage) {
        String string = "";
        if (detailUsage != null) {
//            Berkemah, Berburu, Memancing, Lain-lain
            switch (detailUsage) {
                case 1:
                    string = "Berkemah";
                    break;
                case 2:
                    string = "Berburu";
                    break;
                case 3:
                    string = "Memancing";
                    break;
                case 4:
                    string = "Lain-Lain";
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStringUsageType(Integer usageType) {
        String string = "";
        if (usageType != null) {
            switch (usageType) {
                case 1:
                    string = "Baling-baling, Mesin Tunggal";
                    break;
                case 2:
                    string = "Baling-baling, Multi Mesin";
                    break;
                case 3:
                    string = "Jet, Mesin Tunggal";
                    break;
                case 4:
                    string = "Jet, Multi Mesin";
                    break;
                case 5:
                    string = "Helikopter";
                    break;
                case 6:
                    string = "Lain";
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStringSailType(Integer sailType) {
        String string = "";
        if (sailType != null) {
            switch (sailType) {
                case 1:
                    string = "Yach";
                    break;
                case 2:
                    string = "Schooner";
                    break;
                case 3:
                    string = "Sekoci";
                    break;
                case 4:
                    string = "Lain";
                    break;
                default:
                    break;
            }
        }
        return string;
    }

    public String getStringMachineType(Integer machineType) {
        String string = "";
        if (machineType != null) {
            if (machineType == 1) {
                string = "Manual";
            } else if (machineType == 2) {
                string = "Otomatis";
            }
        }
        return string;
    }

    public void showForm(UtiAirvessel ua, Boolean isUbah) {
        logger.debug("IS_UBAH : {}", isUbah);
        isDetailProses = true;
        this.isUbah = isUbah;
        if (ua != null) {
            airvessel = ua;
            manufacturerId = airvessel.getManufacturer() == null ? "" : airvessel.getManufacturer().getManufacturerId();
            beaconModelId = airvessel.getBeaconModel() == null ? "" : airvessel.getBeaconModel().getBeaconModelId();

            detailPlb = airvessel.getBeaconType().equals(BeaconType.PLB.name());
            detailElt = airvessel.getBeaconType().equals(BeaconType.ELT.name());
            detailEpirb = airvessel.getBeaconType().equals(BeaconType.EPIRB.name());

            activeTabIndexBeacon = 0;
            disableIdLama = airvessel.getRegistrationType() != 3;
            disableOtherManufacturer = !manufacturerId.isEmpty();
            disableOtherBeaconModel = !beaconModelId.isEmpty();
            if (detailPlb) {
                disableOtherDetailUsage = airvessel.getDetailUsage() == null || airvessel.getDetailUsage() != 4;
                disableOtherUsageType = airvessel.getUsageType() == null || airvessel.getUsageType() != 5;
            } else if (detailElt) {
                disableOtherUsageType = airvessel.getUsageType() == null || airvessel.getUsageType() != 6;
                disableOtherRadio = airvessel.getComequipment() == null || !airvessel.getComequipment().contains(ComequipmentELT.LAIN.name());
                if (airvessel.getComequipment() != null) {
                    String[] str = airvessel.getComequipment().split(",");
                    radio = str;
                }
            } else if (detailEpirb) {
                if (airvessel.getComequipment() != null) {
                    String[] str = airvessel.getComequipment().split(",");
                    toolsCom = str;
                }
                passenger = airvessel.getPassenger().toString();
                disableOtherSailType = airvessel.getSailType() == null || airvessel.getSailType() != 4;
                disableOtherMachineType = airvessel.getSailType() == null || airvessel.getMachineType() != 6;
                disableOtherToolsCom = airvessel.getComequipment() == null || !Arrays.toString(toolsCom).contains(ComequipmentEPIRB.LAIN.name());
            }
        } else {
            detailPlb = true;
            detailElt = false;
            detailEpirb = false;
        }
        
    }

    public void showFormApprove(String beaconId) {
        logger.debug("BEACON_ID : {} ", beaconId);
        airvessel = beaconRepo.findOne(beaconId);
        detailPlb = airvessel.getBeaconType().equals(BeaconType.PLB.name());
        detailElt = airvessel.getBeaconType().equals(BeaconType.ELT.name());
        detailEpirb = airvessel.getBeaconType().equals(BeaconType.EPIRB.name());
        approved();
        RequestContext.getCurrentInstance().execute("PF('wgApprove').show()");
    }

    public void hideForm() {
        airvessel = new UtiAirvessel();
        isDetailProses = false;
    }

    public void changeDetail() {
        activeTabIndexBeacon = 0;
        logger.debug("airvessel.getBeaconType() : {}", airvessel.getBeaconType());
        switch (airvessel.getBeaconType()) {
            case "PLB":
                detailPlb = true;
                detailElt = false;
                detailEpirb = false;
                break;
            case "ELT":
                detailPlb = false;
                detailElt = true;
                detailEpirb = false;
                break;
            case "EPIRB":
                detailPlb = false;
                detailElt = false;
                detailEpirb = true;
                break;
            default:
                break;
        }
    }

    public void changeRegistrasiType() {
        logger.debug("regType : {}", airvessel.getRegistrationType());
        disableIdLama = airvessel.getRegistrationType() != 3;
    }

    public void changeDetailUsage() {
        if (airvessel.getDetailUsage() != null) {
            logger.debug("detailUsage : {}", airvessel.getDetailUsage());
            disableOtherDetailUsage = airvessel.getDetailUsage() != 4;
        }
    }

    public void changeUsageType() {
        if (airvessel.getUsageType() != null) {
            logger.debug("beaconType : {}", airvessel.getBeaconType());
            logger.debug("usageType : {}", airvessel.getUsageType());
            if (airvessel.getBeaconType().equals(BeaconType.PLB.name())) {
                disableOtherUsageType = airvessel.getUsageType() != 5;
            } else if (airvessel.getBeaconType().equals(BeaconType.ELT.name())) {
                disableOtherUsageType = airvessel.getUsageType() != 6;
            }
            logger.debug("disableOtherUsageType : {}", disableOtherUsageType);
        }
    }

    public void changeSailType() {
        if (airvessel.getSailType() != null) {
            logger.debug("sailType : {}", airvessel.getSailType());
            disableOtherSailType = airvessel.getSailType() != 4;
        }
    }

    public void changeOtherToolsComp() {
        disableOtherToolsCom = !Arrays.toString(toolsCom).contains(ComequipmentEPIRB.LAIN.name());
    }

    public void changeOtherRadio() {
        logger.debug("disableOtherRadio : {}", disableOtherRadio);
        disableOtherRadio = !Arrays.toString(radio).contains(ComequipmentELT.LAIN.name());
    }

    public void changeMachineType() {
        if (airvessel.getMachineType() != null) {
            logger.debug("machineType : {}", airvessel.getMachineType());
            disableOtherMachineType = airvessel.getMachineType() != 6;
        }
    }

    public List<SelectItem> getListManufacturer() {
        if (listItemManufacturer == null) {
            listItemManufacturer = new ArrayList<>();
            listManufacturer = manufacturerRepo.findAllByIsDeleted(0);
            listManufacturer.stream().forEach((m) -> {
                listItemManufacturer.add(new SelectItem(m.getManufacturerId(), m.getManufacturerName()));
            });
        }
        return listItemManufacturer;
    }

    public void changeManufacuter() {
        if (manufacturerId.equals("")) {
            disableOtherManufacturer = false;
            listBeaconModel = null;
        } else {
            disableOtherManufacturer = true;
            listBeaconModel = beaconModelRepo.findAllByManufacturerAndIsDeleted(manufacturerRepo.findOne(manufacturerId), 0);
        }
        disableOtherBeaconModel = false;
    }

    public void changeBeaconModel() {
        disableOtherBeaconModel = !beaconModelId.equals("");
    }

    private Specification<UtiAirvessel> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return (Root<UtiAirvessel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (StringUtils.isNotBlank(value)) {
                switch (field) {
                    case "model":
                        predicates.add(cb.and(
                                cb.like(cb.lower(root.<String>get("otherBeaconModel")), getLikePattern(value.trim())),
                                cb.isNull(root.<BeaconModel>get("beaconModel"))
                        ));
                        predicates.add(cb.and(
                                cb.isNotNull(root.<BeaconModel>get("beaconModel")),
                                cb.like(cb.lower(root.<BeaconModel>get("beaconModel").<String>get("beaconModelName")),
                                        getLikePattern(value.trim()))));
                        return orTogether(predicates, cb);
                    case "pembuat":
                        predicates.add(cb.and(
                                cb.isNull(root.<Manufacturer>get("manufacturer")),
                                cb.like(cb.lower(root.<String>get("otherManufacturer")),
                                        getLikePattern(value.trim()))));
                        predicates.add(cb.and(
                                cb.isNotNull(root.<Manufacturer>get("manufacturer")),
                                cb.like(cb.lower(root.<Manufacturer>get("manufacturer")
                                        .<String>get("manufacturerName")),
                                        getLikePattern(value.trim()))));
                        return orTogether(predicates, cb);
                    case "registrationType":
                        predicates.add(cb.equal(root.<Integer>get(field), value));
                        break;
                    case "beaconType":
                        predicates.add(cb.equal(root.<String>get(field), value));
                        break;
                    default:
                        predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                getLikePattern(value.trim())));
                        break;
                }
            }
            predicates.add(cb.notEqual(root.<Boolean>get("isDeleted"), true));
            query.orderBy(cb.asc(root.get("approved")));
            return andTogether(predicates, cb);
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

    public void chengeToCombo() {
        value = "";
        showCombo = field.equals("registrationType") || field.equals("beaconType");
    }

    public void onTabChangeBeacon(TabChangeEvent event) {
        TabView tv = (TabView) event.getComponent();
        activeTabIndexBeacon = tv.getActiveIndex();
        logger.debug("activeTabIndex : {}", activeTabIndexBeacon);
        //loginBean.setApprovalTabIndex(activeTabIndexHome);
        //initiateDataTablePage();
    }

    public void hapusBeacon(UtiAirvessel u) {
        u.setIsDeleted(true);
        beaconRepo.save(u);
        addMessage("Sukses", "Data berhasil dihapus");
    }

    public void reset() {
        field = "ownerName";
        value = "";
        showCombo = false;
        RequestContext.getCurrentInstance().execute("PF('listWidget').getPaginator().setPage(0)");
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public void reportPrint() throws ParseException {
        try {
            airvessel = (UtiAirvessel) getRequestParam("obj");
//            String id = (String) getRequestParam("obj");
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
            String user = getCurrentUser().getParty().getFullName();
            HashMap hm = new HashMap();
            String jrxml = null;
          
            System.out.println("-----beaconType : "+ airvessel.getBeaconType());
            if (airvessel.getBeaconType().equals("PLB")) {
                jrxml = "/report/report_jasper/UTILITIES_beaconplb.jrxml";
            } else if (airvessel.getBeaconType().equals("EPIRB")) {
                jrxml = "/report/report_jasper/UTILITIES_beaconepirb.jrxml";
            } else if (airvessel.getBeaconType().equals("ELT")) {
                jrxml = "/report/report_jasper/UTILITIES_beaconelt.jrxml";
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
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("BEACONID", airvessel.getBeaconId());
            hm.put("user", user);
            hm.put("currentDate", now.format(new Date()));

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
}
