/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
import prosia.basarnas.model.GtwEmailOut;
import prosia.basarnas.model.GtwSmsOut;
import prosia.basarnas.repo.GtwEmailOutRepo;
import prosia.basarnas.repo.GtwSmsOutRepo;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class EmailOutMBean extends AbstractManagedBean implements InitializingBean {

    private String tipe;
    private String kolompencarian;
    private String currentId;
    private Map<String, String> listKolomPencarian;
    private Map<String, String> listDropDown;

    private Map<String, Map<String, String>> data = new HashMap<>();
    private String textfield;
    private String dropdown;
    private Boolean isText;
    private Boolean isDropDown;

    private Boolean terkirim;
    private Boolean pending;
    private Boolean tidakterkirim;

    private Integer terkirimint;
    private Integer pendingint;
    private Integer tidakterkirimint;

    private LazyDataModelJPA<GtwEmailOut> lazyDataModelJPAEmail;

    @Autowired
    private GtwEmailOutRepo gtwEmailOutRepo;

    public EmailOutMBean() {
        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Kategori", "emailcategoryid");
        listKolomPencarian.put("Nama Penerima", "recipientname");
        listKolomPencarian.put("No. Telp/Email", "emailaddress");
        listKolomPencarian.put("Tipe Pesan", "messagetype");

        listDropDown = new LinkedHashMap<String, String>();
        listDropDown.put("Penting", "CGK-1211-0001");

        isText = false;
        isDropDown = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPAEmail = new LazyDataModelJPA(gtwEmailOutRepo) {
            @Override
            protected long getDataSize() {
                if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
                    dropdown = textfield;
                }
                return gtwEmailOutRepo.count(whereQueryEmailOut(kolompencarian, dropdown));

            }

            @Override
            protected Page getDatas(PageRequest request) {
                if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
                    dropdown = textfield;
                }
                return gtwEmailOutRepo.findAll(whereQueryEmailOut(kolompencarian, dropdown), request);
            }
        };

    }

    @PostConstruct
    public void init() {

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
//                query.orderBy(cb.asc(root.get("delivereddate")));
                    query.orderBy(cb.desc(root.<Date>get("datecreated")));
                } else {
                    predicates.add(cb.equal(root.<String>get("usersiteid"), currentId));
//                query.orderBy(cb.asc(root.get("delivereddate")));
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
//                query.orderBy(cb.asc(root.get("delivereddate")));
                    query.orderBy(cb.desc(root.<Date>get("datecreated")));
                } else {
                    predicates.add(cb.equal(root.<String>get("usersiteid"), currentId));
//                query.orderBy(cb.asc(root.get("delivereddate")));
                    query.orderBy(cb.desc(root.<Date>get("datecreated")));
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
        if (kolompencarian != null && (kolompencarian.equals("recipientname") || kolompencarian.equals("emailaddress"))) {
            isText = true;
            isDropDown = false;
        } else {
            isText = false;
            isDropDown = true;
            if (kolompencarian != null && (kolompencarian.equals("emailcategoryid"))) {
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
