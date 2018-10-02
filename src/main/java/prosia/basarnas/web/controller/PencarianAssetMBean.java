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
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResAssetContact;
import prosia.basarnas.repo.ResAssetContactRepo;
import prosia.basarnas.temp_model.SmsEmail;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class PencarianAssetMBean extends AbstractManagedBean implements InitializingBean {

    private String textfield;
    private String kolompencarian;
    private String currentId;
    private LazyDataModelJPA<ResAssetContact> lazyDataModelJPAAssetContact;
    private Map<String, String> listKolomPencarian;
    private List<ResAssetContact> checkboxasset;

    private Map<String, SmsEmail> mapPersonilId = new LinkedHashMap<>();

    @Autowired
    private SmsOutMBean smsoutmbean;

    @Autowired
    private ResAssetContactRepo resAssetContactRepo;

    public void onKolomPencarianKepadaAsset() {
        textfield = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPAAssetContact = new LazyDataModelJPA(resAssetContactRepo) {
            @Override
            protected long getDataSize() {
                return resAssetContactRepo.count(whereQuery(kolompencarian, textfield));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return resAssetContactRepo.findAll(whereQuery(kolompencarian, textfield), request);
            }
        };
    }

    public void openFormTambahKepada() {
        textfield = "";
        kolompencarian = "";
        checkboxasset = new ArrayList<>();

        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Nama", "contactname");
        listKolomPencarian.put("Asset", "assetid");
        listKolomPencarian.put("No. Handphone", "phonenumber2");
        listKolomPencarian.put("Email", "email");

        RequestContext.getCurrentInstance().update("accId:idTambahKepadaOut");
        RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').show()");
    }

    public void sendSelected() {
        mapPersonilId = new HashMap<String, SmsEmail>();
        if (checkboxasset.isEmpty() || checkboxasset.size() < 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN,
                    "WARNING!!!", "Asset contact harus dipilih");
        } else {
            for (ResAssetContact rp : checkboxasset) {
                SmsEmail se = new SmsEmail();
                se.setName(rp.getContactname());
                se.setPhonenumber(rp.getPhonenumber2());
                se.setEmail(rp.getEmail());
                mapPersonilId.put(rp.getContactname(), se);
            }
            smsoutmbean.isiMapPersonilId(mapPersonilId);
            if (checkboxasset.size() > 0) {
//              checkboxpotency.clear();
            }
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Asset Contact berhasil ditambahkan"));            
//            addPopUpMessage(FacesMessage.SEVERITY_INFO,
//                    "Sukses!!!", "Asset contact berhasil ditambahkan");
            RequestContext.getCurrentInstance().execute("PF('widgetTambahKepadaOut').hide()");
        }
    }

    public void reset() {
        textfield = new String();
        kolompencarian = new String();
        RequestContext.getCurrentInstance().execute("PF('listWidget3').getPaginator().setPage(0)");
    }

    private Specification<ResAssetContact> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        return new Specification<ResAssetContact>() {

            @Override
            public Predicate toPredicate(Root<ResAssetContact> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    if (field.equals("assetid")) {
                        predicates.add(cb.like(cb.lower(root.<ResAsset>get("assetid").<String>get("name")),
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
                    predicates.add(cb.equal(root.<ResAsset>get("assetid").<MstKantorSar>get("kantorsarid").<String>get("kantorsarid"), currentId));
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
