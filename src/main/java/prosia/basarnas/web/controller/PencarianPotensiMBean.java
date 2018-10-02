/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

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
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.repo.ResPotencyContactRepo;
//import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.temp_model.SmsEmail;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class PencarianPotensiMBean extends AbstractManagedBean implements InitializingBean {

    private String textfield;
    private String kolompencarian;
    private LazyDataModelJPA<ResPotencyContact> lazyDataModelJPAPotencyContact;
    private Map<String, String> listKolomPencarian;
    private List<ResPotencyContact> checkboxpotency;
    private String currentId;

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private SmsOutMBean smsoutmbean;

    private Map<String, ResPotencyContact> mapPotencyContactId = new LinkedHashMap<>();

    private Map<String, SmsEmail> mapPersonilId = new LinkedHashMap<>();

    public void onKolomPencarianKepadaPotency() {
        textfield = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPAPotencyContact = new LazyDataModelJPA(resPotencyContactRepo) {
            @Override
            protected long getDataSize() {
                return resPotencyContactRepo.count(whereQuery(kolompencarian, textfield));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return resPotencyContactRepo.findAll(whereQuery(kolompencarian, textfield), request);
            }
        };
    }

    public void sendSelected() {
        mapPersonilId = new HashMap<String, SmsEmail>();
        if (checkboxpotency.isEmpty() || checkboxpotency.size() < 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "WARNING!!!", "Potency contact harus dipilih");
        } else {
            for (ResPotencyContact rp : checkboxpotency) {
                SmsEmail se = new SmsEmail();
                se.setName(rp.getContactname());
                se.setPhonenumber(rp.getPhonenumber2());
                se.setEmail(rp.getEmail());
                mapPersonilId.put(rp.getContactname(), se);
            }
            smsoutmbean.isiMapPersonilId(mapPersonilId);
            if (checkboxpotency.size() > 0) {
//                checkboxpotency.clear();
            }
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Potency Contact berhasil ditambahkan"));            
//            addPopUpMessage(FacesMessage.SEVERITY_INFO,
//                    "Sukses!!!", "Potency Contact berhasil ditambahkan");
            RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').hide()");
        }
    }

    public void reset() {
        textfield = new String();
        kolompencarian = new String();
        RequestContext.getCurrentInstance().execute("PF('listWidget2').getPaginator().setPage(0)");
    }

    public void openFormTambahKepada() {
        textfield = "";
        kolompencarian = "";
        checkboxpotency = new ArrayList<>();

        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Nama Lengkap", "contactname");
        listKolomPencarian.put("Nama Potensi", "potencyid");
        listKolomPencarian.put("No. HP", "phonenumber2");
        listKolomPencarian.put("Email", "email");

        RequestContext.getCurrentInstance().update("accId:idTambahKepadaOut");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').show()");
    }

    private Specification<ResPotencyContact> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<ResPotencyContact>() {

            @Override
            public Predicate toPredicate(Root<ResPotencyContact> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    if (field.equals("potencyid")) {
                        predicates.add(cb.like(cb.lower(root.<ResPotency>get("potencyid").<String>get("potencyname")),
                                getLikePattern(value.trim())));
                    } else {
                        predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                getLikePattern(value.trim())));
                    }
                }
                Predicate p1 = cb.isNotNull(root.<String>get("phonenumber2"));
                Predicate p2 = cb.isNotNull(root.<String>get("email"));
                if (currentId.equals("BSN")) {
                    predicates.add(cb.or(p1, p2));
                    query.orderBy(cb.asc(root.get("contactname")));
                } else {
                    predicates.add(cb.equal(root.<ResPotency>get("potencyid").<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), currentId));
                    predicates.add(cb.or(p1, p2));
                    query.orderBy(cb.asc(root.get("contactname")));
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
