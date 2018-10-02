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
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.MstPosSarImages;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstPosSarImagesRepo;
import prosia.basarnas.repo.MstPosSarRepo;
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
class MstPosSarMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private MstPosSar mstPosSar;
    private LazyDataModelJPA<MstPosSar> lazyDataModelJPA;
    private MstPosSarImages posSarImages;
    private UploadedFile uploadFile;
    private File file;
    private File srcDir;
    private File destDir;

    @Autowired
    private MstPosSarRepo mstPosSarRepo;

    @Autowired
    private MstPosSarImagesRepo posSarImagesRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    private List<SelectItem> listKantorSar;
    private List<MstPosSarImages> listPosSarImages;
    private List<MstPosSarImages> dataDeleteImages;
    private List<MstPosSarImages> dataDeleteImagesTemp;

    @Value("${uploadFolder}")
    private String uploadFolder;
    private String field;
    private String value;
    private String possarid;
    private String possarname;
    private String address;
    private String longitude;
    private String latitude;
    private String kantorsarid;
    private String kantorsarname;
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
    private Boolean showButtonInput;
    private Boolean showButton;
    private Boolean disabled;
    private Boolean disabled1;
    private Boolean showCombo;
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Boolean isReadOnly;
    private Boolean showDetail;

    @Autowired
    private OfficeSarService officeSarService;

    @Autowired
    private MstKantorSarRepo kantorSarRepo;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstPosSarRepo) {
            @Override
            protected long getDataSize() {
                return mstPosSarRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstPosSarRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public MstPosSarMBean() {
        initCoordinate();
        mstPosSar = new MstPosSar();
        possarname = new String();
        address = new String();
        showButton = true;
        showButtonInput = true;
        disabled = false;
        disabled1 = false;
        showCombo = true;
        isReadOnly = true;
        showDetail = true;
        posSarImages = new MstPosSarImages();
        listPosSarImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        uuid = UUID.randomUUID().toString();
    }

    public void chengeToCombo() {
        value = "";
        if (field.equals("kantorsarid") == true) {
            showCombo = false;
        } else {
            showCombo = true;
        }
    }

    private Specification<MstPosSar> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<MstPosSar>() {

            @Override
            public Predicate toPredicate(Root<MstPosSar> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "kantorsarid":
                            System.out.println("value : " + getLikePattern(value));
                            predicates.add(
                                    cb.like(cb.lower(root.<MstKantorSar>get("kantorsarid")
                                            .<String>get("kantorsarname")),
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
                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), currentId));
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

    public void uploadImage(FileUploadEvent e) {
        synchronized (this) {
            try {
                posSarImages = new MstPosSarImages();
                uploadFile = e.getFile();
                System.out.println("uploadFile: " + uploadFile);
                filePath = "TEMP_POSSAR" + File.separator + uuid + File.separator;
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
                    for (int i = 0; i < listPosSarImages.size(); i++) {
                        System.out.println("listPosSarImages.size(): " + listPosSarImages.size());
                        temp = listPosSarImages.get(i).getPathname().split("\\\\");
                        source = filePath + temp[2];
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        System.out.println("uploadFolder + source: " + uploadFolder + source);
                        srcDir = new File(uploadFolder + source);
                        System.out.println("listPosSarImages.indexOf(i): " + listPosSarImages.indexOf(i));
                        if ((uploadFolder + source).equals(name)) {
                            System.out.println("a");
                            listPosSarImages.remove(i);
                        }
                    }

                    //to upload single photo
//                    for (MstPosSarImages oldImages : listPosSarImages) {
//                        System.out.println("listKanSarImages: " + listPosSarImages);
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
//                        listPosSarImages = new ArrayList<>();
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

                    posSarImages.setPathname(filePath + fileName);
                    listPosSarImages.add(posSarImages);
                    System.out.println("kanSarImages.setPathName: " + posSarImages.getPathname());
                    System.out.println("listKansarImages 2: " + listPosSarImages);
                }
                RequestContext.getCurrentInstance().update("possar-content:form-posSAR:imgPosSar");
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            } catch (Exception ex) {
                ex.printStackTrace();
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            }
        }
    }

    public void imageFunction() {
        System.out.println("Masuk kesini");
        String combine = mstPosSar.getPossarid();
        System.out.println("combine: " + combine);
        combine = combine.replaceAll("\\s+", "_");
        System.out.println("combine 2: " + combine);
        filePathSource = "TEMP_POSSAR" + File.separator + uuid + File.separator;
        System.out.println("filePathSource Function: " + filePathSource);
        filePath = "POSSAR" + File.separator + combine + File.separator;
        System.out.println("filePath Function: " + filePath);
        srcDir = null;
        destDir = null;
        File destDirName = null;

        for (MstPosSarImages images : listPosSarImages) {
            System.out.println("listKanSarImages Function: " + listPosSarImages);
            System.out.println("images Function: " + images);
            temp = images.getPathname().split("\\\\");
            System.out.println("temp Function: " + temp);
            images.setPosSar(mstPosSar);
            images.setPathname(filePath + temp[2]);
            images.setDeleted(false);

            posSarImagesRepo.save(images);
            System.out.println("images.set Function: " + images.getPosSar() + ", " + images.getPathname() + ", " + images.getDeleted());

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
                    for (int i = 0; i < listPosSarImages.size(); i++) {
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        System.out.println("name: " + name);
                        if ((uploadFolder + filePath + tempDest[2]).equals(name)) {
                            System.out.println("a");
                            //listPosSarImages.remove(i);
                            destDirName.delete();
                        }
                    }

                    FileUtils.moveFileToDirectory(srcDir, destDir, true);
//                    FileUtils.deleteDirectory(new File(uploadFolder + filePathSource));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        //update status isDeleted
        for (MstPosSarImages posSarImage : dataDeleteImages) {
            posSarImage.setDeleted(true);
            posSarImagesRepo.save(posSarImage);
        }
    }

    public void simpan() {
        if (isReadOnly == true) {
            MstPosSar id = mstPosSarRepo.findTopOneMstPosSarByPossarid(mstPosSar.getPossarid());
            if (id != null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN,
                        "Peringatan",
                        "ID Pos SAR Sudah Ada ");
                return;
            } else {
                mstPosSar.setIsdeleted(BigInteger.ZERO);
                mstPosSar.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                mstPosSar.setDatecreated(new Date());
                mstPosSar.setUsersiteid(ProsiaConstant.SITES);
                System.out.println("kantor sar name= " + kantorsarname);
                MstKantorSar sar = kantorSarRepo.findByKantorsarname(kantorsarname);
                //System.out.println("kantor sar id= "+ sar);
                if (mstPosSar.getKantorsarid() == null) {
                    mstPosSar.setKantorsarid(sar);
                }
                setCoordinate();
                mstPosSarRepo.save(mstPosSar);
                imageFunction();
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Pos SAR berhasil disimpan!");
                refresh();
                showDetail = true;
//                RequestContext.getCurrentInstance().execute("PF('widgetPosSAR').hide()");
//                RequestContext.getCurrentInstance().execute("PF('widgetPosSARInput').hide()");
            }
        } else {
            mstPosSar.setIsdeleted(BigInteger.ZERO);
            mstPosSar.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            mstPosSar.setDatecreated(new Date());
            mstPosSar.setUsersiteid(ProsiaConstant.SITES);
            System.out.println("kantor sar name= " + kantorsarname);
            MstKantorSar sar = kantorSarRepo.findByKantorsarname(kantorsarname);
            //System.out.println("kantor sar id= "+ sar);
            if (mstPosSar.getKantorsarid() == null) {
                mstPosSar.setKantorsarid(sar);
            }
            setCoordinate();
            mstPosSarRepo.save(mstPosSar);
            imageFunction();
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Pos SAR berhasil disimpan!");
            refresh();
            showDetail = true;
//            RequestContext.getCurrentInstance().execute("PF('widgetPosSAR').hide()");
//            RequestContext.getCurrentInstance().execute("PF('widgetPosSARInput').hide()");
        }
    }

    public void hapusImage(MstPosSarImages images) {
        listPosSarImages.remove(images);
        dataDeleteImages.add(images);
//        String gambar[] = images.getPathname().split("\\\\");
//        String namaGambar = gambar[2];
//        System.out.println("namaGambar: " + namaGambar);
//        if (namaGambar.equals(images)) {
//            System.out.println("Benar" + images);
//        } else {
//            System.out.println("Salah" + images);
//        }
        addPopUpMessage("Informasi", "Image berhasil dihapus!");
    }

    public void add() {
        isReadOnly = true;
        disabled = false;
        disabled1 = false;
        showButton = true;
        showDetail = false;
        headerDetail = "Tambah Pos SAR";
        initCoordinate();
        refresh();
        RequestContext.getCurrentInstance().reset("possar-content:form-posSAR");
        RequestContext.getCurrentInstance().update("possar-content:form-posSAR");
//        RequestContext.getCurrentInstance().execute("PF('widgetPosSARInput').show()");
    }

    public void viewPosSAR(String possarid, Boolean edited) {
        isReadOnly = false;
        showDetail = false;
        this.mstPosSar = mstPosSarRepo.findOne(possarid);
        possarname = mstPosSar.getPossarname();
        address = mstPosSar.getAddress();
        initCoordinate();
        viewCoordinate();
        phonenumber1 = mstPosSar.getPhonenumber1();
        phonenumber2 = mstPosSar.getPhonenumber2();
        phonenumber3 = mstPosSar.getPhonenumber3();
        faxnumber = mstPosSar.getFaxnumber();
        radiofrequency = mstPosSar.getRadiofrequency();
        kantorsarname = mstPosSar.getKantorsarid().getKantorsarname();
        listPosSarImages = posSarImagesRepo.findByPosSarAndDeleted(mstPosSar, false);

        logger.debug("POSSAR ID : {}", mstPosSar.getPossarid());
        disabled = edited;
        if (edited == true) {
            disabled1 = edited;
            headerDetail = "Lihat Pos SAR";
        } else {
            disabled1 = !edited;
            headerDetail = "Ubah Pos SAR";
        }
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
        RequestContext.getCurrentInstance().reset("possar-content:form-posSAR");
        RequestContext.getCurrentInstance().update("possar-content:form-posSAR");
//        RequestContext.getCurrentInstance().execute("PF('widgetPosSAR').show()");
    }

    public void batal() {
        refresh();
        showDetail = true;
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        mstPosSar = new MstPosSar();
        kantorsarname = new String();
        posSarImages = new MstPosSarImages();
        listPosSarImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
    }

    public void hapus(MstPosSar i) {
        logger.debug("POSSAR : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstPosSarRepo.save(i);

        /*
        If the main MstPosSar is deleted, automatically posSarImages is setDeleted(true)
        This code just for MULTIPLE PHOTO
         */
//        listPosSarImages = posSarImagesRepo.findByPosSarAndDeleted(i, false);
//        for (MstPosSarImages posSarImage : listPosSarImages) {
//            posSarImage.setDeleted(true);
//            posSarImagesRepo.save(posSarImage);
//        }
        /*
        If the main MstPosSar is deleted, automatically posSarImages is setDeleted(true)
        This code just for SINGLE PHOTO
         */
//        posSarImages = posSarImagesRepo.findAllByPosSarAndDeleted(i, false);
//        posSarImages.setDeleted(true);
//        posSarImagesRepo.save(posSarImages);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Pos SAR berhasil dihapus");
    }

    public String getKantorSarByKantorSarID(String kantorsarid) {
        return kantorSarRepo.findOne(kantorsarid).getKantorsarname();
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

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    /*public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }*/
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
        mstPosSar.setLatitude(coordinateLatitude.getValidDecimalDegrees() != null
                ? coordinateLatitude.getValidDecimalDegrees().toString() : null);
        mstPosSar.setLongitude(coordinateLongitude.getValidDecimalDegrees() != null
                ? coordinateLongitude.getValidDecimalDegrees().toString() : null);
    }

    public void viewCoordinate() {
        if (mstPosSar.getLatitude() != null
                || StringUtils.isNotBlank(mstPosSar.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(mstPosSar.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (mstPosSar.getLongitude() != null
                || StringUtils.isNotBlank(mstPosSar.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(mstPosSar.getLongitude());
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
