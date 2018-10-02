package prosia.basarnas.web.controller;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
//import com.sun.java.swing.plaf.windows.resources.windows;
import java.awt.RenderingHints;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ValueMapConstant;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.app.web.controller.ReportController;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.service.OfficeSarService;

@Component
@Scope("session")
public @Data
class DashboardIncidentMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ReportController reportController;

    @Autowired
    private OfficeSarService officeSarService;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

    private String idKansar;
    private String kansarinsiden;
    private MstKantorSar kanSar;
    private List<SelectItem> listKantorSar;
    private List<SelectItem> getListinsidenKantorSar;
    private String dashinsidentype;
    private Date insidenawal = date;
    private Date insidenahir = date;
    private Boolean showDlg;
    private Boolean disabled;
    private String dashincinsidentype;
    private StreamedContent streamedContent;
    private byte[] dataStream;

    public void reset() {

        insidenawal = date;
        insidenahir = date;
        dashinsidentype = "";
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

    public void generateDataInsiden() {
        try {
            if (insidenahir.before(insidenawal)) {
                addPopUpMessage("Pesan", "Tanggal awal periode harus sebelum atau sama dengan tanggal akhir periode");
            } else {
                streamedContent = new DefaultStreamedContent();
                DateFormat now = new SimpleDateFormat("yyyy-MM-dd");
                HashMap hm = new HashMap();
            //    String jrxml = "/report/report_jasper/Dashboard_GrafikInsiden.jrxml";
                String jrxml = File.separator+"report"+File.separator+"report_jasper"+File.separator+"Dashboard_GrafikInsiden.jrxml";
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
                hm.put("kansarinsiden", kansarinsiden);
                hm.put("dashinsidentype", dashinsidentype);
                hm.put("insidenawal", now.format(insidenawal).toString());
                hm.put("insidenahir", now.format(insidenahir).toString());
              
                Connection connection = dataSource.getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
                HttpServletResponse response = (HttpServletResponse) ext.getResponse();
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                InputStream in = new ByteArrayInputStream(bytes);
                streamedContent = new DefaultStreamedContent(in, "application/pdf");
            }
        } catch (JRException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public StreamedContent getStreamedContentPdfInsiden() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            return streamedContent;
        }
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        CreatePDF();
        kansarinsiden = "ALL";
        dashinsidentype = "0";
        insidenawal = new Date();
        insidenahir = new Date();
        generateDataInsiden();
    }
}
