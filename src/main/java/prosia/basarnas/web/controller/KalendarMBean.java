/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

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
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.CPKalender;
import prosia.basarnas.repo.KalenderRepo;
import prosia.basarnas.web.util.Tanggal;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
public @Data
class KalendarMBean extends AbstractManagedBean implements InitializingBean {

    private LazyDataModelJPA<CPKalender> listKegiatan;
    private CPKalender kegiatan;
    private String field;
    private String value;
    private String headerItem;
    private Boolean isView;
    private Boolean disableCalendar;

    @Autowired
    private KalenderRepo kegiatanRepo;

    @Override
    public void init() {
        kegiatan = new CPKalender();
        disableCalendar = false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listKegiatan = new LazyDataModelJPA(kegiatanRepo) {
            @Override
            protected long getDataSize() {
                return kegiatanRepo.count(whereQuery());
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return kegiatanRepo.findAll(whereQuery(), request);
            }
        };
    }

    private Specification<CPKalender> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPKalender>() {
            @Override
            public Predicate toPredicate(Root<CPKalender> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(cb.like(cb.lower(root.<String>get(field)),
                            getLikePattern(value.trim())));
                }
                predicates.add(cb.equal(root.<Boolean>get("deleted"), false));
                cq.orderBy(cb.desc(root.get("createdDate")));
                return andTogether(predicates, cb);
            }
        };
    }

    public void loadForm() {
        kegiatan = (CPKalender) getRequestParam("listRow");
        if (kegiatan != null) {
            Integer flag = Integer.parseInt((String) getRequestParam("flag"));
            if (flag == 1) {
                isView = true;
                disableCalendar = kegiatan.getCalSeharian();
                headerItem = "Lihat Kegiatan";
            } else {
                isView = false;
                headerItem = "Ubah Kegiatan";
            }
        } else {
            isView = false;
            kegiatan = new CPKalender();
            kegiatan.setCalSeharian(false);
            kegiatan.setCalMulai(new Date());
            kegiatan.setCalSampai(new Date());
            headerItem = "Kegiatan Baru";
        }
        RequestContext.getCurrentInstance().reset("idDtlKegiatan");
        RequestContext.getCurrentInstance().update("idDtlKegiatan");
        RequestContext.getCurrentInstance().reset("form-dtl-kegiatan");
        RequestContext.getCurrentInstance().update("form-dtl-kegiatan");
        RequestContext.getCurrentInstance().execute("PF('wgDtlKegiatan').show()");
    }

    public void saveKegiatan() {
        if (kegiatan.getCalId() != null) {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Kegiatan berhasil diubah");
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Kegiatan berhasil disimpan");
        }
        kegiatan.setDeleted(false);
        kegiatanRepo.save(kegiatan);
        RequestContext.getCurrentInstance().execute("PF('wgDtlKegiatan').hide()");
        RequestContext.getCurrentInstance().update("kegiatan-content");
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

    public void removeKegiatan() {
        kegiatan = (CPKalender) getRequestParam("listRow");
        kegiatan.setDeleted(true);
        kegiatanRepo.save(kegiatan);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Kegiatan berhasil dihapus");
    }

    public String timestampConvert(Date date) {
        return Tanggal.dateTimeStringConvert(date);
    }

    public void disabledCalendar() {
        disableCalendar = kegiatan.getCalSeharian();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }
}
