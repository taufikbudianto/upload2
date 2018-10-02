package prosia.basarnas.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.CPNotifPersonil;
import prosia.basarnas.model.CPNotifTanggapan;
import prosia.basarnas.model.CPNotifikasi;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.repo.MstEmployeeUnitRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.NotifikasiPersonilRepo;
import prosia.basarnas.repo.NotifikasiRepo;
import prosia.basarnas.repo.NotifikasiTanggapanRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.service.NotifikasiService;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
public @Data
class NotifikasiMBean extends AbstractManagedBean implements InitializingBean {

    private LazyDataModelJPA<CPNotifikasi> listNotifikasi;
    private LazyDataModelJPA<ResPersonnel> listPersonil;
    private LazyDataModelJPA<CPNotifTanggapan> listTanggapan;
    private Map<String, String> listDropDownPersonil;
    @Value("${uploadFolder}")
    private String uploadFolder;
    private String fieldNotifikasi;
    private String valueNotifikasi;
    private String fieldPersonil;
    private String valuePersonil;
    private String headerItem;
    private Boolean isText;
    private Boolean isDropDown;
    private Boolean isDate;
    private Boolean isView;
    private Boolean isTextPersonil;
    private Boolean showItemNotif;
    private Boolean showListNotif;
    private Boolean showListPersonil;
    private Boolean showDetailPersonil;
    private Integer activeTabIndex;
    private Date valueDate;
    private CPNotifikasi notifikasi;
    private CPNotifTanggapan tanggapan;
    private ResPersonnel personil;
    private UploadedFile attachmentFile;
    private List<ResPersonnel> listPersonilChecked;
    private List<ResPersonnel> listPersonilTemp;
    private List<ResPersonnel> listPersonilTempChecked;
    private List<SelectItem> listKantorSar;

    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    @Autowired
    private NotifikasiRepo notifikasiRepo;
    @Autowired
    private ResPersonnelRepo personilRepo;
    @Autowired
    private MstEmployeeClassRepo classRepo;
    @Autowired
    private MstEmployeeUnitRepo unitRepo;
    @Autowired
    private MstEmployeePositionRepo positionRepo;
    @Autowired
    private NotifikasiPersonilRepo notifPersonilRepo;
    @Autowired
    private NotifikasiTanggapanRepo tanggapanRepo;
    @Autowired
    private NotifikasiService notifikasiService;

    @Override
    public void init() {
        notifikasi = new CPNotifikasi();
        listPersonilTemp = new ArrayList<>();
        listPersonilChecked = new ArrayList<>();
        listPersonilTempChecked = new ArrayList<>();
        listDropDownPersonil = new LinkedHashMap<>();
        isView = false;
        isDate = true;
        isText = false;
        isDropDown = false;
        showListNotif = true;
        showItemNotif = false;
        showListPersonil = false;
        showDetailPersonil = false;
        fieldPersonil = "";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listNotifikasi = new LazyDataModelJPA(notifikasiRepo) {
            @Override
            protected long getDataSize() {
                return notifikasiRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return notifikasiRepo.findAll(whereQuery(), request);
            }
        };
    }

    private Specification<CPNotifikasi> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPNotifikasi>() {
            @Override
            public Predicate toPredicate(Root<CPNotifikasi> root, CriteriaQuery<?> cq,
                    CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(valueNotifikasi) || valueDate != null) {
                    switch (fieldNotifikasi) {
                        case "notifTipe":
                            predicates.add(cb.equal(root.<Integer>get(fieldNotifikasi), valueNotifikasi));
                            break;
                        case "createdDate":
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String tanggal = formatter.format(valueDate);
                                String firstDate = Tanggal.dateTimeStringConvert(
                                        Tanggal.stringDateConvert(tanggal));
                                String secondDate = Tanggal.dateTimeStringConvert(
                                        Tanggal.addDayToDate(Tanggal.stringDateConvert(tanggal), 1));
                                predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(fieldNotifikasi),
                                        formatter.parse(firstDate)));
                                predicates.add(cb.lessThan(root.<Date>get(fieldNotifikasi),
                                        formatter.parse(secondDate)));
                            } catch (ParseException e) {
                                log.error("Parse Exception : {}", e.getMessage());
                            }
                            break;
                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(fieldNotifikasi)),
                                    getLikePattern(valueNotifikasi.trim())));
                            break;
                    }
                }
                cq.orderBy(cb.desc(root.get("createdDate")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<ResPersonnel> whereQueryPersonil() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResPersonnel>() {
            @Override
            public Predicate toPredicate(Root<ResPersonnel> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(valuePersonil)) {
                    switch (fieldPersonil) {
                        case "kantorsarname":
                            predicates.add(cb.equal(root.<MstKantorSar>get("officeSar").<String>get("kantorsarid"), valuePersonil));
                            break;
                        case "personnelType":
                            predicates.add(cb.equal(root.<Integer>get(fieldPersonil), Integer.parseInt(valuePersonil)));
                            break;
                        case "employmentClass":
                            predicates.add(cb.equal(root.<MstEMployeeClass>get(fieldPersonil).<String>get("employmentclassid"), valuePersonil));
                            break;
                        case "unit":
                            predicates.add(cb.equal(root.<MstEmployeeUnit>get(fieldPersonil).<String>get("employmentunitid"), valuePersonil));
                            break;
                        case "functionalPosition":
                            predicates.add(cb.equal(root.<MstEmployeePosition>get(fieldPersonil).<String>get("employmentpositionid"), valuePersonil));
                            break;
                        default:
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get(fieldPersonil)), getLikePattern(valuePersonil.trim()))
                            );
                            break;
                    }
                }
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), BigInteger.ONE));
                listPersonilTemp.forEach((temp) -> {
                    predicates.add(cb.notEqual(root.<String>get("personnelID"), temp.getPersonnelID()));
                });
                cq.orderBy(cb.asc(root.get("personnelName")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<CPNotifTanggapan> whereQueryTanggapan() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPNotifTanggapan>() {
            @Override
            public Predicate toPredicate(Root<CPNotifTanggapan> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<CPNotifikasi>get("notifikasi").
                        <Integer>get("notifId"), notifikasi.getNotifId()));
                predicates.add(cb.equal(root.<Boolean>get("status"), false));
                cq.orderBy(cb.desc(root.get("createdDate")));
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

    public void saveNotifikasi() {
        try {
            String filePath = "NotifUploaded" + File.separator;
            File file = new File(uploadFolder + filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            byte[] bytes = null;
            if (attachmentFile != null) {
                bytes = attachmentFile.getContents();
                String filename = FilenameUtils.getName(attachmentFile.getFileName());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(uploadFolder + filePath + filename)));
                stream.write(bytes);
                stream.close();
            }
        } catch (IOException e) {
            log.error("error file upload : {}", e.getMessage());
            e.printStackTrace();
        }
        notifikasiService.saveNotifikasi(notifikasi, listPersonilTemp);
        showListNotif = true;
        showListPersonil = false;
        showDetailPersonil = false;
        showItemNotif = false;
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Notifikasi berhasil disimpan");
    }

    public void onKolomPencarianChange() {
        valueDate = new Date();
        valueNotifikasi = "";
        if (fieldNotifikasi.equals("notifJudul")
                || fieldNotifikasi.equals("createdBy")) {
            isText = true;
            isDropDown = false;
            isDate = null;
        } else if (fieldNotifikasi.equals("notifTipe")) {
            isText = false;
            isDropDown = true;
            isDate = false;
        } else {
            isText = false;
            isDropDown = false;
            isDate = true;
        }
    }

    public void onChangeValue() {
        valuePersonil = "";
        if (fieldPersonil.equals("personnelName") || fieldPersonil.equals("personnelCode")) {
            isTextPersonil = true;
        } else {
            listDropDownPersonil = new LinkedHashMap<>();
            isTextPersonil = false;
            if (fieldPersonil.equals("kantorsarname")) {
                List<MstKantorSar> mstKantorSars
                        = kantorSarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.valueOf(0));
                mstKantorSars.forEach((kantorSar) -> {
                    listDropDownPersonil.put(kantorSar.getKantorsarname(), kantorSar.getKantorsarid());
                });
            } else if (fieldPersonil.equals("personnelType")) {
                listDropDownPersonil.put("Personil", "0");
                listDropDownPersonil.put("Potency", "1");
            } else if (fieldPersonil.equals("employmentClass")) {
                List<MstEMployeeClass> list = classRepo.findAll();
                list.forEach((eClass) -> {
                    listDropDownPersonil.put(eClass.getEmploymentclassname(), eClass.getEmploymentclassid());
                });
            } else if (fieldPersonil.equals("unit")) {
                List<MstEmployeeUnit> list = unitRepo.findAll();
                list.forEach((unit) -> {
                    listDropDownPersonil.put(unit.getEmploymentunitname(), unit.getEmploymentunitid());
                });
            } else if (fieldPersonil.equals("functionalPosition")) {
                List<MstEmployeePosition> list = positionRepo.findAllPositionFungsional();
                list.forEach((obj) -> {
                    listDropDownPersonil.put(obj.getEmploymentpositionname(), obj.getEmploymentpositionid());
                });
            }
        }
    }

    public void resetFilter() {
        fieldPersonil = "";
        valuePersonil = "";
        isTextPersonil = true;
    }

    public void loadForm() {
        notifikasi = (CPNotifikasi) getRequestParam("listRow");
        showItemNotif = true;
        showListNotif = false;
        showListPersonil = false;
        showDetailPersonil = false;
        listPersonilTemp = new ArrayList<>();
        if (notifikasi != null) {
            isView = true;
            headerItem = "Lihat Notifikasi";
            List<CPNotifPersonil> lPersonil = notifPersonilRepo.findByNotifikasi(notifikasi);
            lPersonil.forEach((res) -> {
                listPersonilTemp.add(res.getPersonnel());
            });
            loadListTanggapan();
        } else {
            notifikasi = new CPNotifikasi();
            headerItem = "Tambah Notifikasi";
        }
    }

    public void loadPersonil() {
        personil = (ResPersonnel) getRequestParam("rowPersonil");
        activeTabIndex = 0;
        showListNotif = false;
        showListPersonil = false;
        showDetailPersonil = true;
        showItemNotif = false;
    }

    public void hidePersonil() {
        showListNotif = false;
        showListPersonil = false;
        showDetailPersonil = false;
        showItemNotif = true;
    }

    public void attachment(FileUploadEvent e) {
        attachmentFile = e.getFile();
        notifikasi.setNotifAttachment(attachmentFile.getFileName());
        log.debug("attachmentFile : {}", attachmentFile.getFileName());
    }

    public void loadTanggapan() {
        tanggapan = (CPNotifTanggapan) getRequestParam("rowTanggap");
        RequestContext.getCurrentInstance().execute("PF('wgDtlTanggapan').show();");
    }

    public void hapusTanggapan() {
        tanggapan = (CPNotifTanggapan) getRequestParam("rowTanggap");
        if (tanggapan != null) {
            tanggapan.setStatus(true);
            tanggapanRepo.save(tanggapan);
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Tanggapan berhasil dihapus");
        }
    }

    public void loadListTanggapan() {
        listTanggapan = new LazyDataModelJPA(tanggapanRepo) {
            @Override
            protected long getDataSize() {
                return tanggapanRepo.count(whereQueryTanggapan());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return tanggapanRepo.findAll(whereQueryTanggapan(), request);
            }
        };
    }

    public void showPersonil() {
        showListPersonil = true;
        showDetailPersonil = false;
        showListNotif = false;
        showItemNotif = false;
        listPersonilChecked = new ArrayList<>();
        listPersonil = new LazyDataModelJPA(personilRepo) {
            @Override
            protected long getDataSize() {
                return personilRepo.count(whereQueryPersonil());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return personilRepo.findAll(whereQueryPersonil(), request);
            }
        };
        RequestContext.getCurrentInstance().execute("PF('listPersonil').getPaginator().setPage(0)");
    }

    public void resetPersonil() {
        valuePersonil = "";
    }

    public void personilChecked() {
        if (listPersonilChecked.isEmpty() || listPersonilChecked.size() < 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan!!!", "Personil harus dipilih");
        } else {
            listPersonilChecked.forEach((person) -> {
                listPersonilTemp.add(person);
            });
            showListNotif = false;
            showListPersonil = false;
            showDetailPersonil = false;
            showItemNotif = true;
        }
    }

    public void removePersonilTempChecked() {
        if (listPersonilTempChecked.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan!!!", "Pilih personil yang akan dihapus");
        } else {
            listPersonilTempChecked.forEach((tempChecked) -> {
                listPersonilTemp.remove(tempChecked);
            });
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses!!!", "Personil berhasil dihapus");
        }
    }

    public List<SelectItem> getListKantorSar() {
        listKantorSar = new ArrayList<>();
        kantorSarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.ZERO).stream().forEach((kantorSar) -> {
            listKantorSar.add(new SelectItem(kantorSar.getKantorsarid(), kantorSar.getKantorsarname()));
        });
        return listKantorSar;
    }

    public String timestampConvert(Date date) {
        return Tanggal.dateTimeStringConvert(date);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
