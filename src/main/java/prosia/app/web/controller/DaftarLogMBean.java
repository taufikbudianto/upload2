/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentRadiogram;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.LogImagesRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.web.util.LatitudeLongitude;
//import prosia.basarnas.service.Tanggal;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class DaftarLogMBean extends AbstractManagedBean implements InitializingBean {

    @Value("${uploadFolder}")
    private String uploadFolder;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<IncidentLog> dataModel;
    private LazyDataModelJPA<LogImages> dataModelLogImages;
    private Boolean showButton;
    private Boolean disabled;
    private Incident incident;
    private IncidentLog incidentLog;
    private MstKantorSar mstKantorSar;
    private Date logdate;
    private String remarks;
    private String field;
    private String value;
    private String value2;
    private String longitude;
    private String latitude;
    private Integer imageid;
    private LogImages logImages;
    private String uuid;
    private Date twTgl;
    private int showCombo = 0;
    private List<SelectItem> listKantorSar;
    private IncidentRadiogram incidentRadiogram;
    private String attachment;
    //upload file
    private UploadedFile uploadfile;
    private Boolean isShowDaftarLogInput;
    private Boolean isShowDataLog;
    private List<LogImages> listImage;
    private List<String> gambar;
    private List<LogImages> dataDeleteImage;

    @Autowired
    private IncidentLogRepo incidentLogRepo;
    @Autowired
    private OfficeSarService officeSarService;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private LogImagesRepo logImagesRepo;

    public DaftarLogMBean() {
        this.incidentLog = new IncidentLog();
        incidentRadiogram = new IncidentRadiogram();
        showButton = true;
        logdate = new Date();
        remarks = null;
        showCombo = 1;
        attachment = null;
        isShowDaftarLogInput = false;
        isShowDataLog = false;
        listImage = new ArrayList<LogImages>();
        dataDeleteImage = new ArrayList<>();
        logImages = new LogImages();
        uuid = UUID.randomUUID().toString();
    }

    public void setData() {
        dataModel = new LazyDataModelJPA(incidentLogRepo) {
            @Override
            protected long getDataSize() {
                return incidentLogRepo.count(whereQuery(field, value, value2));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                //return incidentLogRepo.findAll(request);
                return incidentLogRepo.findAll(whereQuery(field, value, value2), request);
            }

        };
    }

    private Specification<IncidentLog> whereQuery(
            final String field,
            final String value,
            final String value2) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<IncidentLog>() {

            @Override
            public Predicate toPredicate(Root<IncidentLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "radiogram":
                            predicates.add(cb.and(
                                    cb.like(cb.lower(root.<IncidentRadiogram>get("radiogram")
                                            .<String>get("radiogramID")),
                                            getLikePattern(value))));
                            return orTogether(predicates, cb);
                        case "userSiteID":
                            predicates.add(cb.equal(root.<String>get(field), value));
                            break;
                        case "logdate":
                            Predicate logdate = cb.between(root.get("logDate"), convertDate(value), convertDate(value2));
                            predicates.add(logdate);
                            break;
                        case "modifydate":
                            Predicate modifydate = cb.between(root.get("lastModified"), convertDate(value), convertDate(value2));
                            predicates.add(modifydate);
                            break;
                        default:
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get(field)), getLikePattern(value))
                            );
                            break;
                    }
                }
                predicates.add(cb.notEqual(root.<String>get("status"), "Close"));
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));
                //predicates.add(cb.equal(root.<boolean>get("deleted"), false));
                return andTogether(predicates, cb);
            }

        };

    }

    public void viewlogImages(String logID) {
        System.out.println("logid1 :" + logID);
        // System.out.println("aa :" +logImagesRepo.count(whereQuerylogImages(logID)));
        // System.out.println("bb :" +logImagesRepo.findAll(whereQuerylogImages(logID)));
        if (logID != null) {
            System.out.println("a");
            this.dataModelLogImages = new LazyDataModelJPA(logImagesRepo) {

                @Override
                protected long getDataSize() {
                    System.out.println("aa :" + logImagesRepo.count(whereQuerylogImages(logID)));
                    return logImagesRepo.count(whereQuerylogImages(logID));
                }

                @Override
                protected Page getDatas(PageRequest request) {
                    System.out.println("bb");
                    return logImagesRepo.findAll(whereQuerylogImages(logID), request);
                }
            };
        }
    }

    private Specification<LogImages> whereQuerylogImages(final String logID) {
        System.out.println("logid2 :" + logID);
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<LogImages>() {

            @Override
            public Predicate toPredicate(Root<LogImages> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<IncidentLog>get("logID").<String>get("logID"), logID));
                System.out.println("a");
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

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public void viewDaftarLog(String incidentLogId, Boolean edited) {
        isShowDataLog = true;
        this.incidentLog = incidentLogRepo.findOne(incidentLogId);
        twTgl = incidentLog.getLogDate();
        remarks = incidentLog.getRemarks();
        System.out.println("incidentLogId :" + incidentLogId);
        disabled = edited;
        showButton = !edited;
        isShowDaftarLogInput = true;
        listImage = logImagesRepo.findByLogIDAndDeleted(incidentLog, false);
        if (listImage == null) {
            listImage = new ArrayList<>();
        }
        System.out.println("hasil dari listImage =" + listImage);
    }

    public void simpanDaftarLog() {
        System.out.println("b");
        if (incidentLog.getLogType() != null && incidentLog.getRemarks() != null) {
            if (incidentLog.getLogID() == null) {
                incidentLog.setLogID(formatclassId());
            }
            incidentLog.setLogDate(twTgl);
            incidentLog.setUserSiteID(ProsiaConstant.SITES);
            incidentLog.setDateCreated(new Date());
            incidentLog.setStatus(ProsiaConstant.OPEN);
            incidentLog.setDeleted(false);
            incidentLogRepo.save(incidentLog);

            String filePath = "LOG" + File.separator + incidentLog.getLogID() + File.separator;
            String filePathSource = "TEMP_LOG" + File.separator + uuid + File.separator;
            File destDir = null;
            File srcDir = null;
            for (LogImages li : listImage) {
                System.out.println("size : " + listImage.size());
                String temp[] = li.getPathname().split("\\\\");
                li.setLogID(incidentLog);
                li.setPathname(filePath + temp[2]);
                li.setDeleted(false);
                logImagesRepo.save(li);

                //
                // An existing directory to copy.
                //
                System.out.println("temp = " + temp[2]);
                String source = filePathSource + temp[2];
                System.out.println("source : " + uploadFolder + source);
                srcDir = new File(uploadFolder + source);

                //System.out.println("directoryListing :" + directoryListing.length);
                //
                // The destination directory to copy to. This directory
                // doesn't exists and will be created during the copy
                // directory process.
                //
                if (logImages.getPathname() != null) {
                    String tempDest[] = logImages.getPathname().split("\\\\");
                    String destination = tempDest[0] + File.separator + tempDest[1];
                    System.out.println("destination: " + destination);
                    //System.out.println("dest : " + uploadFolder + tempDest[0] + File.separator + tempDest[1]);
                    destDir = new File(uploadFolder + filePath);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }

                    try {
                        //
                        // Copy source directory into destination directory
                        // including its child directories and files. When
                        // the destination directory is not exists it will
                        // be created. This copy process also preserve the
                        // date information of the file.
                        //

                        FileUtils.moveFileToDirectory(srcDir, destDir, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            //update status isDeleted
            for (LogImages logImages : dataDeleteImage) {
                logImages.setDeleted(true);
                logImagesRepo.save(logImages);
            }

            refresh();
            isShowDaftarLogInput = false;
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Log berhasil disimpan");
        }
    }

    public void hapusDaftarLog(IncidentLog i) {
        i.setDeleted(true);
        incidentLogRepo.save(i);
        addMessage("Sukses", "Log berhasil dihapus");
    }

    public void upload(FileUploadEvent e) {
        synchronized (this) {
            try {
                logImages = new LogImages();
                System.out.println("---runApplication--" + e.getFile());
                uploadfile = e.getFile();
                String filePath = "TEMP_LOG" + File.separator + uuid + File.separator;
                System.out.println("Upload Path " + filePath);
                File file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;

                if (uploadfile != null) {
                    bytes = uploadfile.getContents();
                    String filename = FilenameUtils.getName(uploadfile.getFileName());
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(uploadFolder + filePath + filename)));
                    stream.write(bytes);
                    stream.close();

                    logImages.setPathname(filePath + uploadfile.getFileName());
                    listImage.add(logImages);
                    System.out.println("Path : " + logImages.getPathname());
                }
                RequestContext.getCurrentInstance().update("daftarlog-content:form-daftarLog:listImg");
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));

            }
        }
    }

    public void batal() {
        refresh();
    }

    private void refresh() {
        this.incidentLog = new IncidentLog();
        listImage = new ArrayList<>();
        dataDeleteImage = new ArrayList<>();
        incidentRadiogram = new IncidentRadiogram();
        logdate = new Date();
        remarks = null;
        attachment = null;
        twTgl = new Date();
    }

    public void add() {
        System.out.println("aaaa");
        refresh();
        disabled = false;
        showButton = true;
        isShowDaftarLogInput = true;
    }

    public String formatclassId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String classId = "";

        List<IncidentLog> lis = incidentLogRepo.findAllBylogIDContaining(ProsiaConstant.SITES);

        if (lis.isEmpty()) {
            System.out.println("A");
            classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            System.out.println("B");
            String maxId = incidentLogRepo.findClassByMaxId(ProsiaConstant.SITES);
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
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
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                classId = ProsiaConstant.SITES + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return classId;
    }

    public String getIncidentByIncidentId(String incidentId) {
        try {
            return incidentRepo.findOne(incidentId).getIncidentnumber();
        } catch (Exception ex) {
            return null;
        }
    }

    public String getKantorSarByKantorSarId(String kantorSarId) {
        return kantorSarRepo.findOne(kantorSarId).getKantorsarname();
    }

    public List<SelectItem> getListKantorSar() {
        if (listKantorSar == null) {
            listKantorSar = new ArrayList<>();
            officeSarService.getOfficeSar().stream().forEach((kantorSar) -> {
                listKantorSar.add(new SelectItem(kantorSar.getKantorsarid(), kantorSar.getKantorsarname()));
            });
        }
        return listKantorSar;
    }

    public void changeToCombo() {
        value = "";
        if (field.equals("logType") == true) {
            showCombo = 1;
        } else if (field.equals("userSiteID") == true) {
            showCombo = 2;
        } else if (field.equals("logdate") == true) {
            showCombo = 3;
        } else if (field.equals("modifydate") == true) {
            showCombo = 4;
        } else {
            showCombo = 0;
        }
    }

    private String convertDateToString(String dateString) {
        try {
            String convertedDateString = null;
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = parseFormat.parse(dateString);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            convertedDateString = format.format(date);
            return convertedDateString;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private Date convertDate(String dateString) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = parseFormat.parse(dateString);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            return date;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setData();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }

    public void hideForm() {
        isShowDaftarLogInput = false;
    }

    public void hapusImage(LogImages images) {
        listImage.remove(images);
        dataDeleteImage.add(images);
        addMessage("Sukses", "Image berhasil dihapus");
    }

    public void showImage(String images) throws IOException {
        System.out.println("hasilssssss =" + uploadFolder + images);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(uploadFolder + images);
    }

}
