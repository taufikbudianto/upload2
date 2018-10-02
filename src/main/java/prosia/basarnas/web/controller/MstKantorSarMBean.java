/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.faces.application.FacesMessage;
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
import org.springframework.stereotype.Component;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.MstKantorSarImages;
import prosia.basarnas.repo.MstKantorSarImagesRepo;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstKantorSarMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstKantorSar kantorSar;
    private MstKantorSarImages kanSarImages;
    private UploadedFile uploadFile;
    private File file;
    private File srcDir;
    private File destDir;
    private LazyDataModelJPA<MstKantorSar> lazyDataModelJPA;

    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;

    @Autowired
    private MstKantorSarImagesRepo kanSarImagesRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private OfficeSarService officeSarService;

    @Value("${uploadFolder}")
    private String uploadFolder;
    private String field;
    private String value;
    private String kantorsarid;
    private String kantorsarname;
    private String address;
    private String longitude;
    private String latitude;
    private String phonenumber1;
    private String phonenumber2;
    private String phonenumber3;
    private String faxnumber;
    private String radiofrequency;
    private String headerDetail;
    private String filePath;
    private String filePathSource;
    private String fileName;
    private String source;
    private String destination;
    private String temp[];
    private String tempDest[];
    private String uuid;
    private String currentId;
    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean disabled;
    private Boolean disabled1;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean isReadOnly;
    private Boolean showCombo;
    private Boolean showDetail;

    private List<MstKantorSar> listKanSar;
    private List<SelectItem> listKantorSar;
    private List<MstKantorSarImages> listKanSarImages;
    private List<MstKantorSarImages> dataDeleteImages;
    private List<MstKantorSarImages> dataDeleteImagesTemp;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstKantorSarRepo) {
            @Override
            protected long getDataSize() {
                return mstKantorSarRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstKantorSarRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public MstKantorSarMBean() {
        kantorSar = new MstKantorSar();
        kantorsarname = new String();
        initCoordinate();
        address = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        disabled1 = false;
        isReadOnly = true;
        showCombo = true;
        showDetail = true;
        kanSarImages = new MstKantorSarImages();
        listKanSarImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        uuid = UUID.randomUUID().toString();
    }

    public void chengeToCombo() {
        value = "";
        if (field.equals("kantorsarname") == true) {
            showCombo = false;
        } else {
            showCombo = true;
        }
    }

    private Specification<MstKantorSar> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<MstKantorSar>() {
            @Override
            public Predicate toPredicate(Root<MstKantorSar> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "kantorsarname":
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get("kantorsarname")),
                                            getLikePattern(value.trim())));
                            break;
                        //return orTogether(predicates, cb);

                        default:
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get(field)), getLikePattern(value.trim()))
                            );
                            break;
                    }
                }
                if (currentId.equals("BSN")) {
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                } else {
                    predicates.add(cb.equal(root.<String>get("kantorsarid"), currentId));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
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

    public List<SelectItem> getListKantorSar() {
        if (listKantorSar == null) {
            listKantorSar = new ArrayList<>();
            officeSarService.getOfficeSar().stream().forEach((kantorSar) -> {
                listKantorSar.add(new SelectItem(kantorSar.getKantorsarname(), kantorSar.getKantorsarname()));
            });
        }
        return listKantorSar;
    }

    public void uploadImage(FileUploadEvent e) {
        synchronized (this) {
            try {
                kanSarImages = new MstKantorSarImages();
                uploadFile = e.getFile();
                System.out.println("uploadFile: " + uploadFile);
                filePath = "TEMP_KANTORSAR" + File.separator + uuid + File.separator;
                System.out.println("filePath: " + filePath);
                file = new File(uploadFolder + filePath);
                System.out.println("file: " + file);
                if (!file.exists()) {
                    file.mkdirs();
                    System.out.println("file.exists: " + file.exists());
                    System.out.println("file.mkdir: " + file.mkdir());
                    System.out.println("file.mkdirs: " + file.mkdirs());
                }
                byte[] bytes = null;
                srcDir = null;

                if (uploadFile != null) {
                    for (int i = 0; i < listKanSarImages.size(); i++) {
                        System.out.println("listKanSarImages.size(): " + listKanSarImages.size());
                        temp = listKanSarImages.get(i).getPathname().split("\\\\");
                        source = filePath + temp[2];
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        System.out.println("uploadFolder + source: " + uploadFolder + source);
                        srcDir = new File(uploadFolder + source);
                        System.out.println("listKanSarImages.indexOf(i): " + listKanSarImages.indexOf(i));
                        if ((uploadFolder + source).equals(name)) {
                            System.out.println("a");
                            listKanSarImages.remove(i);
                        }
                    }

                    //to upload single photo
//                    for (MstKantorSarImages oldImages : listKanSarImages) {
//                        System.out.println("listKanSarImages: " + listKanSarImages);
//                        System.out.println("images: " + oldImages);
//                        temp = oldImages.getPathname().split("\\\\");
//                        source = filePath + temp[2];
//                        System.out.println("source: " + source);
//                        srcDir = new File(uploadFolder + source);
//                        System.out.println("srcDir: " + srcDir);
//                        
//                        if (isReadOnly == false) {
//                            if (filePath.contains("TEMP")) {
//                                System.out.println("masuk ke TEMP");
//                                oldImages.setDeleted(true);
//                                dataDeleteImagesTemp.add(oldImages);
//                                System.out.println("dataDeleteImagesTemp: " + dataDeleteImagesTemp);
//                            }
//                            System.out.println("masuk ke ELSE TEMP");
//                            oldImages.setDeleted(true);
//                            dataDeleteImages.add(oldImages);
//                            System.out.println("dataDeleteImages: " + dataDeleteImages);
//                        }                        
//                        srcDir.delete();
//                        listKanSarImages = new ArrayList<>();
//                    }
                    bytes = uploadFile.getContents();
                    System.out.println("bytes: " + bytes);
                    fileName = FilenameUtils.getName(uploadFile.getFileName());
                    System.out.println("fileName: " + fileName);
                    fileName = fileName.replaceAll("\\s+", "_");
                    System.out.println("fileName 2: " + fileName);
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(uploadFolder + filePath + fileName)));
                    System.out.println("stream: " + stream);
                    stream.write(bytes);
                    stream.close();

                    kanSarImages.setPathname(filePath + fileName);
                    listKanSarImages.add(kanSarImages);
                    System.out.println("kanSarImages.setPathName: " + kanSarImages.getPathname());
                    System.out.println("listKansarImages 2: " + listKanSarImages);
                }
                RequestContext.getCurrentInstance().update("kantorsar-content:form-kantorSARInput:imgKanSar");
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            } catch (Exception ex) {
                ex.printStackTrace();
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            }
        }
    }

    public void imageFunction() {
        System.out.println("Masuk kesini");
        String combine = kantorSar.getKantorsarid();
        System.out.println("combine: " + combine);
        combine = combine.replaceAll("\\s+", "_");
        System.out.println("combine 2: " + combine);
        filePathSource = "TEMP_KANTORSAR" + File.separator + uuid + File.separator;
        System.out.println("filePathSource Function: " + filePathSource);
        filePath = "KANTORSAR" + File.separator + combine + File.separator;
        System.out.println("filePath Function: " + filePath);
        srcDir = null;
        destDir = null;
        File destDirName = null;

        for (MstKantorSarImages images : listKanSarImages) {
            System.out.println("listKanSarImages Function: " + listKanSarImages);
            System.out.println("images Function: " + images);
            temp = images.getPathname().split("\\\\");
            System.out.println("temp Function: " + temp);
            images.setKanSar(kantorSar);
            images.setPathname(filePath + temp[2]);
            images.setDeleted(false);

            kanSarImagesRepo.save(images);
            System.out.println("images.set Function: " + images.getKanSar() + ", " + images.getPathname() + ", " + images.getDeleted());

            source = filePathSource + temp[2];
            System.out.println("source Function: " + source);
            srcDir = new File(uploadFolder + source);
            System.out.println("srcDir Function: " + srcDir);

            if (images.getPathname() != null) {
                tempDest = images.getPathname().split("\\\\");
                System.out.println("tempDest Function: " + tempDest);
                destination = tempDest[0] + File.separator + tempDest[1];
                System.out.println("destination Function: " + filePath);
                destDir = new File(uploadFolder + filePath);
                System.out.println("destDir Function: " + destDir);
                destDirName = new File(uploadFolder + filePath + tempDest[2]);
                System.out.println("destDirName Function: " + destDirName);

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                try {
                    for (int i = 0; i < listKanSarImages.size(); i++) {
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        System.out.println("name: " + name);
                        if ((uploadFolder + filePath + tempDest[2]).equals(name)) {
                            System.out.println("a");
                            //listKanSarImages.remove(i);
                            destDirName.delete();
                        }
                    }

                    System.out.println("Masuk kesini (FileUtils)");
                    FileUtils.moveFileToDirectory(srcDir, destDir, true);
//                    FileUtils.deleteDirectory(new File(uploadFolder + filePathSource));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        //update status isDeleted
        for (MstKantorSarImages kanSarImage : dataDeleteImages) {
            kanSarImage.setDeleted(true);
            kanSarImagesRepo.save(kanSarImage);
        }
    }

    public void simpan() {
        if (isReadOnly == true) {
            String iD = kantorSar.getKantorsarid().toUpperCase();
            MstKantorSar id = mstKantorSarRepo.findTopOneMstKantorSarByKantorsarid(iD);
            log.debug("id : " + id);
            if (id != null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN,
                        "Peringatan",
                        "ID KanSAR Sudah Ada ");
                return;
            } else {
                kantorSar.setKantorsarid(iD);
                kantorSar.setIsdeleted(BigInteger.ZERO);
                kantorSar.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                kantorSar.setDatecreated(new Date());
                kantorSar.setUsersiteid(ProsiaConstant.SITES);
                setCoordinate();
                mstKantorSarRepo.save(kantorSar);
                imageFunction();
                addPopUpMessage(FacesMessage.SEVERITY_INFO,
                        "Sukses", "Kantor SAR berhasil disimpan!");
                //addMessage("Sukses", "Kantor SAR berhasil disimpan");                
                refresh();
                showDetail = true;
//                RequestContext.getCurrentInstance().execute("PF('widgetKantorSAR').hide()");
//                RequestContext.getCurrentInstance().execute("PF('widgetKantorSARInput').hide()");
            }
        } else {
            kantorSar.setIsdeleted(BigInteger.ZERO);
            kantorSar.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            kantorSar.setDatecreated(new Date());
            kantorSar.setUsersiteid(ProsiaConstant.SITES);
            setCoordinate();
            mstKantorSarRepo.save(kantorSar);
            imageFunction();
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Kantor SAR berhasil disimpan!");
            refresh();
            showDetail = true;
//            listImagePersonil.stream().forEach(x -> x.setPersonnelID(resPersonnel));
//            listPotensiImagePersonil.removeIf(x -> x.getPersonnelID().getPersonnelID().equals(resPersonnel.getPersonnelID()));
//            listPotensiImagePersonil.addAll(listImagePersonil);
//            RequestContext.getCurrentInstance().execute("PF('widgetKantorSAR').hide()");
//            RequestContext.getCurrentInstance().execute("PF('widgetKantorSARInput').hide()");
        }
    }

    public void viewKantorSAR(String kantorsarid, Boolean edited) {
        isReadOnly = false;
        showDetail = false;
        this.kantorSar = mstKantorSarRepo.findOne(kantorsarid);
        kantorsarname = kantorSar.getKantorsarname();
        address = kantorSar.getAddress();
        phonenumber1 = kantorSar.getPhonenumber1();
        phonenumber2 = kantorSar.getPhonenumber2();
        phonenumber3 = kantorSar.getPhonenumber3();
        faxnumber = kantorSar.getFaxnumber();
        radiofrequency = kantorSar.getRadiofrequency();
        listKanSarImages = kanSarImagesRepo.findByKanSarAndDeleted(kantorSar, false);
        initCoordinate();
        viewCoordinate();
        logger.debug("KANSAR ID : {}", kantorSar.getKantorsarid());
        disabled = edited;
        if (edited == true) {
            disabled1 = edited;
            headerDetail = "Lihat Kantor SAR";
        } else {
            disabled1 = !edited;
            headerDetail = "Ubah Kantor SAR";
        }
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);

        RequestContext.getCurrentInstance().reset("kantorsar-content:form-kantorSARInput");
        RequestContext.getCurrentInstance().update("kantorsar-content:form-kantorSARInput");
//        RequestContext.getCurrentInstance().execute("PF('widgetKantorSAR').show()");
    }

    public void batal() {
        refresh();
        showDetail = true;
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        kantorSar = new MstKantorSar();
        kantorsarid = new String();
        kantorsarname = new String();
        address = new String();
        latitude = new String();
        longitude = new String();
        phonenumber1 = new String();
        phonenumber2 = new String();
        phonenumber3 = new String();
        faxnumber = new String();
        radiofrequency = new String();
        kanSarImages = new MstKantorSarImages();
        listKanSarImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
    }

    public void hapus(MstKantorSar i) {
        logger.debug("KANSAR : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstKantorSarRepo.save(i);

        /*
        If the main MstPosSar is deleted, automatically posSarImages is setDeleted(true)
        This code just for MULTIPLE PHOTO
         */
//        listKanSarImages = kanSarImagesRepo.findByKanSarAndDeleted(i, false);
//        for (MstKantorSarImages kanSarImage : listKanSarImages) {
//            kanSarImage.setDeleted(true);
//            kanSarImagesRepo.save(kanSarImage);
//        }
        /*
        If the main MstPosSar is deleted, automatically posSarImages is setDeleted(true)
        This code just for SINGLE PHOTO
         */
//        kanSarImages = kanSarImagesRepo.findAllByKanSarAndDeleted(i, false);
//        kanSarImages.setDeleted(true);
//        kanSarImagesRepo.save(kanSarImages);
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Sukses", "Kantor SAR berhasil dihapus");
    }

    public void hapusImage(MstKantorSarImages images) {
        listKanSarImages.remove(images);
//        String gambar[] = images.getPathname().split("\\\\");
//        String namaGambar = gambar[2];
//        srcDir = new File(images.getPathname() + namaGambar);
//        srcDir.delete();
        dataDeleteImages.add(images);
//        System.out.println("namaGambar: " + namaGambar);
//        if (namaGambar.equals(images)) {
//            System.out.println("Benar" + images);
//        } else {
//            System.out.println("Salah" + images);
//        }
        addPopUpMessage("Informasi", "Image berhasil dihapus!");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public void add() {
        isReadOnly = true;
        disabled = false;
        disabled1 = false;
        showButton = true;
        showDetail = false;
        headerDetail = "Tambah Kantor SAR";
        initCoordinate();
        refresh();
//        RequestContext.getCurrentInstance().execute("PF('widgetKantorSARInput').show()");
        RequestContext.getCurrentInstance().reset("kantorsar-content:form-kantorSARInput");
        RequestContext.getCurrentInstance().update("kantorsar-content:form-kantorSARInput");

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
        kantorSar.setLatitude(coordinateLatitude.getValidDecimalDegrees() != null
                ? coordinateLatitude.getValidDecimalDegrees().toString() : null);
        kantorSar.setLongitude(coordinateLongitude.getValidDecimalDegrees() != null
                ? coordinateLongitude.getValidDecimalDegrees().toString() : null);
    }

    public void viewCoordinate() {
        if (kantorSar.getLatitude() != null
                || StringUtils.isNotBlank(kantorSar.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(kantorSar.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (kantorSar.getLongitude() != null
                || StringUtils.isNotBlank(kantorSar.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(kantorSar.getLongitude());
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
}
