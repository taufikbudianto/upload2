/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
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
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelFilterJPA;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsw;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.IncidentAswRepo;

/**
 *
 * @author Ismail
 */
@Component
@Scope("view")
@Data
public class IncidentAswMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private IncidentMBean incidentMBean;
    @Autowired
    private IncidentAswRepo incidentAswRepo;
    private IncidentAsw incidentAsw;
    private LazyDataModelJPA<IncidentAsw> listIncidentAsw;
    private Integer sumHours;
    private Double sumASin;
    private Double sumACos;
    private Double sumASin2;
    private Double sumACos2;
    private Double sumAtan;
    private Double sumSqrt;
    private Double sumVector;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public void init() {
        incidentAsw = new IncidentAsw();
        incidentAsw.setTimeInterval1("0");
        incidentAsw.setTimeInterval2("0");
        incidentAsw.setNumberHours("0");
        incidentAsw.setContribution("0");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listIncidentAsw = new LazyDataModelJPA(incidentAswRepo) {
            @Override
            protected long getDataSize() {
                return incidentAswRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentAswRepo.findAll(whereQuery(), request);
            }

        };
        init();
        sumAllAsw();
    }

    public Specification<IncidentAsw> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<IncidentAsw>() {
            @Override
            public Predicate toPredicate(Root<IncidentAsw> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(incidentMBean.getIncident().getIncidentid())) {
                    predicates.add(cb.equal(root.<Incident>get("incident"),
                            incidentMBean.getIncident()));
                }
                cq.orderBy(cb.desc(root.<Date>get("timeObservation")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public void onChangeInterval() {
        int interval1 = Integer.parseInt(incidentAsw.getTimeInterval1());
        int interval2 = Integer.parseInt(incidentAsw.getTimeInterval2());
        if (interval1 < interval2) {
            incidentAsw.setNumberHours(String.valueOf(interval2 - interval1));
        } else {
            incidentAsw.setNumberHours(String.valueOf(interval1 - interval2));
        }
        onChangeContribution();
    }

    public void onChangeContribution() {
        if (StringUtils.isNotBlank(incidentAsw.getWinSpeed())) {
            incidentAsw.setContribution(
                    String.valueOf(
                            Integer.parseInt(incidentAsw.getNumberHours())
                            * Integer.parseInt(incidentAsw.getWinSpeed())));
        } else {
            incidentAsw.setContribution("0");
        }
    }

    public void sumAllAsw() {
        List<IncidentAsw> listAsw = incidentAswRepo.findAll(whereQuery());
        sumHours = listAsw.stream().mapToInt(i -> Integer.parseInt(i.getNumberHours())).sum();
        sumASin = listAsw.stream().mapToDouble(i -> i.getAsin() != null ? Double.parseDouble(i.getAsin()) : 0).sum();
        sumACos = listAsw.stream().mapToDouble(i -> i.getAcos() != null ? Double.parseDouble(i.getAcos()) : 0).sum();
        sumASin2 = sumASin * sumASin;
        sumACos2 = sumACos * sumACos;
        sumAtan = Math.atan(sumASin / sumACos);
        sumSqrt = Math.sqrt(sumASin2 + sumACos2);
        sumVector = sumASin < 0
                ? (sumAtan * 180 / Math.PI < 0
                        ? sumAtan * 180 / Math.PI + 180
                        : sumAtan * 180 / Math.PI) + 180
                : (sumAtan * 180 / Math.PI < 0
                        ? sumAtan * 180 / Math.PI + 180
                        : sumAtan * 180 / Math.PI);
    }

    public void tambahAsw() {
        if (incidentAsw.getTimeObservation() == null
                || StringUtils.isBlank(incidentAsw.getWinDir())
                || StringUtils.isBlank(incidentAsw.getWinSpeed())
                || (incidentAsw.getTimeInterval1().equals("0")
                && incidentAsw.getTimeInterval2().equals("0"))) {
            addPopUpMessage("ASW", "Data tidak lengkap");
            return;
        }
        if (StringUtils.isBlank(incidentAsw.getAswId())) {
            incidentAsw.setAswId(createidtemp(incidentAswRepo.findIncidentAswByMaxId("CGK")));
        }
        incidentAsw.setIncident(incidentMBean.getIncident());
        incidentAsw.setRadian(String.valueOf(Double.parseDouble(incidentAsw.getWinDir()) * Math.PI / 180));
        incidentAsw.setSinus(String.valueOf(Math.sin(Double.parseDouble(incidentAsw.getRadian()))));
        incidentAsw.setCosinus(String.valueOf(Math.cos(Double.parseDouble(incidentAsw.getRadian()))));
        incidentAsw.setAsin(String.valueOf(Double.parseDouble(incidentAsw.getContribution()) * Double.parseDouble(incidentAsw.getSinus())));
        incidentAsw.setAcos(String.valueOf(Double.parseDouble(incidentAsw.getContribution()) * Double.parseDouble(incidentAsw.getCosinus())));
        incidentAswRepo.save(incidentAsw);
        init();
        sumAllAsw();
        RequestContext.getCurrentInstance().update("incidentdetail:form-asw");
    }

    public void ubahAsw() {
        incidentAsw = (IncidentAsw) getRequestParam("listRow");
        RequestContext.getCurrentInstance().update("incidentdetail:form-asw");
    }

    public void hapusAsw() {
        IncidentAsw listRow = (IncidentAsw) getRequestParam("listRow");
        incidentAswRepo.delete(listRow);
        sumAllAsw();
        RequestContext.getCurrentInstance().update("incidentdetail:form-asw");
    }

    private String createidtemp(String maxassetId) {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        if (StringUtils.isBlank(maxassetId)) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }
}
