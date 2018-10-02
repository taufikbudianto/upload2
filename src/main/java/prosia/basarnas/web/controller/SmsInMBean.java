/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.GtwSmsIn;
import prosia.basarnas.repo.GtwSmsInRepo;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
public @Data
class SmsInMBean implements InitializingBean {

    private String kolompencarian;
    private Map<String, String> listKolomPencarian;
    private LazyDataModelJPA<Object[]> lazyDataModelJPASms;
    private String textfield;
    private String queryand;

    @Autowired
    private GtwSmsInRepo gtwSmsInRepo;

    public SmsInMBean() {
        listKolomPencarian = new LinkedHashMap<String, String>();

        listKolomPencarian.put("Nama Pengirim", "nama");
        listKolomPencarian.put("No. HP", "nohp");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
        lazyDataModelJPASms = new LazyDataModelJPA(gtwSmsInRepo) {
            long pageNumber;
            @Override
            protected Page getDatas(PageRequest request) {
                if (kolompencarian != null) {
                    if (kolompencarian.equals("nama")) {
                        return gtwSmsInRepo.findAllViewNama(textfield.toUpperCase().trim(), request);
                    } else {
                        return gtwSmsInRepo.findAllViewNoHp(textfield.toUpperCase().trim(), request);
                    }
                } else {
                    
                    return gtwSmsInRepo.findAllViewAll(request);
                }
            }
            @Override
            protected long getDataSize(){
                 if (kolompencarian != null) {
                      if (kolompencarian.equals("nama")) {
                          return gtwSmsInRepo.countAllViewNama(textfield.toUpperCase().trim());
                      }else{
                          return gtwSmsInRepo.countAllViewNoHp(textfield.toUpperCase().trim());
                      }
                 }else{
                     return gtwSmsInRepo.countAllViewAll();
                 }
                
            }
            
        };

    }

    private Specification<GtwSmsIn> whereQuerySmsIn(
            final String field,
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<GtwSmsIn>() {

            @Override
            public Predicate toPredicate(Root<GtwSmsIn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {

                    predicates.add(cb.like(cb.lower(root.<String>get(field)),
                            getLikePattern(value.trim())));

                }
                query.orderBy(cb.asc(root.get("delivereddate")));
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

}
