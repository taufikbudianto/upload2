package prosia.basarnas.web.controller;
//package org.primefaces.showcase.view.overlay;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import lombok.Data;
 
import org.primefaces.event.CloseEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
 
@ManagedBean

@Component
@Scope("view")
public @Data class DialogViewMBean {

    
    public void destroyWorld() {
        addMessage("System Error", "Please try again later.");
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}