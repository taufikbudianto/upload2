/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
import prosia.basarnas.model.AssetTypeImages;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.AssetTypeImageRepo;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class MstAssetTypeMBean extends AbstractManagedBean implements InitializingBean {

    @Value("${uploadFolder}")
    private String uploadFolder;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<MstAssetType> lazyDataModelJPA;
    private MstAssetType assetType;
    @Autowired
    private MstAssetTypeRepo mstAssetTypeRepo;
    @Autowired
    private MenuNavMBean menuNavMBean;
    private BufferedImage images;
    private AssetTypeImages assetTypeImages;
    private List<AssetTypeImages> listImage;
    private List<AssetTypeImages> dataDeleteImage;
    private String field;
    private String value;
    private Boolean isShowFormAssetType;
    private Boolean showCombo;
    private String where;
    private Boolean showButton;
    private Boolean showButtonInput;
    private Boolean disabled;
    private String uuid;
    private String assetname;
    private String kategori;
    private BigInteger issru;
    private String image;
    private Boolean isReadOnly;

    private UploadedFile uploadfile;
    private StreamedContent imageupload;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MediaManager mediaManager;

    @Autowired
    private AssetTypeImageRepo assetTypeImageRepo;

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(mstAssetTypeRepo) {
            @Override
            protected long getDataSize() {
                return mstAssetTypeRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return mstAssetTypeRepo.findAll(whereQuery(field, value), request);
            }

            private Specification<MstAssetType> whereQuery(
                    final String field,
                    final String value) {
                List<Predicate> predicates = new ArrayList<>();
                return new Specification<MstAssetType>() {

                    @Override
                    public Predicate toPredicate(Root<MstAssetType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        if (StringUtils.isNotBlank(value)) {
                            switch (field) {
                                case "issru":
                                    predicates.add(cb.equal(root.<Integer>get(field), value));
                                    break;

                                default:
                                    predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                            getLikePattern(value.trim())));
                                    break;
                            }
                        }
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
        };
    }

    public void chengeToCombo() {
        value = "";
        showCombo = field.equals("issru");
    }

    public MstAssetTypeMBean() throws IOException {
        assetType = new MstAssetType();
        assetname = new String();
        showButton = true;
        disabled = false;
        showButtonInput = true;
        isShowFormAssetType = false;
        uuid = UUID.randomUUID().toString();
        listImage = new ArrayList<AssetTypeImages>();
        dataDeleteImage = new ArrayList<>();
        assetTypeImages = new AssetTypeImages();
        isReadOnly = true;
    }

    public String formatassetId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATE : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String assetId = "";

        List<MstAssetType> lis = mstAssetTypeRepo.findTopOneByAssettypeidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            assetId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxId = mstAssetTypeRepo.findAssetByMaxId("CGK");
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
                assetId = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                assetId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
            //}
        }
        return assetId;
    }

    public void simpan() throws SQLException, FileNotFoundException {
        MstAssetType validate = mstAssetTypeRepo.findTopOneMstAssetTypeByAssetnameAndIssruAndMatra(assetType.getAssetname(), assetType.getIssru(), assetType.getMatra());
        //employeePosition.setCategory(employeePosition.getKategori().getValue());
        if (validate != null) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "Peringatan",
                    "Nama, Kategori dan Matra Sudah Ada ");
            return;
        } else {
            if (assetType.getAssettypeid() == null) {
                assetType.setAssettypeid(formatassetId());
            }
            assetType.setIssru(assetType.getIssru());
            assetType.setMatra(assetType.getMatra());
            assetType.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
            assetType.setUsersiteid(ProsiaConstant.SITES);
            assetType.setDatecreated(new Date());
            assetType.setIsdeleted(BigInteger.ZERO);
            mstAssetTypeRepo.save(assetType);

            String filePath = "ASSETTYPE" + File.separator + assetType.getAssettypeid() + File.separator;
            String filePathSource = "TEMP_ASSETTYPE" + File.separator + uuid + File.separator;
            File destDir = null;
            File destDirName = null;
            File srcDir = null;
            for (AssetTypeImages ati : listImage) {
                System.out.println("size : " + listImage.size());
                String temp[] = ati.getPathname().split("\\\\");
                ati.setAssettypeid(assetType);
                ati.setPathname(filePath + temp[2]);
                ati.setDeleted(false);
                assetTypeImageRepo.save(ati);

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
                if (assetTypeImages.getPathname() != null) {
                    String tempDest[] = assetTypeImages.getPathname().split("\\\\");
                    String destination = tempDest[0] + File.separator + tempDest[1];
                    System.out.println("destination: " + destination);
                    //System.out.println("dest : " + uploadFolder + tempDest[0] + File.separator + tempDest[1]);
                    destDir = new File(uploadFolder + filePath);
                    destDirName = new File(uploadFolder + filePath + tempDest[2]);
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
                        for (int i = 0; i < listImage.size(); i++) {
                            String name = uploadFolder + filePath + FilenameUtils.getName(uploadfile.getFileName());
                            System.out.println("name = " + name);
                            System.out.println("uploadFolder + source = " + (uploadFolder + filePath + tempDest[2]));
                            System.out.println("destDirName = " + destDirName);
                            if ((uploadFolder + filePath + tempDest[2]).equals(name)) {
                                System.out.println("a");
                                //listImage.remove(i);
                                destDirName.delete();
                            }
                        }

                        FileUtils.moveFileToDirectory(srcDir, destDir, true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            //update status isDeleted
            for (AssetTypeImages assetTypeImages : dataDeleteImage) {
                assetTypeImages.setDeleted(true);
                assetTypeImageRepo.save(assetTypeImages);
            }

            System.out.println("ASEET TYPE ID : " + assetType.getAssettypeid());
            refresh();
            isShowFormAssetType = false;
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Tipe Aset berhasil disimpan");
        }
    }

    public void add() {
        isReadOnly = true;
        showButton = true;
        disabled = false;
        isShowFormAssetType = true;
        refresh();
        RequestContext.getCurrentInstance().reset("assettype-content");
        RequestContext.getCurrentInstance().update("assettype-content");
    }

    public void viewAsset(String assettypeid, Boolean edited) throws IOException {
        isReadOnly = false;
        isShowFormAssetType = true;
        RequestContext.getCurrentInstance().reset("assettype-content");
        RequestContext.getCurrentInstance().update("assettype-content");
        this.assetType = mstAssetTypeRepo.findOne(assettypeid);
        assetname = assetType.getAssetname();
        kategori = assetType.getIssru().toString();
//        mediaManager.setMedia(null);
//        //imageupload = new DefaultStreamedContent(uploadfile.getInputstream());
//        if (assetType.getGissymbol2() != null) {
////            System.out.println("masuk");
////            imageupload = new DefaultStreamedContent(new ByteArrayInputStream(assetType.getGissymbol2()), "image/png");
//            mediaManager.setMedia(assetType.getGissymbol2());
//        }

        listImage = assetTypeImageRepo.findByAssettypeidAndDeleted(assetType, false);
        if (listImage == null) {
            listImage = new ArrayList<>();
        }

        logger.debug("Kategori : {}", kategori);
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);
    }

    public void batal() {
        refresh();
        RequestContext.getCurrentInstance().reset("assettype-content");
        RequestContext.getCurrentInstance().update("assettype-content");
        //RequestContext.getCurrentInstance().execute("PF('widgetNegara').show()");
    }

    public void refresh() {
        assetType = new MstAssetType();
        assetname = new String();
        listImage = new ArrayList<>();
        dataDeleteImage = new ArrayList<>();
        //RequestContext.getCurrentInstance().update("idAssetInput");
    }

    public void hideForm() {
        isShowFormAssetType = false;
    }

    public void hapusSimbol() {
        System.out.println("bbbb");
        mediaManager.setMedia(null);
        assetType.setGissymbol2(null);
        uploadfile = null;
        RequestContext.getCurrentInstance().update("form-asset:pgImage");
        RequestContext.getCurrentInstance().update("form-asset:btnHapus");
        RequestContext.getCurrentInstance().update("form-assetInput:pgImage");
        RequestContext.getCurrentInstance().update("form-assetInput:btnHapus");

    }

    public void hapus(MstAssetType i) {
        logger.debug("Unit : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        mstAssetTypeRepo.save(i);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Tipe Aset berhasil dihapus");
    }

    public StreamedContent getImagePreview() {
        if (uploadfile != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(uploadfile.getContents()), uploadfile.getContentType());
        } else {
            return new DefaultStreamedContent();
        }
    }

//    public void upload(FileUploadEvent e) throws IOException, SQLException {
//        assetname = new String();
//        uploadfile = e.getFile();
//        mediaManager.setMedia(e.getFile().getContents());
//        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));
//
//    }
    public void upload(FileUploadEvent e) {
        synchronized (this) {
            try {
                assetTypeImages = new AssetTypeImages();
                System.out.println("---runApplication--" + e.getFile());
                uploadfile = e.getFile();
                String filePath = "TEMP_ASSETTYPE" + File.separator + uuid + File.separator;
                System.out.println("Upload Path " + filePath);
                File file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;
                File srcDir = null;
                if (uploadfile != null) {

                    for (int i = 0; i < listImage.size(); i++) {
                        System.out.println("listImage.size() = " + listImage.size());
                        String temp[] = listImage.get(i).getPathname().split("\\\\");
                        String source = filePath + temp[2];
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadfile.getFileName());
                        System.out.println("sourcessss : " + uploadFolder + source);
                        srcDir = new File(uploadFolder + source);
                        System.out.println("listImage.indexOf(i)= " + listImage.indexOf(i));
                        if ((uploadFolder + source).equals(name)) {
                            System.out.println("a");
                            listImage.remove(i);
                        }

                    }

                    bytes = uploadfile.getContents();
                    String filename = FilenameUtils.getName(uploadfile.getFileName());
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(uploadFolder + filePath + filename)));
                    stream.write(bytes);
                    stream.close();

                    assetTypeImages.setPathname(filePath + uploadfile.getFileName());
                    System.out.println("assetTypeImages.getPathname() :" + assetTypeImages.getPathname());
                    System.out.println("uploadFolder + filePath + filename :" + uploadFolder + filePath + filename);
                    listImage.add(assetTypeImages);

                    System.out.println("Path : " + assetTypeImages.getPathname());
                }
                RequestContext.getCurrentInstance().update("assettype-content:form-asset:listImg");
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully");
             } catch (Exception ex) {
                addPopUpMessage(FacesMessage.SEVERITY_INFO, "Gagal", "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Failed");
               
            }
        }
    }

    public void hapusImage(AssetTypeImages images) {
        listImage.remove(images);
        dataDeleteImage.add(images);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Image berhasil dihapus");
    }

    public void showImage(String images) throws IOException {
        System.out.println("hasilssssss =" + uploadFolder + images);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(uploadFolder + images);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
