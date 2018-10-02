/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class IncidentSightingBrowseMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<UtiSighting> dataModel;
    private String field;
    private String value;
    private Boolean showCombo;

    @Autowired
    private SightingAndHearingRepo sightingAndHearingRepo;

    public IncidentSightingBrowseMBean() {
        reset();
    }

    public final void reset(){
        field = "";
        value = "";
        showCombo = false;
    }
    
    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addListSighting();
    }

    public void addListSighting() {
        dataModel = new LazyDataModelJPA(sightingAndHearingRepo) {
            @Override
            protected long getDataSize() {
                return sightingAndHearingRepo.count(whereQueryBrowse());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return sightingAndHearingRepo.findAll(whereQueryBrowse(), request);
            }
        };
    }

    private Specification<UtiSighting> whereQueryBrowse() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<UtiSighting>() {
            @Override
            public Predicate toPredicate(Root<UtiSighting> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        case "objectType":
                            predicates.add(cb.equal(root.<Integer>get(field), value));
                            break;
                        default:
                            predicates.add(cb.like(root.<String>get(field), getLikePattern(value)));
                            break;
                    }
                }
                predicates.add(cb.isNull(root.<Incident>get("incident")));
                predicates.add(cb.notEqual(root.<Boolean>get("isDeleted"), true));
                return andTogether(predicates, cb);
            }
        };
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void chengeToCombo() {
        logger.debug("field : {}", field);
        value = "";
        showCombo = field.equals("objectType");
        logger.debug("showCombo : {}", showCombo);
    }

    public String dateTimeStringConvert(Date date) {
        return Tanggal.dateTimeStringConvert(date);
    }

    public String objectTypeToString(Integer objectType) {
        if (null == objectType) {
            return "";
        } else {
            switch (objectType) {
                case 1:
                    return "Kapal";
                case 2:
                    return "Orang";
                case 3:
                    return "Polusi";
                case 4:
                    return "Pesawat";
                case 5:
                    return "Sinyal";
                case 6:
                    return "Kendaraan";
                case 7:
                    return "Lain-lain";
                default:
                    return "";
            }
        }
    }
}
