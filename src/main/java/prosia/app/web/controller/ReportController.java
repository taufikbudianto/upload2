/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Owner
 */
@Controller
@Scope("session")
public class ReportController implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Getter
    @Setter
    private HashMap params;

    @Autowired
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        params = new HashMap();
    }

    /**
     * @param value
     * @return hashmap of the string parameter.
     */
    private HashMap convertStringToHashMap(String value) {
        HashMap map = new HashMap();

        if (value != null && !value.isEmpty()) {
            value = value.substring(1, value.length() - 1);
            String[] keyValuePairs = value.split(",");

            for (String pair : keyValuePairs) {
                String[] entry = pair.split("=");
                map.put(entry[0].trim(), entry[1].trim());
            }
        }
        return map;
    }

    /**
     * Generate report and show in current page.
     *
     * @param paramStr
     * @param path
     */
    public void showReport(String paramStr, String paramStr1, String path) {
        try {
            if (paramStr != null && !"".equals(paramStr)) {
                params = convertStringToHashMap(paramStr);
            }
            if (paramStr1 != null && !"".equals(paramStr1)) {
                params.putAll(convertStringToHashMap(paramStr1));
            }
            System.out.println(params.toString());
            params.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));

            log.debug("show report : {} {}", params, path);

            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();

            String pathJrxml = request.getRealPath(path);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");

            File fileJrxml = new File(pathJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, params, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);

            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();

            facescontext.responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Error", "Gagal menampilkan file.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

}
