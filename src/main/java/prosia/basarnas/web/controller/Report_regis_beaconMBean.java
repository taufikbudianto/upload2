package prosia.basarnas.web.controller;

import com.sun.java.swing.plaf.windows.resources.windows;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.Driver;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import lombok.Cleanup;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import static org.primefaces.behavior.ajax.AjaxBehavior.PropertyKeys.form;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ValueMapConstant;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.app.web.controller.ReportController;

@Component
@Scope("view")
public @Data
class Report_regis_beaconMBean extends AbstractManagedBean {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ReportController reportController;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

    private String field;
    private Date startdate = date;
    private Date enddate = date;
    private Boolean showDlg;

    public void reset() {

        startdate = date;
        enddate = date;
        field = "";
    }

    public void openPrintDialog() {
        //validasi
        if (startdate == null) {
            addPopUpMessage("Pesan", "Tanggal awal periode harus diisi");
            return;
        } else if (enddate == null) {
            addPopUpMessage("Pesan", "Tanggal akhir periode harus diisi");
            return;
        } else if (enddate.before(startdate)) {
            addPopUpMessage("Pesan", "Tanggal awal periode harus sebelum atau sama dengan tanggal akhir periode");
            return;
        } else {
            RequestContext.getCurrentInstance().execute("PF('dialogPrint').show()");

        }
    }

    //RequestContext.getCurrentInstance().execute("PF('widgetPrint').show()");
    public void reportPrint() {
        try {
            String user = getCurrentUser().getParty().getFullName();
            DateFormat now = new SimpleDateFormat("dd-MM-yyyy");
            HashMap hm = new HashMap();
//            String jrxml = "/report/report_jasper/Report_registrasi_beacon.jrxml";
            String jrxml = File.separator + "report" + File.separator + "report_jasper"+File.separator+"Report_registrasi_beacon.jrxml";
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

            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            hm.put("BEACONTYPE", field);
            hm.put("startdate", now.format(startdate).toString());
            hm.put("enddate", now.format(enddate).toString());
            hm.put("user", user);

            //parameter atau field yang akan dijadikan filter
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
