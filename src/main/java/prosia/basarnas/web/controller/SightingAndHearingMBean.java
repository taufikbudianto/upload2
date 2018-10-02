/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.ArrayList;
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
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.SightingAndHearingRepo;

/**
 *
 * @author PROSIA
 */
@Component
@Scope("view")
public @Data
class SightingAndHearingMBean extends AbstractManagedBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<UtiSighting> lazyDataModelJPA;
    private String field;
    private String value;
    private String where;
    private Boolean showCombo;
    private Boolean showDate;

    @Autowired
    private SightingAndHearingRepo sightingAndHearingRepo;

    public SightingAndHearingMBean() {
        showCombo = false;
        showDate = false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lazyDataModelJPA = new LazyDataModelJPA(sightingAndHearingRepo) {
            @Override
            protected long getDataSize() {
                //return sightingAndHearingRepo.count(whereQuery(field, value));
                return sightingAndHearingRepo.count();
            }

            @Override
            protected Page getDatas(PageRequest request) {
                //return sightingAndHearingRepo.findAll(whereQuery(field, value), request);
                return sightingAndHearingRepo.findAll(request);
            }
        };
    }

    private Specification<UtiSighting> whereQuery(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<UtiSighting>() {

            @Override
            public Predicate toPredicate(Root<UtiSighting> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    switch (field) {
                        //examples
                        case "model":
                            predicates.add(cb.and(
                                    cb.like(cb.lower(root.<String>get("otherBeaconModel")), getLikePattern(value)),
                                    cb.isNull(root.<UtiSighting>get("beaconModel"))
                            ));
                            predicates.add(cb.and(
                                    cb.isNotNull(root.<UtiSighting>get("beaconModel")),
                                    cb.like(cb.lower(root.<UtiSighting>get("beaconModel").<String>get("beaconModelName")),
                                            getLikePattern(value))));
                            return orTogether(predicates, cb);
                        case "":
                            predicates.add(cb.and(
                                    cb.isNull(root.<UtiSighting>get("")),
                                    cb.like(cb.lower(root.<String>get("")),
                                            getLikePattern(value))));
                            predicates.add(cb.and(
                                    cb.isNotNull(root.<UtiSighting>get("")),
                                    cb.like(cb.lower(root.<UtiSighting>get("")
                                            .<String>get("")),
                                            getLikePattern(value))));
                            return orTogether(predicates, cb);
                        case "1":
                            predicates.add(cb.equal(root.<Integer>get(field), value));
                            break;
                        case "2":
                            predicates.add(cb.equal(root.<String>get(field), value));
                            break;
                        default:
                            predicates.add(cb.like(cb.lower(root.<String>get(field)),
                                    getLikePattern(value)));
                            break;
                    }
                }
                query.orderBy(cb.asc(root.get("approved")));
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

    public String objectTypeToString(Integer objectType) {
        if (objectType == 1) {
            return "Kapal";
        } else if (objectType == 2) {
            return "Orang";
        } else if (objectType == 3) {
            return "Polusi";
        } else if (objectType == 4) {
            return "Pesawat";
        } else if (objectType == 5) {
            return "Sinyal";
        } else if (objectType == 6) {
            return "Kendaraan";
        } else if (objectType == 7) {
            return "Lain-lain";
        } else {
            return "";
        }
    }

    public String latitideFormatted(String latitude) {
        try {
            String latitiudeFormat = "";
            if (latitude != null && !latitude.equals("")) {
                latitiudeFormat = decimalToTimeConvert(latitude);
                String[] y = latitiudeFormat.split("'");
                if (y.length < 3) {
                    latitiudeFormat = y[0] + "\u00b0 " + y[1] + "' 00\"";
                } else if (y.length == 3) {
                    latitiudeFormat = y[0] + "\u00b0 " + y[1] + "' " + y[2] + "\" ";
                }
                if (latitiudeFormat.split("-").length > 1) {
                    latitiudeFormat = latitiudeFormat.split("-")[1] + " LS";
                } else {
                    latitiudeFormat = latitiudeFormat + " LU";
                }
            } else {
                return "-";
            }
            return latitiudeFormat;
        } catch (Exception e) {
            return "#Format Salah#";
        }
    }

    private static String decimalToTimeConvert(String decimal) {
        LatLangConvertResult result = convertTo(new LatLangConvertResult(decimal, 0.0, ProsiaConstant.DECIMAL_DEGREE));
        return result.getDegree();
    }

    private static LatLangConvertResult convertTo(LatLangConvertResult value) {
        double dfDegree, dfMinute, dfSecond, residual, dfFrac, dfSec, dfDecimal;
        if (value.getType() == ProsiaConstant.TIME_DEGREE) {
            String[] dms = value.getDegree().split("'");
            dfDegree = Double.parseDouble(dms[0]);
            dfMinute = Double.parseDouble(dms[1]);
            dfSecond = Double.parseDouble(dms[2]);
            dfSecond += value.getResedual();
            dfFrac = dfMinute / 60 + dfSecond / 360;
            if (dfDegree < 0) {
                dfDecimal = dfDegree - dfFrac;
            } else {
                dfDecimal = dfDegree + dfFrac;
            }
            return new LatLangConvertResult(String.valueOf(dfDecimal), 0, ProsiaConstant.DECIMAL_DEGREE);
        } else if (value.getType() == ProsiaConstant.DECIMAL_DEGREE) {
            if (value.getDegree().startsWith("+")) {
                value.setDegree(value.getDegree().substring(1));
            }
            Double deg = Double.valueOf(value.getDegree());
            Boolean isMinus = false;
            if (value.getDegree().startsWith("-")) {
                isMinus = true;
            }
            dfDecimal = Math.abs(deg);
            if (isMinus) {
                dfDegree = Math.ceil(deg);
            } else {
                dfDegree = Math.floor(deg);
            }
            dfFrac = Math.abs(deg - dfDegree);
            dfSec = dfFrac * 3600;
            dfMinute = Math.floor(dfSec / 60);
            dfSecond = dfSec - dfMinute * 60;

            if (Math.rint(dfSecond) == 60) {
                dfMinute += 1;
                dfSecond = 0;
            }
            if (Math.rint(dfMinute) == 60) {
                dfDecimal += 1;
                dfMinute = 0;
            }
            residual = dfSecond - Math.floor(dfSecond);

            String degree;
            if (isMinus) {
                degree = "-" + String.valueOf((int) Math.ceil(--dfDecimal));
            } else {
                degree = String.valueOf((int) Math.ceil(dfDecimal));
            }
            int minute = (int) dfMinute;
            int second = (int) Math.floor(dfSecond);
            LatLangConvertResult result = new LatLangConvertResult(degree + "'" + minute + "'" + second, residual, ProsiaConstant.TIME_DEGREE);
            return result;
        }
        return null;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    private @Data
    static class LatLangConvertResult {

        private String degree;
        private double resedual;
        private Integer type;

        public LatLangConvertResult(String degree, double resedual, Integer type) {
            this.degree = degree;
            this.resedual = resedual;
            this.type = type;
        }
    }
}
