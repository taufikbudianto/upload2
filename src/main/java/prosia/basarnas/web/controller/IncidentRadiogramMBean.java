/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.util.Date;
import java.util.TimeZone;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import prosia.app.web.util.LazyDataModelJPA;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentRadiogram;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.IncidentRadiogramRepo;

/**
 *
 * @author Aris
 */
@Controller
@Scope("view")
public @Data 
    class IncidentRadiogramMBean implements InitializingBean{
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LazyDataModelJPA<IncidentRadiogram> dataModel;
    private Boolean showButton;
    private Boolean disabled;
    
    private IncidentLog incidentLog;
    private IncidentRadiogram incidentRadiogram;
    private IncidentRadiogram referensi;
    private ResPersonnel nama;
    
    private String radiogramID;
    private boolean kirim=false;
    private String nasionalformat;
    private String panggilan;
    private String jenis;
    private String stn;
    private String derajat;
    private String instruksi;
    private Date datePenunjukan;
    private TimeZone datePenunjukanTimeZone;
    private Date datePembuatan;
    private TimeZone datePembuatanTimeZone;
    private String prefixNomor;
    private String jumlahKata;
    private String dari;
    private String aksi;
    private String info;
    private String klasifikasi;
    private String nomor;
    private String pengirim;
    private String pangkat;
    private String jabatan;
    private Date dateTwt;
    private TimeZone dateTwtTimeZone;
    private Date dateTwk;
    private TimeZone dateTwkTimeZone;
    private String referensiNotes;
    private Boolean isDisabled;
    
    @Autowired
    private IncidentRadiogramRepo incidentRadiogramRepo;
    
    public IncidentRadiogramMBean(){
        this.incidentRadiogram = new IncidentRadiogram();
        this.referensi = new IncidentRadiogram();
        this.nama = new ResPersonnel();
        nasionalformat = "0";
        radiogramID = null;
        kirim = false;
        panggilan = null;
        jenis = null;
        stn = null;
        derajat = null;
        instruksi = null;
        datePenunjukan = new Date();
        datePenunjukanTimeZone = null;
        datePembuatan = new Date();
        datePembuatanTimeZone = null;
        prefixNomor = null;
        jumlahKata = null;
        dari = null;
        aksi = null;
        info = null;
        klasifikasi = null;
        nomor = null;
        pengirim = null;
        pangkat = null;
        jabatan = null;
        dateTwt = new Date();
        dateTwtTimeZone = null;
        dateTwk = new Date();
        dateTwkTimeZone = null;
        referensiNotes = null;
        isDisabled =true;
    }
    
    public void changeNasional() {
        System.out.println("sss");
        if ("0".equals(nasionalformat)) {
            isDisabled = true;
        } else {
            isDisabled = false;
        }
        System.out.println("Nasional format" + nasionalformat);
        System.out.println("isDisable : " + isDisabled);
    }
    
    public void setData() {
        dataModel = new LazyDataModelJPA(incidentRadiogramRepo) {
            @Override
            protected long getDataSize() {
                return incidentRadiogramRepo.count();
            }

            @Override
            protected Page getDatas(PageRequest request) {
                return incidentRadiogramRepo.findAll(request);
            }
           
        };
    }
    
    public void viewIncidentRadiogram(String incidentRadiogramId, Boolean edited) {
        this.incidentRadiogram = incidentRadiogramRepo.findOne(incidentRadiogramId);
        radiogramID = incidentRadiogram.getRadiogramID();
        kirim = incidentRadiogram.getKirim();
        panggilan = incidentRadiogram.getPanggilan();
        jenis = incidentRadiogram.getJenis();
        stn = incidentRadiogram.getStn();
        derajat = incidentRadiogram.getDerajat();
        instruksi = incidentRadiogram.getInstruksi();
        datePenunjukan = incidentRadiogram.getDatePenunjukan();
        datePenunjukanTimeZone = incidentRadiogram.getDatePenunjukanTimeZone();
        datePembuatan = incidentRadiogram.getDatePembuatan();
        datePembuatanTimeZone = incidentRadiogram.getDatePembuatanTimeZone();
        prefixNomor = incidentRadiogram.getPrefixNomor();
        jumlahKata = incidentRadiogram.getJumlahKata();
        dari = incidentRadiogram.getDari();
        aksi = incidentRadiogram.getAksi();
        info = incidentRadiogram.getInfo();
        klasifikasi = incidentRadiogram.getKlasifikasi();
        nomor = incidentRadiogram.getNomor();
        pengirim = incidentRadiogram.getPengirim();
        pangkat = incidentRadiogram.getPangkat();
        jabatan = incidentRadiogram.getJabatan();
        dateTwt = incidentRadiogram.getDateTwt();
        dateTwtTimeZone = incidentRadiogram.getDateTwtTimeZone();
        dateTwk = incidentRadiogram.getDateTwk();
        dateTwkTimeZone = incidentRadiogram.getDateTwkTimeZone();
        referensiNotes = incidentRadiogram.getReferensiNotes();
        
        disabled = edited;
        showButton = !edited;
        
        RequestContext.getCurrentInstance().execute("PF('widgetIncidentRadiogram').show()");
    }
    
    public void simpan() {
        
    }
    public void hapusRadiogram(IncidentRadiogram i) {
        logger.debug("Insiden : {}", i);
        /*
        i.setIsdeleted(BigInteger.ONE);
        incidentRepo.save(i);
        addMessage("Sukses", "Insiden berhasil dihapus");
*/
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        setData();
    }
}
