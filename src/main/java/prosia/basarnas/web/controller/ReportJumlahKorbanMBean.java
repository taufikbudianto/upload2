package prosia.basarnas.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.time.Instant.now;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
class ReportJumlahKorbanMBean extends AbstractManagedBean {

    private List<SelectItem> listKantorSar;

    private String KATEGORI;
    private String idKansar;
    private String bulan1;
    private String bulan2;
    private String pilihperiode;
    private String pilihperiode2;
    private String thn1;
    private String clear;
    private String thn2;
    private String KANTORSARNAME;
    private String INCIDENTTYPE;
    private String field;
    private String ENDDATE;
    private String STARTDATE;
    private String currentId;
    private Boolean disabled;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private OfficeSarService officeSarService;
    //private String reportPrint;
    private MstKantorSar kanSar;

    public ReportJumlahKorbanMBean() {
        try {
            currentId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
            bulan1 = "Januari";
            bulan2 = "Januari";
            field = "Semua";
            KANTORSARNAME = "Semua";
            thn1 = "2017";
            thn2 = "2017";
        } catch (Exception e) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan", "User tidak memiliki wilayah Kantor SAR!");
            log.error("Wilayah Kantor SAR User: " + currentId);
            e.printStackTrace();
        }
    }

    public void reset() {
        KANTORSARNAME = "Semua";
        bulan1 = "Januari";
        bulan2 = "Januari";
        thn1 = "2017";
        thn2 = "2017";
        field = "Semua";
        INCIDENTTYPE = "Semua";
    }

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

    private String EnddateConversion(String ENDDATE) throws ParseException {
        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

        if (ENDDATE.equals("01")) {
            String tglakhir = "January 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("02")) {
            String tglakhir = "February 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("03")) {
            String tglakhir = "March 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("04")) {
            String tglakhir = "April 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("05")) {
            String tglakhir = "May 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("06")) {
            String tglakhir = "June 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("07")) {
            String tglakhir = "July 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("08")) {
            String tglakhir = "August 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("09")) {
            String tglakhir = "September 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("10")) {
            String tglakhir = "October 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("11")) {
            String tglakhir = "November 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        } else if (ENDDATE.equals("12")) {
            String tglakhir = "December 01, " + thn2;
            Date convertedDate = format.parse(tglakhir);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            Integer tglpalingakhir = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            ENDDATE = thn2 + "-" + bulan2 + "-" + tglpalingakhir.toString();
        }
        return ENDDATE;
    }

    public void openPrintDialog() throws ParseException {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("tahun skrg :" + year);
//validasi
        if (thn1 == null) {
            STARTDATE = year + "-" + bulan1 + "-" + "01";
        } else {
            STARTDATE = thn1 + "-" + bulan1 + "-" + "01";
        }

        ENDDATE = EnddateConversion(bulan2);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date STARTDATE1 = format.parse(STARTDATE);
        Date ENDDATE1 = format.parse(ENDDATE);

        if (ENDDATE1.before(STARTDATE1)) {
            addPopUpMessage("Pesan", "Tanggal awal periode harus sebelum atau sama dengan tanggal akhir periode");
            return;
        } else {
            RequestContext.getCurrentInstance().execute("PF('dialogPrint').show()");
        }
    }

    public void reportPrint() throws ParseException {
        try {
            String STARTDATE = thn1 + "-" + bulan1 + "-" + "01";
            String ENDDATE = EnddateConversion(bulan2);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date STARTDATE1 = format.parse(STARTDATE);
            Date ENDDATE1 = format.parse(ENDDATE);

            DateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String user = getCurrentUser().getParty().getFullName();
            HashMap hm = new HashMap();
//            String jrxml = "/report/report_jasper/Report_jum_korban.jrxml";
            String jrxml = File.separator + "report" + File.separator + "report_jasper" + File.separator + "Report_jum_korban.jrxml";
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
            hm.put("KATEGORI", KATEGORI);
            hm.put("STARTDATE", STARTDATE);
            hm.put("ENDDATE", ENDDATE);
            hm.put("INCIDENTTYPE", INCIDENTTYPE);
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
