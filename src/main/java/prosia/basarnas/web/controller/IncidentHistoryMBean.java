/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.repo.IncidentHistoryRepo;
import prosia.basarnas.web.util.LatitudeLongitude;

/**
 *
 * @author TOMY
 */
@Component
@Scope("view")

public @Data
class IncidentHistoryMBean extends AbstractManagedBean implements InitializingBean {

    private Incident currIncident;
    private LazyDataModelJPA<Incident> dataModel;
    private String field;
    private String value;
    @Autowired
    private IncidentHistoryRepo incidentHistoryRepo;
    @Autowired
    private IncidentMBean incidentMBean;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        currIncident = incidentMBean.getIncident();
        if (currIncident != null) {
            log.debug("Curr Incident :" + currIncident.getIncidentid());
            List<String> nearestIncident = new ArrayList<>();
            if (StringUtils.isNotBlank(currIncident.getLongitude()) && StringUtils.isNotBlank(currIncident.getLatitude())) {
                List<Object[]> listObj = incidentHistoryRepo.getNearestIncident(
                        currIncident.getLongitude(), currIncident.getLatitude());
                if (!listObj.isEmpty()) {
                    for (Object[] obj : listObj) {
                        if (!String.valueOf(obj[0]).equals(currIncident.getIncidentid())) {
                            nearestIncident.add(String.valueOf(obj[0]));
                        } else if(nearestIncident.isEmpty()) {
                            nearestIncident.add(currIncident.getIncidentid());
                        }
                    }
                }else if(nearestIncident.isEmpty()){
                     nearestIncident.add(currIncident.getIncidentid());
                }
            }else{
                nearestIncident.add(currIncident.getIncidentid());
            }
            System.out.println(nearestIncident.toString());
            setData(currIncident.getUsersiteid(), nearestIncident);
        }
    }

    public void setData(String usersiteid, List<String> incidentId) {
        dataModel = new LazyDataModelJPA(incidentHistoryRepo) {
            @Override
            protected long getDataSize() {
                //return incidentHistoryRepo.countListIncident(incidentId);
                return incidentHistoryRepo.count(whereQuery(field, value, incidentId));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                Page<Incident> pageData = new PageImpl<>(incidentHistoryRepo.getListIncident(incidentId));
                // incidentHistoryRepo.getListIncident(incidentId),request,incidentHistoryRepo.countListIncident(incidentId));
                return incidentHistoryRepo.findAll(whereQuery(field, value, incidentId), request);
                //return incidentHistoryRepo.findAll(whereQuery(field, value , usersiteid),request);
            }
        };
    }

    private Specification<Incident> whereQuery(final String field,
            final String value, List<String> incidentId) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<Incident>() {

            @Override
            public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(
                            cb.like(cb.lower(root.<String>get(field)), getLikePattern(value))
                    );
                }
                Expression exp = root.get("incidentid");
                predicates.add(exp.in(incidentId));
                //predicates.add(cb.equal(root.<String>get("usersiteid"), usersite));
                predicates.add(cb.notEqual(root.<BigInteger>get("isdeleted"), 1));
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

    public String getLatitudeFormat(String format) {
        return LatitudeLongitude.latitideFormatted(format);
    }

    public String getLongitudeFormat(String format) {
        return LatitudeLongitude.longitudeFormatted(format);
    }

}
