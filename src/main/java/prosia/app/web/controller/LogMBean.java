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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentRadiogram;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.LogImagesRepo;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data
class LogMBean extends AbstractManagedBean implements InitializingBean {

    @Value("${uploadFolder}")
    private String uploadFolder;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<IncidentLog> dataModel;
    private LogImages logImages;
    //private BaseImpl baseImpl;
    private Date twTgl;
    private Boolean showButton;
    private Boolean disabled;
    private IncidentLog incidentLog;
    private Incident incident;
    private Date logdate;
    private String remarks;
    private String attachment;
    //upload file
    private UploadedFile uploadfile;
    private IncidentRadiogram incidentRadiogram;
    private List<LogImages> listImage;
    private int idtemp;
    private String uuid;
    @Autowired
    private IncidentLogRepo incidentLogRepo;

    @Autowired
    private LogImagesRepo logImagesRepo;
    
    private List<LogImages> dataDeleteImage;

    public LogMBean() {
        this.incidentLog = new IncidentLog();
        incidentRadiogram = new IncidentRadiogram();
        //baseImpl = new BaseImpl();
        incident = null;
        showButton = true;
        logdate = new Date();
        remarks = null;
        attachment = null;
        twTgl = new Date();
        listImage = new ArrayList<LogImages>();
        logImages = new LogImages();
        uuid = UUID.randomUUID().toString();
    }

    public void setData() {
        dataModel = new LazyDataModelJPA(incidentLogRepo) {
            @Override
            protected long getDataSize() {
                return incidentLogRepo.count();
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentLogRepo.findAll(request);
            }

        };
    }

    public void simpan() {
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
                System.out.println("size : " +listImage.size());
                String temp[] = li.getPathname().split("\\\\");
                li.setLogID(incidentLog);
                li.setPathname(filePath + temp[2]);
                li.setDeleted(false);
                logImagesRepo.save(li);

                //
                // An existing directory to copy.
                //
                System.out.println("temp = " +temp[2] );
                String source = filePathSource + temp[2];
                System.out.println("source : " + uploadFolder + source);
                srcDir = new File(uploadFolder + source);
               
                //System.out.println("directoryListing :" + directoryListing.length);

                //
                // The destination directory to copy to. This directory
                // doesn't exists and will be created during the copy
                // directory process.
                //
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
            refresh();
            addMessage("Sukses", "Log berhasil disimpan");
        }
    }

    public String formatclassId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
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

    public void batal() {
        refresh();
    }

    private void refresh() {
        this.incidentLog = new IncidentLog();
        incidentRadiogram = new IncidentRadiogram();
        listImage = new ArrayList<LogImages>();
        twTgl = new Date();
        remarks = null;
        attachment = null;
    }

    public void hapusInsidenLog(IncidentLog i) {
        logger.debug("Log : {}", i);
        i.setDeleted(true);
        incidentLogRepo.save(i);
        addMessage("Sukses", "Log berhasil dihapus");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        idtemp = 0;
        setData();
    }

    public String createidtemp() {
        idtemp = idtemp + 1;
        String idtempstr = String.valueOf(idtemp);
        System.out.println("IDTEMPSTR : " + idtempstr);
        return idtempstr;
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
                RequestContext.getCurrentInstance().update("form-Log:listImg");
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));

            }
        }
    }

    public void hapusImage(LogImages images) {
        listImage.remove(images);
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
