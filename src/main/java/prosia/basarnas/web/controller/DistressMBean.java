/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.UtiBeaconMessage;
import prosia.basarnas.repo.UtiBeaconMessageRepo;
import org.apache.commons.lang3.StringUtils;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import static org.hibernate.internal.CoreLogging.logger;
import static org.hibernate.jpa.internal.HEMLogging.logger;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.service.SimpleDateTimeZoneFormat;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class DistressMBean implements InitializingBean {

    private LazyDataModelJPA<UtiBeaconMessage> lazyDataModelJPA;

    private UtiBeaconMessage utiBeaconMessage;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UtiBeaconMessageRepo utiBeaconMessageRepo;
    private String field;
    private String value;
    private Date valueDate;
    private Integer valueInput;
    private Boolean showButton;
    private Boolean showCombo;
    private Boolean showButtonInput;
    private Boolean disabled;

    public DistressMBean() {
        utiBeaconMessage = new UtiBeaconMessage();
        valueInput = 2;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(utiBeaconMessageRepo) {
            @Override
            protected long getDataSize() {
                return utiBeaconMessageRepo.count(whereQuery(field, value));
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return utiBeaconMessageRepo.findAll(whereQuery(field, value), request);
            }
        };
    }

    public void reset() {
        field = "ownerName";
        value = "";
        showCombo = false;
        valueInput = 2;
        RequestContext.getCurrentInstance().execute("PF('listWidget3').getPaginator().setPage(0)");
    }

    private Specification<UtiBeaconMessage> whereQuery(
            final String field,
            final String value) {

        List<Predicate> predicates = new ArrayList<>();

        return new Specification<UtiBeaconMessage>() {

            @Override
            public Predicate toPredicate(Root<UtiBeaconMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value) || valueDate != null) {
                    System.out.println("field = " + field);
                    System.out.println("value = " + value);
                    switch (field) {
                        case "msgNumber":
                            predicates.add(cb.like(cb.lower(root.<String>get("msgNumber")),
                                    getLikePattern(value.trim())));
                            break;
                        case "transmitDtg":
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                System.out.println(valueDate);
                                String tanggal = formatter.format(valueDate);
                                String firstDate = Tanggal.dateTimeStringConvert(Tanggal.stringDateConvert(tanggal));
                                String secondDate = Tanggal.dateTimeStringConvert(Tanggal.addDayToDate(Tanggal.stringDateConvert(tanggal), 1));
                                predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(field), formatter.parse(firstDate)));
                                predicates.add(cb.lessThan(root.<Date>get(field), formatter.parse(secondDate)));
                            } catch (ParseException ex) {
                                logger.error("Parse Exception : {}", ex.getMessage());
                            }
                            break;
                    }
                }
                //  predicates.add(cb.equal(root.<Integer>get("deleted"), 0));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public void changeValueSearch() {
        if (field.equals("transmitDtg")) {
            valueInput = 1;
        } else {
            valueInput = 2;
        }
    }

    public String transmitDtgFormat(UtiBeaconMessage bc) {
        String transmitDtg = "";
        SimpleDateTimeZoneFormat sdf = new SimpleDateTimeZoneFormat();
        sdf.applyPattern(ProsiaConstant.UI_DATE_TIME_ZONE_FORMAT_X);
        Calendar cal = Calendar.getInstance();
        if (bc.getTransmitDtg() != null) {
            cal.setTime(bc.getTransmitDtg());
            cal.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        transmitDtg = sdf.format(cal);
        return transmitDtg;
    }

    public void viewDetailmsg(UtiBeaconMessage messageID, Boolean edited) {
        this.utiBeaconMessage = messageID;

        logger.debug("Message ID : {}", utiBeaconMessage.getMessageID());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);

        RequestContext.getCurrentInstance().reset("idDetail");
        RequestContext.getCurrentInstance().update("idDetail");
        RequestContext.getCurrentInstance().execute("PF('widgetDetail').show()");

    }

    public void viewDetailNarratives(UtiBeaconMessage messageID, Boolean edited) {
        this.utiBeaconMessage = messageID;

        logger.debug("Message ID : {}", utiBeaconMessage.getMessageID());
        disabled = edited;
        showButton = !edited;
        logger.debug("disabled : {}", disabled);
        logger.debug("showButton : {}", showButton);

        RequestContext.getCurrentInstance().reset("idDetailNarratives");
        RequestContext.getCurrentInstance().update("idDetailNarratives");
        RequestContext.getCurrentInstance().execute("PF('widgetDetailNarratives').show()");

    }

}
