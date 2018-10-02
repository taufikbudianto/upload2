/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import static com.sun.javafx.logging.PulseLogger.addMessage;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static javafx.scene.input.KeyCode.T;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.controller.MenuNavMBean;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwGroup;
import prosia.basarnas.model.GtwGroupDetail;
import prosia.basarnas.model.GtwSmsCategory;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.model.TaskEmail;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwGroupDetailRepo;
import prosia.basarnas.repo.GtwGroupRepo;
import prosia.basarnas.repo.GtwSmsCategoryRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.TaskEmailRepo;
import prosia.basarnas.temp_model.SmsEmail;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class SmsOutMBean extends AbstractManagedBean implements InitializingBean {

    private String tipe;
    private String kolompencarian;
    private Map<String, String> listKolomPencarian;
    private Map<String, String> listDropDown;
    private Map<String, String> listTipe;

    private Map<String, Map<String, String>> data = new HashMap<>();
    private String textfield;
    private String dropdown;
    private Boolean isText;
    private Boolean isDropDown;

    private Boolean isSMS;
    private Boolean isEmail;

    private Boolean terkirim = false;
    private Boolean pending = false;
    private Boolean tidakterkirim = false;

    private Integer terkirimint;
    private Integer pendingint;
    private Integer tidakterkirimint;

    private LazyDataModelJPA<GtwSmsOut> lazyDataModelJPASms;
    private LazyDataModelJPA<GtwEmailOut> lazyDataModelJPAEmail;
    private LazyDataModelJPA<ResPersonnel> lazyDataModelJPAPersonel;

    private String kolomnamagroup;
    private String groupIdSelected;
    private String kategoriSelected;

    private List<GtwGroup> namagroup = new ArrayList<>();

    private List<GtwSmsCategory> kategori;

    private List<ResPersonnel> personel;

    private Map<String, SmsEmail> mapPersonilId = new LinkedHashMap<>();

    private List<SmsEmail> smsEmail;
    private SmsEmail[] smsEmailSelected;

    private boolean isDisabled;
    private boolean isDisabledSms;
    private boolean isDisabledEmail;

    private String radiotipepesan;
    private String isipesansms;
    private Integer jumlahcharsms;
    private String isisms;
    private String subyekEmail;
    private String isiEmail;
    private String currentId;

    private GtwSmsOut gtwSmsOut;
    private TaskEmail taskEmail;
    private GtwEmailOut gtwEmailOut;

    @Autowired
    private GtwSmsOutRepo gtwSmsOutRepo;

    @Autowired
    private TaskEmailRepo taskEmailRepo;

    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;

    @Autowired
    private GtwGroupRepo gtwGroupRepo;

    @Autowired
    private GtwSmsCategoryRepo gtwSmsCategoryRepo;

    @Autowired
    private GtwGroupDetailRepo gtwGroupDetailRepo;

    @Autowired
    private ResPersonnelRepo resPersonnelRepo;

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private ResAssetContactRepo resAssetContactRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    private boolean checksms;
    private boolean checkemail;

    private Integer flag;
    private String ketikindextext;
    private String reference;

    Date date = new Date();
    private String tglhariini;

    public SmsOutMBean() {
        gtwSmsOut = new GtwSmsOut();
        taskEmail = new TaskEmail();
        smsEmail = new ArrayList<>();
        smsEmailSelected = new SmsEmail[0];
        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Kategori", "smscategoryid");
        listKolomPencarian.put("Nama Penerima", "recipientname");
        listKolomPencarian.put("No. Telp/Email", "phonenumber");
        listKolomPencarian.put("Tipe Pesan", "messagetype");

        listTipe = new LinkedHashMap<String, String>();
        listTipe.put("SMS", "sms");
        listTipe.put("Email", "email");

        listDropDown = new LinkedHashMap<String, String>();
        listDropDown.put("Penting", "CGK-1211-0001");

        isText = false;
        isDropDown = true;

        isEmail = false;
        isSMS = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        radiotipepesan = "Umum";
        jumlahcharsms = 0;
        ketikindextext = "Ketik %index% kirim bila sudah terima";
        generateIsiPesanSms();
        lazyDataModelJPASms = new LazyDataModelJPA(gtwSmsOutRepo) {
            @Override
            protected long getDataSize() {
                if (isSMS) {
                    if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("phonenumber"))) {
                        if (StringUtils.isNotBlank(textfield)) {
                            dropdown = textfield;
                        } else {
                            dropdown = kolompencarian;
                            kolompencarian = "kosong";
                        }
                    }
                    return gtwSmsOutRepo.count(whereQuerySmsOut(kolompencarian, dropdown));
                } else {
                    return 0;
                }
            }

            @Override
            protected Page getDatas(PageRequest request) {
                if (isSMS) {
                    if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("phonenumber"))) {
                        if (StringUtils.isNotBlank(textfield)) {
                            dropdown = textfield;
                        } else {
                            dropdown = kolompencarian;
                            kolompencarian = "kosong";
                        }
                    }
                    return gtwSmsOutRepo.findAll(whereQuerySmsOut(kolompencarian, dropdown), request);
                } else {
                    return null;
                }
            }
        };

        lazyDataModelJPAEmail = new LazyDataModelJPA(gtwEmailOutRepo) {
            @Override
            protected long getDataSize() {
                if (isEmail) {
                    if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
                        if (StringUtils.isNotBlank(textfield)) {
                            dropdown = textfield;
                        } else {
                            dropdown = kolompencarian;
                            kolompencarian = "kosong";
                        }

                    }
                    return gtwEmailOutRepo.count(whereQueryEmailOut(kolompencarian, dropdown));
                } else {
                    return 0;
                }
            }

            @Override
            protected Page getDatas(PageRequest request) {
                if (isEmail) {
                    if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
                        if (StringUtils.isNotBlank(textfield)) {
                            dropdown = textfield;
                        } else {
                            dropdown = kolompencarian;
                            kolompencarian = "kosong";
                        }
                    }
                    return gtwEmailOutRepo.findAll(whereQueryEmailOut(kolompencarian, dropdown), request);
                } else {
                    return null;
                }
            }
        };

    }

    @PostConstruct
    public void init() {

    }

    public void batal() {
        refresh();
    }

    public void refresh() {
        gtwSmsOut = new GtwSmsOut();
        taskEmail = new TaskEmail();
        radiotipepesan = "Umum";
        jumlahcharsms = 0;
        isisms = "";
        subyekEmail = "";
        isiEmail = "";
        reference = "";
        ketikindextext = "Ketik %index% kirim bila sudah terima";
        generateIsiPesanSms();

        isText = false;
        isDropDown = true;

        isEmail = false;
        isSMS = true;
    }

    public void simpan() {
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();

        //List<GtwSmsOut> gtwSmsOuts = new ArrayList<>();
        List<TaskEmail> taskEmails = new ArrayList<>();

        for (int i = 0; i < smsEmailSelected.length; i++) {
            if (checksms == true) {
                gtwSmsOut = new GtwSmsOut();
                if (gtwSmsOut.getSmsoutgoingid() == null) {
                    gtwSmsOut.setSmsoutgoingid(formatSmsoutgoingId());
                }

                gtwSmsOut.setRecipientname(smsEmailSelected[i].getName());
                gtwSmsOut.setPhonenumber(smsEmailSelected[i].getPhonenumber());
                gtwSmsOut.setTextmessage(isisms);
                gtwSmsOut.setReference(reference);
                gtwSmsOut.setDatecreated(date);
                gtwSmsOut.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                gtwSmsOut.setUsersiteid(currentId);
                gtwSmsOut.setIsdeleted(BigInteger.ZERO);
                gtwSmsOut.setPrioritas(BigInteger.ONE);
                gtwSmsOut.setSmscategoryid(kategoriSelected);
                gtwSmsOutRepo.save(gtwSmsOut);
                //gtwSmsOuts.add(gtwSmsOut);
            }

            if (checkemail == true) {
                taskEmail = new TaskEmail();
                taskEmail.setTaskName("Kirim Email dari Menu Utilities >> SMS Email Gateway");
                taskEmail.setTaskType("prosia.task.sendemail.SendEmailFromTemplateTask");
                taskEmail.setTaskTypeId(1);
                JSONObject json = new JSONObject();
                json.put("reference", reference);
                json.put("to", smsEmailSelected[i].getEmail());
                json.put("subyek", subyekEmail);
                json.put("isi", isiEmail);
                json.put("nama", smsEmailSelected[i].getName());

                taskEmail.setParameters(json.toString());
                taskEmail.setStatus("new");
                taskEmail.setTimeSubmitted(date);
                taskEmails.add(taskEmail);
                gtwEmailOut = new GtwEmailOut();
                if (gtwEmailOut.getEmailoutgoingid() == null) {
                    gtwEmailOut.setEmailoutgoingid(formatEmailoutgoingId());
                }

                gtwEmailOut.setRecipientname(smsEmailSelected[i].getName());
                gtwEmailOut.setEmailaddress(smsEmailSelected[i].getEmail());
                gtwEmailOut.setSubject(subyekEmail);
                gtwEmailOut.setTextmessage(isiEmail);
                gtwEmailOut.setReference(reference);
                gtwEmailOut.setDatecreated(date);
                gtwEmailOut.setCreatedby(menuNavMBean.getUserSession().getUserId().toString());
                gtwEmailOut.setUsersiteid(currentId);
                gtwEmailOut.setIsdeleted(BigInteger.ZERO);
                gtwEmailOut.setPrioritas(BigInteger.ONE);
                gtwEmailOut.setEmailcategoryid(kategoriSelected);
                gtwEmailOutRepo.save(gtwEmailOut);

            }
        }

        if (checksms == true && checkemail == true) {
//            gtwSmsOutRepo.save(gtwSmsOuts);
            taskEmailRepo.save(taskEmails);
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Sms dan Email berhasil dikirim");
        } else if (checksms == true) {
//            gtwSmsOutRepo.save(gtwSmsOuts);
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Sms berhasil dikirim");
        } else if (checkemail == true) {
            taskEmailRepo.save(taskEmails);
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Email berhasil dikirim");
        }

        refresh();
        RequestContext.getCurrentInstance().update("accId");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahSmsOut').hide()");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String formatSmsoutgoingId() {
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        tglhariini = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = tglhariini.split("-");
        String nextval = "";
        String smsOutId = "";
        List<GtwSmsOut> lis = gtwSmsOutRepo.findTopOneBySmsoutgoingidContaining(currentId);
        if (lis.isEmpty()) {
            smsOutId = currentId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = gtwSmsOutRepo.findGtwSmsOutByMaxId(currentId);
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
                smsOutId = currentId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                smsOutId = currentId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return smsOutId;
    }

    public String formatEmailoutgoingId() {
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        tglhariini = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = tglhariini.split("-");
        String nextval = "";
        String emailOutId = "";
        List<GtwEmailOut> lis = gtwEmailOutRepo.findTopOneByEmailoutgoingidContaining(currentId);
        if (lis.isEmpty()) {
            emailOutId = currentId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = gtwEmailOutRepo.findGtwEmailOutByMaxId(currentId);
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
                emailOutId = currentId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                emailOutId = currentId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return emailOutId;
    }

    private Specification<GtwSmsOut> whereQuerySmsOut(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<GtwSmsOut>() {

            @Override
            public Predicate toPredicate(Root<GtwSmsOut> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                if (StringUtils.isNotBlank(value)) {

                    if (field.equals("messagetype")) {
                        predicates.add(cb.equal(root.<BigInteger>get(field), value));
                    } else if (field.equals("kosong")) {
                        predicates.add(cb.like(cb.lower(root.<String>get("recipientname")),
                                getLikePattern(" ")));
                    } else {
                        predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                getLikePattern(value.trim())));
                    }

                    if (terkirim || pending || tidakterkirim) {
                        List<Predicate> ps = new ArrayList<>();
                        if (terkirim) {
                            ps.add(cb.equal(root.<Integer>get("isdelivered"), 1));
                        }
                        if (pending) {
                            ps.add(cb.isNull(root.<Integer>get("isdelivered")));
                        }
                        if (tidakterkirim) {
                            ps.add(cb.equal(root.<Integer>get("isdelivered"), 0));
                        }
                        predicates.add(orTogether(ps, cb));
                    }
                }
                if (currentId.equals("BSN")) {
                    query.orderBy(cb.desc(root.<Date>get("datecreated")));
                } else {
                    predicates.add(cb.equal(root.<String>get("usersiteid"), currentId));
                    query.orderBy(cb.desc(root.<Date>get("datecreated")));
                }
                return andTogether(predicates, cb);
            }
        };
    }

    private Specification<GtwEmailOut> whereQueryEmailOut(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<GtwEmailOut>() {

            @Override
            public Predicate toPredicate(Root<GtwEmailOut> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    if (field.equals("messagetype")) {
                        predicates.add(cb.equal(root.<BigInteger>get(field), value));
                    } else if (field.equals("kosong")) {
                        predicates.add(cb.like(cb.lower(root.<String>get("recipientname")),
                                getLikePattern("")));
                    } else {
                        predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                getLikePattern(value.trim())));
                    }

                    if (terkirim || pending || tidakterkirim) {
                        List<Predicate> ps = new ArrayList<>();
                        if (terkirim) {
                            ps.add(cb.equal(root.<Integer>get("isdelivered"), 1));
                        }
                        if (pending) {
                            ps.add(cb.isNull(root.<Integer>get("isdelivered")));
                        }
                        if (tidakterkirim) {
                            ps.add(cb.equal(root.<Integer>get("isdelivered"), 0));
                        }
                        predicates.add(orTogether(ps, cb));
                    }
                }
                if (currentId.equals("BSN")) {
                    query.orderBy(cb.asc(root.<Date>get("datecreated")));
                } else {
                    predicates.add(cb.equal(root.<String>get("usersiteid"), currentId));
                    query.orderBy(cb.asc(root.<Date>get("datecreated")));
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

    public void onTipeChange() {
        listKolomPencarian = new LinkedHashMap<String, String>();
        if (tipe != null && tipe.equalsIgnoreCase("sms")) {
            listKolomPencarian.put("Kategori", "smscategoryid");
            listKolomPencarian.put("Nama Penerima", "recipientname");
            listKolomPencarian.put("No. Telp/Email", "phonenumber");
            listKolomPencarian.put("Tipe Pesan", "messagetype");

            isEmail = false;
            isSMS = true;
        }

        if (tipe != null && tipe.equalsIgnoreCase("email")) {
            listKolomPencarian.put("Kategori", "emailoutgoingid");
            listKolomPencarian.put("Nama Penerima", "recipientname");
            listKolomPencarian.put("No. Telp/Email", "emailaddress");
            listKolomPencarian.put("Tipe Pesan", "messagetype");

            for (Map.Entry<String, String> entry : listKolomPencarian.entrySet()) {
            }

            isEmail = true;
            isSMS = false;
        }
    }

    public void onChangeIsiSms() {
        if (isisms != null) {
            jumlahcharsms = isisms.length();
        } else {
            jumlahcharsms = 0;
        }
    }

    public void isiMapPersonilId(Map<String, SmsEmail> m) {
        if (!smsEmail.isEmpty()) {
            for (Map.Entry<String, SmsEmail> entry : m.entrySet()) {
                if (!smsEmail.isEmpty() && !smsEmail.stream().filter(o -> o.getName().equals(entry.getValue().getName())).findFirst().isPresent()) {
                    smsEmail.add(entry.getValue());
                }
            }

        } else {
            for (Map.Entry<String, SmsEmail> entry : m.entrySet()) {
                smsEmail.add(entry.getValue());
            }
        }
        smsEmailSelected = new SmsEmail[smsEmail.size()];
        smsEmailSelected = smsEmail.toArray(smsEmailSelected);
    }

    public void tambahPersonil(String groupId) {
        if (groupIdSelected.equals("Pilih")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "WARNING!!!", "Nama Group harus dipilih"));
            return;
        }
        List<GtwGroupDetail> groupdetail = new ArrayList<>();
        groupdetail = gtwGroupDetailRepo.findByGroupID(groupIdSelected);

        for (GtwGroupDetail ggd : groupdetail) {
            ResPersonnel rp = resPersonnelRepo.findOne(ggd.getPersonnelID());
            SmsEmail se = new SmsEmail();
            se.setName(rp.getPersonnelName());
            se.setPhonenumber(rp.getMobilePhoneNumber());
            se.setEmail(rp.getEmail());
            mapPersonilId.put(ggd.getPersonnelID(), se);
        }
        isiMapPersonilId(mapPersonilId);
    }

    public void changeCheckSms() {
        if (!this.checksms) {
            isDisabledSms = false;
        } else {
            isDisabledSms = true;
        }

        jumlahcharsms = 0;
        isisms = null;
    }

    public void changeCheckEmail() {
        if (!this.checkemail) {
            isDisabledEmail = false;
        } else {
            isDisabledEmail = true;
        }

        isiEmail = null;
    }

    public void changeRadioTipePesan() {
        if ("Umum".equals(radiotipepesan)) {
            isDisabled = true;
        } else {
            isDisabled = false;
        }
    }

    public void generateIsiPesanSms() {
        Date date = new Date();
        String hariini = new SimpleDateFormat("yy-MM-dd-hh-mm").format(date);
        String[] splitDate = hariini.split("-");
        isipesansms = "TW" + splitDate[2] + splitDate[1] + splitDate[3] + splitDate[4] + "G";
    }

    public void openFormTambah() {
        namagroup = new ArrayList<>();
        namagroup = gtwGroupRepo.findByDeleted(false);

        kategori = new ArrayList<>();
        kategori = gtwSmsCategoryRepo.findByDeleted(false);

        smsEmail = new ArrayList<>();
        smsEmailSelected = new SmsEmail[0];
//      personilMasihAktif();

        mapPersonilId = new LinkedHashMap<>();

        groupIdSelected = "Pilih";
        kategoriSelected = "Pilih";
        personel = new ArrayList<>();

        isDisabled = true;
        isDisabledSms = true;
        isDisabledEmail = false;

        checksms = true;
        checkemail = false;

        RequestContext.getCurrentInstance().reset("accId:idTambahSmsOut");
        RequestContext.getCurrentInstance().update("accId:idTambahSmsOut");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahSmsOut').show()");
    }

    public void onKolomNamaGroupChange() {
        if (groupIdSelected != null) {
            tambahPersonil(groupIdSelected);
        }
        RequestContext.getCurrentInstance().update("accId:idTambahSmsOut");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahSmsOut').show()");
    }

    public void onKolomKategoriChange() {
    }

    public void onKolomPencarianChange() {
        if (isSMS.equals(true)) {
            if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("phonenumber"))) {
                isText = true;
                isDropDown = false;
            } else {
                isText = false;
                isDropDown = true;
                if (kolompencarian != null && (kolompencarian.equals("smscategoryid"))) {
                    listDropDown = new LinkedHashMap<String, String>();
                    listDropDown.put("Penting", "CGK-1211-0001");
                } else {
                    listDropDown = new LinkedHashMap<String, String>();
                    listDropDown.put("Utilities", "0");
                    listDropDown.put("Insiden Personil", "1");
                    listDropDown.put("Insiden SRU", "2");
                    listDropDown.put("Insiden Peralatan", "3");
                    listDropDown.put("Insiden Potensi", "4");
                    listDropDown.put("Radiogram", "5");
                }
            }
        }

        if (isEmail.equals(true)) {
            if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
                isText = true;
                isDropDown = false;
            } else {
                isText = false;
                isDropDown = true;
                if (kolompencarian != null && (kolompencarian.equals("emailoutgoingid"))) {
                    listDropDown = new LinkedHashMap<String, String>();
                    listDropDown.put("Penting", "CGK-1211-0001");
                } else {
                    listDropDown = new LinkedHashMap<String, String>();
                    listDropDown.put("Utilities", "0");
                    listDropDown.put("Insiden Personil", "1");
                    listDropDown.put("Insiden SRU", "2");
                    listDropDown.put("Insiden Peralatan", "3");
                    listDropDown.put("Insiden Potensi", "4");
                    listDropDown.put("Radiogram", "5");
                }
            }
        }
    }

    public void checkTerkirim() {
        String summary = terkirim ? "Checked" : "Unchecked";
        if (summary.equals("Unchecked")) {
            //RequestContext.getCurrentInstance().update("idMap");
            terkirimint = 0;
        } else {
            terkirimint = 1;
        }
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public void checkPending() {
        String summary = pending ? "Checked" : "Unchecked";
        if (summary.equals("Unchecked")) {
            pendingint = 0;
        } else {
            pendingint = 1;
        }
    }

    public void checkTidakTerkirim() {
        String summary = tidakterkirim ? "Checked" : "Unchecked";
        if (summary.equals("Unchecked")) {
            tidakterkirimint = 0;
        } else {
            tidakterkirimint = 1;
        }
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
