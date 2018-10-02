/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import static com.sun.javafx.logging.PulseLogger.addMessage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.MstEMployeeClass;
import prosia.basarnas.model.MstEmployeePosition;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.temp_model.SmsEmail;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class PencarianPersonelMBean extends AbstractManagedBean implements InitializingBean {

    private String textfield;
    private String kolompencarian;
    private String currentId;
    private LazyDataModelJPA<ResPersonnel> lazyDataModelJPAPersonel;
    private Map<String, String> listKolomPencarian;
    private List<ResPersonnel> checkboxpersonnel;

    @Autowired
    private ResPersonnelRepo resPersonnelRepo;

    @Autowired
    private SmsOutMBean smsoutmbean;

    private Map<String, SmsEmail> mapPersonilId = new LinkedHashMap<>();

    public void openFormTambahKepada() {
        textfield = "";
        kolompencarian = "";
        checkboxpersonnel = new ArrayList<>();

        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Nama Lengkap", "personnelName");
        listKolomPencarian.put("NIP/NRP", "personnelCode");
        listKolomPencarian.put("No. Hp", "mobilePhoneNumber");
        listKolomPencarian.put("Email", "email");
        listKolomPencarian.put("Jabatan Struktural", "structuralPosition");
        listKolomPencarian.put("Jabatan Fungsional", "functionalPosition");
        listKolomPencarian.put("Jabatan Siaga", "standByPosition");
        listKolomPencarian.put("Golongan", "employmentClass");

        RequestContext.getCurrentInstance().update("accId:idTambahKepadaOut");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').show()");
    }

    public void sendSelected() {
        mapPersonilId= new HashMap<String, SmsEmail>();
        if (checkboxpersonnel.isEmpty() || checkboxpersonnel.size() < 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "WARNING!!!", "Personil harus dipilih");
        } else {
            for (ResPersonnel rp : checkboxpersonnel) {
                SmsEmail se = new SmsEmail();
                se.setName(rp.getPersonnelName());
                se.setPhonenumber(rp.getMobilePhoneNumber());
                se.setEmail(rp.getEmail());
                mapPersonilId.put(rp.getPersonnelID(), se);
            }
            smsoutmbean.isiMapPersonilId(mapPersonilId);
            if (checkboxpersonnel.size() > 0) {
//                checkboxpersonnel.clear();
            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Personil berhasil ditambahkan"));
//            addPopUpMessage(FacesMessage.SEVERITY_INFO,
//                    "Sukses!!!", "Personil berhasil ditambahkan");
            RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').hide()");
        }
    }

    public void reset() {
        textfield = new String();
        kolompencarian = new String();
        RequestContext.getCurrentInstance().execute("PF('listWidget1').getPaginator().setPage(0)");
    }

    public void onKolomPencarianKepadaPersonel() {
        textfield = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPAPersonel = new LazyDataModelJPA(resPersonnelRepo) {
            @Override
            protected long getDataSize() {
                return resPersonnelRepo.count(whereQuery(kolompencarian, textfield));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return resPersonnelRepo.findAll(whereQuery(kolompencarian, textfield), request);
            }
        };
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
                    if (field.equals("structuralPosition")) {
                        predicates.add(cb.like(cb.lower(root.<MstEmployeePosition>get("structuralPosition").<String>get("employmentpositionname")),
                                getLikePattern(value.trim())));
                    } else if (field.equals("functionalPosition")) {
                        predicates.add(cb.like(cb.lower(root.<MstEmployeePosition>get("functionalPosition").<String>get("employmentpositionname")),
                                getLikePattern(value.trim())));
                    } else if (field.equals("standByPosition")) {
                        predicates.add(cb.like(cb.lower(root.<MstEmployeePosition>get("standByPosition").<String>get("employmentpositionname")),
                                getLikePattern(value.trim())));
                    } else if (field.equals("employmentClass")) {
                        predicates.add(cb.like(cb.lower(root.<MstEMployeeClass>get("employmentClass").<String>get("employmentclassname")),
                                getLikePattern(value.trim())));
                    } else {
                        predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                getLikePattern(value.trim())));
                    }
                }
                Predicate p1 = cb.isNotNull(root.<String>get("mobilePhoneNumber"));
                Predicate p2 = cb.isNotNull(root.<String>get("email"));
                if (currentId.equals("BSN")) {
                    predicates.add(cb.or(p1, p2));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), new BigInteger("1")));
                    query.orderBy(cb.asc(root.get("personnelName")));
                } else {
                    predicates.add(cb.equal(root.<MstKantorSar>get("officeSar").<String>get("kantorsarid"), currentId));
                    predicates.add(cb.or(p1, p2));
                    predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), new BigInteger("1")));
                    query.orderBy(cb.asc(root.get("personnelName")));
                }
                return andTogether(predicates, cb);
            }
        };
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

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
