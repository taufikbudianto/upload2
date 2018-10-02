package prosia.basarnas.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.model.ResAssetImages;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.repo.MstAssetTypeRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.repo.ResAssetImagesRepo;
import prosia.basarnas.repo.ResPeralatanRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.service.AssetTypeService;
import prosia.basarnas.service.OfficeSarService;
import prosia.basarnas.web.util.Coordinate;
import prosia.basarnas.web.util.LatitudeLongitude;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author TOMY
 */
@Component
@Scope("view")
public @Data
class ResPeralatanMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private ResAsset ResAsset;
    private ResAssetImages resAssetImages;
    private ResAssetContact resAssetContact;
    private ResPotency resPotency;
    private ResPotencyContact potencyContact;
    private MstKantorSar mstKantorSar;
    private MstAssetType mstAssetType;
    private UploadedFile uploadFile;
    private File file;
    private File srcDir;
    private File destDir;
    private LazyDataModelJPA<ResAsset> lazyDataModelJPA;
    private LazyDataModelJPA<ResAssetContact> lazyDataModelDetailJPA;
    private LazyDataModelJPA<ResPotency> lazyDataModelPotencyJPA;
    private LazyDataModelJPA<ResPotencyContact> lazyDataModelPotencyContactJPA;
    private LazyDataModelJPA<ResAsset> lazyDataModeltabSARJPA;
    @Autowired
    private ResPeralatanRepo peralatanRepo;
    @Autowired
    private ResAssetImagesRepo resAssetImagesRepo;
    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    @Autowired
    private ResAssetContactRepo assetContactRepo;
    @Autowired
    private MstAssetTypeRepo assetTypeRepo;
    @Autowired
    private ResPotencyRepo potencyRepo;
    @Autowired
    private ResPotencyContactRepo potencyContactRepo;
    private List<SelectItem> listKantorSar;
    private List<SelectItem> listAssetType;
    private List<ResAssetContact> assetContacts;
    private List<ResAssetImages> listResAssetImages;
    private List<ResAssetImages> dataDeleteImages;
    private List<ResAssetImages> dataDeleteImagesTemp;
    @Value("${uploadFolder}")
    private String uploadFolder;
    private String filePath;
    private String filePathSource;
    private String fileName;
    private String source;
    private String destination;
    private String temp[];
    private String tempDest[];
    private String uuid;
    private String currentId;
    private String field;
    private String value;
    private String field1;
    private String value1;
    private String longitude;
    private String latitude;
    private String kantorsarname;
    private String name;
    private String asset;
    private String assetname;
    private String vehicletype;
    private Integer status;
    private String potencyname;
    //resource contact
    private String contacttitle;
    private String contactname;
    private String contactsuffix;
    private String contactposition;
    private String phonenumber1;
    private String phonenumber2;
    private String email;
    //respotency
    private String namapotensi;
    private String alamat;
    private String lintang;
    private String bujur;
    private BigInteger matra;
    private String kantorsar;
    private String prov;
    private String kota;
    private String notel1;
    private String notel2;
    private String notel3;
    private String nofax;
    private String freqrad;
    private String emailpotency;
    private String jejaring;
    private Boolean showButton;
    private Boolean showButtonContact;
    private Boolean showButtonPotency;
    private Boolean disabled;
    private Boolean disabledContact;
    private Boolean disabledPotency;
    private Boolean disabledProfil;
    private int showCombo;
    private Boolean showButtonInput;
    private int idtemp;
    @Autowired
    private OfficeSarService officeSarService;
    @Autowired
    private AssetTypeService AssetTypeService;
    @Autowired
    private MenuNavMBean menuNavMBean;

    private Boolean isShowDetailInput;
    private Boolean isShowDetail;

    private Boolean basarnas = true;
    private Boolean potensi;
    private Integer potensiint;
    private Integer basarnasint;

    private String idContactAsset;
    List<String> dataDeleteContact = new ArrayList<>();

    //DEDI - 28 Sept 2017
    private Integer operationaltype;
    private Map<String, Integer> listOperationalType;
    //END
    private Coordinate coordinateLatitude;
    private Coordinate coordinateLongitude;
    private Coordinate coordinateLatitudePotensi;
    private Coordinate coordinateLongitudePotensi;

    @Override
    public void afterPropertiesSet() throws Exception {
        idtemp = 0;
        lazyDataModelJPA = new LazyDataModelJPA(peralatanRepo) {
            @Override
            protected long getDataSize() {
                return peralatanRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return peralatanRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public void reset(ResAsset asset, boolean isReadOnly) {
        disabled = false;
        initCoordinate();
        initCoordinatePotensi();
        showButton = true;
        isShowDetailInput = true;
        isShowDetail = false;
        assetContacts = new ArrayList<>();
        resAssetImages = new ResAssetImages();
        listResAssetImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        ResAsset = new ResAsset();
        assetname = null;
        operationaltype = null;
        potencyname = null;
        kantorsarname = null;
        RequestContext.getCurrentInstance().reset("peralatan-content:form-resPeralatanInput");
        RequestContext.getCurrentInstance().update("peralatan-content:form-resPeralatanInput");
    }

    private Specification<ResAsset> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<ResAsset>() {

            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "assettypeid":
                            predicates.add(cb.like(cb.lower(root.<MstAssetType>get("assettypeid").<String>get("assetname")),
                                    getLikePattern(value.trim())));
                            break;
                        //return orTogether(predicates, cb);

                        case "potencyid":
                            predicates.add(
                                    cb.like(cb.lower(root.<ResPotency>get("potencyid")
                                            .<String>get("potencyname")),
                                            getLikePattern(value.trim())));
                            break;
                        //return orTogether(predicates, cb);

                        case "status":
                            predicates.add(cb.equal(root.<Integer>get(field), value));
                            break;

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
//                if (basarnas == true && potensi == false) {
//                    predicates.add(cb.equal(root.<MstAssetType>get("assettypeid")
//                            .<Integer>get("issru"), 2));
//                    predicates.add(cb.isNotNull(root.<ResAsset>get("kantorsarid")
//                            .<MstKantorSar>get("kantorsarname")));
//                    predicates.add(cb.isNotNull(root.<String>get("code")));
//                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
//                   
//                }else if (potensi == true && basarnas == false) {
//                    //predicates.add(cb.isNotNull(root.<Integer>get("op_type")));
//                    predicates.add(cb.isNotNull(root.<ResPotency>get("potencyid")
//                            .<Integer>get("potencyname")));
//                    predicates.add(cb.notEqual(root.<MstAssetType>get("assettypeid")
//                            .<Integer>get("issru"), 2));
//                } else {
//                    if (basarnas == true && potensi == false) {
//                        predicates.add(cb.equal(root.<MstAssetType>get("assettypeid")
//                                .<Integer>get("issru"), 2));
//                        predicates.add(cb.isNotNull(root.<ResAsset>get("kantorsarid")
//                                .<MstKantorSar>get("kantorsarname")));
//                        predicates.add(cb.isNotNull(root.<String>get("code")));
//                    }
//                    if (potensi == true && basarnas == false) {
//                        //predicates.add(cb.isNotNull(root.<Integer>get("op_type")));
//                        predicates.add(cb.isNotNull(root.<ResPotency>get("potencyid")
//                                .<Integer>get("potencyname")));
//                        predicates.add(cb.notEqual(root.<MstAssetType>get("assettypeid")
//                                .<Integer>get("issru"), 2));
//                    }
//                   
//                }

                if (basarnas == true && potensi == true) {
                } else if (basarnas == true && potensi == false) {
                    predicates.add(cb.equal(root.<MstAssetType>get("assettypeid")
                            .<Integer>get("issru"), 2));
                    predicates.add(cb.isNotNull(root.<String>get("kantorsarid")));
                    predicates.add(cb.isNull(root.<String>get("potencyid")));
                    //predicates.add(cb.isNotNull(root.<String>get("code")));
                } else if (potensi == true && basarnas == false) {
                    //predicates.add(cb.isNotNull(root.<Integer>get("op_type")));
                    //predicates.add(cb.isNotNull(root.<String>get("potencyid")));
                    predicates.add(cb.notEqual(root.<MstAssetType>get("assettypeid")
                            .<Integer>get("issru"), 2));
                    predicates.add(cb.isNull(root.<String>get("kantorsarid")));
                    predicates.add(cb.isNotNull(root.<String>get("potencyid")));
                } else {
                    predicates.add(cb.notEqual(root.<MstAssetType>get("assettypeid")
                            .<Integer>get("issru"), 2));
                    predicates.add(cb.isNull(root.<String>get("kantorsarid")));
                    predicates.add(cb.isNull(root.<String>get("potencyid")));
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

    public void showListPotency() {
        System.out.println("Show list potency");
        potency();
        RequestContext.getCurrentInstance().reset("idCariPotensi");
        RequestContext.getCurrentInstance().update("idCariPotensi");
        RequestContext.getCurrentInstance().execute("PF('widgetCariPotensi').show()");
    }

    public void contact(String assetid) {
        lazyDataModelDetailJPA = new LazyDataModelJPA(assetContactRepo) {
            @Override
            protected long getDataSize() {
                return assetContactRepo.count(whereQueryContact(assetid));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return assetContactRepo.findAll(whereQueryContact(assetid), request);
            }
        };
    }

    public void tabContact(String potencyid) {
        lazyDataModelPotencyContactJPA = new LazyDataModelJPA(potencyContactRepo) {
            @Override
            protected long getDataSize() {
                return potencyContactRepo.count(whereQuerypotencyContact(potencyid));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return potencyContactRepo.findAll(whereQuerypotencyContact(potencyid), request);
            }
        };
    }

    private Specification<ResPotencyContact> whereQuerypotencyContact(final String potencyid) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResPotencyContact>() {

            @Override
            public Predicate toPredicate(Root<ResPotencyContact> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<ResPotency>get("potencyid").<String>get("potencyid"), potencyid));
                return andTogether(predicates, cb);
            }
        };
    }

    public void tabSAR(String potencyid) {
        lazyDataModeltabSARJPA = new LazyDataModelJPA(peralatanRepo) {
            @Override
            protected long getDataSize() {
                return peralatanRepo.count(whereQuerytabSAR(potencyid));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return peralatanRepo.findAll(whereQuerytabSAR(potencyid), request);
            }
        };
    }

    private Specification<ResAsset> whereQuerytabSAR(final String potencyid) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAsset>() {

            @Override
            public Predicate toPredicate(Root<ResAsset> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<ResPotency>get("potencyid").<String>get("potencyid"), potencyid));
                return andTogether(predicates, cb);
            }
        };
    }

    public ResPeralatanMBean() {
        ResAsset = new ResAsset();
        resAssetImages = new ResAssetImages();
        resAssetContact = new ResAssetContact();
        resPotency = new ResPotency();
        listResAssetImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        uuid = UUID.randomUUID().toString();
        showButton = true;
        showButtonContact = true;
        showButtonPotency = true;
        disabled = false;
        disabledContact = false;
        disabledPotency = false;
        disabledProfil = true;
        showButtonInput = true;
        potensi = false;
        //DEDI - 28 Sept 2017
        listOperationalType = new LinkedHashMap<String, Integer>();

        listOperationalType.put("Darat", 0);
        listOperationalType.put("Udara", 1);
        listOperationalType.put("Laut", 2);
        //END
        field = "";
        value = "";
        potencyname = "";
    }

    private Specification<ResAssetContact> whereQueryContact(final String assetid) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ResAssetContact>() {

            @Override
            public Predicate toPredicate(Root<ResAssetContact> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                predicates.add(cb.equal(root.<ResAsset>get("assetid").<String>get("assetid"), assetid));
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<ResPotency> whereQueryPotency(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<ResPotency>() {

            @Override
            public Predicate toPredicate(Root<ResPotency> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(
                            cb.like(cb.lower(root.<String>get(field)), getLikePattern(value.trim()))
                    );
                }
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
                //predicates.add(cb.notEqual(root.<String>get("kantorsarid"), null));
                return andTogether(predicates, cb);
            }

        };

    }

    public void potency() {
        lazyDataModelPotencyJPA = new LazyDataModelJPA(potencyRepo) {
            @Override
            protected long getDataSize() {
                return potencyRepo.count(whereQueryPotency(field1, value1));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return potencyRepo.findAll(whereQueryPotency(field1, value1), request);
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

    public String getKantorSarByKantorSarId(String kantorSarId) {
        return kantorSarRepo.findOne(kantorSarId).getKantorsarname();
    }

    public String getAssetTypeByAssetId(String AssetId) {
        return assetTypeRepo.findOne(AssetId).getAssettypeid();
    }

    public void chengeToCombo() {
        value = "";
        if (field.equals("status") == true) {
            showCombo = 1;
        } else if (field.equals("kantorsarid") == true) {
            showCombo = 2;
        } else {
            showCombo = 0;
        }
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

    public List<SelectItem> getListAssetType() {
        if (listAssetType == null) {
            listAssetType = new ArrayList<>();
            assetTypeRepo.findAllByIssru(2).stream().forEach((AssetType) -> {
                listAssetType.add(new SelectItem(AssetType.getAssetname(), AssetType.getAssetname()));
            });
        }
        return listAssetType;
    }

    public void hideForm() {
        resAssetContact = new ResAssetContact();
        resPotency = new ResPotency();
        resAssetImages = new ResAssetImages();
        listResAssetImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        isShowDetailInput = false;
        isShowDetail = false;
    }

    public void pilih() {
        resPotency = (ResPotency) getRequestParam("potensiPeralatan");
        potencyname = resPotency.getPotencyname();
        field = "code";
        RequestContext.getCurrentInstance().update("peralatan-content:form-resPeralatan:idPanelPeralatanRight");
        RequestContext.getCurrentInstance().update("peralatan-content:form-resPeralatanInput:idPanelPeralatanInputRight");
        RequestContext.getCurrentInstance().execute("PF('widgetCariPotensi').hide()");
    }

    public void viewCheck(ResAsset assetid, Boolean readOnly) {
        assetContacts = assetContactRepo.findAllByAssetid(assetid);
        if (assetContacts == null) {
            assetContacts = new ArrayList<>();
        }

        this.ResAsset = assetid;
        //name = checklist.getName();
        if (ResAsset.getKantorsarid() != null) {
            kantorsarname = ResAsset.getKantorsarid().getKantorsarname();
        } else {
            kantorsarname = "";
        }

        if (ResAsset.getAssettypeid() != null) {
            assetname = ResAsset.getAssettypeid().getAssetname();
        } else {
            assetname = "";
        }

        if (ResAsset.getPotencyid() != null) {
            potencyname = ResAsset.getPotencyid().getPotencyname();
        } else {
            potencyname = "";
        }

        vehicletype = ResAsset.getVehicletype();
        listResAssetImages = resAssetImagesRepo.findByResAssetAndDeleted(ResAsset, false);
        initCoordinate();
        viewCoordinate();
        initCoordinatePotensi();

        disabled = readOnly;
        showButton = !readOnly;

        isShowDetailInput = false;
        isShowDetail = true;

        operationaltype = ResAsset.getOp_type();

        RequestContext.getCurrentInstance().reset("peralatan-content:form-resPeralatan");
        RequestContext.getCurrentInstance().update("peralatan-content:form-resPeralatan");
    }

    public void viewTabProfil(String potencyid) {
        this.resPotency = potencyRepo.findOne(potencyid);
        kantorsar = resPotency.getKantorsarid().getKantorsarname();
        matra = resPotency.getPotencytype();
        namapotensi = resPotency.getPotencyname();
        alamat = resPotency.getAddress();
        initCoordinatePotensi();
        viewCoordinatePotensi();
        notel1 = resPotency.getPhonenumber1();
        notel2 = resPotency.getPhonenumber2();
        notel3 = resPotency.getPhonenumber3();
        nofax = resPotency.getFaxnumber();
        freqrad = resPotency.getRadiofrequency();
        emailpotency = resPotency.getEmail();
        jejaring = resPotency.getSocialnetwork();
        prov = resPotency.getRegionid().getName();
        if (resPotency.getAreacodeid() == null) {
            kota = "";
        } else {
            kota = resPotency.getAreacodeid().getArea();
        }
    }

    public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }

    public void viewTab(String potencyid) {
        System.out.println("potensi :" + potencyid);
        tabContact(potencyid);
        tabSAR(potencyid);
        viewTabProfil(potencyid);
        RequestContext.getCurrentInstance().execute("PF('widgetTab').show()");
    }

    public String formatAssetId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String assetId = "";

        List<ResAsset> lis = peralatanRepo.findAllByAssetidContaining("CGK");
        if (lis.isEmpty()) {
            //System.out.println("A");
            assetId = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxassetId = peralatanRepo.findAssetByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
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

    public String formatContactId() {
        //SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        //String fromDate = sdfToStr.format(LocalDate.now());
        String nextval = "";
        String assetcontactId = "";

        List<ResAssetContact> list = assetContactRepo.findAllByAssetcontactidContaining("CGK");
        if (list.isEmpty()) {
            //System.out.println("A");
            assetcontactId = "CGK" + "-" + ProsiaConstant.SEQUENCE1_000000001;
        } else {
            //for (Incident i : lis) {
            //System.out.println("B");
            String maxassetcontactId = assetContactRepo.findAssetContactByMaxId("CGK");
            //String[] splitId = i.getIncidentid().split("-");
            String[] splitId = maxassetcontactId.split("-");
            System.out.println("splitid[0] = " + splitId[0]);
            System.out.println("splitid[1] = " + splitId[1]);
            int next = Integer.valueOf(splitId[1]) + 1;
            int length = String.valueOf(next).length();
            System.out.println("next = " + next);
            System.out.println("\n length = " + length);
            switch (length) {
                case 1:
                    nextval = ProsiaConstant.SEQUENCE1_00000000 + next;
                    break;
                case 2:
                    nextval = ProsiaConstant.SEQUENCE1_0000000 + next;
                    break;
                case 3:
                    nextval = ProsiaConstant.SEQUENCE1_00000 + next;
                    break;
                case 4:
                    nextval = ProsiaConstant.SEQUENCE1_0000 + next;
                    break;
                case 5:
                    nextval = ProsiaConstant.SEQUENCE1_000 + next;
                    break;
                case 6:
                    nextval = ProsiaConstant.SEQUENCE1_00 + next;
                    break;
                case 7:
                    nextval = ProsiaConstant.SEQUENCE1_0 + next;
                    break;
                case 8:
                    nextval = "" + next;
                    break;
                default:
                    break;
            }
            assetcontactId = "CGK" + "-" + nextval;

            //}
        }
        return assetcontactId;
    }

    public void hapus(ResAsset i) {
        logger.debug("Resource Peralatan : {}", i);
        i.setIsdeleted(BigInteger.ONE);
        peralatanRepo.save(i);
        /*
        If the main ResAssetPeralatan is deleted, automatically resAssetImages is setDeleted(true)
        This code just for MULTIPLE PHOTO
         */
//        listResAssetImages = resAssetImagesRepo.findByResAssetAndDeleted(i, false);
//        for (ResAssetImages resAssetImage : listResAssetImages) {
//            resAssetImage.setDeleted(true);
//            resAssetImagesRepo.save(resAssetImage);
//        }
        /*
        If the main ResAssetPeralatan is deleted, automatically resAssetImages is setDeleted(true)
        This code just for SINGLE PHOTO
         */
//        resAssetImages = resAssetImagesRepo.findAllByResAssetAndDeleted(i, false);
//        resAssetImages.setDeleted(true);
//        resAssetImagesRepo.save(resAssetImages);
        addMessage("Sukses", "Resource Sarana berhasil dihapus");
    }

    public void hapusImage(ResAssetImages images) {
        listResAssetImages.remove(images);
        dataDeleteImages.add(images);
        //addPopUpMessage("Informasi", "Image berhasil dihapus!");
    }

    public void hapusContact(String id) {
        assetContactRepo.delete(id);
        addMessage("Sukses", "Kontak resource berhasil dihapus");
    }

    public void uploadImage(FileUploadEvent e) {
        synchronized (this) {
            try {
                resAssetImages = new ResAssetImages();
                uploadFile = e.getFile();
                filePath = "TEMP_RESPERALATAN" + File.separator + uuid + File.separator;
                file = new File(uploadFolder + filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] bytes = null;
                srcDir = null;

                if (uploadFile != null) {
                    for (int i = 0; i < listResAssetImages.size(); i++) {
                        temp = listResAssetImages.get(i).getPathname().split("\\\\");
                        source = filePath + temp[2];
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        srcDir = new File(uploadFolder + source);
                        if ((uploadFolder + source).equals(name)) {
                            listResAssetImages.remove(i);
                        }
                    }

                    //to upload single photo
//                    for (ResAssetImages oldImages : listResAssetImages) {
//                        temp = oldImages.getPathname().split("\\\\");
//                        source = filePath + temp[2];
//                        srcDir = new File(uploadFolder + source);
//                        
//                        if (isReadOnly == false) {
//                            if (filePath.contains("TEMP")) {
//                                oldImages.setDeleted(true);
//                                dataDeleteImagesTemp.add(oldImages);
//                            }
//                            oldImages.setDeleted(true);
//                            dataDeleteImages.add(oldImages);
//                        }                        
//                        srcDir.delete();
//                        listResAssetImages = new ArrayList<>();
//                    }
                    bytes = uploadFile.getContents();
                    fileName = FilenameUtils.getName(uploadFile.getFileName());
                    fileName = fileName.replaceAll("\\s+", "_");
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(uploadFolder + filePath + fileName)));
                    stream.write(bytes);
                    stream.close();

                    resAssetImages.setPathname(filePath + fileName);
                    listResAssetImages.add(resAssetImages);
                }
                //RequestContext.getCurrentInstance().update("sarana-content:form-resSaranaInput:imgResSaranaInput");
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            } catch (Exception ex) {
                ex.printStackTrace();
                //FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Document (File Name " + uploadFile.getFileName() + " with size " + uploadFile.getSize() + ") uploaded successfully!", ""));
            }
        }
    }

    public void imageFunction() {
        String combine = ResAsset.getAssetid();
        combine = combine.replaceAll("\\s+", "_");
        filePathSource = "TEMP_RESPERALATAN" + File.separator + uuid + File.separator;
        filePath = "RESPERALATAN" + File.separator + combine + File.separator;
        srcDir = null;
        destDir = null;
        File destDirName = null;

        for (ResAssetImages images : listResAssetImages) {
            temp = images.getPathname().split("\\\\");
            images.setResAsset(ResAsset);
            images.setPathname(filePath + temp[2]);
            images.setDeleted(false);

            resAssetImagesRepo.save(images);

            source = filePathSource + temp[2];
            srcDir = new File(uploadFolder + source);

            if (images.getPathname() != null) {
                tempDest = images.getPathname().split("\\\\");
                destination = tempDest[0] + File.separator + tempDest[1];
                destDir = new File(uploadFolder + filePath);
                destDirName = new File(uploadFolder + filePath + tempDest[2]);

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                try {
                    for (int i = 0; i < listResAssetImages.size(); i++) {
                        String name = uploadFolder + filePath + FilenameUtils.getName(uploadFile.getFileName());
                        if ((uploadFolder + filePath + tempDest[2]).equals(name)) {
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
        for (ResAssetImages resAssetImage : dataDeleteImages) {
            resAssetImage.setDeleted(true);
            resAssetImagesRepo.save(resAssetImage);
        }
    }

    public void simpan() {
        if (ResAsset.getAssetid() == null) {
            ResAsset.setAssetid(formatAssetId());
        }
        setCoordinate();
        ResAsset.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
        ResAsset.setUsersiteid(ProsiaConstant.SITES);
        ResAsset.setDatecreated(new Date());
        ResAsset.setIsdeleted(BigInteger.ZERO);
        MstAssetType assettypename = assetTypeRepo.findByAssetname(assetname);
        System.out.println("id :" + assettypename);
        ResAsset.setAssettypeid(assettypename);
        System.out.println("kansar : " + kantorsarname);
        MstKantorSar sar = kantorSarRepo.findByKantorsarname(kantorsarname);
        System.out.println("sar = " + sar);
        ResAsset.setKantorsarid(sar);

        System.out.println("respotensi =" + resPotency.getPotencyid());
        if (resPotency.getPotencyid() != null) {
            ResAsset.setPotencyid(resPotency);
        }
        ResAsset.setOp_type(operationaltype);
        ResAsset.setVehicletype(vehicletype);
        peralatanRepo.save(ResAsset);

        for (ResAssetContact contact : assetContacts) {
            //System.out.println("panjang= " +contact.getAssetcontactid().length());
            if (contact.getAssetcontactid() == null || contact.getAssetcontactid().length() < 5) {
                contact.setAssetcontactid(formatContactId());
            }
            contact.setAssetid(ResAsset);
            assetContactRepo.save(contact);
        }
        String[] arrayData = dataDeleteContact.toArray(new String[0]);
        for (int i = 0; i < arrayData.length; i++) {
            assetContactRepo.delete(arrayData[i]);

        }
        imageFunction();

        //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
        addMessage("Sukses", "Data resource peralatan berhasil disimpan");
        refresh();
        isShowDetailInput = false;
        isShowDetail = false;
        dataDeleteContact.clear();
    }

    public void simpanContact() {
        if (resAssetContact.getAssetcontactid() == null) {
            resAssetContact.setAssetcontactid(formatContactId());
        }
        resAssetContact.setAssetid(ResAsset);

        assetContactRepo.save(resAssetContact);

        //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
        addMessage("Sukses", "Data kontak aset berhasil disimpan");
        //srefresh();

        RequestContext.getCurrentInstance().execute("PF('widgetResContactInput').hide()");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void batal() {
        refresh();
    }

    public void refresh() {
        longitude = new String();
        latitude = new String();
        kantorsarname = new String();
        name = new String();
        asset = new String();
        assetname = new String();
        vehicletype = new String();
        potencyname = new String();
        //resource contact
        contacttitle = new String();
        contactname = new String();
        contactsuffix = new String();
        contactposition = new String();
        phonenumber1 = new String();
        phonenumber2 = new String();
        email = new String();
        ResAsset = new ResAsset();
        resAssetContact = new ResAssetContact();
        assetContacts = new ArrayList<>();
        resAssetImages = new ResAssetImages();
        listResAssetImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
    }

    public void reset() {
        disabled = false;
        initCoordinate();
        showButton = true;
        isShowDetailInput = true;
        isShowDetail = false;
        assetContacts = new ArrayList<>();
        ResAsset = new ResAsset();
        resAssetImages = new ResAssetImages();
        listResAssetImages = new ArrayList<>();
        dataDeleteImages = new ArrayList<>();
        dataDeleteImagesTemp = new ArrayList<>();
        assetname = null;
        operationaltype = null;
        potencyname = null;
        kantorsarname = null;
        assetContacts = new ArrayList<>();
    }

    public void resetContactInput() {
        disabledContact = false;
        resAssetContact = new ResAssetContact();
        showButtonContact = true;
        RequestContext.getCurrentInstance().reset("idResContactInputPeralatan");
        RequestContext.getCurrentInstance().update("idResContactInputPeralatan");
        RequestContext.getCurrentInstance().execute("PF('widgetResContactInputPeralatan').show()");
    }

    public void viewCheckContactTemp(ResAssetContact contact, Boolean edited) {
        resAssetContact = contact;
        disabledContact = edited;
        showButtonContact = !edited;

        RequestContext.getCurrentInstance().reset("idResContactInputPeralatan");
        RequestContext.getCurrentInstance().update("idResContactInputPeralatan");
        RequestContext.getCurrentInstance().execute("PF('widgetResContactInputPeralatan').show()");
    }

    public void hapusContactTemp() {
        int index = assetContacts.indexOf(resAssetContact);
        if (index != -1) {
            assetContacts.remove(index);
            addMessage("Sukses", "Kontak resource berhasil dihapus");
        }
    }

    public void hapusContactTempPeralatan(ResAssetContact contact) {
        int index = assetContacts.indexOf(contact);
        if (index != -1) {
            assetContacts.remove(index);
            idContactAsset = null;
            idContactAsset = contact.getAssetcontactid();
            dataDeleteContact.add(idContactAsset);
            addMessage("Sukses", "Kontak resource berhasil dihapus");
        } else {
            resAssetContact = contact;
            assetContactRepo.delete(contact.getAssetcontactid());
//          RequestContext.getCurrentInstance().update("idResPrasarana");
            addMessage("Sukses", "Kontak resource berhasil dihapus");
        }
    }
    //resContactInput

    public String createidtemp() {
        idtemp = idtemp + 1;
        String idtempstr = String.valueOf(idtemp);
        System.out.println("IDTEMPSTR : " + idtempstr);
        return idtempstr;
    }

    public void add() {
        if (resAssetContact.getAssetcontactid() == null) {
            resAssetContact.setAssetcontactid(createidtemp());
            System.out.println("hasil resAssetContact :" + resAssetContact);
            assetContacts.add(resAssetContact);
        } else {
            int index = assetContacts.indexOf(resAssetContact);
            if (index != -1) {
                assetContacts.set(index, resAssetContact);
            }
        }

        System.out.println("hasil assetContact :" + assetContacts);

        RequestContext.getCurrentInstance().execute("PF('widgetResContactInputPeralatan').hide()");

    }

    public void hapusContactTemp(ResAssetContact i) {
        int index = assetContacts.indexOf(i);
        if (index != -1) {
            assetContacts.remove(index);
            addMessage("Sukses", "Kontak resource berhasil dihapus");
        }
    }

    public void checkPotensi() {
        String summary = potensi ? "Checked" : "Unchecked";

        System.out.println("summary : " + summary);
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

    public void resetsearch() {
        field = "code";
        value = "";
        showCombo = 0;
    }

    public void initCoordinate() {
        coordinateLatitude = new Coordinate();
        coordinateLongitude = new Coordinate();
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
    }

    public void initCoordinatePotensi() {
        coordinateLatitudePotensi = new Coordinate();
        coordinateLongitudePotensi = new Coordinate();
        coordinateLatitudePotensi.setType(true);
        coordinateLongitudePotensi.setType(false);
    }

    public void setCoordinate() {
        coordinateLatitude.setType(true);
        coordinateLongitude.setType(false);
        coordinateLatitude.converter();
        coordinateLongitude.converter();
        ResAsset.setLatitude(coordinateLatitude.getValidDecimalDegrees() != null
                ? coordinateLatitude.getValidDecimalDegrees().toString() : null);
        ResAsset.setLongitude(coordinateLongitude.getValidDecimalDegrees() != null
                ? coordinateLongitude.getValidDecimalDegrees().toString() : null);
    }

    public void viewCoordinate() {
        if (ResAsset.getLatitude() != null
                || StringUtils.isNotBlank(ResAsset.getLatitude())) {
            coordinateLatitude.setType(true);
            coordinateLatitude.setCounter(1);
            Double latitude = Double.parseDouble(ResAsset.getLatitude());
            coordinateLatitude.setDecimalDegrees(latitude);
            coordinateLatitude.converter();
            coordinateLatitude.setCounter(3);
        }
        if (ResAsset.getLongitude() != null
                || StringUtils.isNotBlank(ResAsset.getLongitude())) {
            coordinateLongitude.setType(false);
            coordinateLongitude.setCounter(1);
            Double longitude = Double.parseDouble(ResAsset.getLongitude());
            coordinateLongitude.setDecimalDegrees(longitude);
            coordinateLongitude.converter();
            coordinateLongitude.setCounter(3);
        }
    }

    public void viewCoordinatePotensi() {
        if (resPotency.getLatitude() != null
                || StringUtils.isNotBlank(resPotency.getLatitude())) {
            coordinateLatitudePotensi.setType(true);
            coordinateLatitudePotensi.setCounter(1);
            Double latitude = Double.parseDouble(resPotency.getLatitude());
            coordinateLatitudePotensi.setGaris(latitude < 0 ? 1 : 0);
            coordinateLatitudePotensi.setDecimalDegrees(Math.abs(latitude));
            coordinateLatitudePotensi.converter();
            coordinateLatitudePotensi.setCounter(3);
        }
        if (resPotency.getLongitude() != null
                || StringUtils.isNotBlank(resPotency.getLongitude())) {
            coordinateLongitudePotensi.setType(false);
            coordinateLongitudePotensi.setCounter(1);
            Double longitude = Double.parseDouble(resPotency.getLongitude());
            coordinateLongitudePotensi.setGaris(longitude < 0 ? 1 : 0);
            coordinateLongitudePotensi.setDecimalDegrees(Math.abs(longitude));
            coordinateLongitudePotensi.converter();
            coordinateLongitudePotensi.setCounter(3);
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

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
