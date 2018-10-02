/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Randy
 */
@Controller
@Scope("request")
public class DocumentController implements ApplicationContextAware {
    
    protected Logger log = LoggerFactory.getLogger(getClass());
    
    @Value("${uploadFolder}")
    private String rootPath;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @RequestMapping(value = "/temp/{type}/{uuid}/{docName:.+}", 
            method = RequestMethod.GET)
    public @ResponseBody FileSystemResource retrieveDocument(@PathVariable String type, 
            @PathVariable String uuid, @PathVariable String docName) throws IOException {
        
        Resource document = this.applicationContext.getResource(
                "file:/" + this.rootPath + type + "/" + uuid + "/" + docName);
        
        if (!document.getFile().exists()) {
            log.info("File not found : {}", document.getURL());
        }
        
        return new FileSystemResource(document.getFile());
    }
    
}
