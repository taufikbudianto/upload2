/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import prosia.basarnas.model.CPPerpusPersonil;
import prosia.basarnas.model.CPPerpustakaan;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.repo.MstEmployeeUnitRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.PerpustakaanPersonilRepo;
import prosia.basarnas.repo.PerpustakkanRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.service.PerpustakaanService;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
public @Data
class PerpustakaanMBean extends AbstractManagedBean implements InitializingBean {

    private LazyDataModelJPA<CPPerpustakaan> listPerpus;
    private LazyDataModelJPA<ResPersonnel> listPersonil;
    private List<ResPersonnel> listPersonilTemp;
    private List<ResPersonnel> listPersonilChecked;
    private List<ResPersonnel> listPersonilTempChecked;
    private Map<String, String> listDropDown;
    private CPPerpustakaan perpus;
    private ResPersonnel personil;
    @Value("${uploadFolder}")
    private String uploadFolder;
    private String fieldPersonil;
    private String valuePersonil;
    private String headerItem;
    private String fieldPerpus;
    private String valuePerpus;
    private Date valueDate;
    private Boolean showListPerpus;
    private Boolean showItemPerpus;
    private Boolean showListPersonil;
    private Boolean showDetailPersonil;
    private Boolean isDate;
    private Boolean isView;
    private Boolean isFieldPersonil;
    private UploadedFile attachmentFile;
    private Integer activeTabIndex;

    @Autowired
    private PerpustakkanRepo perpusRepo;
    @Autowired
    private PerpustakaanPersonilRepo perpusPersonilRepo;
    @Autowired
    private ResPersonnelRepo personilRepo;
    @Autowired
    private MstEmployeeClassRepo classRepo;
    @Autowired
    private MstEmployeeUnitRepo unitRepo;
    @Autowired
    private MstEmployeePositionRepo positionRepo;
    @Autowired
    private MstKantorSarRepo kansarRepo;
    @Autowired
    private PerpustakaanService perpusService;

    @Override
    public void init() {
        listPersonilChecked = new ArrayList<>();
        listPersonilTempChecked = new ArrayList<>();
        perpus = new CPPerpustakaan();
        showListPerpus = true;
        showItemPerpus = false;
        showListPersonil = false;
        showDetailPersonil = false;
        isDate = true;
        isView = false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listPerpus = new LazyDataModelJPA(perpusRepo) {
            @Override
            protected long getDataSize() {
                return perpusRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return perpusRepo.findAll(whereQuery(), request);
            }
        };
    }

    private Specification<CPPerpustakaan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPPerpustakaan>() {
            @Override
            public Predicate toPredicate(Root<CPPerpustakaan> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(valuePerpus) || valueDate != null) {
                    switch (fieldPerpus) {
                        case "createdDate":
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String tanggal = formatter.format(valueDate);
                                String firstDate = Tanggal.dateTimeStringConvert(
                                        Tanggal.stringDateConvert(tanggal));
                                String secondDate = Tanggal.dateTimeStringConvert(
                                        Tanggal.addDayToDate(Tanggal.stringDateConvert(tanggal), 1));
                                predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(fieldPerpus),
                                        formatter.parse(firstDate)));
                                predicates.add(cb.lessThan(root.<Date>get(fieldPerpus),
                                        formatter.parse(secondDate)));
                            } catch (ParseException e) {
                                log.error("Parse Exception : {}", e.getMessage());
                            }
                            break;
                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(fieldPerpus)),
                                    getLikePattern(valuePerpus.trim())));
                            break;
                    }
                }
                predicates.add(cb.equal(root.<Boolean>get("deleted"), false));
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

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void showPersonil() {
        showListPersonil = true;
        showListPerpus = false;
        showItemPerpus = false;
        showDetailPersonil = false;
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

    public void savePerpustakaan() {
        try {
            String filePath = "LibUploaded" + File.separator;
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
        if (perpus.getLibId() != null) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Perpustakaan berhasil diubah");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Perpustakaan berhasil disimpan");
        }
        perpusService.savePerpustakaan(perpus, listPersonilTemp);
        showListPerpus = true;
        showListPersonil = false;
        showDetailPersonil = false;
        showItemPerpus = false;
    }

    public void onFilterChange() {
        valueDate = new Date();
        valuePerpus = "";
        isDate = fieldPerpus.equals("createdDate");
    }

    public void onChangeValue() {
        if (fieldPersonil.equals("personnelName") || fieldPersonil.equals("personnelCode")) {
            isFieldPersonil = true;
        } else {
            listDropDown = new LinkedHashMap<>();
            isFieldPersonil = false;
            if (fieldPersonil.equals("kantorsarname")) {
                List<MstKantorSar> mstKantorSars
                        = kansarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.valueOf(0));
                mstKantorSars.forEach((kantorSar) -> {
                    listDropDown.put(kantorSar.getKantorsarname(), kantorSar.getKantorsarid());
                });
            } else if (fieldPersonil.equals("personnelType")) {
                listDropDown.put("Personil", "0");
                listDropDown.put("Potency", "1");
            } else if (fieldPersonil.equals("employmentClass")) {
                List<MstEMployeeClass> list = classRepo.findAll();
                list.forEach((eClass) -> {
                    listDropDown.put(eClass.getEmploymentclassname(), eClass.getEmploymentclassid());
                });
            } else if (fieldPersonil.equals("unit")) {
                List<MstEmployeeUnit> list = unitRepo.findAll();
                list.forEach((unit) -> {
                    listDropDown.put(unit.getEmploymentunitname(), unit.getEmploymentunitid());
                });
            } else if (fieldPersonil.equals("functionalPosition")) {
                List<MstEmployeePosition> list = positionRepo.findAllPositionFungsional();
                list.forEach((obj) -> {
                    listDropDown.put(obj.getEmploymentpositionname(), obj.getEmploymentpositionid());
                });
            }
        }
    }

    public void removePersonilTempChecked() {
        if (listPersonilTempChecked.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan!!!", "Pilih personil yang akan dihapus");
        } else {
            listPersonilTempChecked.forEach((resP) -> {
                listPersonilTemp.remove(resP);
            });
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses!!!", "Personil berhasil dihapus");
        }
    }

    public void removePerpus() {
        perpus = (CPPerpustakaan) getRequestParam("listRow");
        perpus.setDeleted(true);
        perpusRepo.save(perpus);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses!!!", "Perpustakaan berhasil dihapus");
    }

    public void loadForm() {
        perpus = (CPPerpustakaan) getRequestParam("listRow");
        showItemPerpus = true;
        showListPerpus = false;
        showListPersonil = false;
        showDetailPersonil = false;
        listPersonilTemp = new ArrayList<>();
        if (perpus != null) {
            Integer flag = Integer.parseInt((String) getRequestParam("flag"));
            if (flag == 1) {
                isView = true;
                headerItem = "Daftar Perpustakaan";
            } else {
                isView = false;
                headerItem = "Ubah Perpustakaan";
            }
            List<CPPerpusPersonil> lPersonil = perpusPersonilRepo.findByPerpustakaan(perpus);
            lPersonil.forEach((cppp) -> {
                listPersonilTemp.add(cppp.getPersonil());
            });
        } else {
            perpus = new CPPerpustakaan();
            headerItem = "Tambah Perpustakaan";
        }
    }

    public void personilChecked() {
        if (listPersonilChecked.isEmpty() || listPersonilChecked.size() < 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan!!!", "Personil harus dipilih");
        } else {
            listPersonilChecked.forEach((person) -> {
                listPersonilTemp.add(person);
            });
            showListPerpus = false;
            showListPersonil = false;
            showDetailPersonil = false;
            showItemPerpus = true;
        }
    }

    public void loadPersonil() {
        personil = (ResPersonnel) getRequestParam("rowPersonil");
        activeTabIndex = 0;
        showListPerpus = false;
        showListPersonil = false;
        showDetailPersonil = true;
        showItemPerpus = false;
    }

    public void resetFilter() {
        fieldPersonil = "";
        valuePersonil = "";
        isFieldPersonil = true;
    }

    public void hideListPersonil() {
        showListPersonil = false;
        showDetailPersonil = false;
        showListPerpus = false;
        showItemPerpus = true;
    }

    public void attachment(FileUploadEvent e) {
        attachmentFile = e.getFile();
        perpus.setLibAttachment(attachmentFile.getFileName());
        log.debug("attachmentFile : {}", attachmentFile.getFileName());
    }

    public String timestampConvert(Date date) {
        return Tanggal.dateTimeStringConvert(date);
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
