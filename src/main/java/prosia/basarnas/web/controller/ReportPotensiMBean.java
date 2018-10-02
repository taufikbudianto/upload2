package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import lombok.Cleanup;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
/*
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
 */
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.service.OfficeSarService;

@Component
@Scope("view")

public @Data
class ReportPotensiMBean extends AbstractManagedBean implements InitializingBean {

    private List<SelectItem> listKantorSar;

    private String idKansar;
    private String KANTORSARNAME;
    private String currentId;
    private Boolean disabled;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private OfficeSarService officeSarService;
    //private String reportPrint;
    private MstKantorSar kanSar;

    public void afterPropertiesSet() throws Exception {
        try {
            currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan", "User tidak memiliki wilayah Kantor SAR!");
            log.error("Wilayah Kantor SAR User: " + currentId);
            e.printStackTrace();
        }
    }

    public List<SelectItem> getListKantorSar() {
        try {
            kanSar = getCurrentUser().getResPersonnel().getOfficeSar();
            if (listKantorSar == null) {
                listKantorSar = new ArrayList<>();
                if (kanSar.getKantorsarid().equals("BSN")) {
                    listKantorSar.add(new SelectItem("ALL", "SEMUA"));
                    for (MstKantorSar kansar : officeSarService.getOfficeSar()) {
                        listKantorSar.add(new SelectItem(kansar.getKantorsarname(), kansar.getKantorsarname()));
                    }
                } else {
                    listKantorSar.add(new SelectItem(kanSar.getKantorsarname(), kanSar.getKantorsarname()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listKantorSar;
    }
//  

    public void reportPrint() {
        try {
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String user = getCurrentUser().getParty().getFullName();
            HashMap hm = new HashMap();
            //String jrxml = "/report/report_jasper/Report_Potency.jrxml";
            String jrxml = File.separator + "report" + File.separator + "report_jasper" + File.separator + "Report_Potency.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");

            File fileJrxml = new File(pathJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }
//parameter atau field yang akan dijadikan filter
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("KANTORSARNAME", KANTORSARNAME);
            hm.put("user", user);
            hm.put("currentDate", now.format(new Date()));

            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"CetakPresensi.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();

            facescontext.responseComplete();
        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

}
