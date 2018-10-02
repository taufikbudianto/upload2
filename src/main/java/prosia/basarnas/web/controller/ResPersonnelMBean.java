/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prosia.app.model.Role;
import prosia.app.model.Setting;
import prosia.app.model.User;
import prosia.app.repo.RoleRepo;
import prosia.app.repo.SettingRepo;
import prosia.app.repo.UserRepo;
import prosia.app.service.UserService;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.LogImages;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstEmployeeUnit;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.MstSkill;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelImages;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.repo.MstEmployeeClassRepo;
import prosia.basarnas.repo.MstEmployeePositionRepo;
import prosia.basarnas.repo.MstEmployeeUnitRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstPosSarRepo;
import prosia.basarnas.repo.MstSkillRepo;
import prosia.basarnas.repo.PersonelHistoryRepo;
import prosia.basarnas.repo.PersonelTrainingRepo;
import prosia.basarnas.repo.ResPersonnelImagesRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.service.PersonnelService;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")

public @Data
class ResPersonnelMBean extends AbstractManagedBean implements InitializingBean {

    @Value("${uploadFolder}")
    private String uploadFolder;
    private LazyDataModelJPA<ResPersonnel> lazyDataModelJPA;
    @Autowired
    private ResPersonnelRepo personnelRepo;
    @Autowired
    private PersonelHistoryRepo personelHistoryRepo;
    @Autowired
    private PersonelTrainingRepo personelTrainingRepo;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    @Autowired
    private MstEmployeeClassRepo mstEmployeeClassRepo;
    @Autowired
    private MstEmployeePositionRepo mstEmployeePositionRepo;
    @Autowired
    private MstEmployeeUnitRepo unitRepo;
    @Autowired
    private MstSkillRepo skillRepo;
    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;
    @Autowired
    private MstPosSarRepo posSarRepo;
    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private ResPersonnelImagesRepo resPersonnelImagesRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private SettingRepo settingRepo;
    @Autowired
    private UserRepo userRepo;
    private ResPersonnelImages resPersonnelImages;
    private UploadedFile uploadfile;
    private String uuid;
    private ResPersonnel resPersonnel;
    private ResPersonnelHistory personnelHistory;
    private ResPersonnelTraining personnelTraining;
    private MstSkill mstSkill;
    private User userDetail;
    private Role selectedRole;
    private List<ResPersonnelImages> dataDeleteImage;
    private List<ResPersonnelHistory> listPersonelHistory;
    private List<MstKantorSar> listKansar;
    private List<ResPersonnelTraining> listPersonnelTraining;
    private List<MstPosSar> listPostSar;
    private List<MstEMployeeClass> listGolongan;
    private List<MstSkill> listSkill;
    private List<MstEmployeePosition> listJabatanStruktural;
    private List<MstEmployeePosition> listJabatanFungsional;
    private List<MstEmployeePosition> listJabatanSiaga;
    private List<MstEmployeeUnit> listUnitKerja;
    private List<ResPersonnelImages> listImage;
    private List<Role> availableRoles;
    private List<SelectItem> rolesList;
    private List<SelectItem> selectKanSar;
    private List<User> listUser;
    private DualListModel<Role> roles;
    private String field;
    private String value;
    private String dropdown;
    private String currentId;
    private String currentPersonnel;
    private String currentRole;
    private String headerDetail;
    private String passwordInput;
    private String fullNameInput;
    private String userName;
    private String personelUser;
    private String namaKansar;
    private Boolean isText;
    private Boolean isDropDown;
    private Map<String, String> listDropDown;
    private String imgName;
    private Boolean showButton;
    private Boolean disabled;
    private Boolean disabledKanSar;
    private Boolean disabledRole;
    private Boolean showCombo;
    private Boolean isReadOnly;
    private Boolean isShowDetail;
    private Boolean isPotencyDisabled;
    private Boolean isShowDetailOnTab;
    private Boolean isShowDetailUser;
    private Boolean isShowDetailPersonil;
    private Boolean isReadOnlyField;
    private Boolean isMultipleRoles;
    private Boolean isGeneratedPassword;
    private Boolean potensi = false;
    private Boolean basarnas = true;
    private Integer potensiint;
    private Integer basarnasint;
    private Map<String, String> checkedUser;

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        lazyDataModelJPA = new LazyDataModelJPA(personnelRepo) {
//            @Override
//            protected long getDataSize() {
//                return personnelRepo.count(whereQuery(field, value));
//            }
//
//            @Override
//            protected Page getDatas(PageRequest request) {
//                return personnelRepo.findAll(whereQuery(field, value), request);
//            }
//        };
//    }
    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(personnelRepo) {
            @Override
            protected long getDataSize() {
                return personnelRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                Page<ResPersonnel> rp = personnelRepo.findAll(whereQuery(field, value), request);
                checkedUser = prepareUser(rp, checkedUser);
                return rp;
            }
        };

        availableRoles = roleRepo.findAllByEnabledAndCurrentTenant(true);

        try {
            isMultipleRoles = Boolean.valueOf(settingRepo.findOne(Setting.X_MULTI_ROLES).getValue());
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_MULTI_ROLES);
            isMultipleRoles = false;
        }

        if (!isMultipleRoles) {
            rolesList = new ArrayList<>();
            for (Role role : availableRoles) {
                rolesList.add(new SelectItem(role, role.getRoleName()));
            }
        }
    }

    @Override
    public void init() {
        resPersonnel = new ResPersonnel();
        personnelHistory = new ResPersonnelHistory();
        personnelTraining = new ResPersonnelTraining();
        mstSkill = new MstSkill();
        selectedRole = new Role();
        userName = new String();
        isText = true;
        isDropDown = false;
        uuid = UUID.randomUUID().toString();
        listImage = new ArrayList<>();
        dataDeleteImage = new ArrayList<>();
        isShowDetail = true;
        isShowDetailUser = false;
        isShowDetailPersonil = false;
        disabled = false;
        disabledKanSar = false;
        disabledRole = false;
        isGeneratedPassword = false;
        checkedUser = new HashMap<String, String>();
    }

    private Map<String, String> prepareUser(Page<ResPersonnel> page, Map<String, String> checked) {
        for (ResPersonnel ra : page.getContent()) {
            User u = userRepo.findOneByResPersonnel(ra);
            if (u == null) {
                if (!checked.isEmpty() && checked.containsKey(ra.getPersonnelID())) {
                    checked.replace(ra.getPersonnelID(), "amber-btn");
                } else {
                    checked.put(ra.getPersonnelID(), "amber-btn");
                }
            } else {
                if (!checked.isEmpty() && checked.containsKey(ra.getPersonnelID())) {
                    checked.replace(ra.getPersonnelID(), "blue-grey-btn");
                } else {
                    checked.put(ra.getPersonnelID(), "blue-grey-btn");
                }
            }
        }
        return checked;
    }

    public void showAllList() {

        this.listKansar = mstKantorSarRepo.findAll();
        this.listGolongan = mstEmployeeClassRepo.findAll();
        this.listUnitKerja = unitRepo.findAll();
        this.listJabatanFungsional = mstEmployeePositionRepo.findAllPositionFungsional();
        this.listJabatanStruktural = mstEmployeePositionRepo.findAllPositionStruktural();
        this.listJabatanSiaga = mstEmployeePositionRepo.findAllPositionSiaga();
        this.listSkill = skillRepo.findSkill();
    }

    public void checkPotensi() {
        String summary = potensi ? "Checked" : "Unchecked";

        //System.out.println("summary : " + summary);
        if (summary.equals("Unchecked")) {
            potensi = false;
            potensiint = 0;
        } else {
            potensi = true;
            potensiint = 1;
        }
    }

    public void checkBasarnas() {
        String summary = basarnas ? "Checked" : "Unchecked";
        if (summary.equals("Unchecked")) {
            basarnas = false;
            basarnasint = 0;
        } else {
            basarnas = true;
            basarnasint = 1;
        }
    }

    private Specification<ResPersonnel> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<ResPersonnel>() {
            @Override
            public Predicate toPredicate(Root<ResPersonnel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "kantorsarname":
                            predicates.add(cb.equal(root.<MstKantorSar>get("officeSar").<String>get("kantorsarid"), value));
                            break;
//                        case "personnelType":
//                            predicates.add(cb.equal(root.<Integer>get(field), Integer.parseInt(value)));
//                            break;
//                        case "employmentClass":
//                            predicates.add(cb.equal(root.<MstEMployeeClass>get(field).<String>get("employmentclassid"), value));
//                            break;
//                        case "unit":
//                            predicates.add(cb.equal(root.<MstEmployeeUnit>get(field).<String>get("employmentunitid"), value));
//                            break;
//                        case "functionalPosition":
//                            predicates.add(cb.equal(root.<MstEmployeePosition>get(field).<String>get("employmentpositionid"), value));
//                            break;
                        case "potencyname":
                            //System.out.println("Test : " + getLikePattern(value));
                            predicates.add(cb.like(cb.upper(root.<ResPotency>get("potency").<String>get("potencyname")), getLikePattern(value.trim()).toUpperCase()));
                            //predicates.add(cb.like(root.<ResPotency>get("potency").<String>get("potencyname"), getLikePattern(value)));
                            break;
                        default:
                            predicates.add(
                                    cb.like(cb.lower(root.<String>get(field)), getLikePattern(value.trim()))
                            );
                            break;
                    }
                }
                if (basarnas == true && potensi == true) {
                    //System.out.println("Masuk true semua");
                    predicates.add(cb.isNotNull(root.<String>get("officeSar")));
                    predicates.add(cb.isNotNull(root.<String>get("potency")));
                } else if (basarnas == true && potensi == false) {
                    //System.out.println("Masuk true basarnas");
                    //predicates.add(cb.isNotNull(root.<String>get("officeSar")));
                    predicates.add(cb.isNull(root.<String>get("potency")));
                    //predicates.add(cb.isNotNull(root.<String>get("code")));
                } else if (potensi == true && basarnas == false) {
                    //predicates.add(cb.isNull(root.<String>get("officeSar")));
                    predicates.add(cb.isNotNull(root.<String>get("potency")));
                } else {
                    predicates.add(cb.isNull(root.<String>get("officeSar")));
                    predicates.add(cb.isNull(root.<String>get("potency")));
                }

                if (currentId.equals("BSN")) {
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                } else {
                    predicates.add(cb.equal(root.<MstKantorSar>get("officeSar").<String>get("kantorsarid"), currentId));
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

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public void onKolomPencarianChange() {
        dropdown = "";
        value = "";
        if (field != null && (field.equals("personnelName") || field.equals("personnelCode")
                || field.equals("phoneNumber") || field.equals("email") || field.equals("potencyname"))) {
            isText = true;
            isDropDown = false;
        } else {
            listDropDown = new LinkedHashMap<String, String>();
            isText = false;
            isDropDown = true;
            if (field != null && (field.equals("kantorsarname"))) {
                List<MstKantorSar> mstKantorSars
                        = mstKantorSarRepo.findByIsdeletedOrderByKantorsarname(BigInteger.valueOf(0));
                System.out.println("mstKantorSars " + mstKantorSars.size());
                for (MstKantorSar kantorSar : mstKantorSars) {
                    listDropDown.put(kantorSar.getKantorsarname(), kantorSar.getKantorsarid());
                }
            } else if (field != null && (field.equals("personnelType"))) {
                listDropDown.put("Personil", "0");
                listDropDown.put("Potency", "1");
            } else if (field != null && (field.equals("employmentClass"))) {
                List<MstEMployeeClass> list = mstEmployeeClassRepo.findAll();
                for (MstEMployeeClass obj : list) {
                    listDropDown.put(obj.getEmploymentclassname(), obj.getEmploymentclassid());
                }
            } else if (field != null && (field.equals("unit"))) {
                List<MstEmployeeUnit> list = unitRepo.findAll();
                for (MstEmployeeUnit obj : list) {
                    listDropDown.put(obj.getEmploymentunitname(), obj.getEmploymentunitid());
                }
            } else if (field != null && (field.equals("functionalPosition"))) {
                List<MstEmployeePosition> list = mstEmployeePositionRepo.findAllPositionFungsional();
                for (MstEmployeePosition obj : list) {
                    listDropDown.put(obj.getEmploymentpositionname(), obj.getEmploymentpositionid());
                }
            }
        }
    }

    public String getKantorSarByKantorSarId(String kantorSarId) {
        return kantorSarRepo.findOne(kantorSarId).getKantorsarname();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public List<SelectItem> getSelectKanSar() {
        if (selectKanSar == null) {
            selectKanSar = new ArrayList<>();
            for (MstKantorSar kanSar : kantorSarRepo.findAll()) {
                selectKanSar.add(new SelectItem(kanSar.getKantorsarname(), kanSar.getKantorsarname()));
            }
        }
        return selectKanSar;
    }

    public void showUser() {
        resPersonnel = (ResPersonnel) getRequestParam("listRowUser");
        personelUser = resPersonnel.getPersonnelID();
        userDetail = userRepo.findOneByResPersonnel(resPersonnel);
        currentPersonnel = getCurrentUser().getResPersonnel().getPersonnelID();
        currentRole = getCurrentUser().getFirstRole().getRoleName().toLowerCase();
        if (personelUser.equals(currentPersonnel) && currentRole.contains("administrator")) {
            if (resPersonnel.getOfficeSar() == null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan",
                        "User tidak memiliki wilayah Kantor SAR!");
                namaKansar = "";
                disabledKanSar = false;
            } else {
                namaKansar = resPersonnel.getOfficeSar().getKantorsarname();
                disabledKanSar = true;
            }
            disabled = false;
            disabledRole = false;
            if (userDetail == null) {
                headerDetail = "Tambah Data Pengguna";
            } else {
                headerDetail = "Ubah Data Pengguna";
            }
        } else if (currentRole.contains("administrator")) {
            if (resPersonnel.getOfficeSar() == null) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan",
                        "User tidak memiliki wilayah Kantor SAR!");
                namaKansar = "";
                disabledKanSar = false;
            } else {
                namaKansar = resPersonnel.getOfficeSar().getKantorsarname();
                disabledKanSar = true;
            }
            disabled = false;
            disabledRole = false;
            if (userDetail == null) {
                headerDetail = "Tambah Data Pengguna";
            } else {
                headerDetail = "Ubah Data Pengguna";
            }
        } else if (personelUser.equals(currentPersonnel)) {
            System.out.println("masuk ke else if currentPersonel");
            if (resPersonnel.getOfficeSar() == null) {
                namaKansar = "";
            } else {
                namaKansar = resPersonnel.getOfficeSar().getKantorsarname();
            }
            disabled = false;
            disabledRole = true;
            disabledKanSar = true;
            headerDetail = "Ubah Data Pengguna";
        } else {
            System.out.println("masuk ke else sama sekali");
            if (resPersonnel.getOfficeSar() == null) {
                namaKansar = "";
            } else {
                namaKansar = resPersonnel.getOfficeSar().getKantorsarname();
            }
            disabled = true;
            disabledRole = true;
            disabledKanSar = true;
            headerDetail = "Lihat Data Pengguna";
        }

        if (userDetail != null) {
            userName = userDetail.getLogin();

            //roles
            if (isMultipleRoles) {
                List<Role> sourceRoles = new ArrayList<>(availableRoles);
                sourceRoles.removeAll(userDetail.getRoles());
                roles = new DualListModel<>(sourceRoles, userDetail.getRoles());
                selectedRole = null;
            } else {
                roles = null;
                selectedRole = !userDetail.getRoles().isEmpty() ? userDetail.getRoles().get(0) : null;
            }

        } else {
            userDetail = new User();

            //roles
            if (isMultipleRoles) {
                roles = new DualListModel<>(availableRoles, new ArrayList<>());
                selectedRole = null;
            } else {
                roles = null;
                selectedRole = null;
            }
        }
        isShowDetail = false;
        isShowDetailPersonil = false;
        isShowDetailUser = true;
        isGeneratedPassword = false;
        fullNameInput = resPersonnel.getPersonnelName();
        RequestContext.getCurrentInstance().reset("personel-content:data-user");
        RequestContext.getCurrentInstance().update("personel-content:data-user");
    }

    public void showDetail() {
        resPersonnel = (ResPersonnel) getRequestParam("listRow");
        this.isShowDetail = false;
        this.isShowDetailOnTab = false;
        isShowDetailUser = false;
        isShowDetailPersonil = true;
        headerDetail = "Lihat Resource Personil";
        isReadOnlyField = true;
        isPotencyDisabled = true;
        showAllList();
        if (resPersonnel != null) {
            this.isReadOnly = true;
            listPersonelHistory = personelHistoryRepo.findAllResPersonnelHistoryBypersonnel(resPersonnel);
            listPersonnelTraining = personelTrainingRepo.findAllResPersonnelTrainingBypersonnel(resPersonnel);
        }
    }

    public void loadDetail() {
        resPersonnel = (ResPersonnel) getRequestParam("listRow");
        this.isShowDetail = false;
        isShowDetailPersonil = true;
        isShowDetailUser = false;
        isReadOnlyField = false;
        showAllList();
        if (resPersonnel != null) {
            this.isReadOnly = true;
            System.out.println("Ada Isi");
//            if (personnel.getImg() != null) {
//                mediaManager.setMedia(resPersonnel.getImg());
//            } else {
//                mediaManager.setMedia(null);
//            }
            listPersonelHistory = personelHistoryRepo.findAllResPersonnelHistoryBypersonnel(resPersonnel);
            listPersonnelTraining = personelTrainingRepo.findAllResPersonnelTrainingBypersonnel(resPersonnel);
            listImage = resPersonnelImagesRepo.findByPersonnelIDAndDeleted(resPersonnel, false);
            if (listImage == null) {
                listImage = new ArrayList<>();
            }
        } else {
            this.isReadOnly = false;
            this.isShowDetailOnTab = true;
            listPersonelHistory = new ArrayList<>();
            listPersonnelTraining = new ArrayList<>();
            //mediaManager.setMedia(null);
            init();
        }
    }

    public void hapusPersonnel() throws Exception {
        resPersonnel = (ResPersonnel) getRequestParam("listRow");
        resPersonnel.setIsdeleted(BigInteger.ONE);
        personnelRepo.save(resPersonnel);
        afterPropertiesSet();
        addMessage("Sukses", "Personil Berhasil Di Hapus");
    }

    public void hapusSimbol() {
        mediaManager.setMedia(null);
        resPersonnel.setPhotoUrl(null);
        uploadfile = null;
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:imgTampil");
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:btnHapus");
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:imgTampil");
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:btnHapus");
    }

    public void closeSaveInformationDialog() {
        RequestContext.getCurrentInstance().execute("PF('saveNotification').hide()");
        batal();
    }

    public void simpanUser() {
        if (roles != null && roles.getTarget().isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_user_empty_role_selecting"));
            return;
        }

        try {
            // new user and update
            if (userDetail.getUserId() == null) {
                userDetail.setEnabled(true);
                userDetail.setActivated(true);
                ResPersonnel personel = personnelRepo.findByPersonnelID(personelUser);
                userDetail.setResPersonnel(personel);
                userDetail.setLogin(userName);
                userDetail.setDefaultTenant(isMultipleRoles
                        ? roles.getTarget().get(0).getTenant()
                        : selectedRole.getTenant());
            }

            // update user password
            if (passwordInput != null) {
                userDetail.setHashedPassword(passwordEncoder.encode(passwordInput));
            }

            // update user_roles
            if (isMultipleRoles) {
                userService.saveUser(userDetail, fullNameInput,
                        isGeneratedPassword, roles.getTarget());
            } else {
                List<Role> newRoles = new ArrayList<>();
                newRoles.add(selectedRole);
                userService.saveUser(userDetail, fullNameInput,
                        isGeneratedPassword, newRoles);
            }

            // if personnel has not kantor sar
            if (resPersonnel.getOfficeSar() == null) {
                MstKantorSar sar = kantorSarRepo.findByKantorsarname(namaKansar);
                resPersonnel.setOfficeSar(sar);
                personnelRepo.save(resPersonnel);
            }

            RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");

        } catch (DuplicateKeyException dx) {
            if (dx.getMessage().equals("Username already in used.") && dx.getMessage().equals("PersonnelId already in used.")) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Error", "Duplicate Username and PersonnelId");
            }

            if (dx.getMessage().equals("Username already in used.")) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                        getMessageLocale("error_user_duplicate_username"));
            }

            if (dx.getMessage().equals("PersonnelId already in used.")) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, "Error", "Duplicate PersonnelId");
            }
            log.error("Failed save user's detail : ", dx);
        } catch (Exception ex) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                    getMessageLocale("error_user_save"));
            log.error("Failed save user's detail : ", ex);
        }
    }

    public void simpan() {
        System.out.println(">> " + resPersonnel.getPersonnelCode());
        ResPersonnel personnels = this.personnelRepo.findTopOneResPersonnelBypersonnelCode(resPersonnel.getPersonnelCode());
//        if (uploadfile != null) {
//            resPersonnel.setImg(uploadfile.getContents());
//        }
        try {
            if (isReadOnly == false) {
                if (personnels != null) {
                    addPopUpMessage("Peringatan", "Nip Sudah Ada Di Database");
                    return;
                } else {
                    personnelService.savePersonnel(resPersonnel, listPersonelHistory, listPersonnelTraining);
                    ImageFunction();
                    RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");
                }
            } else {
                List<ResPersonnelHistory> lsPersonelHistory = personelHistoryRepo.findAllResPersonnelHistoryBypersonnel(resPersonnel);
                personelHistoryRepo.delete(lsPersonelHistory);
                List<ResPersonnelTraining> lsPersonelTraining = personelTrainingRepo.findAllResPersonnelTrainingBypersonnel(resPersonnel);
                personelTrainingRepo.delete(lsPersonelTraining);
                personnelService.updatePersonnel(resPersonnel, listPersonelHistory, listPersonnelTraining);
                ImageFunction();
                RequestContext.getCurrentInstance().execute("PF('updateNotification').show()");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ImageFunction() {
        String combine = resPersonnel.getPersonnelID();
        combine = combine.replaceAll("\\s+", "_");
        String filePath = "PERSONNEL" + File.separator + combine + File.separator;
        String filePathSource = "TEMP_PERSONNEL" + File.separator + uuid + File.separator;
        File destDir = null;
        File srcDir = null;

        for (ResPersonnelImages personnelImages : listImage) {
            System.out.println("size : " + listImage.size());
            String temp[] = personnelImages.getPathname().split("\\\\");
            personnelImages.setPersonnelID(resPersonnel);
            personnelImages.setPathname(filePath + temp[2]);
            personnelImages.setDeleted(false);
            resPersonnelImagesRepo.save(personnelImages);

            System.out.println("temp = " + temp[2]);
            String source = filePathSource + temp[2];
            System.out.println("source : " + uploadFolder + source);
            srcDir = new File(uploadFolder + source);

            if (personnelImages.getPathname() != null) {
                String tempDest[] = personnelImages.getPathname().split("\\\\");
                String destination = tempDest[0] + File.separator + tempDest[1];
                System.out.println("destination: " + destination);
                destDir = new File(uploadFolder + filePath);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                try {
                    FileUtils.moveFileToDirectory(srcDir, destDir, true);
                    FileUtils.deleteDirectory(new File(uploadFolder + filePathSource));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        //update status isDeleted
        for (ResPersonnelImages personnelImages : dataDeleteImage) {
            personnelImages.setDeleted(true);
            resPersonnelImagesRepo.save(personnelImages);
        }
    }

    public StreamedContent getImagePreview() {
        if (uploadfile != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(uploadfile.getContents()), uploadfile.getContentType());
        } else {
            return new DefaultStreamedContent();
        }
    }

//    public void upload(FileUploadEvent e) throws IOException, SQLException {
//        uploadfile = e.getFile();
//        mediaManager.setMedia(e.getFile().getContents());
//        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));
//
//    }
    public void upload(FileUploadEvent e) {
        synchronized (this) {
            try {
                resPersonnelImages = new ResPersonnelImages();
                System.out.println("---runApplication--" + e.getFile());
                uploadfile = e.getFile();
                String filePath = "TEMP_PERSONNEL" + File.separator + uuid + File.separator;
                System.out.println("Upload Path " + filePath);
                File file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;
                File srcDir = null;

                if (uploadfile != null) {

                    for (ResPersonnelImages images : listImage) {
                        String temp[] = images.getPathname().split("\\\\");
                        String source = filePath + temp[2];
                        System.out.println("sourcessss : " + uploadFolder + source);
                        srcDir = new File(uploadFolder + source);
                        if (isReadOnly == true) {
                            images.setDeleted(true);
//                            resPersonnelImagesRepo.save(images);
                            dataDeleteImage.add(images);
                        }
                        srcDir.delete();
                        listImage = new ArrayList<>();
                    }
                    bytes = uploadfile.getContents();
                    String filename = FilenameUtils.getName(uploadfile.getFileName());
                    filename = filename.replaceAll("\\s+", "_");
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(uploadFolder + filePath + filename)));
                    stream.write(bytes);
                    stream.close();

                    resPersonnelImages.setPathname(filePath + filename);
                    listImage.add(resPersonnelImages);
                    System.out.println("Path : " + resPersonnelImages.getPathname());
                }
                //RequestContext.getCurrentInstance().update("incidentdetail:tablog-content:listImg");
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Your Document (File Name " + uploadfile.getFileName() + " with size " + uploadfile.getSize() + ")  Uploaded Successfully", ""));

            }
        }
    }

    public void hapusImage(ResPersonnelImages images) {
        listImage.remove(images);
        dataDeleteImage.add(images);
        addMessage("Sukses", "Image berhasil dihapus");
    }

    public void batal() {
        this.isShowDetail = true;
        isShowDetailPersonil = false;
        isShowDetailUser = false;
        init();
        mediaManager.setMedia(null);
    }

    public void addPersonelTraining() throws ParseException {
        if (StringUtils.isBlank(personnelTraining.getTrainingDate())) {
            addPopUpMessage("Peringatan", "Tanggal training mohon diisi");
            return;
        }
        SimpleDateFormat readFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = readFormat.parse(personnelTraining.getTrainingDate());
        SimpleDateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
        personnelTraining.setTrainingDate(writeFormat.format(date));
        if (personnelTraining.getPersonnelTrainingID() == null) {
            personnelTraining.setPersonnel(resPersonnel);
            personnelTraining.setPersonnelTrainingID("tmp");
            listPersonnelTraining.add(personnelTraining);

        } else {
            int index = listPersonnelTraining.indexOf(personnelTraining);
            if (index != -1) {
                listPersonnelTraining.set(index, personnelTraining);
            }
        }
//        personnelTraining.setPersonnel(resPersonnel);
//        listPersonnelTraining.add(personnelTraining);
        personnelTraining = new ResPersonnelTraining();
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-sertifikasi-sar");
    }

    public void removePersonelTraining() {
        Integer index = (Integer) getRequestParam("rowIndex");
        listPersonnelTraining.remove(listPersonnelTraining.get(index));
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-sertifikasi-sar");
    }

    public void editPersonelTraining() {
        Integer index = (Integer) getRequestParam("rowIndex");
        personnelTraining = listPersonnelTraining.get(index);
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-sertifikasi-sar");
    }

    public void addPersonelhistory() {
        if (StringUtils.isBlank(personnelHistory.getIncidentYear())
                || StringUtils.isBlank(personnelHistory.getIncidentName())) {
            addPopUpMessage("Peringatan", "Bulan/Tahun dan Nama mohon diisi");
            return;
        }
        if (personnelHistory.getPersonnelHistoryID() == null) {
            System.out.println("Insert Personel History");
            personnelHistory.setPersonnelHistoryID("tmp");
            listPersonelHistory.add(personnelHistory);

        } else {
            int index = listPersonelHistory.indexOf(personnelHistory);
            if (index != -1) {
                listPersonelHistory.set(index, personnelHistory);
            }

        }
//        listPersonelHistory.add(personnelHistory);
        personnelHistory = new ResPersonnelHistory();
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-riwayat-sar");
    }

    public void editPersonelhistory() {
        Integer index = (Integer) getRequestParam("rowIndex");
        personnelHistory = listPersonelHistory.get(index);
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-riwayat-sar");
    }

    public void removePersonelhistory() {
        Integer index = (Integer) getRequestParam("rowIndex");
        listPersonelHistory.remove(listPersonelHistory.get(index));
        RequestContext.getCurrentInstance().update("personel-content:personel-detail:personel-tabs:personel-riwayat-sar");
    }

    public void onChangePosSar() {
        this.listPostSar = posSarRepo.findAllMstPosSarBykantorsarid(resPersonnel.getOfficeSar());
    }

}
