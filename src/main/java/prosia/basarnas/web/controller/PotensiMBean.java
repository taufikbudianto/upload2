/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.CrmAreaCode;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.MstRegion;
import prosia.basarnas.model.MstSkill;
import prosia.basarnas.model.PotencyImages;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelImages;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.repo.CrmAreaRepo;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.repo.MstEmployeeUnitRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstPosSarRepo;
import prosia.basarnas.repo.MstRegionRepo;
import prosia.basarnas.repo.MstSkillRepo;
import prosia.basarnas.repo.PersonelHistoryRepo;
import prosia.basarnas.repo.PersonelTrainingRepo;
import prosia.basarnas.repo.PotencyImagesRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPersonnelImagesRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.service.MstAssetTypeService;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.service.PersonnelService;
import prosia.basarnas.service.PotensiService;
import prosia.basarnas.web.util.Coordinate;

/**
 *
 * @author Satria Putera Utama
 */
@Component
@Scope("view")

public @Data
class PotensiMBean extends AbstractManagedBean implements InitializingBean {

    @Value("${uploadFolder}")
    private String uploadFolder;
    private String pathFile;
    private String value;
    private String dropdown;
    private String lintang;
    private String uuid;
    private String currentId;
    private Map<String, String> listDropDown;
    private Map<String, String> listKolomPencarian;
    private UploadedFile file;
    private UploadedFile filePersonil;
    private PotencyImages potencyImages;
    private ResPersonnelImages resPersonnelImages;
    private String headerDetail;
    private String kolompencarian;
    private boolean isReadOnly;
    private boolean isPotencyDisabled;
    private boolean isDisableButton;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private MstPosSarRepo posSarRepo;

    @Autowired
    private ResPotencyRepo resPotencyRepo;

    @Autowired
    private MstAssetTypeRepo mstAssetTypeRepo;

    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;

    @Autowired
    private OfficeSarService officeSarService;

    @Autowired
    private MstAssetTypeService mstAssetTypeService;

    @Autowired
    private MstRegionRepo propinsiRepo;

    @Autowired
    private CrmAreaRepo kotaRepo;

    @Autowired
    private ResAssetRepo resAssetRepo;

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private MstEmployeeClassRepo mstEmployeeClassRepo;

    @Autowired
    private MstEmployeePositionRepo mstEmployeePositionRepo;

    @Autowired
    private MstEmployeeUnitRepo unitRepo;

    @Autowired
    private MstSkillRepo skillRepo;

    @Autowired
    private ResPotencyContactRepo kontakRepo;

    @Autowired
    private PotensiService potensiService;

    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private ResPersonnelRepo personnelRepo;

    @Autowired
    private PersonelHistoryRepo personelHistoryRepo;

    @Autowired
    private PersonelTrainingRepo personelTrainingRepo;

    @Autowired
    private PotencyImagesRepo potencyImagesRepo;

    @Autowired
    private ResPersonnelImagesRepo resPersonnelImagesRepo;

    private LazyDataModelJPA<ResPotency> listResPotency;
    private List<MstKantorSar> kansar;
    private List<MstKantorSar> listKantorSar;
    private List<MstPosSar> listPostSar;
    private List<ResAsset> listResAsset;
    private List<MstAssetType> listMstAset;
    private List<MstRegion> listPropinsi;
    private List<CrmAreaCode> listKota;
    private List<MstEMployeeClass> listGolongan;
    private List<MstEmployeePosition> listJabatanStruktural;
    private List<MstEmployeePosition> listJabatanFungsional;
    private List<MstEmployeePosition> listJabatanSiaga;
    private List<MstEmployeeUnit> listUnitKerja;
    private List<MstSkill> listSkill;
    private List<ResPersonnelTraining> listPersonelTraining;
    private List<ResPersonnelTraining> listPotensiPersonelTraining;
    private List<ResPersonnelHistory> listPersonelHistory;
    private List<ResPersonnelHistory> listPotensiPersonelHistory;
    private List<ResPotencyContact> listkontak;
    private List<ResPersonnel> listPersonel;
    private List<PotencyImages> listImage;
    private List<PotencyImages> dataDeleteImage;
    private List<ResPersonnelImages> listImagePersonil;
    private List<ResPersonnelImages> listPotensiImagePersonil;
    private List<ResPersonnelImages> dataDeleteImagePersonil;

    private MstEmployeeUnit unitKerja;
    private MstRegion propinsi;
    private MstEMployeeClass golongan;
    private MstEmployeePosition jabatan;
    private MstPosSar posSar;
    private MstKantorSar mstKantorSar;
    private Coordinate latitudePotensi;
    private Coordinate longitudePotensi;
    private Coordinate latitudeAsset;
    private Coordinate longitudeAsset;
    private CrmAreaCode kota;
    private ResPotency resPotency;
    private ResPotencyContact resPotencyContact;
    private ResPersonnel resPersonnel;
    private ResAsset resAsset;
    private ResPersonnelHistory personnelHistory;
    private ResPersonnelTraining personnelTraining;
    private MstSkill mstSkill;
    private MstAssetType mstAssetType;

    private int idtemp;
    private int idtempPersonelKontak;
    private Integer activeIndex;
    private Integer idtempPersonel;
    private Boolean isText;
    private Boolean isDropDown;
    private Boolean isShowListPotensi;
    private Boolean isShowEditPotensi;
    private Boolean isShowEditPersonil;
    private Boolean showButton;
    private Boolean disabled;
    private Date date;

    @Override
    public void init() {
        unitKerja = new MstEmployeeUnit();
        propinsi = new MstRegion();
        mstAssetType = new MstAssetType();
        golongan = new MstEMployeeClass();
        jabatan = new MstEmployeePosition();
        posSar = new MstPosSar();
        mstKantorSar = new MstKantorSar();
        initCoordinatePotensi();
        initCoordinateAsset();
        kota = new CrmAreaCode();
        resPotency = new ResPotency();
        resPotencyContact = new ResPotencyContact();
        resPersonnel = new ResPersonnel();
        resAsset = new ResAsset();
        listPotensiPersonelHistory = new ArrayList<>();
        personnelHistory = new ResPersonnelHistory();
        listPotensiPersonelTraining = new ArrayList<>();
        personnelTraining = new ResPersonnelTraining();
        mstSkill = new MstSkill();
        idtempPersonel = 1;
        listKolomPencarian = new LinkedHashMap<String, String>();
        listKolomPencarian.put("potencyname", "Nama Potensi");
        listKolomPencarian.put("address", "Alamat");
        listKolomPencarian.put("kantorsarid", "Nama KanSAR");
        //listKolomPencarian.put("potencytype", "Matra");
        listKolomPencarian.put("phonenumber1", "No. Telepon");
        listKolomPencarian.put("faxnumber", "No. Fax");
        //listKolomPencarian.put("radiofrequency", "Freq. Radio");
        listDropDown = new LinkedHashMap<String, String>();
        isText = true;
        isDropDown = false;
        activeIndex = 0;
        isShowListPotensi = true;
        isShowEditPotensi = false;
        isShowEditPersonil = false;
        listImage = new ArrayList<>();
        dataDeleteImage = new ArrayList<>();
        listPotensiImagePersonil = new ArrayList<>();
        dataDeleteImagePersonil = new ArrayList<>();
        uuid = UUID.randomUUID().toString();
    }

    public void showAllListPotency() {
        listPropinsi = propinsiRepo.findAll();
        listKantorSar = mstKantorSarRepo.findAll();
        this.listGolongan = mstEmployeeClassRepo.findAll();
        this.listUnitKerja = unitRepo.findAll();
        this.listJabatanFungsional = mstEmployeePositionRepo.findAllPositionFungsional();
        this.listJabatanStruktural = mstEmployeePositionRepo.findAllPositionStruktural();
        this.listJabatanSiaga = mstEmployeePositionRepo.findAllPositionSiaga();
        this.listSkill = skillRepo.findSkill();
        this.listMstAset = mstAssetTypeRepo.findAll();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        init();
        showButton = true;
        listResPotency = new LazyDataModelJPA(resPotencyRepo) {
            @Override
            protected long getDataSize() {
                if (StringUtils.isNotBlank(dropdown)) {
                    value = dropdown;
                }
                return resPotencyRepo.count(whereQuery(kolompencarian, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                if (StringUtils.isNotBlank(dropdown)) {
                    value = dropdown;
                }
                return resPotencyRepo.findAll(whereQuery(kolompencarian, value), request);
            }
        };
    }

    private Specification<ResPotency> whereQuery(
            final String field,
            final String value) {
        System.out.println("whereQuery : " + value);
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<ResPotency>() {
            @Override
            public Predicate toPredicate(Root<ResPotency> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "kantorsarid":
                            predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get(field), value));
                            break;
                        case "potencytype":
                            predicates.add(cb.equal(root.<BigInteger>get(field), BigInteger.valueOf(Integer.parseInt(value))));
                            break;
                        default:
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get(field)), getLikePattern(value.trim()))
                            );
                            break;
                    }
                }
                if (currentId.equals("BSN")) {
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                } else {
                    predicates.add(cb.equal(root.<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), currentId));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.valueOf(1)));
                }
                return andTogether(predicates, cb);
            }

        };
    }

    public void onKolomPencarianChange() {
        dropdown = "";
        value = "";
        if (kolompencarian != null && (kolompencarian.equals("potencyname") || kolompencarian.equals("address") || kolompencarian.equals("phonenumber1")
                || kolompencarian.equals("faxnumber") || kolompencarian.equals("radiofrequency"))) {
            isText = true;
            isDropDown = false;
        } else {
            listDropDown = new LinkedHashMap<String, String>();
            isText = false;
            isDropDown = true;
            if (kolompencarian != null && (kolompencarian.equals("potencytype"))) {
                listDropDown.put("Darat", "0");
                listDropDown.put("Udara", "1");
                listDropDown.put("Laut", "2");
            } else {
                kansar = new ArrayList<>();
                kansar = mstKantorSarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.valueOf(0));
                for (MstKantorSar mks : kansar) {
                    listDropDown.put(mks.getKantorsarname(), mks.getKantorsarid());
                }
            }
        }
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public void onRowSelect(SelectEvent event) {
        RequestContext.getCurrentInstance().execute("PF('widgetAsetResPotensi').show()");
    }

    public void onRowSelectKontak(SelectEvent event) {
        RequestContext.getCurrentInstance().execute("PF('dlgkontak').show()");
    }

    public void onRowSelectPersonel(SelectEvent event) {
        RequestContext.getCurrentInstance().execute("PF('dlgPersonil').show()");
    }

    public void uploadImgPersonil(FileUploadEvent e) {
        synchronized (this) {
            try {
                resPersonnelImages = new ResPersonnelImages();
                filePersonil = e.getFile();
                String filePath = "TEMP_POTENCY_PERSONNEL" + File.separator + uuid + File.separator;
                File file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;
                File srcDir = null;

                if (filePersonil != null) {
                    for (ResPersonnelImages images : listImagePersonil) {
                        String temp[] = images.getPathname().split("\\\\");
                        String source = filePath + temp[2];
                        srcDir = new File(uploadFolder + source);
                        if (isReadOnly == true) {
                            images.setDeleted(true);
                            dataDeleteImagePersonil.add(images);
                        }
                        srcDir.delete();
                        listImagePersonil = new ArrayList<>();
                    }
                    bytes = filePersonil.getContents();
                    String fileName = FilenameUtils.getName(filePersonil.getFileName());
                    fileName = fileName.replaceAll("\\s+", "_");
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(uploadFolder + filePath + fileName)));
                    stream.write(bytes);
                    stream.close();

                    resPersonnelImages.setPathname(filePath + fileName);
                    listImagePersonil.add(resPersonnelImages);
                }
                RequestContext.getCurrentInstance().update("respotensi-content:profil-tabs:imgResPers");
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + filePersonil.getFileName() + " with size " + filePersonil.getSize() + ") Uploaded Successfully", ""));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + filePersonil.getFileName() + " with size " + filePersonil.getSize() + ") Uploaded Successfully", ""));
            }
        }
    }

    public void upload(FileUploadEvent e) {
        synchronized (this) {
            try {
                potencyImages = new PotencyImages();
                System.out.println("---RunPotencyImages---" + e.getFile());
                file = e.getFile();
                String filePath = "TEMP_POTENCY_PROFIL" + File.separator + uuid + File.separator;
                File file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;
                File srcDir = null;

                if (this.file != null) {

                    for (PotencyImages images : listImage) {
                        String temp[] = images.getPathname().split("\\\\");
                        String source = filePath + temp[2];
                        srcDir = new File(uploadFolder + source);
                        if (isReadOnly == true) {
                            images.setDeleted(true);
                            dataDeleteImage.add(images);
                        }
                        srcDir.delete();
                        listImage = new ArrayList<>();
                    }
                    bytes = this.file.getContents();
                    String filename = FilenameUtils.getName(this.file.getFileName());
                    filename = filename.replaceAll("\\s+", "_");
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(uploadFolder + filePath + filename)));
                    stream.write(bytes);
                    stream.close();

                    potencyImages.setPathname(filePath + filename);
                    listImage.add(potencyImages);
                }
                RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:img");
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + this.file.getFileName() + " with size " + this.file.getSize() + ") Uploaded Successfully", ""));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Your Document (File Name " + file.getFileName() + " with size " + file.getSize() + ") Uploaded Successfully", ""));
            }
        }
    }

    public void hapusImage(PotencyImages images) {
        listImage.remove(images);
        dataDeleteImage.add(images);
        addMessage("Sukses", "Image berhasil dihapus!");

//        potencyImages = (PotencyImages) getRequestParam("filename");
//        listImage.remove(potencyImages);
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Image berhasil dihapus!");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:img");
    }

    public void hapusImagePersonil(ResPersonnelImages images) {
        listImagePersonil.remove(images);
        dataDeleteImagePersonil.add(images);
        addMessage("Sukses", "Image Personil berhasil dihapus!");
    }

    public void showImage(String images) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(uploadFolder + images);
    }

//    public void upload() {
//        //save image to server
//        byte[] bytes = null;
//        System.out.println("Upload " + uploadFolder);
//        try {
//            if (file != null) {
//                bytes = file.getContents();
//                File folder = new File(uploadFolder + File.separator + UUID.randomUUID().toString());
//                if (!folder.exists()) {
//                    folder.mkdirs();
//                }
//                BufferedOutputStream stream
//                        = new BufferedOutputStream(
//                                new FileOutputStream(
//                                        new File(folder + File.separator + file.getFileName())));
//                stream.write(bytes);
//                stream.close();
//                pathFile = folder.getAbsolutePath();
//                System.out.println(pathFile);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public void imageFunction() {
        String combine = resPotency.getPotencyid();
        combine = combine.replaceAll("\\s+", "_");
        String filePath = "POTENCY_PROFIL" + File.separator + combine + File.separator;
        String filePathSource = "TEMP_POTENCY_PROFIL" + File.separator + uuid + File.separator;
        File destDir = null;
        File srcDir = null;
        for (PotencyImages potencyImages : listImage) {
            String temp[] = potencyImages.getPathname().split("\\\\");
            potencyImages.setPotencyid(resPotency);
            potencyImages.setPathname(filePath + temp[2]);
            potencyImages.setDeleted(false);
            potencyImagesRepo.save(potencyImages);

            String source = filePathSource + temp[2];
            srcDir = new File(uploadFolder + source);

            if (potencyImages.getPathname() != null) {
                String tempDest[] = potencyImages.getPathname().split("\\\\");
                String destination = tempDest[0] + File.separator + tempDest[1];
                destDir = new File(uploadFolder + filePath);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                try {
                    FileUtils.moveFileToDirectory(srcDir, destDir, true);
                    FileUtils.deleteDirectory(new File(uploadFolder + filePathSource));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //update status isDeleted
        for (PotencyImages potencyImages : dataDeleteImage) {
            potencyImages.setDeleted(true);
            potencyImagesRepo.save(potencyImages);
        }
    }

    public void simpan() {
        if (isReadOnly == true) {
            resPotency.setCreatedby(String.valueOf(menuNavMBean.getUserSession().getUserId()));
            potensiService.savePotency(resPotency, listResAsset, listPersonel,
                    listPotensiPersonelHistory, listPotensiPersonelTraining, listkontak);
            imageFunction();
            imagePersonnelFunction();
            RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");
        } else {
            setCoordinatePotensi();
            resPotency.setPotencylevel(BigInteger.ZERO);
            potensiService.savePotency(resPotency, listResAsset, listPersonel,
                    listPotensiPersonelHistory, listPotensiPersonelTraining, listkontak);
            imageFunction();
            imagePersonnelFunction();
            RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");
        }

    }

    public void onChangePosSar() {
        this.listPostSar = posSarRepo.findAllMstPosSarBykantorsarid(resPersonnel.getOfficeSar());
    }

    public void onChangeCity() {
        this.listKota = kotaRepo.findByregion(resPotency.getRegionid());
    }

    public void loadUnsur() {
        resAsset = (ResAsset) getRequestParam("listRow");
        if (resAsset == null) {
            resAsset = new ResAsset();
        }
        initCoordinateAsset();
        viewCoordinateAsset();
//        listMstAset = mstAssetTypeRepo.findAll();
        RequestContext.getCurrentInstance().reset("idAsetResPotensi");
        RequestContext.getCurrentInstance().update("idAsetResPotensi");
        RequestContext.getCurrentInstance().execute("PF('widgetAsetResPotensi').show()");
    }

    public void loadPersonnel() {
        activeIndex = 2;
        resPersonnel = (ResPersonnel) getRequestParam("listRow");
        isShowEditPotensi = false;
        isShowEditPersonil = true;
        this.isDisableButton = true;
        listPersonelHistory = new ArrayList<>();
        listPersonelTraining = new ArrayList<>();
        listImagePersonil = new ArrayList<>();
//        listPotensiImagePersonil = new ArrayList<>();
        if (resPersonnel == null) {
            headerDetail = "Tambah Personil";
            resPersonnel = new ResPersonnel();
            personnelHistory = new ResPersonnelHistory();
            personnelTraining = new ResPersonnelTraining();
//            resPersonnelImages = new ResPersonnelImages();
//            listImagePersonil = listPotensiImagePersonil.stream()
//                    .filter(x -> x.getPersonnelID().getPersonnelID().equals(resPersonnel.getPersonnelID()))
//                    .collect(Collectors.toList());
//            System.out.println("List Image Personil di loadPersonnel yang if = null: "+ listImagePersonil.size());
            isPotencyDisabled = true;
        } else {
            isPotencyDisabled = true;
            headerDetail = "Ubah Personil";
            listPersonelHistory = listPotensiPersonelHistory.stream()
                    .filter(x -> x.getPersonnel().getPersonnelID().equals(resPersonnel.getPersonnelID()))
                    .collect(Collectors.toList());
            listPersonelTraining = listPotensiPersonelTraining.stream()
                    .filter(x -> x.getPersonnel().getPersonnelID().equals(resPersonnel.getPersonnelID()))
                    .collect(Collectors.toList());
            listImagePersonil = listPotensiImagePersonil.stream()
                    .filter(x -> x.getPersonnelID().getPersonnelID().equals(resPersonnel.getPersonnelID()))
                    .collect(Collectors.toList());
        }

    }

    public void loadkontak() {
        resPotencyContact = (ResPotencyContact) getRequestParam("listRow");
        if (resPotencyContact == null) {
            resPotencyContact = new ResPotencyContact();
        }
        RequestContext.getCurrentInstance().reset("personil-potensi-kontak");
        RequestContext.getCurrentInstance().update("personil-potensi-kontak");
        RequestContext.getCurrentInstance().execute("PF('dlgkontak').show()");
    }

    public void loadDetail() {
        resPotency = (ResPotency) getRequestParam("listRow");
        showAllListPotency();
        this.isDisableButton = false;
        isShowListPotensi = false;
        isShowEditPotensi = true;
        listPotensiPersonelHistory = new ArrayList<>();
        listPotensiPersonelTraining = new ArrayList<>();
        listImage = new ArrayList<>();
        listImagePersonil = new ArrayList<>();
        listPotensiImagePersonil = new ArrayList<>();
        initCoordinatePotensi();
        if (resPotency != null) {
            headerDetail = "Ubah Potensi";
            this.isReadOnly = true;
            isPotencyDisabled = true;
            disabled = true;
            viewCoordinatePotensi();
            listResAsset = resAssetRepo.findBypotencyidAndisdeleted(resPotency);
            listKota = kotaRepo.findByregion(resPotency.getRegionid());
            listPersonel = personnelRepo.findAllBypotencyAndIsdeleted(resPotency, BigInteger.ZERO);
            listImage = potencyImagesRepo.findByPotencyidAndDeleted(resPotency, false);
//            listImagePersonil = resPersonnelImagesRepo.findByPersonnelIDAndDeleted(resPersonnel, false);
//            System.out.println("Ini list image Personil: " + listImagePersonil);
//            listPotensiImagePersonil = resPersonnelImagesRepo.findByPersonnelIDAndDeleted(resPersonnel, false);
//            System.out.println("List Potensi Image Personil: "+listPotensiImagePersonil);
            for (ResPersonnel personnel : listPersonel) {
                listPotensiPersonelHistory.addAll(
                        personelHistoryRepo.findAllResPersonnelHistoryBypersonnel(personnel));
                listPotensiPersonelTraining.addAll(
                        personelTrainingRepo.findAllResPersonnelTrainingBypersonnel(personnel));
                listPotensiImagePersonil.addAll(
                        resPersonnelImagesRepo.findByPersonnelIDAndDeleted(personnel, false));
            }
            listkontak = kontakRepo.findAllBypotencyid(resPotency);
            if (listImage == null) {
                listImage = new ArrayList<>();
            }

            if (listImagePersonil == null) {
                listImagePersonil = new ArrayList<>();
            }
        } else {
            headerDetail = "Tambah Potensi";
            resPotency = new ResPotency();
            listPersonel = new ArrayList<>();
            listkontak = new ArrayList<>();
            listResAsset = new ArrayList<>();
            listPersonelHistory = new ArrayList<>();
            listPersonelTraining = new ArrayList<>();
            listImage = new ArrayList<>();
            listImagePersonil = new ArrayList<>();
        }
    }

    public void loadTraining() {
        personnelTraining = (ResPersonnelTraining) getRequestParam("listRow");
    }

    public void loadHistory() {
        personnelHistory = (ResPersonnelHistory) getRequestParam("listRow");
    }

    public void loadImagePersonil() {
        resPersonnelImages = (ResPersonnelImages) getRequestParam("listRow");
    }

    public void hpsPotensi() throws Exception {
        resPotency = (ResPotency) getRequestParam("listRow");
        resPotency.setIsdeleted(BigInteger.ONE);
        resPotencyRepo.save(resPotency);
        kolompencarian = null;
        value = null;
        afterPropertiesSet();
        addMessage("Sukses", "Potensi Berhasil Di Hapus");
    }

    public void hpsPersonil() {
        resPersonnel = (ResPersonnel) getRequestParam("listRow");
        resPersonnel.setIsdeleted(BigInteger.ONE);
        int index = listPersonel.indexOf(resPersonnel);
        if (index != -1) {
            listPersonel.remove(resPersonnel);
            addMessage("Sukses", "Personnel Resource Berhasil Di Hapus");
        }
//        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:profil-tabs:data-profil");
    }

    public void hpsUnsur() {
        resAsset = (ResAsset) getRequestParam("listRow");
        int index = listResAsset.indexOf(resAsset);
        if (index != -1) {
            listResAsset.remove(resAsset);
            addMessage("Sukses", "Aset Resource Berhasil Di Hapus");
        }
        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:data-unsur");
    }

    public void hpsHistory() {
        personnelHistory = (ResPersonnelHistory) getRequestParam("listRow");
        int index = listPersonelHistory.indexOf(personnelHistory);
        if (index != -1) {
            listPersonelHistory.remove(personnelHistory);
            addMessage("Sukses", "history Berhasil Di Hapus");
        }
        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:profil-tabs:data-riwayat");
    }

    public void hpsTraining() {
        personnelTraining = (ResPersonnelTraining) getRequestParam("listRow");
        int index = listPersonelTraining.indexOf(personnelTraining);
        if (index != -1) {
            listPersonelTraining.remove(index);
            addMessage("Sukses", "Training Berhasil Di Hapus");
        }
//        RequestContext.getCurrentInstance().update("profil-tabs:data-profil");
    }

    public void hpsKontak() {
        resPotencyContact = (ResPotencyContact) getRequestParam("listRow");
        int index = listkontak.indexOf(resPotencyContact);
        if (index != -1) {
            listkontak.remove(resPotencyContact);
            addMessage("Sukses", "Kontak Berhasil Di Hapus");
        }
        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:data-kontak");
    }

    public void tambahUnsur() {
        if (resAsset.getAssetid() == null) {
            resAsset.setAssetid("tmp");
            setCoordinateAsset();
            resAsset.setIsdeleted(BigInteger.ZERO);
            listResAsset.add(resAsset);

            addMessage("Sukses", "Aset Resource Berhasil Di Tambah");
        } else {
            setCoordinateAsset();
            int index = listResAsset.indexOf(resAsset);
            if (index != -1) {
                listResAsset.set(index, resAsset);
            }
            addMessage("Sukses", "Aset Resource Berhasil Di update");
        }
        resAsset = new ResAsset();
        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:data-unsur");
        RequestContext.getCurrentInstance().execute("PF('widgetAsetResPotensi').hide()");
    }

    public void tambahKontak() {
        if (resPotencyContact.getPotencycontactid() == null) {
            resPotencyContact.setPotencycontactid("tmp");
            listkontak.add(resPotencyContact);
            resPotencyContact = new ResPotencyContact();
            addMessage("Sukses", "Kontak Resource Berhasil Di Tambah");
        } else {
            int index = listkontak.indexOf(resPotencyContact);
            if (index != -1) {
                listkontak.set(index, resPotencyContact);
            }
            addMessage("Sukses", "Kontak Resource Berhasil Di update");
        }
        RequestContext.getCurrentInstance().update("respotensi-content:potensi-tabs:data-kontak");
        RequestContext.getCurrentInstance().execute("PF('dlgkontak').hide()");
    }

    public void backPersonel() {
        activeIndex = 2;
        isShowEditPersonil = false;
        isShowEditPotensi = true;
        this.isDisableButton = false;
        if (StringUtils.isBlank(resPotency.getPotencyid())) {
            headerDetail = "Tambah Potensi";
        } else {
            headerDetail = "Ubah Potensi";
        }
    }

    public void createId() {
        String id = personnelService.createidtempPersonel(0);
        String[] splitId = personnelService.createidtempPersonel(0).split("-");
        int sequence = Integer.parseInt(splitId[2]);
        int index = listPersonel.indexOf(resPersonnel);
        int count = sequence + index;
        int result = Integer.toString(count).length();
        String nextval = "";
        String inCaseSetId = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        listPersonel = personnelRepo.findAllBypersonnelIDLike("CGK");
        if (listPersonel.isEmpty()) {
            inCaseSetId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            switch (result) {
                case 1:
                    nextval = ProsiaConstant.SEQUENCE_000 + count;
                    break;
                case 2:
                    nextval = ProsiaConstant.SEQUENCE_00 + count;
                    break;
                case 3:
                    nextval = ProsiaConstant.SEQUENCE_0 + count;
                    break;
                case 4:
                    nextval = "" + count;
                    break;
                default:
                    break;
            }
            inCaseSetId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
        }

        if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
            inCaseSetId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        }
        resPersonnel.setPersonnelID(inCaseSetId);
    }

    public void imagePersonnelFunction() {
        createId();
        String combine = resPersonnel.getPersonnelID();
        combine = combine.replaceAll("\\s+", "_");
        String filePath = "POTENCY_PERSONNEL" + File.separator + combine + File.separator;
        String filePathSource = "TEMP_POTENCY_PERSONNEL" + File.separator + uuid + File.separator;
        File destDir = null;
        File srcDir = null;
        for (ResPersonnelImages personnelImages : listImagePersonil) {
            String temp[] = personnelImages.getPathname().split("\\\\");
//            personnelImages.setPersonnelID(resPersonnel);
            personnelImages.setPathname(filePath + temp[2]);
            personnelImages.setDeleted(false);
            resPersonnelImagesRepo.save(personnelImages);

            String source = filePathSource + temp[2];
            srcDir = new File(uploadFolder + source);

            if (personnelImages.getPathname() != null) {
                String tempDest[] = personnelImages.getPathname().split("\\\\");
                String destination = tempDest[0] + File.separator + tempDest[1];
                destDir = new File(uploadFolder + filePath);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                try {
                    FileUtils.moveFileToDirectory(srcDir, destDir, true);
                    FileUtils.deleteDirectory(new File(uploadFolder + filePathSource));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //update status isDeleted
        for (ResPersonnelImages personnelImages : dataDeleteImagePersonil) {
            personnelImages.setDeleted(true);
            resPersonnelImagesRepo.save(personnelImages);
        }
    }

    public void tambahPersonil() {

        if (resPersonnel.getPersonnelID() == null) {
            ResPersonnel personnel
                    = personnelRepo.findTopOneResPersonnelBypersonnelCode(
                            resPersonnel.getPersonnelCode());
            if (personnel != null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN,
                        "Peringatan",
                        "Nip Sudah Di Ada ");
                return;
            }

            resPersonnel.setPersonnelID(idtempPersonel.toString());
            resPersonnel.setIsdeleted(BigInteger.ZERO);
            listPersonel.add(resPersonnel);

            for (ResPersonnelHistory history : listPersonelHistory) {
                history.setPersonnel(resPersonnel);
                listPotensiPersonelHistory.add(history);
            }

            for (ResPersonnelTraining training : listPersonelTraining) {
                training.setPersonnel(resPersonnel);
                listPotensiPersonelTraining.add(training);
            }

            for (ResPersonnelImages images : listImagePersonil) {
                images.setPersonnelID(resPersonnel);
                listPotensiImagePersonil.add(images);
            }

            idtempPersonel += 1;
            addMessage("Sukses", "Personel Resource Berhasil Di Tambah");
        } else {
            int index = listPersonel.indexOf(resPersonnel);
            if (index != -1) {
                listPersonel.set(index, resPersonnel);
            }
            listPersonelHistory.stream().forEach(x -> x.setPersonnel(resPersonnel));
            listPotensiPersonelHistory.removeIf(x -> x.getPersonnel().getPersonnelID().equals(resPersonnel.getPersonnelID()));
            listPotensiPersonelHistory.addAll(listPersonelHistory);
            listPersonelTraining.stream().forEach(x -> x.setPersonnel(resPersonnel));
            listPotensiPersonelTraining.removeIf(x -> x.getPersonnel().getPersonnelID().equals(resPersonnel.getPersonnelID()));
            listPotensiPersonelTraining.addAll(listPersonelTraining);
            listImagePersonil.stream().forEach(x -> x.setPersonnelID(resPersonnel));
            listPotensiImagePersonil.removeIf(x -> x.getPersonnelID().getPersonnelID().equals(resPersonnel.getPersonnelID()));
            listPotensiImagePersonil.addAll(listImagePersonil);

            addMessage("Sukses", "Personel Berhasil Di update");
        }
        resPersonnel = new ResPersonnel();
        personnelHistory = new ResPersonnelHistory();
        personnelTraining = new ResPersonnelTraining();
        resPersonnelImages = new ResPersonnelImages();

        isShowEditPersonil = false;
        isShowEditPotensi = true;
        this.isDisableButton = false;
        if (StringUtils.isBlank(resPotency.getPotencyid())) {
            headerDetail = "Tambah Potensi";
        } else {
            headerDetail = "Ubah Potensi";
        }
    }

    public void tambahPersonilHistory() {
        try {
            if (personnelHistory.getPersonnelHistoryID() == null) {
                personnelHistory.setPersonnelHistoryID("tmp");
                personnelHistory.setPersonnel(resPersonnel);
                listPersonelHistory.add(personnelHistory);
                addMessage("Sukses", "Personel History Resource Berhasil Di Tambah");
            } else {
                int index = listPersonelHistory.indexOf(personnelHistory);
                if (index != -1) {
                    listPersonelHistory.set(index, personnelHistory);
                }
                addMessage("Sukses", "Personel History Berhasil Di update");
            }

            personnelHistory = new ResPersonnelHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tambahPersonilTraining() {
        try {
            SimpleDateFormat readFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = readFormat.parse(personnelTraining.getTrainingDate());

            SimpleDateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
            personnelTraining.setTrainingDate(writeFormat.format(date));
            if (personnelTraining.getPersonnelTrainingID() == null) {
                personnelTraining.setPersonnelTrainingID("tmp");
                personnelTraining.setPersonnel(resPersonnel);
                listPersonelTraining.add(personnelTraining);
                addMessage("Sukses", "Personel History Resource Berhasil Di Tambah");
            } else {
                int index = listPersonelTraining.indexOf(personnelTraining);
                if (index != -1) {
                    listPersonelTraining.set(index, personnelTraining);
                }
                addMessage("Sukses", "Personel History Berhasil Di update");
            }
            personnelTraining = new ResPersonnelTraining();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyUp() {
        ResPersonnel personnel = personnelRepo.findTopOneResPersonnelBypersonnelCode(resPersonnel.getPersonnelCode());
        if (personnel == null) {
            this.isPotencyDisabled = false;
        } else {
            if (StringUtils.isNotBlank(resPersonnel.getPersonnelCode())) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN,
                        "Peringatan",
                        "Nip Sudah Di Ada ");
            }
            this.isPotencyDisabled = true;
            this.isDropDown = true;
        }
    }

    public void initCoordinatePotensi() {
        latitudePotensi = new Coordinate();
        longitudePotensi = new Coordinate();
        latitudePotensi.setType(true);
        longitudePotensi.setType(false);
    }

    public void initCoordinateAsset() {
        latitudeAsset = new Coordinate();
        longitudeAsset = new Coordinate();
        latitudeAsset.setType(true);
        longitudeAsset.setType(false);
    }

    public void actionLatitudePotensi() {
        latitudePotensi.converter();
        latitudePotensi.setCounter(latitudePotensi.getCounter() + 1);
        if (latitudePotensi.getCounter() > 3) {
            latitudePotensi.setCounter(1);
        }
    }

    public void actionLongitudePotensi() {
        longitudePotensi.converter();
        longitudePotensi.setCounter(longitudePotensi.getCounter() + 1);
        if (longitudePotensi.getCounter() > 3) {
            longitudePotensi.setCounter(1);
        }
    }

    public void actionLatitudeAsset() {
        latitudeAsset.converter();
        latitudeAsset.setCounter(latitudeAsset.getCounter() + 1);
        if (latitudeAsset.getCounter() > 3) {
            latitudeAsset.setCounter(1);
        }
    }

    public void actionLongitudeAsset() {
        longitudeAsset.converter();
        longitudeAsset.setCounter(longitudeAsset.getCounter() + 1);
        if (longitudeAsset.getCounter() > 3) {
            longitudeAsset.setCounter(1);
        }
    }

    public void setCoordinatePotensi() {
        latitudePotensi.setType(true);
        longitudePotensi.setType(false);
        latitudePotensi.converter();
        longitudePotensi.converter();
        resPotency.setLatitude(latitudePotensi.getValidDecimalDegrees() != null
                ?latitudePotensi.getValidDecimalDegrees().toString() : null);
        resPotency.setLongitude(longitudePotensi.getValidDecimalDegrees() != null
                ? longitudePotensi.getValidDecimalDegrees().toString() : null);
    }

    public void setCoordinateAsset() {
        latitudeAsset.setType(true);
        longitudeAsset.setType(false);
        latitudeAsset.converter();
        longitudeAsset.converter();
        resAsset.setLatitude(latitudeAsset.getValidDecimalDegrees() != null
                ? latitudeAsset.getValidDecimalDegrees().toString() : null);
        resAsset.setLongitude(longitudeAsset.getValidDecimalDegrees() != null
                ? longitudeAsset.getValidDecimalDegrees().toString() : null);
    }

    public void viewCoordinatePotensi() {
        if (resPotency.getLatitude() != null
                || StringUtils.isNotBlank(resPotency.getLatitude())) {
            latitudePotensi.setType(true);
            latitudePotensi.setCounter(1);
            Double latitude = Double.parseDouble(resPotency.getLatitude());
            latitudePotensi.setDecimalDegrees(latitude);
            latitudePotensi.converter();
            latitudePotensi.setCounter(3);
        }
        if (resPotency.getLongitude() != null
                || StringUtils.isNotBlank(resPotency.getLongitude())) {
            longitudePotensi.setType(false);
            longitudePotensi.setCounter(1);
            Double longitude = Double.parseDouble(resPotency.getLongitude());
            longitudePotensi.setDecimalDegrees(longitude);
            longitudePotensi.converter();
            longitudePotensi.setCounter(3);
        }
    }

    public void viewCoordinateAsset() {
        if (resAsset.getLatitude() != null
                || StringUtils.isNotBlank(resAsset.getLatitude())) {
            latitudeAsset.setType(true);
            latitudeAsset.setCounter(1);
            Double latitude = Double.parseDouble(resAsset.getLatitude());
            latitudeAsset.setDecimalDegrees(latitude);
            latitudeAsset.converter();
            latitudeAsset.setCounter(3);
        }
        if (resAsset.getLongitude() != null
                || StringUtils.isNotBlank(resAsset.getLongitude())) {
            longitudeAsset.setType(false);
            longitudeAsset.setCounter(1);
            Double longitude = Double.parseDouble(resAsset.getLongitude());
            longitudeAsset.setDecimalDegrees(longitude);
            longitudeAsset.converter();
            longitudeAsset.setCounter(3);
        }
    }
}
