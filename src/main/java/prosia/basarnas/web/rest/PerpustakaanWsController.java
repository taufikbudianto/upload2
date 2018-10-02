/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.model.User;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.dto.ResponseData;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.ResponseCode;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.CPPerpustakaan;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.PerpustakaanPersonilRepo;
import prosia.basarnas.repo.PerpustakkanRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.web.rest.dto.PerpustakaanWs;

/**
 *
 * @author Taufik
 */
@RestController
public class PerpustakaanWsController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<ResPersonnel> lazyDataModelJPA;
    @Autowired
    private PerpustakkanRepo perpusRepo;
    @Autowired
    private PerpustakaanPersonilRepo perpusPersonilRepo;
    @Autowired
    private ResPersonnelRepo personnelRepo;

    @RequestMapping(value = "/library", method = RequestMethod.GET)
    public ResponseEntity<?> getDataLibrary(@RequestParam Map<String, String> mapParam) {
        Integer limit = 10;
        Integer page = 1;
        List<Integer> listLibId;

        //=10&=2
        for (Map.Entry<String, String> map : mapParam.entrySet()) {
            if (map.getKey().equals("limit")) {
                limit = Integer.parseInt(map.getValue());
            }
            if (map.getKey().equals("start_from_id")) {
                page = Integer.parseInt(map.getValue());
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseData responseData = new ResponseData();
        try {
            User user = (User) authentication.getPrincipal();
            ResPersonnel resPersonel = personnelRepo.findByPersonnelCode(user.getUsername());
            listLibId = perpusPersonilRepo.getLibId(resPersonel.getPersonnelID());
            if (listLibId.isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setResponseMessage(BodyUtil.X_MESSAGE_SUCCESS);
            List<PerpustakaanWs> listPerpustakaan = new ArrayList<>();
            //List<CPPerpustakaan> listCpPerpus = perpusRepo.findAll(whereQuery(listLibId));
            Page<CPPerpustakaan> listCpPerpus = perpusRepo.findAll(whereQuery(listLibId), new PageRequest(page - 1, limit));
            //System.out.println("List : "+listCpPerpus2.getContent().size());
            //if (listCpPerpus.isEmpty()) {
            if (listCpPerpus.getContent().isEmpty()) {
                Response error = new Response();
                error.setResponseCode(ResponseCode.ZERO_RESULT);
                error.setResponseMessage(BodyUtil.X_MESSAGE_DATA_NULL);
                return new ResponseEntity<Response>(error, HttpStatus.NOT_FOUND);
            }
            //for (CPPerpustakaan perpus : listCpPerpus) {
            for (CPPerpustakaan perpus : listCpPerpus.getContent()) {
                PerpustakaanWs perpusWs = new PerpustakaanWs();
                perpusWs.setLibId(perpus.getLibId());
                perpusWs.setTitle(perpus.getLibJudul());
                perpusWs.setCreatedBy(perpus.getCreatedBy());
                perpusWs.setCreatedDate(String.valueOf(perpus.getCreatedDate()));
                perpusWs.setBookContent(perpus.getLibKonten());//download/{id}/{name}
                perpusWs.setLibAttachment(perpus.getLibAttachment());
                perpusWs.setUrl_image_lib("http://apps.prosia.co.id:8072/sarcore/document/library/"+String.valueOf(perpus.getLibId()));
                if(perpus.getLibAttachment()!=null){
                    perpusWs.setDownloadfile("http://apps.prosia.co.id:8072/sarcore/download/"+String.valueOf(perpus.getLibId())+"?name="+perpus.getLibAttachment());
                }
                listPerpustakaan.add(perpusWs);
            }
            responseData.setData(listPerpustakaan);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            Response error = new Response();
            error.setResponseCode(ResponseCode.GENERAL_ERROR);
            error.setResponseMessage(e.getMessage());
            log.error(e.getMessage());
            return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/download/{id}", produces = MediaType.ALL_VALUE)
    public @ResponseBody
    HttpEntity<byte[]> getFile(
            @PathVariable("id") Integer id,@RequestParam("name") String name) throws IOException {
        String imgPath = "C:\\\\Basarnas\\\\upload_potensi\\\\LibUploaded\\\\"+name;
        File files = new File(imgPath);
        HttpHeaders header = new HttpHeaders();
        byte[] file = new byte[(int) files.length()];
        header.set("Content-Disposition", "attachment; filename="+name);
        header.setContentLength(file.length);

        return new HttpEntity<byte[]>(file, header);
    }
   
    private Specification<CPPerpustakaan> whereQuery(List<Integer> list) {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<CPPerpustakaan>() {
            @Override
            public Predicate toPredicate(Root<CPPerpustakaan> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Expression<Integer> exp = root.get("libId");//deleted
                predicates.add(cb.notEqual(root.<Boolean>get("deleted"), true));
                predicates.add(exp.in(list));
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

}